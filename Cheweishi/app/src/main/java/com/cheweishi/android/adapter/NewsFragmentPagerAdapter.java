package com.cheweishi.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cheweishi.android.entity.NewsTypeResponse;
import com.cheweishi.android.fragement.NewsPageFragment;

import java.util.List;

/**
 * Created by Tanck on 2015/8/27.
 */
public class NewsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private List<NewsTypeResponse.MsgBean> list;

    public NewsFragmentPagerAdapter(FragmentManager fm, Context context, List<NewsTypeResponse.MsgBean> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return NewsPageFragment.newInstance(position,list.get(position).getId());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }

}
