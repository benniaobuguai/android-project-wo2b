package com.wo2b.tu123.ui.image;

import java.io.File;

import opensource.component.imageloader.cache.disc.naming.Md5FileNameGenerator;

/**
 * 图片辅助类
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class ImageHelper
{
	
	/**
	 * 返回缓存的路径
	 * 
	 * @param cacheDir
	 * @param requestUrl
	 * @return
	 */
	public static final String getCachePath(String cacheDir, String requestUrl)
	{
		Md5FileNameGenerator md5 = new Md5FileNameGenerator();
		String filename = md5.generate(requestUrl);
		String imageUri = cacheDir + "/" + filename;
		
		return imageUri;
	}
	
	/**
	 * 返回缓存的文件
	 * 
	 * @param cacheDir
	 * @param requestUrl
	 * @return
	 */
	public static final File getCacheFile(String cacheDir, String requestUrl)
	{
		return new File(getCachePath(cacheDir, requestUrl));
	}
	
}
