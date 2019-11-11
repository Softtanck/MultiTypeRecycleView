package com.cheweishi.android.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;

import com.cheweishi.android.R;
import com.cheweishi.android.tools.ScreenTools;

/**
 * 雷达View
 * 
 * @author 胡健 2015.6.24
 */
public class LeiDaView extends RelativeLayout {

	private float offsetArgs = 0;
	private boolean isSearching = true;
	private Bitmap bitmap;
	private Bitmap bitmap2;
	private Rect rMoon;
	public Context context;

	public boolean isSearching() {
		return isSearching;
	}

	public void setSearching(boolean isSearching) {
		this.isSearching = isSearching;
		invalidate();
	}

	public LeiDaView(Context context) {
		this(context, null);
	}

	public LeiDaView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LeiDaView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initBitmap();
	}

	private void initBitmap() {
		if (bitmap == null) {
			try {
				bitmap = Bitmap.createBitmap(BitmapFactory.decodeResource(
						context.getResources(), R.drawable.home_yuan));
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("result", e + "");
			}
			
			//bitmap = small(bitmap);
		}
		if (bitmap2 == null) {
			try {
				bitmap2 = Bitmap.createBitmap(BitmapFactory.decodeResource(
						context.getResources(), R.drawable.home_yinying));
			} catch (Exception e) {
				e.printStackTrace();
				Log.e("result", e + "");
			}
			//bitmap2 = small1(bitmap2);
		}
	}

	private int getLeiDaHeight() {
		Resources res = getContext().getResources();
		int i1 = res.getDimensionPixelSize(R.dimen.marging_four_dp);
		int i2 = res.getDimensionPixelSize(R.dimen.car_main_item_margin) * 2;
		int i3 = res.getDimensionPixelSize(R.dimen.car_main_item_width) * 2;
		int i4 = res.getDimensionPixelSize(R.dimen.car_main_below_padding) * 2;
		return i1 + i2 + i3 + i4;
	}

	private int getRadiu() {
		int width = ScreenTools.getScreentWidth((Activity) getContext());
		if (width > getLeiDaHeight()) {
			return getLeiDaHeight()/2;
		} else {
			return width/2;
		}
	}

	private Bitmap small(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		float mm = ((float) getRadiu()) / ((float) bitmap.getWidth() / 2);
		matrix.postScale(mm, mm); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}
	private Bitmap small1(Bitmap bitmap) {
		Matrix matrix = new Matrix();
		float mm = ((float) getRadiu()) / ((float) bitmap.getWidth());
		matrix.postScale(mm, mm); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, getWidth() / 2 - bitmap.getWidth() / 2,
				getHeight() / 2 - bitmap.getHeight() / 2, null);
		if (isSearching) {
			rMoon = new Rect(getWidth() / 2 - bitmap2.getWidth() / 2,
					getHeight() / 2 - bitmap2.getHeight(), getWidth() / 2
							+ bitmap2.getWidth() / 2, getHeight() / 2);
			canvas.rotate(offsetArgs, getWidth() / 2, getHeight() / 2);
			offsetArgs = offsetArgs - 1;
		} else {
			canvas.rotate(offsetArgs, getWidth() / 2, getHeight() / 2);
		}
		canvas.drawBitmap(bitmap2, null, rMoon, null);
		if (isSearching) {
			invalidate();
		}
	}
}