package com.example.app.model;

public class County {
	private int id;
	private String CountyName;
	private String CountyCode;
	private int cityId;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCountyName() {
		return CountyName;
	}
	public void setCountyName(String CountyName) {
		this.CountyName = CountyName;
	}
	public String getCountyCode() {
		return CountyCode;
	}
	public void setCountyCode(String CountyCode) {
		this.CountyCode = CountyCode;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	
	
}
