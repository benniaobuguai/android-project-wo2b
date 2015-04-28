package com.wo2b.sdk.common.util;

import java.io.File;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.wo2b.sdk.assistant.log.Log;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-16
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class TDevice
{

	public static boolean GTE_HC;
	public static boolean GTE_ICS;
	public static boolean PRE_HC;
	private static Boolean _hasBigScreen = null;
	private static Boolean _hasCamera = null;
	private static Boolean _isTablet = null;
	private static Integer _loadFactor = null;
	private static int _pageSize = -1;
	public static float displayDensity = 0.0F;

	static
	{
		GTE_ICS = Build.VERSION.SDK_INT >= 14;
		GTE_HC = Build.VERSION.SDK_INT >= 11;
		PRE_HC = Build.VERSION.SDK_INT >= 11 ? false : true;
	}

	public TDevice()
	{
	}

	public static float dpToPixel(Context context, float dp)
	{
		return dp * (getDisplayMetrics(context).densityDpi / 160F);
	}

	public static int getDefaultLoadFactor(Context context)
	{
		if (_loadFactor == null)
		{
			Integer integer = Integer.valueOf(0xf & context.getResources().getConfiguration().screenLayout);
			_loadFactor = integer;
			_loadFactor = Integer.valueOf(Math.max(integer.intValue(), 1));
		}
		return _loadFactor.intValue();
	}

	public static float getDensity(Context context)
	{
		if (displayDensity == 0.0)
			displayDensity = getDisplayMetrics(context).density;
		return displayDensity;
	}

	public static DisplayMetrics getDisplayMetrics(Context context)
	{
		DisplayMetrics displaymetrics = new DisplayMetrics();
		((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(
				displaymetrics);
		return displaymetrics;
	}

	public static int getScreenHeight(Context context)
	{
		return getDisplayMetrics(context).heightPixels;
	}

	public static int getScreenWidth(Context context)
	{
		return getDisplayMetrics(context).widthPixels;
	}

	public static int[] getRealScreenSize(Activity activity)
	{
		int[] size = new int[2];
		int screenWidth = 0, screenHeight = 0;
		WindowManager w = activity.getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);
		// since SDK_INT = 1;
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
			try
			{
				screenWidth = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
				screenHeight = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
			}
			catch (Exception ignored)
			{
			}
		// includes window decorations (statusbar bar/menu bar)
		if (Build.VERSION.SDK_INT >= 17)
			try
			{
				Point realSize = new Point();
				Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
				screenWidth = realSize.x;
				screenHeight = realSize.y;
			}
			catch (Exception ignored)
			{
			}
		size[0] = screenWidth;
		size[1] = screenHeight;
		return size;
	}

	public static int getStatusBarHeight(Context context)
	{
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0;
		try
		{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			return context.getResources().getDimensionPixelSize(x);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	public static int getPageSize(Context context)
	{
		if (_pageSize == -1)
			if (TDevice.isTablet(context))
				_pageSize = 50;
			else if (TDevice.hasBigScreen(context))
				_pageSize = 20;
			else
				_pageSize = 10;
		return _pageSize;
	}

	private static String PREF_NAME = "creativelockerV2.pref";

	public static String getUdid(Context context)
	{
		SharedPreferences pre = context.getSharedPreferences(PREF_NAME, Context.MODE_MULTI_PROCESS);

		String udid = pre.getString("udid", "");
		if (udid.length() == 0)
		{
			SharedPreferences.Editor editor = pre.edit();
			udid = String.format("%s", UUID.randomUUID());
			editor.putString("udid", udid);
			editor.commit();
		}

		return udid;
	}

	public static boolean hasBigScreen(Context context)
	{
		boolean flag = true;
		if (_hasBigScreen == null)
		{
			boolean flag1;
			if ((0xf & context.getResources().getConfiguration().screenLayout) >= 3)
				flag1 = flag;
			else
				flag1 = false;
			Boolean boolean1 = Boolean.valueOf(flag1);
			_hasBigScreen = boolean1;
			if (!boolean1.booleanValue())
			{
				if (getDensity(context) <= 1.5F)
					flag = false;
				_hasBigScreen = Boolean.valueOf(flag);
			}
		}
		return _hasBigScreen.booleanValue();
	}

	public static final boolean hasCamera(Context context)
	{
		if (_hasCamera == null)
		{
			PackageManager pckMgr = context.getPackageManager();
			boolean flag = pckMgr.hasSystemFeature("android.hardware.camera.front");
			boolean flag1 = pckMgr.hasSystemFeature("android.hardware.camera");
			boolean flag2;
			if (flag || flag1)
				flag2 = true;
			else
				flag2 = false;
			_hasCamera = Boolean.valueOf(flag2);
		}
		return _hasCamera.booleanValue();
	}

	public static boolean hasHardwareMenuKey(Context context)
	{
		boolean flag = false;
		if (PRE_HC)
			flag = true;
		else if (GTE_ICS)
		{
			flag = ViewConfiguration.get(context).hasPermanentMenuKey();
		}
		else
			flag = false;
		return flag;
	}

	public static boolean hasInternet(Context context)
	{
		boolean flag;
		if (((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo() != null)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static boolean gotoGoogleMarket(Activity activity, String pck)
	{
		try
		{
			Intent intent = new Intent();
			intent.setPackage("com.android.vending");
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=" + pck));
			activity.startActivity(intent);
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static boolean isPackageExist(Context context, String pckName)
	{
		try
		{
			PackageInfo pckInfo = context.getPackageManager().getPackageInfo(pckName, 0);
			if (pckInfo != null)
				return true;
		}
		catch (NameNotFoundException e)
		{
			Log.E("TDevice", e.getMessage());
		}
		return false;
	}

	public static void hideAnimatedView(View view)
	{
		if (PRE_HC && view != null)
			view.setPadding(view.getWidth(), 0, 0, 0);
	}

	public static void hideSoftKeyboard(Context context, View view)
	{
		if (view == null)
			return;
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
				view.getWindowToken(), 0);
	}

	public static boolean isLandscape(Context context)
	{
		boolean flag;
		if (context.getResources().getConfiguration().orientation == 2)
			flag = true;
		else
			flag = false;
		return flag;
	}

	public static boolean isPortrait(Context context)
	{
		boolean flag = true;
		if (context.getResources().getConfiguration().orientation != 1)
			flag = false;
		return flag;
	}

	public static boolean isTablet(Context context)
	{
		if (_isTablet == null)
		{
			boolean flag;
			if ((0xf & context.getResources().getConfiguration().screenLayout) >= 3)
				flag = true;
			else
				flag = false;
			_isTablet = Boolean.valueOf(flag);
		}
		return _isTablet.booleanValue();
	}

	public static float pixelsToDp(Context context, float f)
	{
		return f / (getDisplayMetrics(context).densityDpi / 160F);
	}

	public static void showAnimatedView(View view)
	{
		if (PRE_HC && view != null)
			view.setPadding(0, 0, 0, 0);
	}

	public static void showSoftKeyboard(Dialog dialog)
	{
		dialog.getWindow().setSoftInputMode(4);
	}

	public static void showSoftKeyboard(Context context, View view)
	{
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view,
				InputMethodManager.SHOW_FORCED);
	}

	public static void toogleSoftKeyboard(Context context, View view)
	{
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static boolean isSdcardReady()
	{
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	public static String getCurCountryLan(Context context)
	{
		return context.getResources().getConfiguration().locale.getLanguage() + "-"
				+ context.getResources().getConfiguration().locale.getCountry();
	}

	public static boolean isZhCN(Context context)
	{
		String lang = context.getResources().getConfiguration().locale.getCountry();
		if (lang.equalsIgnoreCase("CN"))
		{
			return true;
		}
		return false;
	}

	public static String percent(double p1, double p2)
	{
		String str;
		double p3 = p1 / p2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		str = nf.format(p3);
		return str;
	}

	public static String percent2(double p1, double p2)
	{
		String str;
		double p3 = p1 / p2;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(0);
		str = nf.format(p3);
		return str;
	}

	public static void gotoMarket(Context context, String pck)
	{
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=" + pck));
		context.startActivity(intent);
	}

	public static void openAppInMarket(Context context)
	{
		if (context != null)
		{
			String pckName = context.getPackageName();
			try
			{
				gotoMarket(context, pckName);
			}
			catch (Exception ex)
			{
				try
				{
					String otherMarketUri = "http://market.android.com/details?id=" + pckName;
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(otherMarketUri));
					context.startActivity(intent);
				}
				catch (Exception e)
				{

				}
			}
		}
	}

	public static void setFullScreen(Activity activity)
	{
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
		activity.getWindow().setAttributes(params);
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	public static void cancelFullScreen(Activity activity)
	{
		WindowManager.LayoutParams params = activity.getWindow().getAttributes();
		params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
		activity.getWindow().setAttributes(params);
		activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	public static PackageInfo getPackageInfo(Context context, String pckName)
	{
		try
		{
			return context.getPackageManager().getPackageInfo(pckName, 0);
		}
		catch (NameNotFoundException e)
		{
			Log.E("TDevice", e.getMessage());
		}
		return null;
	}

	public static int getVersionCode(Context context)
	{
		int versionCode = 0;
		try
		{
			versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			versionCode = 0;
		}
		return versionCode;
	}

	public static int getVersionCode(Context context, String packageName)
	{
		int versionCode = 0;
		try
		{
			versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			versionCode = 0;
		}
		return versionCode;
	}

	public static String getVersionName(Context context)
	{
		String name = "";
		try
		{
			name = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
		}
		catch (PackageManager.NameNotFoundException ex)
		{
			name = "";
		}
		return name;
	}

	public static boolean isScreenOn(Context context)
	{
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();
	}

	public static void installAPK(Context context, File file)
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public static Intent getInstallApkIntent(File file)
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		return intent;
	}

	public static void openDial(Context context, String number)
	{
		Uri uri = Uri.parse("tel:" + number);
		Intent it = new Intent(Intent.ACTION_DIAL, uri);
		context.startActivity(it);
	}

	public static void openSMS(Context context, String smsBody, String tel)
	{
		Uri uri = Uri.parse("smsto:" + tel);
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);
		it.putExtra("sms_body", smsBody);
		context.startActivity(it);
	}

	public static void openDail(Context context)
	{
		Intent intent = new Intent(Intent.ACTION_DIAL);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void openSendMsg(Context context)
	{
		Uri uri = Uri.parse("smsto:");
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void openCamera(Context context)
	{
		Intent intent = new Intent(); // 调用照相机
		intent.setAction("android.media.action.STILL_IMAGE_CAMERA");
		intent.setFlags(0x34c40000);
		context.startActivity(intent);
	}

	public static String getIMEI(Context context)
	{
		TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tel.getDeviceId();
	}

	public static String getPhoneType()
	{
		return android.os.Build.MODEL;
	}

	public static void openApp(Context context, String packageName)
	{
		Intent mainIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);
		// mainIntent.setAction(packageName);
		if (mainIntent == null)
		{
			mainIntent = new Intent(packageName);
		}
		else
		{
			Log.I("TDevice", "Action:" + mainIntent.getAction());
		}
		context.startActivity(mainIntent);
	}

	public static boolean isWifiOpen(Context context)
	{
		boolean isWifiConnect = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// check the networkInfos numbers
		NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
		for (int i = 0; i < networkInfos.length; i++)
		{
			if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED)
			{
				if (networkInfos[i].getType() == ConnectivityManager.TYPE_MOBILE)
				{
					isWifiConnect = false;
				}
				if (networkInfos[i].getType() == ConnectivityManager.TYPE_WIFI)
				{
					isWifiConnect = true;
				}
			}
		}
		return isWifiConnect;
	}

	public static void uninstallApk(Context context, String packageName)
	{
		if (isPackageExist(context, packageName))
		{
			Uri packageURI = Uri.parse("package:" + packageName);
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
			context.startActivity(uninstallIntent);
		}
	}

	@SuppressWarnings("deprecation")
	public static void copyTextToBoard(Context context, String string)
	{
		if (TextUtils.isEmpty(string))
			return;
		ClipboardManager clip = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clip.setText(string);
	}

	public static void sendEmail(Context context, String email, String content)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]
			{
				email
			});
			context.startActivity(intent);
		}
		catch (ActivityNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	public static int getStatuBarHeight(Context context)
	{
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 38;// 默认为38，貌似大部分是这样的
		try
		{
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);

		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return sbar;
	}

	public static boolean hasStatusBar(Activity activity)
	{
		WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
		if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * 判断指定包名的进程是否运行
	 * 
	 * @param context
	 * @param packageName
	 *            指定包名
	 * @return 是否运行
	 */
	public static boolean isRunning(Context context, String packageName)
	{
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> infos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo rapi : infos)
		{
			if (rapi.processName.equals(packageName))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 用来判断服务是否运行.
	 * 
	 * @param context
	 * @param className 判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext, String className)
	{
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(300);
		if (!(serviceList.size() > 0))
		{
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++)
		{
			if (serviceList.get(i).service.getClassName().equals(className))
			{
				return true;
			}
		}
		return isRunning;
	}
	
	/**
	 * 是否在SdCard
	 * 
	 * @return
	 */
	public static boolean hasSdcard()
	{
		boolean bool = Environment.getExternalStorageState().equals("mounted");
		File localFile = null;
		if (bool)
		{
			localFile = Environment.getExternalStorageDirectory();
		}

		return localFile != null;
	}
	
}
