package com.cheweishi.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 /**
 * 使用fontAwesome做图标的VIEW
 *
 * @author 陈国庆
 * @date 2015年5月7日
 */

public class FontAwesomeView extends TextView {
	PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,
			Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
	private static Typeface font;
	private Paint paint;
	private int offsetX;

	public FontAwesomeView(Context context) {
		super(context);
		init();
	}

	public FontAwesomeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FontAwesomeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.CircleImageView, defStyle, 0);
		// mBorderWidth =
		// a.getDimension(R.styleable.CircleImageView_border_width,
		// TDevice.dpToPixel(DEFAULT_BORDER_WIDTH));
		// mBorderColor = a.getColor(R.styleable.CircleImageView_border_color,
		// DEFAULT_BORDER_COLOR);
		// a.recycle();
		init();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measuredWidth = getMeasuredWidth();
		int measuredHeight = getMeasuredHeight();
		int max = Math.max(measuredWidth, measuredHeight);
		setMeasuredDimension(max, max);
		// int i = max - measuredWidth;
		// offsetX = i == 0 ? max - measuredHeight : i;
		offsetX = (max - measuredWidth) / 2;
	}
	@SuppressLint("NewApi")
	@Override
	public void setBackground(Drawable background) {
		if (background instanceof ColorDrawable) {
			ColorDrawable colorDrawable = (ColorDrawable) background;
			if (paint == null)
				paint = new Paint();
			// if ((colorDrawable.getColor() >>> 24) != 0) {
			paint.setColor(colorDrawable.getColor());
			// }
		} else {
			super.setBackground(background);
		}
		invalidate();
	}
	/**
	 * ��ʼ�����
	 */
	private void init() {
		if (font == null) {
			font = Typeface.createFromAsset(getContext().getAssets(),
					"icomoon.ttf");
		}
		setTypeface(font);
		if (paint != null)
			paint.setAntiAlias(true);
	}

	@Override
	public void draw(Canvas canvas) {
		try {
			canvas.setDrawFilter(pfd);
			if (paint != null) {
				// paint.setColor(Color.GREEN);
				// RectF rf = new RectF(Math.max(getMeasuredWidth(),
				// getMeasuredHeight()) / 2, Math.max(getMeasuredWidth(),
				// getMeasuredHeight()) / 2, Math.max(getMeasuredWidth(),
				// getMeasuredHeight()) / 2, Math.max(getMeasuredWidth(),
				// getMeasuredHeight()) / 2);
				// canvas.drawRect(rf, paint);
				canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2,
						Math.max(getMeasuredWidth(), getMeasuredHeight()) / 2,
						paint);
			}
			canvas.translate(offsetX, 0);
			super.draw(canvas);
			canvas.restore(); // TODO 6.0系统上有Bug.
		}catch (Exception e){}
	}
}
