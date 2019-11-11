package com.cheweishi.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.cheweishi.android.R;
import com.cheweishi.android.entity.CarReporMileViewInfoNative;
import com.cheweishi.android.entity.CarReportMileInfoNative;
import com.cheweishi.android.tools.ScreenTools;
import com.cheweishi.android.utils.DisplayUtil;

public class CarReportMileView extends View {
	private Context context;
	private int gray = 0;
	private int yellow = 0;
	private int yellow_background = 0;
	private int gray_background = 0;
//	private int white = 0;
	private float width = 0;// 屏幕宽度
	private float heigth = 0;// 屏幕高度
	private float unitWidth = 0;// 表示一分钟占的像素

	private float kuangLeft = 0;// 外框左边坐标
	private float kuangTop = 0;// 外框上部坐标
	private float kuangRight = 0;// 外框右边坐标
//	private float kuangBottom = 0;// 外框底部坐标
	private float newKuangLeft = 0;// 外框左边坐标
	private float newKuangTop = 0;// 外框上部坐标
	private float newKuangRight = 0;// 外框右边坐标
//	private float newKuangBottom = 0;// 外框底部坐标
	private float kuangWidth = 0;// 实际框宽
	private float newKuangWidth = 0;// 下一个框的宽
	private float kuangHeigth = 0;// 实际框宽
	private float newKuangHeigth = 0;// 下一个框的宽
	private String mile = "";// 历史里程数

	private float XStartx = 0;// 坐标轴的起点x坐标
	private float XStarty = 0;// 坐标轴的起点y坐标
	private float XStopx;// 坐标轴的起点x坐标
//	private float XStopy = 0;// 坐标轴的起点y坐标
	private float screenScaleY = 0.28f;// 占屏幕比例
	private float hisScaleY = 0.66f;// 历史里程高度比例系数
	private float hisTop = 0;// 历史里程顶部坐标
	private float hisBottom = 0;// 历史里程底部坐标
	private float hisleft = 0;// 历史里程左边坐标
	private float hisRigth = 0;// 历史里程右边坐标
	private float hisWidth = 0;// 历史里程宽度
	private float mobile = 0;// 移动距离
	private List<CarReporMileViewInfoNative> mileViewInfoList;// 里程矩形图信息列表
//	private List<CarReportMileInfoNative> mileInfoList;// 里程单个里程信息列表
//	private String date = "";// 日期
	private float maxMile;// 最大里程
	private float minMile;// 最小里程
	private int isX = 0;// 0:表示被点击位置在框左边、1表示被点击位置在框右 、2：表示被点击位置在框范围内
//	private float isY = 0;// 点击事件Y判断
	private float speed = 20;// 移动速度
	private boolean isShow = true;// 是否显示数据说明
	private boolean isHis = false;// 判断点击的是否是历史里程
	private int isDown = 0;// 表示点击的是当日里程
	private int j = 0;// 外框的默认选项
	private ValueAnimator animator;
	private boolean isNotData = true;
	private String status = "";
	private float backgroundRectWidth = 0;
	/** 0表示全部 ，其他表示单选 */
	private int type = 0;

	public CarReportMileView(Context context) {
		super(context);
		this.context = context;
		mileViewInfoList = new ArrayList<CarReporMileViewInfoNative>();
		// 数据初始化
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		width = display.getWidth();
		heigth = display.getHeight();
//		white = context.getResources().getColor(R.color.white);
		yellow = context.getResources().getColor(R.color.main_blue);
		yellow_background = context.getResources().getColor(R.color.light_blue);
		gray = context.getResources().getColor(R.color.gray_normal);
		gray_background = context.getResources().getColor(
				R.color.gray_backgroud);
		backgroundRectWidth = width - DisplayUtil.dip2px(context, 20);
		hisBottom = heigth * screenScaleY;
		animator = ValueAnimator.ofFloat(0, 1);
		// animator.setDuration(200);
		// animator.setCurrentPlayTime(200);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				if (isX == 0) {
					speed = mobile / 10;
					kuangRight = (kuangRight - speed);
					if (kuangWidth > newKuangWidth) {
						kuangLeft = (kuangLeft - (speed - (speed
								* (kuangWidth - newKuangWidth) / mobile)));
						if (kuangHeigth > newKuangHeigth) {
							kuangTop = kuangTop + speed
									* (kuangHeigth - newKuangHeigth) / mobile;
						} else if (kuangHeigth < newKuangHeigth) {
							kuangTop = kuangTop - speed
									* (newKuangHeigth - kuangHeigth) / mobile;
						}
					} else if (kuangWidth < newKuangWidth) {
						kuangLeft = (kuangLeft - (speed + speed
								* (newKuangWidth - kuangWidth) / mobile));
						if (kuangHeigth > newKuangHeigth) {
							kuangTop = kuangTop + speed
									* (kuangHeigth - newKuangHeigth) / mobile;
						} else if (kuangHeigth < newKuangHeigth) {
							kuangTop = kuangTop - speed
									* (newKuangHeigth - kuangHeigth) / mobile;
						}

					} else if (kuangWidth == newKuangWidth) {
						kuangLeft = (kuangLeft - speed);
						if (kuangHeigth > newKuangHeigth) {
							kuangTop = kuangTop + speed
									* (kuangHeigth - newKuangHeigth) / mobile;
						} else if (kuangHeigth < newKuangHeigth) {
							kuangTop = kuangTop - speed
									* (newKuangHeigth - kuangHeigth) / mobile;
						}
					}

					if (kuangRight <= newKuangRight + 0.5) {
						animator.cancel();
						kuangLeft = newKuangLeft;
						kuangRight = newKuangRight;
//						kuangBottom = newKuangBottom;
						kuangTop = newKuangTop;
						isShow = true;
					}
				} else if (isX == 1) {
					speed = mobile / 10;
					kuangLeft = kuangLeft + speed;
					if (kuangWidth > newKuangWidth) {
						kuangRight = (kuangRight + (speed - speed
								* (kuangWidth - newKuangWidth) / mobile));
						if (kuangHeigth > newKuangHeigth) {
							kuangTop = kuangTop + speed
									* (kuangHeigth - newKuangHeigth) / mobile;
						} else if (kuangHeigth < newKuangHeigth) {
							kuangTop = kuangTop - speed
									* (newKuangHeigth - kuangHeigth) / mobile;
						}
					} else if (kuangWidth < newKuangWidth) {
						kuangRight = (kuangRight + (speed + (speed
								* (newKuangWidth - kuangWidth) / mobile)));
						if (kuangHeigth > newKuangHeigth) {
							kuangTop = kuangTop + speed
									* (kuangHeigth - newKuangHeigth) / mobile;
						} else if (kuangHeigth < newKuangHeigth) {
							kuangTop = kuangTop - speed
									* (newKuangHeigth - kuangHeigth) / mobile;
						}
					} else if (kuangWidth == newKuangWidth) {
						kuangRight = (kuangRight + speed);
						if (kuangHeigth > newKuangHeigth) {
							kuangTop = kuangTop + speed
									* (kuangHeigth - newKuangHeigth) / mobile;
						} else if (kuangHeigth < newKuangHeigth) {
							kuangTop = kuangTop - speed
									* (newKuangHeigth - kuangHeigth) / mobile;
						}
					}

					if (kuangLeft >= newKuangLeft - 0.5) {
						animator.cancel();
						kuangLeft = newKuangLeft;
						kuangRight = newKuangRight;
//						kuangBottom = newKuangBottom;
						kuangTop = newKuangTop;
						isShow = true;
					}
				} else if (isX == 2) {
					if (animator.isRunning()) {
						animator.cancel();
						isShow = true;
					}
				}
				invalidate();
			}
		});
		animator.setRepeatCount(ValueAnimator.INFINITE);
	}

	public CarReportMileView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(
				(int) (ScreenTools.getScreentWidth((Activity) context) * 0.87f)
						+ ScreenTools.getScreentWidth((Activity) context),
				ScreenTools.getScreentHeight((Activity) context) / 3
						- DisplayUtil.dip2px(context, 10));
	}

	// }

	@Override
	protected void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		// 去锯齿
		paint.setAntiAlias(true);
		// 绘制背景圆角长方形
		drawRoundRect(canvas, paint);
		if (!isNotData) {
			// 绘制底部文字
			drawBottomText(canvas, paint);
			if (isHis) {
				// 绘制历史记录
				drawHisRecord(canvas, paint);
			}

			// 绘制当日数据矩形
			drawSamedayRect(canvas, paint);

			// 绘制数据说明
			drawDataExplain(canvas, paint);
		}

		super.onDraw(canvas);
	}

	/**
	 * 绘制数据说明
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawDataExplain(Canvas canvas, Paint paint) {
		if (isHis == true && isShow == false) {// 历史里程说明
			// 画小圆点
			paint.setColor(gray);
			paint.setStrokeWidth(8);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(hisleft + hisWidth / 2,
					hisTop - DisplayUtil.dip2px(context, 5),
					DisplayUtil.dip2px(context, 3), paint);
			// 画说明线
			paint.setStrokeWidth(2);

			canvas.drawLine(hisleft + hisWidth / 2,
					hisTop - DisplayUtil.dip2px(context, 5), hisleft + hisWidth
							/ 2 + DisplayUtil.dip2px(context, 10), hisTop
							- DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, paint);

			canvas.drawLine(
					hisleft + hisWidth / 2 + DisplayUtil.dip2px(context, 10),
					hisTop - DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, hisleft + hisWidth / 2
							+ DisplayUtil.dip2px(context, 10) + hisWidth * 2,
					hisTop - DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, paint);

			// 画文字说明
			paint.setStrokeWidth(1);
			paint.setTextAlign(Align.LEFT);
			paint.setTextSize(DisplayUtil.sp2px(context, 14));
			canvas.drawText(mile + "km", hisleft + (hisRigth - hisleft) / 2
					+ DisplayUtil.dip2px(context, 10),
					hisTop - DisplayUtil.dip2px(context, 10)
							- (hisBottom - hisTop) / 4, paint);
		} else if (isShow == true && isHis == false) {
			// 画右边小圆点
			float kuangStartX = 0;
			paint.setColor(yellow);
			paint.setStrokeWidth(8);
			paint.setStyle(Paint.Style.FILL);
			if (kuangWidth < DisplayUtil.dip2px(context, 4)) {
				kuangStartX = kuangRight - kuangWidth / 2;
			} else {
				kuangStartX = kuangRight - DisplayUtil.dip2px(context, 2);
			}
			canvas.drawCircle(kuangStartX,
					kuangTop - DisplayUtil.dip2px(context, 5),
					DisplayUtil.dip2px(context, 3), paint);

			// 画右边说明线(开始时间、终止时间)
			paint.setStrokeWidth(2);
			canvas.drawLine(
					kuangStartX,
					kuangTop - DisplayUtil.dip2px(context, 5),
					kuangRight - DisplayUtil.dip2px(context, 5)
							+ DisplayUtil.dip2px(context, 10), kuangTop
							- DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, paint);
			canvas.drawLine(
					kuangRight - DisplayUtil.dip2px(context, 5)
							+ DisplayUtil.dip2px(context, 10),
					kuangTop - DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4,
					kuangRight - DisplayUtil.dip2px(context, 5)
							+ DisplayUtil.dip2px(context, 10) + hisWidth * 2f,
					kuangTop - DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, paint);

			// 画右边文字说明(开始时间)
			paint.setStrokeWidth(1);
			paint.setTextAlign(Align.LEFT);
			paint.setTextSize(DisplayUtil.sp2px(context, 12));
			canvas.drawText(
					"起:" + mileViewInfoList.get(isDown).getStartTime(),
					kuangRight - DisplayUtil.dip2px(context, 5)
							+ DisplayUtil.dip2px(context, 10), kuangTop
							- DisplayUtil.dip2px(context, 10)
							- (hisBottom - hisTop) / 4, paint);
			// 画右边文字说明(停止时间)
			paint.setStrokeWidth(1);
			paint.setTextSize(DisplayUtil.sp2px(context, 12));
			canvas.drawText(
					"止:" + mileViewInfoList.get(isDown).getEndTime(),
					kuangRight - DisplayUtil.dip2px(context, 5)
							+ DisplayUtil.dip2px(context, 10), kuangTop
							+ DisplayUtil.dip2px(context, 7)
							- (hisBottom - hisTop) / 4, paint);
			// 画左边小圆点
			paint.setStrokeWidth(8);
			paint.setStyle(Paint.Style.FILL);
			if (kuangWidth < DisplayUtil.dip2px(context, 4)) {
				kuangStartX = kuangLeft + kuangWidth / 2;
			} else {
				kuangStartX = kuangLeft + DisplayUtil.dip2px(context, 2);
			}
			canvas.drawCircle(kuangStartX,
					kuangTop - DisplayUtil.dip2px(context, 5),
					DisplayUtil.dip2px(context, 3), paint);
			// 画左边说明线(里程)
			paint.setStrokeWidth(2);
			canvas.drawLine(
					kuangStartX,
					kuangTop - DisplayUtil.dip2px(context, 5),
					kuangLeft + DisplayUtil.dip2px(context, 5)
							- DisplayUtil.dip2px(context, 10), kuangTop
							- DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, paint);
			canvas.drawLine(
					kuangLeft + DisplayUtil.dip2px(context, 5)
							- DisplayUtil.dip2px(context, 10),
					kuangTop - DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4,
					kuangLeft + DisplayUtil.dip2px(context, 5)
							- DisplayUtil.dip2px(context, 10) - hisWidth * 2f,
					kuangTop - DisplayUtil.dip2px(context, 5)
							- (hisBottom - hisTop) / 4, paint);
			// 画左边文字说明(里程)
			paint.setStrokeWidth(1);
			paint.setTextAlign(Align.RIGHT);
			paint.setTextSize(DisplayUtil.sp2px(context, 14));
			canvas.drawText(
					mileViewInfoList.get(isDown).getDriverMile() + "km",
					kuangLeft + DisplayUtil.dip2px(context, 5)
							- DisplayUtil.dip2px(context, 15), kuangTop
							- DisplayUtil.dip2px(context, 10)
							- (hisBottom - hisTop) / 4, paint);
		}
	}

	/**
	 * 绘制当日数据矩形
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawSamedayRect(Canvas canvas, Paint paint) {
		for (int i = 0; i < mileViewInfoList.size(); i++) {
			if (type == 0) {
				paint.setColor(yellow);
			} else {
				if (isDown == i) {
					paint.setColor(yellow);
				} else {
					paint.setColor(yellow_background);
				}
			}
			RectF rectF = new RectF();
			rectF.left = mileViewInfoList.get(i).getXleft();
			rectF.top = mileViewInfoList.get(i).getYTop();
			rectF.right = mileViewInfoList.get(i).getXRinth();
			rectF.bottom = mileViewInfoList.get(i).getYBottm();
			canvas.drawRoundRect(rectF, 1, 1, paint);
		}
	}

	/**
	 * 绘制历史记录
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawHisRecord(Canvas canvas, Paint paint) {
		paint.setColor(gray);
		RectF rectF = new RectF();
		rectF.left = hisleft;
		rectF.top = hisTop;
		rectF.right = hisRigth;
		rectF.bottom = hisBottom;
		canvas.drawRoundRect(rectF, 1, 1, paint);
		// canvas.drawRect(hisleft, hisTop, hisRigth, hisBottom, paint);
	}

	/**
	 * 绘制底部文字
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawBottomText(Canvas canvas, Paint paint) {
		int px = DisplayUtil.dip2px(context, 15);
		paint.setColor(gray);
		paint.setStrokeWidth(1);
		paint.setTextAlign(Align.LEFT);
		paint.setTextSize(DisplayUtil.sp2px(context, 12));
		if (isHis) {
			// 历史记录
			canvas.drawText("历史驾驶", XStartx, XStarty + px, paint);
		}
		// 0时
		paint.setStrokeWidth(1);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(DisplayUtil.sp2px(context, 12));
		canvas.drawText("0时", hisRigth + DisplayUtil.dip2px(context, 30),
				XStarty + px, paint);
		// 6时
		canvas.drawText("6时", DisplayUtil.dip2px(context, 30) + hisRigth + 360
				* unitWidth, XStarty + px, paint);
		// 12时
		canvas.drawText("12时", DisplayUtil.dip2px(context, 30) + hisRigth + 12
				* 60 * unitWidth, XStarty + px, paint);
		// 18时
		canvas.drawText("18时", DisplayUtil.dip2px(context, 30) + hisRigth + 18
				* 60 * unitWidth, XStarty + px, paint);
		// 24时
		canvas.drawText("24时", DisplayUtil.dip2px(context, 30) + hisRigth + 24
				* 60 * unitWidth, XStarty + px, paint);

		// // 日期
		// paint.setStrokeWidth(1);
		// paint.setTextSize(DisplayUtil.sp2px(context, 10));
		// canvas.drawText(date, XStartx, XStarty + px + px, paint);
	}

	/**
	 * 绘制圆角长方形
	 * 
	 * @param canvas
	 * @param paint
	 */
	private void drawRoundRect(Canvas canvas, Paint paint) {
		paint.setColor(gray_background);
		RectF rectF = new RectF();
		rectF.left = 0;
		rectF.top = 0;
		rectF.right = backgroundRectWidth;
		rectF.bottom = hisBottom;
		canvas.drawRoundRect(rectF, 5, 5, paint);
	}

	/**
	 * 数据处理
	 * 
	 * @param list
	 */
	private void getDriverMileHeigth(List<CarReportMileInfoNative> list) {
		mileViewInfoList.clear();
		j = 0;
		isDown = 0;
		float hisHeigth = hisBottom - hisTop;
		float maxHeigth = hisHeigth * 0.7f;
		float hisRigth = XStartx + 5 + hisWidth;
		float minHeigth = 25f;
		float width = 0;
		float heigth = 0;
		// String startTime = "";
		// String endTime = "";
		// String driverMile = "";
		int hour = 0;
		String strhour = "";
		int Minute = 0;
		String strminute = "";
		// float left;
		// float right;
		// int firstStart = 0;//
		CarReporMileViewInfoNative mileViewInfo;
		if (list != null && list.size() > 1) {
			// firstStart = list.get(0).getStart();
			for (int i = 0; i < list.size(); i++) {
				mileViewInfo = new CarReporMileViewInfoNative();
				if (minMile == maxMile) {
					heigth = maxHeigth;
					mileViewInfo.setHeigth(maxHeigth);
				} else {
					heigth = 25 + (((maxHeigth - minHeigth) * (list.get(i)
							.getDriverMile() - minMile)) / (maxMile - minMile));
					mileViewInfo.setHeigth(heigth);
				}
				width = list.get(i).getEnd() / 60 * unitWidth
						- list.get(i).getStart() / 60 * unitWidth;
				mileViewInfo.setWidth(width);
				mileViewInfo.setXleft(list.get(i).getStart() / 60 * unitWidth
						+ hisRigth + DisplayUtil.dip2px(context, 35));
				mileViewInfo.setXRinth(list.get(i).getEnd() / 60 * unitWidth
						+ hisRigth + DisplayUtil.dip2px(context, 35));
				mileViewInfo.setYBottm(hisBottom);
				mileViewInfo.setYTop(hisBottom - heigth);
				hour = list.get(i).getStart() / 3600;
				if (hour > 9) {
					strhour = hour + "";
				} else {
					strhour = "0" + hour;
				}
				Minute = list.get(i).getStart() % 3600 / 60;
				if (Minute > 9) {
					strminute = "" + Minute;
				} else {
					strminute = "0" + Minute;
				}
				mileViewInfo.setStartTime(strhour + "时" + strminute + "分");
				hour = list.get(i).getEnd() / 3600;
				if (hour > 9) {
					strhour = hour + "";
				} else {
					strhour = "0" + hour;
				}
				Minute = list.get(i).getEnd() % 3600 / 60;
				if (Minute > 9) {
					strminute = "" + Minute;
				} else {
					strminute = "0" + Minute;
				}
				mileViewInfo.setEndTime(strhour + "时" + strminute + "分");
				mileViewInfo.setDriverMile(list.get(i).getDriverMile() + "");
				mileViewInfoList.add(mileViewInfo);
			}
		} else if (list != null && list.size() == 1) {
			mileViewInfo = new CarReporMileViewInfoNative();
			mileViewInfo.setHeigth(maxHeigth);
			width = list.get(0).getEnd() / 60 * unitWidth
					- list.get(0).getStart() / 60 * unitWidth;
			mileViewInfo.setWidth(width);
			mileViewInfo.setXleft(list.get(0).getStart() / 60 * unitWidth
					+ hisRigth + DisplayUtil.dip2px(context, 35));
			mileViewInfo.setXRinth(list.get(0).getEnd() / 60 * unitWidth
					+ hisRigth + DisplayUtil.dip2px(context, 35));
			mileViewInfo.setYBottm(hisBottom);
			mileViewInfo.setYTop(hisBottom - maxHeigth);
			hour = list.get(0).getStart() / 3600;
			if (hour > 9) {
				strhour = hour + "";
			} else {
				strhour = "0" + hour;
			}
			Minute = list.get(0).getStart() % 3600 / 60;
			if (Minute > 9) {
				strminute = "" + Minute;
			} else {
				strminute = "0" + Minute;
			}
			mileViewInfo.setStartTime(strhour + "时" + strminute + "分");

			hour = list.get(0).getEnd() / 3600;
			if (hour > 9) {
				strhour = hour + "";
			} else {
				strhour = "0" + hour;
			}
			Minute = list.get(0).getEnd() % 3600 / 60;
			if (Minute > 9) {
				strminute = "" + Minute;
			} else {
				strminute = "0" + Minute;
			}
			mileViewInfo.setEndTime(strhour + "时" + strminute + "分");
			mileViewInfo.setDriverMile(list.get(0).getDriverMile() + "");
			mileViewInfoList.add(mileViewInfo);
		}
		if (list != null && list.size() > 0) {
			if (type == 0) {
//				kuangBottom = hisBottom;
				kuangLeft = hisleft;
				kuangRight = hisRigth;
				if (maxMile == minMile) {
					kuangTop = hisBottom - maxHeigth;
				} else {
					kuangTop = hisBottom - hisHeigth;
				}
			} else {
				j = type - 1;
				isDown = type - 1;
//				newKuangBottom = hisBottom;
				newKuangLeft = list.get(j).getStart() / 60 * unitWidth
						+ hisRigth + DisplayUtil.dip2px(context, 35);
				newKuangRight = list.get(j).getEnd() / 60 * unitWidth
						+ hisRigth + DisplayUtil.dip2px(context, 35);
				if (maxMile == minMile) {
					newKuangTop = hisBottom - maxHeigth;
				} else {
					newKuangTop = hisBottom
							- (25 + (((maxHeigth - minHeigth) * (list.get(j)
									.getDriverMile() - minMile)) / (maxMile - minMile)));
				}
				kuangWidth = newKuangRight - newKuangLeft;
				if (newKuangLeft > kuangLeft) {
					isX = 1;
					mobile = newKuangLeft - kuangLeft;
				} else if (newKuangLeft < kuangLeft) {
					isX = 0;
					mobile = kuangRight - newKuangRight;
				} else {
					isX = 2;
				}
				animator.start();
			}
		}
	}

	public int getFirstRect() {
		if (type == 0) {
			return 0;
		} else {
			if ((int) mileViewInfoList.get(type - 1).getXleft() > XStopx
					* (3f / 4)) {
				return (int) (XStopx * (3f / 4f));
			} else if ((int) mileViewInfoList.get(type - 1).getXleft() <= (int) (width * 0.75f * 0.5f)) {
				return 0;
			} else {
				return (int) mileViewInfoList.get(type - 1).getXleft()
						- (int) (width * 0.75f * 0.5f);
			}
		}
	}

	public void setInvalidate(Bundle bundle,
			List<CarReportMileInfoNative> mileInfoList) {
//		this.mileInfoList = mileInfoList;

		maxMile = bundle.getFloat("maxMile");
		minMile = bundle.getFloat("minMile");
		mile = bundle.getString("hisMile");
//		date = bundle.getString("date");
		status = bundle.getString("status");
		type = bundle.getInt("type");
		// XStartx = DisplayUtil.dip2px(context, 20);
		hisWidth = DisplayUtil.dip2px(context, 20);
		hisleft = XStartx + DisplayUtil.dip2px(context, 10);
		hisRigth = hisleft + hisWidth;
		hisTop = hisBottom - (heigth * screenScaleY * hisScaleY);
		XStarty = hisBottom + 2;
//		XStopy = XStarty;
		XStopx = width * 2;
		if (status.equals("0")) {
			isNotData = true;
			// unitWidth = (width * 2 - (hisWidth + XStartx + 5)) / 1440;
			backgroundRectWidth = width - DisplayUtil.dip2px(context, 20);
		} else if (type == 0) {
			isShow = false;
			isNotData = false;
			isHis = true;
			unitWidth = (width * 0.75f - DisplayUtil.dip2px(context, 20)) * 0.7f / 1440;
			backgroundRectWidth = width - DisplayUtil.dip2px(context, 20);
			getDriverMileHeigth(mileInfoList);
		} else {
			isShow = false;
			isNotData = false;
			isHis = false;
			backgroundRectWidth = width * 2;
			unitWidth = (width * 0.75f * 0.7f + width) / 1440;
			getDriverMileHeigth(mileInfoList);
		}

		invalidate();
	}
}
