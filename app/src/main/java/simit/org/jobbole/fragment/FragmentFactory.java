package simit.org.jobbole.fragment;

import android.support.v4.app.Fragment;

import simit.org.jobbole.bean.BlogItem;
import simit.org.jobbole.config.JobboleConstants;

/**
 * Created by liuchun on 2016/4/2.
 */
public class FragmentFactory {

    /** 根据channelId创建不同的Fragment,填充到ViewPager中*/
    public static Fragment createFragment(int channelId){
        Fragment fragment;

        if(channelId == JobboleConstants.HOT_TOPIC || channelId == JobboleConstants.BLOG
                || channelId == JobboleConstants.GROUP){
            /** Main Channel */
            fragment = BlogItemsFragment.newInstance(channelId);
        }else if(channelId == JobboleConstants.CHANNEL){
            /** Channel page */
            fragment = ChannelFragment.newInstance(channelId);
        }else if(channelId >= JobboleConstants.SUB_OFFSET){
            /** Sub Channel */
            fragment = BlogItemsFragment.newInstance(channelId);
        }else if(channelId == JobboleConstants.RESOURCE){
            /** Resource Channel */
            String oriUrl = JobboleConstants.getOriUrl(channelId);
            fragment = ResourceFragment.newInstance(channelId, oriUrl);
        }else if(channelId == JobboleConstants.DATE){
            String url = JobboleConstants.getOriUrl(channelId) + "/tag/shanghai/"; // default city is shanghai
            fragment = BlogItemsFragment.newInstance(channelId, url);
        }else {
            /** TODO add more custom fragments here according the channelId */
            fragment = BlogItemsFragment.newInstance(channelId);
        }

        return fragment;
    }
}
