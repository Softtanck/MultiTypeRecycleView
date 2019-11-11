package com.cheweishi.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.activity.OrderDetailsActivity;
import com.cheweishi.android.entity.MainSellerInfoNative;
import com.cheweishi.android.entity.MainSellerServiceInfoNative;
import com.cheweishi.android.utils.StringUtil;

import java.util.List;

/**
 * 新版首页商家服务列表适配器
 * 
 * @author mingdasen
 * 
 */
public class MiantainSellerServiceAdapater extends BaseAdapter implements
		OnClickListener {

	private Context mContext;
	private List<MainSellerServiceInfoNative> list;
	private MainSellerInfoNative mainSellerInfo;

	public MiantainSellerServiceAdapater(Context mContext,
			MainSellerInfoNative mainSellerInfo) {
		this.mContext = mContext;
		this.mainSellerInfo = mainSellerInfo;
		this.list = mainSellerInfo.getServices();
	}

	@Override
	public int getCount() {
		return StringUtil.isEmpty(list) ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_main_seller_service, null);
			holder.tv_service_name = (TextView) convertView
					.findViewById(R.id.tv_service_name);
			holder.tv_service_fPrice_name = (TextView) convertView
					.findViewById(R.id.tv_service_fPrice_name);
			holder.tv_service_fPrice = (TextView) convertView
					.findViewById(R.id.tv_service_fPrice);
			// holder.tv_service_price = (TextView) convertView
			// .findViewById(R.id.tv_service_price);
			holder.tv_red_packets = (TextView) convertView
					.findViewById(R.id.tv_time);
			holder.btn_pay = (TextView) convertView.findViewById(R.id.btn_pay);
			holder.btn_pay.setTag(position);
			holder.btn_pay.setOnClickListener(this);
			// holder.btn_pay.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// if (mListener != null) {
			// mListener.onBtnClick(v, position);
			// }
			// }
			// // TODO Auto-generated method stub
			// Intent intent = new Intent(mContext,
			// WashCarPayActivity.class);
			// intent.putExtra("seller", mainSellerInfo.getName());
			// intent.putExtra("service", list.get(position).getName());
			// if (StringUtil.isEmpty(list.get(position).getfPrice())
			// || StringUtil.isEquals("null", list.get(position)
			// .getfPrice(), true)) {
			// intent.putExtra("price", list.get(position).getPrice());
			// } else {
			// intent.putExtra("price", list.get(position).getfPrice());
			// }
			// if (list.get(position).getName().contains("普洗")) {
			// intent.putExtra("type", "px");
			// } else {
			// intent.putExtra("type", "");
			// }
			// mContext.startActivity(intent);
			// }
			// });
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
			holder.btn_pay.setTag(position);
		}

		if (!StringUtil.isEmpty(list) && list.size() > position) {
			holder.tv_service_name.setText(list.get(position).getName());
			holder.tv_service_fPrice_name.setText(list.get(position)
					.getDescribe());
			holder.tv_service_fPrice.setText("￥"
					+ list.get(position).getfPrice());
			if (StringUtil.isEquals("0", list.get(position).getIsRed(), true)) {
				holder.tv_red_packets.setVisibility(View.VISIBLE);
			} else {
				holder.tv_red_packets.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	class ViewHolder {
		private TextView tv_service_name;
		private TextView tv_service_fPrice_name;
		private TextView tv_service_fPrice;
		private TextView tv_service_price;
		private TextView btn_pay;
		private TextView tv_red_packets;
	}

	// public interface OnBtnClickListener {
	// void onBtnClick(View v,int position);
	// }
	// /**
	// * 单击事件监听器
	// */
	// private OnBtnClickListener mListener = null;
	//
	// public void setOnBtnClickListener(OnBtnClickListener listener) {
	// mListener = listener;
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_pay:
			Intent intent = new Intent(mContext, OrderDetailsActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString("goods_id", list.get((Integer) v.getTag()).getId());
			bundle.putString("store_id", mainSellerInfo.getId());
			bundle.putString("price", list.get((Integer)v.getTag()).getfPrice());
//			if (type.get(groupPosition).getGoodsList().get(childPosition)
//					.getIs_discount_price().equals("0")) {
//				bundle.putString("price", type.get(groupPosition)
//						.getGoodsList().get(childPosition).getPrice());
//			} else {
//				bundle.putString("price", type.get(groupPosition)
//						.getGoodsList().get(childPosition).getDiscount_price());
//			}
			intent.putExtra("bundle", bundle);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}
}
