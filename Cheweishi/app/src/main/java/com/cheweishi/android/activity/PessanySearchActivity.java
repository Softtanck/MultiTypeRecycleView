package com.cheweishi.android.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cheweishi.android.adapter.ChangeCarAdapter;
import com.cheweishi.android.R;
import com.cheweishi.android.adapter.PessanyAdapter;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.cheweishi.android.entity.MyCarManagerResponse;
import com.cheweishi.android.entity.PessanyResponse;
import com.cheweishi.android.tools.EmptyTools;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.GsonUtil;
import com.cheweishi.android.widget.MyUnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 违章查询
 *
 * @author Xiaojin
 */
@ContentView(R.layout.activity_pessany_search)
public class PessanySearchActivity extends BaseActivity implements
        OnClickListener, OnItemClickListener {
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.right_action)
    private TextView right_action;
    @ViewInject(R.id.lv_pessanySearch)
    private ListView lv_pessanySearch;
    private PessanyAdapter adapter;
    private List<PessanyResponse.MsgBean> listPessanySearch;

    private List<MyCarManagerResponse.MsgBean> msg;
    private boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initViews();
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        title.setText(R.string.title_activity_pessany_search);
        left_action.setText(R.string.back);
        right_action.setText("切换车辆");
        getData(loginResponse.getMsg().getDefaultVehiclePlate());
    }

    private void getData(String plate) {
        if (null == plate && !"".equals(plate)) {
            showToast("获取车牌号失败");
            return;
        }
        ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_RECORD + NetInterface.ILLEGAL_RECORD + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("plate", plate);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String data) {
        if (isSearch)
            ProgrosDialog.closeProgrosDialog();

        PessanyResponse response = (PessanyResponse) GsonUtil.getInstance().convertJsonStringToObject(data, PessanyResponse.class);
        if (null == response || !response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(response.getDesc());
            return;
        }

        // 填充数据
        if (null != response.getMsg() && 0 < response.getMsg().size()) {
            listPessanySearch = response.getMsg();
            adapter = new PessanyAdapter(this, listPessanySearch);
            lv_pessanySearch.setAdapter(adapter);
            lv_pessanySearch.setOnItemClickListener(this);
        } else {
            EmptyTools.setEmptyView(baseContext, lv_pessanySearch);
            EmptyTools.setImg(R.drawable.mycar_icon);
            EmptyTools.setMessage("您还没有违章记录");
        }
        loginResponse.setToken(response.getToken());
        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

        if (!isSearch)
            connectToServer();
    }

    @Override
    public void receive(String TAG, String data) {
        ProgrosDialog.closeProgrosDialog();
        MyCarManagerResponse response = (MyCarManagerResponse) GsonUtil.getInstance().convertJsonStringToObject(data, MyCarManagerResponse.class);
        if (!response.getCode().equals(NetInterface.RESPONSE_SUCCESS)) {
            showToast(R.string.server_link_fault);
            return;
        }
        if (null != response.getMsg()) {
            msg = response.getMsg();
        }

        loginResponse.setToken(response.getToken());
//        LoginMessageUtils.saveloginmsg(baseContext, loginResponse);

    }

    @OnClick({R.id.left_action, R.id.right_action})
    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.right_action:
                showPopupWindow(arg0);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent(this, PessanySearchDetailActivity.class);
        intent.putExtra("PessanySearchNative", listPessanySearch.get(arg2));
        startActivity(intent);
    }


    private MyUnSlidingListView listView;
    private ChangeCarAdapter changeCarAdapter;
    private PopupWindow popupWindow;

    private void showPopupWindow(View down) {
        if (null == msg)
            return;
        if (null == popupWindow) {
            View view = View.inflate(baseContext, R.layout.change_car, null);
//            int screen = ScreenTools.getScreentWidth(PessanySearchActivity.this);
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            listView = (MyUnSlidingListView) view.findViewById(R.id.lv_change_car);
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            return;
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        changeCarAdapter = new ChangeCarAdapter(baseContext, msg);
        listView.setAdapter(changeCarAdapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                if (popupWindow != null)
                    popupWindow.dismiss();

                // TODO 提交查询记录
                isSearch = true;
                getData(msg.get(position).getPlate());
            }
        });
//        popupWindow.showAsDropDown(down, ScreenTools.getScreentWidth(PessanySearchActivity.this) / 10, 0);
        popupWindow.showAsDropDown(down, (int) (down.getWidth() / 2.5), 0);
    }


    /**
     * 请求车辆列表
     */
    private void connectToServer() {
        String url = NetInterface.BASE_URL + NetInterface.TEMP_CAR_URL + NetInterface.LIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put(Constant.PARAMETER_TAG, NetInterface.LIST + "CHANGE");
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EmptyTools.destory();
    }
}
