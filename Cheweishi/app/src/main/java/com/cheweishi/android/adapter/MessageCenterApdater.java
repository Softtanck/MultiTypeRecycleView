package com.cheweishi.android.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.MessagerCenterActivity;
import com.cheweishi.android.entity.CheckEntityNative;
import com.cheweishi.android.entity.MessageResponse;
import com.cheweishi.android.tools.DBTools;

public class MessageCenterApdater extends BaseAdapter {
	private List<MessageResponse.MsgBean> textlist;
	private Context mContext;
	private LayoutInflater mInflater;
	// 0正常状态，1编辑状态
	private int ISSHOW = 0;
	// 右边箭头，1显示 0隐藏
	private int isShowJianTou = 1;
	public boolean flag = false;
	private List<CheckEntityNative> checkEntities;
	private MessageResponse.MsgBean messagCenterInfo = null;
	private boolean isRead = false;
	public int check = 0;

	// 展开
	 private Animation animationOpen;
	// 关闭
	 private Animation animationClose;

	public void setISSHOW(int iSSHOW, int isShowJianTou) {
		this.ISSHOW = iSSHOW;
		this.isShowJianTou = isShowJianTou;
		this.notifyDataSetChanged();
		this.flag = true;
	}

	public MessageCenterApdater(List<MessageResponse.MsgBean> textlist,
			Context mContext) {
		super();
		this.textlist = textlist;
		this.mContext = mContext;
		mInflater = LayoutInflater.from(mContext);
		checkEntities = new ArrayList<CheckEntityNative>();
		this.flag = false;
		initCheckEntities();
		notifyDataSetChanged();
	}

	/** 初始化check false **/
	private void initCheckEntities() {
		int size = textlist.size();
		checkEntities.clear();
		for (int i = 0; i < size; i++) {
			checkEntities.add(new CheckEntityNative(false));
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return textlist == null ? 0 : textlist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return textlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void setData(List<MessageResponse.MsgBean> textlist) {
		this.textlist = textlist;
		this.notifyDataSetChanged();
		initCheckEntities();
	}

	public void setDeleteData(List<MessageResponse.MsgBean> textlist) {
		this.textlist = textlist;
		this.notifyDataSetChanged();
		initCheckEntities();
		this.flag = false;
	}

	private String transferLongToDate(Long millSec){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date date= new Date(millSec);
		return sdf.format(date);
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.item_message_center, null);
			viewHolder = new ViewHolder(convertView);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (textlist != null && textlist.size() > position) {
			messagCenterInfo = textlist.get(position);
			// String type = messagCenterInfo.getType();
			isRead = messagCenterInfo.isIsRead();



			if("NEWSMSG".equals(messagCenterInfo.getMessageType())){ // 新闻
				viewHolder.img_logo.setImageResource(R.drawable.message_huodong);
			}else if("PERSONALMSG".equals(messagCenterInfo.getMessageType())){ //个人消息
				viewHolder.img_logo.setImageResource(R.drawable.message_baojing);
			}


			//图片设置
//			if (StringUtil.isEquals("1", messagCenterInfo.getType(), true)) {
//				viewHolder.img_logo.setImageResource(R.drawable.message_baojing);
//			} else if(StringUtil.isEquals("2", messagCenterInfo.getType(), true)){
//				viewHolder.img_logo.setImageResource(R.drawable.message_huodong);
//			}else if(StringUtil.isEquals("3", messagCenterInfo.getType(), true)){
//				viewHolder.img_logo.setImageResource(R.drawable.message_banben);
//			}else if(StringUtil.isEquals("4", messagCenterInfo.getType(), true)){
//				viewHolder.img_logo.setImageResource(R.drawable.message_jiaoyi);
//			}else if(StringUtil.isEquals("5", messagCenterInfo.getType(), true)){
//				viewHolder.img_logo.setImageResource(R.drawable.message_dongtai);
//			}

			//标题
			viewHolder.tv_title.setText(messagCenterInfo.getMessageTitle());
			//时间
			viewHolder.tv_time.setText(transferLongToDate(messagCenterInfo.getCreateDate()));
			//内容
			viewHolder.tv_content.setText(messagCenterInfo.getMessageContent());

			//是否已读
			if (!isRead) {
				viewHolder.img_dian.setVisibility(View.VISIBLE);
			} else {
				viewHolder.img_dian.setVisibility(View.GONE);
			}
			
			if (ISSHOW == 0) {
//				viewHolder.msg_item_check.setVisibility(View.GONE);
				viewHolder.startAnimation(1);
			} else {
//				viewHolder.msg_item_check.setVisibility(View.VISIBLE);
				viewHolder.startAnimation(0);
			}

			viewHolder.msg_item_check
					.setOnCheckedChangeListener(new ItemonCheckeListener(
							position));
			 viewHolder.msg_item_check.setChecked
//
			 (checkEntities.get(position).isCheck());
		}
		return convertView;
	}

	private class ViewHolder {
		// 类型
		TextView tv_title;
		// 时间
		TextView tv_time;
		// 消息
		TextView tv_content;
		// 类型图片
		ImageView img_logo;
		// 选中标记
		CheckBox msg_item_check;
		// 未读标记
		ImageView img_dian;
		// RelativeLayout item_right;
		// LinearLayout msg_item_recheck;
		// TextView msg_item_tvred1;
		//
		// TextView msg_item_tvred2;
		// LinearLayout msg_itm_gone_lin1;
		// LinearLayout msg_itm_gone_lin2;
		 LinearLayout ll_msg_item;
		 LinearLayout ll_msg_content;
		// CheckBox msg_item_check;
		// TextView right_cor;

		public ViewHolder(View view) {
			
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_content = (TextView) view.findViewById(R.id.tv_content);
			img_logo = (ImageView) view.findViewById(R.id.img_logo);
			// 类型
			msg_item_check = (CheckBox) view.findViewById(R.id.msg_item_check);
			img_dian = (ImageView) view.findViewById(R.id.img_dian);
			ll_msg_item = (LinearLayout) view.findViewById(R.id.ll_msg_item);
			ll_msg_content = (LinearLayout) view.findViewById(R.id.ll_mes_content);
			view.setTag(this);
		}

//		// 打开的动画
//		 public void AnimationOpen() {
//		 // 初始化数组
//		 int[] location = new int[2];
//		 msg_item_check.getLocationInWindow(location);
//		 int x = location[0];
//		 int y = location[1];
//		 animationOpen = new TranslateAnimation(y, y, x - 500, x);
//		 animationOpen.setDuration(300);
//		 }

		public void startAnimation(int type) {
			// removeAnimation();

			if (type == 0) {
				final Animation animation1 = AnimationUtils.loadAnimation(
						mContext, R.anim.checkbox_enter);
				final Animation animation2 = AnimationUtils.loadAnimation(
						mContext, R.anim.message_item_enter);
//				ll_msg_item.startAnimation(animation2);
//				msg_item_check.startAnimation(animation1);
				animation1.setFillAfter(true);
				msg_item_check.setVisibility(View.VISIBLE);
				ll_msg_content.startAnimation(animation1);
				animation1.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						
					}
				});
//				right_cor.setVisibility(View.GONE);
			} else if (type == 1) {
				// Toast.makeText(mContext, "haha" + type,
				// Toast.LENGTH_LONG).show();
				final Animation animation1 = AnimationUtils.loadAnimation(
						mContext, R.anim.checkbox_exit);
				final Animation animation2 = AnimationUtils.loadAnimation(
						mContext, R.anim.message_item_exit);
//				right_cor.setVisibility(View.VISIBLE);
				// left_menu_show_bg.startAnimation(animation1);
//				msg_item_check.startAnimation(animation1);
				animation1.setFillAfter(true);
				ll_msg_content.startAnimation(animation1);
//				ll_msg_item.startAnimation(animation2);
				animation1.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						msg_item_check.setVisibility(View.GONE);
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						

					}
				});
			}
		}
	}

	private class ItemonCheckeListener implements OnCheckedChangeListener {
		private int position;

		public ItemonCheckeListener(int position) {
			this.position = position;
		}

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
//			if (isChecked) {
//				check ++;
//			}else {
//				check--;
//			}
			CheckEntityNative mEntity = checkEntities.remove(position);
			mEntity.setCheck(isChecked);
			checkEntities.add(position, mEntity);
			getcheckTrueSize();
		}
	}

	/**
	 * 获取选中的size
	 * 
	 * @return
	 */
	public int getcheckTrueSize() {
		int trueSiez = 0;
		int size = checkEntities.size();
		Log.i("hujian", "sizecheckEntities" + size);
		for (int i = 0; i < size; i++) {
			if (checkEntities.get(i).isCheck()) {
				trueSiez++;
			}
		}
		((MessagerCenterActivity) mContext).setDeletaNum(trueSiez);
		Log.i("hujian", "size" + trueSiez);
		return trueSiez;
	}

	/**
	 * 删除所有选中的
	 */
	public void deletaCheackAll() {
		// this.flag = flag;
		int size = checkEntities.size();
		for (int i = size - 1; i >= 0; i--) {
			CheckEntityNative checkEntity = checkEntities.get(i);
			if (checkEntity.isCheck() == true) {
				((MessagerCenterActivity) mContext).remevePageList(i);
			}
		}
		((MessagerCenterActivity) mContext).Relafresh();
	}



}
