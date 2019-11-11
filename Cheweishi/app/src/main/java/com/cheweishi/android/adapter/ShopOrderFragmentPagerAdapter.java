package com.cheweishi.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.cheweishi.android.entity.NewsTypeResponse;
import com.cheweishi.android.fragement.NewsPageFragment;
import com.cheweishi.android.fragement.OrderPageFragment;

import java.util.List;

/**
 * Created by Tanck on 2015/8/27.
 */
public class ShopOrderFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    private List<String> list;

    public ShopOrderFragmentPagerAdapter(FragmentManager fm, Context context, List<String> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return OrderPageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }

}
