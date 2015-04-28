package com.wo2b.sdk.core;

import android.app.Application;
import android.content.Context;

/**
 * Base Application
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-1
 */
public abstract class BaseApplication extends Application
{

	/** 是否已经进入过应用程序 */
	public static boolean isEntry = false;

	private static Context mRockyApplication = null;

	@Override
	public void onCreate()
	{
		super.onCreate();

		// 自行实现, 系统崩溃的处理类
		// RockyUncaughtExceptionHandler.getInstance().init(this);

		mRockyApplication = this;

		setUp();
	}

	public static class Config
	{

		public static final boolean DEVELOPER_MODE = false;
	}

	// -------------------- To do sth. --------------------
	/**
	 * Call when application set up.
	 */
	protected abstract void setUp();

	/**
	 * Not support now, it will be used in future.
	 */
	@Deprecated
	protected abstract void tearDown();

	// -------------------- Common --------------------

	public static Context getRockyApplication()
	{
		return mRockyApplication;
	}

	public static boolean isEntry()
	{
		return isEntry;
	}

	public static void setEntry(boolean isEntry)
	{
		BaseApplication.isEntry = isEntry;
	}

}
