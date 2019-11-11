package com.cheweishi.android.widget;

import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.view.View;

public abstract class ClickableSpan extends CharacterStyle implements
		UpdateAppearance {

	/**
	 * Performs the click action associated with this span.
	 */
	public abstract void onClick(View widget);

	/**
	 * Makes the text underlined and in the link color.
	 */
	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(ds.linkColor);
		ds.setUnderlineText(true);
	}
}