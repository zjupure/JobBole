package simit.org.jobbole.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

import okhttp3.Response;
import simit.org.jobbole.bean.BlogItem;
import simit.org.jobbole.bean.RSSFeed;
import simit.org.jobbole.bean.RSSItem;
import simit.org.jobbole.bean.ResHeader;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.datacenter.DataManager;
import simit.org.jobbole.datacenter.DbManager;
import simit.org.jobbole.network.JobboleHttpClient;
import simit.org.jobbole.parser.DefaultRssParser;
import simit.org.jobbole.parser.IPageParser;
import simit.org.jobbole.parser.InfoExtractorProxy;

public class SplashActivity extends AppCompatActivity {
    private static final int CHANNEL_COUNT = 6;
    private static final int SUB_CHANNEL_COUNT = 6;
    private static final int DEFAULT_LIMIT = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
        Fresco.initialize(getApplicationContext());
        //
        setContentView(R.layout.activity_splash);

        // 发起网络请求,获取数据
        String feedURL;
        for(int i = 0; i < CHANNEL_COUNT; i++){
            feedURL = JobboleConstants.getFeedUrl(i);
            if(TextUtils.isEmpty(feedURL)){
                continue;
            }

            JobboleHttpClient.getRssFeed(feedURL, new SimpleRssParser(this, i));
        }
        for(int i = 0; i < SUB_CHANNEL_COUNT; i++){
            int channel = i + JobboleConstants.SUB_OFFSET;
            feedURL = JobboleConstants.getFeedUrl(channel);

            if(TextUtils.isEmpty(feedURL)){
                continue;
            }

            JobboleHttpClient.getRssFeed(feedURL, new SimpleRssParser(this, channel));
        }
        // 延迟进入主Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
        //
        Thread loadTask = new Thread(new Runnable() {
            @Override
            public void run() {
                // if there exist data in database first load local data
                DbManager db = DbManager.getInstance(SplashActivity.this);
                for(int i = 0; i < CHANNEL_COUNT; i++){
                    List<BlogItem> blogItems = db.getBlogItems(i, DEFAULT_LIMIT);
                    DataManager.putBlogItems(i, blogItems);
                }
                for(int i = 0; i < SUB_CHANNEL_COUNT; i++){
                    int channel = i + JobboleConstants.SUB_OFFSET;
                    List<BlogItem> blogItems = db.getBlogItems(channel, DEFAULT_LIMIT);
                    DataManager.putBlogItems(channel, blogItems);
                }
            }
        });
        loadTask.start();
    }


    /** Simple Rss Parser for network */
    public static class SimpleRssParser extends DefaultRssParser{
        private Context context;
        private int channel;

        public SimpleRssParser(Context context, int channel){
            this.context = context;
            this.channel = channel;
        }

        @Override
        public RSSFeed parse(Response response) throws Exception {
            RSSFeed rssFeed = super.parse(response);
            // extract rssItems from Feed
            List<RSSItem> items = rssFeed.getRssItems();
            /** TODO database operation */
            for(RSSItem item : items){
                // add channel info to the item
                item.setChannel(channel);
            }
            /** insert data to database */
            DbManager db = DbManager.getInstance(context);
            db.insert(items);
            /** get data from database then put them to memory */
            List<BlogItem> blogItems = db.getBlogItems(channel, DEFAULT_LIMIT);
            DataManager.putBlogItems(channel, blogItems);

            Log.d("SimpleRssParser", "curChannel: " + channel);

            return rssFeed;
        }
    }
}
