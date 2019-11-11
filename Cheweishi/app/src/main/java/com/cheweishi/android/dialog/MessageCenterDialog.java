package com.cheweishi.android.dialog;


import com.cheweishi.android.R;
import com.cheweishi.android.activity.MessagerCenterActivity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MessageCenterDialog extends BaseDialog {
	private static MessageCenterDialog mMessageCenterDialog;
	// 确认
	private Button positiveButton;
	// 取消
	private Button negativeButton;
	// 消息
	private TextView message;
	/**
	 * 1 全部已读 0 清楚所有,3删除
	 */
	private  int type;
	
	private Context context;

	private MessageCenterDialog(Context context,int type) {
		super(context);
		this.context=context;
		this.type=type;
	}

	public static synchronized void OpenDialog(Context context,int type) {
		if (mMessageCenterDialog == null) {
			mMessageCenterDialog = new MessageCenterDialog(context,type);
			mMessageCenterDialog.setCancelable(false);
		}
		mMessageCenterDialog.show();
	}
	
	public  static void claoseDialog(){
		if (mMessageCenterDialog != null) {
			mMessageCenterDialog.dismiss();
			mMessageCenterDialog=null;
		}
	}

	@Override
	public void initContentView() {
		this.setContentView(R.layout.dialog_messagecenter);
	}

	@Override
	public void initViews() {
		this.positiveButton = (Button) findViewById(R.id.positiveButton);
		this.negativeButton = (Button) findViewById(R.id.negativeButton);
		this.message = (TextView) findViewById(R.id.message);
	}

	@Override
	public void initLists() {
		this.positiveButton.setOnClickListener(this);
		this.negativeButton.setOnClickListener(this);
	}
	@Override
	public void initDatas() {
		if (type==3) {
			this.message.setText("您确认要将所有信息设置成为已读吗？？？");
		}else if(type==0){
			this.message.setText("您确认要将所有信息清空吗？");
		}else if (type==1){
			this.message.setText("您确认要删除选中的信息吗？");
		}else if (type==5) {
			this.message.setText(context.getString(R.string.no_band_gasstation));
		}else if (type==6) {
			this.message.setText(context.getString(R.string.no_login_gasstation));
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.positiveButton:
			if (type==3) {
				((MessagerCenterActivity)mContext).setAllread();
				claoseDialog();
			}else if(type==0)  {
				((MessagerCenterActivity)mContext).setAllClear();
				claoseDialog();
			}
			else if(type==1)  {
				((MessagerCenterActivity)mContext).checkDe();
				claoseDialog();
			}else if (type==5) {
				claoseDialog();
			}else if (type==6) {
				claoseDialog();
			}
			break;
		case R.id.negativeButton:
				claoseDialog();
			break;

		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		claoseDialog();
		super.onBackPressed();
	}
}
