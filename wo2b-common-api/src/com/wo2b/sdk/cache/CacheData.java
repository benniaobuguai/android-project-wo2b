package com.wo2b.sdk.cache;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.util.LruCache;

import com.wo2b.sdk.assistant.log.Log;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-6-6
 * @Modify 2014-6-6
 */
public class CacheData
{

	private static final String TAG = "Global.CacheData";

	/**
	 * 存储大小可配置
	 */
	private static int MAX_CACHE_SIZE = CacheConfig.MAX_DATA_CACHE_SIZE;

	private static LruCache<String, Object> mCacheList = null;

	private static volatile CacheData mCacheData = null;

	private CacheData()
	{
		mCacheList = new LruCache<String, Object>(MAX_CACHE_SIZE);
		Log.I(TAG, "Cache data size: " + MAX_CACHE_SIZE);
	}

	public static CacheData getCacher()
	{
		if (mCacheData == null)
		{
			synchronized (CacheData.class)

			{
				if (mCacheData == null)
				{
					mCacheData = new CacheData();
				}
			}
		}

		return mCacheData;
	}

	/**
	 * 存储对象
	 * 
	 * @param key
	 * @param value
	 */
	public Object putObject(String key, Object value)
	{
		return mCacheList.put(key, value);
	}

	/**
	 * 获取某个对象
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(String key)
	{
		return (T) mCacheList.get(key);
	}

	/**
	 * 去掉某个对象的缓存
	 * 
	 * @param key
	 */
	public void remove(String key)
	{
		mCacheList.remove(key);
	}

	/**
	 * 是否存在此对象
	 * 
	 * @param key
	 * @return
	 */
	public boolean contains(String key)
	{
		return mCacheList.get(key) == null;
	}

	// ------------------------------------------------------------------------------------
	// --- 缓存列表数据
	/**
	 * 缓存列表数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> void putList(String key, List<T> value)
	{
		mCacheList.put(key, value);
	}

	/**
	 * 获取列表数据
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> ArrayList<T> getList(String key)
	{
		return getObject(key);
	}

	/**
	 * 缓存列表数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> void addToList(String key, List<T> value)
	{
		getList(key).addAll(value);
	}

	/**
	 * 缓存某个对象至列表最后
	 * 
	 * @param key
	 * @param value
	 */
	public <T> void addToList(String key, T value)
	{
		getList(key).add(value);
	}

	/**
	 * 缓存某个对象至列表指定位置
	 * 
	 * @param key
	 * @param position
	 * @param value
	 */
	public <T> void addToList(String key, int position, T value)
	{
		getList(key).add(position, value);
	}

	/**
	 * 设置存储容量, 在使用之前先设置缓存可用大小
	 * 
	 * @param maxSize
	 */
	public static void setMaxCacheSize(int maxSize)
	{
		MAX_CACHE_SIZE = maxSize;
	}

	/**
	 * 返回存储容量
	 * 
	 * @return
	 */
	public static int getMaxCacheSize()
	{
		return MAX_CACHE_SIZE;
	}

}
