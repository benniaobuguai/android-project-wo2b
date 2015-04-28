package com.wo2b.sdk.core.cache;

import com.wo2b.sdk.core.RockyConfig;

/**
 * 应用目录抽象类
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-9-14
 */
public class RockyCacheFactory
{

	/**
	 * www.wo2b.com系统级基目录
	 */
	private static final String wo2bDir = RockyConfig.WO2B_DIR + "/";

	/**
	 * 应用基目录
	 */
	private String appDir;

	/**
	 * www.wo2b.com缺省的目录地址
	 */
	//public static final String WO2B_DIR = RockyConfig.WO2B_DIR;

	/**
	 * 构造函数
	 * 
	 * @param appName 应用程序名称
	 */
	public RockyCacheFactory(String appName)
	{
		//this.wo2bDir = WO2B_DIR + "/";
		this.appDir = wo2bDir + "/" + appName + "/";
	}

	/**
	 * 构造函数, 不推荐使用此构造方法, 请使用{@linkp AbsRockyCacheFactory#AbsRockyCacheFactory(String)}
	 * 
	 * @param wo2bDir 基础目录
	 * @param appName 应用程序名称
	 */
	@Deprecated
	public RockyCacheFactory(String wo2bDir, String appName)
	{
		//this.wo2bDir = wo2bDir + "/";
		this.appDir = wo2bDir + "/" + appName + "/";
	}
	
//	/**
//	 * 扩展目录
//	 */
//	public static interface ExtraDir
//	{
//
//		public static final String USERS = "users/";
//
//		public static final String ALBUM = "album/";
//
//		public static final String IMAGE = "image/";
//
//		public static final String ADS = "ads/";
//	}

	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	// ### 系统级目录信息
	/**
	 * 系统级基目录
	 * 
	 * @return
	 */
	public static String getWo2bDir()
	{
		return wo2bDir;
	}

	/**
	 * 系统级用户信息目录
	 * 
	 * @return
	 */
	public static String getWo2bUserDir()
	{
		return wo2bDir + "user/";
	}

	/**
	 * 系统级缓存目录
	 * 
	 * @return
	 */
	public static String getWo2bCacheDir()
	{
		return wo2bDir + "cache/";
	}

	/**
	 * 系统级图片目录
	 * 
	 * @return
	 */
	public static String getWo2bImageDir()
	{
		return wo2bDir + "image/";
	}

	/**
	 * 系统级下载目录
	 * 
	 * @return
	 */
	public static String getWo2bDownloadDir()
	{
		return wo2bDir + "download/";
	}

	// -------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------
	// ### 应用级目录信息
	/**
	 * 应用基目录
	 * 
	 * @return
	 */
	public String getAppDir()
	{
		return this.appDir;
	}

	/**
	 * 应用缓存目录
	 * 
	 * @return
	 */
	public String getAppCacheDir()
	{
		return this.appDir + "cache/";
	}

	/**
	 * 临时缓存目录
	 * 
	 * @return
	 */
	public String getAppTempDir()
	{
		return this.appDir + "temp/";
	}

	/**
	 * 应用图片目录
	 * 
	 * @return
	 */
	public String getAppImageDir()
	{
		return this.appDir + "image/";
	}

	/**
	 * 应用下载目录
	 * 
	 * @return
	 */
	public String getAppDownloadDir()
	{
		return this.appDir + "download/";
	}

	/**
	 * 应用用户信息
	 * 
	 * @return
	 */
	public String getAppUserDir()
	{
		return this.appDir + "user/";
	}

	/**
	 * 备份目录
	 * 
	 * @return
	 */
	public String getAppBackupDir()
	{
		return this.appDir + "backup/";
	}

}
