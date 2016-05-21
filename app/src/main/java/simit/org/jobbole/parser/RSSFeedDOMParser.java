package simit.org.jobbole.parser;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import simit.org.jobbole.bean.RSSFeed;
import simit.org.jobbole.bean.RSSItem;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.utility.JobUtil;

/**
 * Created by liuchun on 2016/3/29.
 */
public class RSSFeedDOMParser implements IRSSParser {
    @Override
    public RSSFeed parse(InputStream is) throws Exception {
        RSSFeed rssFeed;
        RSSItem rssItem;
        List<RSSItem> rssItems;
        List<String> category;
        //
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(is);
        //
        Element root = document.getDocumentElement();
        NodeList channelNodes = root.getElementsByTagName("channel");
        if(channelNodes.getLength() < 1){
            return null;
        }
        rssFeed = new RSSFeed();
        rssItems = new ArrayList<>();
        NodeList childNodes = channelNodes.item(0).getChildNodes();
        // loop for the channel item lists
        for(int i = 0; i < childNodes.getLength(); i++){
            Node child = childNodes.item(i);
            if(child.getNodeName().equals("title")){
                rssFeed.setTitle(child.getFirstChild().getTextContent());
            }else if(child.getNodeName().equals("link")){
                rssFeed.setLink(child.getFirstChild().getTextContent());
            }else if(child.getNodeName().equals("description")){
                rssFeed.setDescription(child.getFirstChild().getTextContent());
            }else if(child.getNodeName().equals("item")){
                rssItem = new RSSItem();
                category = new ArrayList<>();
                NodeList item = child.getChildNodes();
                // loop for the item detail
                for(int j = 0; j < item.getLength(); j++) {
                    Node itemChild = item.item(j);
                    if (itemChild.getNodeName().equals("title")) {
                        String title = itemChild.getFirstChild().getTextContent().trim();
                        rssItem.setTitle(title);
                    } else if (itemChild.getNodeName().equals("link")) {
                        rssItem.setLink(itemChild.getFirstChild().getTextContent());
                    } else if (itemChild.getNodeName().equals("description")) {
                        /** TODO remove the tailer in the end, for example <p>The post *** </p> */
                        String description = itemChild.getFirstChild().getTextContent();
                        description = description.replaceAll(JobboleConstants.DESCRIPTION_PATTERN, "").trim();
                        rssItem.setDescription(description);
                    } else if (itemChild.getNodeName().equals("comments")) {
                        rssItem.setComments_link(itemChild.getFirstChild().getTextContent());
                    } else if (itemChild.getNodeName().equals("pubDate")) {
                        /** TODO format the pubDate to correct Time Zone */
                        String pubDate = itemChild.getFirstChild().getTextContent();
                        pubDate = JobUtil.toDisplayTime(pubDate);
                        rssItem.setPubDate(pubDate);
                    } else if (itemChild.getNodeName().equals("dc:creator")) {
                        rssItem.setAuthor(itemChild.getFirstChild().getTextContent());
                    } else if (itemChild.getNodeName().equals("category")) {
                        category.add(itemChild.getFirstChild().getTextContent());
                    } else if (itemChild.getNodeName().equals("content:encoded")) {
                        rssItem.setContent(itemChild.getFirstChild().getTextContent());
                    }
                }
                rssItem.setCategory(category);
                rssItems.add(rssItem);
            }
        }
        //
        rssFeed.setRssItems(rssItems);

        return rssFeed;
    }

    @Override
    public String serialize(RSSFeed feed) throws Exception {
        return null;
    }
}
