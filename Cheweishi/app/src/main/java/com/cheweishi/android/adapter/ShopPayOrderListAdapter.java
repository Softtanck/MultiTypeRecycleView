package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.ShopPayOrderNative;
import com.cheweishi.android.utils.StringUtil;

import java.util.List;

/**
 * Created by tangce on 8/30/2016.
 */
public class ShopPayOrderListAdapter extends BaseAdapter {

    private Context context;
    private List<ShopPayOrderNative> list;

    public ShopPayOrderListAdapter(Context context, List<ShopPayOrderNative> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ShopPayOrderNative> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;


        if (null == convertView) {

            holder = new ViewHolder();


            convertView = View.inflate(context, R.layout.item_shop_order_pay, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_sp_order_item);
            holder.name = (TextView) convertView.findViewById(R.id.tv_sp_order_name);
            holder.money = (TextView) convertView.findViewById(R.id.tv_sp_order_money);
            holder.number = (TextView) convertView.findViewById(R.id.tv_sp_order_number);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (StringUtil.isEmpty(list.get(position).getIcon()))
            holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
        XUtilsImageLoader.getHomeAdvImg(context, R.drawable.udesk_defualt_failure, holder.icon, list.get(position).getIcon());
        holder.name.setText(list.get(position).getName());
        holder.number.setText("x" + list.get(position).getNumber());
        holder.money.setText("￥" + list.get(position).getMoney());


        return convertView;
    }

    private class ViewHolder {
        private ImageView icon;

        private TextView name;

        private TextView number;

        private TextView money;
    }
}
