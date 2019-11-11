package com.cheweishi.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.NewsListResponse;
import com.cheweishi.android.entity.ShopListResponse;
import com.cheweishi.android.widget.RatioImageView;
import com.cheweishi.android.widget.SimpleTagImageView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by tangce on 7/19/2016.
 */
public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {

    private Context context;

    private List<ShopListResponse.MsgBean> list;

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public ShopListAdapter(Context context, List<ShopListResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ShopListResponse.MsgBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context, R.layout.item_shop_list, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        XUtilsImageLoader.getHomeAdvImg(context, R.drawable.udesk_defualt_failure, holder.icon, list.get(position).getImage());
        holder.desc.setText(list.get(position).getName());
        holder.buyCount.setText(list.get(position).getSales() + "人已购");
        holder.money.setText(list.get(position).getPrice());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RatioImageView icon;
        private TextView desc;
        private TextView money;
        private TextView buyCount;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            icon = (RatioImageView) itemView.findViewById(R.id.iv_shop_item_img);
            desc = (TextView) itemView.findViewById(R.id.tv_shop_item_desc);
            buyCount = (TextView) itemView.findViewById(R.id.tv_shop_item_buy_count);
            money = (TextView) itemView.findViewById(R.id.tv_shop_item_money);
//            icon.setOriginalSize(50, 50);
        }

        @Override
        public void onClick(View v) {
            if (null != mOnItemClickListener)
                mOnItemClickListener.onItemClick(v, getPosition());
        }
    }

}
