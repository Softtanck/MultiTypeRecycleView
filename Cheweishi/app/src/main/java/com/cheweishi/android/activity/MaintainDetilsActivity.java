package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.ExpandableListViewAdapter;
import com.cheweishi.android.adapter.ImgAdapter;
import com.cheweishi.android.adapter.WashCarCommentAdapter;
import com.cheweishi.android.entity.MainGridInfoNative;
import com.cheweishi.android.widget.CustomDialog;
import com.cheweishi.android.widget.MyGallery;
import com.cheweishi.android.widget.UnSlidingExpandableListView;
import com.cheweishi.android.widget.UnSlidingListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 保养详情
 * 
 * @author xmh
 * 
 */
public class MaintainDetilsActivity extends BaseActivity {
	@ViewInject(R.id.title)
	private TextView tvTitle;
	@ViewInject(R.id.left_action)
	private Button btnLeft;
	@ViewInject(R.id.tv_maintain_ok)
	private TextView tv_maintain_ok;
	@ViewInject(R.id.mygallery)
	// 滚动广告模块
	private MyGallery mygallery;
	private ImgAdapter imgAdapter;// mygrallery适配器
	private List<Integer> adList;
	private ExpandableListViewAdapter exListAdapter;
	private ArrayList<ImageView> portImg;

	private int preSelImgIndex = 0;

	private Intent intent;
	private CustomDialog.Builder builder;
	private CustomDialog dialog;
	public static MaintainDetilsActivity own;
	/**
	 * ............
	 */
	@ViewInject(R.id.lv_maintain_pinglun)
	private UnSlidingListView lv_maintain_pinglun;
	@ViewInject(R.id.lv_maintain_detils)
	private UnSlidingExpandableListView lv_maintain_detils;// 二级列表
	private ExpandableListViewAdapter mExpandableListViewAdapter;
//	private List<UserCommentNative> comments;
	private WashCarCommentAdapter commentAdapter;

	@ViewInject(R.id.car_tv_car_iv_location)
	private TextView car_tv_car_iv_location;
	@ViewInject(R.id.car_xlocation)
	private TextView car_xlocation;
	@ViewInject(R.id.tv_package_note)
	private TextView tv_package_note;
	@ViewInject(R.id.tv_package)
	private TextView tv_package;
	@ViewInject(R.id.tv_service)
	private TextView tv_service;
	@ViewInject(R.id.car_iv_location)
	private ImageView car_iv_location;

	/**
	 * ...........
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintain_detils);
		ViewUtils.inject(this);
		own = this;

		init();
	}

	private void init() {

		tvTitle.setText("保养详情");
		btnLeft.setText(R.string.back);

//		comments = new ArrayList<UserCommentNative>();
//		for (int i = 0; i < 3; i++) {
//			UserCommentNative comm = new UserCommentNative();
//			comm.setUser_name("x******1");
//			comm.setTime("2015-12-28");
//			comm.setUserMsg("东西很好，物流很快，很满意！！！");
//			comments.add(comm);
//		}
//		commentAdapter = new WashCarCommentAdapter(this, comments);
//		lv_maintain_pinglun.setAdapter(commentAdapter);

		adList = new ArrayList<Integer>();
		for (int i = 0; i < 4; i++) {
			MainGridInfoNative gridInfo = new MainGridInfoNative();
			gridInfo.setImgUrl("asdasdas");
			adList.add(R.drawable.fuwu_bj);
		}

		imgAdapter = new ImgAdapter(MaintainDetilsActivity.this, adList);
		mygallery.setAdapter(imgAdapter);
		mygallery.setFocusable(true);
		mygallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int selIndex, long arg3) {
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// mExpandableListViewAdapter = new ExpandableListViewAdapter(this);
		lv_maintain_detils.setAdapter(mExpandableListViewAdapter);
		// lv_maintain_detils.expandGroup(0);

		lv_maintain_detils
				.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						// TODO Auto-generated method stub
						// "ExpandableListView GroupClickListener groupPosition="
						// + groupPosition);
						return false;
					}
				});
		lv_maintain_detils
				.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						// TODO Auto-generated method stub
						return false;
					}
				});
	}

	@OnClick({ R.id.left_action, R.id.tv_maintain_ok })
	public void finishActivity(View v) {
		switch (v.getId()) {
		case R.id.left_action:
			onBackPressed();
			break;
		case R.id.tv_maintain_ok:
			showPhoneDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 确认预约对话框
	 */
	public void showPhoneDialog() {
		builder = new CustomDialog.Builder(this);
		builder.setMessage(R.string.maintain_ok);
		builder.setPositiveButton(R.string.sure,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						intent = new Intent(MaintainDetilsActivity.this,
								MaintainOrderDetilsActivity.class);
						startActivity(intent);
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog = builder.create();
		dialog.show();
	}





}
