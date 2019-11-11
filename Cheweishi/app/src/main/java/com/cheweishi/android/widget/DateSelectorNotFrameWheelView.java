package com.cheweishi.android.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.ChooseDateBirthActivity;
import com.cheweishi.android.utils.DateUtils;

/**
 * 不带边框的日期选择器
 */
public class DateSelectorNotFrameWheelView extends RelativeLayout implements
		OnWheelChangedListener {
	private final String flag = "PfpsDateWheelView";
	private LinearLayout llWheelViews;
	private WheelView wvYear;//年滚轮
	private WheelView wvMonth;//月滚轮
	private WheelView wvDay;//日滚轮
	private String[] years = new String[100];//年份数组
	private String[] months = new String[12];//月份数组
	private String[] tinyDays = new String[28];//非闰年二月日数组
	private String[] smallDays = new String[29];//闰年二月日数组
	private String[] normalDays = new String[30];//除二月外的小月日数组
	private String[] bigDays = new String[31];//大月数组
	private StrericWheelAdapter yearsAdapter;//年滚轮适配器
	private StrericWheelAdapter monthsAdapter;//月滚轮适配器
	private StrericWheelAdapter tinyDaysAdapter;//非闰年二月日滚轮适配器
	private StrericWheelAdapter smallDaysAdapter;//闰年二月日滚轮适配器
	private StrericWheelAdapter bigDaysAdapter;//大月日滚轮适配器
	private StrericWheelAdapter normalDaysAdapter;//小月日滚轮适配器
	private String year = "";//年
	private String month = "";//月
	private String day = "";//日
	private String date = null;//日期
	private int age;//年龄

	public DateSelectorNotFrameWheelView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initLayout(context);
	}

	public DateSelectorNotFrameWheelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initLayout(context);
	}

	public DateSelectorNotFrameWheelView(Context context) {
		super(context);
		initLayout(context);
	}
	/**
	 * 初始化布局
	 * @param context
	 */
	private void initLayout(Context context) {
		LayoutInflater.from(context).inflate(
				R.layout.dete_time_notframe_layout, this, true);
		llWheelViews = (LinearLayout) findViewById(R.id.ll_wheel_views);
		wvYear = (WheelView) findViewById(R.id.wv_date_of_year);
		wvMonth = (WheelView) findViewById(R.id.wv_date_of_month);
		wvDay = (WheelView) findViewById(R.id.wv_date_of_day);
		setData();
		wvYear.addChangingListener(this);
		wvMonth.addChangingListener(this);
		wvDay.addChangingListener(this);
		date = getToday();
		((ChooseDateBirthActivity) context).setAgeText(0, date);
	}
	
	/**
	 * 添加年月日数据
	 */
	private void setData() {
		//添加年份
		for (int i = 0; i < years.length; i++) {
			years[i] = 1960 + i + " 年";
		}
		//添加月份
		for (int i = 0; i < months.length; i++) {
			if (i < 9) {
				months[i] = "0" + (1 + i) + " 月";
			} else {
				months[i] = (1 + i) + " 月";
			}
		}
		//添加非闰年二月天数
		for (int i = 0; i < tinyDays.length; i++) {
			if (i < 9) {
				tinyDays[i] = "0" + (1 + i) + " 日";
			} else {
				tinyDays[i] = (1 + i) + " 日";
			}
		}
		//添加闰年二月天数
		for (int i = 0; i < smallDays.length; i++) {
			if (i < 9) {
				smallDays[i] = "0" + (1 + i) + " 日";
			} else {
				smallDays[i] = (1 + i) + " 日";
			}
		}
		//添加小月天数
		for (int i = 0; i < normalDays.length; i++) {
			if (i < 9) {
				normalDays[i] = "0" + (1 + i) + " 日";
			} else {
				normalDays[i] = (1 + i) + " 日";
			}
		}
		//添加大月天数
		for (int i = 0; i < bigDays.length; i++) {
			if (i < 9) {
				bigDays[i] = "0" + (1 + i) + " 日";
			} else {
				bigDays[i] = (1 + i) + " 日";
			}
		}
		
		//创建适配器
		yearsAdapter = new StrericWheelAdapter(years);
		monthsAdapter = new StrericWheelAdapter(months);
		tinyDaysAdapter = new StrericWheelAdapter(tinyDays);
		smallDaysAdapter = new StrericWheelAdapter(smallDays);
		normalDaysAdapter = new StrericWheelAdapter(normalDays);
		bigDaysAdapter = new StrericWheelAdapter(bigDays);
		
		//年月日滚轮设置适配器
		wvYear.setAdapter(yearsAdapter);
		wvYear.setCurrentItem(getTodayYear());
		wvYear.setCyclic(true);
		wvMonth.setAdapter(monthsAdapter);
		wvMonth.setCurrentItem(getTodayMonth());
		wvMonth.setCyclic(true);
		if (isBigMonth(getTodayMonth() + 1)) {
			wvDay.setAdapter(bigDaysAdapter);
		} else if (getTodayMonth() == 1
				&& isLeapYear(wvYear.getCurrentItemValue().subSequence(0, 4)
						.toString().trim())) {
			wvDay.setAdapter(smallDaysAdapter);
		} else if (getTodayMonth() == 1) {
			wvDay.setAdapter(tinyDaysAdapter);
		} else {
			wvDay.setAdapter(normalDaysAdapter);
		}
		wvDay.setCurrentItem(getTodayDay());
		wvDay.setCyclic(true);
		
		//获取默认值得年月日
		year = wvYear.getCurrentItemValue().subSequence(0, 4).toString().trim();
		month = wvMonth.getCurrentItemValue().subSequence(0, 2).toString()
				.trim();
		day = wvDay.getCurrentItemValue().subSequence(0, 2).toString().trim();
	}

	/**
	 * 获取当前日期的天数的日子
	 * 
	 * @return
	 */
	private int getTodayDay() {
		// 2015年12月01日
		int position = 0;
		String today = getToday();
		String day = today.substring(8, 10);
		day = day + " 日";
		for (int i = 0; i < bigDays.length; i++) {
			if (day.equals(bigDays[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 获取当前日期的月数的位置
	 * 
	 * @return
	 */
	private int getTodayMonth() {
		// 2015年12月01日
		int position = 0;
		String today = getToday();
		String month = today.substring(5, 7);
		month = month + " 月";
		for (int i = 0; i < months.length; i++) {
			if (month.equals(months[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 获取当天的年份
	 * 
	 * @return
	 */
	private int getTodayYear() {
		int position = 0;
		String today = getToday();
		String year = today.substring(0, 4);
		year = year + " 年";
		for (int i = 0; i < years.length; i++) {
			if (year.equals(years[i])) {
				position = i;
				break;
			}
		}
		return position;
	}

	/**
	 * 设置当前显示的年份
	 * 
	 * @param year
	 */
	public void setCurrentYear(String year) {
		boolean overYear = true;
		year = year + " 年";
		for (int i = 0; i < years.length; i++) {
			if (year.equals(years[i])) {
				wvYear.setCurrentItem(i);
				overYear = false;
				break;
			}
		}
		if (overYear) {
			Log.e(flag, "设置的年份超出了数组的范围");
		}
	}

	/**
	 * 设置当前显示的月份
	 * 
	 * @param month
	 */
	public void setCurrentMonth(String month) {
		month = month + " 月";
		for (int i = 0; i < months.length; i++) {
			if (month.equals(months[i])) {
				wvMonth.setCurrentItem(i);
				break;
			}
		}
	}

	/**
	 * 设置当前显示的日期号
	 * 
	 * @param day
	 *            14
	 */
	public void setCurrentDay(String day) {
		day = day + " 日";
		for (int i = 0; i < smallDays.length; i++) {
			if (day.equals(smallDays[i])) {
				wvDay.setCurrentItem(i);
				break;
			}
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

	/**
	 * 判断是否是大月
	 * 
	 * @param month
	 * @return
	 */
	private boolean isBigMonth(int month) {
		boolean isBigMonth = false;
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			isBigMonth = true;
			break;

		default:
			isBigMonth = false;
			break;
		}
		return isBigMonth;
	}

	int currentMonth = 1;

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		String trim = null;
		switch (wheel.getId()) {
		case R.id.wv_date_of_year:
			trim = DateUtils.splitDateString(wvYear.getCurrentItemValue())
					.trim();
			if (isLeapYear(trim)) {
				if (currentMonth == 2) {
					wvDay.setAdapter(smallDaysAdapter);
				} else if (isBigMonth(currentMonth)) {
					wvDay.setAdapter(bigDaysAdapter);
				} else {
					wvDay.setAdapter(normalDaysAdapter);
				}
			} else if (currentMonth == 2) {
				wvDay.setAdapter(tinyDaysAdapter);
			} else if (isBigMonth(currentMonth)) {
				wvDay.setAdapter(bigDaysAdapter);
			} else {
				wvDay.setAdapter(normalDaysAdapter);
			}
			year = trim;
			break;
		case R.id.wv_date_of_month:
			trim = DateUtils.splitDateString(wvMonth.getCurrentItemValue())
					.trim();
			currentMonth = Integer.parseInt(trim);
			switch (currentMonth) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				wvDay.setAdapter(bigDaysAdapter);
				break;
			case 2:
				String yearString = DateUtils.splitDateString(
						wvYear.getCurrentItemValue()).trim();
				if (isLeapYear(yearString)) {
					wvDay.setAdapter(smallDaysAdapter);
				} else {
					wvDay.setAdapter(tinyDaysAdapter);
				}
				break;
			default:
				wvDay.setAdapter(normalDaysAdapter);
				break;
			}
			month = trim;
			break;
		case R.id.wv_date_of_day:
			trim = DateUtils.splitDateString(wvDay.getCurrentItemValue())
					.trim();
			Log.i("result", "==daytrim==" + trim);
			day = trim;
			break;
		}
		date = year + "-" + month + "-" + day;
		if (isSize(date)) {
			wvYear.setCurrentItem(getTodayYear());
			wvMonth.setCurrentItem(getTodayMonth());
			wvDay.setCurrentItem(getTodayDay());
		}
		age = Integer.parseInt(getToday().substring(0, 4).toString())
				- Integer.parseInt(DateUtils.splitDateString(
						wvYear.getCurrentItemValue()).trim());
		if (age >= 0) {
			ChooseDateBirthActivity.instance.setAgeText(age, date);
		} else {
			ChooseDateBirthActivity.instance.setAgeText(0, date);
		}
	}

	/**
	 * 判断日期大小
	 */
	@SuppressLint("SimpleDateFormat")
	private boolean isSize(String date) {
		// 设定时间的模板
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 得到指定模范的时间
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(date);
			d2 = sdf.parse(getToday());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 比较
		return d1.getTime() - d2.getTime() > 0;
	}

	/**
	 * 获取今天的日期
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String getToday() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = formatter.format(curDate);
		return str;
	}

}
