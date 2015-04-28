package com.wo2b.tu123.global;

import opensource.component.imageloader.cache.disc.naming.Md5FileNameGenerator;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.ImageLoaderConfiguration;
import opensource.component.imageloader.core.assist.QueueProcessingType;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.core.BaseApplication;
import com.wo2b.sdk.core.exception.CrashHandler;
import com.wo2b.sdk.umeng.MobclickAgentProxy;
import com.wo2b.tu123.global.Global.Version;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class GApplication extends BaseApplication implements GApplicationHandler
{

	private static final String TAG = "Global.Application";

	private static Context mContext;
	
	private static Global.Version mVersion = Global.Version.Defualt;

	/** 更新检查, 默认需要检查更新 */
	private static boolean checkUpdateFlag = true;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate()
	{
		if (BaseApplication.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		mContext = getApplicationContext();

		// 保存文件目录
		mVersion = getGVerion();
		// XPreferenceManager.putGVersion(mVersion);

		// MemoryHelper.loadDefaultVmPolicy();
	}
	
	@Override
	protected void setUp()
	{
		Global.init(getApplicationContext());
		
		loadConfig();

		// 异常统一处理
		CrashHandler.getInstance(this).init();

		// 开启友盟日志.
		// com.umeng.common.Log.LOG = true;
		// MobclickAgentProxy.setDebugMode(true);
		MobclickAgentProxy.openActivityDurationTrack(false);

		initImageLoader(getRockyApplication());
	}

	@Override
	@Deprecated
	protected void tearDown()
	{

	}

	public void loadConfig()
	{
		Log.I(TAG, "------------------- Load Config -------------------");

	}
	
	/**
	 * ImageLoader初始化
	 * 
	 * @param context
	 */
	protected void initImageLoader(Context context)
	{
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
			.baseDirectory(new AppCacheFactory().getAppDir()) // Custom directory
			.threadPriority(Thread.NORM_PRIORITY - 2)
			.denyCacheImageMultipleSizesInMemory()
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.FIFO)
			//.writeDebugLogs() // Remove for release app
			.build();
		
		ImageLoader.getInstance().init(config);
	}

	public void loadSharedPreferences()
	{

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onTerminate()
	{
		super.onTerminate();
	}

	/**
	 * 返回Application Context实例.
	 * 
	 * @return
	 */
	public static Context getContext()
	{
		return mContext;
	}

	/**
	 * 返回检查更新
	 * 
	 * @return
	 */
	public static boolean isUpdateCheck()
	{
		return checkUpdateFlag;
	}

	/**
	 * 设置是否需要检查更新
	 * 
	 * @param isUpdateCheck
	 */
	public static void setUpdateCheck(boolean isUpdateCheck)
	{
		GApplication.checkUpdateFlag = isUpdateCheck;
	}

	@Override
	public Version getGVerion()
	{
		return null;
	}

}
