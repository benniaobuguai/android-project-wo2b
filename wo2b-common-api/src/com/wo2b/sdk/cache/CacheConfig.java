package com.wo2b.sdk.cache;

import java.io.File;

/**
 * 缓存等参数配置
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-6
 */
public class CacheConfig
{

	/**
	 * 数据缓存默认值
	 */
	public static final int MAX_DATA_CACHE_SIZE = 3 * 1024 * 1024;

	/**
	 * 图片缓存默认值
	 */
	public static final int MAX_IMAGE_CACHE_SIZE = 7 * 1024 * 1024;

	/**
	 * TODO: Must
	 * 
	 * @return
	 */
	@Deprecated
	public static File getCacheDir()
	{
		return null;
	}

}
