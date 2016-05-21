package simit.org.jobbole.config;

/**
 * Created by liuchun on 2016/3/31.
 */
public class JobboleConstants {
    public static final String CHANNEL_NAME = "channel";
    public static final String CHANNEL_LINK = "link";
    public static final String CHANNEL_TITLE = "title";
    /** 频道列表 */
    public static final int HOT_TOPIC = 0;
    public static final int BLOG = 1;
    public static final int CHANNEL = 2;
    public static final int RESOURCE = 3;
    public static final int GROUP = 4;
    public static final int DATE = 5;
    /** 子频道列表 */
    public static final int SUB_DESIGN = 6;
    public static final int SUB_WEB = 7;
    public static final int SUB_PYTHON = 8;
    public static final int SUB_JAVA = 9;
    public static final int SUB_ANDROID = 10;
    public static final int SUB_IOS = 11;
    public static final int SUB_OFFSET = 6;  //偏移量,修正下标数组
    /** 资源下属列表 */
    public static final int SUB_RES_CHANNEL = 20;
    public static final int SUB_SUB_RES_CHANNEL = 21;
    /** 频道Feed订阅地址 */
    public static final String HOT_TOPIC_FEED = "http://top.jobbole.com/feed/";
    public static final String BLOG_FEED = "http://blog.jobbole.com/feed/";
    public static final String CHANNEL_FEED = "";
    public static final String RESOURCE_FEED = "";
    public static final String GROUP_FEED = "http://group.jobbole.com/feed/";
    public static final String DATE_FEED = "";
    /** 子频道Feed订阅地址 */
    public static final String SUB_DESIGN_FEED = "http://design.jobbole.com/feed/";
    public static final String SUB_WEB_FEED = "http://web.jobbole.com/feed/";
    public static final String SUB_PYTHON_FEED = "http://python.jobbole.com/feed/";
    public static final String SUB_JAVA_FEED = "";
    public static final String SUB_ANDROID_FEED = "http://android.jobbole.com/feed/";
    public static final String SUB_IOS_FEED = "http://ios.jobbole.com/feed/";
    /** 频道的原生URL地址, 需要手动解析HTML网页获取数据 */
    public static final String HOT_TOPIC_URL = "http://www.jobbole.com";  //热门列表,不是按时间顺序
    public static final String BLOG_URL = "http://blog.jobbole.com";
    public static final String CHANNEL_URL = "http://channel.jobbole.com.xxx"; // 不存在的url
    public static final String RESOURCE_URL = "http://hao.jobbole.com";
    public static final String GROUP_URL = "http://group.jobbole.com";
    public static final String DATE_URL = "http://date.jobbole.com";  //默认按HOT排序
    // 相亲栏目可以按城市抓取,在原URL之后加"tag/city/",for example "http://date.jobbole.com/tag/beijing/"
    /** 子频道的原生URL地址 **/
    public static final String SUB_DESIGN_URL = "http://design.jobbole.com";
    public static final String SUB_WEB_URL = "http://web.jobbole.com";
    public static final String SUB_PYTHON_URL = "http://python.jobbole.com";
    public static final String SUB_JAVA_URL = "http://www.importnew.com/";  //无Feed,不处理
    public static final String SUB_ANDROID_URL = "http://android.jobbole.com";
    public static final String SUB_IOS_URL = "http://ios.jobbole.com";

    /** 存入列表 **/
    public static final String[] FEED_URL_LIST = {HOT_TOPIC_FEED, BLOG_FEED, CHANNEL_FEED,
            RESOURCE_FEED, GROUP_FEED, DATE_FEED};
    public static final String[] SUB_FEED_URL_LIST = {SUB_DESIGN_FEED, SUB_WEB_FEED, SUB_PYTHON_FEED,
            SUB_JAVA_FEED, SUB_ANDROID_FEED, SUB_IOS_FEED};
    public static final String[] ORI_URL_LIST = {HOT_TOPIC_URL, BLOG_URL, CHANNEL_URL, RESOURCE_URL,
            GROUP_URL, DATE_URL};
    public static final String[] SUB_ORI_URL_LIST = {SUB_DESIGN_URL, SUB_WEB_URL, SUB_PYTHON_URL,
            SUB_JAVA_URL, SUB_ANDROID_URL, SUB_IOS_URL};

    /** Feed时间格式 */
    public static final String DEFAULT_TIME_FORMAT = "E, dd MMM yyyy HH:mm:ss Z";
    public static final String DISPLAY_TIME_FORMAT = "yyyy年M月d日 HH:mm";

    /** 匹配对于字段 */
    public static final String DESCRIPTION_PATTERN = "<p>The post.*</p>";
    public static final String DESCRIPTION_BLOG_PATTERN = "<p><a href=\"http://blog.jobbole.com/\\d+/\">.*</p>";


    /** 根据Link判断当前所属频道 */
    public static int getChannel(String link){
        return getIndex(link, ORI_URL_LIST);
    }

    /** 根据Link判断当前所属子频道 */
    public static int getSubChannel(String link){
        int index = getIndex(link, SUB_ORI_URL_LIST);

        if(index > 0){
            index += SUB_OFFSET;  // 加上偏移量
        }
        return index;
    }

    /** 获取Channel对应的FeedURL地址 */
    public static String getFeedUrl(int channel){
        String feedURL;
        if(channel >= SUB_OFFSET){
            feedURL = SUB_FEED_URL_LIST[channel - SUB_OFFSET];
        }else{
            feedURL = FEED_URL_LIST[channel];
        }

        return feedURL;
    }

    /** 获取Channel对应的原生地址 */
    public static String getOriUrl(int channel){
        String oriURL;
        if(channel >= SUB_OFFSET){
            oriURL = SUB_ORI_URL_LIST[channel - SUB_OFFSET];
        }else {
            oriURL = ORI_URL_LIST[channel];
        }

        return oriURL;
    }

    /** 获取在数组中的下标 **/
    private static int getIndex(String link, String[] urls){

        for(int i = 0; i < urls.length; i++){
            if(link.equals(urls[i])){
                return i;
            }
        }
        return -1;
    }


}
