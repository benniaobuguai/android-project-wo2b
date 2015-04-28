package com.wo2b.sdk.core;

import android.content.Context;
import android.os.Environment;

/**
 * <pre>
 * <ul><b>版本更新日志</b></ul>
 * <li>2013-12-18 Library基本稳定<b> [1.0.0]</b></li>
 * <li>2014-01-15 Library更改包名<b> [2.0.0]</b></li>
 * </pre>
 * 
 * @author 笨鸟不乖
 * 
 */
public class RockyConfig
{

	/**
	 * 类库名称
	 */
	public final static String SDK_NAME = "Rocky.Sdk";

	/**
	 * Sdk版本
	 */
	public final static String SDK_VERSION = "2.0.0";

	/**
	 * 通用日志标签
	 */
	public final static String TAG = "Rocky.Sdk";
	
	/**
	 * 调试模式: 用于控制打印日志等
	 */
	public static boolean DEBUG = true;
	
	/**
	 * 发布版本
	 */
	public final static boolean RELEASE = false;
	
	/**
	 * 保存路径
	 */
	public static final String WO2B_DIR = Environment.getExternalStorageDirectory() + "/wo2b";

	/**
	 * 临时数据
	 */
	private static final String WO2B_DIR_TEMP = WO2B_DIR + "/temp";

	/**
	 * 普通缓存
	 */
	private static final String WO2B_DIR_CACHE = WO2B_DIR + "/cache";
	
	private Context context;
	
	private ClientInfo clientInfo;
	
	private boolean hasAdBanner;
	private boolean hasAdPointsWall;
	
	private RockyConfig(final Builder builder)
	{
		this.context = builder.context;
		this.clientInfo = builder.clientInfo;
		this.hasAdBanner = builder.hasAdBanner;
		this.hasAdPointsWall = builder.hasAdPointsWall;
	}
	
	/**
	 * 版本信息
	 * 
	 * @return
	 */
	public static String getSdkVersion()
	{
		return SDK_VERSION;
	}
	
	/**
	 * Returns Rocky SDK name.
	 * 
	 * @return
	 */
	public static String getSdkName()
	{
		return SDK_NAME;
	}

	/**
	 * Always the application context.
	 * 
	 * @return
	 */
	public Context getContext()
	{
		return context;
	}

	/**
	 * Returns client info.
	 * 
	 * @return
	 */
	public ClientInfo getClientInfo()
	{
		return clientInfo;
	}

	/**
	 * 是否需要显示Banner广告
	 * 
	 * @return
	 */
	public boolean hasAdBanner()
	{
		return hasAdBanner;
	}

	/**
	 * 是否需要显示积分墙广告
	 * 
	 * @return
	 */
	public boolean hasAdPointsWall()
	{
		return hasAdPointsWall;
	}

	/**
	 * www.wo2b.com文件的根目录
	 * 
	 * @return
	 */
	public static String getWo2bDir()
	{
		return WO2B_DIR;
	}

	/**
	 * 临时文件目录, 临时策略-->用以"伪装"保存用户信息
	 * 
	 * @return
	 */
	public static String getWo2bTempDir()
	{
		return WO2B_DIR_TEMP;
	}

	/**
	 * 缓存目录
	 * 
	 * @return
	 */
	public static String getWo2bCacheDir()
	{
		return WO2B_DIR_CACHE;
	}

	public static class Builder
	{

		private Context context;
		private ClientInfo clientInfo;
		private boolean hasAdBanner;
		private boolean hasAdPointsWall;

		public Builder(Context application)
		{
			this.context = application;
			this.clientInfo = ClientInfo.DEFAULT_CLIENT;
		}

		public Builder clientInfo(ClientInfo clientInfo)
		{
			this.clientInfo = clientInfo;
			return this;
		}

		/**
		 * 是否需要显示Banner广告
		 * 
		 * @param hasAdBanner
		 * @return
		 */
		public Builder hasAdBanner(boolean hasAdBanner)
		{
			this.hasAdBanner = hasAdBanner;
			return this;
		}

		/**
		 * 是否需要显示积分墙广告
		 * 
		 * @param hasAdPointsWall
		 * @return
		 */
		public Builder hasAdPointsWall(boolean hasAdPointsWall)
		{
			this.hasAdPointsWall = hasAdPointsWall;
			return this;
		}
		
		public RockyConfig build()
		{
			return new RockyConfig(this);
		}

	}
	
}
