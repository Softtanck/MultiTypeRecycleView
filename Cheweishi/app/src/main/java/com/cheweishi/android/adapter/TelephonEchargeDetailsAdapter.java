package com.cheweishi.android.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.ChargeResponse;
import com.cheweishi.android.utils.StringUtil;

/*****
 * 话费详情，返费详情 adapter
 *
 * @author Administrator
 */
public class TelephonEchargeDetailsAdapter extends BaseAdapter {

    private List<ChargeResponse.MsgBean> list;
    private Context context;

    public TelephonEchargeDetailsAdapter(Context context, List<ChargeResponse.MsgBean> list) {
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
        return 0;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    private String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.telephoneecharge_pull_listview_item, null);
            viewHolder.MoldMoney = (TextView) convertView
                    .findViewById(R.id.telephone_pulltorefresh_tv_moldmoney);
            viewHolder.Money = (TextView) convertView
                    .findViewById(R.id.telephone_pulltorefresh_tv_money);
            viewHolder.Time = (TextView) convertView
                    .findViewById(R.id.telephone_pulltorefresh_tv_time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ChargeResponse.MsgBean info = list.get(position);
        viewHolder.Time.setText(transferLongToDate(info.getCreateDate()));

        if (info.getBalanceType().equals("OUTCOME")) {
            viewHolder.Money.setTextColor(context.getResources().getColor(
                    R.color.red));
            viewHolder.Money.setText("-" + info.getMoney());

        } else if (info.getBalanceType().equals("INCOME")) {

            viewHolder.Money.setTextColor(Color.GREEN);
//            if (!StringUtil.isEmpty(info.getRedPacket())) {
//                viewHolder.Money.setText("+" + info.getRedPacket());
//            } else {
//                viewHolder.Money.setText("+" + info.getMoney());
//            }
            viewHolder.Money.setText("+" + info.getMoney());
        }
        viewHolder.MoldMoney.setText(info.getRemark());
        return convertView;
    }

    private class ViewHolder {
        TextView MoldMoney;
        TextView Time;
        TextView Money;
    }
}
