package com.cheweishi.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;

import com.cheweishi.android.R;

/**
 * 选择生日日期的Dialog
 */
public class DateSelectorDialogBuilder extends Dialog {

	/**
	 * // * 默认方向标示 //
	 */
	public interface OnSaveListener {
		void onSaveSelectedDate(String selectedDate);
	}

	public DateSelectorDialogBuilder(Context context) {
		super(context);
	}

	public DateSelectorDialogBuilder(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private DateSelectorWheelView dateWheelView;
		private FrameLayout flSecondeCustomLayout;
		private Button button1;
		private Button button2;
		private OnSaveListener saveListener;
		private android.view.View.OnClickListener clickListener1;
		private android.view.View.OnClickListener clickListener2;

		public Builder(Context context) {
			this.context = context;
		}

		public DateSelectorDialogBuilder create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final DateSelectorDialogBuilder dialog = new DateSelectorDialogBuilder(
					context, R.style.Dialog);
			View view = inflater.inflate(R.layout.dialog_date_selector_layout,
					null);
			dialog.addContentView(view, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			dateWheelView = (DateSelectorWheelView) view
					.findViewById(R.id.pdwv_date_time_selector_wheelView);
			button1 = (Button) view.findViewById(R.id.positiveButton);
			button2 = (Button) view.findViewById(R.id.negativeButton);
			button1.setOnClickListener(clickListener1);
			button2.setOnClickListener(clickListener2);
			dateWheelView.setTitleVisibility(false);
			return dialog;
		}

		/**
		 * 获取日期选择器
		 * 
		 * @return
		 */
		public DateSelectorWheelView getDateWheelView() {
			return dateWheelView;
		}

		public String getSelectedDate() {
			return dateWheelView.getSelectedDate();
		}

		/**
		 * 设置保存监听
		 * 
		 * @param saveListener
		 */
		public void setOnSaveListener(OnSaveListener saveListener) {
			this.saveListener = saveListener;
		}

		/**
		 * 设置左侧按钮监听
		 * 
		 * @param saveListener
		 */
		public void setOnButton1Listener(
				android.view.View.OnClickListener clickListener) {
			this.clickListener1 = clickListener;
		}

		/**
		 * 设置右侧监听
		 * 
		 * @param saveListener
		 */
		public void setOnButton2Listener(
				android.view.View.OnClickListener clickListener) {
			this.clickListener2 = clickListener;
		}

		/**
		 * 最初显示时是否可以可见
		 * 
		 * @param visibility
		 */
		public void setWheelViewVisibility(int visibility) {
			dateWheelView.setDateSelectorVisiblility(visibility);
		}

		/**
		 * 最初显示时是否可以可见
		 * 
		 * @param visibility
		 */
		public void setCurrentYear(String year) {
			dateWheelView.setCurrentYear(year);
		}
		
	}
}
