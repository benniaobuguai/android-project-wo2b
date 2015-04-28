package com.wo2b.sdk.config;

import com.wo2b.sdk.core.ClientInfo;

/**
 * 基本配置, 包含常规存储至SharePreference的操作.
 * 
 * @author 笨鸟不乖
 * 
 */
public final class SdkConfig
{
	
	/**
	 * 默认时长
	 */
	public static final long DEFAULT_TIME = 0;
	
	/**
	 * 设备类型
	 * 
	 */
	public interface Device
	{
		
		/**
		 * 手机
		 */
		int PHONE = ClientInfo.DEVICE_PHONE;
		
		/**
		 * 平板
		 */
		int PAD = ClientInfo.DEVICE_PAD;
		
		/**
		 * 机顶盒
		 */
		int STB = ClientInfo.DEVICE_STB;
		
		/**
		 * 默认设备
		 */
		int DEFAULT_DEVICE = ClientInfo.DEVICE_DEFAULT;
		
	}
	
	/**
	 * 私有构造函数
	 */
	private SdkConfig()
	{
		
	}
	
}
