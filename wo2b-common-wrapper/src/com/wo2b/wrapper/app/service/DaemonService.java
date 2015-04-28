package com.wo2b.wrapper.app.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.bus.GBus;
import com.wo2b.sdk.bus.GEvent;
import com.wo2b.sdk.network.NetworkStatus;

/**
 * 系统守护进程
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-2
 */
public class DaemonService extends Service
{
	
	private static final String TAG = "Rocky.DaemonService";

	/**
	 * 网络状态
	 */
	public static NetworkStatus mNetworkStatus = NetworkStatus.CONNECTED;

	// 网络状态监听
	private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			Log.I(TAG, "Network state changed, action: " + action);

			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mobileNetInfo.isConnected() && wifiNetInfo.isConnected())
			{
				mNetworkStatus = NetworkStatus.CONNECTED;
			}
			else if (mobileNetInfo.isConnected())
			{
				mNetworkStatus = NetworkStatus.MOBILE;
			}
			else if (wifiNetInfo.isConnected())
			{
				Log.I(TAG, "Wifi Network is connected.");
				mNetworkStatus = NetworkStatus.WIFI;
			}
			else if (!mobileNetInfo.isConnected() && !wifiNetInfo.isConnected())
			{
				// 所有网络都没有连接至网络
				mNetworkStatus = NetworkStatus.DISCONNECTED;
			}
			else
			{
				// 所有网络都没有连接至网络
				mNetworkStatus = NetworkStatus.DISCONNECTED;
			}

			Log.D(TAG, "Network status changed: " + mNetworkStatus.name());

			GBus.post(GEvent.NETWORK_STATUS, mNetworkStatus);
		}
	};

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.I(TAG, "DaemonService-->onCreate");

		registerNetworkBroadcastReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		unregisterNetworkBroadcastReceiver();
	}

	/**
	 * 注册网络连接广播接收器
	 */
	private void registerNetworkBroadcastReceiver()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		registerReceiver(mConnectionReceiver, intentFilter);
	}

	/**
	 * 取消网络连接广播接收器
	 */
	private void unregisterNetworkBroadcastReceiver()
	{
		if (mConnectionReceiver != null)
		{
			unregisterReceiver(mConnectionReceiver);
		}
	}

	// NETWORK
	public boolean isNetworkAvailable()
	{
		Context context = getApplicationContext();
		ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connect == null)
		{
			return false;
		}
		else
		{
			// get all network info
			NetworkInfo[] info = connect.getAllNetworkInfo();
			if (info != null)
			{
				for (int i = 0; i < info.length; i++)
				{
					if (info[i].getState() == NetworkInfo.State.CONNECTED)
					{
						return true;
					}
				}
			}
		}

		return false;
	}

}
