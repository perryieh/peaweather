package com.example.app.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.app.model.City;
import com.example.app.model.County;
import com.example.app.model.PeaWeatherDB;
import com.example.app.model.Province;
import com.example.app.util.HttpCallbackListener;
import com.example.app.util.HttpUtil;
import com.example.app.util.Utility;


import com.example.app.R;
import android.app.Activity;
import android.app.DownloadManager.Query;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;

import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;
	protected static final String TAG = "ChooseAreaActivity";
	
	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private PeaWeatherDB peaWeatherDB;
	private List<String> dataList = new ArrayList<String>();
	/*
	 * ʡ�б�
	 */
	private List<Province> provinceList;
	/*
	 * ���б�
	 */
	private List<City> cityList;
	/*
	 * ���б�
	 */
	private List<County> countyList;
	/*
	 * ѡ�е�ʡ��
	 */
	private Province selectProvince;
	/*
	 * ѡ�еĳ���
	 */
	private City selectCity;
	/*
	 * ��ǰѡ�еļ���
	 */
	private int currentLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		listView = (ListView) findViewById(R.id.list_view);
		titleText = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		peaWeatherDB = PeaWeatherDB.getInStanceDb(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (currentLevel == LEVEL_PROVINCE) {
					selectProvince = provinceList.get(position);
					queryCities();
					
				} else if (currentLevel == LEVEL_CITY) {
					selectCity = cityList.get(position);
					queryCounties();
				}
			}
		});
		queryProvinces();
	}
	/*
	 * ��ѯȫ�����е�ʡ�����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ
	 */
	private void queryProvinces () {
		provinceList = peaWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province province : provinceList) {
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			currentLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer (null, "province");
		}
	}
	
	/*
	 * ��ѯѡ��ʡ�����е��У����ȴ����ݿ��в�ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryCities () {
		cityList = peaWeatherDB.loadCities(selectProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		} else {
			queryFromServer (selectProvince.getProvinceCode(), "city");
			//Log.d(TAG, selectProvince.getProvinceName() + "----<<<");
		}
	}
	/*
	 * ��ѯѡ���������е��أ����ȴ����ݿ��ѯ�����û�в�ѯ����ȥ�������ϲ�ѯ��
	 */
	private void queryCounties () {
		countyList = peaWeatherDB.loadCounties(selectCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectCity.getCityName());
			currentLevel = LEVEL_COUNTY;
		} else {
			queryFromServer (selectCity.getCityCode(), "county");
		}
	}
	/*
	 * ���ݴ���Ĵ��ź����ʹӷ������ϲ�ѯʡ�������ݡ�
	 */
	private void queryFromServer(final String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(peaWeatherDB, response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(peaWeatherDB, response, selectProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(peaWeatherDB, response, selectCity.getId());
				}
				if (result) {
					//ͨ��runOnUiThread()�����ص����̴߳����߼�
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals("type")) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				//ͨ��runonuithread()�����ص����̴߳����߼�
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��", Toast.LENGTH_LONG).show();
					}
				});
			}
		});
	}
	/*
	 * ��ʾ���ȶԻ���.
	 */
	private void showProgressDialog() {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	/*
	 * �رս��ȶԻ���
	 */
	private void closeProgressDialog () {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}
	/*
	 * ����Back���������ݵ�ǰ�ļ������жϣ���ʱӦ�÷������б�ʡ�б�����ֱ���˳���
	 */
	@Override
	public void onBackPressed() {
		if (currentLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (currentLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}
}
