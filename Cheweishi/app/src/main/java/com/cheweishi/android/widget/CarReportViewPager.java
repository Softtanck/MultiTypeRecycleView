package com.cheweishi.android.widget;

import java.util.HashMap;
import java.util.LinkedHashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import com.cheweishi.android.activity.CarReportActivity;

/**
 * 
 * @author zhangq<br>
 *         com.cheweishi.android.widget.CarReportViewPager
 */
public class CarReportViewPager extends ViewPager {
	private float mScale;

	private HashMap<Integer, View> mChildViews = new LinkedHashMap<Integer, View>();
	private View leftView;
	private View rightView;

	public CarReportViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// this.position = position % mChildViews.size();
		// 滑动特别小的距离时，我们认为没有动，可有可无的判断
		float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

		leftView = mChildViews.get(position);
		rightView = mChildViews.get(position + 1);

		animateStack(leftView, rightView, effectOffset, positionOffsetPixels);
		super.onPageScrolled(position, positionOffset, positionOffsetPixels);
	}

	private boolean isSmall(float positionOffset) {
		return Math.abs(positionOffset) < 0.0001;
	}

	public void setObjectForPosition(View view, int position) {
		mChildViews.put(position, view);
	}

	/**
	 * 根据横向滑动比例可以做出多种动画
	 * 
	 * @param leftView
	 * @param rightView
	 * @param effectOffset
	 * @param positionOffsetPixels
	 */
	private void animateStack(View leftView, View rightView,
			float effectOffset, int positionOffsetPixels) {
		if (leftView != null) {

			mScale = 2 * effectOffset >= 1 ? 0 : 1 - 2 * effectOffset;
			if (leftView instanceof ReportCircleView) {
				((ReportCircleView) leftView).resume();
				((ReportCircleView) leftView).setTransOffset(mScale);
				if (effectOffset == 0) {
					((ReportCircleView) leftView).pause();
				}
			}
			// mTrans = -getPageMargin() - effectOffset * positionOffsetPixels /
			// 2;
			// leftView.setScaleX(mScale);
			// leftView.setScaleY(mScale);
			// leftView.setTranslationX(mTrans);

		}
		if (rightView != null) {
			/**
			 * 缩小比例 如果手指从右到左的滑动（切换到后一个）：0.0~1.0，即从一半到最大
			 * 如果手指从左到右的滑动（切换到前一个）：1.0~0，即从最大到一半
			 */
			mScale = effectOffset >= 0.5 ? 2 * (effectOffset - 0.5f) : 0;
			if (rightView instanceof ReportCircleView) {
				((ReportCircleView) rightView).resume();
				((ReportCircleView) rightView).setTransOffset(mScale);
				if (effectOffset == 0) {
					((ReportCircleView) rightView).pause();
				}
			}

			// mScale=effectOffset.

			/**
			 * x偏移量： 如果手指从右到左的滑动（切换到后一个）：0-720 如果手指从左到右的滑动（切换到前一个）：720-0
			 */
			// mTrans = -getPageMargin() - effectOffset * positionOffsetPixels /
			// 2;
			// + "\n----getPageMargin():" + getPageMargin()
			// + "\n-----positionOffsetPixels:" + positionOffsetPixels);
			// rightView.setScaleX(mScale);
			// rightView.setScaleY(mScale);
		}
		// 好像没什么用
		// if (leftView != null) {
		// leftView.bringToFront();
		// }
	}

	/**
	 * 是否点击到五个圆圈
	 */
	private boolean isCircle;
	// 初始点X
	private float originalX;
	// 初始点Y
	private float originalY;
	// 移动后X
	private float moveX;
	// 移动后Y
	private float moveY;
	/**
	 * 判断是否是移动事件
	 */
	private boolean isMove;
	/**
	 * 点击事件位置1-7，0是空白区域
	 */
	private int position;

	/**
	 * 旋转圆圈是否是第一圈
	 */
	private boolean isFirst;

	/**
	 * 触摸监听，实现多种动作
	 */
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		ReportCircleView circlePre = (ReportCircleView) mChildViews.get(0);
		ReportCircleView circle = (ReportCircleView) mChildViews.get(1);
		ReportCircleView circleNext = (ReportCircleView) mChildViews.get(2);
		/**
		 * 这里circleNext有时会出null指针错误，不知道原因，现在处理方式如下：
		 */
		if (circle == null || circleNext == null || circlePre == null) {
			circle = null;
			circleNext = null;
			circlePre = null;
			return super.onTouchEvent(e);
		}
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isMove = false;
			if (isClickedItem(e, circle)) {
				isCircle = true;
				isFirst = true;
				circle.setIsTranslate(false);
				circleNext.setIsTranslate(false);
				circlePre.setIsTranslate(false);
				originalX = e.getX();
				originalY = e.getY();
				return true;
			} else {
				isCircle = false;
				if (position == 6 || position == 7) {
					return true;
				}
				circle.setIsTranslate(true);
				circlePre.setIsTranslate(true);
				circleNext.setIsTranslate(true);
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isCircle) {
				moveX = e.getX();
				moveY = e.getY();
				double len = Math.sqrt(Math.pow((moveX - originalX), 2)
						+ Math.pow(moveY - originalY, 2));
				if (len < circle.bitR && isFirst) {
					isMove = false;
				} else {
					isFirst = false;
					isMove = true;
					circle.resume();
					circle.setRotateSpeed(calculateDegree(originalX, originalY,
							moveX, moveY));
					circlePre.resume();
					circlePre.setRotateSpeed(calculateDegree(originalX,
							originalY, moveX, moveY));
					circleNext.resume();
					circleNext.setRotateSpeed(calculateDegree(originalX,
							originalY, moveX, moveY));
				}
				return true;
			} else if (position == 6) {
				// 滑动超过中心区域
				if (e.getX() < (circle.centerW - circle.bitHW)
						|| e.getX() > (circle.centerW + circle.bitHW * 3)
						|| e.getY() < circle.centerH
						|| e.getY() > (circle.centerH + circle.bitHH * 4)) {
					isMove = true;
					circle.setIsTranslate(true);
					circlePre.setIsTranslate(true);
					circleNext.setIsTranslate(true);
				} else {
					isMove = false;
					return true;
				}
			} else if (position == 7) {
				// 滑动超过查看详情图片区域
				if (e.getX() < circle.textW
						|| e.getX() > (circle.textW + circle.bitTextW)
						|| e.getY() < (circle.textH - circle.bitTextH)
						|| e.getY() > (circle.textH + circle.bitTextH * 2)) {
					isMove = true;
					circle.setIsTranslate(true);
					circlePre.setIsTranslate(true);
					circleNext.setIsTranslate(true);
				} else {
					isMove = false;
					return true;
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			if (isCircle) {
				// 圆环移动
				if (isMove) {
					circle.pause();
					circlePre.pause();
					circleNext.pause();
					return true;
				} else if (isFirst) {
					// 圆环点击
					CarReportActivity a = (CarReportActivity) CarReportViewPager.this
							.getContext();
					a.changeFragment(position);
				}
			} else {
				if (!isMove && position != 0) {
					// 其他点击事件
					CarReportActivity a = (CarReportActivity) CarReportViewPager.this
							.getContext();
					a.changeFragment(position);
				}
				// 其他区域平移
			}

			break;
		default:
			break;
		}
		// viewpager平移滑动
		return super.onTouchEvent(e);

	}

	/**
	 * 点击区域判断
	 * 
	 * @param e
	 * @param circle
	 * @return
	 */
	private boolean isClickedItem(MotionEvent e, ReportCircleView circle) {
		if (circle.suduW < e.getX()
				&& e.getX() < (circle.suduW + circle.bitR * 2)
				&& (circle.suduH) < e.getY()
				&& e.getY() < (circle.suduH + circle.bitR * 2)) {
			position = 1;
			return true;
		} else if (circle.bailiW < e.getX()
				&& e.getX() < (circle.bailiW + circle.bitR * 2)
				&& (circle.bailiH) < e.getY()
				&& e.getY() < (circle.bailiH + circle.bitR * 2)) {
			position = 2;
			return true;
		} else if (circle.lichengW < e.getX()
				&& e.getX() < (circle.lichengW + circle.bitR * 2)
				&& (circle.lichengH) < e.getY()
				&& e.getY() < (circle.lichengH + circle.bitR * 2)) {
			position = 3;
			return true;
		} else if (circle.shijianW < e.getX()
				&& e.getX() < (circle.shijianW + circle.bitR * 2)
				&& (circle.shijianH) < e.getY()
				&& e.getY() < (circle.shijianH + circle.bitR * 2)) {
			position = 4;
			return true;
		} else if (circle.youhaoW < e.getX()
				&& e.getX() < (circle.youhaoW + circle.bitR * 2)
				&& (circle.youhaoH) < e.getY()
				&& e.getY() < (circle.youhaoH + circle.bitR * 2)) {
			position = 5;
			return true;
		} else if (circle.centerW < e.getX()
				&& e.getX() < (circle.centerW + circle.bitHW * 2)
				&& (circle.centerH) < e.getY()
				&& e.getY() < (circle.centerH + circle.bitHH * 4)) {
			position = 6;
			return false;
		} else if (circle.textW < e.getX()
				&& e.getX() < (circle.textW + circle.bitTextW)
				&& (circle.textH) < e.getY()
				&& e.getY() < (circle.textH + circle.bitTextH * 2)) {
			position = 7;
			return false;
		}

		position = 0;
		return false;
	}

	/**
	 * 状态栏高度
	 */
	private int decorHeight;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {

			View v = ((Activity) getContext()).getWindow().getDecorView();
			Rect outRect = new Rect();
			v.getWindowVisibleDisplayFrame(outRect);
			decorHeight = outRect.height();

			Display display = ((Activity) getContext()).getWindowManager()
					.getDefaultDisplay();
			@SuppressWarnings("deprecation")
			int disHeight = display.getHeight();

			decorHeight = disHeight - decorHeight;
		}
	}

	boolean isRight = true;

	/**
	 * 计算移动的角度 cosAB=(a^2+b^2-c^2)/(2*a*b)
	 * 
	 * @param oX
	 *            originalX
	 * @param oY
	 * @param mX
	 *            moveX
	 * @param mY
	 * @return 计算出的角度 单位（度）
	 */
	private int calculateDegree(float oX, float oY, float mX, float mY) {
		int cX = getWidth() / 2;
		int cY = getHeight() / 2;

		double sideA = Math.sqrt(Math.pow(cX - oX, 2) + Math.pow(cY - oY, 2));
		double sideB = Math.sqrt(Math.pow(cX - mX, 2) + Math.pow(cY - mY, 2));
		double sideC = Math.sqrt(Math.pow(mX - oX, 2) + Math.pow(mY - oY, 2));

		double cosAB = (Math.pow(sideA, 2) + Math.pow(sideB, 2) - Math.pow(
				sideC, 2)) / (2 * sideA * sideB);
		int degree = (int) (Math.acos(cosAB) * 180 / Math.PI);

		if (isLeftRight(oX, oY, mX, mY)) {
			return degree;
		} else {
			return 360 - degree;
		}

	}

	/**
	 * 判断点(mX,mY)是否在向量(oX,oY)到(cX,cY)的左边 <br>
	 * 两点 构成的直线为： -(cY-oY)/(cX-oX)*x+y=(cX*oY-oX*cY)/(cX-oX)
	 * 
	 * @param oX
	 *            originalX
	 * @param oY
	 * @param mX
	 *            moveX
	 * @param mY
	 * @return true是左边
	 */
	private boolean isLeftRight(float oX, float oY, float mX, float mY) {
		int cX = getWidth() / 2;
		int cY = getHeight() / 2;

		double lineK = -(cY - oY) / (cX - oX);
		double lineB = (cX * oY - oX * cY) / (cX - oX);

		double d = lineK * mX + mY;
		return (oX - cX) * (d - lineB) > 0;
	}
}
