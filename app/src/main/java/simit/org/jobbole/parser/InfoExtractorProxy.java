package simit.org.jobbole.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import simit.org.jobbole.bean.BlogItem;
import simit.org.jobbole.bean.ResHeader;
import simit.org.jobbole.bean.ResItem;
import simit.org.jobbole.config.JobboleConstants;

/**
 * Created by liuchun on 2016/4/16.
 */
public class InfoExtractorProxy {
    /** BLOG **/
    public static final String BODY_RULE = "body";
    public static final String SPEC_RULE = "div.p-single";  // hot-topic & group & date
    public static final String RESOURCE_RULE = "article.rpost-entry"; // resource
    public static final String GENERAL_RULE = "div.grid-8"; // others
    public static final String BODY_SCRIPT_RULE = "body > script";
    /** RESOURCE **/
    public static final String RES_DIV_LIST_RULE = "div.list-rs";
    public static final String RES_DIV_SET_RULE = "div.lr-box.lr-box-nav";
    public static final String RES_ICON_RULE = "img.cat-thumbnail";
    public static final String RES_TITLE_RULE = "h2";
    public static final String RES_ITEM_RULE = "li.top-sub-category";
    public static final String RES_SUB_ITEM_RULE = "li.top-resource";
    /** RESOUCE ITEM **/
    public static final String RES_ITEM_DETAIL_RULE = "li.res-item";
    public static final String RES_ITEM_ICON_RULE = "img";
    public static final String RES_ITEM_TITLE_RULE = "h3";
    public static final String RES_ITEM_DESP_RULE = "p";
    /** DATE ITEM */
    public static final String DATE_ITEM_RULE = "li.media";
    public static final String DATE_ITEM_TITLE = "div.media-body h3";
    public static final String DATE_ITEM_DESP = "div.media-body p";

    /** extarct validate information from html source code */
    public static String extractBlog(String html, int channel){
        // Jsoup parse the DOM
        Document doc = Jsoup.parse(html);
        Element body = doc.select(BODY_RULE).first();
        Element div;

        if(channel == JobboleConstants.HOT_TOPIC || channel == JobboleConstants.GROUP ||
                channel == JobboleConstants.DATE){
            div = doc.select(SPEC_RULE).first();
        }else if(channel == JobboleConstants.RESOURCE || channel == JobboleConstants.SUB_SUB_RES_CHANNEL){
            div = doc.select(RESOURCE_RULE).first();
        }else {
            div = doc.select(GENERAL_RULE).first();
        }
        StringBuilder builder = new StringBuilder(div.outerHtml());
        Elements scripts = doc.select(BODY_SCRIPT_RULE);
        for(Element s : scripts){
            builder.append(s.outerHtml());
        }
        // replace the html in body to div content
        body.html(builder.toString());

        return doc.outerHtml();
    }

    /** 从资源页面提取信息 **/
    public static List<ResHeader> extractRes(String html){
        List<ResHeader> resHeaders = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Element div_rs = doc.select(RES_DIV_LIST_RULE).first();
        Elements elements = div_rs.select(RES_DIV_SET_RULE);
        for(Element element : elements){
            /** 遍历所有的资源 */
            ResHeader header = new ResHeader();
            Element icon = element.select(RES_ICON_RULE).first();
            String icon_link = "";  // get icon link
            if(icon != null){
                icon_link = icon.attr("src");
            }

            Element h2 = element.select(RES_TITLE_RULE).first();
            String title = h2.text();
            Element title_link = h2.select("a").first();
            String link = title_link.attr("href");
            //
            header.setTitle(title);
            header.setIconLink(icon_link);
            header.setLink(link);
            //
            ResHeader.ResItemWrapper wrapper = header.getItemWrapper();
            List<ResItem> items = wrapper.getItemList();
            //
            Elements item_elements = element.select(RES_ITEM_RULE);
            for(Element item_element: item_elements){
                String item_title = item_element.text();
                Element item_link_elem = item_element.select("a").first();
                String item_link = item_link_elem.attr("href");
                //
                ResItem resItem = new ResItem();
                resItem.setGroup(title);
                resItem.setTitle(item_title);
                resItem.setLink(item_link);
                resItem.setDescription("");
                items.add(resItem);
            }

            Elements sub_item_elements = element.select(RES_SUB_ITEM_RULE);
            for(Element sub_item_element : sub_item_elements){
                Element sub_title = sub_item_element.select("h3").first();
                String sub_item_title = sub_title.text();
                Element sub_title_link = sub_title.select("a").first();
                String sub_item_link = sub_title_link.attr("href");
                Element sub_desp = sub_item_element.select("p").first();
                String sub_item_desp = sub_desp.text();
                //
                ResItem resItem = new ResItem();
                resItem.setGroup(title);
                resItem.setTitle(sub_item_title);
                resItem.setLink(sub_item_link);
                resItem.setDescription(sub_item_desp);
                items.add(resItem);
            }
            // add to the resHeaders
            resHeaders.add(header);
        }

        return resHeaders;
    }

    /** 提取资源列表细节 */
    public static List<BlogItem> extractResInfos(String html){
        List<BlogItem> blogItems = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(RES_ITEM_DETAIL_RULE);
        for(Element element : elements){
            /** 遍历所有的资源 */
            BlogItem item = new BlogItem();
            Element icon = element.select(RES_ITEM_ICON_RULE).first();
            String iconLink = icon.attr("src");

            Element h3 = element.select(RES_ITEM_TITLE_RULE).first();
            String title = h3.text();
            Element title_link = h3.select("a").first();
            String link = title_link.attr("href");

            Element p = element.select(RES_ITEM_DESP_RULE).first();
            String desp = p.text();
            // remove 详细介绍
            int index = desp.indexOf("详细介绍");
            if(index > 0){
                desp = desp.substring(0, index);
            }
            //
            item.setTitle(title);
            item.setDescription(desp);
            item.setLink(link);
            item.setIconLink(iconLink);
            //
            blogItems.add(item);
        }

        return blogItems;
    }

    /** 提取相亲页面 */
    public static List<BlogItem> extractDateInfos(String html){
        List<BlogItem> blogItems = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(DATE_ITEM_RULE);
        for(Element element : elements){
            /** 遍历所有的资源 */
            BlogItem item = new BlogItem();
            Element h3 = element.select(DATE_ITEM_TITLE).first();
            String title = h3.text();
            if(title.equals("觉得积分很难拿？除了签到，推荐一个更好的方式~")){
                continue; // this is advertisement
            }

            Element title_link = h3.select("a").first();
            String link = title_link.attr("href");
            Element p = element.select(DATE_ITEM_DESP).first();
            String desp = p.text();

            item.setTitle(title);
            item.setLink(link);
            item.setDescription(desp);
            //
            blogItems.add(item);
        }

        return blogItems;
    }
}
