package com.cheweishi.android.widget;

import com.cheweishi.android.R;
import com.cheweishi.android.tools.ScreenTools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class AssortView extends Button {

	public interface OnTouchAssortListener {
		void onTouchAssortListener(int index);

		void onTouchAssortUP();
	}

	public AssortView(Context context) {
		super(context);
	}

	public AssortView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AssortView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 分类
	private String[] assort = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	private Paint paint = new Paint();
	// 选择的索引
	private int selectIndex = -1;
	private float RATIO;
	private static int TEXT_SIZE = 20;
	// 字母监听器
	private OnTouchAssortListener onTouch;

	public void setOnTouchAssortListener(OnTouchAssortListener onTouch) {
		this.onTouch = onTouch;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		int height = getHeight();
		int width = getWidth();
		int interval = height / assort.length;

		for (int i = 0, length = assort.length; i < length; i++) {
			// 抗锯齿
			paint.setAntiAlias(true);
			// 默认粗体
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			// 白色
			paint.setColor(Color.BLACK);
			int screenWidth = ScreenTools
					.getScreentWidth((Activity) getContext());
			int screenHeight = ScreenTools
					.getScreentHeight((Activity) getContext());
			float ratioWidth = (float) screenWidth / 720;
			float ratioHeight = (float) screenHeight / 1280;

			RATIO = Math.min(ratioWidth, ratioHeight);
			TEXT_SIZE = Math.round(20 * RATIO);
			paint.setTextSize(TEXT_SIZE);
			if (i == selectIndex) {
				// 被选择的字母改变颜色和粗体
				paint.setColor(Color.parseColor("#3399ff"));
				paint.setFakeBoldText(true);
				paint.setTextSize(getResources().getDimension(
						R.dimen.AssortView_text_size));
			}
			// 计算字母的X坐标
			float xPos = width / 2 - paint.measureText(assort[i]) / 2;
			// 计算字母的Y坐标
			float yPos = interval * i + interval;
			canvas.drawText(assort[i], xPos, yPos, paint);
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float y = event.getY();
		int index = (int) (y / getHeight() * assort.length);
		if (index >= 0 && index < assort.length) {

			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				// 如果滑动改变
				if (selectIndex != index) {
					selectIndex = index;
					if (onTouch != null) {
						onTouch.onTouchAssortListener(selectIndex);
					}

				}
				break;
			case MotionEvent.ACTION_DOWN:
				selectIndex = index;
				if (onTouch != null) {
					onTouch.onTouchAssortListener(selectIndex);
				}

				break;
			case MotionEvent.ACTION_UP:
				if (onTouch != null) {
					onTouch.onTouchAssortUP();
				}
				selectIndex = -1;
				break;
			}
		} else {
			selectIndex = -1;
			if (onTouch != null) {
				onTouch.onTouchAssortUP();
			}
		}
		invalidate();

		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
