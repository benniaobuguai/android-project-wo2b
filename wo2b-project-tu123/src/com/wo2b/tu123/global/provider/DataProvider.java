package com.wo2b.tu123.global.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.business.image.AlbumBiz;
import com.wo2b.tu123.business.image.Module;
import com.wo2b.tu123.model.global.XGroup;
import com.wo2b.tu123.model.image.AlbumInfo;

/**
 * Data Provider.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public final class DataProvider
{
	
	private static final String TAG = "Rocky.DataProvider";
	
	// ----------------------------------------------------------------------------------------
	// 针对数据进行优化的方案
	// 1. 加载首页相册数据, 全部缓存起来
	// 2. 加载广场数据, 全部缓存起来
	// 3. 加载本地相册, 全部缓存, 并且监听媒体数据变化, 实时后台更新数据
	// 4. 插件库, 暂时选择的方案是每次查询的时候就进行遍历
	// 5.
	// ----------------------------------------------------------------------------------------
	
	private static final byte[] mLock = new byte[0];
	private static final byte[] mCategoryLock = new byte[0];
	private static final byte[] mChapterLock = new byte[0];
	// private static final byte[] mPluginLock = new byte[0];
	
	private static volatile DataProvider mInstance = null;
	
	private Context mContext;
	private List<AlbumInfo> mCategoryList;
	
	// ----------------------------------------------------------------------------------------
	// &&&&&&&&&&&&&& 章节内容
	private List<XGroup> mChapterList; // 章节列表
	private List<HashMap<String, List<AlbumInfo>>> mChapterContentList; // 章节内容列表
	// ----------------------------------------------------------------------------------------
	
	/**
	 * 私有构造函数
	 * 
	 * @param application
	 */
	private DataProvider()
	{
		mCategoryList = new ArrayList<AlbumInfo>();
		mChapterList = new ArrayList<XGroup>();
		mChapterContentList = new ArrayList<HashMap<String, List<AlbumInfo>>>();
	}

	/**
	 * 返回DataProvider对象
	 * 
	 * @param context
	 * @return
	 */
	public static DataProvider getInstance()
	{
		if (mInstance == null)
		{
			synchronized (mLock)
			{
				if (mInstance == null)
				{
					mInstance = new DataProvider();
				}
			}
		}
		
		return mInstance;
	}
	
	public void init(Context application)
	{
		this.mContext = application;
	}
	
	/**
	 * 加载首页分类
	 */
	public void loadCategoryList(Context context)
	{
		synchronized (mCategoryLock)
		{
			// Log.I(TAG, "Load category start...");
			// SystemClock.sleep(10 * 1000);
			AlbumBiz albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(context));
			mCategoryList = albumBiz.queryByModule(Module.A);
			// Log.I(TAG, "Load category end...");
		}
	}
	
	/**
	 * 返回分类列表
	 * 
	 * @return
	 */
	public List<AlbumInfo> getCategoryList(Context context)
	{
		if (mCategoryList == null || mCategoryList.isEmpty())
		{
			loadCategoryList(context);
		}
		synchronized (mCategoryLock)
		{
			return mCategoryList;
		}
	}
	
	/**
	 * 加载广场相册
	 */
	public void loadChapterList(Context context)
	{
		synchronized (mChapterLock)
		{
			AlbumBiz albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(context));
			// 获取所有组名, 去重复, 得到实质分组.
			List<AlbumInfo> albumList = albumBiz.queryAllCategory(Module.H);
			
			// 迭代所有分组的ID
			HashMap<String, List<AlbumInfo>> albumMap = new HashMap<String, List<AlbumInfo>>();
			
			int count = albumList.size();
			AlbumInfo albumInfo = null;
			List<AlbumInfo> tempList;
			XGroup group = null;
			for (int i = 0; i < count; i++)
			{
				albumInfo = albumList.get(i);
				tempList = albumBiz.queryByCategory(albumInfo.getCategory());
				
				group = new XGroup();
				group.setGroupId(i);
				group.setGroupName(albumInfo.getCategory());
				mChapterList.add(group);
				albumMap.put(albumInfo.getCategory(), tempList);
				
				mChapterContentList.add(albumMap);
			}
		}
	}
	
	/**
	 * 返回广场分组
	 * 
	 * @return
	 */
	public List<XGroup> getChapterList(Context context)
	{
		if (mChapterList == null || mChapterList.isEmpty())
		{
			loadChapterList(context);
		}
		synchronized (mChapterLock)
		{
			return mChapterList;
		}
	}
	
	/**
	 * 返回广场相册
	 * 
	 * @return
	 */
	public List<HashMap<String, List<AlbumInfo>>> getChapterContentList()
	{
		synchronized (mChapterLock)
		{
			return mChapterContentList;
		}
	}
	
}