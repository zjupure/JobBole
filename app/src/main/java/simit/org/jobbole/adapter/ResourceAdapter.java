package simit.org.jobbole.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import simit.org.jobbole.activity.DetailActivity;
import simit.org.jobbole.activity.MainActivity;
import simit.org.jobbole.activity.R;
import simit.org.jobbole.activity.SubResActivity;
import simit.org.jobbole.bean.ResHeader;
import simit.org.jobbole.bean.ResItem;
import simit.org.jobbole.config.JobboleConstants;

/**
 * Created by liuchun on 2016/4/17.
 */
public class ResourceAdapter extends ExpandableRecyclerAdapter<ResourceAdapter.TitleViewHolder,ResourceAdapter.ItemViewHolder> {
    private static final int DEFAULT_COLUMN = 2;
    private Context context;
    private LayoutInflater mInflator;
    private OnParentClickListener mListener;

    public ResourceAdapter(Context context, List<ResHeader> parentItemList) {
        super(parentItemList);
        this.context = context;
        mInflator = LayoutInflater.from(context);
        //resHeaders = parentItemList;
    }

    @Override
    public TitleViewHolder onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View itemView = mInflator.inflate(R.layout.res_header_layout, parentViewGroup, false);
        return new TitleViewHolder(itemView);
    }

    @Override
    public ItemViewHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View itemView = mInflator.inflate(R.layout.res_item_wrapper, childViewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindParentViewHolder(final TitleViewHolder parentViewHolder, final int position, final ParentListItem parentListItem) {
        final ResHeader resHeader = (ResHeader)parentListItem;

        // title
        parentViewHolder.mTitle.setText(resHeader.getTitle());
        // icon link
        String iconLink = resHeader.getIconLink();
        if(TextUtils.isEmpty(iconLink)){
            // empty
            parentViewHolder.mIcon.setVisibility(View.GONE);
        }else{
            parentViewHolder.mIcon.setImageURI(Uri.parse(iconLink));
        }
        //parentViewHolder.mMore.setText("更多");
        // more
        final String link = resHeader.getLink();
        parentViewHolder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.onParentItemClickListener(position, parentListItem);
                }
            }
        });
    }

    @Override
    public void onBindChildViewHolder(ItemViewHolder childViewHolder, int position, final Object childListItem) {
        final ResHeader.ResItemWrapper wrapper = (ResHeader.ResItemWrapper)childListItem;
        RecyclerView itemWrapper = childViewHolder.mItemWrapper;
        itemWrapper.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, DEFAULT_COLUMN);
        itemWrapper.setLayoutManager(layoutManager);
        ResItemAdapter itemAdapter = new ResItemAdapter(wrapper.getItemList());
        itemWrapper.setAdapter(itemAdapter);
        /** TODO setOnClick Event */
        itemAdapter.setOnItemClickListener(new ResItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ResItem resItem = wrapper.getItemList().get(position);
                if(context instanceof MainActivity){
                    // mainActivity, then start SubResActivity
                    Intent intent = new Intent(context, SubResActivity.class);
                    intent.putExtra("title", resItem.getTitle());
                    intent.putExtra("link", resItem.getLink());
                    intent.putExtra("channel", JobboleConstants.SUB_SUB_RES_CHANNEL);
                    context.startActivity(intent);
                }else if(context instanceof SubResActivity){
                    // subActivity, then start DetailActivity
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("title", resItem.getTitle());
                    intent.putExtra("link", resItem.getLink());
                    intent.putExtra("channel", JobboleConstants.SUB_SUB_RES_CHANNEL);
                    context.startActivity(intent);
                }
            }
        });
    }

    public void setOnParentClickListener(OnParentClickListener listener){
        mListener = listener;
    }

    /** parent click listener */
    public interface OnParentClickListener{

        void onParentItemClickListener(int position, ParentListItem parentListItem);
    }

    /** hold the parent view */
    public class TitleViewHolder extends ParentViewHolder{
        SimpleDraweeView mIcon;
        TextView mTitle;
        TextView mMore;

        public TitleViewHolder(View itemView) {
            super(itemView);

            mIcon = (SimpleDraweeView) itemView.findViewById(R.id.res_icon);
            mTitle = (TextView) itemView.findViewById(R.id.res_title);
            mMore = (TextView) itemView.findViewById(R.id.res_more);
            //
        }
    }

    /** hold the child view */
    public class ItemViewHolder extends ChildViewHolder{
        RecyclerView mItemWrapper;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mItemWrapper = (RecyclerView) itemView.findViewById(R.id.item_wrapper);
        }
    }
}
