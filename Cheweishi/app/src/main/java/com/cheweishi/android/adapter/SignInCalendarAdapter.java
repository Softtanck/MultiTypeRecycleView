package com.cheweishi.android.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.cheweishi.android.R;

/**
 * 签到详情gridview
 * 
 * @author zhangq
 * 
 */
public class SignInCalendarAdapter extends BaseAdapter {
	private Calendar calToday = Calendar.getInstance();
	private Calendar calStartDate = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();
	private int selectMonth;
	private Context mContext;
	private Resources resources;

	private ArrayList<Date> dates;
	private ArrayList<Date> signinDates;

	public SignInCalendarAdapter(Context context, Calendar calendar,
			ArrayList<Date> list) {
		this.mContext = context;
		this.calStartDate = calendar;
		dates = getDates();
		resources = context.getResources();
		signinDates = list;
	}

	// 根据改变的日期更新日历
	// 填充日历控件用
	private void UpdateStartDateForMonth() {
		calStartDate.set(Calendar.DATE, 1); // 设置成当月第一天
		selectMonth = calStartDate.get(Calendar.MONTH);// 得到当前日历显示的月

		// 星期一是2 星期天是1 填充剩余天数
		int iDay = 0;
		int iFirstDayOfWeek = Calendar.MONDAY;
		int iStartDay = iFirstDayOfWeek;
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);

		calStartDate.add(Calendar.DAY_OF_MONTH, -1);// 周日第一位

	}

	private ArrayList<Date> getDates() {

		UpdateStartDateForMonth();

		ArrayList<Date> alArrayList = new ArrayList<Date>();

		for (int i = 1; i <= 42; i++) {
			alArrayList.add(calStartDate.getTime());
			calStartDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return alArrayList;
	}

	@Override
	public int getCount() {
		return dates == null ? 0 : dates.size();
	}

	@Override
	public Object getItem(int position) {
		return dates.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		LinearLayout iv = new LinearLayout(mContext);
		iv.setId(position + 5000);
		iv.setGravity(Gravity.CENTER);
		iv.setOrientation(1);
		iv.setBackgroundResource(R.drawable.qiandao_kuang3x);
		Date myDate = (Date) getItem(position);
		Calendar calCalendar = Calendar.getInstance();
		calCalendar.setTime(myDate);

		final int iMonth = calCalendar.get(Calendar.MONTH);
		if (iMonth == selectMonth && isSignin(myDate)) {
			// iv.setBackgroundColor(resources.getColor(R.color.white));
			iv.setBackgroundResource(R.drawable.sign_qiandao2);
		}

		// 日期开始
		TextView txtDay = new TextView(mContext);// 日期
		txtDay.setGravity(Gravity.CENTER_HORIZONTAL);

		// 判断是否是当前月
		if (iMonth == selectMonth) {
			txtDay.setTextColor(resources.getColor(R.color.black));
		} else {
			txtDay.setTextColor(resources.getColor(R.color.dark_gray));
		}

		int day = myDate.getDate(); // 日期
		txtDay.setId(position + 500);
		iv.setTag(myDate);
		if (iMonth == selectMonth && isSignin(myDate)) {
			// 当前日期
		} else {
			txtDay.setText(String.valueOf(day));
		}

		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		iv.addView(txtDay, lp);

		return iv;
	}

	OnClickListener view_listener = new OnClickListener() {

		@Override
		public void onClick(View view) {
			view.setBackgroundColor(resources
					.getColor(android.R.color.darker_gray));
		}
	};

	public void setSelectedDate(Calendar cal) {
		calSelected = cal;
	}

	private Boolean equalsDate(Date date1, Date date2) {

		return date1.getYear() == date2.getYear()
				&& date1.getMonth() == date2.getMonth()
				&& date1.getDate() == date2.getDate();

	}

	private boolean isSignin(Date date) {
		if (signinDates != null) {
			for (Date d : signinDates) {
				if (equalsDate(date, d)) {
					return true;
				}
			}
		}
		return false;
	}
}
