package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.ComponentServiceResponse;
import com.cheweishi.android.entity.ComponentServiceShowResponse;
import com.cheweishi.android.response.BaseResponse;

import java.util.List;

/**
 * Created by tangce on 6/1/2016.
 */
public class ComponentServiceAdapter extends BaseAdapter {

    private Context context;

    private List<ComponentServiceShowResponse> list;

    public ComponentServiceAdapter(Context context, List<ComponentServiceShowResponse> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ComponentServiceShowResponse> list) {
        if (null != list) {
            this.list = list;
            notifyDataSetChanged();
        }
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
            convertView = View.inflate(context, R.layout.item_component_service, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_component_service_name);
            holder.price = (TextView) convertView.findViewById(R.id.tv_component_service_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getServiceName() + " - " + list.get(position).getMsg().getName());
        holder.price.setText("￥" + list.get(position).getMsg().getPrice() + "元");

        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView price;
    }
}
