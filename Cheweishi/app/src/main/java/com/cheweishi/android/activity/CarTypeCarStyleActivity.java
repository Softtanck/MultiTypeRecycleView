package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.CarTypeCarStyleExpandableListViewAdapter;
import com.cheweishi.android.biz.XUtilsImageLoader;
import com.cheweishi.android.entity.QueryCarModeResponse;
import com.cheweishi.android.utils.GsonUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnChildClick;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnGroupClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 车辆型号选择
 *
 * @author Xiaojin
 */
public class CarTypeCarStyleActivity extends BaseActivity implements
        OnClickListener, OnChildClickListener, OnGroupClickListener {
    @ViewInject(R.id.tv_letter)
    private TextView tvBrandGroupName;
    @ViewInject(R.id.tv_carname)
    private TextView tvBrandName;
    @ViewInject(R.id.img_carlogo)
    private ImageView carLogo;
    @ViewInject(R.id.tv_cardetailname)
    private TextView tvCarStyleName;
    @ViewInject(R.id.elv_cartype_detail)
    private ExpandableListView expandableListView;
    private CarTypeCarStyleExpandableListViewAdapter carStyleExpandableListViewAdapter;
    private List<QueryCarModeResponse.MsgBean> styleList = new ArrayList<QueryCarModeResponse.MsgBean>();
    @ViewInject(R.id.left_action)
    private Button ibtnBack;
    @ViewInject(R.id.title)
    private TextView title;
    private String brandGroupName, modelGroupName, json, carModelName,
            carModelLogo;
    private String pinyinNum;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        styleList.clear();
        setContentView(R.layout.null_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.cartype_carstyle_layout);
        ViewUtils.inject(this);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        ibtnBack.setText(R.string.back);
        title.setText(R.string.style_choose);
        expandableListView.setChildDivider(getResources().getDrawable(
                R.color.line_gray));
        Intent intent = getIntent();
        brandGroupName = intent.getStringExtra("brandGroupName");
        modelGroupName = intent.getStringExtra("modelGroupName");
        json = intent.getStringExtra("json");
        carModelName = intent.getStringExtra("carModelName");
        carModelLogo = intent.getStringExtra("url");
        pinyinNum = intent.getStringExtra("brandGroup");
        tvBrandGroupName.setText(brandGroupName);
        tvBrandName.setText(pinyinNum);
        XUtilsImageLoader.getxUtilsImageLoader(this, R.drawable.home_color_car,
                carLogo, carModelLogo);
        tvCarStyleName.setText(carModelName);
        tvCarStyleName.setText(modelGroupName);
        styleList = getCarStyleJsonData(json);
        carStyleExpandableListViewAdapter = new CarTypeCarStyleExpandableListViewAdapter(
                this, styleList);
        expandableListView.setAdapter(carStyleExpandableListViewAdapter);
        for (int i = 0; i < carStyleExpandableListViewAdapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }
    }

    /**
     * 解析车型具体型号的json数据
     *
     * @param json json格式的数据
     */
    protected List<QueryCarModeResponse.MsgBean> getCarStyleJsonData(String json) {
        QueryCarModeResponse queryCarModeResponse = (QueryCarModeResponse) GsonUtil.getInstance().convertJsonStringToObject(json, QueryCarModeResponse.class);
        styleList = queryCarModeResponse.getMsg();
        return styleList;
    }

    /**
     * back键的返回事件
     */
    @OnClick({R.id.left_action})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 汽车品牌item，车型分类item事件 onchildclick
     */
    @OnChildClick({R.id.elv_cartype_detail})
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        String styleId = String.valueOf(styleList.get(childPosition).getId());
        ImageView imgCheck = (ImageView) v.findViewById(R.id.img_ifchecked);
        imgCheck.setVisibility(View.VISIBLE);
        Intent intent = new Intent();
        intent.putExtra("styleId", styleId);
        intent.putExtra("carLogo", carModelLogo);
        setResult(RESULT_OK, intent);

        finish();
        return true;
    }

    @OnGroupClick({R.id.elv_cartype_detail})
    @Override
    public boolean onGroupClick(ExpandableListView arg0, View arg1, int arg2,
                                long arg3) {
        // TODO Auto-generated method stub
        return true;
    }

}
