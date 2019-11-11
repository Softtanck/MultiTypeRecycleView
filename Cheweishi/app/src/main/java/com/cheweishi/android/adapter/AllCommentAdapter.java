package com.cheweishi.android.adapter;

import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.UserCommentNative;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class AllCommentAdapter extends BaseAdapter {

    private Context mContext;
    private List<UserCommentNative> mList;
    private LayoutInflater mLayoutInflater;

    public AllCommentAdapter(Context context, List<UserCommentNative> list) {
        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setList(List<UserCommentNative> list) {
        mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.item_all_comment,
                    null);
            viewHolder.img_all_comm_tx = (ImageView) convertView
                    .findViewById(R.id.img_all_comm_tx);
            viewHolder.tv_all_comm_time = (TextView) convertView
                    .findViewById(R.id.tv_all_comm_time);
            viewHolder.tv_all_comm_msg = (TextView) convertView
                    .findViewById(R.id.tv_all_comm_msg);
            viewHolder.tv_all_comm_username = (TextView) convertView
                    .findViewById(R.id.tv_all_comm_username);
            viewHolder.tmg_all_comm_one = (ImageView) convertView
                    .findViewById(R.id.tmg_all_comm_one);
            viewHolder.tmg_all_comm_two = (ImageView) convertView
                    .findViewById(R.id.tmg_all_comm_two);
            viewHolder.tmg_all_comm_three = (ImageView) convertView
                    .findViewById(R.id.tmg_all_comm_three);
            viewHolder.ratingBar1 = (RatingBar) convertView
                    .findViewById(R.id.ratingBar1);
//			ViewGroup.LayoutParams lp = viewHolder.ratingBar1.getLayoutParams();
//			float density = mContext.getResources().getDisplayMetrics().density;
//			if (density - (1.5f) < 0.0001f) {
//				lp.height = px2dip(mContext, 27) * 3 / 2;
//			} else {
//				lp.height = px2dip(mContext, 27) * 2;
//			}
//			viewHolder.ratingBar1.setLayoutParams(lp);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_all_comm_username.setText(mList.get(position)
                .getUser_name());
        viewHolder.tv_all_comm_time.setText(mList.get(position).getTime());
        viewHolder.tv_all_comm_msg.setText(mList.get(position).getUserMsg());

        return convertView;
    }

    private void setStarHeight() {

    }

    private class ViewHolder {
        private ImageView img_all_comm_tx;
        private TextView tv_all_comm_time;
        private TextView tv_all_comm_username;
        private TextView tv_all_comm_msg;
        private ImageView tmg_all_comm_one;
        private ImageView tmg_all_comm_two;
        private ImageView tmg_all_comm_three;
        private RatingBar ratingBar1;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f) - 1;
    }
}
