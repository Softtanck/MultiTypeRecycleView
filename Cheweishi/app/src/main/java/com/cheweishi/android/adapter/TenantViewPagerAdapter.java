package com.cheweishi.android.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cheweishi.android.R;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.ServiceDetailResponse;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by tangce on 6/24/2016.
 */
public class TenantViewPagerAdapter extends PagerAdapter {

    private Context context;

    private List<ImageView> views;

    private List<ServiceDetailResponse.MsgBean.tenantImagesBean> list;

    public TenantViewPagerAdapter(Context context, List<ServiceDetailResponse.MsgBean.tenantImagesBean> list) {
        this.context = context;
        this.list = list;

        init();
    }

    private void init() {
        views = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            views.add(new ImageView(context));
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = views.get(position);
        XUtilsImageLoader.getxUtilsImageLoader(context, R.drawable.zhaochewei_img,
                imageView, list.get(position).getImage());
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
