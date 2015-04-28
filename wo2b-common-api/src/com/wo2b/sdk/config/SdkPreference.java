package com.wo2b.sdk.config;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.wo2b.sdk.common.util.DateUtils;
import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.core.RockySdk;

/**
 * 偏好设置工具类
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-6
 */
public class SdkPreference
{

	private static SharedPreferences mTempPref;
	private static SharedPreferences mSystemPref;

	/**
	 * 私有构造函数
	 * 
	 * @param application
	 */
	protected SdkPreference()
	{

	}

	/**
	 * 
	 * @return
	 */
	public static SharedPreferences getTemp()
	{
		if (mTempPref == null)
		{
			mTempPref = RockySdk.getInstance().getContext().getSharedPreferences("com.wo2b.temp", Context.MODE_PRIVATE);
		}

		return mTempPref;
	}

	/**
	 * 
	 * @return
	 */
	public static SharedPreferences getPrefSystem()
	{
		if (mSystemPref == null)
		{
			mSystemPref = PreferenceManager.getDefaultSharedPreferences(RockySdk.getInstance().getContext());
		}

		return mSystemPref;
	}
	
	
	// -------------------------------- System Preference --------------------------------
	public static int getInt(String key, int defValue)
	{
		return getPrefSystem().getInt(key, defValue);
	}

	public static long getLong(String key, long defValue)
	{
		return getPrefSystem().getLong(key, defValue);
	}

	public static float getFloat(String key, float defValue)
	{
		return getPrefSystem().getFloat(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue)
	{
		return getPrefSystem().getBoolean(key, defValue);
	}

	public static String getString(String key, String defValue)
	{
		return getPrefSystem().getString(key, defValue);
	}

	public static boolean putInt(String key, int value)
	{
		Editor editor = edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static boolean putLong(String key, long value)
	{
		Editor editor = edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static boolean putFloat(String key, float value)
	{
		Editor editor = edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public static boolean putBoolean(String key, boolean value)
	{
		Editor editor = edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static boolean putString(String key, String value)
	{
		Editor editor = edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static boolean remove(String key)
	{
		Editor editor = edit();
		editor.remove(key);
		return editor.commit();
	}

	public static Editor edit()
	{
		return getPrefSystem().edit();
	}

	// -------------------------------- System Preference --------------------------------
    
	
	// -------------------------------- Temp Preference --------------------------------
	public static int getIntTemp(String key, int defValue)
	{
		return getTemp().getInt(key, defValue);
	}

	public static long getLongTemp(String key, long defValue)
	{
		return getTemp().getLong(key, defValue);
	}

	public static float getFloatTemp(String key, float defValue)
	{
		return getTemp().getFloat(key, defValue);
	}

	public static boolean getBooleanTemp(String key, boolean defValue)
	{
		return getTemp().getBoolean(key, defValue);
	}

	public static boolean putIntTemp(String key, int value)
	{
		Editor editor = editTemp();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static boolean putLongTemp(String key, long value)
	{
		Editor editor = editTemp();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static boolean putFloatTemp(String key, float value)
	{
		Editor editor = editTemp();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public static boolean putBooleanTemp(String key, boolean value)
	{
		Editor editor = editTemp();
		editor.putBoolean(key, value);
		return editor.commit();
	}
	
	public static boolean putStringTemp(String key, String value)
	{
		Editor editor = editTemp();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public static String getStringTemp(String key, String defValue)
	{
		return getTemp().getString(key, defValue);
	}

	public static boolean removeTemp(String key)
	{
		Editor editor = editTemp();
		editor.remove(key);
		return editor.commit();
	}

	public static Editor editTemp()
	{
		return getTemp().edit();
	}
	// -------------------------------- Temp Preference --------------------------------
	
	/**
	 * 根据资源Id返回字符串
	 * 
	 * @param resId
	 * @return
	 */
	public String getKeyString(int resId)
	{
		return RockySdk.getInstance().getContext().getResources().getString(resId);
	}
	
	
	// -------------------------------- High Frequency --------------------------------
	/** 应用版本 */
	public static final String PREF_APP_VERSION = "rocky_app_version";
	/** 统计使用次数, 总数 */
	public static final String PREF_USE_TOTAL_COUNT = "rocky_use_total_count";
	/** 统计使用次数, 每天只统计一次 */
	public static final String PREF_USE_DAY_COUNT = "rocky_use_day_count";
	/** 统计使用次数的日期 */
	public static final String PREF_USE_DAY_COUNT_DATE = "rocky_use_day_count_date";
	
	/**
	 * 返回最近一次存储的版本
	 * 
	 * @return
	 */
	public static int getAppVersion()
	{
		return getInt(PREF_APP_VERSION, 0);
	}
	
	/**
	 * 保存版本
	 */
	public static boolean putAppVersion(int version)
	{
		return putInt(PREF_APP_VERSION, version);
	}
	
	/**
	 * 获取用天统计的使用次数, 即每天统计一次
	 */
	public static long getUseDayCount()
	{
		return getLong(PREF_USE_DAY_COUNT, 0);
	}
	
	/**
	 * 保存用天统计的使用次数
	 */
	public static boolean putUseDayCount(long count)
	{
		if (isNeedDayCount())
		{
			// 今天需要统计
			putLong(PREF_USE_DAY_COUNT, count);
			putUseDayCountDate(System.currentTimeMillis());
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 今天是否需要统计
	 * 
	 * @return
	 */
	public static boolean isNeedDayCount()
	{
		long latestDay = DateUtils.getStartOfDate(new Date(getUseDayCountDate()));
		long today = DateUtils.getStartOfToday();
		
		return (today - latestDay != 0);
	}
	
	/**
	 * 获取最新记录天使用次数的日期, 单位毫秒
	 */
	public static long getUseDayCountDate()
	{
		return getLong(PREF_USE_DAY_COUNT_DATE, 0L);
	}
	
	/**
	 * 最新记录天使用次数的日期
	 */
	public static boolean putUseDayCountDate(long millisecond)
	{
		return putLong(PREF_USE_DAY_COUNT_DATE, millisecond);
	}

	/**
	 * 获取总使用次数
	 */
	public static long getUseTotalCount()
	{
		return getLong(PREF_USE_TOTAL_COUNT, 0);
	}

	/**
	 * 保存总使用次数
	 */
	public static boolean putUseTotalCount(long count)
	{
		return putLong(PREF_USE_TOTAL_COUNT, count);
	}

	/**
	 * 智能获取和存储deviceGUID, 取得后会自动保存, 加快读取速度.
	 * 
	 * @return
	 */
	public static String getDeviceGUID()
	{
		String deviceGUID = getString("device_guid", "");
		if (deviceGUID == null || deviceGUID.length() == 0)
		{
			deviceGUID = DeviceInfoManager.getDeviceGUID(RockySdk.getInstance().getContext());
			putString("device_guid", deviceGUID);
		}

		return deviceGUID;
	}

}
