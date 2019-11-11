package com.cheweishi.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;

public class CustomEditDialog extends Dialog {

	public CustomEditDialog(Context context) {
		super(context);
	}

	public CustomEditDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String pointMessage;
		private String positiveButtonText;
		private String negativeButtonText;
		private String editText1;
		private String editText2;
//		private String tv_text;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private EditText edit1;
		private EditText edit2;
//		private TextView textView;
		private View bottom_line;
		private boolean flag;
		private boolean showEdit1 = false;
		private boolean showEdit2 = false;
		private boolean showText = false;
		public Builder(Context context) {
			this.context = context;
		}

		public Builder(Context context, boolean flag) {
			this.context = context;
			this.flag = flag;
		}

		/** 设置title */
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		/** 设置提示内容 */
		public Builder setMessage(String pointMessage) {
			this.pointMessage = pointMessage;
			return this;
		}

		/** 设置第一个编辑框提示 */
		public Builder setEdit1(String edit) {
			this.editText1 = edit;
			return this;
		}

		/** 设置第二个编辑框提示 */
		public Builder setEdit2(String edit) {
			this.editText2 = edit;
			return this;
		}
//		/** 设置第二个编辑框提示 */
//		public Builder setTextView(String text) {
//			this.tv_text = text;
//			return this;
//		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
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

		public CustomEditDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomEditDialog dialog = new CustomEditDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_custom_edit, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			((TextView) layout.findViewById(R.id.title)).setText(title);
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
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
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
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			if (pointMessage != null) {
				((TextView) layout.findViewById(R.id.tv_message))
						.setText(pointMessage);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			if (showEdit1 == true) {
				EditText et1 = ((EditText) layout.findViewById(R.id.et_message));
				et1.setVisibility(View.VISIBLE);
				et1.setText(editText1);
				setEt1(et1);
			}else {
				layout.findViewById(R.id.et_message).setVisibility(View.GONE);
			}
			if (showEdit2 == true) {
				EditText et2 =((EditText) layout.findViewById(R.id.et_Maintenance_mileage));
				et2.setText(editText2);
				setEt2(et2);
			}else {
				layout.findViewById(R.id.et_Maintenance_mileage).setVisibility(View.GONE);
			}
//			if (showText == true) {
//				TextView tv =((TextView) layout.findViewById(R.id.tv_year_message));
//				tv.setVisibility(View.VISIBLE);
//				tv.setText(tv_text);
//				setTv(tv);
//			}else {
//				((TextView) layout.findViewById(R.id.tv_year_message)).setVisibility(View.GONE);
//			}
			bottom_line = layout.findViewById(R.id.dialog_bottomLayout);
			if (flag == true) {
				hideLayout();
			}
			dialog.setContentView(layout);
			return dialog;

		}

		public boolean isShowEdit1() {
			return showEdit1;
		}

		public boolean isShowEdit2() {
			return showEdit2;
		}
		public boolean isShowTv() {
			return showText;
		}

		public void setShowEdit1(boolean showEtFlag) {
			this.showEdit1 = showEtFlag;
		}

		public void setShowEdit2(boolean showEtFlag) {
			this.showEdit2 = showEtFlag;
		}
		public void setShowTv(boolean showTvFlag) {
			this.showText = showTvFlag;
		}

		public EditText getEt1() {
			return edit1;
		}

		public void setEt1(EditText et) {
			this.edit1 = et;
		}

		public EditText getEt2() {
			return edit2;
		}

		public void setEt2(EditText et) {
			this.edit2 = et;
		}
//		public TextView getTv() {
//			return textView;
//		}
//
//		public void setTv(TextView tv) {
//			this.textView = tv;
//		}
	}
}
