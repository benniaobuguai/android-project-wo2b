package com.wo2b.tu123.global;

import com.wo2b.sdk.core.cache.RockyCacheFactory;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-4
 */
public class AppCacheFactory extends RockyCacheFactory
{

	public AppCacheFactory()
	{
		super("tu123");
	}

	// -----------------------------------------------------------------------
	// ------------------ Application data directory.

	/**
	 * 扩展目录
	 */
	public static interface ExtraDir
	{

		public static final String USERS = "users/";

		public static final String ALBUM = "album/";

		public static final String IMAGE = "image/";

		public static final String ADS = "ads/";
	}

	/**
	 * 壁纸目录
	 * 
	 * @return
	 */
	public String getWallpaper()
	{
		return getAppDir() + "wallpaper/";
	}

}
