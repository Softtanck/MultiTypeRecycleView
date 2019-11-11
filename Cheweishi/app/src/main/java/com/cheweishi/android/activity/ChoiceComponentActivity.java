package com.cheweishi.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ChoiceComponentAdapter;
import com.cheweishi.android.adapter.ComponentServiceAdapter;
import com.cheweishi.android.entity.ComponentServiceResponse;
import com.cheweishi.android.entity.ComponentServiceShowResponse;
import com.cheweishi.android.widget.UnSlidingExpandableListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangce on 6/2/2016.
 */
public class ChoiceComponentActivity extends BaseActivity implements View.OnClickListener, ExpandableListView.OnChildClickListener {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.tv_choice_service)
    private TextView tv_choice_service; // 服务名字

    @ViewInject(R.id.usl_choice_content)
    private UnSlidingExpandableListView usl_choice_content; // 主要的Item

    @ViewInject(R.id.tv_choice_total_money)
    private TextView tv_choice_total_money; // 总价格

    @ViewInject(R.id.tv_choice_ok)
    private TextView tv_choice_ok;// 确认

    private ChoiceComponentAdapter adapter;

    private ComponentServiceResponse response;


    private double totalMoneyTemp = 0; // 总价钱

//    private List<ComponentServiceShowResponse> showData = new ArrayList<>(); // 组合界面需要展示的数据


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_component);
        ViewUtils.inject(this);

        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText("选择配件");
        response = (ComponentServiceResponse) getIntent().getSerializableExtra("totalData");
        tv_choice_service.setText(getIntent().getStringExtra("serviceName"));
        if (null != response) {
            adapter = new ChoiceComponentAdapter(baseContext, response);
            usl_choice_content.setAdapter(adapter);
            OpenGroup(usl_choice_content, adapter);
            calcMoney(response);
            usl_choice_content.setOnChildClickListener(this);
        }
    }


    @OnClick({R.id.left_action, R.id.tv_choice_ok})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
            case R.id.tv_choice_ok: // 确定
                Intent intent = new Intent();
                intent.putExtra("totalData", response);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void calcMoney(ComponentServiceResponse response) {
        try {
            totalMoneyTemp = 0;
            for (int i = 0; i < response.getMsg().size(); i++) {
                if (null != response.getMsg().get(i)) {
                    for (int j = 0; j < response.getMsg().get(i).getItemParts().size(); j++) {
                        if (response.getMsg().get(i).getItemParts().get(j).isIsDefault()) { // 找到有的了.
                            totalMoneyTemp += Double.valueOf(response.getMsg().get(i).getItemParts().get(j).getPrice());// 计算总价格
                        }
                    }
                }
            }

            tv_choice_total_money.setText("总价: ￥" + totalMoneyTemp + "元");
        } catch (Exception e) { // 可能会出现异常
            e.printStackTrace();
        }
    }

    private void OpenGroup(UnSlidingExpandableListView listView, BaseExpandableListAdapter adapter) {
        // 展开某组的列表
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        boolean result = response.getMsg().get(groupPosition).getItemParts().get(childPosition).isIsDefault();

        if (result) {
            response.getMsg().get(groupPosition).getItemParts().get(childPosition).setIsDefault(false);
        } else {
            response.getMsg().get(groupPosition).getItemParts().get(childPosition).setIsDefault(true);
        }

        adapter.setData(response); // 更新UI
        calcMoney(response); // 计算价格

        return true;
    }
}
