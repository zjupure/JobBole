package simit.org.jobbole.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        holder.mDescription.setText(Html.fromHtml(description));
        Log.d("rssItem", "description: " + description);

        //String normDate = JobUtil.toDisplayTime(item.getPubDate());
        String pubDate = item.getPubDate();
        holder.mPubDate.setText(pubDate);
        Log.d("rssItem", "pubDate: " + pubDate);
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
        public TextView mTitle;
        public TextView mDescription;
        public TextView mPubDate;

        public ViewHolder(View itemView) {
            super(itemView);
            //
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
