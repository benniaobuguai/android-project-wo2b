package com.wo2b.sdk.core;


import android.content.Context;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.config.SdkConfig;

/**
 * Rocky Sdk, 项目通用类库.
 * 
 * @author Rocky
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @date 2014-07-15
 */
public final class RockySdk
{
	
	public static final String TAG = RockyConfig.TAG;

	private static final byte[] mLock = new byte[0];

	/** RockySdk对象 */
	private static volatile RockySdk mSdk = null;

	/** RockyConfig */
	private RockyConfig mRockyConfig;

	/** Client info */
	private ClientInfo mClientInfo;

	/** 静态调试标识, 方便全局使用 */
	public static boolean DEBUG = false;
	
	/**
	 * 二级调试开关, 仅提供开发人员使用, 会提示更多更全的日志信息.
	 */
	public static final boolean DEBUG_SECONDARY = true;
	
	/**
	 * 私有构造函数
	 * 
	 * @param context
	 */
	private RockySdk()
	{
		// 默认的ClientContext
		this.mClientInfo = ClientInfo.DEFAULT_CLIENT;
		// 默认已经发布, 并支持调试
		this.mClientInfo.setFlags(ClientInfo.FLAG_RELEASE | ClientInfo.FLAG_DEBUG);

		DEBUG = this.mClientInfo.isDebug();
	}
	
	/**
	 * 返回RockySdk实例
	 * 
	 * @return
	 */
	public static RockySdk getInstance()
	{
		if (mSdk == null)
		{
			synchronized (mLock)
			{
				if (mSdk == null)
				{
					mSdk = new RockySdk();
				}
			}
		}

		return mSdk;
	}
	
	/**
	 * 类库初始化
	 * 
	 * @param context
	 * @return
	 */
	public void init(RockyConfig config)
	{
		Log.I(TAG, "Welcome to use " + RockyConfig.SDK_NAME + "(v" + RockyConfig.SDK_VERSION + ").");
		this.mRockyConfig = config;
		this.mClientInfo = config.getClientInfo();
		
		if (DEBUG)
		{
			Log.W(TAG, RockyConfig.SDK_NAME + " is in debug mode.");
		}
	}
	
	/**
	 * Change client info.
	 * 
	 * @param clientInfo
	 */
	public void changedClientInfo(ClientInfo clientInfo)
	{
		this.mClientInfo = clientInfo;

		if (DEBUG)
		{
			printClientContext(this.mClientInfo);
		}
	}
	
	/**
	 * 打印客户端信息
	 * 
	 * @param clientInfo
	 */
	private static void printClientContext(ClientInfo clientInfo)
	{
		Log.E(TAG, "------------------------ Client Info ------------------------");
		
		Log.I(TAG, "CLIENT_ID: " + clientInfo.getPkgname());
		switch (clientInfo.getDeviceType())
		{
			case SdkConfig.Device.PHONE:
			{
				Log.I(TAG, "DEVICE_TYPE: PHONE");
				break;
			}
			case SdkConfig.Device.PAD:
			{
				Log.I(TAG, "DEVICE_TYPE: PAD");
				break;
			}
			case SdkConfig.Device.STB:
			{
				Log.I(TAG, "DEVICE_TYPE: STB");
				break;
			}
			default:
			{
				Log.E(TAG, "Unknown device");
				break;
			}
		}
		Log.I(TAG, "DEVICE_NAME: " + clientInfo.getAppname());
		Log.I(TAG, "RELEASE_FLG: " + clientInfo.isRelease());
		Log.I(TAG, "DEBUG_FLG: " + clientInfo.isDebug());
		
		Log.E(TAG, "------------------------ Client Info ------------------------");
	}

	/**
	 * Returns RockyConfig.
	 * 
	 * @return
	 */
	public RockyConfig getRockyConfig()
	{
		return mRockyConfig;
	}

	/**
	 * Returns ClientInfo.
	 * 
	 * @return
	 */
	public ClientInfo getClientInfo()
	{
		return mClientInfo;
	}

	/**
	 * Returns ApplicationContext.
	 * 
	 * @return
	 */
	public Context getContext()
	{
		if (mRockyConfig != null)
		{
			return mRockyConfig.getContext();
		}

		return null;
	}
	
	/**
	 * 版本信息
	 * 
	 * @return
	 */
	public String getSdkVersion()
	{
		return RockyConfig.getSdkVersion();
	}
	
	/**
	 * 类库名称
	 * 
	 * @return
	 */
	public String getSdkName()
	{
		return RockyConfig.getSdkName();
	}
	
	/**
	 * 主动/异常退出, Library释放资源
	 */
	public void onDestroy()
	{
		Log.I(TAG, "Rocky sdk--->onDestroy()");
		
		// RockySdk资源释放
		
		Log.I(TAG, "Rocky sdk quit.");
	}

}
