package com.wo2b.sdk.common.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * 设备信息
 * 
 * @author Rocky
 * 
 */
public class DeviceInfoManager
{

	/**
	 * 获取Mac地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context)
	{
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();

		return info.getMacAddress();
	}

	/**
	 * 获取设备唯一ID
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context)
	{
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId();
	}

	/**
	 * 返回设置唯一ID, 如果没有直接使用MAC地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceGUID(Context context)
	{
		String deviceGUID = getDeviceId(context);
		if (deviceGUID == null || deviceGUID.length() == 0)
		{
			return getMacAddress(context);
		}

		return deviceGUID;
	}

}
