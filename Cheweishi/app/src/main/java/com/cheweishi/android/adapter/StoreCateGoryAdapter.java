package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;

import java.util.List;

/**
 * Created by tangce on 7/14/2016.
 */
public class StoreCateGoryAdapter extends BaseAdapter {

    private Context context;

    private List<String> list;

    private int type; // 1:服务列表

    public StoreCateGoryAdapter(Context context, List<String> list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    public void setData(List<String> list, int type) {
        this.list = list;
        this.type = type;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return null != list ? list.size() : 0;
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
            convertView = View.inflate(context, R.layout.store_category_item, null);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_store_category_item);
            holder.name = (TextView) convertView.findViewById(R.id.tv_store_category_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (1 == type) {
            holder.icon.setVisibility(View.VISIBLE);
            switch (list.get(position)) {
                case "洗车服务":
                    holder.icon.setImageResource(R.drawable.xiche);
                    break;
                case "保养服务":
                    holder.icon.setImageResource(R.drawable.baoyang);
                    break;
                case "美容服务":
                    holder.icon.setImageResource(R.drawable.meirong);
                    break;
            }
        } else {
            holder.icon.setVisibility(View.GONE);
        }

        holder.name.setText(list.get(position));

        return convertView;
    }

    private class ViewHolder {
        private ImageView icon;
        private TextView name;
    }
}
