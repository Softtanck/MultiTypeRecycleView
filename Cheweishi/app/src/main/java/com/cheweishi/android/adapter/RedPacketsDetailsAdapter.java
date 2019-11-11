package com.cheweishi.android.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.ChargeResponse;

/*****
 * 话费详情，返费详情 adapter
 *
 * @author Administrator
 */
public class RedPacketsDetailsAdapter extends BaseAdapter {

    private List<ChargeResponse.MsgBean> list;
    private Context context;
    private XUtilsImageLoader imageLoader;

    public RedPacketsDetailsAdapter(Context context,
                                    List<ChargeResponse.MsgBean> list) {
        this.list = list;
        this.context = context;

    }

    public void setlist(List<ChargeResponse.MsgBean> list) {
        this.list = list;
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return (list == null || list.size() == 0) ? 0 : list.size();
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
    public boolean isEnabled(int position) {
        return true;
    }

    private String transferLongToDate(Long millSec) {
        if (0 == millSec)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_purse_hongbao, null);
            viewHolder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            viewHolder.tv_note = (TextView) convertView
                    .findViewById(R.id.tv_note);
            viewHolder.img_purse_draw = (TextView) convertView.findViewById(R.id.img_purse_draw);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChargeResponse.MsgBean info = list.get(position);
        viewHolder.tv_time.setText("到期时间:2016-5-10 10:58:04");//transferLongToDate(info.getCreateDate()));//+ " - " + "getEFect");
        viewHolder.tv_note.setText(info.getRemark());
        viewHolder.img_purse_draw.setText("￥" + info.getRedPacket());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_time;
        private TextView tv_note;
        private TextView img_purse_draw;

    }
}
