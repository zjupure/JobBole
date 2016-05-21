package simit.org.jobbole.bean;

import java.util.List;

/**
 * Created by liuchun on 2016/3/27.
 */
public class RSSItem {
    // item channel group
    private int channel;
    // item title
    private String title;
    // item link
    private String link;
    // item comments link
    private String comments_link;
    // item pubDate
    private String pubDate;
    // item author
    private String author;
    // item catagory
    private List<String> category;
    // item description
    private String description;
    // item content
    private String content;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

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

    public String getComments_link() {
        return comments_link;
    }

    public void setComments_link(String comments_link) {
        this.comments_link = comments_link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
