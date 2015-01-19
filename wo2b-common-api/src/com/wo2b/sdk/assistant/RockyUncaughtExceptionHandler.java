package com.wo2b.sdk.assistant;

import java.io.File;
import java.io.FileOutputStream;
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
import android.os.Looper;
import android.widget.Toast;

import com.wo2b.sdk.assistant.log.Log;

/**
 * UncaughtException处理类, 当程序发生Uncaught异常的时候, 由该类来接管程序, 并记录发送错误报告.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class RockyUncaughtExceptionHandler implements UncaughtExceptionHandler
{

	private static final String TAG = "Rocky.UncaughtExceptionHandler";

	private static byte[] mLock = new byte[0];

	/**
	 * CrashHandler实例
	 */
	private static volatile RockyUncaughtExceptionHandler mInstance = null;

	/**
	 * 程序的Context对象
	 */
	private Context mContext;

	/**
	 * 系统默认的UncaughtException处理类
	 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;

	/**
	 * 用来存储设备信息和异常信息
	 */
	private Map<String, String> mLogInfos = new HashMap<String, String>();

	/**
	 * 用于格式化日期,作为日志文件名的一部分
	 */
	private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);

	/**
	 * 保证只有一个CrashHandler实例
	 */
	private RockyUncaughtExceptionHandler()
	{

	}

	/**
	 * 获取CrashHandler实例 ,单例模式
	 */
	public static RockyUncaughtExceptionHandler getInstance()
	{
		if (mInstance == null)
		{
			synchronized (mLock)
			{
				if (mInstance == null)
				{
					mInstance = new RockyUncaughtExceptionHandler();
				}
			}
		}

		return mInstance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init(Context context)
	{
		mContext = context.getApplicationContext();
		// 获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		// 设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(final Thread thread, Throwable ex)
	{
		if (ex != null)
		{
			Log.E(TAG, "", ex);
		}

		// FIXME: 在这里拦截系统自定义需要全局处理的异常
		//if (ex instanceof RockyNoLoginException)
		//{
		//	//Toast.makeText(mContext, "你还没有登录呢?", Toast.LENGTH_LONG).show();
		//	Log.E(TAG, "你还没有登录呢?", ex);
		//
		//	return;
		//}

		
		// try
		// {
		// Runtime.getRuntime().exec("logcat -d");
		// Runtime.getRuntime().exec("logcat >> /sdcard/logcat.log");
		// }
		// catch (IOException e)
		// {
		// e.printStackTrace();
		// }


//		if (!handleException(ex) && mDefaultHandler != null)
//		{
//			// 如果用户没有处理则让系统默认的异常处理器来处理
//			mDefaultHandler.uncaughtException(thread, ex);
//		}
//		else
//		{
//			// SystemClock.sleep(3000);
//			Log.D(TAG, "------------------ Handle exception, exit the application. ------------------");
//			// 退出程序
//			killSelf();
//		}
	}

	/**
	 * 系统全局可被处理的异常
	 * 
	 * @param ex
	 * @return
	 */
	private boolean handleRockyException(Throwable ex)
	{
		return true;
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
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		saveCrashInfo2File(ex);

		// 使用Toast来显示异常信息
		new Thread()
		{

			@Override
			public void run()
			{
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉, 程序出现异常, 即将退出.", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		}.start();

		return true;
	}

	/**
	 * 收集设备参数信息
	 * 
	 * @param context
	 */
	public void collectDeviceInfo(Context context)
	{
		try
		{
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null)
			{
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				mLogInfos.put("versionName", versionName);
				mLogInfos.put("versionCode", versionCode);
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
				mLogInfos.put(field.getName(), field.get(null).toString());
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
		for (Map.Entry<String, String> entry : mLogInfos.entrySet())
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
		try
		{
			long timestamp = System.currentTimeMillis();
			String time = mDateFormat.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";

			String path = mContext.getDir("crash", 0).getPath() + "/";// "/sdcard/crash/";
			File dir = new File(path);
			if (!dir.exists())
			{
				dir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(path + fileName);
			fos.write(sb.toString().getBytes());
			fos.close();

			return fileName;
		}
		catch (Exception e)
		{
			Log.E(TAG, "An error occured while writing file...", e);
		}

		return null;
	}

	private void killSelf()
	{
		// reStart();

		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(1);
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
