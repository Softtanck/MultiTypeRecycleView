package com.cheweishi.android.dialog;

import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.utils.StringUtil;

public class WashCarDateDialog extends Dialog {

	public WashCarDateDialog(Context context) {
		super(context);
	}

	public WashCarDateDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;

		private Date mDate;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private int selectPosition = 1;
		private ImageView img1;
		private ImageView img2;
		private TextView tv_count;
		

		public Builder(Context context) {
			this.context = context;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public void setDate(Date date) {
			mDate = date;
		}

		private String getCurrentDate(Date date) {
			return StringUtil.getDate(date, "yyyy-MM-dd");
		}

		public String getSelectPosition() {
			return "(" + getCurrentDate(mDate) + ")";
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public void setCount(String str){
			tv_count.setText(str);
		} 
		
		public WashCarDateDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final WashCarDateDialog dialog = new WashCarDateDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_washcar, null);
			tv_count = (TextView) layout.findViewById(R.id.tv_count);
			initView(layout, dialog);

			dialog.setContentView(layout);
			return dialog;
		}

		private void initView(View v, final WashCarDateDialog dialog) {

			v.findViewById(R.id.positiveButton).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							positiveButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_POSITIVE);
						}
					});

			v.findViewById(R.id.negativeButton).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							negativeButtonClickListener.onClick(dialog,
									DialogInterface.BUTTON_NEGATIVE);
						}
					});

		}

	}

}
