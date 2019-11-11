package com.cheweishi.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.DateUtils;
import com.cheweishi.android.utils.LogHelper;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.wheelview.OnWheelChangedListener;
import com.cheweishi.android.wheelview.StrericWheelAdapter;
import com.cheweishi.android.wheelview.WheelView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 项目名称: DateSelector 类名称: DateSelectorWheelView 创建人: xhl 创建时间: 2015-1-14
 * 下午5:07:34 版本: v1.0 类描述:
 */
// com.cheweishi.android.widget.DateSelectorWheelView
public class DateSelectorWheelViewByHour extends RelativeLayout implements
        OnWheelChangedListener {
    private final String flag = "Tanck";
    private RelativeLayout rlTitle;
    private View line1;
    private LinearLayout llWheelViews;
    private TextView tvSubTitle;
    private TextView tvHour;
    private TextView tvMinute;
    private WheelView wvHour;
    private WheelView wvMinute;
    private String[] hours = new String[24];
    private String[] minutes = new String[60];
    private StrericWheelAdapter hoursAdapter;
    private StrericWheelAdapter minutesAdapter;

    public DateSelectorWheelViewByHour(Context context, AttributeSet attrs,
                                       int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout(context);
    }

    public DateSelectorWheelViewByHour(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLayout(context);
    }

    public DateSelectorWheelViewByHour(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        LayoutInflater.from(context).inflate(R.layout.dete_time_layout_hour, this,
                true);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_date_time_title);
        line1 = findViewById(R.id.line_1);
        llWheelViews = (LinearLayout) findViewById(R.id.ll_wheel_views);
        tvSubTitle = (TextView) findViewById(R.id.tv_date_time_subtitle);
        tvHour = (TextView) findViewById(R.id.tv_date_time_hour);
        tvMinute = (TextView) findViewById(R.id.tv_date_time_minute);
        wvHour = (WheelView) findViewById(R.id.wv_date_of_hour);
        wvMinute = (WheelView) findViewById(R.id.wv_date_of_minute);
        wvHour.addChangingListener(this);
        wvMinute.addChangingListener(this);
        setData();
    }

    private void setData() {
        for (int i = 0; i < hours.length; i++) {
            if (i < 9) {
                hours[i] = "0" + (1 + i) + " 时";
            } else {
                hours[i] = (1 + i) + " 时";
            }
        }
        for (int i = 0; i < minutes.length; i++) {
            if (i < 9) {
                minutes[i] = "0" + (1 + i) + " 分";
            } else {
                minutes[i] = (1 + i) + " 分";
            }
        }
        hoursAdapter = new StrericWheelAdapter(hours);
        minutesAdapter = new StrericWheelAdapter(minutes);
        wvHour.setAdapter(hoursAdapter);
        wvHour.setCurrentItem(getTodayDayHour()); // TODO 设置默认
        wvHour.setCyclic(true);
        wvMinute.setAdapter(minutesAdapter);
        wvMinute.setCurrentItem(getTodayDayMinute()); // TODO 设置默认
        wvMinute.setCyclic(true);


    }

    /**
     * 获取当前日期的天数的小时
     *
     * @return
     */
    private int getTodayDayHour() {
        // 2015年12月01日
        int position = 0;
        String today = getToday();
        String hour = today.substring(0, 4);
        for (int i = 0; i < hours.length; i++) {
            if (hour.equals(hours[i])) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 获取当前日期的天数的分钟
     *
     * @return
     */
    private int getTodayDayMinute() {
        // 2015年12月01日
        int position = 0;
        String today = getToday();
        String minute = today.substring(4, 8);
        for (int i = 0; i < minutes.length; i++) {
            if (minute.equals(minutes[i])) {
                position = i;
                break;
            }
        }
        return position;
    }

    /**
     * 设置当前显示的小时
     *
     * @param month
     */
    public void setCurrentMonth(String month) {
        month = month + " 时";
        for (int i = 0; i < hours.length; i++) {
            if (month.equals(hours[i])) {
                wvHour.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     * 设置当前显示的分钟
     *
     * @param day 14
     */
    public void setCurrentDay(String day) {
        day = day + " 日";
        for (int i = 0; i < minutes.length; i++) {
            if (day.equals(minutes[i])) {
                wvMinute.setCurrentItem(i);
                break;
            }
        }
    }

    /**
     * 获取选择的日期的值
     *
     * @return
     */
    public String getSelectedDate() {
        DecimalFormat df = new DecimalFormat("00");
        return tvHour.getText().toString().trim() + ":"
                + df.format(StringUtil.getInt(tvMinute.getText().toString()));
    }

    /**
     * 设置标题的点击事件
     *
     * @param onClickListener
     */
    public void setTitleClick(OnClickListener onClickListener) {
        rlTitle.setOnClickListener(onClickListener);
    }

    /**
     * 设置标题是否可见
     */
    public void setTitleVisibility(boolean isVisible) {
        if (isVisible) {
            rlTitle.setVisibility(View.VISIBLE);
            line1.setVisibility(View.VISIBLE);
        } else {
            rlTitle.setVisibility(View.GONE);
            line1.setVisibility(View.GONE);
        }
    }

    /**
     * 设置日期选择器的日期转轮是否可见
     *
     * @param visibility
     */
    public void setDateSelectorVisiblility(int visibility) {
        llWheelViews.setVisibility(visibility);
    }

    public int getDateSelectorVisibility() {
        return llWheelViews.getVisibility();
    }

    /**
     * 判断是否是闰年
     *
     * @param year
     * @return
     */
    private boolean isLeapYear(String year) {
        int temp = Integer.parseInt(year);
        return temp % 4 == 0 && (temp % 100 != 0 || temp % 400 == 0) ? true
                : false;
    }


    int currentMonth = 1;

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        String trim = null;
        switch (wheel.getId()) {
            case R.id.wv_date_of_hour:
                trim = DateUtils.splitDateString(wvHour.getCurrentItemValue())
                        .trim();
                tvHour.setText(trim);
                break;
            case R.id.wv_date_of_minute:
                trim = DateUtils.splitDateString(wvMinute.getCurrentItemValue())
                        .trim();
                currentMonth = Integer.parseInt(trim);
                tvMinute.setText(trim);
                break;
        }
    }

    /**
     * 获取今天的日期
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    private String getToday() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH 时mm 分");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

}
