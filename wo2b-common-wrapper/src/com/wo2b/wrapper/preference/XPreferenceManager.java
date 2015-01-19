package com.wo2b.wrapper.preference;

import com.wo2b.wrapper.R;
import com.wo2b.sdk.config.SdkPreference;
import com.wo2b.wrapper.preference.RockyKeyValues.Keys;
import com.wo2b.wrapper.preference.RockyKeyValues.Values;

/**
 * 偏好设置工具类
 * 
 * @author Rocky
 * 
 */
public final class XPreferenceManager extends SdkPreference
{
	
	private static final byte[] mLock = new byte[0];


	private static XPreferenceManager mInstance = null;
	
	/**
	 * 私有构造函数
	 * 
	 * @param application
	 */
	private XPreferenceManager()
	{
		
	}

	/**
	 * 返回XPreferenceManager对象
	 * 
	 * @param context
	 * @return
	 */
	public static XPreferenceManager getInstance()
	{
		if (mInstance == null)
		{
			synchronized (mLock)
			{
				if (mInstance == null)
				{
					mInstance = new XPreferenceManager();
				}
			}
		}

		return mInstance;
	}
	
	// -------------------------------- High Frequency --------------------------------
	/**
	 * 获取睡眠模式
	 */
	public String getSleepMode()
	{
		return getString(Keys.PREF_SLEEP_MODE, Values.PREF_SLEEP_MODE_DEFAULT);
	}
	
	/**
	 * 保存睡眠模式
	 */
	public boolean putSleepMode(String mode)
	{
		return putString(Keys.PREF_SLEEP_MODE, mode);
	}
	
	/**
	 * 设置壁纸
	 * 
	 * @return
	 */
	public boolean isSetWallpaper()
	{
		return getBoolean(getKeyString(R.string.pk_wallpaper), true);
	}
	
	/**
	 * 是否自动播放
	 * 
	 * @return
	 */
	public boolean isAutoPlay()
	{
		return getBoolean(getKeyString(R.string.pk_auto_play), true);
	}
	
	/**
	 * 是否缓存本地
	 * 
	 * @return
	 */
	public boolean cacheLocalable()
	{
		return getBoolean(getKeyString(R.string.pk_cache_local), true);
	}
	
	/**
	 * 是否下载图片
	 * 
	 * @return
	 */
	public boolean imageDownloadable()
	{
		return getBoolean(getKeyString(R.string.pk_image_download), true);
	}
	
	/**
	 * 是否下载图片
	 * 
	 * @return
	 */
	public boolean hasMusicBackground()
	{
		return getBoolean(getKeyString(R.string.pk_bg_music), true);
	}
	
	/**
	 * 是否加载
	 * 
	 * @return
	 */
	public boolean isPasswordLock()
	{
		return getBoolean(getKeyString(R.string.pk_password_lock), true);
	}
	
}
