//package com.cheweishi.android.widget;
//
//import com.cheweishi.android.R;
//import com.cheweishi.android.biz.XUtilsImageLoader;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
///**
// * @author Bill
// * @rotateDegrees 仅支持90.0 180.0 0.0(默认)
// * @background 图片资源文件
// */
//public class RotateableView extends ImageView {
//	/**
//	 * 命名区域
//	 */
//	private final String namespace = "http://com.bill.cn";
//	/**
//	 * 保存创建旋转角度
//	 */
//	private float mRotateDegrees;
//	/**
//	 * 保存创建背景图片的ID
//	 */
//	private int mBackGroudDrawableId;
//	/**
//	 * 利用图片ID加载图片
//	 */
//	private Drawable mBackGroudDrawable;
//	/**
//	 * 原始图片所需宽、高
//	 */
//	private int mBackGroundWidth;
//	private int mBackGroundHeight;
//	private String path;
//
//	public RotateableView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//		// TODO Auto-generated constructor stub
//		mBackGroudDrawableId = attrs.getAttributeResourceValue(namespace,
//				"background", R.drawable.location_point);
//		mBackGroudDrawable = context.getResources().getDrawable(
//				mBackGroudDrawableId);
//		mRotateDegrees = attrs.getAttributeFloatValue(namespace,
//				"rotateDegrees", 0.0f);
//	}
//
//	@Override
//	protected void onDraw(Canvas canvas) {
//		// TODO Auto-generated method stub
//		super.onDraw(canvas);
//		/**
//		 * 旋转画布
//		 */
//		if (mRotateDegrees == 90.0f) {
//			canvas.rotate(mRotateDegrees, 0, 0);
//			canvas.translate(0, -mBackGroundHeight);
//		} else {
//			canvas.rotate(mRotateDegrees, mBackGroundWidth / 2,
//					mBackGroundHeight / 2);
//		}
//		/**
//		 * 执行draw
//		 */
//		mBackGroudDrawable.setBounds(10, 10, mBackGroundWidth - 10,
//				mBackGroundHeight - 10);
//		mBackGroudDrawable.draw(canvas);
//	}
//
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		/**
//		 * 设定View显示区域
//		 */
//		mBackGroundHeight = 120;// mBackGroudDrawable.getMinimumHeight();
//		mBackGroundWidth = 120;// mBackGroudDrawable.getMinimumWidth();
//		if (mRotateDegrees == 90.0f) {
//			setMeasuredDimension(mBackGroundHeight, mBackGroundWidth);
//		} else {
//			setMeasuredDimension(mBackGroundWidth, mBackGroundHeight);
//		}
//	}
//
//	public String getPath() {
//		return path;
//	}
//
//	public void setPath(String path) {
//		this.path = path;// }
//		XUtilsImageLoader.getxUtilsImageLoader(getContext(),
//				R.drawable.infor_moren2x).load(this, path);
//	}
//}