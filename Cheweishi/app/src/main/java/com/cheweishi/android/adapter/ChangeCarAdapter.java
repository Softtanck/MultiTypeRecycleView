package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.MyCarManagerResponse;

import java.util.List;

/**
 * Created by tangce on 4/12/2016.
 */
public class ChangeCarAdapter extends BaseAdapter {

    private Context context;
    private List<MyCarManagerResponse.MsgBean> msgBeen;

    public ChangeCarAdapter(Context context, List<MyCarManagerResponse.MsgBean> msgBeen) {
        this.context = context;
        this.msgBeen = msgBeen;
    }

    @Override
    public int getCount() {
        return msgBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return msgBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = View.inflate(context, R.layout.item_change_car, null);

        TextView textView = (TextView) convertView.findViewById(R.id.tv_change_car_name);
        textView.setText(msgBeen.get(position).getPlate());

        return convertView;
    }
}
