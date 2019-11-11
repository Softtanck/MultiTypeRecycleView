package com.cheweishi.android.widget;

import com.cheweishi.android.R;
import com.cheweishi.android.tools.EmptyTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SwipeListView extends ListView {
	private Boolean mIsHorizontal;

	private View mPreItemView;

	private View mCurrentItemView;

	private float mFirstX;

	private float mFirstY;

	private int mRightViewWidth;

	// private boolean mIsInAnimation = false;
	private final int mDuration = 100;

	private final int mDurationStep = 10;

	private boolean mIsShown;

	public SwipeListView(Context context) {
		this(context, null);
	}

	public SwipeListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.swipelistviewstyle);

		// 获取自定义属性和默认值
		mRightViewWidth = (int) mTypedArray.getDimension(
				R.styleable.swipelistviewstyle_right_width, 200);

		mTypedArray.recycle();
	}

	/**
	 * return true, deliver to listView. return false, deliver to child. if
	 * move, return true
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mIsHorizontal = null;
			mFirstX = lastX;
			mFirstY = lastY;
			int motionPosition = pointToPosition((int) mFirstX, (int) mFirstY);

			if (motionPosition >= 0) {
				View currentItemView = getChildAt(motionPosition
						- getFirstVisiblePosition());
				mPreItemView = mCurrentItemView;
				mCurrentItemView = currentItemView;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			float dx = lastX - mFirstX;
			float dy = lastY - mFirstY;

			if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
				return true;
			}
			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (mIsShown
					&& (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
				/**
				 * 情况一：
				 * <p>
				 * 一个Item的右边布局已经显示，
				 * <p>
				 * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
				 */
				hiddenRight(mPreItemView);
			}
			break;
		}

		return super.onInterceptTouchEvent(ev);
	}

	private boolean isHitCurItemLeft(float x) {
		return x < getWidth() - mRightViewWidth;
	}

	/**
	 * @param dx
	 * @param dy
	 * @return judge if can judge scroll direction
	 */
	private boolean judgeScrollDirection(float dx, float dy) {
		boolean canJudge = true;

		if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
			mIsHorizontal = true;
		} else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
			mIsHorizontal = false;
		} else {
			canJudge = false;
		}

		return canJudge;
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.

		super.setAdapter(adapter);
//
//		if (adapter.getCount() <= 0) {
//			EmptyTools.setEmpty(getContext(), this);
//			
//		}
	}

//	@Override
//	public void setEmptyView(View view) {
//		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();// 调用Activity的getLayoutInflater()
//		View emptyView = inflater.inflate(R.layout.no_data, null);
//		this.setEmptyView(emptyView);
//	}

//	private void setEmpty() {
//		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();// 调用Activity的getLayoutInflater()
//		View view = inflater.inflate(R.layout.no_data, null);
//		this.setEmptyView(view);
//	}

	/**
	 * return false, can't move any direction. return true, cant't move
	 * vertical, can move horizontal. return super.onTouchEvent(ev), can move
	 * both.
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;

		case MotionEvent.ACTION_MOVE:
			float dx = lastX - mFirstX;
			float dy = lastY - mFirstY;

			// confirm is scroll direction
			if (mIsHorizontal == null) {
				if (!judgeScrollDirection(dx, dy)) {
					break;
				}
			}

			if (mIsHorizontal) {
				if (mCurrentItemView != null) {
					if (mIsShown && mPreItemView != mCurrentItemView) {
						/**
						 * 情况二：
						 * <p>
						 * 一个Item的右边布局已经显示，
						 * <p>
						 * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
						 * <p>
						 * 向左滑动只触发该情况，向右滑动还会触发情况五
						 */
						hiddenRight(mPreItemView);
					}

					if (mIsShown && mPreItemView == mCurrentItemView) {
						dx = dx - mRightViewWidth;
					}

					// can't move beyond boundary
					if (dx < 0 && dx > -mRightViewWidth) {
						mCurrentItemView.scrollTo((int) (-dx), 0);
					}
				}
				return true;
			} else {
				if (mIsShown) {
					/**
					 * 情况三：
					 * <p>
					 * 一个Item的右边布局已经显示，
					 * <p>
					 * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
					 */
					hiddenRight(mPreItemView);
				}
			}

			break;

		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			clearPressedState();
			if (mIsShown) {
				/**
				 * 情况四：
				 * <p>
				 * 一个Item的右边布局已经显示，
				 * <p>
				 * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
				 */
				hiddenRight(mPreItemView);
			}

			if (mIsHorizontal != null && mIsHorizontal) {
				if (mFirstX - lastX > mRightViewWidth / 2) {
					showRight(mCurrentItemView);
				} else {
					/**
					 * 情况五：
					 * <p>
					 * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
					 */
					hiddenRight(mCurrentItemView);
				}

				return true;
			}

			break;
		}

		return super.onTouchEvent(ev);
	}

	private void clearPressedState() {
		// TODO current item is still has background, issue
		if (mCurrentItemView != null) {
			mCurrentItemView.setPressed(false);
		}
		setPressed(false);
		refreshDrawableState();
		// invalidate();
	}

	private void showRight(View view) {
		if (view != null) {

			Message msg = new MoveHandler().obtainMessage();
			msg.obj = view;
			msg.arg1 = view.getScrollX();
			msg.arg2 = mRightViewWidth;
			msg.sendToTarget();

			mIsShown = true;
		}
	}

	public void hiddenRight(View view) {
		// Toast.makeText(getContext(), "good1", Toast.LENGTH_LONG).show();
		if (view != null) {
			if (mCurrentItemView == null) {
				return;
			}
			Message msg = new MoveHandler().obtainMessage();//
			msg.obj = view;
			msg.arg1 = view.getScrollX();
			msg.arg2 = 0;

			msg.sendToTarget();

			mIsShown = false;
		} else {
			// Toast.makeText(getContext(), "good", Toast.LENGTH_LONG).show();
			if (mPreItemView != null) {
				if (mCurrentItemView == null) {
					return;
				}
				Message msg = new MoveHandler().obtainMessage();//
				msg.obj = mPreItemView;
				msg.arg1 = mPreItemView.getScrollX();
				msg.arg2 = 0;

				msg.sendToTarget();

				mIsShown = false;
			}

		}
	}

	/**
	 * show or hide right layout animation
	 */
	@SuppressLint("HandlerLeak")
	class MoveHandler extends Handler {
		int stepX = 0;

		int fromX;

		int toX;

		View view;

		private boolean mIsInAnimation = false;

		private void animatioOver() {
			mIsInAnimation = false;
			stepX = 0;
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (stepX == 0) {
				if (mIsInAnimation) {
					return;
				}
				mIsInAnimation = true;
				view = (View) msg.obj;
				fromX = msg.arg1;
				toX = msg.arg2;
				stepX = (int) ((toX - fromX) * mDurationStep * 1.0 / mDuration);
				if (stepX < 0 && stepX > -1) {
					stepX = -1;
				} else if (stepX > 0 && stepX < 1) {
					stepX = 1;
				}
				if (Math.abs(toX - fromX) < 10) {
					view.scrollTo(toX, 0);
					animatioOver();
					return;
				}
			}

			fromX += stepX;
			boolean isLastStep = (stepX > 0 && fromX > toX)
					|| (stepX < 0 && fromX < toX);
			if (isLastStep) {
				fromX = toX;
			}

			view.scrollTo(fromX, 0);
			invalidate();

			if (!isLastStep) {
				this.sendEmptyMessageDelayed(0, mDurationStep);
			} else {
				animatioOver();
			}
		}
	}

	public int getRightViewWidth() {
		return mRightViewWidth;
	}

	public void setRightViewWidth(int mRightViewWidth) {
		this.mRightViewWidth = mRightViewWidth;
	}

	public void hideView() {
		hiddenRight(mPreItemView);
	}
}
