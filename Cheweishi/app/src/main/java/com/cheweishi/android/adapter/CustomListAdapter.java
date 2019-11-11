package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;

import java.util.List;

/**
 * Created by Administrator on 2016/4/24.
 */
public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private List<String> city;

    public CustomListAdapter(List<String> city, Context context) {
        this.city = city;
        this.context = context;
    }

    @Override
    public int getCount() {
        return city.size();
    }

    @Override
    public Object getItem(int position) {
        return city.get(position);
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
            convertView = View.inflate(context, R.layout.item_custom_list, null);
            holder.content = (TextView) convertView.findViewById(R.id.tv_custom_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.content.setText(city.get(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView content;
    }
}
