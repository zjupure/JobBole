package simit.org.jobbole.bean;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by liuchun on 2016/3/27.
 */
public class RSSFeed {
    // channel title
    private String title;
    // channel link
    private String link;
    // channel description
    private String description;
    // channel items
    private List<RSSItem> rssItems;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RSSItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(List<RSSItem> rssItems) {
        this.rssItems = rssItems;
    }
}
