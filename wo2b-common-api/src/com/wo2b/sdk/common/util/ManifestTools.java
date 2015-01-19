package com.wo2b.sdk.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.assistant.upgrade.VersionInfo;

/**
 * ManifestTools
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-10-22
 */
public class ManifestTools
{

	private static final String TAG = "ManifestTools";

	/**
	 * 获取版本信息
	 * 
	 * @return
	 */
	public static VersionInfo getVersionInfo(Context context)
	{
		VersionInfo versionInfo = new VersionInfo();

		try
		{
			PackageInfo pkg = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionInfo.setAppName(pkg.applicationInfo.name);
			versionInfo.setVersionCode(pkg.versionCode);
			versionInfo.setVersionName(pkg.versionName);
		}
		catch (NameNotFoundException e)
		{
			Log.E(TAG, "getVersionInfo error" + e.getMessage());
			e.printStackTrace();
		}

		return versionInfo;
	}

	/**
	 * 返回版本信息
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppVersion(Context context)
	{
		PackageManager localPackageManager = context.getPackageManager();
		try
		{
			String str = localPackageManager.getPackageInfo(context.getPackageName(), 0).versionName;
			return str;
		}
		catch (PackageManager.NameNotFoundException e)
		{
			Log.E(TAG, "getAppVersion error" + e.getMessage());
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 返回应用名称
	 * 
	 * @param context
	 * @return
	 */
	public static final String getApplicationLable(Context context)
	{
		if (context != null)
		{
			CharSequence label = context.getPackageManager().getApplicationLabel(context.getApplicationInfo());
			if (label != null)
			{
				return label.toString();
			}
		}

		return null;
	}

	/**
	 * 返回源数据信息
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static Object getApplicationMetaData(Context context, String key)
	{
		try
		{
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);

			return appInfo.metaData.get(key);
		}
		catch (NameNotFoundException e)
		{
			Log.E(TAG, "getApplicationMetaData error" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 返回源数据信息
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getActivityMetaData(Activity context, String key)
	{
		try
		{
			ActivityInfo info = context.getPackageManager().getActivityInfo(context.getComponentName(),
					PackageManager.GET_META_DATA);
			return info.metaData.getString(key);
		}
		catch (NameNotFoundException e)
		{
			Log.E(TAG, "getActivityMetaData error" + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}
