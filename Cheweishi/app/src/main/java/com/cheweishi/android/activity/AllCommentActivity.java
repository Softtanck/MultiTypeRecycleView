package com.cheweishi.android.activity;

import java.util.ArrayList;
import java.util.List;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.AllCommentAdapter;
import com.cheweishi.android.entity.UserCommentNative;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AllCommentActivity extends BaseActivity{

	@ViewInject(R.id.left_action)
	private Button left_action;
	@ViewInject(R.id.title)
	private TextView title;
	@ViewInject(R.id.lv_all_comment)
	private ListView lv_all_comment;
	private List<UserCommentNative> comments;
	private AllCommentAdapter commentAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_comment);
		ViewUtils.inject(this);
		
		
		init();
	}
	private void init() {
		title.setText("全部评论");
		left_action.setText(R.string.back);
		
		comments = new ArrayList<UserCommentNative>();
		for (int i = 0; i < 8; i++) {
			UserCommentNative comm = new UserCommentNative();
			comm.setUser_name("x******1");
			comm.setTime("2015-12-28");
			comm.setUserMsg("东西很好，物流很快，很满意！！！");
			comments.add(comm);
		}
		commentAdapter = new AllCommentAdapter(this,comments);
		lv_all_comment.setAdapter(commentAdapter);
	}
	@OnClick({ R.id.left_action,R.id.tv_maintain_ok})
	public void finishActivity(View v) {
		switch (v.getId()) {
		case R.id.left_action:
			onBackPressed();
			break;
		default:
			break;
		}
	}
}
