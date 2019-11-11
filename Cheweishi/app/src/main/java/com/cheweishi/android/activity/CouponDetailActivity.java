package com.cheweishi.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.ActivityControl;
import com.zzhoujay.richtext.ImageFixCallback;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

/**
 * Created by tangce on 5/18/2016.
 */
public class CouponDetailActivity extends BaseActivity implements View.OnClickListener, ImageFixCallback {

    private TextView tv_coupon_detail;

    private TextView title;

    private Button left_action;
    private RichText richText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);

        init();

    }

    private void init() {
        tv_coupon_detail = (TextView) findViewById(R.id.tv_coupon_detail);
        left_action = (Button) findViewById(R.id.left_action);
        left_action.setText("返回");
        title = (TextView) findViewById(R.id.title);
        left_action.setOnClickListener(this);
        title.setText("优惠券详情");
        String temp = getIntent().getStringExtra("COUPON_DETAIL");
        if (null != temp) {
//            setRitchText(temp, tv_coupon_detail);
            richText = RichText.from(temp);
            richText.autoFix(false).fix(this).into(tv_coupon_detail);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_action:
                finish();
                break;
        }
    }

    public void setRitchText(String text, TextView textView) {
        richText = RichText.from(text);
        richText.autoFix(false).fix(new ImageFixCallback() {
            @Override
            public void onFix(ImageHolder holder, boolean imageReady) {
                if (holder.getWidth() > 500 && holder.getHeight() > 500) {
                    holder.setAutoFix(true);
                }
            }
        }).into(textView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != richText) {
            richText.onDestroy();
        }
        richText = null;
    }

    @Override
    public void onFix(ImageHolder holder, boolean imageReady) {
        if (holder.getWidth() > 500 && holder.getHeight() > 500) {
            holder.setAutoFix(true);
        }
    }
}
