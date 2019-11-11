package com.cheweishi.android.adapter;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.config.Constant;
import com.cheweishi.android.utils.SpecialCalendar;
import com.cheweishi.android.utils.StringUtil;

/**
 * 报告日历gridview
 * 
 * @author zhangq
 * 
 */
public class DateAdapter extends BaseAdapter {
	private static String TAG = "ZzL";
	private boolean isLeapyear = false; // 是否为闰年
	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几
	private int nextDayOfWeek = 0;
	private int lastDayOfWeek = 0;
	private int lastDaysOfMonth = 0; // 上一个月的总天数
	private int eachDayOfWeek = 0;
	private Context context;
	private SpecialCalendar sc = null;
	private Resources res = null;
	private Drawable drawable = null;
	private String[] dayNumber = new String[7];
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private DecimalFormat df = new DecimalFormat("00");
	private int currentFlag = -1; // 用于标记当天
	private String nowDate = "";// 当前时间
	private int outstrip = -1;
	private int colorBlack;
	private int colorOrange;
	private int colorGray;

	private String sdate;
	// 系统当前时间
	private String sysDate = "";// 选中时间
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";
	private String currentYear = "";
	private String currentMonth = "";
	private String currentWeek = "";
	private String currentDay = "";
	private int weeksOfMonth;
	private int default_postion;
	private int clickTemp = -1;
	private int week_num = 0;
	private int week_c = 0;
	private int month = 0;
	private int jumpWeek = 0;
	private int c_month = 0;
	private int c_day_week = 0;
	private int n_day_week = 0;
	private boolean isStart;
	private String clickDate= "";

	// 标识选择的Item
	public void setSeclection(int position) {
		clickTemp = position;
//		clickDate = getClickDate(clickTemp);
	}

	public void setDefaultPosition(int position) {
		default_postion = position;
	}

	public DateAdapter(String sDate) {
		Date date = new Date();
		sdate = sdf.format(date);
		if (StringUtil.isEmpty(sDate)) {
			sysDate = sdate; // 当期日期
		} else {
			sysDate = sDate;
		}
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
		month = Integer.parseInt(sys_month);

		nowDate = sdf.format(Calendar.getInstance().getTime());
	}

	public DateAdapter(Context context, Resources rs, int year_c, int month_c,
			int week_c, int week_num, int default_postion, boolean isStart,
			String sDate) {
		this(sDate);
		this.context = context;
		this.res = rs;
		this.default_postion = default_postion;
		this.week_c = week_c;
		this.isStart = isStart;
		this.colorOrange = rs.getColor(R.color.orange_text_color);
		this.colorBlack = rs.getColor(R.color.gray_normal);
		this.colorGray = rs.getColor(R.color.gray);
		sc = new SpecialCalendar();

		lastDayOfWeek = sc.getWeekDayOfLastMonth(year_c, month_c,
				sc.getDaysOfMonth(sc.isLeapYear(year_c), month_c));
		Log.i(TAG, "week_c:" + week_c);
		currentYear = String.valueOf(year_c);
		// 得到当前的年份
		currentMonth = String.valueOf(month_c); // 得到本月
												// （jumpMonth为滑动的次数，每滑动一次就增加一月或减一月）
		currentDay = String.valueOf(sys_day); // 得到当前日期是哪天
		getCalendar(Integer.parseInt(currentYear),
				Integer.parseInt(currentMonth));
		currentWeek = String.valueOf(week_c);
		getWeek(Integer.parseInt(currentYear), Integer.parseInt(currentMonth),
				Integer.parseInt(currentWeek));

	}

	public int getTodayPosition() {
		int todayWeek = sc.getWeekDayOfLastMonth(Integer.parseInt(sys_year),
				Integer.parseInt(sys_month), Integer.parseInt(sys_day));
		Log.i("zzqq", "--sys_day--" + sys_day);
		if (todayWeek == 7) {
			clickTemp = 0;
		} else {
			clickTemp = todayWeek;
		}
//		clickDate = getClickDate(clickTemp);
		return clickTemp;
	}

	public int getCurrentMonth(int position) {
		int thisDayOfWeek = sc.getWeekdayOfMonth(Integer.parseInt(currentYear),
				Integer.parseInt(currentMonth));
		if (isStart) {
			if (thisDayOfWeek != 7) {
				if (position < thisDayOfWeek) {
					return Integer.parseInt(currentMonth) - 1 == 0 ? 12
							: Integer.parseInt(currentMonth) - 1;
				} else {
					return Integer.parseInt(currentMonth);
				}
			} else {
				return Integer.parseInt(currentMonth);
			}
		} else {
			return Integer.parseInt(currentMonth);
		}

	}

	public int getCurrentYear(int position) {
		int thisDayOfWeek = sc.getWeekdayOfMonth(Integer.parseInt(currentYear),
				Integer.parseInt(currentMonth));
		if (isStart) {
			if (thisDayOfWeek != 7) {
				if (position < thisDayOfWeek) {
					return Integer.parseInt(currentMonth) - 1 == 0 ? Integer
							.parseInt(currentYear) - 1 : Integer
							.parseInt(currentYear);
				} else {
					return Integer.parseInt(currentYear);
				}
			} else {
				return Integer.parseInt(currentYear);
			}
		} else {
			return Integer.parseInt(currentYear);
		}
	}

	public void getCalendar(int year, int month) {
		isLeapyear = sc.isLeapYear(year); // 是否为闰年
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month); // 某月的总天数
		dayOfWeek = sc.getWeekdayOfMonth(year, month); // 某月第一天为星期几
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1);
		nextDayOfWeek = sc.getDaysOfMonth(isLeapyear, month + 1);
	}

	public void getWeek(int year, int month, int week) {
		for (int i = 0; i < dayNumber.length; i++) {
			if (dayOfWeek == 7) {
				dayNumber[i] = String.valueOf((i + 1) + 7 * (week - 1));
			} else {
				if (week == 1) {
					if (i < dayOfWeek) {
						dayNumber[i] = String.valueOf(lastDaysOfMonth
								- (dayOfWeek - (i + 1)));
					} else {
						dayNumber[i] = String.valueOf(i - dayOfWeek + 1);
					}
				} else {
					dayNumber[i] = String.valueOf((7 - dayOfWeek + 1 + i) + 7
							* (week - 2));
				}
			}

		}
	}

	public String[] getDayNumbers() {
		return dayNumber;
	}

	/**
	 * 得到某月有几周(特殊算法)
	 */
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

	/**
	 * 某一天在第几周
	 */
	public void getDayInWeek(int year, int month) {

	}

	@Override
	public int getCount() {
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_calendar, null);
		}
		TextView tvCalendar = (TextView) convertView
				.findViewById(R.id.tv_calendar);

		// 设置字体颜色、背景圆圈
		if (isToday(position)) {
//			outstrip = position;
			tvCalendar.setTextColor(colorOrange);
			if (default_postion != -1) {
				tvCalendar
						.setBackgroundResource(R.drawable.report_calendar_red);
			}
		} else {
			if (compare_date(getDate(),position) == -1) {
				tvCalendar.setTextColor(colorGray);
			} else {
				tvCalendar.setTextColor(colorBlack);
			}
			if (default_postion != -1) {
				tvCalendar
						.setBackgroundResource(R.drawable.report_calendar_red);
			}
		}
		tvCalendar.setText(dayNumber[position]);
		
		if ((clickTemp == position && default_postion != -1) || (compare_date(Constant.CAR_REPORT_DATE, position) == 0 && default_postion == -1)) {
			tvCalendar.setSelected(true);
			tvCalendar.setTextColor(Color.WHITE);
			Constant.CAR_REPORT_DATE = getClickDate(position);
			tvCalendar
			   .setBackgroundResource(R.drawable.report_calendar_red);
		} else {
			tvCalendar.setSelected(false);
			tvCalendar.setBackgroundColor(Color.TRANSPARENT);
		}
		return convertView;
	}

	private boolean isToday(int position) {
		String s = getClickDate(position);
		return sdate.equals(s);
	}
	
	/**
	 *转换选择项日期
	 * @param position
	 * @return
	 */
	public String getClickDate(int position) {
		String s = getCurrentYear(position) + "-"
				+ df.format(getCurrentMonth(position)) + "-"
				+ df.format(StringUtil.getInt(dayNumber[position]));
		return s;
	}
	
	/**
	 * 两个日期的比较
	 * @param date
	 * @param position
	 * @return
	 */
	private int compare_date(String date,int position) {
        
		String s = getClickDate(position);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(date);
            Date dt2 = df.parse(s);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else if(dt1.getTime() == dt2.getTime()){
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
	
	/**
	 * 获取当前日期
	 * @return
	 */
	private String getDate(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");    
		String date=sdf.format(new java.util.Date());
		return date;
	}
}
