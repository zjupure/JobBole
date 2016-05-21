package simit.org.jobbole.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


import okhttp3.Response;
import simit.org.jobbole.activity.R;
import simit.org.jobbole.network.JobboleHttpClient;
import simit.org.jobbole.parser.InfoExtractorProxy;
import simit.org.jobbole.parser.IPageParser;

/**
 * Created by liuchun on 2016/4/16.
 */
public class DetailFragment extends Fragment {
    // UI components
    private WebView mWebView;
    private WebSettings mWebSetting;
    private ProgressBar mLoading;
    //
    private int curChannel;

    public static Fragment newInstance(int channelID, String link){
        Fragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt("channel", channelID);
        args.putString("link", link);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // layout
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //
        initWebView(getView());
    }

    /** init the webview */
    private void initWebView(View rootView){
        // Webview
        mWebView = (WebView) rootView.findViewById(R.id.web_container);
        mLoading = (ProgressBar) rootView.findViewById(R.id.load_progress);
        // Websettings
        mWebSetting = mWebView.getSettings();
        mWebSetting.setJavaScriptEnabled(true);
        mWebSetting.setDomStorageEnabled(true);
        mWebSetting.setAppCacheEnabled(true);
        mWebSetting.setUseWideViewPort(true);
        mWebSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebSetting.setBlockNetworkImage(true);
        mWebSetting.setDefaultTextEncodingName("UTF-8");
        //
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // start loading image
                mWebSetting.setBlockNetworkImage(false);
                mLoading.setVisibility(View.INVISIBLE);
            }
        });

        // load the data
        Bundle bundle = getArguments();
        curChannel = bundle.getInt("channel", 0);
        String link = bundle.getString("link");
        // 异步加载数据
        mLoading.setVisibility(View.VISIBLE);
        JobboleHttpClient.getPageSource(link, new DefaultArticleExtractor());
    }


    /** 默认的Article信息提取类 */
    public class DefaultArticleExtractor implements IPageParser<String> {

        /** 在子线程处理 */
        @Override
        public String parse(Response response) throws Exception {
            //
            String html = response.body().string();
            String info = InfoExtractorProxy.extractBlog(html, curChannel);

            return info;
        }

        /** 在主线程回调 */
        @Override
        public void onSuccess(String response) {
            if(getActivity() != null){
                // not detach from the activity
                Log.d("DetailFragment", response);
                mWebView.loadData(response, "text/html;charset=UTF-8", null);
            }
        }

        @Override
        public void onFailure(Exception e) {

        }
    }
}
