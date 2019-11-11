package com.cheweishi.android.adapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.cheweishi.android.R;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.BaiduMapView;

public class GasStationAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> list;
    private boolean isDraw;
    private LatLng latLng;

    public GasStationAdapter(Context context, List<Map<String, String>> list,
                             boolean isDraw, LatLng latLng) {
        this.context = context;
        this.list = list;
        this.isDraw = isDraw;
        this.latLng = latLng;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list == null ? 0 : list.size();
    }

    public void setList(List<Map<String, String>> listMessage, boolean is,
                        LatLng latLng) {
        this.list = null;
        this.list = listMessage;
        isDraw = is;
        this.latLng = latLng;
        this.notifyDataSetChanged();

    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int arg0, View arg1, ViewGroup arg2) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.gasstation_listview_item, null);

        TextView address = (TextView) view
                .findViewById(R.id.gasstation_textview_address);
        TextView distance = (TextView) view
                .findViewById(R.id.gasstationlistview_item_distance);
        TextView addressdetails = (TextView) view
                .findViewById(R.id.gasstation_listview_item_addressdetails);
        TextView findcarportlistview_item_number = (TextView) view
                .findViewById(R.id.gasstationlistview_item_number);
        LinearLayout linearLayoutLocation = (LinearLayout) view
                .findViewById(R.id.gasstation_linearLayoutLocation);
        LinearLayout linearLayoutNavitave = (LinearLayout) view
                .findViewById(R.id.gasstation_linearLayoutNavitave);
        ImageView imageView = (ImageView) view
                .findViewById(R.id.gasstation_imageview_pnone);
        double lat = Double.parseDouble(list.get(arg0).get("lat"));
        double lng = Double.parseDouble(list.get(arg0).get("lng"));
        LatLng latLng2 = new LatLng(lat, lng);

        if (isDraw) {

            String distance1 = list.get(arg0).get("distance");
            double distanced = StringUtil.getDouble(distance1);
            if (distanced > 1000) {
                String str = getScale2(distanced / 1000) + "";
                distance.setText(str + "千米");
            } else {
                distance.setText((int) distanced + "米");
            }

        } else {
            double distance1 = DistanceUtil.getDistance(
                    MyMapUtils.getLatLng(context), latLng2);
            if (distance1 > 1000) {
                String str = getScale2(distance1 / 1000) + "";
                distance.setText(str + "千米");

            } else {
                distance.setText((int) distance1 + "米");

            }

            // String distance1=list.get(arg0).get("distance");
            // distance.setText((int)distance1+"");
        }

        findcarportlistview_item_number.setText(arg0 + 1 + ".");
        address.setText(list.get(arg0).get("name"));
        addressdetails.setText(list.get(arg0).get("address"));
        if (list.get(arg0).get("telephone").equals("")) {
            linearLayoutLocation.setEnabled(false);
            imageView.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.dianhua1));
        } else {

            imageView.setImageDrawable(context.getResources().getDrawable(
                    R.drawable.dianhua));
            linearLayoutLocation.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    String number = list.get(arg0).get("telephone");
                    Intent tel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                            + number));
                    tel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(tel);
                }
            });
        }

        linearLayoutNavitave.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                double lat = Double.parseDouble(list.get(arg0).get("lat"));
                double lng = Double.parseDouble(list.get(arg0).get("lng"));
                if (lat != 0 && lng != 0) {
                    BaiduMapView baiduMapView = new BaiduMapView();
                    baiduMapView.initMap(context.getApplicationContext());
                    if (isDraw) {
                        baiduMapView.baiduNavigation(
                                GasStationAdapter.this.latLng.latitude,
                                GasStationAdapter.this.latLng.longitude,
                                MyMapUtils.getAddress(context.getApplicationContext()), lat, lng, list
                                        .get(arg0).get("address"));
                    } else {
                        baiduMapView.baiduNavigation(MyMapUtils
                                .getLatLng(context.getApplicationContext()).latitude, MyMapUtils
                                .getLatLng(context.getApplicationContext()).longitude, MyMapUtils
                                .getAddress(context.getApplicationContext()), lat, lng, list.get(arg0)
                                .get("address"));
                    }
                }
            }
        });

        return view;
    }

    private double getScale2(double d) {
        BigDecimal b = new BigDecimal(d);
        double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}
