package com.cheweishi.android.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.ShopTypeResponse;

import java.util.List;

/**
 * Created by tangce on 7/14/2016.
 */
public class ShopCateGoryAdapter extends BaseAdapter {

    private Context context;

    private SparseArray<String> list;


    public ShopCateGoryAdapter(Context context, SparseArray<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(SparseArray<String> list) {
        this.list = list;
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
            convertView = View.inflate(context, R.layout.shop_category_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_shop_category_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position));

        return convertView;
    }

    private class ViewHolder {
        private TextView name;
    }
}
