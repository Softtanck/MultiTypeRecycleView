//package com.cheweishi.android.activity;
//
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnFocusChangeListener;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.baidu.nplatform.comapi.syncclouddata.SyncListener;
//import com.cheweishi.android.R;
//import com.cheweishi.android.adapter.UmengReplyAdapter;
//import com.cheweishi.android.utils.BitmapUtils;
//import com.cheweishi.android.utils.CommonUtils;
//import com.cheweishi.android.widget.PasteEditText;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.view.annotation.ViewInject;
///**
// * 友盟意见反馈
// * @author mingdasen
// *
// */
//public class UmengFbConversationActivity extends BaseActivity {
//	@ViewInject(R.id.recording_hint)
//	private TextView recordingHint;
//	@ViewInject(R.id.recording_container)
//	private View recordingContainer;
//	@ViewInject(R.id.et_sendmessage)
//	private PasteEditText mEditTextContent;
//	@ViewInject(R.id.btn_set_mode_keyboard)
//	private View buttonSetModeKeyboard;
//	@ViewInject(R.id.list)
//	private ListView listView;
//	@ViewInject(R.id.btn_set_mode_voice)
//	private View buttonSetModeVoice;
//	@ViewInject(R.id.btn_more)
//	private Button btnMore;
//	@ViewInject(R.id.name)
//	private TextView titleName;
//	@ViewInject(R.id.container_remove)
//	private RelativeLayout containerRemove;
//	@ViewInject(R.id.iv_emoticons_normal)
//	private ImageView iv_emoticons_normal;
//	@ViewInject(R.id.iv_emoticons_checked)
//	private ImageView iv_emoticons_checked;
//	@ViewInject(R.id.edittext_layout)
//	private RelativeLayout edittext_layout;// 输入框
//	@ViewInject(R.id.more)
//	private View more;
//	@ViewInject(R.id.ll_face_container)
//	private LinearLayout emojiIconContainer;
//	@ViewInject(R.id.ll_btn_container)
//	private LinearLayout btnContainer;
//	@ViewInject(R.id.btn_send)
//	private View buttonSend;
//	private Conversation mConversation;// 反馈会话
//	private UmengReplyAdapter adapter;
//	private final int RESULT_LOAD_IMAGE = 1;
//	private Timer timer = new Timer();
//	private MyTimerTask mTimerTask;
//	private List<Reply> replyList;
//	public final int HANDLE_MASSAGE_TYPE_ADD_IMAGE_REPLY = 4;// 添加图片反馈
//
//	@SuppressLint("HandlerLeak")
//	private Handler mHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case HANDLE_MASSAGE_TYPE_ADD_IMAGE_REPLY:
//				if (CommonUtils.isNetWorkConnected(UmengFbConversationActivity.this)) {
//					mConversation.addUserReply("", (String) msg.obj, Reply.CONTENT_TYPE_IMAGE_REPLY, -1);
//					sync();
//					adapter.notifyDataSetChanged();
//					// 数据同步
//					sync();
//				} else {
//					showToast(R.string.network_isnot_available);
//				}
//				break;
//
//			case 100:
//				replyList = mConversation.getReplyList();
//				adapter.notifyDataSetChanged();
//				break;
//			}
//		}
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_chat);
//		ViewUtils.inject(this);
//		initView();
//		setUpView();
//	}
//	/**
//	 * 数据初始化
//	 */
//	private void setUpView() {
//		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//		//电源状态设置
////		((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(
////				PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
//		//获取反馈记录
//		mConversation = new FeedbackAgent(this).getDefaultConversation();
//		replyList = mConversation.getReplyList();
//		adapter = new UmengReplyAdapter(UmengFbConversationActivity.this,replyList);
//		listView.setAdapter(adapter);
//		listView.setSelection(replyList.size());
//		sync();//后台检查是否有新的来自开发者的回复
//		mTimerTask = new MyTimerTask(); // 新建一个任务
//		timer.schedule(mTimerTask, 1000, 5000);
//	}
//	/**
//	 * 初始化控件
//	 */
//	private void initView() {
//		containerRemove.setVisibility(View.GONE);
//		titleName.setText(R.string.feed_back);
//		// 聊天表情隐藏
//		iv_emoticons_normal.setVisibility(View.GONE);
//		// 聊天表情隐藏
//		iv_emoticons_checked.setVisibility(View.GONE);
//		buttonSetModeKeyboard.setVisibility(View.GONE);
//		buttonSetModeVoice.setVisibility(View.GONE);
//		// 设置输入框的背景图片
//		edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
//		edittext_layout.requestFocus();
//		setEditTextListener();
//	}
//	
//	/**
//	 * 编辑控件mEditTextContent相关监听设置
//	 */
//	private void setEditTextListener() {
//		mEditTextContent.setOnFocusChangeListener(new OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if (hasFocus) {
//					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
//				} else {
//					edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_normal);
//				}
//			}
//		});
//		mEditTextContent.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				edittext_layout.setBackgroundResource(R.drawable.input_bar_bg_active);
//				more.setVisibility(View.GONE);
//				iv_emoticons_normal.setVisibility(View.GONE);
//				iv_emoticons_checked.setVisibility(View.GONE);
//				emojiIconContainer.setVisibility(View.GONE);
//				btnContainer.setVisibility(View.GONE);
//			}
//		});
//		// 监听文字框
//		mEditTextContent.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				if (!TextUtils.isEmpty(s)) {
//					btnMore.setVisibility(View.GONE);
//					buttonSend.setVisibility(View.VISIBLE);
//				} else {
//					btnMore.setVisibility(View.VISIBLE);
//					buttonSend.setVisibility(View.GONE);
//				}
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence arg0, int arg1,
//					int arg2, int arg3) {
//			}
//
//			@Override
//			public void afterTextChanged(Editable arg0) {
//			}
//		});
//	}
//
//	/**
//	 * 图标点击事件
//	 * 
//	 * @param view
//	 */
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.btn_send:// 发送
//			String s = mEditTextContent.getText().toString();
//			mEditTextContent.getEditableText().clear();
//			sendText(s);
//			break;
//		default:
//			break;
//		}
//	}
//	/**
//	 * 图片选择
//	 * @param view
//	 */
//	public void moreClick(View view) {
//		Intent intent = new Intent(Intent.ACTION_PICK,
//				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		startActivityForResult(intent, RESULT_LOAD_IMAGE);
//	}
//
//	/**
//	 * 发送文本消息
//	 * 
//	 * @param s
//	 */
//	private void sendText(String content) {
//		if (!TextUtils.isEmpty(content)) {
//			if (CommonUtils.isNetWorkConnected(UmengFbConversationActivity.this)) {
//				// 将内容添加到会话列表
//				mConversation.addUserReply(content);
//				// 刷新ListView
//				// 数据同步
//				sync();
//				adapter.notifyDataSetChanged();
//				// scrollToBottom();
//			} else {
//				showToast(R.string.network_isnot_available);
//			}
//		}
//	}
//
//	// 数据同步
//	public void sync() {
//
//		mConversation.sync(new SyncListener() {
//
//			@Override
//			public void onSendUserReply(List<Reply> replyList) {
//			}
//
//			@Override
//			public void onReceiveDevReply(List<Reply> replyList) {
//				// 刷新ListView
//				if (replyList == null || replyList.size() < 1) {
//					return;
//				}
//				Log.i("result", "===onReceiveDevReply====");
//				Message message = new Message();
//				message.what = 100;
//				mHandler.sendMessage(message);
//			}
//		});
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode,
//			android.content.Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == Activity.RESULT_OK && requestCode == RESULT_LOAD_IMAGE && data != null) {
//			// 选择的是视频，直接返回
//			if (BitmapUtils.isImage(UmengFbConversationActivity.this, data.getData())) {
//				saveReplyImage(UmengFbConversationActivity.this,data.getData(), makeReplyId());
//			} else {
//				showToast(R.string.image_type);
//			}
//		}
//	}
//	/**
//	 * 设置唯一标识码
//	 * @return
//	 */
//	private String makeReplyId() {
//		return "R" + java.util.UUID.randomUUID().toString();
//	}
//
//	/**
//	 * 开启异步任务，保存图片
//	 * 
//	 * @param context
//	 * @param originalUri
//	 * @param name
//	 */
//	public void saveReplyImage(final Context context, final Uri originalUri,
//			final String name) {
//
//		new AsyncTask<Void, Void, Boolean>() {
//			@Override
//			protected Boolean doInBackground(Void... voids) {
//				return BitmapUtils.saveImage(context, originalUri, name);
//			}
//
//			@Override
//			protected void onPostExecute(Boolean result) {
//				if (result) {
//					Message msg = new Message();
//					msg.what = HANDLE_MASSAGE_TYPE_ADD_IMAGE_REPLY;
//					msg.obj = name;
////					pathName = name;
//					mHandler.sendMessage(msg);
//				}
//			}
//		}.execute();
//	}
//	
//	/**
//	 * 返回按钮事件
//	 */
//	public void back(View view) {
//		finish();
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		sync();
//	}
//
//	class MyTimerTask extends TimerTask {
//		@Override
//		public void run() {
//			handler.sendMessage(new Message());
//		}
//	}
//
//	@SuppressLint("HandlerLeak")
//	Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			replyList = mConversation.getReplyList();
//			adapter.notifyDataSetChanged();
//			sync();
//		};
//	};
//
//	@Override
//	protected void onDestroy() {
//		if (mTimerTask != null) {
//			mTimerTask.cancel();
//			mTimerTask = null;
//		}
//		if (timer != null) {
//			timer.cancel();
//			timer = null;
//		}
//		if (handler != null) {
//			handler.removeCallbacksAndMessages(null);
//		}
//		if (mHandler != null) {
//			mHandler.removeCallbacksAndMessages(null);
//		}
//		System.gc();
//		super.onDestroy();
//
//	}
//}
