package com.wo2b.tu123.global;

import java.lang.reflect.Field;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 
 * @author Rocky
 * 
 */
public class CommonUtils
{
	
	/**
	 * Returns DisplayMetrics.
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context)
	{
		WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		window.getDefaultDisplay().getMetrics(outMetrics);
		
		return outMetrics;
	}
	
	/**
	 * Returns width of screen.
	 * 
	 * @return
	 */
	public static int getScreenWidth(Context context)
	{
		return getDisplayMetrics(context).widthPixels;
	}
	
	/**
	 * Returns height of screen, including status bar height.
	 * 
	 * @return
	 */
	public static int getScreenHeight(Context context)
	{
		return getDisplayMetrics(context).heightPixels;
	}
	
	/**
	 * Returns status bar height;
	 * 
	 * @param context
	 * @return
	 */
	public static int getStatusBarHeight(Context context)
	{
		int height = 0;
		try
		{
			Class<?> c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int temp = Integer.parseInt(field.get(obj).toString());
			height = context.getResources().getDimensionPixelSize(temp);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return height;
	}
	
	
	/**
	 * 根据包名判断APK是否存在
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppExists(Context context, String packageName)
	{
		PackageManager pm = context.getPackageManager();
		try
		{
			PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			if (packageInfo != null)
			{
				return true;
			}
		}
		catch (NameNotFoundException e)
		{
			return false;
		}
		
		return true;
	}

}
