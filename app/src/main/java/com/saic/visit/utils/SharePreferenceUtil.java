package com.saic.visit.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.saic.visit.constant.Constants;

/**
 * 数据存储
 */
public class SharePreferenceUtil {

	public static void save(String key, String value, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG,
				0);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void save(String key, String value, Context mContext,String tag) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag,
				0);
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getStringValue(String key, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG, 
				0);
		return sharedPreferences.getString(key, "");
	}
	
	public static String getStringValue(String key, Context mContext,String tag) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag, 
				0);
		return sharedPreferences.getString(key, "");
	}
	
	public static void saveInt(String key, int value, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG,
				0);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void saveInt(String key, int value, Context mContext,String tag) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag,
				0);
		Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static int getIntValue(String key, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG, 
				0);
		return sharedPreferences.getInt(key, 0);
	}
	
	public static int getIntValue(String key, Context mContext,String tag) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(tag, 
				0);
		return sharedPreferences.getInt(key, 0);
	}
	
	public static long getLongValue(String key, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG, 
				0);
		return sharedPreferences.getLong(key, 0);
	}
	
	public static void saveLong(String key, long value, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG,
				0);
		Editor editor = sharedPreferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}
	public static Boolean getBooleanValue(String key, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG, 
				0);
		return sharedPreferences.getBoolean(key, false);
	}
	
	public static void saveBoolean(String key, boolean value, Context mContext) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.SPTAG,
				0);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
}
