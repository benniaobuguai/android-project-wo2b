package com.wo2b.wrapper.app.background;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.network.NetworkSettings;

/**
 * 系统守护进程
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class DaemonService extends Service
{
	
	private static final String TAG = "Rocky.DaemonService";

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

			Intent newIntent = new Intent();
			IntentFilter filter = new IntentFilter();

			if (mobileNetInfo.isConnected() && wifiNetInfo.isConnected())
			{
				Log.I(TAG, "Mobile/Wifi Network is connected.");
				filter.addAction(NetworkSettings.ACTION_NETWORK_ALL);
			}
			else if (mobileNetInfo.isConnected())
			{
				Log.I(TAG, "Mobile Network is connected.");
				filter.addAction(NetworkSettings.ACTION_NETWORK_MOBILE);
			}
			else if (wifiNetInfo.isConnected())
			{
				Log.I(TAG, "Wifi Network is connected.");
				filter.addAction(NetworkSettings.ACTION_NETWORK_WIFI);
			}
			else if (!mobileNetInfo.isConnected() && !wifiNetInfo.isConnected())
			{
				// 所有网络都没有连接至网络
				Log.D(TAG, "Network is not connected.");
				filter.addAction(NetworkSettings.ACTION_NETWORK_NONE);
			}
			else
			{
				// 所有网络都没有连接至网络
				Log.D(TAG, "Network is not connected.");
				filter.addAction(NetworkSettings.ACTION_NETWORK_NONE);
			}

			sendBroadcast(newIntent);
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

		//registerNetworkBroadcastReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		//unregisterNetworkBroadcastReceiver();
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
