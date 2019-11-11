//package com.cheweishi.android.adapter;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.cheweishi.android.R;
//import com.cheweishi.android.biz.XUtilsImageLoader;
//import com.cheweishi.android.tools.LoginMessageUtils;
//import com.cheweishi.android.utils.BitmapUtils;
//import com.cheweishi.android.widget.XCRoundImageView;
//
///**
// * 友盟意见反馈适配器
// * 
// * @author mingdasen
// * 
// */
//public class UmengReplyAdapter extends BaseAdapter implements OnClickListener {
//
//	private static final int MESSAGE_TYPE_REVC_TXT = 0;
//	private static final int MESSAGE_TYPE_SENT_TXT = 1;
//	private static final int MESSAGE_TYPE_REVC_IMAGE = 2;
//	private static final int MESSAGE_TYPE_SENT_IMAGE = 3;
//	private List<Reply> replyList;
//	private Activity context;
//	private LayoutInflater inflater;
//	private int convertType;
//	private SimpleDateFormat sdf;
//
//	public UmengReplyAdapter(Activity context, List<Reply> replyList) {
//		this.replyList = replyList;
//		this.context = context;
//		inflater = LayoutInflater.from(context);
//	}
//
//	@Override
//	public int getCount() {
//		return replyList == null ? 0 : replyList.size();
//	}
//
//	/**
//	 * 刷新页面
//	 */
//	public void refresh() {
//		notifyDataSetChanged();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return replyList.get(position);
//	}
//
//	@Override
//	public long getItemId(int arg0) {
//		return arg0;
//	}
//
//	/**
//	 * 获取item类型数
//	 */
//	@Override
//	public int getViewTypeCount() {
//		return 4;
//	}
//
//	@Override
//	public int getItemViewType(int position) {
//		// 获取单条回复
//		Reply reply = replyList.get(position);
//		if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
//			// 开发者回复Item布局
//			return "text_reply".equals(reply.content_type) ? MESSAGE_TYPE_REVC_TXT
//					: MESSAGE_TYPE_REVC_IMAGE;
//		} else {
//			// 用户反馈、回复Item布局
//			return "text_reply".equals(reply.content_type) ? MESSAGE_TYPE_SENT_TXT
//					: MESSAGE_TYPE_SENT_IMAGE;
//		}
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// 获取单条回复
//		Reply reply = replyList.get(position);
//		convertType = getItemViewType(position);
//		switch (convertType) {
//		case MESSAGE_TYPE_REVC_TXT:
//			ViewHolder1 holder1 = null;
//			if (convertView == null) {
//				holder1 = new ViewHolder1();
//				convertView = inflater.inflate(R.layout.row_received_message,
//						null);
//				holder1.timestamp = (TextView) convertView
//						.findViewById(R.id.timestamp);
//				holder1.pb = (ProgressBar) convertView
//						.findViewById(R.id.pb_sending);
//				holder1.staus_iv = (ImageView) convertView
//						.findViewById(R.id.msg_status);
//				holder1.iv_avatar = (XCRoundImageView) convertView
//						.findViewById(R.id.iv_userhead);
//				// 这里是文字内容
//				holder1.tv = (TextView) convertView
//						.findViewById(R.id.tv_chatcontent);
//				holder1.tv_usernick = (TextView) convertView
//						.findViewById(R.id.tv_userid);
//				holder1.timestamp.setVisibility(View.GONE);
//				convertView.setTag(holder1);
//			} else {
//				holder1 = (ViewHolder1) convertView.getTag();
//			}
//			holder1.tv.setText(reply.content);
//			setItemStatus(position, reply, holder1.staus_iv, holder1.pb,
//					holder1.iv_avatar, holder1.timestamp);
//			break;
//		case MESSAGE_TYPE_REVC_IMAGE:
//			ViewHolder2 holder2 = null;
//			if (convertView == null) {
//				holder2 = new ViewHolder2();
//				convertView = inflater.inflate(R.layout.row_received_picture,
//						null);
//				holder2.timestamp = (TextView) convertView
//						.findViewById(R.id.timestamp);
//				holder2.iv = ((ImageView) convertView
//						.findViewById(R.id.iv_receiPicture));
//				holder2.iv_avatar = (XCRoundImageView) convertView
//						.findViewById(R.id.iv_userhead);
//				holder2.tv = (TextView) convertView
//						.findViewById(R.id.percentage);
//				holder2.pb = (ProgressBar) convertView
//						.findViewById(R.id.progressBar);
//				holder2.staus_iv = (ImageView) convertView
//						.findViewById(R.id.msg_status);
//				holder2.tv_usernick = (TextView) convertView
//						.findViewById(R.id.tv_userid);
//				holder2.timestamp.setVisibility(View.GONE);
//				holder2.iv.setTag(position);
//				holder2.iv.setOnClickListener(this);
//				convertView.setTag(holder2);
//			} else {
//				holder2 = (ViewHolder2) convertView.getTag();
//				holder2.iv.setTag(position);
//			}
//
//			XUtilsImageLoader.getxUtilsImageLoader(context,
//					R.drawable.infor_moren2x,holder2.iv, reply.content);
//			setItemStatus(position, reply, holder2.staus_iv, holder2.pb,
//					holder2.iv_avatar, holder2.timestamp);
//			break;
//		case MESSAGE_TYPE_SENT_TXT:
//			ViewHolder3 holder3 = null;
//			if (convertView == null) {
//				holder3 = new ViewHolder3();
//				convertView = inflater.inflate(R.layout.row_sent_message, null);
//				holder3.timestamp = (TextView) convertView
//						.findViewById(R.id.timestamp);
//				holder3.pb = (ProgressBar) convertView
//						.findViewById(R.id.pb_sending);
//				holder3.staus_iv = (ImageView) convertView
//						.findViewById(R.id.msg_status);
//				holder3.iv_avatar = (XCRoundImageView) convertView
//						.findViewById(R.id.iv_userhead);
//				// 这里是文字内容
//				holder3.tv = (TextView) convertView
//						.findViewById(R.id.tv_chatcontent);
//				holder3.tv_usernick = (TextView) convertView
//						.findViewById(R.id.tv_userid);
//				holder3.timestamp.setVisibility(View.GONE);
//				convertView.setTag(holder3);
//			} else {
//				holder3 = (ViewHolder3) convertView.getTag();
//			}
//			holder3.tv.setText(reply.content);
//			setItemStatus(position, reply, holder3.staus_iv, holder3.pb,
//					holder3.iv_avatar, holder3.timestamp);
//			break;
//		case MESSAGE_TYPE_SENT_IMAGE:
//			ViewHolder4 holder4 = null;
//			if (convertView == null) {
//				holder4 = new ViewHolder4();
//				convertView = inflater.inflate(R.layout.row_sent_picture, null);
//				holder4.timestamp = (TextView) convertView
//						.findViewById(R.id.timestamp);
//				holder4.iv = ((ImageView) convertView
//						.findViewById(R.id.iv_sendPicture));
//				holder4.iv_avatar = (XCRoundImageView) convertView
//						.findViewById(R.id.iv_userhead);
//				holder4.tv = (TextView) convertView
//						.findViewById(R.id.percentage);
//				holder4.tv.setVisibility(View.GONE);
//				holder4.pb = (ProgressBar) convertView
//						.findViewById(R.id.progressBar);
//				holder4.pb.setVisibility(View.GONE);
//				holder4.staus_iv = (ImageView) convertView
//						.findViewById(R.id.msg_status);
//				holder4.tv_usernick = (TextView) convertView
//						.findViewById(R.id.tv_userid);
//				holder4.timestamp.setVisibility(View.GONE);
//				holder4.iv.setTag(position);
//				holder4.iv.setOnClickListener(this);
//				convertView.setTag(holder4);
//			} else {
//				holder4 = (ViewHolder4) convertView.getTag();
//				holder4.iv.setTag(position);
//			}
//			XUtilsImageLoader.getxUtilsImageLoader(context,
//					R.drawable.infor_moren2x,holder4.iv,
//					Uri.fromFile(new File(BitmapUtils.getImagePathWithName(
//							context, reply.reply_id))) + "");
//			setItemStatus(position, reply, holder4.staus_iv, holder4.pb,
//					holder4.iv_avatar, holder4.timestamp);
//			break;
//
//		default:
//			break;
//		}
//		return convertView;
//	}
//
//	/**
//	 * 显示item状态
//	 * 
//	 * @param position
//	 * @param reply
//	 * @param holder4
//	 */
//	private void setItemStatus(int position, Reply reply, ImageView staus_iv,
//			ProgressBar pb, XCRoundImageView iv_avatar, TextView timestamp) {
//		// 在App界面，对于开发者的Reply来讲status没有意义
//		if (!Reply.TYPE_DEV_REPLY.equals(reply.type)) {
//			// 根据Reply的状态来设置replyStateFailed的状态
//			if (Reply.STATUS_NOT_SENT.equals(reply.status)) {
//				staus_iv.setVisibility(View.VISIBLE);// ImageView
//			} else {
//				staus_iv.setVisibility(View.GONE);
//			}
//
//			// 根据Reply的状态来设置replyProgressBar的状态
//			if (Reply.STATUS_SENDING.equals(reply.status)) {
//				pb.setVisibility(View.VISIBLE);// ProgressBar
//			} else {
//				pb.setVisibility(View.GONE);
//			}
//		}
//		if (Reply.TYPE_DEV_REPLY.equals(reply.type)) {
//			iv_avatar.setBackgroundResource(R.drawable.img);// XCRoundImageView
//		} else {
//			if (LoginMessageUtils.getLoginMessage(context) != null
//					&& LoginMessageUtils.getLoginMessage(context).getUid() != null) {
//				// ImageLoader.getInstance().displayImage(
//				XUtilsImageLoader.getxUtilsImageLoader(context,
//						R.drawable.infor_moren2x,iv_avatar,
//						"http://115.28.161.11:8080/XAI/appDownLoad/downLoadPhoto?path="
//								+ LoginMessageUtils.getLoginMessage(context)
//										.getPhoto());
//			}
//		}
//
//		// 回复的时间数据，这里仿照QQ两条Reply之间相差100000ms则展示时间
//		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if (Reply.TYPE_NEW_FEEDBACK.equals(reply.type) || position == 0) {
//			Date replyTime = new Date(reply.created_at);
//			timestamp.setText(sdf.format(replyTime));// TextView
//			timestamp.setVisibility(View.VISIBLE);
//		}
//		if (replyList.size() > 1 && position > 1) {
//			Reply nextReply = replyList.get(position - 1);
//			if (reply.created_at - nextReply.created_at > 60000) {
//				Date replyTime = new Date(reply.created_at);
//				timestamp.setText(sdf.format(replyTime));
//				timestamp.setVisibility(View.VISIBLE);
//			} else {
//				timestamp.setVisibility(View.GONE);
//			}
//		}
//	}
//
//	class ViewHolder1 {
//		TextView tv;
//		ProgressBar pb;
//		ImageView staus_iv;
//		XCRoundImageView iv_avatar;
//		TextView tv_usernick;
//		TextView timestamp;
//	}
//
//	class ViewHolder2 {
//		ImageView iv;
//		TextView tv;
//		ProgressBar pb;
//		ImageView staus_iv;
//		XCRoundImageView iv_avatar;
//		TextView tv_usernick;
//		TextView timestamp;
//	}
//
//	class ViewHolder3 {
//		TextView tv;
//		ProgressBar pb;
//		ImageView staus_iv;
//		XCRoundImageView iv_avatar;
//		TextView tv_usernick;
//		TextView timestamp;
//	}
//
//	class ViewHolder4 {
//		ImageView iv;
//		TextView tv;
//		ProgressBar pb;
//		ImageView staus_iv;
//		XCRoundImageView iv_avatar;
//		TextView tv_usernick;
//		TextView timestamp;
//	}
//
//	@Override
//	public void onClick(View view) {
//		switch (view.getId()) {
//		case R.id.iv_sendPicture:// 发送
//			showImgDialog(Uri.fromFile(new File(BitmapUtils.getImagePathWithName(
//							context, replyList.get((Integer) view.getTag()).reply_id))) + "");
//			break;
//		case R.id.iv_receiPicture:
//			showImgDialog(replyList.get((Integer) view.getTag()).content);
//			break;
//		default:
//			break;
//		}
//	}
//	
//	private Dialog dialog1;
//	private Button origionImgBtn;
//	private LinearLayout btn_layout;
//	private ImageView origionalImg;
//	/**
//	 * 
//	 * @Title: showDialog
//	 * @Description: TODO(dialog弹出和显示的样式)
//	 * @param 设定文件
//	 * @return void 返回类型
//	 * @throws
//	 */
//	private void showImgDialog(String url) {
//		View view = context.getLayoutInflater().inflate(R.layout.person_seting_dialog,
//				null);
//		dialog1 = new Dialog(context, R.style.transparentFrameWindowStyle);
//
//		dialog1.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.WRAP_CONTENT));
//		origionImgBtn = (Button) view.findViewById(R.id.origionImgBtn);
//		btn_layout = (LinearLayout) view.findViewById(R.id.btn_layout);
//		origionalImg = (ImageView) view.findViewById(R.id.origionalImg);
//		origionImgBtn.setOnClickListener(myListener);
//		origionalImg.setOnClickListener(myListener);
//		btn_layout.setVisibility(View.INVISIBLE);
//		origionalImg.setVisibility(View.VISIBLE);
//		Window window = dialog1.getWindow();
//		// 设置显示动画
//		window.setWindowAnimations(R.style.main_menu_animstyle);
//		WindowManager.LayoutParams wl = window.getAttributes();
//		wl.x = 0;
//		wl.y = context.getWindowManager().getDefaultDisplay().getHeight();
//		// 以下这两句是为了保证按钮可以水平满屏
//		wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
//
//		// 设置显示位置
//		dialog1.onWindowAttributesChanged(wl);
//		// 设置点击外围解散
//		dialog1.setCanceledOnTouchOutside(true);
//
//		final Animation animation1 = AnimationUtils.loadAnimation(context,
//				R.anim.score_business_query_enter);
//		origionalImg.startAnimation(animation1);
//		XUtilsImageLoader.getxUtilsImageLoader(context, R.drawable.zhaochewei_img,origionalImg, url);
//		dialog1.show();
//	}
//
//	OnClickListener myListener = new OnClickListener() {
//
//		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.origionalImg:
//				dialog1.dismiss();
//				break;
//			case R.id.origionImgBtn:
//				btn_layout.setVisibility(View.INVISIBLE);
//				origionalImg.setVisibility(View.VISIBLE);
//				break;
//			}
//		}
//	};
//
//}
