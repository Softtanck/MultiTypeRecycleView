package com.cheweishi.android.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.TenantViewPagerAdapter;
import com.cheweishi.android.entity.ServiceDetailResponse;

import java.util.List;

/**
 * Created by tangce on 6/24/2016.
 */
public class TenantHeadImgActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private Button left_action;
    private ViewPager viewPager;

    private TenantViewPagerAdapter adapter;

    private TextView title;

    private TextView tv_current_position;

    private List<ServiceDetailResponse.MsgBean.tenantImagesBean> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_head_img);
        init();
    }

    private void init() {
        left_action = (Button) findViewById(R.id.left_action);
        title = (TextView) findViewById(R.id.title);
        viewPager = (ViewPager) findViewById(R.id.vp_tenant_head);
        tv_current_position = (TextView) findViewById(R.id.tv_current_position);
        left_action.setText(R.string.back);
        title.setText(R.string.tenant_title_head);
        left_action.setOnClickListener(this);
        try {
            data = ((ServiceDetailResponse) getIntent().getSerializableExtra("data")).getMsg().getTenantImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == data || 0 >= data.size()) {
            finish();
            return;
        }
        adapter = new TenantViewPagerAdapter(baseContext, data);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        tv_current_position.setText("1/" + data.size());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tv_current_position.setText((position + 1) + "/" + data.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
        }
    }
}
