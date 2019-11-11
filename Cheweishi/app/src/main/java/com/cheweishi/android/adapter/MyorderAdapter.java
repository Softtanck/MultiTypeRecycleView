package com.cheweishi.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaskOrderActivity;
import com.cheweishi.android.activity.OrderDetailsActivity;
import com.cheweishi.android.activity.WashCarPayActivity;
import com.cheweishi.android.entity.OrderResponse;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyorderAdapter extends BaseAdapter {
    private List<OrderResponse.MsgBean> mData;
    private Context mContext;
    private LayoutInflater mInflater;

    public MyorderAdapter(List<OrderResponse.MsgBean> mData, Context mContext) {
        super();
        this.mData = mData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void setData(List<OrderResponse.MsgBean> mData) {
//        this.mData.clear();
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int arg0) {

        return mData == null ? null : mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {

        return arg0;
    }


    /**
     * 把毫秒转化成日期
     *
     * @param millSec(毫秒数)
     * @return
     */
    private String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        ViewHolder holder = null;
        if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = mInflater.inflate(R.layout.item_my_order, null);

            holder.btn_order_comment = (TextView) arg1
                    .findViewById(R.id.btn_order_comment);
            holder.btn_order_detail = (TextView) arg1
                    .findViewById(R.id.btn_order_detail);
            holder.img_order = (ImageView) arg1.findViewById(R.id.img_order);
            holder.tv_order_class = (TextView) arg1
                    .findViewById(R.id.tv_order_class);
            holder.tv_order_class_name = (TextView) arg1
                    .findViewById(R.id.tv_order_class_name);
            holder.tv_shopping = (TextView) arg1.findViewById(R.id.tv_shopping);
            holder.tv_order_owner_name = (TextView) arg1
                    .findViewById(R.id.tv_order_owner_name);
            holder.tv_money = (TextView) arg1.findViewById(R.id.tv_money);
            holder.tv_order_price = (TextView) arg1
                    .findViewById(R.id.tv_order_price);
            holder.tv_time = (TextView) arg1.findViewById(R.id.tv_time);
            holder.tv_order_time = (TextView) arg1
                    .findViewById(R.id.tv_order_time);
            arg1.setTag(holder);
            holder.btn_order_comment.setTag(arg0);
            holder.btn_order_detail.setTag(arg0);

        } else {
            holder = (ViewHolder) arg1.getTag();
            holder.btn_order_detail.setTag(arg0);
            holder.btn_order_comment.setTag(arg0);
        }

        // TODO 自己服务器参数
        /**    预约
         RESERVATION,
         未支付
         UNPAID,
         已支付
         PAID
         */

        // TODO 最新状态
        /*** 预约中
         RESERVATION,
         预约成功
         RESERVATION_SUCCESS,
         预约失败
         RESERVATION_FAIL,
         正在服务
         IN_SERVICE,
         未支付
         UNPAID,
         已支付
         PAID,
         完成
         FINISH,
         过期
         OVERDUE,
         */


        if (mData.get(arg0).getChargeStatus().equals("RESERVATION")) {// 预约中
            holder.tv_order_class_name.setText("预约中");
            holder.tv_time.setText("预约时间");
            holder.tv_order_time.setText(""
                    + transferLongToDate(mData.get(arg0).getCreateDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.service_green));
            holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);

            // holder.tv_order_class.setText("现代朗动保养");
            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // TODO 查看详情
                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);
                }
            });
        } else if (mData.get(arg0).getChargeStatus().equals("RESERVATION_SUCCESS")) { // 预约成功
            holder.tv_order_class_name.setText("预约成功");
            holder.tv_time.setText("预约时间");
            holder.tv_order_time.setText(""
                    + transferLongToDate(mData.get(arg0).getCreateDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.service_orange));
            holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);

            // holder.tv_order_class.setText("现代朗动保养");
            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO 查看详情
                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);
                }
            });
        } else if (mData.get(arg0).getChargeStatus().equals("RESERVATION_FAIL")) { // 预约失败
            holder.tv_order_class_name.setText("预约失败");
            holder.tv_time.setText("预约时间");
            holder.tv_order_time.setText(""
                    + transferLongToDate(mData.get(arg0).getCreateDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.viewfinder_mask));
            holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);

            // holder.tv_order_class.setText("现代朗动保养");
            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO 查看详情
                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);
                }
            });

        } else if (mData.get(arg0).getChargeStatus().equals("IN_SERVICE")) { // 正在服务
            holder.tv_order_class_name.setText("正在享受服务中");
            holder.tv_time.setText("下单时间");
            holder.tv_order_time.setText("" + transferLongToDate(mData.get(arg0).getCreateDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.service_orange));
            holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);

            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO 查看详情
                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);
                }
            });

        } else if (mData.get(arg0).getChargeStatus().equals("UNPAID")) {// 未支付
            double price = mData.get(arg0).getPrice();
            if (-1 == price && 5 == mData.get(arg0).getCarService().getServiceCategory().getId()) { // 价格为-1,且为美容
                holder.tv_order_class_name.setText("价格面谈中");
                holder.tv_time.setText("下单时间");
                holder.tv_order_time.setText("" + transferLongToDate(mData.get(arg0).getCreateDate()));
                holder.tv_order_class_name.setTextColor(mContext.getResources()
                        .getColor(R.color.service_orange));
                holder.btn_order_comment.setVisibility(View.GONE);
                holder.btn_order_detail.setVisibility(View.VISIBLE);

                holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO 查看详情
                        Intent intent = new Intent();
                        intent.setClass(mContext, OrderDetailsActivity.class);
                        intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                        mContext.startActivity(intent);
                    }
                });
            } else {
                holder.tv_order_class_name.setText("未付款");
                holder.tv_time.setText("下单时间");
                holder.tv_order_time
                        .setText("" + transferLongToDate(mData.get(arg0).getCreateDate()));
                holder.tv_order_class_name.setTextColor(mContext.getResources()
                        .getColor(R.color.red));
                holder.btn_order_comment.setVisibility(View.VISIBLE);
                holder.btn_order_detail.setVisibility(View.GONE);
                holder.btn_order_comment.setText("确认付款");
                holder.btn_order_comment.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg1) {
                        // TODO 付款

                        Intent intent = new Intent(mContext, WashCarPayActivity.class);
                        intent.putExtra("seller", mData.get(arg0).getTenantName());
                        // TODO 不知道是什么
//					intent.putExtra("seller_id", mainSellerInfo.getId());
                        intent.putExtra("service", mData.get(arg0).getCarService().getServiceName());
                        //TODO 服务id暂时获取不了.
                        intent.putExtra("service_id", String.valueOf(mData.get(arg0).getCarService().getId()));
                        intent.putExtra("recordId", String.valueOf(mData.get(arg0).getId()));
                        intent.putExtra("price", String.valueOf(mData.get(arg0).getPrice()));
                        // TODO 不知道是什么
//                    if (StringUtil.isEquals(list.get(position).getCate_id_2(), "30", true)) {
//                        intent.putExtra("type", "px");
//                    } else {
//                        intent.putExtra("type", "");
//                    }
                        mContext.startActivity(intent);
                    }
                });
            }
        } else if (mData.get(arg0).getChargeStatus().equals("PAID")) { // 已支付
            holder.tv_order_class_name.setText("已支付");
            holder.tv_time.setText("支付时间");
            holder.tv_order_time.setText(""
                    + transferLongToDate(mData.get(arg0).getCreateDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.main_text_blue));
            holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);

            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO 查看详情
                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);
                }
            });
        } else if (mData.get(arg0).getChargeStatus().equals("FINISH")) {// 订单完成
            holder.tv_order_class_name.setText("已完成");
            holder.tv_time.setText("完成时间");
            holder.tv_order_time.setText("" + transferLongToDate(mData.get(arg0).getPaymentDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.main_orange));
            if (null == mData.get(arg0).getTenantEvaluate()) {
                holder.btn_order_comment.setVisibility(View.VISIBLE);
                holder.btn_order_comment.setText(R.string.btn_evaluate);
            } else
                holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);
            holder.btn_order_comment.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // TODO 评价
                    int po = Integer.valueOf(String.valueOf(arg0.getTag()));
                    Intent intent = new Intent();
                    intent.setClass(mContext, BaskOrderActivity.class);
                    intent.putExtra("price", "" + mData.get(po).getPrice());
                    intent.putExtra("tenantname", mData.get(po).getTenantName());
                    intent.putExtra("servicename", mData.get(po).getCarService().getServiceName());
                    intent.putExtra("tenantID", mData.get(po).getTenantID());
                    intent.putExtra("tenantPhoto", mData.get(po).getTenantPhoto());
                    intent.putExtra("recordId", String.valueOf(mData.get(po).getId()));
                    mContext.startActivity(intent);
                }
            });
            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // TODO 详情

                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("chargeStatus", mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getChargeStatus());
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);

                }
            });

        } else if (mData.get(arg0).getChargeStatus().equals("OVERDUE")) {// 过期
            holder.tv_order_class_name.setText("已过期");
            holder.tv_time.setText("过期时间");
            holder.tv_order_time.setText(""
                    + transferLongToDate(mData.get(arg0).getCreateDate()));
            holder.tv_order_class_name.setTextColor(mContext.getResources()
                    .getColor(R.color.gray_pressed));
            holder.btn_order_comment.setVisibility(View.GONE);
            holder.btn_order_detail.setVisibility(View.VISIBLE);
            holder.btn_order_detail.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    // TODO 详情

                    Intent intent = new Intent();
                    intent.setClass(mContext, OrderDetailsActivity.class);
                    intent.putExtra("chargeStatus", mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getChargeStatus());
                    intent.putExtra("recordId", String.valueOf(mData.get(Integer.valueOf(String.valueOf(arg0.getTag()))).getId()));
                    mContext.startActivity(intent);

                }
            });
        }
        // 0订单完成1订单进行中2已取消3NO
//        if (mData.get(arg0).getChargeStatus().equals("0")) {// 订单已取消
//            holder.tv_order_class_name.setText("已取消");
//            holder.tv_time.setText("取消时间");
//            holder.tv_order_time.setText(""
//                    + mData.get(arg0).getFinished_time());
//            holder.tv_order_class_name.setTextColor(mContext.getResources()
//                    .getColor(R.color.gray_pressed));
//            holder.btn_order_comment.setVisibility(View.GONE);
//            holder.btn_order_detail.setVisibility(View.GONE);
//
//            // holder.tv_order_class.setText("现代朗动保养");
//            holder.btn_order_detail.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, CancelOrderActivity.class);
//                    mContext.startActivity(intent);
//
//                }
//            });
//        } else if (mData.get(arg0).getStatus().equals("12")) {// 订单进行中
//            holder.tv_time.setText("下单时间");
//
//            holder.tv_order_class_name.setText("进行中");
//            holder.tv_order_time.setText("" + mData.get(arg0).getAdd_time());
//            holder.tv_order_class_name.setTextColor(mContext.getResources()
//                    .getColor(R.color.green_deep));
//            holder.btn_order_comment.setVisibility(View.GONE);
//            holder.btn_order_detail.setVisibility(View.GONE);
//            holder.btn_order_detail.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, OrderDetailsActivity.class);
//                    mContext.startActivity(intent);
//
//                }
//            });
//        } else if (mData.get(arg0).getStatus().equals("1")) {// 确认付款
//            holder.tv_order_class_name.setText("未付款");
//            holder.tv_time.setText("下单时间");
//            holder.tv_order_time
//                    .setText("" + mData.get(arg0).getAdd_time());
//            holder.tv_order_class_name.setTextColor(mContext.getResources()
//                    .getColor(R.color.red));
//            holder.btn_order_comment.setVisibility(View.VISIBLE);
//            holder.btn_order_detail.setVisibility(View.GONE);
//            holder.btn_order_comment.setText("确认付款");
//            holder.btn_order_comment.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg1) {
//                    Intent intent = new Intent();
//                    intent.putExtra("seller", mData.get(arg0).getSeller_name());
//                    intent.putExtra("service", mData.get(arg0).getGoods_name());
//                    intent.putExtra("price", mData.get(arg0).getPrice());
//                    if (mData.get(arg0).getCate_id_2().equals("30")) {
//                        intent.putExtra("type", "px");
//                    } else {
//                        intent.putExtra("type", "npx");
//                    }
//                    intent.putExtra("order_sn", mData.get(arg0).getOrder_sn());
//                    intent.putExtra("seller_id", mData.get(arg0).getSeller_id());
//                    intent.setClass(mContext, WashCarPayActivity.class);
//                    mContext.startActivity(intent);
//                }
//            });
//        } else if (mData.get(arg0).getStatus().equals("3")) {// 订单完成
//            holder.tv_order_class_name.setText("已完成");
//            holder.tv_time.setText("支付时间");
//            holder.tv_order_time.setText(""
//                    + mData.get(arg0).getFinished_time());
//            holder.tv_order_class_name.setTextColor(mContext.getResources()
//                    .getColor(R.color.main_orange));
//            holder.btn_order_comment.setVisibility(View.GONE);
//            holder.btn_order_detail.setVisibility(View.GONE);
//            holder.btn_order_comment.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, BaskOrderActivity.class);
//                    mContext.startActivity(intent);
//                }
//            });
//        } else {// 过期
//            holder.tv_order_class_name.setText("已过期");
//            holder.tv_time.setText("过期时间");
//            holder.tv_order_time.setText(""
//                    + mData.get(arg0).getFinished_time());
//            holder.tv_order_class_name.setTextColor(mContext.getResources()
//                    .getColor(R.color.gray_pressed));
//            holder.btn_order_comment.setVisibility(View.GONE);
//            holder.btn_order_detail.setVisibility(View.GONE);
//            holder.btn_order_comment.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, BaskOrderActivity.class);
//                    mContext.startActivity(intent);
//                }
//            });
//        }
        if (2 == mData.get(arg0).getCarService().getServiceCategory().getId()) {// 洗车
            holder.img_order.setBackgroundResource(R.drawable.xiche);
        } else if (1 == mData.get(arg0).getCarService().getServiceCategory().getId()) {// 保养
            holder.img_order.setBackgroundResource(R.drawable.baoyang);
        } else if (5 == mData.get(arg0).getCarService().getServiceCategory().getId()) {// 美容
            holder.img_order.setBackgroundResource(R.drawable.meirong);
        } else if (4 == mData.get(arg0).getCarService().getServiceCategory().getId()) {
            holder.img_order.setBackgroundResource(R.drawable.jinjijiuyuan); // 紧急救援
        } else if (6 == mData.get(arg0).getCarService().getServiceCategory().getId()) { // 保险
            holder.img_order.setBackgroundResource(R.drawable.jinjijiuyuan); // 紧急救援
        }
//        }else{
//            holder.img_order.setBackgroundResource(R.drawable.feiyong_baoxian2x); // 保险
//        }


        holder.tv_order_owner_name.setText(mData.get(arg0).getTenantName());
        String priceTemp = null;
        double price = mData.get(arg0).getPrice();
        if (-1 == price) { // 预约未定价
            priceTemp = "当前服务暂时未定价";
        } else {
            priceTemp = "￥" + price;
        }
        holder.tv_order_price.setText(priceTemp);
        String temp = mData.get(arg0).getCarService().getServiceName();
        if (null == temp)
            temp = mData.get(arg0).getCarService().getServiceCategory().getCategoryName();
        holder.tv_order_class.setText(temp);

        return arg1;

    }

    class ViewHolder {
        @ViewInject(R.id.img_order)
        private ImageView img_order;
        @ViewInject(R.id.tv_order_class)
        private TextView tv_order_class;
        @ViewInject(R.id.tv_order_class_name)
        private TextView tv_order_class_name;
        @ViewInject(R.id.tv_order_owner_name)
        private TextView tv_order_owner_name;
        @ViewInject(R.id.tv_order_price)
        private TextView tv_order_price;
        @ViewInject(R.id.tv_order_time)
        private TextView tv_order_time;
        @ViewInject(R.id.tv_shopping)
        private TextView tv_shopping;
        @ViewInject(R.id.tv_money)
        private TextView tv_money;
        @ViewInject(R.id.tv_time)
        private TextView tv_time;
        private TextView tv_order_class_name_overdue;
        @ViewInject(R.id.btn_order_comment)
        private TextView btn_order_comment;
        @ViewInject(R.id.btn_order_detail)
        private TextView btn_order_detail;
    }

}
