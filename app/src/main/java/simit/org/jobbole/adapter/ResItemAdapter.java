package simit.org.jobbole.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import simit.org.jobbole.activity.R;
import simit.org.jobbole.bean.ResItem;

/**
 * Created by liuchun on 2016/4/17.
 */
public class ResItemAdapter extends RecyclerView.Adapter<ResItemAdapter.ItemViewHolder>{
    private List<ResItem> items;

    public ResItemAdapter(List<ResItem> items){
        this.items = items;
    }

    @Override
    public ResItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.res_item_layout, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResItemAdapter.ItemViewHolder holder, int position) {
        ResItem item = items.get(position);
        // title
        holder.mTitle.setText(item.getTitle());
        // description
        String description = item.getDescription();
        if(TextUtils.isEmpty(description)){
            holder.mDescription.setVisibility(View.GONE);
        }else {
            holder.mDescription.setVisibility(View.VISIBLE);
            holder.mDescription.setText(description);
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    /** Veiw Holder for resource items */
    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mDescription;

        public ItemViewHolder(View itemView) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.item_title);
            mDescription = (TextView) itemView.findViewById(R.id.item_description);
        }
    }
}
