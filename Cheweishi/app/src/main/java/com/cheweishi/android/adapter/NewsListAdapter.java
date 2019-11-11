package com.cheweishi.android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.NewsListResponse;
import com.cheweishi.android.widget.SimpleTagImageView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by tangce on 7/19/2016.
 */
public class NewsListAdapter extends BaseAdapter {

    private Context context;

    private List<NewsListResponse.MsgBean> list;


    public NewsListAdapter(Context context, List<NewsListResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<NewsListResponse.MsgBean> list) {
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
            convertView = View.inflate(context, R.layout.item_news_list, null);

            holder.icon = (SimpleTagImageView) convertView.findViewById(R.id.iv_news_item_icon);
            holder.time = (TextView) convertView.findViewById(R.id.tv_news_item_time);
            holder.title = (TextView) convertView.findViewById(R.id.tv_news_item_title);
            holder.subTitle = (TextView) convertView.findViewById(R.id.tv_news_item_subtitle);
            holder.readCount = (TextView) convertView.findViewById(R.id.tv_news_item_read);
            holder.commentCount = (TextView) convertView.findViewById(R.id.tv_news_item_comment);
            holder.likeCount = (TextView) convertView.findViewById(R.id.tv_news_item_like);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        XUtilsImageLoader.getxUtilsImageLoader(context, R.drawable.udesk_defalut_image_loading, R.drawable.udesk_defualt_failure, holder.icon, list.get(position).getImgUrl());
        holder.icon.setTagText("热门");

//        LogHelper.d("第:" + position + "消息是:" + list.get(position).getReadCounts() + "--" + list.get(position).getTitle());
        holder.title.setText(list.get(position).getTitle());
        holder.subTitle.setText(list.get(position).getSubTitle());
        holder.readCount.setText(list.get(position).getReadCounts());
        holder.commentCount.setText(list.get(position).getCommentCounts());
        holder.likeCount.setText(list.get(position).getLikeCounts());
        holder.time.setText(formatDate(list.get(position).getModifyDate()));

        return convertView;
    }

    private class ViewHolder {
        private SimpleTagImageView icon;
        private TextView time;
        private TextView title;
        private TextView subTitle;
        private TextView readCount;
        private TextView commentCount;
        private TextView likeCount;
    }

    /**
     * 格式化日期
     *
     * @param dateStr
     * @return
     */
    private String formatDate(Long dateStr) {
        String hms = "今天";
        if (86400000 >= Math.abs(System.currentTimeMillis() - dateStr)) {
            return hms;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//初始化Formatter的转换格式。
        hms = formatter.format(dateStr);
        return hms;
    }
}
