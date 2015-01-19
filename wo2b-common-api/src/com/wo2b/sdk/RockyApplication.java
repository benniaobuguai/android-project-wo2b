package com.wo2b.sdk;

import android.app.Application;
import android.content.Context;

import com.wo2b.sdk.assistant.RockyUncaughtExceptionHandler;

/**
 * Rocky Application
 * 
 * @author Rocky
 * 
 */
public abstract class RockyApplication extends Application
{

	/** 是否已经进入过应用程序 */
	public static boolean isEntry = false;

	private static Context mRockyApplication = null;

	@Override
	public void onCreate()
	{
		super.onCreate();

		// 自行实现, 系统崩溃的处理类
		//RockyUncaughtExceptionHandler.getInstance().init(this);

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
		RockyApplication.isEntry = isEntry;
	}
	
	
}
