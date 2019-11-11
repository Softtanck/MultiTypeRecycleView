package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.config.NetInterface;
import com.cheweishi.android.dialog.ProgrosDialog;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tanck on 10/26/2016.
 * <p>
 * Describe:
 */
public class ReturnActivity extends BaseActivity {

    @ViewInject(R.id.left_action)
    private Button left_action;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.ptl_reback)
    private PullToRefreshListView ptl_reback;

    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);
        ViewUtils.inject(this);
        init();
    }

    private void init() {
        left_action.setText(R.string.back);
        title.setText(R.string.reback_money);
        sendPacket(0);
    }

    private void sendPacket(int type) {
        if (0 == type)
            ProgrosDialog.openDialog(baseContext);
        String url = NetInterface.BASE_URL + NetInterface.TEMP_ORDER_CREATE + NetInterface.RETURNLIST + NetInterface.SUFFIX;
        Map<String, Object> param = new HashMap<>();
        param.put("userId", loginResponse.getDesc());
        param.put("token", loginResponse.getToken());
        param.put("pageNumber", page);
        param.put("pageSize", 10);
        param.put(Constant.PARAMETER_TAG, NetInterface.RETURNLIST);
        netWorkHelper.PostJson(url, param, this);
    }

    @Override
    public void receive(String TAG, String data) {
    }

    @OnClick({R.id.left_action})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:
                finish();
                break;
        }
    }
}
