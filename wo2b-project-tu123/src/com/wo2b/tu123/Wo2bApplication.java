package com.wo2b.tu123;

import opensource.component.imageloader.cache.disc.naming.Md5FileNameGenerator;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.ImageLoaderConfiguration;
import opensource.component.imageloader.core.assist.QueueProcessingType;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.StrictMode;

import com.wo2b.sdk.RockyApplication;
import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.wrapper.preference.XPreferenceManager;
import com.wo2b.tu123.global.AppCacheFactory;

/**
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class Wo2bApplication extends RockyApplication
{

	private static final String TAG = "Rocky.Application";
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")
	@Override
	public void onCreate()
	{
		if (RockyApplication.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}

		super.onCreate();
		//test();
	}

	public void test()
	{
		Intent intent = new Intent();
		// intent.setClass(this, Wo2bAppListActivity.class);
		intent.setAction("com.wo2b.user.LOGIN");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory("android.intent.category.DEFAULT");
		startActivity(intent);
		
		//Intent intent = new Intent();
		//intent.setClass(this, Wo2bAppListActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//startActivity(intent);
	}

	/**
	 * 初始化配置
	 */
	@Override
	public void setUp()
	{
		Log.I(TAG, "---------------- Rocky.Tu123 ----------------");

		// 初始化系统偏好设置, 便于全局访问.
		XPreferenceManager.getInstance().init(getRockyApplication());
		// 初始化ImageLoader.
		initImageLoader(getRockyApplication());
		
		// 广告初始化
		//ad_setUp(getRockyApplication());
	}
	
	@Override
	@Deprecated
	protected void tearDown()
	{

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
	
//	/**
//	 * 广告初始化
//	 * 
//	 * @param context
//	 */
//	public static final void ad_setUp(Context context)
//	{
//		try
//		{
//			// 初始化接口，应用启动的时候调用
//			// 参数：appId, appSecret, 调试模式
//			AdManager.getInstance(context).init("73057fb81878f2ba", "f2707618edb7c2a4", true);
//			// 如果使用积分广告，请务必调用积分广告的初始化接口:
//			OffersManager.getInstance(context).onAppLaunch();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * 退出广告
//	 * 
//	 * @param context
//	 */
//	public static final void ad_shutdown(Context context)
//	{
//		// 如果使用积分广告，请务必调用积分广告的初始化接口:
//		try
//		{
//			OffersManager.getInstance(context).onAppExit();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

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
	
}