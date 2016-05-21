package simit.org.jobbole.parser;

import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import simit.org.jobbole.bean.RSSFeed;
import simit.org.jobbole.bean.RSSItem;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.utility.JobUtil;

/**
 * Created by liuchun on 2016/3/29.
 */
public class RSSFeedPULLParser implements IRSSParser {
    @Override
    public RSSFeed parse(InputStream is) throws Exception {
        RSSFeed rssFeed = new RSSFeed();
        RSSItem rssItem = new RSSItem();
        List<RSSItem> rssItems = new ArrayList<>();
        List<String> category = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new InputStreamReader(is));
        int eventType = xpp.getEventType();
        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_DOCUMENT){
                Log.d("PULLParser", "START_DOCUMENT");
            }else if(eventType == XmlPullParser.START_TAG){
                Log.d("PULLParser", "START_TAG");
                if(xpp.getName().equals("channel")){
                    rssFeed = new RSSFeed();
                    rssItems = new ArrayList<>();
                }else if(xpp.getName().equals("item")){
                    rssItem = new RSSItem();
                    category = new ArrayList<>();
                }else if(xpp.getName().equals("title")){
                    xpp.next();
                    if(xpp.getDepth() == 3){
                        // this is feed title
                        rssFeed.setTitle(xpp.getText());
                    }else if(xpp.getDepth() == 4){
                        rssItem.setTitle(xpp.getText().trim());
                    }
                }else if(xpp.getName().equals("link")){
                    xpp.next();
                    if(xpp.getDepth() == 3){
                        rssFeed.setLink(xpp.getText());
                    }else if(xpp.getDepth() == 4){
                        rssItem.setLink(xpp.getText());
                    }
                }else if(xpp.getName().equals("description")){
                    xpp.next();
                    if(xpp.getDepth() == 3){
                        rssFeed.setDescription(xpp.getText());
                    }else if(xpp.getDepth() == 4){
                        /** TODO remove the tailer in the end, for example <p>The post *** </p> */
                        String description = xpp.getText().replaceAll(JobboleConstants.DESCRIPTION_PATTERN, "").trim();
                        description = description.replaceAll(JobboleConstants.DESCRIPTION_BLOG_PATTERN, "").trim();
                        rssItem.setDescription(description);
                    }
                }else if(xpp.getName().equals("comments")){
                    xpp.next();
                    rssItem.setComments_link(xpp.getText());
                }else if(xpp.getName().equals("pubDate")){
                    xpp.next();
                    /** TODO format the pubDate to system Time Zone */
                    String pubDate = JobUtil.toDisplayTime(xpp.getText());
                    rssItem.setPubDate(pubDate);
                }else if(xpp.getName().equals("dc:creator")){
                    xpp.next();
                    rssItem.setAuthor(xpp.getText());
                }else if(xpp.getName().equals("category")){
                    xpp.next();
                    category.add(xpp.getText());
                }else if(xpp.getName().equals("content:encoded")){
                    xpp.next();
                    rssItem.setContent(xpp.getText());
                }
            }else if(eventType == XmlPullParser.TEXT){
                Log.d("PULLParser", "TEXT");
            }else if(eventType == XmlPullParser.END_TAG){
                Log.d("PULLParser", "END_TAG");
                if(xpp.getName().equals("channel")){
                    rssFeed.setRssItems(rssItems);
                }else if(xpp.getName().equals("item")){
                    rssItem.setCategory(category);
                    rssItems.add(rssItem);
                }
            }
            //
            eventType = xpp.next();
        }

        return rssFeed;
    }

    @Override
    public String serialize(RSSFeed rssFeed) throws Exception {
        return rssFeed.toString();
    }
}
