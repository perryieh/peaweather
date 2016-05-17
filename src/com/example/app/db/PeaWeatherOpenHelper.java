package com.example.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PeaWeatherOpenHelper extends SQLiteOpenHelper {
	
	/*
	 * Province���������
	 */
	public static final String CREATE_PROVINCE = "create table Province (" +
			"id integer primary key autoincrement, " +
			"province_name text, " +
			"province_code text)";
	
	/*
	 * City�������
	 */
	public static final String CREATE_CITY = "create table City (" +
			"id integer primary key autoincrement, " +
			"city_name text, " +
			"city_code text, " +
			"provice_id integer";
	/*
	 * County�������
	 */
	public static final String CREATE_County = "create table County(" +
			"id integer primary key autoincrement, " +
			"County_name text, " +
			"County_code text, " +
			"city_id integer)";
	public PeaWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_County);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}