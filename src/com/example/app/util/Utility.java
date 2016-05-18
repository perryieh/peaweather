package com.example.app.util;

import android.text.TextUtils;

import com.example.app.model.City;
import com.example.app.model.County;
import com.example.app.model.PeaWeatherDB;
import com.example.app.model.Province;

public class Utility {
	/*
	 * ����������������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(
			PeaWeatherDB peaWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (allProvinces != null && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					peaWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}

	/*
	 * ����������������ص��м�����
	 */
	public static boolean handleCitiesResponse(PeaWeatherDB peaWeatherDB,
			String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (allCities != null && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					peaWeatherDB.saveCity(city);

				}
				return true;
			}
		}
		return false;
	}

	/*
	 * ����������������ص��ؼ�����.
	 */
	public static boolean handleCountiesResponse(PeaWeatherDB peaWeatherDB,
			String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String [] allCounties = response.split(",");
			if (allCounties != null && allCounties.length > 0) {
				for (String c : allCounties) {
					String [] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					peaWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}
