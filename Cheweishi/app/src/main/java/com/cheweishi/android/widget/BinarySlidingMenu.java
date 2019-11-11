package com.cheweishi.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.ScreenUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * http://blog.csdn.net/lmj623565791
 * 
 * @author zhy
 * 
 */
@SuppressLint("NewApi")
public class BinarySlidingMenu extends HorizontalScrollView {

	/**
	 * 菜单的宽度
	 */
	private int mMenuWidth;
	private int mHalfMenuWidth;

	private boolean isOperateRight;
	private boolean isOperateLeft;

	private boolean once;

	private ViewGroup mLeftMenu;
	private ViewGroup mContent;
	private ViewGroup mRightMenu;
	private ViewGroup mWrapper;

	private boolean isLeftMenuOpen;
	private boolean isRightMenuOpen;
	private int scrollXTemp;

	/**
	 * 回调的接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface OnMenuOpenListener {
		/**
		 * 
		 * @param isOpen
		 *            true打开菜单，false关闭菜单
		 * @param flag
		 *            0 左侧， 1右侧
		 */
		void onMenuOpen(boolean isOpen, int flag);
	}

	public OnMenuOpenListener mOnMenuOpenListener;

	public void setOnMenuOpenListener(OnMenuOpenListener mOnMenuOpenListener) {
		this.mOnMenuOpenListener = mOnMenuOpenListener;
	}

	public BinarySlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;

	/**
	 * dp 菜单距离屏幕的右边距
	 */
	private int mMenuRightPadding;

	public BinarySlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScreenWidth = ScreenUtils.getScreenWidth(context);
		this.setOverScrollMode(OVER_SCROLL_NEVER);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.BinarySlidingMenu, defStyle, 0);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.BinarySlidingMenu_rightPadding:
				// 默认50
				mMenuRightPadding = a.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50f,
								getResources().getDisplayMetrics()));// 默认为10DP
				break;
			}
		}
		a.recycle();
	}

	public BinarySlidingMenu(Context context) {
		this(context, null, 0);
		this.setOverScrollMode(OVER_SCROLL_NEVER);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		/**
		 * 显示的设置一个宽度
		 */
		if (!once) {

			mWrapper = (LinearLayout) getChildAt(0);
			mLeftMenu = (ViewGroup) mWrapper.getChildAt(0);
			mContent = (ViewGroup) mWrapper.getChildAt(1);
			mRightMenu = (ViewGroup) mWrapper.getChildAt(2);
			mContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(isLeftMenuOpen == true) {
						BinarySlidingMenu.this.smoothScrollTo(mMenuWidth, 0);
						// 如果当前左侧菜单是开启状态，且mOnMenuOpenListener不为空，则回调关闭菜单
						if (isLeftMenuOpen && mOnMenuOpenListener != null) {
							// 第一个参数true：打开菜单，false：关闭菜单;第二个参数 0 代表左侧；1代表右侧
							mOnMenuOpenListener.onMenuOpen(false, 0);
						}
						isLeftMenuOpen = false;
					}
				}
			});
			mMenuWidth = mScreenWidth - mMenuRightPadding;
			mHalfMenuWidth = mMenuWidth / 2;
			mLeftMenu.getLayoutParams().width = mMenuWidth;
			mContent.getLayoutParams().width = mScreenWidth;
			mRightMenu.getLayoutParams().width = 0;

		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			// 将菜单隐藏
			this.scrollTo(mMenuWidth, 0);
			once = true;
		}

	}

	public void closeLeftMenu() {
		this.smoothScrollTo(mMenuWidth, 0);
		// 如果当前左侧菜单是开启状态，且mOnMenuOpenListener不为空，则回调关闭菜单
		if (isLeftMenuOpen && mOnMenuOpenListener != null) {
			// 第一个参数true：打开菜单，false：关闭菜单;第二个参数 0 代表左侧；1代表右侧
			mOnMenuOpenListener.onMenuOpen(false, 0);
		}
		isLeftMenuOpen = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		int action = ev.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// Toast.makeText(getContext(), ev.getX() + "", Toast.LENGTH_LONG)
			// .show();
			break;
		// Up时，进行判断，如果显示区域大于菜单宽度一半则完全显示，否则隐藏
		case MotionEvent.ACTION_UP:
			// Toast.makeText(getContext(), ev.getX() + "haha",
			// Toast.LENGTH_LONG)
			// .show();
			int scrollX = getScrollX();

			// 如果是操作左侧菜单
			if (isOperateLeft) {
				// 如果影藏的区域大于菜单一半，则影藏菜单
				if ((scrollX - scrollXTemp) < 5 || scrollXTemp == 0) {
					Toast.makeText(getContext(), "关闭", Toast.LENGTH_LONG).show();
					this.smoothScrollTo(mMenuWidth, 0);
					// 如果当前左侧菜单是开启状态，且mOnMenuOpenListener不为空，则回调关闭菜单
					if (isLeftMenuOpen && mOnMenuOpenListener != null) {
						// 第一个参数true：打开菜单，false：关闭菜单;第二个参数 0 代表左侧；1代表右侧
						mOnMenuOpenListener.onMenuOpen(false, 0);
					}
					isLeftMenuOpen = false;

				} else
				// 关闭左侧菜单
				{
					
					this.smoothScrollTo(0, 0);
					mContent.bringToFront();
					// 如果当前左侧菜单是关闭状态，且mOnMenuOpenListener不为空，则回调打开菜单
					if (!isLeftMenuOpen && mOnMenuOpenListener != null) {
						mOnMenuOpenListener.onMenuOpen(true, 0);
					}
					isLeftMenuOpen = true;

				}
				scrollXTemp = scrollX;
			}

			// 操作右侧
			if (isOperateRight) {
				// 打开右侧侧滑菜单
				if (scrollX > mHalfMenuWidth + mMenuWidth) {
					this.smoothScrollTo(mMenuWidth + mMenuWidth, 0);
				} else
				// 关闭右侧侧滑菜单
				{
					this.smoothScrollTo(mMenuWidth, 0);
					mContent.bringToFront();
				}
			}

			return true;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		if (l > mMenuWidth) {
			// 1.0 ~2.0 1.0~0.0
			// (2-scale)
			float scale = l * 1.0f / mMenuWidth;
			isOperateRight = true;
			isOperateLeft = false;
			ViewHelper.setTranslationX(mRightMenu, -mMenuWidth * (2 - scale));

		} else {
			float scale = l * 1.0f / mMenuWidth;
			isOperateRight = false;
			isOperateLeft = true;
			ViewHelper.setTranslationX(mLeftMenu, mMenuWidth * scale);

		}
	}

}
