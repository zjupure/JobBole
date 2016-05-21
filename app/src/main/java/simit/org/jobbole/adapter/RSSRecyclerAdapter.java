package simit.org.jobbole.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import simit.org.jobbole.activity.R;
import simit.org.jobbole.bean.BlogItem;

/**
 * Created by liuchun on 2016/3/31.
 */
public class RSSRecyclerAdapter extends RecyclerView.Adapter<RSSRecyclerAdapter.ViewHolder> {
    private List<BlogItem> blogItems;
    private OnItemClickListener mListener;

    public RSSRecyclerAdapter(List<BlogItem> blogItems) {
        this.blogItems = blogItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 渲染view,装入ViewHolder复用
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rssitem_layout, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 加载数据
        BlogItem item = blogItems.get(position);
        String title = item.getTitle();
        holder.mTitle.setText(title);
        Log.d("rssItem", "title: " + title);
        //
        String description = item.getDescription();
        if(TextUtils.isEmpty(description)){
            holder.mDescription.setVisibility(View.GONE);
        }else {
            holder.mDescription.setText(Html.fromHtml(description));
            Log.d("rssItem", "description: " + description);
        }
        //String normDate = JobUtil.toDisplayTime(item.getPubDate());
        String pubDate = item.getPubDate();
        if(!TextUtils.isEmpty(pubDate)){
            holder.mPubDate.setVisibility(View.VISIBLE);
            holder.mPubDate.setText(pubDate);
            Log.d("rssItem", "pubDate: " + pubDate);
        }


        // display icon in the left
        String iconLink = item.getIconLink();
        if(!TextUtils.isEmpty(iconLink)){
            holder.mIcon.setVisibility(View.VISIBLE);
            holder.mIcon.setImageURI(Uri.parse(iconLink));
            Log.d("rssItem", "iconLink: " + iconLink);
        }
    }

    @Override
    public int getItemCount() {
        return blogItems == null ? 0 : blogItems.size();
    }



    /** RecyclerView Item点击事件 */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    /** ViewHolder容器类 */
    public class ViewHolder extends RecyclerView.ViewHolder{
        public SimpleDraweeView mIcon;
        public TextView mTitle;
        public TextView mDescription;
        public TextView mPubDate;

        public ViewHolder(View itemView) {
            super(itemView);
            //
            mIcon = (SimpleDraweeView) itemView.findViewById(R.id.item_icon);
            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mDescription = (TextView) itemView.findViewById(R.id.item_description);
            mPubDate = (TextView) itemView.findViewById(R.id.item_time);
            //
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onItemClick(v, getLayoutPosition());
                    }
                }
            });
        }
    }
}
