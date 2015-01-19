package com.wo2b.sdk.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

/**
 * 网络连接管理器
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public final class NetworkConnectivityManager
{

	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null)
			{
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isWifiConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null)
			{
				return mWiFiNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isMobileConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null)
			{
				return mMobileNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable())
			{
				return mNetworkInfo.getType();
			}
		}

		return -1;
	}
	
	/**
	 * 
	 * @param context
	 * @param intent
	 * @param action
	 */
	public void wifiStateChangedAction(Context context, Intent intent, String action)
	{
		if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action))
		{
			// 在此监听wifi有无
			int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
			switch (wifiState)
			{
				case WifiManager.WIFI_STATE_DISABLED:
				{
					Toast.makeText(context, "WIFI_STATE_DISABLED", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_DISABLING:
				{
					Toast.makeText(context, "WIFI_STATE_DISABLING", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_ENABLED:
				{
					Toast.makeText(context, "WIFI_STATE_ENABLED", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_ENABLING:
				{
					Toast.makeText(context, "WIFI_STATE_ENABLING", Toast.LENGTH_LONG).show();
					break;
				}
				case WifiManager.WIFI_STATE_UNKNOWN:
				{
					Toast.makeText(context, "WIFI_STATE_UNKNOWN", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}
	}

}
