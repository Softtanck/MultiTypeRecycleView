package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;

import java.util.List;

/**
 * Created by tangce on 3/22/2016.
 */
public class CarShopAdapter extends BaseAdapter {


    private Context context;

    private List<String> list;

    public CarShopAdapter(Context context, List<String> list) {
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
        ViewHolder holder;
        if (null == convertView) {

            holder = new ViewHolder();

            convertView = View.inflate(context, R.layout.item_car_shop, null);

            holder.textView = (TextView) convertView.findViewById(R.id.tv_car_shop_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        return convertView;
    }


    private class ViewHolder {
        private TextView textView;
    }
}
