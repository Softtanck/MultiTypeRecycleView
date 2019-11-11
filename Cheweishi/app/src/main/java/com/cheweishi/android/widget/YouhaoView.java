package com.cheweishi.android.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.BitmapUtils;
import com.cheweishi.android.utils.DisplayUtil;

public class YouhaoView extends View implements OnTouchListener {
	
	private static final String YOUHAO="今日油耗";
	private static final String SURPLUS_YOULIANG="剩余油量";
	private static final String UNABLE_OBTAIN="无法获取";
	// 定义屏幕的宽度
	private int mWidth;
	// 定义屏幕的高度
	private int mHeight;
	// 定义画笔
	private Paint mPaint;
	// 定义文字画笔
	private TextPaint mTextPaint;
	private TextPaint textPaint;
	// 定义圆的画笔
	private Paint bPaint;
	// 定义矩形
	private Rect rect;
	private Rect rect1;
	private float uh;

	private float center;
	private int changeT;
	private double kkk;
	private float k = 1;
	// 图片的高度
	private float heightdrawle;
	// 图片的宽度
	private float widthdrawle;
	private float w;

	private int top;
	private int left;
	private int right;
	private int buttom;
	private float changeheight;
	private Bitmap bitmap;
	private Bitmap bitmap1;
	private Bitmap bitmap2;
	private boolean isF = false;
	private RectF mRectF;
	private String olr;
	private String surplus;
	private ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 1);
	private DisplayMetrics metric = new DisplayMetrics();
	private Context context;

	public YouhaoView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public void initDate(final Context context, float uh, float center,
			String olr, String surplus) {
		this.context = context;
		this.olr = olr;
		this.surplus = surplus;
		this.uh = uh;
		this.center = center;
		// mHeight = ((Activity) context).getWindowManager().getDefaultDisplay()
		// .getHeight();
		// mWidth = ((Activity) context).getWindowManager().getDefaultDisplay()
		// .getWidth();

		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;

		mPaint = new Paint();
		textPaint = new TextPaint();
		mTextPaint = new TextPaint();
		bPaint = new Paint();
		bPaint.setStyle(Style.STROKE);
		bPaint.setColor(getResources().getColor(R.color.main_blue));
		int px = DisplayUtil.sp2px(context, 12);
		mTextPaint.setTextSize(px);
		textPaint.setTextSize(px);
		textPaint.setColor(Color.WHITE);
		this.setOnTouchListener(this);
		mPaint.setColor(getResources().getColor(R.color.light_blue));
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		Drawable drawable = context.getResources().getDrawable(
				R.drawable.youhao_icon1);
		Drawable drawable1 = context.getResources().getDrawable(
				R.drawable.youhao_icon2);
		Drawable drawable2 = context.getResources().getDrawable(
				R.drawable.youhao_icon3);
		heightdrawle = drawable.getIntrinsicHeight();
		widthdrawle = drawable.getIntrinsicWidth();
		w = mWidth - widthdrawle;
		left = (int) (w / 2);
		top = (int) (mHeight / 19.2);
		right = (int) ((mWidth + widthdrawle) / 2);
		buttom = (int) (top + heightdrawle);
		bitmap = BitmapUtils.drawableToBitmap(drawable);
		bitmap1 = BitmapUtils.drawableToBitmap(drawable1);
		bitmap2 = BitmapUtils.drawableToBitmap(drawable2);

		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if (isF) {
					if (rect.top < mRectF.top) {
						changeT -= 2;
						double per = Math.abs(2 / changeheight);
						k -= per * kkk;
					} else {
						valueAnimator.cancel();
						isF = false;
					}
				} else {
					if (mRectF.top < rect1.top) {
						changeT += 2;
						double per = Math.abs(2 / changeheight);
						k += per * kkk;
					} else {
						valueAnimator.cancel();
						isF = true;
					}
				}
				invalidate();

			}
		});
		valueAnimator.setDuration(2000);
		valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
		this.invalidate();
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		if (bitmap == null) {

		} else {
			canvas.drawBitmap(bitmap, left, top, mPaint);
			canvas.restore();
			int top1 = (int) (top + heightdrawle * uh);
			int bottom = (int) (buttom - heightdrawle * center);
			rect = new Rect(left, top1, right, bottom);
			canvas.save();
			int centerx = rect.centerX();
			int centery = rect.centerY();
			canvas.clipRect(rect);
			textPaint.setColor(Color.WHITE);
			canvas.drawBitmap(bitmap1, left, top, mPaint);
			((Activity) context).getWindowManager().getDefaultDisplay()
					.getMetrics(metric);

			int densityDpi = metric.densityDpi;
			if (densityDpi <= 240) {
				int x = (int) (centerx / 1.29);
				canvas.drawText(YOUHAO, x, centery + mHeight / 160 + 3,
						textPaint);
			} else if (densityDpi > 240) {
				int x = (int) (centerx / 1.26);
				canvas.drawText(YOUHAO, x, centery + mHeight / 160, textPaint);
			}
			canvas.restore();
			int top2 = bottom;
			rect1 = new Rect(left, top2, right, buttom);
			canvas.save();
			canvas.clipRect(rect1);
			canvas.drawBitmap(bitmap2, left, top, mPaint);
			textPaint.setColor(Color.WHITE);
			int centerY = rect1.centerY();
			if (densityDpi <= 240) {
				canvas.drawText(SURPLUS_YOULIANG, (float) (rect1.centerX() / 1.29),
						rect1.centerY() + mHeight / 160, textPaint);
			} else {
				canvas.drawText(SURPLUS_YOULIANG, (float) (rect1.centerX() / 1.26),
						rect1.centerY() + mHeight / 160, textPaint);
			}
			canvas.restore();
			canvas.save();
			if (densityDpi <= 240) {
				if (!isF) {
					if (olr.equals("")) {
					} else {
						canvas.drawCircle(left - 8, centery, 5, mPaint);
						canvas.drawLine(left - 8, centery, left - mWidth / 18,
								(float) (centery - mHeight / 38.4), mPaint);
						canvas.drawLine(left - mWidth / 18,
								(float) (centery - mHeight / 38.4), left
										- mWidth / 6,
								(float) (centery - mHeight / 38.4), mPaint);
						textPaint.setColor(getResources().getColor(
								R.color.main_blue));
						canvas.drawText(olr + "L", left - mWidth / 6,
								(float) (centery - mHeight / 32), textPaint);
					}
				} else {
					if (surplus != null && !surplus.equals("")) {
						canvas.drawCircle(left - 8, centerY, 5, mPaint);
						canvas.drawLine(left - 8, centerY, left - mWidth / 18,
								(float) (centerY - mHeight / 38.4), mPaint);
						canvas.drawLine(left - mWidth / 18,
								(float) (centerY - mHeight / 38.4),
								(float) (left - mWidth / 5.4),
								(float) (centerY - mHeight / 38.4), mPaint);
						textPaint.setColor(getResources().getColor(
								R.color.orange_currs));
						if (surplus.equals(UNABLE_OBTAIN) || surplus.equals("0.0")) {
							canvas.drawText(UNABLE_OBTAIN,
									(float) (left - mWidth / 5.4),
									(float) (centerY - mHeight / 32), textPaint);
						} else {
							canvas.drawText(surplus + "L",
									(float) (left - mWidth / 5.4), centerY
											- mHeight / 32, textPaint);
						}
					}
				}
			} else if (densityDpi == 320) {
				if (!isF) {
					if (olr.equals("")) {
					} else {
						canvas.drawCircle(left + 10, centery, 5, mPaint);
						canvas.drawLine(left + 10, centery, left - mWidth / 18,
								(float) (centery - mHeight / 38.4), mPaint);
						canvas.drawLine(left - mWidth / 18,
								(float) (centery - mHeight / 38.4), left
										- mWidth / 6,
								(float) (centery - mHeight / 38.4), mPaint);
						textPaint.setColor(getResources().getColor(
								R.color.main_blue));
						canvas.drawText(olr + "L", left - mWidth / 6,
								(float) (centery - mHeight / 32), textPaint);
					}
				} else {
					if (surplus != null && !surplus.equals("")) {
						canvas.drawCircle(left + 10, centerY, 5, mPaint);
						canvas.drawLine(left + 10, centerY, left - mWidth / 18,
								(float) (centerY - mHeight / 38.4), mPaint);
						canvas.drawLine(left - mWidth / 18,
								(float) (centerY - mHeight / 38.4),
								(float) (left - mWidth / 5.4),
								(float) (centerY - mHeight / 38.4), mPaint);
						textPaint.setColor(getResources().getColor(
								R.color.main_blue));
						if (surplus.equals(UNABLE_OBTAIN) || surplus.equals("0.0")) {
							canvas.drawText(UNABLE_OBTAIN,
									(float) (left - mWidth / 5.4),
									(float) (centerY - mHeight / 32), textPaint);
						} else {
							canvas.drawText(surplus + "L",
									(float) (left - mWidth / 5.4), centerY
											- mHeight / 32, textPaint);
						}
					}
				}
			} else {
				if (!isF) {
					if (olr.equals("")) {
					} else {
						canvas.drawCircle(left + 20, centery, 5, mPaint);
						canvas.drawLine(left + 20, centery, left - mWidth / 18,
								(float) (centery - mHeight / 38.4), mPaint);
						canvas.drawLine(left - mWidth / 18,
								(float) (centery - mHeight / 38.4), left
										- mWidth / 6,
								(float) (centery - mHeight / 38.4), mPaint);
						textPaint.setColor(getResources().getColor(
								R.color.main_blue));
						canvas.drawText(olr + "L", left - mWidth / 6,
								(float) (centery - mHeight / 32), textPaint);
					}
				} else {
					if (surplus != null && !surplus.equals("")) {
						canvas.drawCircle(left + 20, centerY, 5, mPaint);
						canvas.drawLine(left + 20, centerY, left - mWidth / 18,
								(float) (centerY - mHeight / 38.4), mPaint);
						canvas.drawLine(left - mWidth / 18,
								(float) (centerY - mHeight / 38.4),
								(float) (left - mWidth / 5.4),
								(float) (centerY - mHeight / 38.4), mPaint);
						textPaint.setColor(getResources().getColor(
								R.color.main_blue));
						if (surplus.equals(UNABLE_OBTAIN) || surplus.equals("0.0")) {
							canvas.drawText(UNABLE_OBTAIN,
									(float) (left - mWidth / 5.4),
									(float) (centerY - mHeight / 32), textPaint);
						} else {
							canvas.drawText(surplus + "L",
									(float) (left - mWidth / 5.4), centerY
											- mHeight / 32, textPaint);
						}
					}
				}
			}
			if (densityDpi <= 240) {
				mRectF = new RectF(left - 5, top1 - 2 + changeT, right + 5,
						(float) ((top1 + 2 + changeT) + heightdrawle * 0.2 * k));

			} else if (densityDpi == 320) {
				mRectF = new RectF(left + 10, top1 - 2 + changeT, right - 10,
						(float) ((top1 + 2 + changeT) + heightdrawle * 0.2 * k));

			} else if (densityDpi > 320) {
				mRectF = new RectF(left + 20, top1 - 1 + changeT, right - 20,
						(float) ((top1 + 1 + changeT) + heightdrawle * 0.2 * k));
			}
			canvas.drawRect(mRectF, bPaint);
			canvas.restore();
			canvas.save();
			mTextPaint.setTextAlign(Align.CENTER);
			mTextPaint.setColor(getResources().getColor(R.color.huise));
			canvas.drawText("点击图形以了解油耗详情", mWidth / 2, buttom + mHeight / 24,mTextPaint);
			canvas.restore();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (event.getX() > rect.left && event.getX() < rect.right
					&& event.getY() > rect.top && rect.bottom > event.getY()) {
				if (isF) {
					k = (buttom - (buttom - heightdrawle * center)) / ((buttom - heightdrawle
							* center) - (top + heightdrawle * uh)) - 0.15f;
					kkk = 1
							- ((double) (buttom - heightdrawle * center) - (top + heightdrawle
									* uh))
							/ ((double) buttom - (buttom - heightdrawle
									* center));
					changeheight = mRectF.top - rect.top;
					valueAnimator.start();
				}
			} else if (event.getX() > rect1.left && event.getX() < rect1.right
					&& event.getY() > rect1.top && rect1.bottom > event.getY()) {
				if (!isF) {
					k = 1;
					kkk = ((double) buttom - (buttom - heightdrawle * center))
							/ ((double) (buttom - heightdrawle * center) - (top + heightdrawle
									* uh)) - 1;
					changeheight = rect1.bottom - mRectF.bottom;
					valueAnimator.start();
				}
			}
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return true;
	}

}
