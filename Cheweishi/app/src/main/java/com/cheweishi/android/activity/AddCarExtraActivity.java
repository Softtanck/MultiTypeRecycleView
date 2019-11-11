package com.cheweishi.android.activity;

import com.cheweishi.android.R;
import com.cheweishi.android.widget.NoUnderlineSpan;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Xiaojin 绑定车辆附加项
 */
public class AddCarExtraActivity extends BaseActivity implements
        OnClickListener {
    @ViewInject(R.id.tv_addCarExtraDevice)
    private TextView tv_addCarExtraDevice;
    @ViewInject(R.id.left_action)
    private Button left_action;
    @ViewInject(R.id.title)
    private TextView title;
    @ViewInject(R.id.tv_extra_communicate)
    private TextView tv_extra_communicate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_extra);
        ViewUtils.inject(this);
        initViews();
        setListeners();
    }

    private void initViews() {
        title.setText(R.string.exta_item);
        left_action.setText(R.string.back);
        SpannableString sp = new SpannableString(getResources().getString(
                R.string.car_extra_app));
        sp.setSpan(new NoUnderlineSpan("http://www.chcws.com"), 41, 45,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(
                new ForegroundColorSpan(this.getResources().getColor(
                        R.color.orange_text_color)), 41, 45,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_addCarExtraDevice.setText(sp);
        tv_addCarExtraDevice
                .setMovementMethod(LinkMovementMethod.getInstance());
        sp = new SpannableString(getResources().getString(
                R.string.car_extra_tel));
        sp.setSpan(new NoUnderlineSpan("tel:400-793-0888"), 15, 27,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(
                new ForegroundColorSpan(this.getResources().getColor(
                        R.color.orange_text_color)), 15, 27,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tv_extra_communicate.setText(sp);
        tv_extra_communicate
                .setMovementMethod(LinkMovementMethod.getInstance());
        tv_addCarExtraDevice.setClickable(true);
        tv_addCarExtraDevice
                .setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * 设置监听器
     */
    private void setListeners() {
        left_action.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.left_action:
                AddCarExtraActivity.this.finish();
                break;
        }
    }

}
