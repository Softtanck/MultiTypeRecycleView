package com.cheweishi.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.BaseActivity;
import com.cheweishi.android.activity.LoginActivity;
import com.cheweishi.android.activity.MainNewActivity;
import com.cheweishi.android.activity.WelcomeActivity;
import com.cheweishi.android.tools.LoginMessageUtils;
import com.cheweishi.android.utils.StringUtil;

public class WelcomeAdapter extends BaseAdapter {

	private Context _context;
	private List<Integer> imgList;

	public WelcomeAdapter(Context context, List<Integer> imgList) {
		_context = context;
		this.imgList = imgList;
	}

	public int getCount() {
		return imgList == null ? 0 : imgList.size();
	}

	public Object getItem(int position) {

		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(imgList.get(position % imgList.size()),
				null);
		if (position == 3) {
			ImageView immediateExperience = (ImageView) convertView
					.findViewById(R.id.immediateExperienceBtn);
			immediateExperience.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
//					Intent i = new Intent(_context, MainNewActivity.class);
//					_context.startActivity(i);
//					WelcomeActivity.instance.finish();

					if (!(StringUtil.isEmpty(BaseActivity.loginResponse) || StringUtil
							.isEmpty(BaseActivity.loginResponse.getId()))) {
						Intent intent = new Intent(_context,
								MainNewActivity.class);
						intent.putExtra("className", "WelcomeActivity");
						_context.startActivity(intent);
						((WelcomeActivity)_context).finish();
					} else {
						Intent intent = new Intent(_context,
								LoginActivity.class);
						intent.putExtra("className", "WelcomeActivity");
						LoginMessageUtils.showDialogFlag = true;
						_context.startActivity(intent);
						((WelcomeActivity)_context).finish();
					}
				}

			});
		}
		return convertView;
	}
}
