package com.wo2b.sdk.common.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 常用工具类, 是否有网络|是否有摄像头等.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-10-30
 */
public class Utils
{

	public static boolean GTE_HC;
	public static boolean GTE_ICS;
	public static boolean PRE_HC;

	private static Boolean _hasCamera = null;
	private static Boolean _isTablet = null;
	public static float displayDensity = 0.0F;

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static float getDensity(Context context)
	{
		if (displayDensity == 0.0)
		{
			displayDensity = getDisplayMetrics(context).density;
		}

		return displayDensity;
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context)
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		windowManager.getDefaultDisplay().getMetrics(displaymetrics);

		return displaymetrics;
	}

	/**
	 * 是否为平板
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context)
	{
		if (_isTablet == null)
		{
			boolean flag = false;
			if ((0xf & context.getResources().getConfiguration().screenLayout) >= 3)
			{
				flag = true;
			}

			_isTablet = Boolean.valueOf(flag);
		}

		return _isTablet.booleanValue();
	}

	/**
	 * 是否有摄像头
	 * 
	 * @param context
	 * @return
	 */
	public static final boolean hasCamera(Context context)
	{
		if (_hasCamera == null)
		{
			PackageManager packageManager = context.getPackageManager();
			boolean flag = packageManager.hasSystemFeature("android.hardware.camera.front");
			boolean flag1 = packageManager.hasSystemFeature("android.hardware.camera");
			boolean flag2;
			if (flag || flag1)
			{
				flag2 = true;
			}
			else
			{
				flag2 = false;
			}

			_hasCamera = Boolean.valueOf(flag2);
		}

		return _hasCamera.booleanValue();
	}

	/**
	 * 是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasInternet(Context context)
	{
		boolean flag = false;
		if (((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null)
		{
			flag = true;
		}

		return flag;
	}

}
