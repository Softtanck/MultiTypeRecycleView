package com.cheweishi.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.WebActivity;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.API;
import com.cheweishi.android.entity.ADInfo;
import com.cheweishi.android.entity.AdvResponse;
import com.cheweishi.android.tools.ScreenTools;
import com.cheweishi.android.utils.LogHelper;

public class ImgAdapter extends BaseAdapter {

    private BaseActivity _context;
    private List<Integer> imgList;
    private AdvResponse adInfos;
    private int returnCount = -1;

    private int h, w;

    private SparseArray<ViewHolder> holders = new SparseArray<>();

    public ImgAdapter(BaseActivity context, List<Integer> imgList) {
        _context = context;
        this.imgList = imgList;
    }

    public ImgAdapter(BaseActivity context, AdvResponse adInfos,
                      int returnCount) {
        _context = context;
        this.adInfos = adInfos;
        this.returnCount = returnCount;
        w = ScreenTools.getScreentWidth(_context);
        h = (int) (((float) ScreenTools.getScreentWidth(_context)) * (((float) 80) / ((float) 320)));
    }

    public void setData(AdvResponse adInfos, int count) {
        this.adInfos = adInfos;
        this.returnCount = count;
        notifyDataSetChanged();
    }

    public int getCount() {
        if (returnCount == -1) {
            return Integer.MAX_VALUE;
        } else {
            return adInfos == null ? 0 : adInfos.getMsg().size();
        }

    }

    public Object getItem(int position) {

        return adInfos.getMsg().get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        int p = 0;
        if (null != adInfos && null != adInfos.getMsg() && 0 < adInfos.getMsg().size()) {
            p = position % adInfos.getMsg().size();
            try {
                viewHolder = holders.get(p);
            } catch (Exception e) {
            }
            if (null == viewHolder) {
                viewHolder = new ViewHolder();
                ImageView imageView = new ImageView(_context);
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ScaleType.FIT_XY);
                imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                viewHolder.imageView = imageView;
                holders.put(p, viewHolder);
//                holders.set(p, viewHolder);
            }

            ViewGroup.LayoutParams lp = viewHolder.imageView
                    .getLayoutParams();
            lp.width = w;
            lp.height = h;
            viewHolder.imageView.setLayoutParams(lp);
            XUtilsImageLoader.getxUtilsImageLoader(_context,
                    R.drawable.ad_default_back,
                    viewHolder.imageView, adInfos.getMsg().get(p).getAdvImageUrl());
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(_context, WebActivity.class);
                    intent.putExtra("url", adInfos.getMsg().get(position % adInfos.getMsg().size()).getAdvContentLink());
                    _context.startActivity(intent);
                }
            });
        }
        return null == viewHolder ? new View(_context) : viewHolder.imageView;
    }

    private static class ViewHolder {
        ImageView imageView;
    }

}
