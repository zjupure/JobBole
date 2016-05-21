package simit.org.jobbole.parser;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import simit.org.jobbole.bean.RSSFeed;
import simit.org.jobbole.bean.RSSItem;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.utility.JobUtil;

/**
 * Created by liuchun on 2016/3/29.
 */
public class RSSFeedSAXParser implements IRSSParser {

    @Override
    public RSSFeed parse(InputStream is) throws Exception {
        //
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        RSSFeedSAXHandler handler = new RSSFeedSAXHandler();
        parser.parse(is, handler);

        return handler.getRssFeed();
    }

    @Override
    public String serialize(RSSFeed feed) throws Exception {
        return null;
    }

    /** SAXHandler for the RSSFeed Parser */
    public class RSSFeedSAXHandler extends DefaultHandler{
        private RSSFeed rssFeed = null;
        private RSSItem rssItem = null;
        private List<RSSItem> rssItems = null;
        private List<String> category = null;
        private String content = null;
        private int depth = 0;

        public RSSFeed getRssFeed(){
            return rssFeed;
        }

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            Log.d("SAXParser", "START DOCUMENT");
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            //
            if(localName.equals("channel")){
                rssFeed = new RSSFeed();
                rssItems = new ArrayList<>();
            }else if(localName.equals("item")){
                rssItem = new RSSItem();
                category = new ArrayList<>();
            }
            //
            depth++;
            Log.d("SAXParser", "START ELEMENT");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            content = new String(ch, start, length);
            Log.d("SAXParser", "CHARACTERS");
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            //
            if(localName.equals("channel")){
                rssFeed.setRssItems(rssItems);
            }else if(localName.equals("item")){
                rssItem.setCategory(category);
                rssItems.add(rssItem);
            }else if(localName.equals("title")){
                if(depth == 3){
                    rssFeed.setTitle(content);
                }else if(depth == 4){
                    rssItem.setTitle(content.trim());
                }
            }else if(localName.equals("link")){
                if(depth == 3){
                    rssFeed.setLink(content);
                }else if(depth == 4){
                    rssItem.setLink(content);
                }
            }else if(localName.equals("description")){
                if(depth == 3){
                    rssFeed.setDescription(content);
                }else if(depth == 4){
                    /** TODO remove the tailer in the end, for example <p>The post *** </p> */
                    String description = content.replaceAll(JobboleConstants.DESCRIPTION_PATTERN, "").trim();
                    rssItem.setDescription(description);
                }
            }else if(localName.equals("comments")){
                rssItem.setComments_link(content);
            }else if(localName.equals("pubDate")){
                /** TODO format the pubDate to correct Time Zone */
                String pubDate = JobUtil.toDisplayTime(content);
                rssItem.setPubDate(pubDate);
            }else if(localName.equals("dc:creator")){
                rssItem.setAuthor(content);
            }else if(localName.equals("category")){
                category.add(content);
            }else if(localName.equals("content:encoded")){
                rssItem.setContent(content);
            }
            //
            depth--;
            Log.d("SAXParser", "END ELEMENTS");
        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            Log.d("SAXParser", "END DOCUMENT");
        }
    }







}
