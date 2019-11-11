package com.cheweishi.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.MaintainComponentActivity;
import com.cheweishi.android.activity.OrderDetailsActivity;
import com.cheweishi.android.activity.WashCarPayActivity;
import com.cheweishi.android.biz.JSONCallback;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.StoreListResponse;
import com.cheweishi.android.http.NetWorkHelper;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder;
import com.lidroid.xutils.http.ResponseInfo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by tangce on 7/14/2016.
 */
public class StoreListAdapter extends BaseAdapter {

    private Context context;

    private List<StoreListResponse.MsgBean> list;

    private String currentType = "2";//默认洗车

    private int mServiceId;

    public StoreListAdapter(Context context, List<StoreListResponse.MsgBean> list) {
        this.context = context;
        this.list = list;
        initView();
    }

    private ImageView initView() {
        ImageView rateImage = new ImageView(context);
        rateImage.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT));
        rateImage.setPadding(0, 0, 2, 0);
        rateImage.setScaleType(ImageView.ScaleType.FIT_XY);
        rateImage.setImageResource(R.drawable.haoping);
        return rateImage;
    }

    public void setData(List<StoreListResponse.MsgBean> list, String type) {
        this.list = list;
        this.currentType = type;
        notifyDataSetChanged();
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
            convertView = View.inflate(context, R.layout.item_store_list, null);
            holder.tenantName = (TextView) convertView.findViewById(R.id.tv_store_item_tenant_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_store_item);
            holder.praiseRate = (LinearLayout) convertView.findViewById(R.id.ll_store_item_rate);
            holder.rateCount = (TextView) convertView.findViewById(R.id.tv_store_item_rateCount);
            holder.rateNumber = (TextView) convertView.findViewById(R.id.tv_store_item_rate_number);
            holder.distance = (TextView) convertView.findViewById(R.id.tv_store_item_distance);
            holder.money = (TextView) convertView.findViewById(R.id.tv_store_item_money);
            holder.showMoney = (TextView) convertView.findViewById(R.id.tv_store_item_all_money);
            holder.pay = (TextView) convertView.findViewById(R.id.tv_store_item_buy);
            holder.address = (TextView) convertView.findViewById(R.id.tv_store_item_address);
            holder.serviceDesc = (TextView) convertView.findViewById(R.id.tv_store_item_wash_desc);
            holder.moneyDesc = (TextView) convertView.findViewById(R.id.tv_store_item_all_money_desc);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        XUtilsImageLoader.getxUtilsImageLoader(context, R.drawable.udesk_defalut_image_loading, R.drawable.udesk_defualt_failure, holder.icon, list.get(position).getPhoto());

        holder.tenantName.setText(list.get(position).getTenantName());
        holder.praiseRate.removeAllViews();
        // TODO 添加钻石
        for (int i = 0; i < list.get(position).getPraiseRate(); i++) {
            holder.praiseRate.addView(initView());
        }

        holder.rateNumber.setText(list.get(position).getPraiseRate() + "分");
        holder.rateCount.setText(list.get(position).getRateCounts() + "条评论");
        if (!StringUtil.isEmpty(list.get(position).getDistance())) {
            holder.distance.setVisibility(View.VISIBLE);
            holder.distance.setText(list.get(position).getDistance() + "km");
        } else {
            holder.distance.setVisibility(View.GONE);
        }
        holder.address.setText(list.get(position).getAddress());
        StoreListResponse.MsgBean.CarServiceBean carServiceBean = list.get(position).getCarService();
        if (null != carServiceBean) { // 证明是洗车服务
            holder.money.setVisibility(View.VISIBLE);
            holder.showMoney.setVisibility(View.VISIBLE);
            holder.moneyDesc.setVisibility(View.VISIBLE);
            holder.pay.setVisibility(View.VISIBLE);
            holder.serviceDesc.setVisibility(View.VISIBLE);
            holder.pay.setBackgroundResource(R.drawable.pay_click_selector);
            holder.pay.setTextColor(context.getResources().getColor(R.color.main_blue));
            holder.pay.setText("支付");
            holder.serviceDesc.setText(list.get(position).getCarService().getServiceName());
            holder.money.setText("￥" + list.get(position).getCarService().getPrice());
            holder.showMoney.setText("￥" + list.get(position).getCarService().getPromotion_price());
            holder.pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO 购买
                    /**
                     * 快速点击忽略处理
                     */
                    if (ButtonUtils.isFastClick()) {
                        return;
                    }
                    Intent intent = new Intent(context, WashCarPayActivity.class);
                    intent.putExtra("seller", list.get(position).getTenantName());
                    intent.putExtra("service", list.get(position).getCarService().getServiceName());
                    intent.putExtra("service_id", "" + list.get(position).getCarService().getService_id());
                    if (StringUtil.isEmpty(list.get(position).getCarService().getPromotion_price()) || StringUtil.isEquals("null", String.valueOf(list.get(position).getCarService().getPromotion_price()), true)) {
                        intent.putExtra("price", String.valueOf(list.get(position).getCarService().getPrice()));
                    } else {
                        intent.putExtra("price", String.valueOf(list.get(position).getCarService().getPromotion_price()));
                    }
                    context.startActivity(intent);
                }
            });
        } else { // 不是洗车服务
            holder.serviceDesc.setVisibility(View.GONE);
            holder.money.setVisibility(View.GONE);
            holder.showMoney.setVisibility(View.GONE);
            holder.moneyDesc.setVisibility(View.GONE);
            holder.pay.setVisibility(View.GONE);
//            switch (currentType) {
//                case "1": // 保养
//                    holder.pay.setBackgroundResource(R.drawable.maintain_click_selector);
//                    holder.pay.setTextColor(context.getResources().getColor(R.color.main_orange));
//                    holder.serviceDesc.setText("保养");
//                    holder.pay.setText("预约");
//                    holder.pay.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            int id = list.get(position).getCarService().getService_id();
//                            Intent intent = new Intent(context, MaintainComponentActivity.class);
//                            intent.putExtra("serviceid", id);
//                            intent.putExtra("serviceName", "常规保养");
//                            context.startActivity(intent);
//                        }
//                    });
//                    break;
//                case "5"://美容
//                    holder.serviceDesc.setText("美容");
//                    holder.pay.setBackgroundResource(R.drawable.maintain_click_selector);
//                    holder.pay.setTextColor(context.getResources().getColor(R.color.main_orange));
//                    holder.pay.setText("预约");
//                    holder.pay.setOnClickListener(new View.OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            //  TODO 预约
//                            DateTimeSelectorDialogBuilder builder = DateTimeSelectorDialogBuilder.getInstance(context);
//                            builder.setWheelViewVisibility(View.GONE);
//                            builder.setOnSaveListener(new mYSaveListener());
//                            builder.setSencondeCustomView(R.layout.yuyue_date_time_seletor, context);
//                            builder.show();
//                            mServiceId = list.get(position).getCarService().getService_id();
//                        }
//                    });
//                    break;
//            }
        }
        return convertView;
    }


    private class ViewHolder {
        private ImageView icon;
        private TextView tenantName;
        private LinearLayout praiseRate;
        private TextView rateCount; // 评论数量
        private TextView rateNumber;//评分
        private TextView address;
        private TextView distance;
        private TextView money;
        private TextView showMoney;
        private TextView pay;
        private TextView serviceDesc;
        private TextView moneyDesc;
    }


    private class mYSaveListener implements DateTimeSelectorDialogBuilder.OnSaveListener {

        @Override
        public void onSaveSelectedDate(String selectedDate) {
            LogHelper.d("onSaveSelectedDate:" + selectedDate);

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
                    Locale.CHINA);
            try {
                Date date1 = sf.parse(selectedDate);// 选择时间
                Date date = new Date(); // 当前时间
                String dateStr = sf.format(date);
                date = sf.parse(dateStr);
                if (date1.getTime() < date.getTime()) {

                    Toast.makeText(context, "预约时间必须大于当前日期", Toast.LENGTH_SHORT).show();
                } else {
                    subscript(mServiceId, selectedDate);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 预约发包
     *
     * @param id
     */
    private void subscript(int id, String date) {

        ProgrosDialog.openDialog(context);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.SUBSCRIBE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", ((BaseActivity) context).getUserId());
        param.put("token", ((BaseActivity) context).getToken());
        param.put("serviceId", id);
        param.put("subscribeDate", date);
        NetWorkHelper.getInstance(context).PostJson(url, param, new JSONCallback() {
            @Override
            public void receive(int type, String data) {

            }

            @Override
            public void downFile(int type, ResponseInfo<File> arg0) {

            }

            @Override
            public void receive(String TAG, String data) {

            }

            @Override
            public void receive(String data) {
                ProgrosDialog.closeProgrosDialog();
                BaseResponse response = (BaseResponse) GsonUtil.getInstance().convertJsonStringToObject(data, BaseResponse.class);
                if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
                    Toast.makeText(context, response.getDesc(), Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra("recordId", response.getDesc());
                context.startActivity(intent);
                ((BaseActivity) context).setUserToken(response.getToken());
            }

            @Override
            public void error(String errorMsg) {
                ProgrosDialog.closeProgrosDialog();
            }
        });
    }
}
