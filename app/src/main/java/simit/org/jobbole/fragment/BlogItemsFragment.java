package simit.org.jobbole.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Response;
import simit.org.jobbole.activity.DetailActivity;
import simit.org.jobbole.activity.MainActivity;
import simit.org.jobbole.activity.R;
import simit.org.jobbole.activity.SplashActivity;
import simit.org.jobbole.adapter.RSSRecyclerAdapter;
import simit.org.jobbole.bean.BlogItem;
import simit.org.jobbole.bean.RSSFeed;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.datacenter.DataManager;
import simit.org.jobbole.datacenter.DbManager;
import simit.org.jobbole.network.JobboleHttpClient;
import simit.org.jobbole.parser.IPageParser;
import simit.org.jobbole.parser.InfoExtractorProxy;

/**
 * Created by liuchun on 2016/3/31.
 */
public class BlogItemsFragment extends Fragment {
    // UI components
    private SwipeRefreshLayout mRefreshView;
    private RecyclerView mRecyclerView;
    // LayoutManager & Adapter
    private RecyclerView.LayoutManager mLayoutManager;
    private RSSRecyclerAdapter mAdapter;
    private List<BlogItem> blogItems;
    //
    private int limit = 30;
    // current page indicator
    private int curChannel = 0;
    private String link = "";

    public static Fragment newInstance(int channelId){
        return newInstance(channelId, "");
    }

    public static Fragment newInstance(int channelId, String link){
        Bundle args = new Bundle();
        args.putInt(JobboleConstants.CHANNEL_NAME, channelId);
        args.putString(JobboleConstants.CHANNEL_LINK, link);
        Fragment fragment = new BlogItemsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rssitems, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // get args
        curChannel = getArguments().getInt(JobboleConstants.CHANNEL_NAME, 0);
        link = getArguments().getString(JobboleConstants.CHANNEL_LINK);

        if(curChannel == JobboleConstants.DATE){
            // update the link according to the cityTag
            if(getActivity() instanceof MainActivity){
                String cityTag = ((MainActivity)getActivity()).getCityTag();
                String format = JobboleConstants.getOriUrl(curChannel) + "/tag/%s/";

                if(!TextUtils.isEmpty(cityTag)){
                    link = String.format(Locale.getDefault(), format, cityTag);
                }
            }
        }

        // View initialization
        initViews(getView());
    }

    /** initial views in fragment */
    private void initViews(View rootView){
        mRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        /** TODO deal with special cases, need fixed later */
        if(curChannel == JobboleConstants.SUB_JAVA){
            mRefreshView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.GONE);
            // inflate the viewstub
            ViewStub stub = (ViewStub) rootView.findViewById(R.id.err_message);
            if(stub == null){
                // duplicated inflate
                return;
            }
            stub.inflate();
            TextView textView = (TextView) rootView.findViewById(android.R.id.text1);
            textView.setGravity(Gravity.CENTER);
            textView.setText(R.string.nonexist_message);
            // return back
            return ;
        }
        // SwipeRefreshLayout setup
        mRefreshView.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        mRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /** TODO refreshing operation */
                limit += 10;
                if(curChannel == JobboleConstants.SUB_SUB_RES_CHANNEL ||
                        curChannel == JobboleConstants.DATE){
                    // fetch data from network
                    JobboleHttpClient.getPageSource(link, new ResInfoExtractor());
                    return;
                }
                // other channels
                final String feedUrl = JobboleConstants.getFeedUrl(curChannel);
                if(TextUtils.isEmpty(feedUrl)){
                    mRefreshView.setRefreshing(false);
                    return;
                }
                JobboleHttpClient.getRssFeed(feedUrl, new RssParser());
            }
        });
        // RecyclerView setup
        mRecyclerView.setHasFixedSize(true);
        // LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // init data source
        blogItems = DataManager.getBlogItems(curChannel);
        // set adapter
        mAdapter = new RSSRecyclerAdapter(blogItems);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RSSRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /** TODO RecyclerView Item点击事件, 跳转到详情页 */
                BlogItem item = blogItems.get(position);
                String link = item.getLink();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(JobboleConstants.CHANNEL_NAME, curChannel);
                intent.putExtra("link", link);
                if(curChannel == JobboleConstants.SUB_SUB_RES_CHANNEL){
                    String[] titles = item.getTitle().split("：");
                    intent.putExtra("title", titles[0]);
                }

                startActivity(intent);
            }
        });
        // if it is sub Res channel or date, then fetch data from network
        if(curChannel == JobboleConstants.SUB_SUB_RES_CHANNEL
                || curChannel == JobboleConstants.DATE){
            JobboleHttpClient.getPageSource(link, new ResInfoExtractor());
        }
    }

    /** get page source according to different cityTag */
    public void updateCity(String cityTag){
        String format = JobboleConstants.getOriUrl(curChannel) + "/tag/%s/";
        String url = String.format(format, cityTag);

        if(TextUtils.isEmpty(cityTag)){
            //全国
            url = JobboleConstants.getOriUrl(curChannel);
        }

        JobboleHttpClient.getPageSource(url, new ResInfoExtractor());
        link = url;  // update args
    }

    /** show toast */
    private void showToast(CharSequence text){
        if(getActivity() != null){
            Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /** current rss parser for swipe refresh */
    public class RssParser extends SplashActivity.SimpleRssParser{

        public RssParser() {
            super(getActivity(), curChannel);
        }

        @Override
        public void onSuccess(RSSFeed response) {
            super.onSuccess(response);
            //
            mAdapter.notifyDataSetChanged();
            //
            if(mRefreshView.isRefreshing()){
                mRefreshView.setRefreshing(false);
                showToast("刷新成功");
            }
        }

        @Override
        public void onFailure(Exception e) {
            super.onFailure(e);

            showToast("网络访问出错");
            if(mRefreshView.isRefreshing()){
                mRefreshView.setRefreshing(false);
            }
        }
    }

    /** 从资源页面抓取信息 */
    public class ResInfoExtractor implements IPageParser<String>{
        @Override
        public String parse(Response response) throws Exception {

            String html = response.body().string();
            List<BlogItem> items = new ArrayList<>();

            if(curChannel == JobboleConstants.SUB_SUB_RES_CHANNEL){
                items = InfoExtractorProxy.extractResInfos(html);
            }else if(curChannel == JobboleConstants.DATE){
                items = InfoExtractorProxy.extractDateInfos(html);
            }
            //
            blogItems.clear();
            blogItems.addAll(items);

            return html;
        }

        @Override
        public void onSuccess(String response) {
            Log.d("Date Extractor", "date page");
            //
            mAdapter.notifyDataSetChanged();
            //
            if(mRefreshView.isRefreshing()){
                mRefreshView.setRefreshing(false);
                showToast("刷新成功");
            }
        }

        @Override
        public void onFailure(Exception e) {

            showToast("网络访问出错");
            if(mRefreshView.isRefreshing()){
                mRefreshView.setRefreshing(false);
            }
        }
    }
}
