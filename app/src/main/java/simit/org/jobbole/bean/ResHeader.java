package simit.org.jobbole.bean;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuchun on 2016/4/17.
 */
public class ResHeader implements ParentListItem {
    // title
    private String title;
    // icon link
    private String iconLink;
    // link
    private String link;
    // description
    private String description;
    // items
    //private ResItemWrapper itemWrapper = new ResItemWrapper();
    //
    private List<ResItemWrapper> itemWrappers;

    public ResHeader(){
        itemWrappers = new ArrayList<>();
        itemWrappers.add(new ResItemWrapper());
    }

    @Override
    public List<?> getChildItemList() {

        return itemWrappers;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
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

    public ResItemWrapper getItemWrapper() {
        return itemWrappers.get(0);
    }

    public static class ResItemWrapper{
        List<ResItem> itemList = new ArrayList<>();

        public List<ResItem> getItemList(){
            return itemList;
        }
    }
}
