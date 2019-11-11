package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.UserCommentsResponse;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.XCRoundImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Tanck on 10/25/2016.
 * <p>
 * Describe:用户评论适配器
 */
public class CommentsAdapter extends BaseAdapter {

    private Context context;

    private List<UserCommentsResponse.MsgBean> list;

    public CommentsAdapter(Context context, List<UserCommentsResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<UserCommentsResponse.MsgBean> list) {
        this.list = list;
        notifyDataSetChanged();
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
            convertView = View.inflate(context, R.layout.item_user_comment, null);
            holder.icon = (XCRoundImageView) convertView.findViewById(R.id.xiv_item_product_common_user);
            holder.userName = (TextView) convertView.findViewById(R.id.tv_item_product_common_user_name);
            holder.time = (TextView) convertView.findViewById(R.id.tv_item_product_common_time);
            holder.content = (TextView) convertView.findViewById(R.id.tv_item_product_common_content);
            holder.biz = (TextView) convertView.findViewById(R.id.tv_item_product_detail_biz);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userName.setText(list.get(position).getMember().getUserName());
        XUtilsImageLoader.getHomeAdvImg(context, R.drawable.udesk_defualt_failure, holder.icon, list.get(position).getMember().getPhoto());
        holder.time.setText(getDate(list.get(position).getCreateDate()));
        holder.content.setText(list.get(position).getContent());
        if (!StringUtil.isEmpty(list.get(position).getBizReply())) {
            holder.biz.setVisibility(View.VISIBLE);
            holder.biz.setText(list.get(position).getBizReply());
        } else {
            holder.biz.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder {
        private XCRoundImageView icon;
        private TextView userName;
        private TextView time;
        private TextView content;
        private TextView biz;
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
