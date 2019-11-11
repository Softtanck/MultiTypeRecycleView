package com.cheweishi.android.adapter;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.cheweishi.android.activity.MaintainComponentActivity;
import com.cheweishi.android.activity.MaintainDetailsActivity;
import com.cheweishi.android.activity.SoSActivity;
import com.cheweishi.android.biz.JSONCallback;
import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.OrderDetailsActivity;
import com.cheweishi.android.activity.WashCarPayActivity;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.ServiceDetailResponse;
import com.cheweishi.android.http.NetWorkHelper;
import com.cheweishi.android.response.BaseResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.widget.DateSelectorDialogBuilder;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder;
import com.cheweishi.android.widget.UnSlidingListView;
import com.lidroid.xutils.http.ResponseInfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {
    private String type;
    private List<ServiceDetailResponse.MsgBean.CarServicesBean> washCar;
    LayoutInflater mInflater;
    Context context;
    private String tenantName;
    private int Serviceid; // 服务id
    private ServiceDetailResponse.MsgBean msgBean;//

    public ExpandableListViewAdapter(Context context, ServiceDetailResponse.MsgBean washCar, String tenantName) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
        this.washCar = washCar.getCarServices();
        this.tenantName = tenantName;
        this.msgBean = washCar;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return washCar.get(groupPosition).getSubServices().get(childPosition);// child[groupPosition][childPosition];
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 拨打电话
     */
    public void turnToPhone(String phoneNumber) {
        if (null == phoneNumber) {
            ((BaseActivity) context).showToast("当前商家没有提供电话号码");
            return;
        }
        Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                + phoneNumber));
        tel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(tel);
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = mInflater.inflate(R.layout.expandablelistview_item,
                    null);
            // mViewChild.gridView = (UnSlidingListView) convertView
            // .findViewById(R.id.channel_item_child_listview);

            mViewChild.tv_item_child_name = (TextView) convertView
                    .findViewById(R.id.tv_item_child_name);
            mViewChild.tv_item_child_discount_showOrNot = (TextView) convertView
                    .findViewById(R.id.tv_item_child_discount_showOrNot);
            mViewChild.tv_discount_price_remind = (TextView) convertView
                    .findViewById(R.id.tv_discount_price_remind);
            mViewChild.tv_discount_price = (TextView) convertView
                    .findViewById(R.id.tv_discount_price);
            mViewChild.tv_original_price = (TextView) convertView
                    .findViewById(R.id.tv_original_price);
            mViewChild.btn_pay = (TextView) convertView
                    .findViewById(R.id.btn_pay);
            mViewChild.tv_original_price.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }
        String tempServiceName = washCar.get(groupPosition).getSubServices().get(childPosition).getServiceName();
        if (null != tempServiceName)
            mViewChild.tv_item_child_name.setText(tempServiceName);
        else {
            mViewChild.tv_item_child_name.setText("");
        }
        double price = washCar.get(groupPosition).getSubServices().get(childPosition).getPrice();
//        mViewChild.tv_original_price.setText("￥");


        int id = washCar.get(groupPosition).getCategoryId();
        switch (id) {
            case 1: // 保养
                mViewChild.btn_pay.setBackgroundResource(R.drawable.maintain_click_selector);
                mViewChild.btn_pay.setTextColor(context.getResources().getColor(
                        R.color.main_orange));
                mViewChild.btn_pay.setText("预约");
                mViewChild.btn_pay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO 预约,因为要展示,所以暂时注释,调用预约接口

                        Serviceid = washCar.get(groupPosition).getSubServices().get(childPosition).getId();
                        Intent intent = new Intent(context, MaintainComponentActivity.class);
                        intent.putExtra("serviceid", Serviceid);
                        intent.putExtra("serviceName", washCar.get(groupPosition).getSubServices().get(childPosition).getServiceName());
                        context.startActivity(intent);

                    }
                });
                // 隐藏价格信息
                mViewChild.tv_discount_price.setVisibility(View.GONE);
                mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
                mViewChild.tv_original_price.setVisibility(View.GONE);
                mViewChild.tv_item_child_discount_showOrNot.setVisibility(View.GONE);
                break;
            case 2: // 洗车
                mViewChild.btn_pay
                        .setBackgroundResource(R.drawable.pay_click_selector);
                mViewChild.btn_pay.setTextColor(context.getResources().getColor(
                        R.color.main_blue));
                mViewChild.btn_pay.setText("支付");
                mViewChild.btn_pay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO 支付,因为要展示,所以暂时注释

                        Intent intent = new Intent(context, WashCarPayActivity.class);
                        intent.putExtra("seller", tenantName);
                        intent.putExtra("service", washCar.get(groupPosition).getCategoryName());
                        intent.putExtra("service_id", String.valueOf(washCar.get(groupPosition).getSubServices().get(childPosition).getId()));
                        String price = "" + washCar.get(groupPosition).getSubServices().get(childPosition).getPromotionPrice();
                        if (null == price || "".equals(price)) {
                            price = "" + washCar.get(groupPosition).getSubServices().get(childPosition).getPrice();
                        }
                        intent.putExtra("price", price);
                        LogHelper.d("price:" + price);
                        context.startActivity(intent);

                    }
                });

                // 展示洗车的价格信息
                if (0 != washCar.get(groupPosition).getSubServices().get(childPosition).getPromotionPrice()) {
                    mViewChild.tv_discount_price_remind.setVisibility(View.VISIBLE); // "优惠价"
                    mViewChild.tv_discount_price.setVisibility(View.VISIBLE); // 优惠价
                    mViewChild.tv_original_price.setVisibility(View.GONE); // 原价
                    mViewChild.tv_discount_price.setText("￥"
                            + washCar.get(groupPosition).getSubServices().get(childPosition)
                            .getPromotionPrice());
//                mViewChild.tv_original_price.setText("￥"
//                        + washCar.get(groupPosition).getSubServices().get(childPosition).getPrice());
                } else {
                    mViewChild.tv_discount_price_remind.setVisibility(View.GONE); // "优惠价"
                    mViewChild.tv_discount_price.setVisibility(View.GONE); // 优惠价
                    mViewChild.tv_original_price.setVisibility(View.VISIBLE); // 原价

                    mViewChild.tv_original_price.setText("￥"
                            + washCar.get(groupPosition).getSubServices().get(childPosition).getPrice());
//                mViewChild.tv_original_price.setVisibility(View.GONE);
//                mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
                }
                break;
            case 4: // 紧急救援
                mViewChild.btn_pay.setBackgroundResource(R.drawable.sos_click_selector);
                mViewChild.btn_pay.setTextColor(context.getResources().getColor(
                        R.color.btn_unpress_color));
                mViewChild.btn_pay.setText("救援");
                mViewChild.btn_pay.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, SoSActivity.class);
                        context.startActivity(intent);
                    }
                });
                // 隐藏价格信息
                mViewChild.tv_discount_price.setVisibility(View.GONE);
                mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
                mViewChild.tv_original_price.setVisibility(View.GONE);
                mViewChild.tv_item_child_discount_showOrNot.setVisibility(View.GONE);
                break;
            case 5:// 美容
                mViewChild.btn_pay.setBackgroundResource(R.drawable.maintain_click_selector);
                mViewChild.btn_pay.setTextColor(context.getResources().getColor(
                        R.color.main_orange));
                mViewChild.btn_pay.setText("预约");
                mViewChild.btn_pay.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        // TODO 预约,因为要展示,所以暂时注释,调用预约接口
                        Serviceid = washCar.get(groupPosition).getSubServices().get(childPosition).getId();
                        DateTimeSelectorDialogBuilder builder = DateTimeSelectorDialogBuilder.getInstance(context);
                        builder.setWheelViewVisibility(View.GONE);
                        builder.setOnSaveListener(new mYSaveListener());
                        builder.setSencondeCustomView(R.layout.yuyue_date_time_seletor, context);
                        builder.show();
                    }
                });
                // 隐藏价格信息
                mViewChild.tv_discount_price.setVisibility(View.GONE);
                mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
                mViewChild.tv_original_price.setVisibility(View.GONE);
                mViewChild.tv_item_child_discount_showOrNot.setVisibility(View.GONE);
                break;
            case 6://保险
                mViewChild.btn_pay.setBackgroundResource(R.drawable.ask_click_selector);
                mViewChild.btn_pay.setTextColor(context.getResources().getColor(
                        R.color.btn_green_pressed));
                mViewChild.btn_pay.setText("咨询");
                mViewChild.btn_pay.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnToPhone("" + msgBean.getContactPhone());
                    }
                });
                // 隐藏价格信息
                mViewChild.tv_discount_price.setVisibility(View.GONE);
                mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
                mViewChild.tv_original_price.setVisibility(View.GONE);
                mViewChild.tv_item_child_discount_showOrNot.setVisibility(View.GONE);
                break;
        }


        if (-1 == price) {
            mViewChild.tv_discount_price.setVisibility(View.GONE);
            mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
            mViewChild.tv_original_price.setVisibility(View.GONE);
            mViewChild.tv_item_child_discount_showOrNot.setVisibility(View.GONE);
        } else {

            if (0 != washCar.get(groupPosition).getSubServices().get(childPosition).getPromotionPrice()) {
                mViewChild.tv_discount_price_remind.setVisibility(View.VISIBLE); // "优惠价"
                mViewChild.tv_discount_price.setVisibility(View.VISIBLE); // 优惠价
                mViewChild.tv_original_price.setVisibility(View.GONE); // 原价
                mViewChild.tv_discount_price.setText("￥"
                        + washCar.get(groupPosition).getSubServices().get(childPosition)
                        .getPromotionPrice());
//                mViewChild.tv_original_price.setText("￥"
//                        + washCar.get(groupPosition).getSubServices().get(childPosition).getPrice());
            } else {
                mViewChild.tv_discount_price_remind.setVisibility(View.GONE); // "优惠价"
                mViewChild.tv_discount_price.setVisibility(View.GONE); // 优惠价
                mViewChild.tv_original_price.setVisibility(View.VISIBLE); // 原价

                mViewChild.tv_original_price.setText("￥"
                        + washCar.get(groupPosition).getSubServices().get(childPosition).getPrice());
//                mViewChild.tv_original_price.setVisibility(View.GONE);
//                mViewChild.tv_discount_price_remind.setVisibility(View.GONE);
            }
        }

        // 会员红包一直隐藏
        mViewChild.tv_item_child_discount_showOrNot.setVisibility(View.GONE);
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

                    Toast.makeText(context, "预约时间必须大于当前日期", Toast.LENGTH_SHORT).show();
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

        ProgrosDialog.openDialog(context);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER + NetInterface.SUBSCRIBE + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", ((BaseActivity) context).getUserId());
        param.put("token", ((BaseActivity) context).getToken());
        param.put("serviceId", id);
        param.put("subscribeDate", date);
//        param.put("price", price);
//        LogHelper.d(id + "----" + price);
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
                Toast.makeText(context, R.string.server_link_fault, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 设置gridview数据
     *
     * @param data
     * @return
     */
    private ArrayList<HashMap<String, Object>> setGridViewData(String[] data) {
        ArrayList<HashMap<String, Object>> gridItem = new ArrayList<HashMap<String, Object>>();
        Log.i("==============", data + "");
        for (int i = 0; i < data.length; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("channel_gridview_item", data[i]);
            gridItem.add(hashMap);
        }
        return gridItem;
    }

    /**
     * @param gridView
     */
    private void setGridViewListener(final ListView gridView) {
        gridView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;
                    Toast.makeText(context,
                            "position=" + position + "||" + tv.getText(),
                            Toast.LENGTH_SHORT).show();
                    Log.e("hefeng", "gridView listaner position=" + position
                            + "||text=" + tv.getText());
                }
            }
        });
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return washCar.get(groupPosition).getSubServices() == null ? 0 : washCar.get(groupPosition).getSubServices().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return washCar.get(groupPosition);// [groupPosition];
    }

    @Override
    public int getGroupCount() {
        return washCar == null ? 0 : washCar.size();// .length;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewChild = new ViewChild();
            convertView = mInflater.inflate(R.layout.item_expandablellistview,
                    null);
            mViewChild.tv_project_name = (TextView) convertView
                    .findViewById(R.id.tv_project_name);
            mViewChild.tv_project_note = (TextView) convertView
                    .findViewById(R.id.tv_project_note);
            mViewChild.tv_project_money = (TextView) convertView
                    .findViewById(R.id.tv_project_money);
            mViewChild.img_project_right = (ImageView) convertView
                    .findViewById(R.id.img_project_right);
            convertView.setTag(mViewChild);
        } else {
            mViewChild = (ViewChild) convertView.getTag();
        }

        if (isExpanded) {
            mViewChild.img_project_right
                    .setImageResource(R.drawable.channel_expandablelistview_top_icon);
        } else {
            mViewChild.img_project_right
                    .setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
        }
        mViewChild.tv_project_name.setText(washCar.get(groupPosition).getCategoryName());
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    ViewChild mViewChild;

    static class ViewChild {
        ImageView img_project_right;
        TextView tv_project_name;
        TextView tv_project_note;
        TextView tv_project_money;
        UnSlidingListView gridView;

        TextView tv_item_child_name;
        TextView tv_item_child_discount_showOrNot;
        TextView tv_discount_price_remind;
        TextView tv_discount_price;
        TextView tv_original_price;
        TextView btn_pay;
    }

}
