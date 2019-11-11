package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.AreaListResponse;

import java.util.List;

/**
 * Created by Tanck on 9/2/2016.
 * <p>
 * Describe:
 */
public class AreaListDialogAdapter extends BaseAdapter {

    private Context context;

    private List<AreaListResponse.MsgBean> list;

    public AreaListDialogAdapter(Context context, List<AreaListResponse.MsgBean> list) {
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
            convertView = View.inflate(context, R.layout.item_oil, null);
            holder.textView = (TextView) convertView.findViewById(R.id.tv_item_dialog);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(list.get(position).getName());
        return convertView;
    }

    private class ViewHolder {
        private TextView textView;
    }
}
