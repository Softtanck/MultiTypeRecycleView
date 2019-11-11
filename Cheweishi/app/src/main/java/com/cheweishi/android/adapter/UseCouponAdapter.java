package com.cheweishi.android.adapter;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.PayCouponListResponse;
import com.cheweishi.android.entity.PreparePayCouponResponse;

import java.util.List;

/**
 * Created by tangce on 5/17/2016.
 */
public class UseCouponAdapter extends BaseAdapter {

    private Context context;

    private List<PreparePayCouponResponse.MsgBean.CouponListBean> list;

    private OnUserClickCouponListener listener;

    public interface OnUserClickCouponListener {
        /**
         * 当选择了一个Item后callback
         *
         * @param position
         */
        void onCheckCoupon(int position);
    }


    public void setOnUserClickCouponListener(OnUserClickCouponListener listener) {
        this.listener = listener;
    }

    public UseCouponAdapter(Context context, List<PreparePayCouponResponse.MsgBean.CouponListBean> list) {
        this.context = context;
        this.list = list;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_use_coupon, null);
            holder.left = (LinearLayout) convertView.findViewById(R.id.ll_use_coupon_left);
            holder.right = (LinearLayout) convertView.findViewById(R.id.ll_use_coupon_right);
            holder.money = (TextView) convertView.findViewById(R.id.tv_use_coupon_money);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_use_coupon_desc);
            holder.check = (ImageView) convertView.findViewById(R.id.iv_use_coupon_check);
            holder.type = (TextView) convertView.findViewById(R.id.tv_use_coupon_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.money.setText(list.get(position).getCoupon().getAmount() + "元");
//        holder.desc.setText(list.get(position).getCoupon().getRemark());
        // 根据类型展示不同的背景
        if (null != list.get(position).getCoupon().getType() && "COMMON".equals(list.get(position).getCoupon().getType())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.left.setBackground(context.getResources().getDrawable(R.drawable.b_item_coupon_left));
                holder.right.setBackground(context.getResources().getDrawable(R.drawable.b_item_coupon_right));
                holder.type.setText("通用优惠券");
                holder.desc.setText("全场通用优惠劵");
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.left.setBackground(context.getResources().getDrawable(R.drawable.b_item_coupon_pink_left));
                holder.right.setBackground(context.getResources().getDrawable(R.drawable.b_item_coupon_pink_right));
                holder.type.setText("特殊优惠券");
                holder.desc.setText("特殊优惠劵");
            }
        }

        // 判断是否点击
        if (list.get(position).isCheck()) {
            holder.check.setImageResource(R.drawable.mess_yuan_click);
        } else {
            holder.check.setImageResource(R.drawable.mess_yuan);
        }

        holder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(position);
            }
        });
        return convertView;
    }

    private void setCheck(int p) {
        if (p == getCheckCouponPosition()) {
            list.get(p).setCheck(false);
            notifyDataSetChanged();
            if (null != listener)
                listener.onCheckCoupon(-1);
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck())
                list.get(i).setCheck(false);
        }
        list.get(p).setCheck(true);
        notifyDataSetChanged();

        if (null != listener)
            listener.onCheckCoupon(p);
    }

    public void setPositionChecked(int p,boolean result){
        list.get(p).setCheck(result);
        notifyDataSetChanged();
    }

    public int getCheckCouponPosition() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isCheck())
                return i;
        }
        return -1;
    }

    private class ViewHolder {
        private LinearLayout left;
        private LinearLayout right;
        private TextView money;
        private TextView desc;
        private ImageView check;
        private TextView type;
    }
}
