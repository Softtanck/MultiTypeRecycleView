package com.cheweishi.android.widget;

import com.cheweishi.android.R;
import com.cheweishi.android.tools.EmptyTools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

/**
 * An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * Implement IXListViewListener, and see stopRefresh() / stopLoadMore()
 * 
 * @author morton
 * 
 */
public class XListView extends ListView implements OnScrollListener {

	private float mLastY = -1; // save event y
	private Scroller mScroller; // used for scroll back
	private OnScrollListener mScrollListener; // user's scroll listener
	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;
	// -- header view
	private XListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.
	private RelativeLayout mHeaderViewContent;
	private TextView mHeaderTimeView;
	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;//
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private XListViewFooter mFooterView;
	private boolean mEnablePullLoad;
	private boolean mPullLoading;
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;
	private Context context;

	private final static int SCROLL_DURATION = 400; // scroll back duration
	private final static int PULL_LOAD_MORE_DELTA = 10; // when pull up >= 50px
														// at bottom, trigger
														// load more.
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	// 编辑状态下不可下拉刷新
	private boolean compileRefresh = true;
	// 小于10条数据的时候
	public boolean isRefresh = true;

	public void setCompileRefresh(boolean compileRefresh) {

		if (!compileRefresh) {
			mFooterView.setmHintViewGone();
		} else {
			mFooterView.setmHintViewVisible();
		}
		this.compileRefresh = compileRefresh;
	}

	/**
	 * @param context
	 */
	public XListView(Context context) {
		super(context);
		this.context = context;
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initWithContext(context);
	}

	public XListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);
		// init header view
		mHeaderView = new XListViewHeader(context);
		mHeaderViewContent = (RelativeLayout) mHeaderView
				.findViewById(R.id.xlistview_header_content);
		mHeaderTimeView = (TextView) mHeaderView
				.findViewById(R.id.xlistview_header_time);
		addHeaderView(mHeaderView);
		// init footer view
		mFooterView = new XListViewFooter(context);
		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderViewContent.getHeight();
						getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		// make sure XListViewFooter is the last footer view, and only add once.

		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			addFooterView(mFooterView);
			this.setFooterDividersEnabled(false);
//			if (adapter.getCount() <= 0) {
//				EmptyTools.setEmpty(getContext(), this);
//			}
		}
		super.setAdapter(adapter);
	}

	/**
	 * enable or disable pull down refresh feature.
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderViewContent.setVisibility(View.INVISIBLE);
		} else {
			mHeaderViewContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * enable or disable pull up load more feature.
	 * 
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			// mFooterView.hide();
			mFooterView.setOnClickListener(null);
			mFooterView.setState(XListViewFooter.STATE_LOADINGOVER);
		} else {
			mPullLoading = false;
			mFooterView.show();
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			// both "pull up" and "click" will invoke load more.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	public void stopLoadMore() {
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(XListViewFooter.STATE_NORMAL);
		}
	}

	/**
	 * set last refresh time
	 * 
	 * @param time
	 */
	public void setRefreshTime(String time) {
		mHeaderTimeView.setText(time);
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta
				+ mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(XListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(XListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); // scroll to top each time
	}

	/**
	 * reset header view's height.
	 */
	private void resetHeaderHeight() {
		int height = mHeaderView.getVisiableHeight();
		if (height == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && height <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && height > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		mScroller.startScroll(0, height, 0, finalHeight - height,
				SCROLL_DURATION);
		// trigger computeScroll
		invalidate();
	}

	public void itemXiaoyuTen(boolean Refresh) {
		// if (getAdapter().getCount()-2<10) {

		this.isRefresh = Refresh;
		if (!Refresh) {
			mFooterView.setState(XListViewFooter.STATE_LOADINGOVER);
		}
	}

	// /**
	// * 设置没有数据了
	// */
	// public void setTextOber(){
	// int itemHeight = getItemHeight();
	// int scressHeight = ScreenUtils.getScressHeight(context);
	// if (itemHeight<scressHeight) {
	// mFooterView.setState(XListViewFooter.STATE_LOADINGOVER);
	// }
	// }

	boolean flg = false;

	/**
	 * 更新下拉刷新
	 * 
	 * @param delta
	 */
	private void updateFooterHeight(float delta) {

		int height = mFooterView.getBottomMargin() + (int) delta;
		if (mEnablePullLoad && !mPullLoading) {
			if (height > PULL_LOAD_MORE_DELTA) { // height enough to invoke load
				mFooterView.setState(XListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(XListViewFooter.STATE_NORMAL);
			}
		}
		mFooterView.setBottomMargin(height);
	}

	/**
	 * 移动到当前我位置
	 */
	private void resetFooterHeight() {
		int bottomMargin = mFooterView.getBottomMargin();
		if (bottomMargin > 0) {
			mScrollBack = SCROLLBACK_FOOTER;
			mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
					SCROLL_DURATION);
			invalidate();
		}
	}

	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(XListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (getFirstVisiblePosition() == 0
					&& (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
				resetHeaderHeight();
			} else if (getLastVisiblePosition() == mTotalItemCount - 1
					&& (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
				// last item, already pulled up or want to pull up.

				// 如果大于十条，执行更新
				if (isRefresh && compileRefresh) {
					updateFooterHeight(-deltaY / OFFSET_RADIO);
				}
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0) {
				// invoke refresh
				if (mEnablePullRefresh
						&& mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(XListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						mListViewListener.onRefresh();
					}
					resetHeaderHeight();
				} else {
					if (getLastVisiblePosition() == mTotalItemCount - 1) {
						// invoke load more.
						if (mEnablePullLoad
								&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
							startLoadMore();
						}
					}
				}
			} else if (getLastVisiblePosition() == mTotalItemCount - 1) {
				// invoke load more.
				if (mEnablePullLoad
						&& mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
					startLoadMore();
				}
				resetFooterHeight();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	// /**
	// * 计算所以Itm的高度
	// * @return
	// */
	// public boolean getItemHeight(){
	// boolean flag=false;
	// int count = getAdapter().getCount();
	// //总长度
	// int totalHeight=0;
	// for (int i = 0; i < count; i++) {
	// View listItem = getAdapter().getView(i, null, this);
	// listItem.measure(0, 0); // 计算子项View 的宽高
	// int list_child_item_height =
	// listItem.getMeasuredHeight()+this.getDividerHeight();
	// totalHeight += list_child_item_height; // 统计所有子项的总高度
	// }
	//
	// if (totalHeight<ScreenUtils.getScressHeight(context)) {
	// flag=true;
	// }
	// return flag;
	// }

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			} else {
				mFooterView.setBottomMargin(mScroller.getCurrY());
			}
			postInvalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	/**
	 * firstVisibleItem 当前屏幕上第一个的下标
	 * 
	 * visibleItemCount 可见item的总数
	 * 
	 * totalItemCount item的总数
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	}

	public void setXListViewListener(IXListViewListener l) {
		mListViewListener = l;
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		void onXScrolling(View view);
	}

	/**
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		void onRefresh();

		void onLoadMore();
	}
}
