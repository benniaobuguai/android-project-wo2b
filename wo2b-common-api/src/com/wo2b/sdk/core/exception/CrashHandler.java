package com.wo2b.sdk.core.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.wo2b.sdk.assistant.log.Log;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-1
 */
public class CrashHandler implements UncaughtExceptionHandler
{

	private static final String TAG = "Global.CrashHandler";

	private static byte[] mLock = new byte[0];

	/**
	 * 系统默认的UncaughtException处理类
	 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/**
	 * 程序的Context对象
	 */
	private Context mContext;

	/**
	 * 用于格式化日期,作为日志文件名的一部分
	 */
	private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());

	/**
	 * 用来存储设备信息和异常信息
	 */
	private Map<String, String> mInfos = null;

	/**
	 * CrashHandler实例
	 */
	private static volatile CrashHandler mInstance = null;

	/**
	 * 私有构造函数
	 */
	private CrashHandler(Context context)
	{
		this.mContext = context;
		this.mInfos = new HashMap<String, String>();
	}

	/**
	 * 返回CrashHandler单实例对象
	 * 
	 * @return
	 */
	public static CrashHandler getInstance(Context context)
	{
		if (mInstance != null)
		{
			return mInstance;
		}
		synchronized (mLock)
		{
			if (mInstance == null)
			{
				mInstance = new CrashHandler(context);
			}
		}

		return mInstance;
	}

	/**
	 * 初始化
	 * 
	 */
	public void init()
	{
		mContext = mContext.getApplicationContext();

		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex)
	{
		if (ex != null)
		{
			Log.E(TAG, "", ex);
		}

		if (!handleException(ex) && mDefaultHandler != null)
		{
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);

			Log.W(TAG, "Default Handler Exception...");
			return;
		}
		// else if (ex instanceof OutOfMemoryError)
		// {
		// Log.E(TAG, "Special operation.........");
		// }
		else
		{
			// try {
			// Thread.sleep(3000);
			// } catch (InterruptedException e) {
			// Log.e(TAG, "error : ", e);
			// }

			// 退出程序
			killSelf();
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex)
	{
		if (ex == null)
		{
			return false;
		}

		// new Thread()
		// {
		// @Override
		// public void run()
		// {
		// Looper.prepare();
		// Toast.makeText(mContext, "Sorry, Abnormal Termination.",
		// Toast.LENGTH_LONG).show();
		// Looper.loop();
		// }
		// }.start();

		// 收集设备参数信息
		// collectDeviceInfo();
		// 保存日志文件
		// saveCrashInfo2File(ex);

		//MobclickAgentProxy.reportError(mContext, ex);
		Log.D("Y_(^@^)_Y", TAG + "------->handleException()");

		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public void collectDeviceInfo()
	{
		try
		{
			PackageManager pm = mContext.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				mInfos.put("versionName", versionName);
				mInfos.put("versionCode", versionCode);
			}
		}
		catch (NameNotFoundException e)
		{
			Log.E(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields)
		{
			try
			{
				field.setAccessible(true);
				mInfos.put(field.getName(), field.get(null).toString());
				Log.D(TAG, field.getName() + " : " + field.get(null));
			}
			catch (Exception e)
			{
				Log.E(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return 返回文件名称,便于将文件传送到服务器
	 */
	private String saveCrashInfo2File(Throwable ex)
	{

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : mInfos.entrySet())
		{
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}

		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null)
		{
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);

		FileOutputStream fos = null;
		try
		{
			long timestamp = System.currentTimeMillis();
			String time = mFormatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";

			String path = mContext.getDir("crash", 0).getPath() + "/";// "/sdcard/crash/";
			File dir = new File(path);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			fos = new FileOutputStream(path + fileName);
			fos.write(sb.toString().getBytes());
			fos.flush();

			return fileName;
		}
		catch (Exception e)
		{
			Log.E(TAG, "an error occured while writing file...", e);
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private void killSelf()
	{
		// reStart();
		//YSApplication.clearAll();
		// 如果开发者调用 Process.kill 或者 System.exit 之类的方法杀死进程，请务必再次之前调用此方法，用来保存统计数据。
		//MobclickAgentProxy.onKillProcess(mContext);

		//mContext.stopService(new Intent(mContext, BackupService.class));
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

	/**
	 * 重新启动
	 */
	public void reStart()
	{
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		List<RunningTaskInfo> taskInfos = am.getRunningTasks(Integer.MAX_VALUE);
		RunningTaskInfo taskInfo = null;
		ComponentName baseActivity = null;
		String pkgName = null;

		for (int i = 0, len = taskInfos.size(); i < len; i++)
		{
			taskInfo = taskInfos.get(i);
			baseActivity = taskInfo.baseActivity;
			pkgName = baseActivity.getPackageName();
			if (pkgName.equalsIgnoreCase(mContext.getPackageName()))
			{
				ComponentName topActivity = taskInfo.topActivity;
				if (!topActivity.getClassName().equalsIgnoreCase(baseActivity.getClassName()))
				{

					Intent intent = new Intent();
					intent.setClassName(mContext, baseActivity.getClassName());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
							Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mgr.set(AlarmManager.RTC, System.currentTimeMillis(), pendingIntent);
				}

				return;
			}
		}
	}

}
