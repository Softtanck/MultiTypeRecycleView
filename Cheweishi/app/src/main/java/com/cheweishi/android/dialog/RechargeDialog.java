package com.cheweishi.android.dialog;

import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.cheweishi.android.R;
import com.cheweishi.android.dialog.WashCarDateDialog.Builder;
import com.cheweishi.android.utils.CustomProgressDialog;
import com.cheweishi.android.utils.StringUtil;
import com.cheweishi.android.widget.CustomDialog;

public class RechargeDialog extends Dialog {
	public static float moneyAccount;

	public RechargeDialog(Context context) {
		super(context);
	}

	public RechargeDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private CharSequence message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private boolean showEtFlag = false;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private EditText et;
		private View bottom_line;
		private boolean flag;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder(Context context, boolean flag) {
			this.context = context;
			this.flag = flag;
		}

		public Builder setMessage(CharSequence message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = context.getText(message);
			return this;
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

		public void hideLayout() {
			// Toast.makeText(context, "xiasj", Toast.LENGTH_LONG).show();
			bottom_line.setVisibility(View.GONE);
		}

		public RechargeDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final RechargeDialog dialog = new RechargeDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_recharge, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			// ((TextView) layout.findViewById(R.id.title)).setText(title);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					layout.findViewById(R.id.positiveButton)
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			}
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					layout.findViewById(R.id.negativeButton)
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);

								}
							});
				}
			}
			moneyAccount = 600f;
			RadioGroup rg = (RadioGroup) layout.findViewById(R.id.radiogroup);
			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(RadioGroup arg0, int arg1) {
					// TODO Auto-generated method stub
					switch (arg1) {
					case R.id.rbtn1:
						moneyAccount = 100f;
						break;
					case R.id.rbtn2:
						moneyAccount = 300f;
						break;
					case R.id.rbtn3:
						moneyAccount = 600f;
						break;
					case R.id.rbtn4:
						moneyAccount = 1000f;
						break;
					}
				}
			});
			dialog.setContentView(layout);
			return dialog;
		}

		public boolean isShowEtFlag() {
			return showEtFlag;
		}

		public void setShowEtFlag(boolean showEtFlag) {
			this.showEtFlag = showEtFlag;
		}

		public EditText getEt() {
			return et;
		}

		public void setEt(EditText et) {
			this.et = et;
		}

	}
}
