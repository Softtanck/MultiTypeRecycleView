package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.ProductDetailResponse;

import java.util.List;

/**
 * Created by tangce on 8/26/2016.
 */
public class ProductParamAdapter extends BaseAdapter {

    private Context context;

    private List<ProductDetailResponse.MsgBean.ProductParamBean> list;

    public ProductParamAdapter(Context context, List<ProductDetailResponse.MsgBean.ProductParamBean> list) {
        this.context = context;
        this.list = list;
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
            convertView = View.inflate(context, R.layout.item_param, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_param_item_name);
            holder.value = (TextView) convertView.findViewById(R.id.tv_param_item_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        holder.value.setText(list.get(position).getValue());

        return convertView;
    }

    private class ViewHolder {
        private TextView name;
        private TextView value;
    }
}
