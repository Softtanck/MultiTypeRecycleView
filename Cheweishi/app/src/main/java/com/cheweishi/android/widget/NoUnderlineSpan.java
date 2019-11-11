package com.cheweishi.android.widget;

import android.text.TextPaint;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

//自定义urlspan 去掉下划线 
public class NoUnderlineSpan extends URLSpan {
	public NoUnderlineSpan(String url) {
		super(url);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		super.updateDrawState(ds);
		ds.setUnderlineText(false);
	//	ds.setColor(Color.BLACK);
	}
}