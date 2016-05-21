package simit.org.jobbole.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Response;
import simit.org.jobbole.activity.R;
import simit.org.jobbole.activity.SubResActivity;
import simit.org.jobbole.adapter.ResourceAdapter;
import simit.org.jobbole.bean.ResHeader;
import simit.org.jobbole.config.JobboleConstants;
import simit.org.jobbole.network.JobboleHttpClient;
import simit.org.jobbole.parser.IPageParser;
import simit.org.jobbole.parser.InfoExtractorProxy;

/**
 * Created by liuchun on 2016/4/17.
 */
public class ResourceFragment extends Fragment {
    // UI components
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ResourceAdapter mAdapter;
    // channelId
    private int curChannel = 0;
    private String link;
    // List
    private List<ResHeader> resHeaders;

    public static Fragment newInstance(int channelId, String link){
        Bundle args = new Bundle();
        args.putInt(JobboleConstants.CHANNEL_NAME, channelId);
        args.putString(JobboleConstants.CHANNEL_LINK, link);
        Fragment fragment = new ResourceFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //
        return inflater.inflate(R.layout.fragment_resource, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //
        Bundle bundle = getArguments();
        curChannel = bundle.getInt(JobboleConstants.CHANNEL_NAME, JobboleConstants.RESOURCE);
        link = bundle.getString(JobboleConstants.CHANNEL_LINK);

        initView(getView());
    }

    private void initView(View rootView){
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // set data source and adapter
        resHeaders = new ArrayList<>();
        mAdapter = new ResourceAdapter(getActivity(), resHeaders);
        mRecyclerView.setAdapter(mAdapter);
        // set listener
        mAdapter.setOnParentClickListener(new ResourceAdapter.OnParentClickListener() {
            @Override
            public void onParentItemClickListener(int position, ParentListItem parentListItem) {
                ResHeader resHeader = (ResHeader)parentListItem;
                String title = resHeader.getTitle();
                String link = resHeader.getLink();

                Intent intent = new Intent(getActivity(), SubResActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("link", link);

                if(position == mAdapter.getParentItemList().size() - 1){
                    // last position
                    intent.putExtra("channel", JobboleConstants.SUB_RES_CHANNEL);
                }else {
                    intent.putExtra("channel", curChannel);
                }

                startActivity(intent);
            }
        });
        // fetch data from network
        JobboleHttpClient.getPageSource(link, new ResExtractor());
    }


    /** 从资源页面抽取信息 */
    public class ResExtractor implements IPageParser<String> {
        @Override
        public String parse(Response response) throws Exception {
            //
            String html = response.body().string();
            List<ResHeader> headers = InfoExtractorProxy.extractRes(html);
            /** TODO Write to JSON file **/

            /** save to memory */
            resHeaders.clear();
            resHeaders.addAll(headers);

            return html;
        }

        @Override
        public void onSuccess(String response) {
            Log.d("ResExtractor", "resource page");
            mAdapter.notifyParentItemRangeInserted(0, resHeaders.size());
        }

        @Override
        public void onFailure(Exception e) {
            Log.d("ResExt", "fecth data failure");
        }
    }
}
