package com.cheweishi.android.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;

import com.cheweishi.android.entity.NewsTypeResponse;
import com.cheweishi.android.entity.ShopTypeResponse;
import com.cheweishi.android.fragement.NewsPageFragment;
import com.cheweishi.android.fragement.ShopPageFragment;

import java.lang.ref.SoftReference;
import java.util.List;

/**
 * Created by tanck on 2016/08/07.
 */
public class ShopFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<ShopTypeResponse.MsgBean> list;

    private SparseArray<SoftReference<Fragment>> fragments = new SparseArray<>();

    private Fragment f;

    public ShopFragmentPagerAdapter(FragmentManager fm, Context context, List<ShopTypeResponse.MsgBean> list) {
        super(fm);
        this.list = list;
    }

    public void setData(List<ShopTypeResponse.MsgBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        if (null != fragments.get(position)) {
            f = fragments.get(position).get();
            if (null != f)
                return f;

        }
        f = ShopPageFragment.newInstance(position, list.get(position).getId(), list.get(position).getName());
        fragments.put(position, new SoftReference<Fragment>(f));
        return f;
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
