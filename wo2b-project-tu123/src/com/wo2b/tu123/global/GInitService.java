package com.wo2b.tu123.global;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Toast;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.bus.GBus;
import com.wo2b.sdk.bus.GEvent;

/**
 * 全局系统后台初始化
 * 
 * @author 笨鸟不乖
 * 
 */
public class GInitService extends IntentService
{

	private static final String TAG = "Global.InitService";

	public static final String ACTION_INIT_SERVICE = "com.wo2b.app.INIT_SERVICE";

	private boolean mInitCompleted = false;

	private Handler mHandler = null;

	public GInitService()
	{
		super("InitService");
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.I(TAG, "GInitService-->onCreate()");

		mHandler = new Handler();
		GBus.register(this);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.I(TAG, "GInitService-->onDestroy()");

		// mHandler = null;
		GBus.unregister(this);
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		// 创建完成后, 发送主线事件.
		GBus.post(GEvent.CONNECT_COMPLETE);

		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				doMoreAfter();

				// 所有操作完成后, 延时几秒后停止该服务.
				SystemClock.sleep(3 * 1000);
				stopSelf();
			}
		}, "init_thread").start();
	}

	/**
	 * 基本初始化
	 */
	public void doMoreAfter()
	{
		doSomething();
	}

	/**
	 * XXX初始化
	 */
	private void doSomething()
	{

	}

	/**
	 * 提示信息
	 * 
	 * @param text
	 */
	public void showToast(final String text)
	{
		mHandler.post(new Runnable()
		{

			@Override
			public void run()
			{
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 初始化是否完成
	 * 
	 * @return
	 */
	public boolean isInitCompleted()
	{
		return this.mInitCompleted;
	}

	// -------------------------------------------------------------------------------------------
	/**
	 * 启动服务
	 * 
	 * @param context
	 */
	public static void startService(Context context)
	{
		Intent service = new Intent(context, GInitService.class);
		// service.setAction(GInitService.ACTION_INIT_SERVICE);
		context.startService(service);
	}

	/**
	 * 停止服务
	 * 
	 * @param context
	 */
	public static void stopService(Context context)
	{
		Intent service = new Intent();
		service.setAction(ACTION_INIT_SERVICE);
		context.stopService(service);
	}

}
