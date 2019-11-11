package com.cheweishi.android.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.cheweishi.android.R;
import com.cheweishi.android.adapter.FindCarSearchAdapter;
import com.cheweishi.android.utils.DateUtils;
import com.cheweishi.android.utils.MyMapUtils;
import com.cheweishi.android.utils.StringUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 地图搜索界面
 * 
 * @author mingdasen
 * 
 */
@SuppressWarnings("ResourceType")
@ContentView(R.layout.activity_findcar_search)
public class SearchActivity extends BaseActivity {
	@ViewInject(R.id.tv_cancel)
	private TextView tv_cancle;// 取消

	@ViewInject(R.id.ed_search)
	private EditText edSearch;// 搜索

	@ViewInject(R.id.tv_location)
	private TextView tv_location;// 当前位置

	@ViewInject(R.id.seach_listview)
	private ListView mListView;// 数据记录

	private String searchHint;// 搜索框提示

	private Drawable delete_black;
	private Drawable delete_blue;
	private TextView tvFoot;
	private int colorBlue;
	private int colorBlack;
	// private TextView tvHead;

	private String lon = "";// 经度
	private String lat = "";// 纬度
	private LinearLayout layoutFoot;
	private LatLng selectLatLng;
	private boolean hasHead;
	private FindCarSearchAdapter searchAdapter;
	private ArrayList<HashMap<String, String>> datas;
	private ArrayList<HashMap<String, String>> historyDatas;
	private String type = "SOS";

	/**
	 * 标志通过输入键“DONE”发起的搜索
	 */
	private boolean FLAG_DONE = false;
	private GeoCoder coder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);

		initView();

	}

	private void initView() {
		initSearchPart();
		setSearchHint();
	}

	/**
	 * 设置搜索提示
	 */
	private void setSearchHint() {
		searchHint = getIntent().getStringExtra("hint");
		type = getIntent().getStringExtra("type");
		tv_location.setText(MyMapUtils.getAddress(this));
		if (!StringUtil.isEmpty(searchHint)) {
			edSearch.setHint(searchHint);
		} else {
			edSearch.setHint(R.string.search_hint);
		}
		edSearch.requestFocus();

		if (!hasHead && !StringUtil.isEquals(type, "SOS", true)) {
			hasHead = true;
			// mListView.addHeaderView(tvHead);
			mListView.addFooterView(layoutFoot);
		}
		if (!StringUtil.isEquals(type, "SOS", true)) {

			getLocalDatas();

			if (historyDatas != null && historyDatas.size() != 0) {
				setFooterViewColor(colorBlue, delete_blue);
				layoutFoot.setClickable(false);
			} else {
				setFooterViewColor(colorBlack, delete_black);
				layoutFoot.setClickable(true);
			}
			searchAdapter = new FindCarSearchAdapter(this, historyDatas);
			// Log.i("result", "====mListView==" + mListView +
			// "==searchAdapter===" + searchAdapter);
			mListView.setAdapter(searchAdapter);
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.showSoftInput(edSearch, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * 搜索相关设置
	 */
	private void initSearchPart() {
		edSearch.setOnEditorActionListener(onEditorActionListener);
		edSearch.addTextChangedListener(watcher);

		mListView.setOnItemClickListener(onItemClickListener);
		int px10 = DateUtils.dip2Px(this, 10);
		int px5 = DateUtils.dip2Px(this, 5);
		int textSize = 17;

		// tvHead = new TextView(this);
		// tvHead.setText(R.string.lately_search);
		// tvHead.setId(4000);
		// tvHead.setTextSize(textSize);
		// tvHead.setBackgroundColor(getResources().getColor(
		// R.color.gray_backgroud));
		// tvHead.setPadding(px10, px5, px10, px5);

		layoutFoot = new LinearLayout(this);
		layoutFoot.setOrientation(LinearLayout.HORIZONTAL);
		layoutFoot.setId(5000);
		// favFoot = new FontAwesomeView(this);
		// favFoot.setText(getString(R.string.icon_delete));
		// favFoot.setTextSize(13);
		delete_black = getResources().getDrawable(R.drawable.message_icon2x);
		delete_blue = getResources().getDrawable(R.drawable.sousuo_icon);
		colorBlue = getResources().getColor(R.color.orange);
		colorBlack = getResources().getColor(R.color.text_black_colcor);
		tvFoot = new TextView(this);
		tvFoot.setText(R.string.clear_history);
		tvFoot.setTextSize(textSize);
		tvFoot.setCompoundDrawables(delete_blue, null, null, null);
		tvFoot.setCompoundDrawablePadding(px5);
		// tvFoot.setPadding(px5, 0, 0, 0);
		tvFoot.setTextColor(colorBlue);

		// layoutFoot.addView(favFoot);
		layoutFoot.addView(tvFoot);
		layoutFoot.setPadding(px10, px10, px10, px10);
		layoutFoot.setGravity(Gravity.CENTER);

		coder = GeoCoder.newInstance();
		coder.setOnGetGeoCodeResultListener(onGetGeoCodeResultListener);
	}

	private void setFooterViewColor(int color, Drawable drawable) {
		tvFoot.setTextColor(color);
		tvFoot.setCompoundDrawables(drawable, null, null, null);
		// favFoot.setTextColor(color);
	}

	OnEditorActionListener onEditorActionListener = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_DONE) {
				String poiStr = edSearch.getText() == null ? null : edSearch
						.getText().toString();
				FLAG_DONE = true;
				getSugLocation(poiStr);
				return true;
			}
			return false;
		}
	};

	/**
	 * 发起建议点搜索
	 * 
	 * @param s
	 */
	private void getSugLocation(String s) {
		String sCity = MyMapUtils.getCity(SearchActivity.this);
		if (sCity == null) {
			return;
		}
		SuggestionSearch mSearch = SuggestionSearch.newInstance();
		mSearch.setOnGetSuggestionResultListener(sugResultListener);
		SuggestionSearchOption option = new SuggestionSearchOption()
				.city(sCity).keyword(s.toString());
		mSearch.requestSuggestion(option);
	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			HashMap<String, String> items = (HashMap<String, String>) parent
					.getItemAtPosition(position);

			if (items != null) {
				// poiStr = items.get("key");
				// title_icon.setVisibility(View.VISIBLE);
				// tv_title.setText(poiStr);

				lat = items.get("latitude");
				lon = items.get("longitude");
				String keyword = items.get("key");
				String district = items.get("district");
				String city = items.get("city");
				Log.i("result", "===city===" + city);
				Log.i("result", "===district===" + district);
				Log.i("result", "===keyword===" + keyword);
				Log.i("result", "===lon===" + lon);
				Log.i("result", "===lat===" + lat);
				selectLatLng = new LatLng(StringUtil.getDouble(lat),
						StringUtil.getDouble(lon));
				ReverseGeoCodeOption options = new ReverseGeoCodeOption();
				options.location(selectLatLng);
				coder.reverseGeoCode(options);

				if (historyDatas == null) {
					historyDatas = new ArrayList<HashMap<String, String>>();
				}
				historyDatas.add(items);
				saveLocalDatas();
				Intent intent = new Intent();
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				intent.putExtra("keyword", keyword);
				intent.putExtra("district", district);
				intent.putExtra("city", city);
				setResult(RESULT_OK, intent);
				finish();
				// stutic = 0;
				// showOriginal();
			} else if (position != 0) {
				// 清除历史记录
				historyDatas = null;
				searchAdapter.clearData();
				saveLocalDatas();

				setFooterViewColor(colorBlack, delete_black);
				layoutFoot.setClickable(true);
			}

		}
	};

	/**
	 * get local datas
	 * 
	 * @return
	 */
	private void getLocalDatas() {
		// TODO get local datas
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return;
		}
		File file = Environment.getExternalStorageDirectory();
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, "cheweishi/FindCarSearch.txt");
		FileReader fr = null;
		try {
			fr = new FileReader(file);
			char[] buffer = new char[1024];
			StringBuilder sb = new StringBuilder();
			while (fr.read(buffer, 0, buffer.length) > 0) {
				sb.append(buffer);
			}
			String json = sb.toString();
			JSONArray jsonArray = new JSONArray(json);
			historyDatas = new ArrayList<HashMap<String, String>>();
			for (int i = jsonArray.length() - 1; i >= 0; i--) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("city", jsonObject.getString("city"));
				map.put("district", jsonObject.getString("district"));
				map.put("key", jsonObject.getString("key"));
				map.put("latitude", jsonObject.getString("latitude"));
				map.put("longitude", jsonObject.getString("longitude"));
				historyDatas.add(map);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * save datas to local
	 * 
	 * @return
	 */
	private void saveLocalDatas() {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return;
		}
		File file = new File(Environment.getExternalStorageDirectory(),
				"cheweishi");
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(file, "FindCarSearch.txt");

		FileOutputStream fos = null;
		PrintStream ps = null;
		try {
			fos = new FileOutputStream(file);
			ps = new PrintStream(fos);

			Gson gson = new Gson();
			String json = gson.toJson(historyDatas, ArrayList.class);
			ps.println(json);
		} catch (FileNotFoundException e) {
		} finally {
			if (ps != null) {
				ps.flush();
				ps.close();
			}
		}
	}

	// geo搜索结果
	OnGetGeoCoderResultListener onGetGeoCodeResultListener = new OnGetGeoCoderResultListener() {

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				return;
			}
//			String province = result.getAddressDetail().province;
			// if (getText(R.string.chongqing).equals(province)) {
			// type = "cq";
			// } else {
			// type = "qt";
			// }
			// getParkData();
		}

		@Override
		public void onGetGeoCodeResult(GeoCodeResult result) {

		}
	};

	private TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s == null) {
				return;
			}
			getSugLocation(s.toString());
		}
	};

	/**
	 * 建议搜索监听
	 */
	private OnGetSuggestionResultListener sugResultListener = new OnGetSuggestionResultListener() {

		@Override
		public void onGetSuggestionResult(SuggestionResult result) {
			if (result == null) {
				return;
			}
			List<SuggestionInfo> list = result.getAllSuggestions();
			datas = new ArrayList<HashMap<String, String>>();
			if (list == null || list.size() == 0) {
				return;
			}

			if (FLAG_DONE) {
				FLAG_DONE = false;
				dealInfoAfterGo(list);
				return;
			}
			for (SuggestionInfo info : list) {
				// 过滤出有用数据
				if (info.pt != null) {
					HashMap<String, String> items = new HashMap<String, String>();
					items.put("city", info.city);
					items.put("district", info.district);
					items.put("key", info.key);
					items.put("latitude", info.pt.latitude + "");
					items.put("longitude", info.pt.longitude + "");
					datas.add(items);
				}
			}
			searchAdapter = new FindCarSearchAdapter(SearchActivity.this, datas);

			if (hasHead) {
				hasHead = false;
				// mListView.removeHeaderView(tvHead);
				mListView.removeFooterView(layoutFoot);
			}
			mListView.setAdapter(searchAdapter);
		}
	};

	/**
	 * 按下“go”，接收到数据后的操作
	 * 
	 * @param list
	 */
	private void dealInfoAfterGo(List<SuggestionInfo> list) {
		SuggestionInfo info = null;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			info = list.get(i);
			if (info != null && info.pt != null) {
				break;
			}

		}
		if (info == null || info.pt == null) {
			return;
		}

		// poiStr = info.key;
		// title_icon.setVisibility(View.VISIBLE);
		// tv_title.setText(poiStr);

		lat = info.pt.latitude + "";
		lon = info.pt.longitude + "";
		selectLatLng = new LatLng(info.pt.latitude, info.pt.longitude);
		ReverseGeoCodeOption options = new ReverseGeoCodeOption();
		options.location(selectLatLng);
		coder.reverseGeoCode(options);

		if (historyDatas == null) {
			historyDatas = new ArrayList<HashMap<String, String>>();
			return;
		}
		HashMap<String, String> items = new HashMap<String, String>();
		items.put("city", info.city);
		items.put("district", info.district);
		items.put("key", info.key);
		items.put("latitude", info.pt.latitude + "");
		items.put("longitude", info.pt.longitude + "");
		historyDatas.add(items);
		saveLocalDatas();
		// stutic = 0;
		// showOriginal();
	}

	@OnClick({ R.id.tv_cancel })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_cancel:
			finish();
			break;

		default:
			break;
		}
	}

}
