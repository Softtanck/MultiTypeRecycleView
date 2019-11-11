package com.cheweishi.android.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cheweishi.android.R;
import com.cheweishi.android.adapter.DetectionInfoAdapter;
import com.cheweishi.android.entity.CarScanResponse;
import com.cheweishi.android.widget.CustomEditDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 检测详情展示
 *
 * @author mingdasen
 */
public class DetactionInfoActivity extends BaseActivity {
    @ViewInject(R.id.list_detection)
    private ListView listView;
    @ViewInject(R.id.left_action)
    private Button tv_left;
    @ViewInject(R.id.title)
    private TextView tv_title;
    //	@ViewInject(R.id.right_action)
//	private ImageView rigth_action;
    private Context context;
    private DetectionInfoAdapter adapter;
    private CustomEditDialog.Builder builder;
    private CustomEditDialog dialog;
    // private LoginMessage loginMessage;
    // private String uid = "";
    // private String key = "";
    // private String cid = "";
    private String date = "";
//	private AkeytestInfo akeytestInfo;
//	private AkeyTextAllInfo akeyTextAllInfo;// 检测数据
//	private String DTCName = "";// 故障码
//	private String DTCdes = "";// 故障码说明
//	private String status;


    private CarScanResponse response;

    private List<Map<String, Object>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection_info);
        ViewUtils.inject(this);
        context = this;
        tv_title.setText(R.string.test_info);
        Bundle bundle = getIntent().getExtras();
        response = (CarScanResponse) bundle.getSerializable("data");

        if (null != response) {
            perpareData();

            adapter = new DetectionInfoAdapter(context, data);
            listView.setAdapter(adapter);
            tv_left.setText(R.string.back);
        }
//		tv_left.setOnClickListener(onClick);
//		rigth_action.setOnClickListener(onClick);
        // 更改年检、保养状态处理
//		adapter.setOnVIewClickListener(new DetectionInfoAdapter.OnViewClickListener() {
//
//			@Override
//			public void onTextViewClick(int position) {
//				switch (position) {
//				case 17:// 保养状态
//					showCustomEditDialog();
//					break;
//				case 16:// 年检状态
//					showDateView();
//					break;
//				default:
//					break;
//				}
//			}
//		});
        // 获取登录信息
        // loginMessage = LoginMessageUtils.getLoginMessage(context);
        // if (loginMessage != null && loginMessage.getUid() != null) {
        // uid = loginMessage.getUid();
        // key = loginMessage.getKey();
        // cid = loginMessage.getCar().getCid();
        // }
        // 故障码详情跳转
//		listView.setOnItemClickListener(itemClick);
    }

    private void perpareData() {
        data = new ArrayList<>();
        try {

            //totalMileAge
            Map<String, Object> totalMileAge = new HashMap<>();
            totalMileAge.put("data", "总里程(k)  " + response.getMsg().getTotalMileAge());
            data.add(totalMileAge);

            //obdAFR
            Map<String, Object> obdAFR = new HashMap<>();
            obdAFR.put("data", "空燃比系数  " + response.getMsg().getObdAFR());
            data.add(obdAFR);

            //obdCTA
            Map<String, Object> obdCTA = new HashMap<>();
            obdCTA.put("data", "气节门开度  " + response.getMsg().getObdCTA());
            data.add(obdCTA);

            //obdEngLoad
            Map<String, Object> obdEngLoad = new HashMap<>();
            obdEngLoad.put("data", "发动机负荷  " + response.getMsg().getObdEngLoad());
            data.add(obdEngLoad);

            //obdEngRuntime
            Map<String, Object> obdEngRuntime = new HashMap<>();
            obdEngRuntime.put("data", "发动机运行时间  " + response.getMsg().getObdEngRuntime());
            data.add(obdEngRuntime);

            //obdIFC
            Map<String, Object> obdIFC = new HashMap<>();
            obdIFC.put("data", "百公里油耗  " + response.getMsg().getObdIFC());
            data.add(obdIFC);

            //obdRemainingGas
            Map<String, Object> obdRemainingGas = new HashMap<>();
            obdRemainingGas.put("data", "剩余油量  " + response.getMsg().getObdRemainingGas());
            data.add(obdRemainingGas);

            //obdRPM
            Map<String, Object> obdRPM = new HashMap<>();
            obdRPM.put("data", "转速  " + response.getMsg().getObdRPM());
            data.add(obdRPM);

            //obdspeed
            Map<String, Object> obdspeed = new HashMap<>();
            obdspeed.put("data", "车速  " + response.getMsg().getObdspeed());
            data.add(obdspeed);


            //obdTemp
            Map<String, Object> obdTemp = new HashMap<>();
            obdTemp.put("data", "环境温度  " + response.getMsg().getObdTemp());
            data.add(obdTemp);


            //obdWaterTemp
            Map<String, Object> obdWaterTemp = new HashMap<>();
            obdWaterTemp.put("data", "水温  " + response.getMsg().getObdWaterTemp());
            data.add(obdWaterTemp);


            //obdDate
            Map<String, Object> obdDate = new HashMap<>();
            obdDate.put("data", "生成obd记录时间  " + transferLongToDate(response.getMsg().getObdDate()));
            data.add(obdDate);
        } catch (Exception e) {
        }


    }

    /**
     * 把毫秒转化成日期
     *
     * @param millSec(毫秒数)
     * @return
     */
    private String transferLongToDate(Long millSec) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(millSec);
        return sdf.format(date);
    }

    /**
     //	 * 显示更改保养状态的提示框
     //	 */
//	protected void showCustomEditDialog() {
//		builder = new CustomEditDialog.Builder(context);
//		builder.setTitle(context.getResources().getString(
//				R.string.maintenance_state));
//		builder.setMessage(context.getResources().getString(
//				R.string.has_maintenance));
//		builder.setShowEdit1(true);
//		builder.setShowEdit2(true);
//		builder.setShowTv(false);
//		builder.setPositiveButton(
//				context.getResources().getString(R.string.Submit),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						setMaintenance();
//					}
//				});
//		builder.setNegativeButton(
//				context.getResources().getString(R.string.cancel),
//				new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface arg0, int arg1) {
//						dialog.dismiss();
//					}
//				});
//		dialog = builder.create();
//		dialog.show();
//	}
//
//	DateSelectorDialogBuilder.Builder dateBuilder;
//	DateSelectorDialogBuilder dateDialog;

//	/**
//	 * 时间选择
//	 */
//	@SuppressLint("SimpleDateFormat")
//	private void showDateView() {
//		SimpleDateFormat sdf = new SimpleDateFormat(context.getResources()
//				.getString(R.string.year));
//		String year = (Integer.parseInt(sdf.format(System.currentTimeMillis())) + 2)
//				+ "";
//		if (dateBuilder == null) {
//			dateBuilder = new DateSelectorDialogBuilder.Builder(context);
//			dateBuilder.setOnButton1Listener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					if (calculationDate(dateBuilder.getSelectedDate()) < 0) {
//						showToast(R.string.date_choose_fail);
//					} else {
//						date = dateBuilder.getSelectedDate();
//						updateInspectionStatuc(date);
//						dateDialog.dismiss();
//					}
//				}
//			});
//
//			dateBuilder.setOnButton2Listener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					dateDialog.dismiss();
//				}
//			});
//			dateDialog = dateBuilder.create();
//			dateBuilder.setCurrentYear(year);
//		}
//		if (CommonUtils.getTopActivity(context).equals(
//				DetactionInfoActivity.class.getName())) {
//			dateDialog.show();
//		}
//	}

//	/**
//	 * 日期格式设置
//	 * 
//	 * @param day2
//	 * @return
//	 */
//	@SuppressLint("SimpleDateFormat")
//	private long calculationDate(String day2) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date date0;
//		Date date1;
//		long day = 0;
//		try {
//			date0 = sdf.parse(sdf.format(System.currentTimeMillis()));
//			date1 = sdf.parse(day2);
//			Calendar cal0 = Calendar.getInstance();
//			cal0.setTime(date0);
//			Calendar cal1 = Calendar.getInstance();
//			cal1.setTime(date1);
//			long time0 = cal0.getTimeInMillis();
//			long time1 = cal1.getTimeInMillis();
//			day = (time1 - time0) / 86400000;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return day;
//	}

    /**
     //	 * 更新年检状态
     //	 *
     //	 * @param inspect
     //	 */
//	private void updateInspectionStatuc(String inspect) {
//		if (isLogined()) {
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("uid", loginMessage.getUid());
//			params.addBodyParameter("key", loginMessage.getKey());
//			params.addBodyParameter("cid", loginMessage.getCar().getCid());
//			params.addBodyParameter("inspect", inspect);
//			httpBiz = new HttpBiz(context);
//			httpBiz.httPostData(102, API.INSPECTION_STATIC_CHANGES_URL, params,
//					this);
//		} else {
//			startActivity(new Intent(DetactionInfoActivity.this, LoginActivity.class));
//			this.finish();
//		}
//	}
//
//	/**
//	 * 编辑设置保养状态
//	 */
//	private void setMaintenance() {
//		String mileage = builder.getEt1().getText().toString();
//		String m_mile = builder.getEt2().getText().toString();
//		Pattern pattern = Pattern.compile("^[0-9]*$");
//		Matcher isNum1 = pattern.matcher(mileage);
//		Matcher isNum2 = pattern.matcher(m_mile);
//		if (StringUtil.isEmpty(mileage)) {
//			showToast(R.string.mile_not_null);
//		} else if (StringUtil.isEmpty(m_mile)) {
//			showToast(R.string.maintain_mile_not_null);
//		} else if (isNum1.matches() == false || isNum2.matches() == false) {
//			showToast(R.string.only_enter_number);
//		} else if (isNum2.matches() && isNum1.matches()
//				&& StringUtil.getDouble(mileage) <= StringUtil.getDouble(m_mile)) {
//			showToast(R.string.true_enter_mile);
//		} else {
//			if (isLogined()) {
//				dialog.dismiss();
//				updateMaintenanceStatus(mileage, m_mile);
//			} else {
//				dialog.dismiss();
//				startActivity(new Intent(DetactionInfoActivity.this,
//						LoginActivity.class));
//				// showToast(R.string.no_result);
//			}
//		}
//	}
//
//	/***
//	 * 请求后台更新保养状态
//	 * 
//	 * @param mileage
//	 * @param m_mile
//	 */
//	private void updateMaintenanceStatus(String mileage, String m_mile) {
//		ProgrosDialog.openDialog(context);
//		if (isLogined()) {
//			RequestParams params = new RequestParams();
//			params.addBodyParameter("uid", loginMessage.getUid());
//			params.addBodyParameter("key", loginMessage.getKey());
//			params.addBodyParameter("cid", loginMessage.getCar().getCid());
//			params.addBodyParameter("mileage", mileage);
//			params.addBodyParameter("m_mile", m_mile);
//			httpBiz = new HttpBiz(context);
//			httpBiz.httPostData(101, API.MAINTENANCE_STATUS_CHANGES_URL, params,
//					this);
//		}else {
//			startActivity(new Intent(DetactionInfoActivity.this, LoginActivity.class));
//			this.finish();
//		}
//	}
//
//	/**
//	 * 数据解析
//	 */
//	@Override
//	public void receive(int type, String data) {
//		ProgrosDialog.closeProgrosDialog();
//		if (StringUtil.isEmpty(data)) {
//			showToast(R.string.no_result);
//		} else {
//			switch (type) {
//			case 101:// 保养状态
////				parseMaintenanceStatus(data);
//				break;
//			case 102:// 年检状态
//				parseInspectionStatus(data);
//				break;
//			default:
//				break;
//			}
//		}
//	}

    /**
     * 解析年检状态更新数据
     *
     * @param data
     */
    private void parseInspectionStatus(String data) {
//		JSONObject jsonObject;
//		try {
//			jsonObject = new JSONObject(data);
//			status = jsonObject.optString("operationState");
//			if (StringUtil.isEquals(status, "SUCCESS", true)) {
//				showToast(R.string.submit_success);
//				akeyTextAllInfo.getAkeytwo().remove(0);
//				akeytestInfo = new AkeytestInfo("(天)", "距下次年检",
//						calculationDate(date) + "", "0", "2");
//				akeyTextAllInfo.getAkeytwo().add(0, akeytestInfo);
//				adapter.setList(akeyTextAllInfo);
//				adapter.notifyDataSetChanged();
//			} else if (StringUtil.isEquals(status, "FAIL", true)) {
//				showToast(jsonObject.optJSONObject("data").optString("msg"));
//			} else if (StringUtil.isEquals(status, "RELOGIN", true)) {
//				DialogTool.getInstance(context).showConflictDialog();
//			} else if (StringUtil.isEquals(status, "DEFAULT", true)) {
//				showToast(jsonObject.optJSONObject("data").optString("msg"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
    }

    /**
     * 保养状态数据解析
     *
     * @param data
     */
//	private void parseMaintenanceStatus(String data) {
//		JSONObject jsonObject;
//		try {
//			jsonObject = new JSONObject(data);
//			status = jsonObject.optString("operationState");
//			if (StringUtil.isEquals(status, "SUCCESS", true)) {
//				showToast(R.string.submit_success);
//				akeyTextAllInfo.getAkeytwo().remove(1);
//				akeytestInfo = new AkeytestInfo("(km)", "距上次保养", builder
//						.getEt2().getText().toString(), "0", "2");
//				akeyTextAllInfo.getAkeytwo().add(1, akeytestInfo);
//				adapter.setList(akeyTextAllInfo);
//				adapter.notifyDataSetChanged();
//			} else if (StringUtil.isEquals(status, "FAIL", true)) {
//				showToast(jsonObject.optJSONObject("data").optString("msg"));
//			} else if (StringUtil.isEquals(status, "RELOGIN", true)) {
//				DialogTool.getInstance(context).showConflictDialog();
//			} else if (StringUtil.isEquals(status, "DEFAULT", true)) {
//				showToast(jsonObject.optJSONObject("data").optString("msg"));
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		ProgrosDialog.closeProgrosDialog();
//	}

//	OnItemClickListener itemClick = new OnItemClickListener() {
//
//		@Override
//		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
//				long id) {
//			paseDtcValue(position - 19);
//			if (position > 18
//					&& akeyTextAllInfo.getAkeythere().get(0).getValue()
//							.length() > 0 && !StringUtil.isEquals("", DTCName, true)) {
//				Intent intent = new Intent(DetactionInfoActivity.this,
//						DTCDetailsActivity.class);
//				intent.putExtra("name", DTCName);
//				intent.putExtra("des", DTCdes);
//				startActivity(intent);
//			}
//		}
//	};

    /**
     * 故障码处理
     *
     * @param tv_nowvalue
     */
//	private void paseDtcValue(int index) {
//		try {
//			JSONArray jsonArray = new JSONArray(akeyTextAllInfo.getAkeythere()
//					.get(0).getValue());
//			JSONObject object = new JSONObject();
//			object = jsonArray.getJSONObject(index);
//			DTCName = object.getString("name");
//			DTCdes = object.getString("describe");
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//	}

    /**
     * 退出此界面时的数据处理
     */
    private void finishData() {
        finish();
    }

    /**
     * 点击事件
     */
//	OnClickListener onClick = new OnClickListener() {
    @OnClick({R.id.left_action, R.id.right_action})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_action:// 返回
                finishData();
                break;
            case R.id.right_action:// 说明
                Intent intent = new Intent(DetactionInfoActivity.this,
                        DetectionExplainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
//	};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // if (list != null) {
        // list.clear();
        // list = null;
        // }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishData();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
