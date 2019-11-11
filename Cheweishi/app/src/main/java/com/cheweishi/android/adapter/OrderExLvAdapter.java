package com.cheweishi.android.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.OrderDetailResponse;
import com.cheweishi.android.widget.UnSlidingListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class OrderExLvAdapter extends BaseExpandableListAdapter {
    // public String[] group = { "更换机油(美孚一号)", "更换机滤", "14项检测", "工时费", "订单金额" };
    // public String[][] listViewChild = { { "美孚二号", "美孚三号", "美孚四号", "美孚五号" },
    // { "一", "二", "三", }, { "体检", "体检", "体检", },
    // { "体检1", "体检1", "体检1", }, { "体12", "体检13", "体检14", } };
    // String[][] child = { { "" }, { "" }, { "" }, { "" }, { "" } };
    private OrderDetailResponse response;
    LayoutInflater mInflater;
    Context context;

    public OrderExLvAdapter(Context context, OrderDetailResponse response) {
        super();
        this.context = context;
        this.response = response;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public Object getChild(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getChildId(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return arg1;
    }

    @Override
    public View getChildView(int arg0, int arg1, boolean arg2, View arg3,
                             ViewGroup arg4) {
        // ViewHolder holder;
        // if (arg3 == null) {
        // holder = new ViewHolder();
        // arg3 = mInflater.inflate(R.layout.item_order_item_details, null);
        // holder.order_item_item = (UnSlidingListView) arg3
        // .findViewById(R.id.order_item_item);
        // arg3.setTag(holder);
        // } else {
        // holder = (ViewHolder) arg3.getTag();
        // }
        // SimpleAdapter mSimpleAdapter = new SimpleAdapter(context,
        // setListViewData(listViewChild[arg0]),
        // R.layout.channel_gridview_item,
        // new String[] { "channel_gridview_item" },
        // new int[] { R.id.channel_gridview_item });
        // holder.order_item_item.setAdapter(mSimpleAdapter);
        // setlistViewListener(holder.order_item_item);
        // holder.order_item_item
        // .setSelector(new ColorDrawable(Color.TRANSPARENT));
        return arg3;
    }

    private List<? extends Map<String, ?>> setListViewData(String[] strings) {
        ArrayList<HashMap<String, Object>> lvItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < strings.length; i++) {
            HashMap<String, Object> hashMap = new HashMap<String, Object>();
            hashMap.put("channel_gridview_item", strings[i]);
            lvItem.add(hashMap);
        }
        return lvItem;
    }

    private void setlistViewListener(ListView order_item_item) {
        order_item_item.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                TextView tv = (TextView) arg1;
                Toast.makeText(context,
                        "position=" + arg0 + "||" + tv.getText(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getChildrenCount(int arg0) {
        return 0;
    }

    @Override
    public Object getGroup(int arg0) {
        // TODO Auto-generated method stub
        return response == null ? null : response.getMsg();
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return response == null ? 0 : 1;
    }

    @Override
    public long getGroupId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getGroupView(int arg0, boolean arg1, View arg2, ViewGroup arg3) {
        ViewHolder viewHolder;
        if (arg2 == null) {
            viewHolder = new ViewHolder();
            arg2 = mInflater.inflate(R.layout.item_order_details, null);
            viewHolder.img_order_Name_img = (ImageView) arg2
                    .findViewById(R.id.img_order_Name_img);
            viewHolder.tv_order_LVName = (TextView) arg2
                    .findViewById(R.id.tv_order_LVName);
            viewHolder.tv_order_LvNameCost = (TextView) arg2
                    .findViewById(R.id.tv_order_LvNameCost);
            viewHolder.tv_order_name = (TextView) arg2
                    .findViewById(R.id.tv_order_name);
            arg2.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) arg2.getTag();

        }
        if (arg1) {
            viewHolder.img_order_Name_img
                    .setImageResource(R.drawable.channel_expandablelistview_top_icon);
        } else {
            viewHolder.img_order_Name_img
                    .setImageResource(R.drawable.channel_expandablelistview_bottom_icon);
        }
        viewHolder.tv_order_name.setText(response.getMsg().getServiceName());


        String priceTemp = null;
        double price = response.getMsg().getPrice();
        if (-1 == price) { // 预约未定价
            priceTemp = "当前服务暂时未定价";
        } else {
            priceTemp = "￥" + price;
        }
        viewHolder.tv_order_LvNameCost.setText(priceTemp);
        return arg2;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    class ViewHolder {
        ImageView img_order_Name_img;
        TextView tv_order_name;
        TextView tv_order_LVName;
        TextView tv_order_LvNameCost;
        UnSlidingListView order_item_item;
    }

}
