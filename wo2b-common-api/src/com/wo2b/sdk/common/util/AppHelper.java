package com.wo2b.sdk.common.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * 应用助手
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-13
 */
public class AppHelper
{

	/**
	 * 运行一下应用程序
	 * 
	 * @param context 上下文
	 * @param pkgname 包名
	 */
	public static void launchApplication(Context context, String pkgname)
	{
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgname);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);

		context.startActivity(intent);
	}

	/**
	 * 打开App市场, 对App打分等.
	 * 
	 * @param context 上下文对象
	 * @param pkgname 包名
	 */
	public static void launchAppMarket(Context context, String pkgname)
	{
		try
		{
			Uri uri = Uri.parse("market://details?id=" + pkgname);
			// uri = Uri.parse("market://details?id=" + "com.tencent.mobileqq");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
			Toast.makeText(context, "找不到应用市场, 操作无法继续!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 安装应用程序
	 * 
	 * @param context 上下文
	 * @param path 文件地址
	 */
	public static final void install(Context context, String path)
	{
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	/**
	 * 卸载应用程序
	 * 
	 * @param context
	 * @param path
	 */
	public static final void uninstall(Context context, String path)
	{

	}

}
