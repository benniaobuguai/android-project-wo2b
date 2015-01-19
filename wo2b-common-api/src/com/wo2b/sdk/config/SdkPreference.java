package com.wo2b.sdk.config;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.wo2b.sdk.common.util.DateUtils;
import com.wo2b.sdk.common.util.DeviceInfoManager;

/**
 * 偏好设置工具类
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-6
 */
public class SdkPreference
{
	
	private Context mContext;
	private SharedPreferences mTempPref;
	private SharedPreferences mSystemPref;

	/**
	 * 私有构造函数
	 * 
	 * @param application
	 */
	protected SdkPreference()
	{
		
	}

	/**
	 * 单例模式产生XPreferenceManager对象
	 * 
	 * @param application
	 * @return
	 */
	public void init(Context application)
	{
		this.mContext = application;
		this.mTempPref = getPrefTemp(application);
		this.mSystemPref = getPrefSystem(application);
	}

	/**
	 * 返回Context对象
	 * 
	 * @return
	 */
	public Context getContext()
	{
		return mContext;
	}
	
	/**
	 * 用于保存临时数据
	 */
	public SharedPreferences getPrefTemp(Context context)
	{
		return context.getSharedPreferences("com.wo2b.temp", Context.MODE_PRIVATE);
	}

	/**
	 * 用于保存整个系统设置
	 */
	public SharedPreferences getPrefSystem(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}

	
	// -------------------------------- System Preference --------------------------------
	public int getInt(String key, int defValue)
	{
		return mSystemPref.getInt(key, defValue);
	}

	public long getLong(String key, long defValue)
	{
		return mSystemPref.getLong(key, defValue);
	}

	public float getFloat(String key, float defValue)
	{
		return mSystemPref.getFloat(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue)
	{
		return mSystemPref.getBoolean(key, defValue);
	}
	
	public String getString(String key, String defValue)
	{
		return mSystemPref.getString(key, defValue);
	}

	public boolean putInt(String key, int value)
	{
		Editor editor = edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public boolean putLong(String key, long value)
	{
		Editor editor = edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public boolean putFloat(String key, float value)
	{
		Editor editor = edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public boolean putBoolean(String key, boolean value)
	{
		Editor editor = edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public boolean putString(String key, String value)
	{
		Editor editor = edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public boolean remove(String key)
	{
		Editor editor = edit();
		editor.remove(key);
		return editor.commit();
	}

	public Editor edit()
	{
		return mSystemPref.edit();
	}

	// -------------------------------- System Preference --------------------------------
    
	
	// -------------------------------- Temp Preference --------------------------------
	public int getIntTemp(String key, int defValue)
	{
		return mTempPref.getInt(key, defValue);
	}

	public long getLongTemp(String key, long defValue)
	{
		return mTempPref.getLong(key, defValue);
	}

	public float getFloatTemp(String key, float defValue)
	{
		return mTempPref.getFloat(key, defValue);
	}

	public boolean getBooleanTemp(String key, boolean defValue)
	{
		return mTempPref.getBoolean(key, defValue);
	}

	public boolean putIntTemp(String key, int value)
	{
		Editor editor = editTemp();
		editor.putInt(key, value);
		return editor.commit();
	}

	public boolean putLongTemp(String key, long value)
	{
		Editor editor = editTemp();
		editor.putLong(key, value);
		return editor.commit();
	}

	public boolean putFloatTemp(String key, float value)
	{
		Editor editor = editTemp();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public boolean putBooleanTemp(String key, boolean value)
	{
		Editor editor = editTemp();
		editor.putBoolean(key, value);
		return editor.commit();
	}
	
	public boolean putStringTemp(String key, String value)
	{
		Editor editor = editTemp();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public String getStringTemp(String key, String defValue)
	{
		return mTempPref.getString(key, defValue);
	}

	public boolean removeTemp(String key)
	{
		Editor editor = editTemp();
		editor.remove(key);
		return editor.commit();
	}

	public Editor editTemp()
	{
		return mTempPref.edit();
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
		return mContext.getResources().getString(resId);
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
	public int getAppVersion()
	{
		return getInt(PREF_APP_VERSION, 0);
	}
	
	/**
	 * 保存版本
	 */
	public boolean putAppVersion(int version)
	{
		return putInt(PREF_APP_VERSION, version);
	}
	
	/**
	 * 获取用天统计的使用次数, 即每天统计一次
	 */
	public long getUseDayCount()
	{
		return getLong(PREF_USE_DAY_COUNT, 0);
	}
	
	/**
	 * 保存用天统计的使用次数
	 */
	public boolean putUseDayCount(long count)
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
	public boolean isNeedDayCount()
	{
		long latestDay = DateUtils.getStartOfDate(new Date(getUseDayCountDate()));
		long today = DateUtils.getStartOfToday();
		
		return (today - latestDay != 0);
	}
	
	/**
	 * 获取最新记录天使用次数的日期, 单位毫秒
	 */
	public long getUseDayCountDate()
	{
		return getLong(PREF_USE_DAY_COUNT_DATE, 0L);
	}
	
	/**
	 * 最新记录天使用次数的日期
	 */
	public boolean putUseDayCountDate(long millisecond)
	{
		return putLong(PREF_USE_DAY_COUNT_DATE, millisecond);
	}
	
	/**
	 * 获取总使用次数
	 */
	public long getUseTotalCount()
	{
		return getLong(PREF_USE_TOTAL_COUNT, 0);
	}
	
	/**
	 * 保存总使用次数
	 */
	public boolean putUseTotalCount(long count)
	{
		return putLong(PREF_USE_TOTAL_COUNT, count);
	}
	
	/**
	 * 智能获取和存储deviceGUID, 取得后会自动保存, 加快读取速度.
	 * 
	 * @return
	 */
	public String getDeviceGUID()
	{
		String deviceGUID = getString("device_guid", "");
		if (deviceGUID == null || deviceGUID.length() == 0)
		{
			deviceGUID = DeviceInfoManager.getDeviceGUID(getContext());
			putString("device_guid", deviceGUID);
		}

		return deviceGUID;
	}

}
