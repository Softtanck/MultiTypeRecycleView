package com.cheweishi.android.interfaces;

import com.cheweishi.android.widget.MyScrollView;

public interface ScrollViewListener {
	void onScrollChanged(MyScrollView scrollView, int x, int y, int oldx,
			int oldy);
}
