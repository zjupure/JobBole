package simit.org.jobbole.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import simit.org.jobbole.activity.R;

/**
 * Created by liuchun on 2016/5/22.
 */
public class CityItemAdapter extends RecyclerView.Adapter<CityItemAdapter.ItemViewHolder>{
    private Context context;
    private OnItemClickListener mListener;
    private String[] cityNames;

    public CityItemAdapter(Context context){
        this.context = context;
        cityNames = context.getResources().getStringArray(R.array.city_names);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.city_item_layout, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String cityName = cityNames[position];

        holder.mCity.setText(cityName);
    }

    @Override
    public int getItemCount() {
        return cityNames != null ? cityNames.length : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    /** Item onClick interface */
    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    /** Veiw Holder for city items */
    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView mCity;


        public ItemViewHolder(View itemView){
            super(itemView);

            mCity = (TextView) itemView.findViewById(R.id.city_tv);
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
