package com.cheweishi.android.thirdpart.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.cheweishi.android.R;
import com.cheweishi.android.thirdpart.holder.CBViewHolderCreator;
import com.cheweishi.android.thirdpart.holder.Holder;
import com.cheweishi.android.thirdpart.view.CBLoopViewPager;
import com.cheweishi.android.utils.LogHelper;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sai on 15/7/29.
 */
public class CBPageAdapter<T> extends PagerAdapter {
    protected List<T> mDatas;
    protected CBViewHolderCreator holderCreator;
    //    private View.OnClickListener onItemClickListener;
    private boolean canLoop = true;
    private CBLoopViewPager viewPager;
    private final int MULTIPLE_COUNT = 300;
    private Activity activity;
    private LinkedList<View> views = new LinkedList<>(); // 强引用即可.

    public int toRealPosition(int position) {
        int realCount = getRealCount();
        if (realCount == 0)
            return 0;
        int realPosition = position % realCount;
        return realPosition;
    }

    @Override
    public int getCount() {
        return canLoop ? getRealCount() * MULTIPLE_COUNT : getRealCount();
    }

    public int getRealCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        int realPosition = toRealPosition(position);
        if (0 < views.size()) {
            view = views.removeFirst();
        }
        view = getView(realPosition, view, container);
//        if(onItemClickListener != null) view.setOnClickListener(onItemClickListener);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
        views.add(view);
    }


    @Override
    public void finishUpdate(ViewGroup container) {
        int position = viewPager.getCurrentItem();
        if (position == 0) {
            position = viewPager.getFristItem();
        } else if (position == getCount() - 1) {
            position = viewPager.getLastItem();
        }
        try {
            viewPager.setCurrentItem(position, false);
        } catch (IllegalStateException e) {
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    public void setViewPager(CBLoopViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public CBPageAdapter(CBViewHolderCreator holderCreator, List<T> datas) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
    }

    public CBPageAdapter(CBViewHolderCreator holderCreator, List<T> datas, Activity activity) {
        this.holderCreator = holderCreator;
        this.mDatas = datas;
        this.activity = activity;
    }

    public void setData(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup container) {
        Holder holder = null;
        if (view == null) {
            holder = (Holder) holderCreator.createHolder();
            view = holder.createView(activity);
            view.setTag(R.id.cb_item_tag, holder);
        } else {
            holder = (Holder<T>) view.getTag(R.id.cb_item_tag);
        }
        if (mDatas != null && !mDatas.isEmpty())
            holder.UpdateUI(container.getContext(), position, mDatas.get(position));
        return view;
    }

//    public void setOnItemClickListener(View.OnClickListener onItemClickListener) {
//        this.onItemClickListener = onItemClickListener;
//    }
}
