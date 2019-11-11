package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.R;
import com.cheweishi.android.entity.MyCarManagerResponse;

import java.util.List;

/**
 * Created by tangce on 4/7/2016.
 */
public class QrCodeAdapter extends BaseAdapter {

    private Context context;

    private List<MyCarManagerResponse.MsgBean> msg;

    private boolean isAddDevice;

    public QrCodeAdapter(Context context, List<MyCarManagerResponse.MsgBean> msgBeen, boolean isAddDevice) {
        this.context = context;
        this.msg = msgBeen;
        this.isAddDevice = isAddDevice;
    }

    @Override
    public int getCount() {
        return msg.size();
    }

    @Override
    public Object getItem(int position) {
        return msg.get(position);
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

            convertView = View.inflate(context, R.layout.item_qr_car_list, null);

            holder.imageView = (ImageView) convertView.findViewById(R.id.iv_qr_code_car_icon);

            holder.textView = (TextView) convertView.findViewById(R.id.tv_qr_code_name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (isAddDevice) {
            if (null != msg.get(position).getDeviceNo() && !"".equals(msg.get(position).getDeviceNo())) {
                holder.textView.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.GONE);
            } else {
                holder.textView.setVisibility(View.VISIBLE);
                holder.imageView.setVisibility(View.VISIBLE);
                holder.textView.setText(msg.get(position).getVehicleFullBrand() + msg.get(position).getPlate());
                XUtilsImageLoader.getxUtilsImageLoader(context,
                        R.drawable.repaire_img, holder.imageView,
                        msg.get(position).getBrandIcon());
            }
        } else {
            holder.textView.setText(msg.get(position).getVehicleFullBrand() + msg.get(position).getPlate());
            XUtilsImageLoader.getxUtilsImageLoader(context,
                    R.drawable.repaire_img, holder.imageView,
                    msg.get(position).getBrandIcon());
        }
        return convertView;
    }


    private class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
