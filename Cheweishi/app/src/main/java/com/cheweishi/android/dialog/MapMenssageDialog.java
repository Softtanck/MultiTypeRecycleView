package com.cheweishi.android.dialog;

import com.cheweishi.android.R;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MapMenssageDialog extends BaseDialog {

	private static MapMenssageDialog mMessageCenterDialog;
	// 确认
	private Button positiveButton;
	// 取消
	private Button negativeButton;
	// 消息
	private TextView message;
	private String msg;

	private MapMenssageDialog(Context context, String msg) {
		super(context);
		this.msg = msg;
	}

	public static synchronized void OpenDialog(Context context, String msg) {
		if (mMessageCenterDialog == null) {
			mMessageCenterDialog = new MapMenssageDialog(context, msg);
			mMessageCenterDialog.setCancelable(false);
		}
		mMessageCenterDialog.show();
	}

	public static void claoseDialog() {
		if (mMessageCenterDialog != null) {
			mMessageCenterDialog.dismiss();
			mMessageCenterDialog = null;
		}
	}

	@Override
	public void initContentView() {
		this.setContentView(R.layout.dialog_normal_layout);
	}

	@Override
	public void initViews() {
		this.positiveButton = (Button) findViewById(R.id.positiveButton);
		this.positiveButton.setText("确定");
		this.negativeButton = (Button) findViewById(R.id.negativeButton);
		this.negativeButton.setVisibility(View.GONE);
		this.message = (TextView) findViewById(R.id.message);
	}

	@Override
	public void initLists() {
		this.positiveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.positiveButton:
			claoseDialog();
			break;

		}
	}

	@Override
	public void initDatas() {
		message.setText(msg);
	}

	@Override
	public void onBackPressed() {
		claoseDialog();
		super.onBackPressed();
	}

}
