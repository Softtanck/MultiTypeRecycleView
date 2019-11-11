package com.cheweishi.android.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.MainGridInfoNative;
import com.cheweishi.android.tools.ScreenTools;
import com.cheweishi.android.utils.DisplayUtil;
import com.cheweishi.android.utils.StringUtil;

/**
 * 商城版首页GridView适配器
 *
 * @author mingdasen
 */
public class MainGridViewAdapter extends BaseAdapter {

    private List<MainGridInfoNative> list;
    private Context mContext;
    private TextView textView;
    private ImageView imageView;
    private boolean showIcon;//是否展示中间标志

    public MainGridViewAdapter(Context mContext, List<MainGridInfoNative> list, boolean showIcon) {
        this.mContext = mContext;
        this.list = list;
        this.showIcon = showIcon;
    }

    @Override
    public int getCount() {
        return StringUtil.isEmpty(list) ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_gridview, null);
        textView = (TextView) convertView.findViewById(R.id.tv_name);
        imageView = (ImageView) convertView.findViewById(R.id.img_icon);
        convertView.setBackgroundResource(R.drawable.more_back);
        if (!showIcon || (position != 5 && position != 6)) {
            textView.setText(list.get(position).getName());
            imageView.setImageResource(list.get(position).getImgId());
        } else if (showIcon && position == 5) {
            float f;
            if (ScreenTools.getScreentWidth((Activity) mContext) <= 480) {
                f = 0.5f;
            } else {
                f = 1;
            }
            convertView.setLayoutParams(new LayoutParams(ScreenTools.getScreentWidth((Activity) mContext) / 2 - DisplayUtil.dip2px(mContext, f), LayoutParams.WRAP_CONTENT));
            textView.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.main_logo);
            convertView.setBackgroundResource(R.color.white);
        } else if (showIcon && position == 6) {
            convertView.setVisibility(View.GONE);
        }
        return convertView;
    }

}
