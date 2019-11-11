package com.cheweishi.android.activity;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.DateAdapter;
import com.cheweishi.android.fragement.BaseFragment;
import com.cheweishi.android.fragement.CarReportMainFragment;
import com.cheweishi.android.fragement.DrivingBehaviorFragment;
import com.cheweishi.android.interfaces.CarReportListener;
import com.cheweishi.android.utils.CustomProgressDialog;
import com.cheweishi.android.utils.SpecialCalendar;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder;
import com.cheweishi.android.widget.DateTimeSelectorDialogBuilder.OnSaveListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 车辆报告
 *
 * @author zhangq
 */
public class CarReportActivity extends BaseActivity implements
        OnGestureListener, CarReportListener {
    @ViewInject(R.id.left_action)
    private TextView tvLeft;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.tv_date)
    private TextView tvDate;// 显示时间
    @ViewInject(R.id.container)
    private RelativeLayout mContainer;

    private FragmentManager manager;
    private FragmentTransaction trans;
    private BaseFragment fragmentBase;
    BaseFragment mSecondFragment;
    /**
     * 每天报告的id
     */
    private int rid;
    // 日历：
    private ViewFlipper flipper1 = null;
    private GridView gridView = null;
    private GestureDetector gestureDetector = null;
    private int year_c = 0;
    private int month_c = 0;
    private int day_c = 0;
    private int week_c = 0;
    private int week_num = 0;
    private String currentDate = "";
    private DateAdapter dateAdapter;
    private int daysOfMonth = 0; // 某月的天数
    private int dayOfWeek = 0; // 具体某一天是星期几
    private int weeksOfMonth = 0;
    private SpecialCalendar sc = null;
    private boolean isLeapyear = false; // 是否为闰年
    private int selectPostion = 0;
    private String dayNumbers[] = new String[7];
    private int currentYear;
    private int currentMonth;
    private int currentWeek;
    private int currentDay;
    private int currentNum;
    private boolean isCircle = false;// 滑动的是否是圆
    private String deviceNo; // 设备号

    private DecimalFormat decimalFormat = new DecimalFormat("00");
    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    private String sTime;// 选中日期
    /* 昨天 */
    private Calendar dayBeforeToday;
    /* 点击之前的日期 */
    private String dayBeforeClick;

    public String getDeviceNo() {
        return deviceNo;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_report);
        ViewUtils.inject(this);

        deviceNo = getIntent().getStringExtra("deviceNo");
        dayBeforeToday = Calendar.getInstance();
        dayBeforeToday.add(Calendar.DAY_OF_MONTH, -1);
        initCalendarData(sdf.format(dayBeforeToday.getTime()));

        tvDate.setText(currentYear + "-" + decimalFormat.format(currentMonth)
                + "-" + decimalFormat.format(currentDay));
        gestureDetector = new GestureDetector(this);
        flipper1 = (ViewFlipper) findViewById(R.id.flipper1);
        dateAdapter = new DateAdapter(this, getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1 ? true : false, sdf.format(dayBeforeToday
                .getTime()));
        addGridView();
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);
        selectPostion = dateAdapter.getTodayPosition();
        // prePosition = selectPostion;
        gridView.setSelection(selectPostion);
        flipper1.addView(gridView, 0);
        tvDate.setOnClickListener(onClickListener);

        initView();
        initData();
    }

    /**
     * 初始化girdview时间
     * <p/>
     * 传入时间 yyyy-MM-dd
     */
    private void initCalendarData(String sDate) {
        currentDate = sDate;
        year_c = Integer.parseInt(currentDate.split("-")[0]);
        month_c = Integer.parseInt(currentDate.split("-")[1]);
        day_c = Integer.parseInt(currentDate.split("-")[2]);
        currentYear = year_c;
        currentMonth = month_c;
        currentDay = day_c;
        sc = new SpecialCalendar();
        getCalendar(year_c, month_c);
        week_num = getWeeksOfMonth();
        currentNum = week_num;
        if (dayOfWeek == 7) {
            week_c = day_c / 7 + 1;
        } else {
            if (day_c <= (7 - dayOfWeek)) {
                week_c = 1;
            } else {
                if ((day_c - (7 - dayOfWeek)) % 7 == 0) {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 1;
                } else {
                    week_c = (day_c - (7 - dayOfWeek)) / 7 + 2;
                }
            }
        }
        currentWeek = week_c;
        getCurrent();

    }

    /**
     * 判断某年某月所有的星期数
     *
     * @param year
     * @param month
     */
    public int getWeeksOfMonth(int year, int month) {
        // 先判断某月的第一天为星期几
        int preMonthRelax = 0;
        int dayFirst = getWhichDayOfWeek(year, month);
        int days = sc.getDaysOfMonth(sc.isLeapYear(year), month);
        if (dayFirst != 7) {
            preMonthRelax = dayFirst;
        }
        if ((days + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (days + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (days + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;

    }

    /**
     * 判断某年某月的第一天为星期几
     *
     * @param year
     * @param month
     * @return
     */
    public int getWhichDayOfWeek(int year, int month) {
        return sc.getWeekdayOfMonth(year, month);

    }

    /**
     * @param year
     * @param month
     */
    public int getLastDayOfWeek(int year, int month) {
        return sc.getWeekDayOfLastMonth(year, month,
                sc.getDaysOfMonth(isLeapyear, month));
    }

    public void getCalendar(int year, int month) {
        isLeapyear = sc.isLeapYear(year); // 是否为闰年
        daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
        dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
    }

    public int getWeeksOfMonth() {
        // getCalendar(year, month);
        int preMonthRelax = 0;
        if (dayOfWeek != 7) {
            preMonthRelax = dayOfWeek;
        }
        if ((daysOfMonth + preMonthRelax) % 7 == 0) {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7;
        } else {
            weeksOfMonth = (daysOfMonth + preMonthRelax) / 7 + 1;
        }
        return weeksOfMonth;
    }

    private void addGridView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        // oom
        gridView = new GridView(this);
        gridView.setNumColumns(7);
        gridView.setGravity(Gravity.CENTER_VERTICAL);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setVerticalSpacing(1);
        gridView.setHorizontalSpacing(1);
        gridView.setOnTouchListener(onTouchListener);

        gridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dayBeforeClick = getSTime();
                selectPostion = position;
                dateAdapter.setSeclection(position);
                dateAdapter.notifyDataSetChanged();
                tvDate.setText(getSTime());
                if (isAfterToday(sTime)) {
                    showToast(R.string.report_choose_after_today);

                    initCalendarData(sdf.format(new Date()));

                    selectPostion = 0;
                    dateAdapter = new DateAdapter(CarReportActivity.this,
                            getResources(), currentYear, currentMonth,
                            currentWeek, currentNum, selectPostion,
                            currentWeek == 1 ? true : false, sdf
                            .format(dayBeforeToday.getTime()));
                    dayNumbers = dateAdapter.getDayNumbers();
                    gridView.setAdapter(dateAdapter);
                    selectPostion = dateAdapter.getTodayPosition();
                    gridView.setSelection(selectPostion);
                    flipper1.removeAllViews();
                    flipper1.setInAnimation(null);
                    flipper1.setOutAnimation(null);
                    flipper1.addView(gridView, 0);

                    tvDate.setText(getSTime());
                    if (dayBeforeClick != null && !dayBeforeClick.equals(sTime)) {
                        updateFragmentData();
                    }
                } else {
                    dateAdapter.setDefaultPosition(position);
                    dateAdapter.notifyDataSetChanged();
                    updateFragmentData();
                }
            }

        });
        gridView.setLayoutParams(params);
    }

    /**
     * 刷新fragment数据
     */
    private void updateFragmentData() {
        showCustomDialog();
        fragmentBase.updateData(sTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onDown(MotionEvent e) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    /**
     * 刷新时间
     *
     * @return
     */
    public String getSTime() {
        if (selectPostion >= 0 && selectPostion <= 6) {
            sTime = dateAdapter.getCurrentYear(selectPostion)
                    + "-"
                    + decimalFormat.format(dateAdapter
                    .getCurrentMonth(selectPostion))
                    + "-"
                    + decimalFormat.format(StringUtil
                    .getInt(dayNumbers[selectPostion]));
        }
        return sTime;
    }

    /**
     * 重新计算当前的年月
     */
    public void getCurrent() {
        if (currentWeek > currentNum) {
            if (currentMonth + 1 <= 12) {
                currentMonth++;
            } else {
                currentMonth = 1;
                currentYear++;
            }
            currentWeek = 1;
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
        } else if (currentWeek == currentNum) {
            if (getLastDayOfWeek(currentYear, currentMonth) == 6) {
            } else {
                if (currentMonth + 1 <= 12) {
                    currentMonth++;
                } else {
                    currentMonth = 1;
                    currentYear++;
                }
                currentWeek = 1;
                currentNum = getWeeksOfMonth(currentYear, currentMonth);
            }

        } else if (currentWeek < 1) {
            if (currentMonth - 1 >= 1) {
                currentMonth--;
            } else {
                currentMonth = 12;
                currentYear--;
            }
            currentNum = getWeeksOfMonth(currentYear, currentMonth);
            currentWeek = currentNum - 1;
        }
    }

    /**
     * 滑动监听
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        int gvFlag = 0;
        if (e1.getX() - e2.getX() > 80) {
            return moveLeft(gvFlag);

        } else if (e1.getX() - e2.getX() < -80) {
            return moveRight(gvFlag);
        }
        return false;
    }

    /**
     * 右滑
     *
     * @param gvFlag
     * @return
     */
    private boolean moveRight(int gvFlag) {
        addGridView();
        if (!isCircle) {
            currentWeek--;
            selectPostion = -1;
        } else {
            if (--selectPostion > -1) {
                getCurrent();
                dateAdapter.setSeclection(selectPostion);
                dateAdapter.notifyDataSetChanged();
                tvDate.setText(getSTime());
                // prePosition = selectPostion;
                updateFragmentData();
                return true;
            }
            selectPostion = 6;
            // prePosition = selectPostion;
            currentWeek--;
        }
        getCurrent();
        dateAdapter = new DateAdapter(this, getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1 ? true : false, "");
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);

        if (selectPostion != -1) {
            // 侧滑日历控件刷新
            tvDate.setText(getSTime());
            updateFragmentData();
            dateAdapter.setSeclection(selectPostion);
        }
        gvFlag++;
        flipper1.addView(gridView, gvFlag);
        this.flipper1.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_right_in));
        this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_right_out));
        this.flipper1.showPrevious();
        flipper1.removeViewAt(0);
        return true;
    }

    /**
     * 向左滑
     *
     * @param gvFlag
     * @return
     */
    private boolean moveLeft(int gvFlag) {
        addGridView();
        if (!isCircle) {
            currentWeek++;
            selectPostion = -1;
        } else {
            if (++selectPostion < 7) {
                getCurrent();
                dateAdapter.setSeclection(selectPostion);
                dateAdapter.notifyDataSetChanged();
                tvDate.setText(getSTime());
                // prePosition = selectPostion;
                updateFragmentData();
                return true;
            }
            selectPostion = 0;
            // prePosition = selectPostion;
            currentWeek++;
        }
        getCurrent();
        dateAdapter = new DateAdapter(this, getResources(), currentYear,
                currentMonth, currentWeek, currentNum, selectPostion,
                currentWeek == 1 ? true : false, "");
        dayNumbers = dateAdapter.getDayNumbers();
        gridView.setAdapter(dateAdapter);

        if (selectPostion != -1) {
            tvDate.setText(getSTime());
            updateFragmentData();
            dateAdapter.setSeclection(selectPostion);
        }
        gvFlag++;
        flipper1.addView(gridView, gvFlag);
        this.flipper1.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_in));
        this.flipper1.setOutAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_out));
        this.flipper1.showNext();
        flipper1.removeViewAt(0);
        return true;
    }

    /**
     * 设置全屏滑动
     */
    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // return this.gestureDetector.onTouchEvent(event);
    // }
    private void initView() {
        tvLeft.setOnClickListener(onClickListener);
        tvTitle.setText(this.getString(R.string.report_title));
    }

    DecimalFormat timeDf = new DecimalFormat("00");

    private void initData() {
        manager = getSupportFragmentManager();
        trans = manager.beginTransaction();
        fragmentBase = new CarReportMainFragment();
        fragmentBase.setCarReportListener(this);
        Bundle bundle = new Bundle();
        bundle.putString("time", getSTime());
        fragmentBase.setArguments(bundle);
        trans.add(R.id.container, fragmentBase);
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.commit();
    }

    /**
     * 切换fragment
     *
     * @param index
     */
    public void changeFragment(int index) {
        // if (rid == 0) {
        // //
        // Toast.makeText(CarReportActivity.this, "暂无数据", Toast.LENGTH_SHORT)
        // .show();
        // return;
        // }

        String title = this.getString(R.string.report_title);
        switch (index) {
            // case 1:
            // // mSecondFragment = new CurrentFuelConsumptionFragment();
            // // title = this.getString(R.string.report_day_oil);
            // break;
            // case 2:
            // // mSecondFragment = new CarReportOilFrament();
            // // title = this.getString(R.string.report_avg_oil);
            // break;
            // case 3:
            // // mSecondFragment = new CarReportSpeedFragment();
            // // title = this.getString(R.string.report_avg_speed);
            // break;
            // case 4:
            // // mSecondFragment = new CarReportDriveTimeFragment();
            // // title = this.getString(R.string.report_drive_time);
            // break;
            // case 5:
            // // mSecondFragment = new CarReportMileFragment();
            // // title = this.getString(R.string.report_day_mile);
            // break;
            // case 6:
            // // mSecondFragment = new CarReportFreeFragment();
            // // title = this.getString(R.string.report_day_free);
            // break;
            case 7:
                showCustomDialog();
                mSecondFragment = new DrivingBehaviorFragment();
                title = this.getString(R.string.report_drive_behavior);
                tvTitle.setText(title);
                switchFragments(mSecondFragment);
                break;
            case 1000:
                if (mSecondFragment instanceof CarReportMainFragment) {

                } else {
                    showCustomDialog();
                    mSecondFragment = new CarReportMainFragment();
                    mSecondFragment.setCarReportListener(this);
                    tvTitle.setText(title);
                    switchFragments(mSecondFragment);
                }
                break;
            default:
                break;
        }

    }

    /**
     * 添加之后fragment
     *
     * @param mSecondFragment
     */
    private void switchFragments(final BaseFragment mSecondFragment) {

        if (mSecondFragment == null) {
            return;
        }
        Animator.AnimatorListener listener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator arg0) {
                if (StringUtil.isEmpty(manager)) {
                    manager = getSupportFragmentManager();
                }
                trans = manager.beginTransaction();
                trans.setCustomAnimations(R.anim.click_in, 0, 0, 0);
                trans.replace(R.id.container, mSecondFragment);
                Bundle bundle = new Bundle();
                bundle.putString("time", getSTime());
                bundle.putInt("rid", rid);
                mSecondFragment.setArguments(bundle);

                trans.commitAllowingStateLoss();
                fragmentBase = mSecondFragment;
            }
        };
        slideBack(listener, fragmentBase);
    }

    AnimatorSet animSet;

    /**
     * 移除现有fragment
     *
     * @param listener
     * @param mFirstFragment
     */
    public void slideBack(Animator.AnimatorListener listener,
                          Fragment mFirstFragment) {
        View movingFragmentView = mFirstFragment.getView();
        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat(
                "translationY", height);
        ObjectAnimator movingFragmentAnimator = ObjectAnimator
                .ofPropertyValuesHolder(movingFragmentView, translationY);

        animSet = new AnimatorSet();
        animSet.setDuration(500);
        animSet.playTogether(movingFragmentAnimator);
        animSet.addListener(listener);
        animSet.start();
    }

    private int height;

    /**
     * 计算fragment所占高度
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        height = mContainer.getHeight();
    }

    OnTouchListener onTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isCircle = false;
            return gestureDetector.onTouchEvent(event);

        }
    };

    OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.left_action:
                    onBackPressed();
                    break;
                case R.id.tv_date:
                    showDateView();
                    break;
                default:
                    break;
            }
        }
    };
    private DateTimeSelectorDialogBuilder builder;

    private void showDateView() {
        builder = DateTimeSelectorDialogBuilder
                .getInstance(CarReportActivity.this);
        builder.setOnSaveListener(saveListener);
        builder.setCurrentDate(sTime);
        builder.show();
    }

    OnSaveListener saveListener = new OnSaveListener() {

        @Override
        public void onSaveSelectedDate(String selectedDate) {
            if (isAfterToday(selectedDate)) {
                showToast(CarReportActivity.this
                        .getString(R.string.report_choose_after_today));
                return;
            }

            tvDate.setText(selectedDate);
            sTime = selectedDate;
            initCalendarData(selectedDate);
            selectPostion = 0;
            dateAdapter = new DateAdapter(CarReportActivity.this,
                    getResources(), currentYear, currentMonth, currentWeek,
                    currentNum, selectPostion, currentWeek == 1 ? true : false,
                    selectedDate);
            dayNumbers = dateAdapter.getDayNumbers();
            gridView.setAdapter(dateAdapter);
            selectPostion = dateAdapter.getTodayPosition();
            gridView.setSelection(selectPostion);
            flipper1.removeAllViews();
            flipper1.setInAnimation(null);
            flipper1.setOutAnimation(null);
            flipper1.addView(gridView, 0);
            updateFragmentData();

            builder.dismiss();
        }
    };

    /**
     * 判断时间是否在今天之后
     *
     * @param sDate
     * @return
     */
    private boolean isAfterToday(String sDate) {
        Date d = StringUtil.getDate(sDate, "yyyy-MM-dd");

        return d.after(dayBeforeToday.getTime());
    }

    @Override
    public void changeDate(int index) {
        isCircle = true;
        if (index == 0) {
            moveLeft(0);

            if (isAfterToday(sTime)) {
                moveRight(0);
                showToast(R.string.report_choose_after_today);
            }
        } else {
            moveRight(0);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSecondFragment == null) {
            CarReportActivity.this.finish();
            return;
        }
        if (mSecondFragment instanceof CarReportMainFragment) {
            CarReportActivity.this.finish();
        } else {
            // 切换至fragmentMain
            changeFragment(1000);
        }
    }

    /**
     * 从CarReportMainFragment里面获取数据
     *
     * @param hasNet
     * @param rid
     */
    public void setInfoFromMain(boolean hasNet, int rid) {
        this.rid = rid;
    }

    private CustomProgressDialog progressDialog;

    public void showCustomDialog() {
        if (progressDialog == null) {
            progressDialog = CustomProgressDialog.getInstance(this);
        }
        progressDialog.show();
    }

    public void disMissCustomDialog() {
        if (progressDialog != null) {
            progressDialog.onDestory();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disMissCustomDialog();
        manager = null;
        builder = null;
    }

}
