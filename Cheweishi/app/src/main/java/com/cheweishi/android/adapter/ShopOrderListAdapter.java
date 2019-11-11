package com.cheweishi.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.ShopOrderDetailActivity;
import com.cheweishi.android.activity.ShopPayActivity;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.ShopOrderListResponse;
import com.cheweishi.android.entity.ShopPayOrderNative;
import com.cheweishi.android.utils.StringUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by tangce on 7/19/2016.
 */
public class ShopOrderListAdapter extends BaseAdapter {

    private Context context;

    private List<ShopOrderListResponse.MsgBean> list;

    private List<ShopPayOrderListAdapter> childAdapters;

    private OrderProcessListener listener;

    public void setListener(OrderProcessListener listener) {
        this.listener = listener;
    }

    public interface OrderProcessListener {
        /**
         * 对应订单取消的时候
         *
         * @param position
         */
        void onCancel(int position);

        /**
         * 对应订单确认收货
         *
         * @param position
         */
        void onReceive(int position);

        /**
         * 对应订单退货
         *
         * @param position
         */
        void onBackGoods(int position);

    }


    public ShopOrderListAdapter(Context context, List<ShopOrderListResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<ShopOrderListResponse.MsgBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    private void initChildAdapter(List<ShopOrderListResponse.MsgBean> parents) {
        if (null == parents || 0 == parents.size())
            return;
        childAdapters = new ArrayList<>();
        for (int i = 0; i < parents.size(); i++) {
            ShopOrderListResponse.MsgBean temp = parents.get(i);
            List<ShopPayOrderNative> orderNatives = new ArrayList<>();
            for (int j = 0; j < temp.getOrderItem().size(); j++) {
                ShopPayOrderNative orderNative = new ShopPayOrderNative();
                orderNative.setId(temp.getOrderItem().get(j).getId());
                orderNative.setNumber(temp.getOrderItem().get(j).getQuantity());
                orderNative.setMoney(temp.getOrderItem().get(j).getPrice());
                orderNative.setName(temp.getOrderItem().get(j).getName());
                orderNative.setIcon(temp.getOrderItem().get(j).getThumbnail());
                orderNatives.add(orderNative);
            }
            childAdapters.add(new ShopPayOrderListAdapter(context, orderNatives));
        }
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
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder = null;

        if (null == convertView) {
            holder = new ViewHolder();
            switch (getItemViewType(position)) {
                case 1: // head
                    convertView = View.inflate(context, R.layout.item_shop_order_list_head, null);
                    holder.time = (TextView) convertView.findViewById(R.id.tv_item_order_time);
                    holder.status = (TextView) convertView.findViewById(R.id.tv_item_order_status);
                    holder.detailHead = (RelativeLayout) convertView.findViewById(R.id.rl_item_order_detail_head);
                    break;
                case 2:// content
                    convertView = View.inflate(context, R.layout.item_shop_order_list_content, null);
                    holder.icon = (ImageView) convertView.findViewById(R.id.iv_sp_my_order_item);
                    holder.orderMoney = (TextView) convertView.findViewById(R.id.tv_sp_my_order_money);
                    holder.number = (TextView) convertView.findViewById(R.id.tv_sp_my_order_number);
                    holder.orderName = (TextView) convertView.findViewById(R.id.tv_sp_my_order_name);
                    holder.content = (RelativeLayout) convertView.findViewById(R.id.rl_sp_my_order_content);
                    break;
                case 3: // bottom
                    convertView = View.inflate(context, R.layout.item_shop_order_list_bottom, null);
                    holder.money = (TextView) convertView.findViewById(R.id.tv_item_order_money);
                    holder.cancel = (TextView) convertView.findViewById(R.id.tv_item_order_cancel);
                    holder.pay = (TextView) convertView.findViewById(R.id.tv_item_order_pay);
                    holder.detailBottom = (LinearLayout) convertView.findViewById(R.id.ll_item_order_detail_bottom);
                    holder.opt = (RelativeLayout) convertView.findViewById(R.id.rl_item_order_opt);
                    holder.back = (TextView) convertView.findViewById(R.id.tv_item_order_back);
                    holder.backGoods = (TextView) convertView.findViewById(R.id.tv_item_order_back_goods);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String tempPayStatus = list.get(position).getPaymentStatus();
        String tempOrderStatus = list.get(position).getOrderStatus();
        String tempShippingStatus = list.get(position).getShippingStatus();
        holder.setListener(position);
        switch (getItemViewType(position)) {
            case 1:// head

                holder.time.setText(context.getString(R.string.order_time) + getDate(list.get(position).getCreateDate()));
                /**
                 * 订单状态
                 未确认
                 unconfirmed,

                 已确认
                 confirmed,

                 已完成
                 completed,

                 已取消
                 cancelled,

                 已失效
                 failure
                 */
                if ("failure".equals(tempOrderStatus)) {
                    holder.status.setText(R.string.time_out_close);
                } else if ("unpaid".equals(tempPayStatus) && "cancelled".equals(tempOrderStatus)) {
                    holder.status.setText(R.string.cancel_order);
                } else if ("unpaid".equals(tempPayStatus)) {
                    holder.status.setText(R.string.unpaid);
                } else if ("paid".equals(tempPayStatus) && "unconfirmed".equals(tempOrderStatus)) {
                    holder.status.setText(R.string.paid);
                } else if ("paid".equals(tempPayStatus) && "confirmed".equals(tempOrderStatus)) {
                    holder.status.setText(R.string.unsend);
                } else if ("shipped".equals(tempShippingStatus)) {
                    holder.status.setText(R.string.unrec);
                } else if ("received".equals(tempShippingStatus)) {
                    holder.status.setText(R.string.received);
                } else {
                    holder.status.setText(R.string.not_no);
                }
                holder.detailHead.setOnClickListener(holder);
                break;
            case 2:// content
                int tempP = list.get(position).getOrderPosition();//获取orderItem真实位置
                String iconString = list.get(position).getOrderItem().get(tempP).getThumbnail();
                if (StringUtil.isEmpty(iconString))
                    holder.icon.setScaleType(ImageView.ScaleType.FIT_XY);
                XUtilsImageLoader.getHomeAdvImg(context, R.drawable.udesk_defualt_failure, holder.icon, iconString);
                holder.orderName.setText(list.get(position).getOrderItem().get(tempP).getName());
                holder.number.setText("x" + list.get(position).getOrderItem().get(tempP).getQuantity());
                holder.orderMoney.setText("￥" + list.get(position).getOrderItem().get(tempP).getPrice());
                holder.content.setOnClickListener(holder);
                break;
            case 3://bottom
                holder.money.setText("共" + list.get(position).getProductCount() + "件商品 实付:￥" + list.get(position).getAmount());
                holder.detailBottom.setOnClickListener(holder);
                if ("failure".equals(tempOrderStatus)) {//交易关闭
                    holder.opt.setVisibility(View.GONE);
                } else if ("unpaid".equals(tempPayStatus) && "cancelled".equals(tempOrderStatus)) {//没给钱,点击已取消
                    holder.opt.setVisibility(View.GONE);
                } else if ("unpaid".equals(tempPayStatus)) {//待付款
                    holder.opt.setVisibility(View.VISIBLE);
                    // TODO 展示取消及付款,隐藏退货,退款
                    holder.pay.setVisibility(View.VISIBLE);
                    holder.cancel.setVisibility(View.VISIBLE);
                    holder.back.setVisibility(View.GONE);
                    holder.backGoods.setVisibility(View.GONE);
                    holder.pay.setOnClickListener(holder);
                    holder.cancel.setOnClickListener(holder);
                } else if ("paid".equals(tempPayStatus) && "unconfirmed".equals(tempOrderStatus)) {//给了钱,没确认
                    holder.opt.setVisibility(View.VISIBLE);
                    // TODO 展示退款,隐藏取消及付款,退货
                    holder.cancel.setVisibility(View.GONE);
                    holder.pay.setVisibility(View.GONE);
                    holder.backGoods.setVisibility(View.GONE);
                    holder.back.setVisibility(View.VISIBLE);
                    holder.back.setOnClickListener(holder);
                } else if ("paid".equals(tempPayStatus) && "confirmed".equals(tempOrderStatus)) {//给了钱,且确认了
                    holder.opt.setVisibility(View.VISIBLE);
                    // TODO 展示退款,隐藏取消及付款,退货
                    holder.cancel.setVisibility(View.GONE);
                    holder.pay.setVisibility(View.GONE);
                    holder.back.setVisibility(View.VISIBLE);
                    holder.backGoods.setVisibility(View.GONE);
                    holder.back.setOnClickListener(holder);
                } else if ("shipped".equals(tempShippingStatus)) {//待收货
                    holder.opt.setVisibility(View.VISIBLE);
                    // TODO 展示退款,隐藏取消及付款,退货(后期可能会增加查看物流)
                    holder.cancel.setVisibility(View.GONE);
                    holder.pay.setVisibility(View.GONE);
                    holder.backGoods.setVisibility(View.GONE);
                    holder.back.setVisibility(View.VISIBLE);
                    holder.back.setOnClickListener(holder);
                } else if ("received".equals(tempShippingStatus)) {//收货了,待评价
                    holder.opt.setVisibility(View.VISIBLE);
                    // TODO 展示退货,隐藏取消及付款,退款
                    holder.back.setVisibility(View.GONE);
                    holder.cancel.setVisibility(View.GONE);
                    holder.pay.setVisibility(View.GONE);
                    holder.backGoods.setVisibility(View.VISIBLE);
                    holder.backGoods.setOnClickListener(holder);
                } else { // 等待确认
                    holder.opt.setVisibility(View.GONE);
                }

                break;
        }

        return convertView;
    }

    private class ViewHolder implements View.OnClickListener {
        private TextView time;
        private TextView status;
        private TextView money;
        private TextView cancel;
        private TextView pay;
        private TextView back;//退款
        private TextView backGoods;//退货

        private RelativeLayout detailHead;

        private LinearLayout detailBottom;

        private RelativeLayout opt;//底部可能需要隐藏

        //订单列表内容
        private ImageView icon;
        private TextView orderMoney;
        private TextView number;
        private TextView orderName;
        private RelativeLayout content;

        private int position;

        public void setListener(int p) {
            this.position = p;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_sp_my_order_content://content
                case R.id.ll_item_order_detail_bottom://detail
                case R.id.rl_item_order_detail_head:
                    Intent detail = new Intent(context, ShopOrderDetailActivity.class);
                    detail.putExtra("data", list.get(position));
                    context.startActivity(detail);
                    break;
                case R.id.tv_item_order_cancel:
                    if (null != listener)
                        listener.onCancel(position);
                    break;
                case R.id.tv_item_order_pay:
                    Intent pay = new Intent(context, ShopPayActivity.class);
                    pay.putExtra("orderId", String.valueOf(list.get(position).getId()));
                    pay.putExtra("money", list.get(position).getAmount());
                    context.startActivity(pay);
                    break;
                case R.id.tv_item_order_back_goods:// 退货
                    if (null != listener)
                        listener.onBackGoods(position);
                    break;
                case R.id.tv_item_order_back://退款
                    break;
//                case R.id.rl_sp_my_order_content://content
//                    Intent detail = new Intent(context, ProductDetailActivity.class);
//                    detail.putExtra("productId", list.get(position).getOrderItem().get(list.get(position).getOrderPosition()).getProduct().getId());
//                    context.startActivity(detail);
//                    break;
            }
        }

    }

    /**
     * 获取订单ids
     *
     * @param position
     * @return
     */
    public String getOrderId(int position) {
        int number = list.get(position).getOrderItem().size();
        StringBuffer orderIds = new StringBuffer();
        for (int i = 0; i < number; i++) {
            if (0 == i)
                orderIds.append(String.valueOf(list.get(position).getOrderItem().get(i).getId()));
            else
                orderIds.append("," + list.get(position).getOrderItem().get(i).getId());
        }
        return orderIds.toString();
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(time);// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }
}
