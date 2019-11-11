package com.cheweishi.android.adapter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.MaintainComponentActivity;
import com.cheweishi.android.activity.OrderDetailsActivity;
import com.cheweishi.android.activity.WashCarPayActivity;
import com.cheweishi.android.biz.JSONCallback;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ServiceListResponse;
import com.cheweishi.android.http.NetWorkHelper;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.ButtonUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 新版首页商家服务列表适配器
 *
 * @author mingdasen
 */
public class MianSellerServiceAdapater extends BaseAdapter {

    private Context mContext;
    private ServiceListResponse.MsgBean list;
    private int Serviceid;

    public MianSellerServiceAdapater(Context mContext,
                                     ServiceListResponse.MsgBean mainSellerInfo) {
        this.mContext = mContext;
        this.list = mainSellerInfo;
    }

    // TODO 写死只展示一个
    @Override
    public int getCount() {
        return StringUtil.isEmpty(list.getCarService()) ? 0 : list.getCarService().size();
    }

    @Override
    public Object getItem(int position) {
        return list.getCarService().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_seller_service, null);
            holder.tv_service_name = (TextView) convertView
                    .findViewById(R.id.tv_service_name);
            holder.tv_service_fPrice_name = (TextView) convertView
                    .findViewById(R.id.tv_service_fPrice_name);
            holder.tv_service_fPrice = (TextView) convertView
                    .findViewById(R.id.tv_service_fPrice);
            holder.tv_service_price = (TextView) convertView
                    .findViewById(R.id.tv_service_price);
            holder.tv_red_packets = (TextView) convertView.findViewById(R.id.tv_time);
            holder.btn_pay = (TextView) convertView.findViewById(R.id.btn_pay);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (!StringUtil.isEmpty(list)) {
            holder.tv_service_name.setText(list.getCarService().get(position).getServiceName());

//            double price = list.getCarService().get(position).getPrice();
            int id = list.getCarService().get(position).getCategoryId();
            switch (id){
                case 1: // 保养
                    holder.btn_pay
                            .setBackgroundResource(R.drawable.maintain_click_selector);
                    holder.btn_pay.setTextColor(mContext.getResources().getColor(
                            R.color.main_orange));
                    holder.btn_pay.setText("预约");
                    holder.tv_service_fPrice_name.setVisibility(View.GONE);
                    holder.tv_service_fPrice.setVisibility(View.GONE);
                    holder.tv_service_price.setVisibility(View.GONE);
                    holder.btn_pay.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //  TODO 预约
                            //快速点击忽略处理
                            if (ButtonUtils.isFastClick()) {
                                return;
                            }
                            Serviceid = list.getCarService().get(position).getService_id();
                            Intent intent = new Intent(mContext, MaintainComponentActivity.class);
                            intent.putExtra("serviceid", Serviceid);
                            intent.putExtra("serviceName", list.getCarService().get(position).getServiceName());
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 2: // 洗车
                    holder.btn_pay
                            .setBackgroundResource(R.drawable.pay_click_selector);
                    holder.btn_pay.setTextColor(mContext.getResources().getColor(
                            R.color.main_blue));
                    holder.btn_pay.setText("支付");
                    if (StringUtil.isEmpty(list.getCarService().get(position).getPromotion_price())) {
                        holder.tv_service_fPrice.setText("￥" + list.getCarService().get(position).getPrice());
                    } else {
                        holder.tv_service_fPrice.setText("￥" + list.getCarService().get(position).getPromotion_price());
                    }
                    holder.tv_service_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
                    holder.tv_service_price.setText("￥" + list.getCarService().get(position).getPrice());
                    holder.btn_pay.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //快速点击忽略处理
                            if (ButtonUtils.isFastClick()) {
                                return;
                            }
                            LogHelper.d("name:" + list.getTenantName());
                            // TODO 点击跳转到洗车界面
                            Intent intent = new Intent(mContext, WashCarPayActivity.class);
                            intent.putExtra("seller", list.getTenantName());
                            // TODO 不知道是什么
//					intent.putExtra("seller_id", mainSellerInfo.getId());
                            intent.putExtra("service", list.getCarService().get(position).getServiceName());
                            intent.putExtra("service_id", "" + list.getCarService().get(position).getService_id());
                            if (StringUtil.isEmpty(list.getCarService().get(position).getPromotion_price()) || StringUtil.isEquals("null", String.valueOf(list.getCarService().get(position).getPromotion_price()), true)) {
                                intent.putExtra("price", String.valueOf(list.getCarService().get(position).getPrice()));
                            } else {
                                intent.putExtra("price", String.valueOf(list.getCarService().get(position).getPromotion_price()));
                            }
                            // TODO 不知道是什么
//                    if (StringUtil.isEquals(list.get(position).getCate_id_2(), "30", true)) {
//                        intent.putExtra("type", "px");
//                    } else {
//                        intent.putExtra("type", "");
//                    }
                            mContext.startActivity(intent);
                        }
                    });
                    break;
                case 4: // 紧急救援
                    break;
                case 5: // 美容
                    holder.btn_pay
                            .setBackgroundResource(R.drawable.maintain_click_selector);
                    holder.btn_pay.setTextColor(mContext.getResources().getColor(
                            R.color.main_orange));
                    holder.btn_pay.setText("预约");
                    holder.tv_service_fPrice_name.setVisibility(View.GONE);
                    holder.tv_service_fPrice.setVisibility(View.GONE);
                    holder.tv_service_price.setVisibility(View.GONE);
                    holder.btn_pay.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            //  TODO 预约
                            //快速点击忽略处理
                            if (ButtonUtils.isFastClick()) {
                                return;
                            }
                            DateTimeSelectorDialogBuilder builder = DateTimeSelectorDialogBuilder.getInstance(mContext);
                            builder.setWheelViewVisibility(View.GONE);
                            builder.setOnSaveListener(new mYSaveListener());
                            builder.setSencondeCustomView(R.layout.yuyue_date_time_seletor, mContext);
                            builder.show();
                            Serviceid = list.getCarService().get(position).getService_id();
                        }
                    });
                    break;
                case 6: // 保险
                    break;
            }

            // TODO 暂时不忙显示出来
//			if (StringUtil.isEquals("0", list.get(position).getIsRed(), true)) {
//				holder.tv_red_packets.setVisibility(View.VISIBLE);
//			}else {
            holder.tv_red_packets.setVisibility(View.GONE);
//			}
        }
        return convertView;
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

                    Toast.makeText(mContext, "预约时间必须大于当前日期", Toast.LENGTH_SHORT).show();
                } else {
                    subscript(Serviceid, selectedDate);
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

        ProgrosDialog.openDialog(mContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.SUBSCRIBE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", ((BaseActivity) mContext).getUserId());
        param.put("token", ((BaseActivity) mContext).getToken());
        param.put("serviceId", id);
        param.put("subscribeDate", date);
//        param.put("price", price);
//        LogHelper.d(id + "----" + price);
        NetWorkHelper.getInstance(mContext).PostJson(url, param, new JSONCallback() {
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
                    Toast.makeText(mContext, response.getDesc(), Toast.LENGTH_SHORT).show();
                    return;
                }


                Intent intent = new Intent(mContext, OrderDetailsActivity.class);
                intent.putExtra("recordId", response.getDesc());
                mContext.startActivity(intent);
                ((BaseActivity) mContext).setUserToken(response.getToken());
            }

            @Override
            public void error(String errorMsg) {
                ProgrosDialog.closeProgrosDialog();
            }
        });
    }

    class ViewHolder {
        private TextView tv_service_name;
        private TextView tv_service_fPrice_name;
        private TextView tv_service_fPrice;
        private TextView tv_service_price;
        private TextView btn_pay;
        private TextView tv_red_packets;
    }
}
