package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.DevicelistResponse;

import java.util.List;

/**
 * Created by tangce on 6/19/2016.
 */
public class DevicesListAdapter extends BaseAdapter {

    private Context context;

    private List<DevicelistResponse.MsgBean> list;

    public DevicesListAdapter(Context context, List<DevicelistResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<DevicelistResponse.MsgBean> list) {
        this.list = list;
        this.notifyDataSetChanged();
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

            convertView = View.inflate(context, R.layout.item_deices_list, null);
            holder.tenantName = (TextView) convertView.findViewById(R.id.tv_device_tenant_name);
            holder.deviceNo = (TextView) convertView.findViewById(R.id.tv_device_no);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tenantName.setText(list.get(position).getTenantName());
        holder.deviceNo.setText(list.get(position).getDeviceNo());

        return convertView;
    }

    private class ViewHolder {
        private TextView tenantName;

        private TextView deviceNo;
    }
}
