//package com.cheweishi.android.fragement;
//
//import java.util.ArrayList;
//import java.util.List;
//import org.json.JSONException;
//import org.json.JSONObject;
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.LinearLayout;
//import com.cheweishi.android.R;
//import com.cheweishi.android.activity.AddCarActivity;
//import com.cheweishi.android.activity.CarDynamicActivity;
//import com.cheweishi.android.activity.CarManagerActivity;
//import com.cheweishi.android.activity.CarManagerBindActivity;
//import com.cheweishi.android.adapter.CarManagerAdapter;
//import com.cheweishi.android.biz.HttpBiz;
//import com.cheweishi.android.chat.utils.StringUtil;
//import com.cheweishi.android.config.API;
//import com.cheweishi.android.config.Constant;
//import com.cheweishi.android.dialog.ProgrosDialog;
//import com.cheweishi.android.entity.Car;
//import com.cheweishi.android.entity.CarManager;
//import com.cheweishi.android.tools.DialogTool;
//import com.cheweishi.android.tools.EmptyTools;
//import com.cheweishi.android.tools.LoginMessageUtils;
//import com.cheweishi.android.utils.ButtonUtils;
//import com.cheweishi.android.widget.CustomDialog;
//import com.cheweishi.android.widget.SwipeListView;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.lidroid.xutils.ViewUtils;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.view.annotation.ViewInject;
//import com.lidroid.xutils.view.annotation.event.OnClick;
//import com.lidroid.xutils.view.annotation.event.OnItemClick;
//
///**
// * 车辆管理列表
// * 
// * @author Xiaojin
// * 
// */
//public class CarManagerListFragment extends BaseFragment implements
//		OnClickListener, CarManagerAdapter.onRightItemClickListener,
//		OnItemClickListener {
//	private View view;
//	@ViewInject(R.id.listView_carManager)
//	private SwipeListView listView_carManager;// 滑动列表
//	private CarManagerAdapter adapter;
//	private List<CarManager> listCarManager = new ArrayList<CarManager>();
//	private List<CarManager> listCarManagerTemp;
//	private int page = 1;
//	CarManager carManagerItem = null;
//	private CustomDialog dialog;
//	private Context mContext;
//	@ViewInject(R.id.ll_head)
//	private LinearLayout ll_head;
//	@ViewInject(R.id.listView_front)
//	private LinearLayout listView_front;
//	private int itemIndex;
//	private MyBroadcastReceiver broad;
//
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		view = inflater.inflate(R.layout.fr_list_carmanager, container, false);
//		ViewUtils.inject(this, view);
//		initViews(view);
//		setListeners();
//		return view;
//
//	}
//
//	private void initViews(View view) {
//		mContext = getActivity();
//		adapter = new CarManagerAdapter(mContext, listCarManager,
//				listView_carManager.getRightViewWidth());
//		listView_carManager.setAdapter(adapter);
//		adapter.setOnRightItemClickListener(this);
//		listView_carManager.setSelector(new ColorDrawable(Color.TRANSPARENT));
//
//		httpBiz = new HttpBiz(mContext);
//
//		connectToServer();
//	}
//
//	private void setListeners() {
//		listView_carManager.setOnItemClickListener(this);
//	}
//
//	@OnClick({ R.id.listView_front })
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		if (broad == null) {
//			broad = new MyBroadcastReceiver();
//		}
//
//		IntentFilter intentFilter = new IntentFilter(Constant.REFRESH_FLAG);
//		if (!StringUtil.isEmpty(mContext)) {
//			mContext.registerReceiver(broad, intentFilter);
//		}
//	}
//
//	@Override
//	public void onRightItemClick(View v, int position) {
//		// TODO Auto-generated method stub
//		if (ButtonUtils.isFastClick()) {
//			return;
//		} else {
//			CarManagerActivity.requestFlag = true;
//			if (listCarManager.size() > position) {
//				Intent intent = new Intent(mContext, AddCarActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("car", listCarManager.get(position));
//				intent.putExtras(bundle);
//				startActivity(intent);
//			}
//		}
//	}
//
//	@OnItemClick({ R.id.listView_carManager })
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//		// TODO Auto-generated method stub
//		if (ButtonUtils.isFastClick()) {
//			return;
//		} else {
//			itemIndex = arg2;
//			carManagerItem = listCarManager.get(itemIndex);
//			//if ((carManagerItem.getFeed()) == 0) {
//				ConnectItemToServer(listCarManager.get(itemIndex).getCid());
//			// } else {
//			// // if (StringUtil.isEmpty(listCarManager.get(itemIndex)
//			// // .getDevice())) {
//			// ConnectItemToServer(listCarManager.get(itemIndex).getCid());
//			// // } else {
//			// // Intent pageIntent = new Intent(mContext,
//			// // CarManagerBindActivity.class);
//			// // Bundle bundle = new Bundle();
//			// // bundle.putSerializable("car", carManagerItem);
//			// // pageIntent.putExtra("currentCid", carManagerItem.getCid());
//			// // pageIntent.putExtra("CarManagerBindActivity", 1000);
//			// // pageIntent.putExtras(bundle);
//			// // startActivity(pageIntent);
//			// // }
//			// }
//		}
//	}
//
//	/**
//	 * 请求车辆列表
//	 */
//	private void connectToServer() {
//		if (isLogined()) {
//			ProgrosDialog.openDialog(mContext);
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("uid", loginMessage.getUid());
//			params.addBodyParameter("key", loginMessage.getKey());
//			params.addBodyParameter("page", page + "");
//			httpBiz.httPostData(10001, API.CAR_MANAGER_URL, params, this);
//		}
//	}
//
//	/**
//	 * 请求设置默认车辆服务器
//	 * 
//	 * @param cid
//	 */
//	private void ConnectItemToServer(String cid) {
//		if (isLogined()) {
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("uid", loginMessage.getUid());
//			params.addBodyParameter("key", loginMessage.getKey());
//			params.addBodyParameter("cid", cid);
//			params.addBodyParameter("re_cid", loginMessage.getCar().getCid());
//			httpBiz.httPostData(2, API.SET_DEDAULT_CAR_URL, params, this);
//		}
//	}
//
//	@Override
//	public void receive(int type, String data) {
//		// TODO Auto-generated method stub
//		ProgrosDialog.closeProgrosDialog();
//		switch (type) {
//		case 10001:
//			parseUserDetailJSON(data);
//			break;
//		case 2:
//			parseJson(data);
//			break;
//		case 400:
//			if (listCarManager == null || listCarManager.size() == 0) {
//				EmptyTools.setEmptyView(mContext, listView_carManager);
//			}
//			break;
//		}
//	}
//
//	/**
//	 * 解析车辆列表
//	 * 
//	 * @param result
//	 */
//	private void parseUserDetailJSON(String result) {
//		if (StringUtil.isEmpty(result)) {
//			showToast(R.string.data_fail);
//		} else {
//			try {
//				JSONObject jsonObject = new JSONObject(result);
//				String resultStr = jsonObject.optString("operationState");
//				if (StringUtil.isEquals(resultStr, "SUCCESS", true)) {
//					listCarManagerTemp = new ArrayList<CarManager>();
//					Gson gson = new Gson();
//					java.lang.reflect.Type type = new TypeToken<List<CarManager>>() {
//					}.getType();
//					listCarManagerTemp = gson.fromJson(jsonObject
//							.optJSONObject("data").optString("cars"), type);
//					listCarManager.clear();
//					listCarManager.addAll(listCarManagerTemp);
//					if (listCarManager.size() > 0) {
//						ll_head.setVisibility(View.VISIBLE);
//					} else {
//						EmptyTools.setEmptyView(mContext, listView_carManager);
//					}
//					listView_carManager.hiddenRight(null);
//					listView_carManager.requestLayout();
//					adapter.setData(listCarManager);
//					listView_front.setVisibility(View.INVISIBLE);
//					CarManagerActivity activity = (CarManagerActivity) mContext;
//					if (activity != null) {
//						activity.setListCarManager(listCarManager);
//					}
//				} else if (StringUtil.isEquals(resultStr, "FAIL", true)) {
//					showToast(jsonObject.optJSONObject("data").optString("msg"));
//				} else if (StringUtil.isEquals(resultStr, "RELOGIN", true)) {
//					DialogTool.getInstance(mContext).showConflictDialog();
//				} else if (StringUtil.isEquals(resultStr, "DEFAULT", true)) {
//					showToast(jsonObject.optJSONObject("data").optString("msg"));
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//	}
//
//	private void judgeCurrentRefreahGoBack() {
//
//		Constant.CURRENT_REFRESH = Constant.CAR_MANAGER_REFRESH;
//		Intent mIntent = new Intent();
//		mIntent.setAction(Constant.REFRESH_FLAG);
//		mContext.sendBroadcast(mIntent);
//	}
//
//	/**
//	 * 解析默认车辆
//	 * 
//	 * @param result
//	 */
//	private void parseJson(String result) {
//		if (StringUtil.isEmpty(result)) {
//			showToast(R.string.data_fail);
//		} else {
//			try {
//				JSONObject jsonObject = new JSONObject(result);
//				String resultStr = jsonObject.optString("operationState");
//				if (StringUtil.isEquals(resultStr, "SUCCESS", true)) {
//					judgeCurrentRefreahGoBack();
//					Car car = loginMessage.getCar();
//					if (carManagerItem != null) {
//						if (carManagerItem != null) {
//							car.setBrand(carManagerItem.getBrand());
//							car.setCarId(carManagerItem.getCarId());
//							car.setCid(carManagerItem.getCid());
//							car.setColor(carManagerItem.getColor());
//							car.setDevice(carManagerItem.getDevice());
//							car.setModule(carManagerItem.getModule());
//							car.setPlate(carManagerItem.getPlate());
//							car.setSeries(carManagerItem.getSeries());
//							car.setVinNo(carManagerItem.getVinNo());
//						}
//						loginMessage.setCar(car);
//						LoginMessageUtils.saveProduct(loginMessage, mContext);
//						adapter.notifyDataSetChanged();
//						if (car.getDevice() != null
//								&& (!car.getDevice().equals(""))) {
//							if (CarManagerActivity.washCarFlag == false) {
//								Intent intent = new Intent(mContext,
//										CarDynamicActivity.class);
//								((Activity) mContext).startActivityForResult(
//										intent, 2000);
//							} else {
//								goToWash();
//							}
//						} else {
//							goToWash();
//						}
//					}
//				} else if (StringUtil.isEquals(resultStr, "FAIL", true)) {
//					showToast(jsonObject.optJSONObject("data").optString("msg"));
//				} else if (StringUtil.isEquals(resultStr, "RELOGIN", true)) {
//					DialogTool.getInstance(mContext).showConflictDialog();
//				} else if (StringUtil.isEquals(resultStr, "DEFAULT", true)) {
//					showToast(jsonObject.optJSONObject("data").optString("msg"));
//				}
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//
//	}
//
//	private void goToWash() {
//		if (CarManagerActivity.washCarFlag == true) {
//			Constant.CURRENT_REFRESH = Constant.CAR_MANAGER_REFRESH;
//			Intent mIntent = new Intent();
//			mIntent.setAction(Constant.REFRESH_FLAG);
//			mContext.sendBroadcast(mIntent);
//			((Activity) mContext).finish();
//		}
//	}
//
//	/**
//	 * 重新链接
//	 */
//	public void reconnect() {
//		if (CarManagerActivity.dialogFlag == true) {
//			if (dialog != null) {
//				dialog.dismiss();
//			}
//		}
//		listView_front.setVisibility(View.VISIBLE);
//		if (listCarManagerTemp != null) {
//			listCarManagerTemp.clear();
//		}
//		page = 1;
//		connectToServer();
//	}
//	
//	public void setListVisible(boolean flag) {
//		if(flag == false) {
//			listView_carManager.setVisibility(View.GONE);
//			//EmptyTools.setEmptyView1(mContext, listView_carManager);
//		} else {
//			//EmptyTools.setEmptyView(mContext, listView_carManager);
//			listView_carManager.setVisibility(View.VISIBLE);
//		}
//	}
//
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data);
//		switch (requestCode) {
//		case 1001:
//			setListeners();
//			connectToServer();
//			listView_carManager
//					.setSelector(new ColorDrawable(Color.TRANSPARENT));
//			break;
//		case 2000:
//			adapter.setData(listCarManager);
//			break;
//		}
//	}
//
//	@Override
//	public void onDestroy() {
//		// TODO Auto-generated method stub
//		super.onDestroy();
//		if (listCarManager != null) {
//			listCarManager.clear();
//		}
//		if (listCarManagerTemp != null) {
//			listCarManagerTemp.clear();
//		}
//		mContext.unregisterReceiver(broad);
//	}
//
//	public class MyBroadcastReceiver extends BroadcastReceiver {
//
//		public void onReceive(Context context, Intent intent) {
//					+ Constant.CURRENT_REFRESH);
//			if (StringUtil.isEquals(Constant.CURRENT_REFRESH,
//					Constant.CAR_MANAGER_REFRESH, true)) {
//				reconnect();
//
//			}
//		}
//	}
//}