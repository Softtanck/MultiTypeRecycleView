package com.cheweishi.android.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.PurseIntegralResponse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IntegralAdapter extends BaseAdapter {

    private Context mContext;
    private List<PurseIntegralResponse.MsgBean> mList;
    private LayoutInflater mLayoutInflater;

    public IntegralAdapter(Context context, List<PurseIntegralResponse.MsgBean> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<PurseIntegralResponse.MsgBean> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 1 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(
                    R.layout.integral_xlistview_item, null);
            viewHolder.sigeAndmileageTextView = (TextView) convertView
                    .findViewById(R.id.sigeAndmileage);
            viewHolder.timeTextView = (TextView) convertView
                    .findViewById(R.id.integral_time);
            viewHolder.integralTextView = (TextView) convertView
                    .findViewById(R.id.integral_textview_number);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        if (mList != null && mList.size() > position) {
            viewHolder.sigeAndmileageTextView.setText(mList.get(position).getRemark());
            viewHolder.timeTextView.setText(transferLongToDate(mList.get(position).getCreateDate()));

            String rule = mList.get(position).getBalanceType();
            if (rule.equals("INCOME")) {
                viewHolder.integralTextView.setText("+" + mList.get(position).getScore() + "分");
            } else if (rule.equals("OUTCOME")) {
                viewHolder.integralTextView.setText("-" + mList.get(position).getScore() + "分");
            }
        }

        return convertView;
    }

    private String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    private class ViewHolder {
        private TextView sigeAndmileageTextView;
        private TextView timeTextView;
        private TextView integralTextView;

    }

}
