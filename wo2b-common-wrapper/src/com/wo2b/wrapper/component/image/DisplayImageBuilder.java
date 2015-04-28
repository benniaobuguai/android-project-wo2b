package com.wo2b.wrapper.component.image;

import opensource.component.imageloader.core.DisplayImageOptions;

import com.wo2b.wrapper.R;

/**
 * 图片显示设置
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-17
 */
public class DisplayImageBuilder
{
	
	/**
	 * 默认, 正方形图标
	 * 
	 * @return
	 */
	public static DisplayImageOptions.Builder getDefault()
	{
		return new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.image_loading_small)
			.showImageForEmptyUri(R.drawable.image_not_found_small)
			.showImageOnFail(R.drawable.image_not_found_small)
			.cacheInMemory(true);
	}
	
	/**
	 * 长方形图标
	 * 
	 * @return
	 */
	public static DisplayImageOptions.Builder getDefaultLong()
	{
		return new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.image_loading_long)
			.showImageForEmptyUri(R.drawable.image_not_found_long)
			.showImageOnFail(R.drawable.image_not_found_long)
			.cacheInMemory(true);
	}
	
}
