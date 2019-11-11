/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.handmark.pulltorefresh.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.internal.EmptyViewMethodAccessor;

public class PullToRefreshExpandableListView extends PullToRefreshAdapterViewBase<ExpandableListView> {

	private static final int BACK_SCALE = 0;
	private boolean isHaveHead = false;// 头部是否有图片
	private float scaleY = 0;
	private boolean isBacking = false;// 是否处在回弹状态
	private int displayWidth;
	private Context mContext;
	private Bitmap bmp;
	private View headerView;
	private ImageView imageView;
	/** 用于记录拖拉图片移动的坐标位置 */
	private Matrix matrix = new Matrix();
	/** 用于记录图片要进行拖拉时候的坐标位置 */
	private Matrix currentMatrix = new Matrix();
	private Matrix defaultMatrix = new Matrix();
	private float imgHeight, imgWidth;
	/** 记录是拖拉照片模式还是放大缩小照片模式 0:拖拉模式，1：放大 */
	private int mode = 0;// 初始状态
	/** 拖拉照片模式 */
	private final int MODE_DRAG = 1;
	/** 用于记录开始时候的坐标位置 */
	private PointF startPoint = new PointF();

	private int mImageId;

	private AttributeSet attrs;

	public PullToRefreshExpandableListView(Context context) {
		super(context);
		this.mContext=context;
		initView();
	}

	public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext=context;
		this.attrs = attrs;
		initView();
	}

	public PullToRefreshExpandableListView(Context context, Mode mode) {
		super(context, mode);
	}

	public PullToRefreshExpandableListView(Context context, Mode mode, AnimationStyle style) {
		super(context, mode, style);
		this.mContext=context;
	}

	@Override
	public final Orientation getPullToRefreshScrollDirection() {
		return Orientation.VERTICAL;
	}

	@Override
	protected ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
		final ExpandableListView lv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			lv = new InternalExpandableListViewSDK9(context, attrs);
		} else {
			lv = new InternalExpandableListView(context, attrs);
		}

		// Set it to this so it can be used in ListActivity/ListFragment
		lv.setId(android.R.id.list);
		return lv;
	}


	private void initView() {
		/* 取得屏幕分辨率大小 */
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mWm.getDefaultDisplay().getMetrics(dm);
		displayWidth = dm.widthPixels;

		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.ImgListView);
		mImageId = a.getResourceId(R.styleable.ImgListView_headimage, 0);
		a.recycle();
		if (null == bmp && mImageId != 0) {
			bmp = BitmapFactory.decodeResource(getResources(), mImageId);
			initHead();
		}
	}


	private void initHead() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		headerView = inflater.inflate(R.layout.top_img, null);
		imageView = (ImageView) headerView.findViewById(R.id.imageView);
		float scale = (float) displayWidth / (float) bmp.getWidth();// 1080/1800
		matrix.postScale(scale, scale, 0, 0);
		imageView.setImageMatrix(matrix);
		defaultMatrix.set(matrix);
		imgHeight = scale * bmp.getHeight();
		imgWidth = scale * bmp.getWidth();
		ListView.LayoutParams relativeLayout = new ListView.LayoutParams((int) imgWidth, (int) imgHeight);
		imageView.setLayoutParams(relativeLayout);
		this.getRefreshableView().addHeaderView(headerView);
		isHaveHead = true;
	}
	class InternalExpandableListView extends ExpandableListView implements EmptyViewMethodAccessor {

		public InternalExpandableListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshExpandableListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}
	}

	@TargetApi(9)
	final class InternalExpandableListViewSDK9 extends InternalExpandableListView {

		public InternalExpandableListViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
									   int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

			// Does all of the hard work...
			OverscrollHelper.overScrollBy(PullToRefreshExpandableListView.this, deltaX, scrollX, deltaY, scrollY,
					isTouchEvent);

			return returnValue;
		}
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (!isHaveHead) {// 无头部图片
			return super.onTouchEvent(event);
		}
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 手指压下屏幕
			case MotionEvent.ACTION_DOWN:
				if (isBacking) {
					return super.onTouchEvent(event);
				}
				int[] location = new int[2];
				imageView.getLocationInWindow(location);
				if (location[1] >= 0) {
					mode = MODE_DRAG;
					// 记录ImageView当前的移动位置
					currentMatrix.set(imageView.getImageMatrix());
					startPoint.set(event.getX(), event.getY());
				}
				break;
			// 手指在屏幕上移动，改事件会被不断触发
			case MotionEvent.ACTION_MOVE:
				// 拖拉图片
				if (mode == MODE_DRAG) {
					float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
					float dy = event.getY() - startPoint.y; // 得到y轴的移动距离
					// 在没有移动之前的位置上进行移动
					if (dy / 2 + imgHeight <= 1.5 * imgHeight) {
						matrix.set(currentMatrix);
						float scale = (dy / 2 + imgHeight) / (imgHeight);// 得到缩放倍数
						if (dy > 0) {
							scaleY = dy;
							ListView.LayoutParams relativeLayout = new ListView.LayoutParams((int) (scale * imgWidth), (int) (scale * imgHeight));
							imageView.setLayoutParams(relativeLayout);
							//scale to 1
							matrix.postScale(1, scale, imgWidth / 2, 0);
							imageView.setImageMatrix(matrix);
						}
					}
				}
				break;
			// 手指离开屏幕
			case MotionEvent.ACTION_UP:
				// 当触点离开屏幕，图片还原
				mHandler.sendEmptyMessage(BACK_SCALE);
			case MotionEvent.ACTION_POINTER_UP:
				mode = 0;
				break;
		}

		return super.onTouchEvent(event);
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case BACK_SCALE:
					float scale = (scaleY / 2 + imgHeight) / (imgHeight);// 得到缩放倍数
					if (scaleY > 0) {
						isBacking = true;
						matrix.set(currentMatrix);
						ListView.LayoutParams relativeLayout = new ListView.LayoutParams((int) (scale * imgWidth), (int) (scale * imgHeight));
						imageView.setLayoutParams(relativeLayout);
						//scale----->1
						matrix.postScale(1, scale, imgWidth / 2, 0);
						imageView.setImageMatrix(matrix);
						scaleY = scaleY / 2 - 1;
						mHandler.sendEmptyMessageDelayed(BACK_SCALE, 20);
					} else {
						scaleY = 0;
						ListView.LayoutParams relativeLayout = new ListView.LayoutParams((int) imgWidth, (int) imgHeight);
						imageView.setLayoutParams(relativeLayout);
						matrix.set(defaultMatrix);
						imageView.setImageMatrix(matrix);
						isBacking = false;
					}
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

}
