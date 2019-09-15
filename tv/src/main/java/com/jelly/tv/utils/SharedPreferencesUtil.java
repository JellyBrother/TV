package com.jelly.tv.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

/**
 * SharedPreferences工具类，用于写入和读取键值对
 */
public class SharedPreferencesUtil {
	public static final String shareFile = "ICBaseCommon";

	public static final String shareFileInMulitProcess = "ICBaseCommon_MulitProcess";

	public static boolean getBoolean(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		return settings.getBoolean(key, false);
	}

	public static boolean putBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putBoolean(key, value);
		return edit.commit();
	}

	/**
	 * 读取String类型值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getString(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		return settings.getString(key, "");
	}

	public static boolean putString(Context context, String key, String value) {
		if (context == null)
			return false;
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putString(key, value);
		boolean commit = edit.commit();
		return commit;
	}


	/**
	 * SharedPreferences 记录集一次性保存，提高存储效率
	 * @param context
	 * @param valueMap
	 * @return
	 */
	public static boolean putValueMap(Context context, Map<String, Object> valueMap){
		if (context == null)
			return false;
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		Editor edit = settings.edit();

		for(String key : valueMap.keySet()){
			Object value = valueMap.get(key);
			if(value instanceof Boolean){
				edit.putBoolean(key, (Boolean) value);
			}else if(value instanceof String){
				edit.putString(key,(String)value);
			}else if(value instanceof Integer){
				edit.putInt(key, (Integer)value);
			}else if(value instanceof Long){
				edit.putLong(key, (Long)value);
			}
		}

		boolean commit = edit.commit();
		valueMap.clear(); //清空，避免重复存储
		return commit;

	}

	/**
	 * 读取String类型值, 不同进程同步的模式
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getMulitString(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(
				shareFileInMulitProcess, Context.MODE_MULTI_PROCESS);
		return settings.getString(key, "");
	}

	public static boolean putMulitString(Context context, String key,
										 String value) {
		SharedPreferences settings = context.getSharedPreferences(
				shareFileInMulitProcess, Context.MODE_MULTI_PROCESS);
		Editor edit = settings.edit();
		edit.putString(key, value);
		boolean commit = edit.commit();
		return commit;
	}

	/**
	 * 读取Long类型值
	 */
	public static long getLong(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		return settings.getLong(key, -1);
	}

	public static boolean putLong(Context context, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putLong(key, value);
		return edit.commit();
	}

	/**
	 * 读取Int类型值
	 */
	public static int getInt(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		return settings.getInt(key, -1);
	}

	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	public static boolean putInt(Context context, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(shareFile,
				Context.MODE_PRIVATE);
		Editor edit = settings.edit();
		edit.putInt(key, value);
		return edit.commit();
	}

}
