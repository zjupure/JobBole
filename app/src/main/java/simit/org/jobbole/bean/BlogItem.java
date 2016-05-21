package simit.org.jobbole.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by liuchun on 2016/4/16.
 */
public class BlogItem implements Parcelable {
    // associated channel
    private int channel;
    // title
    private String title;
    // link
    private String link;
    // description
    private String description;
    // pubdate
    private String pubDate;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.channel);
        dest.writeString(this.title);
        dest.writeString(this.link);
        dest.writeString(this.description);
        dest.writeString(this.pubDate);
    }

    public BlogItem() {
    }

    protected BlogItem(Parcel in) {
        this.channel = in.readInt();
        this.title = in.readString();
        this.link = in.readString();
        this.description = in.readString();
        this.pubDate = in.readString();
    }

    public static final Parcelable.Creator<BlogItem> CREATOR = new Parcelable.Creator<BlogItem>() {
        @Override
        public BlogItem createFromParcel(Parcel source) {
            return new BlogItem(source);
        }

        @Override
        public BlogItem[] newArray(int size) {
            return new BlogItem[size];
        }
    };
}
