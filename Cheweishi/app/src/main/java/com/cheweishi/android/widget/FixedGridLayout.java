package com.cheweishi.android.widget;

import android.content.Context;

import android.content.res.TypedArray;

import android.graphics.Canvas;

import android.graphics.Color;

import android.graphics.Paint;

import android.graphics.Rect;

import android.util.AttributeSet;

import android.view.View;

import android.view.ViewGroup;

import android.view.View.MeasureSpec;

public class FixedGridLayout extends ViewGroup {

	private int mCellWidth;

	private int mCellHeight;

	public FixedGridLayout(Context context) {

		super(context);

		// TODO Auto-generated constructor stub

	}

	public FixedGridLayout(Context context, AttributeSet attrs) {

		super(context, attrs);

		// TODO Auto-generated constructor stub

		// ��ʼ����Ԫ���͸�

		mCellWidth = 60;

		mCellHeight = 60;

	}

	// ���õ�Ԫ����

	public void setCellWidth(int w) {

		mCellWidth = w;

		// Call this when something has changed which has invalidated the layout
		// of this view.

		// This will schedule a layout pass of the view tree

		requestLayout();

	}

	// ���õ�Ԫ��߶�

	public void setCellHeight(int h) {

		mCellHeight = h;

		requestLayout();

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {

		// TODO Auto-generated method stub

		// ��ȡ���ֿؼ����

		int width = getWidth();

		int height = getHeight();

		// ��������

		Paint mPaint = new Paint();

		// ���û��ʵĸ�������

		mPaint.setColor(Color.TRANSPARENT);

		mPaint.setStyle(Paint.Style.STROKE);

		mPaint.setStrokeWidth(10);

		mPaint.setAntiAlias(true);

		// �������ο�

		Rect mRect = new Rect(0, 0, width, height);

		// ���Ʊ߿�

		canvas.drawRect(mRect, mPaint);

		// ��������ø���ķ���

		super.dispatchDraw(canvas);

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		// TODO Auto-generated method stub

		// ������������

		int cellWidthSpec = MeasureSpec.makeMeasureSpec(mCellWidth,
				MeasureSpec.AT_MOST);

		int cellHeightSpec = MeasureSpec.makeMeasureSpec(mCellHeight,
				MeasureSpec.AT_MOST);

		// ��¼ViewGroup��Child���ܸ���

		int count = getChildCount();

		// �����ӿռ�Child�Ŀ��

		for (int i = 0; i < count; i++) {

			View childView = getChildAt(i);

			/*
			 * 
			 * This is called to find out how big a view should be.
			 * 
			 * The parent supplies constraint information in the width and
			 * height parameters.
			 * 
			 * The actual mesurement work of a view is performed in
			 * onMeasure(int, int),
			 * 
			 * called by this method.
			 * 
			 * Therefore, only onMeasure(int, int) can and must be overriden by
			 * subclasses.
			 */

			childView.measure(cellWidthSpec, cellHeightSpec);

		}

		// ���������ؼ���ռ�����С

		// ע��setMeasuredDimension��resolveSize���÷�

		setMeasuredDimension(resolveSize(mCellWidth * count, widthMeasureSpec),
				resolveSize(mCellHeight * count, heightMeasureSpec));

		// setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

		// ����Ҫ���ø���ķ���

		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		// TODO Auto-generated method stub

		int cellWidth = mCellWidth;

		int cellHeight = mCellHeight;

		int columns = (r - l) / cellWidth;

		if (columns < 0) {

			columns = 1;

		}

		int x = 0;

		int y = 0;

		int i = 0;

		int count = getChildCount();

		for (int j = 0; j < count; j++) {

			final View childView = getChildAt(j);

			// ��ȡ�ӿؼ�Child�Ŀ��

			int w = childView.getMeasuredWidth();

			int h = childView.getMeasuredHeight();

			// �����ӿؼ��Ķ������

			int left = x + ((cellWidth - w) / 2);

			int top = y + ((cellHeight - h) / 2);

			// int left = x;

			// int top = y;

			// �����ӿؼ�

			childView.layout(left, top, left + w, top + h);

			if (i >= (columns - 1)) {

				i = 0;

				x = 0;

				y += cellHeight;

			} else {

				i++;

				x += cellWidth;

			}

		}

	}

}