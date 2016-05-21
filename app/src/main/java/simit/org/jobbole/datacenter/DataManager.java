package simit.org.jobbole.datacenter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import simit.org.jobbole.bean.BlogItem;
import simit.org.jobbole.bean.ResHeader;

/**
 * Created by liuchun on 2016/4/16.
 */
public class DataManager {
    // 存储所有的BlogItems
    private ConcurrentHashMap<Integer, List<BlogItem>> blogSets;
    // 资源条目
    private List<ResHeader> resHeaders;

    /** 私有化构造方法 */
    private DataManager(){
        blogSets = new ConcurrentHashMap<>();
        resHeaders = new ArrayList<>();
    }

    /** 内部静态类保证线程安全 */
    private static class InnerHolder{
        private static final DataManager INSTANCE = new DataManager();
    }

    public static DataManager getInstance(){
        return InnerHolder.INSTANCE;
    }

    private synchronized List<BlogItem> _getBlogItems(int channel){
        List<BlogItem> items;
        if(blogSets.containsKey(channel)){
            items = blogSets.get(channel);
        }else {
            items = new ArrayList<>();
            blogSets.put(channel, items);
        }
        return items;
    }

    /** 获取特定Channel的数据 */
    public static List<BlogItem> getBlogItems(int channel){
        return getInstance()._getBlogItems(channel);
    }

    private synchronized void _putBlogItems(int channel, List<BlogItem> items){
        List<BlogItem> lists = _getBlogItems(channel);
        lists.clear();
        lists.addAll(items);
    }

    /** 更新特定Channel的数据 */
    public static void putBlogItems(int channel, List<BlogItem> items){
        getInstance()._putBlogItems(channel, items);
    }

    private synchronized List<ResHeader> _getResLists(){
        return resHeaders;
    }

    /** 获取资源列表 */
    public static List<ResHeader> getResLists(){
        return getInstance()._getResLists();
    }

    private synchronized void _updateResLists(List<ResHeader> headers){
        resHeaders.clear();
        resHeaders.addAll(headers);
    }

    public static void updateResLists(List<ResHeader> headers){
        getInstance()._updateResLists(headers);
    }
}
