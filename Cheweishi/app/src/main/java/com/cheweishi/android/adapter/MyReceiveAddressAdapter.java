package com.cheweishi.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.AddressManagerActivity;
import com.cheweishi.android.entity.MyCarManagerResponse;
import com.cheweishi.android.entity.MyRecevieAddressResponse;

import java.util.List;

/**
 * Created by tangce on 8/31/2016.
 */
public class MyReceiveAddressAdapter extends BaseAdapter {

    private final int HEADER = 0;

    private final int CONTENT = 1;

    private Context context;

    private List<MyRecevieAddressResponse.MsgBean> list;

    public MyReceiveAddressAdapter(Context context, List<MyRecevieAddressResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
//        this.list.add(0, new MyRecevieAddressResponse.MsgBean());
    }

    public void setData(List<MyRecevieAddressResponse.MsgBean> list) {
        this.list = list;
//        this.list.add(0, new MyRecevieAddressResponse.MsgBean());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size() + 1;
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
    public int getItemViewType(int position) {
        if (0 == position)
            return HEADER;
        return CONTENT;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        switch (type) {
            case HEADER:
                HeaderViewHolder headerViewHolder;
                if (null == convertView) {
                    headerViewHolder = new HeaderViewHolder();
                    convertView = View.inflate(context, R.layout.item_address_header, null);
                    headerViewHolder.header = (LinearLayout) convertView.findViewById(R.id.rl_ad_header);
                    convertView.setTag(headerViewHolder);
                } else {
                    headerViewHolder = (HeaderViewHolder) convertView.getTag();
                }

                headerViewHolder.header.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 进入添加地址界面
                        Intent addressManger = new Intent(context, AddressManagerActivity.class);
                        context.startActivity(addressManger);
                    }
                });
                break;
            case CONTENT:
                ContentViewHolder contentViewHolder;
                if (null == convertView) {
                    contentViewHolder = new ContentViewHolder();
                    convertView = View.inflate(context, R.layout.item_address_content, null);
                    contentViewHolder.name = (TextView) convertView.findViewById(R.id.tv_ad_item_name);
                    contentViewHolder.address = (TextView) convertView.findViewById(R.id.tv_ad_item_address);
                    contentViewHolder.number = (TextView) convertView.findViewById(R.id.tv_ad_item_phone);
                    contentViewHolder.mDefault = (TextView) convertView.findViewById(R.id.tv_ad_item_def);
                    contentViewHolder.edit = (TextView) convertView.findViewById(R.id.tv_ad_item_edit);
                    convertView.setTag(contentViewHolder);
                } else {
                    contentViewHolder = (ContentViewHolder) convertView.getTag();
                }
                contentViewHolder.name.setText(list.get(position - 1).getConsignee());
                contentViewHolder.address.setText(list.get(position - 1).getAreaName() + list.get(position - 1).getAddress());
                contentViewHolder.number.setText(list.get(position - 1).getPhone());
                if (list.get(position - 1).isIsDefault())
                    contentViewHolder.mDefault.setVisibility(View.VISIBLE);
                else
                    contentViewHolder.mDefault.setVisibility(View.GONE);
                contentViewHolder.edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO 跳转到编辑页面
                        Intent addressManger = new Intent(context, AddressManagerActivity.class);
                        addressManger.putExtra("data", list.get(position - 1));
                        context.startActivity(addressManger);
                    }
                });

                break;
        }


        return convertView;
    }


    private class HeaderViewHolder {
        private LinearLayout header;
    }

    private class ContentViewHolder {
        private TextView name;
        private TextView number;
        private TextView address;
        private TextView mDefault;
        private TextView edit;
    }

}
