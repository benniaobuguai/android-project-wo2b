package com.wo2b.sdk.network;

/**
 * 网络连接设置
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-2
 */
public enum NetworkStatus
{

	/**
	 * 网络已连接, 手机+wifi
	 */
	CONNECTED,

	/**
	 * 网络已连接-->手机网络
	 */
	MOBILE,

	/**
	 * 网络已连接-->wifi网络
	 */
	WIFI,

	/**
	 * 网络没有连接
	 */
	DISCONNECTED;

}
