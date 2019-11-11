package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.BuyCartListResponse;

import java.util.List;

/**
 * Created by tangce on 8/26/2016.
 */
public class BuyCartListAdapter extends BaseAdapter {

    private Context context;

    private List<BuyCartListResponse.MsgBean> list;

    private onCheckListener checkListener;

    public void setCheckListener(onCheckListener checkListener) {
        this.checkListener = checkListener;
    }

    public interface onCheckListener {
        /**
         * 当check被选择的时候
         *
         * @param position
         */
        void onItemCheckClick(int position);

        /**
         * 增加的时候
         *
         * @param position
         */
        void onItemAddClick(int position);

        /**
         * 减少的时候
         *
         * @param position
         */
        void onItemLessClick(int position);
    }

    public BuyCartListAdapter(Context context, List<BuyCartListResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<BuyCartListResponse.MsgBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_buy_cart, null);
            holder.check = (ImageView) convertView.findViewById(R.id.iv_bc_item_check);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_bc_item_icon);
            holder.name = (TextView) convertView.findViewById(R.id.tv_bc_item_name);
            holder.money = (TextView) convertView.findViewById(R.id.tv_bc_item_money);
            holder.less = (ImageView) convertView.findViewById(R.id.iv_bc_item_num_les);
            holder.add = (ImageView) convertView.findViewById(R.id.iv_bc_item_num_add);
            holder.number = (TextView) convertView.findViewById(R.id.tv_bc_item_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        XUtilsImageLoader.getHomeAdvImg(context, R.drawable.udesk_defualt_failure, holder.icon, list.get(position).getProduct().getImage());

        if (list.get(position).isChecked()) {
            holder.check.setImageResource(R.drawable.mess_yuan_click);
        } else {
            holder.check.setImageResource(R.drawable.mess_yuan);
        }

        holder.name.setText(list.get(position).getProduct().getFullName());


        int temp = 0;
        temp = Integer.valueOf(list.get(position).getProduct().getPrice()) * Integer.valueOf(list.get(position).getQuantity());
        holder.money.setText(String.valueOf(temp));
        holder.number.setText(list.get(position).getQuantity());

        // TODO 关联增加事件
        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != checkListener) {
                    checkListener.onItemCheckClick(position);
                    notifyDataSetChanged();
                }
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != checkListener) {
                    checkListener.onItemAddClick(position);
                    notifyDataSetChanged();
                }
            }
        });

        holder.less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != checkListener) {
                    checkListener.onItemLessClick(position);
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private ImageView check;
        private ImageView icon;
        private TextView name;
        private TextView money;
        private ImageView less;
        private TextView number;
        private ImageView add;
    }
}
