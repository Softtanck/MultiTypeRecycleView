package com.cheweishi.android.tools;

import java.math.BigDecimal;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextViewTools {

	public static void setTextViewFontsStyle(Context mContext, TextView tv) {
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * Float控制小数点后两位
	 * @param floatParam
	 * @return
	 */
	public static String toTwoEndFromFloat(float floatParam) {
		BigDecimal b = new BigDecimal(floatParam);
		float a = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return a + "";
	}

	public static void getWidth(TextView tv) {
		float width = getFontWidth(tv);
		float height = getFontHeight(tv);
		ViewGroup.LayoutParams params = tv.getLayoutParams();
		if (width > height) {
			params.width = ((int) width + 1);
			params.height = ((int) width + 1);
		} else {
			params.width = ((int) height + 1);
			params.height = ((int) height + 1);
		}
		tv.setLayoutParams(params);
	}

	public static int getFontWidth(TextView tv) {
		Paint paint = tv.getPaint();
		paint.setTextSize(tv.getTextSize());
		return getStringWidth(tv.getText().toString(), paint);
	}

	public static int getStringWidth(String str, Paint paint) {
		return (int) paint.measureText(str);
	}

	public static int getFontHeight(TextView tv) {
		Paint paint = tv.getPaint();
		paint.setTextSize(tv.getTextSize());
		FontMetrics fm = paint.getFontMetrics();
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}
}
