package com.wo2b.sdk.cache;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * 图片缓存处理
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-6
 */
public class CacheImage implements ImageCache
{

	private static final String TAG = "Global.CacheImage";

	private static int MAX_CACHE_SIZE = CacheConfig.MAX_IMAGE_CACHE_SIZE;

	private LruCache<String, Bitmap> mCache;

	public CacheImage()
	{
		this.mCache = new LruCache<String, Bitmap>(MAX_CACHE_SIZE)
		{

			@Override
			protected int sizeOf(String key, Bitmap bitmap)
			{
				return bitmap.getRowBytes() * bitmap.getHeight();
			}

		};
	}

	@Override
	public Bitmap getBitmap(String url)
	{
		System.out.println(Thread.currentThread().getName());

		// 从本地获取图片

		return mCache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap)
	{
		System.out.println(Thread.currentThread().getName());

		// 存储图片至本地

		mCache.put(url, bitmap);
	}

}
