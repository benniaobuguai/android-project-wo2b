package com.wo2b.tu123.business.localalbum;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.wo2b.tu123.business.base.UserDatabaseHelper;
import com.wo2b.tu123.model.localalbum.FocusItemInfo;
import com.wo2b.tu123.model.localalbum.LocalImage;

/**
 * 单实例用于存储当前所有照片信息, 根据路径分组, 接收媒体库刷新的通知.
 * 临时策略: 查询出所有的照片信息, 按照路径进行排序.
 * 遍历游标, 填充数据列表.
 * @author 笨鸟不乖
 * 
 */
public class LocalImageFactory
{

	public static final String TAG = "LocalImageFactory";

	private static final byte[] mLock = new byte[0];
	private static final byte[] mFocusLock = new byte[0];

	private static volatile LocalImageFactory mInstance = null;
	
	private static final String[] STORE_IMAGES = {
		MediaStore.Images.Media._ID,
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.TITLE,
		MediaStore.Images.Media.MIME_TYPE,
		MediaStore.Images.Media.SIZE,
		MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
		MediaStore.Images.Media.BUCKET_ID,
		MediaStore.Images.Media.LATITUDE,
		MediaStore.Images.Media.LONGITUDE
	};
	
	private static final String SORT_ORDER = MediaStore.Images.Media.BUCKET_ID;

	private Context mContext = null;
	
	/** KO榜列表 */
	private List<FocusItemInfo> mFocusItemList = null;
	
	/** 正在被外部使用的列表 */
	private List<LocalImage> mFocusList = null;
	/** 正在后台扫描的列表 */
	private List<LocalImage> mBackgroundList = null;
	
	// private HashMap<String, List<LocalImage>> mFocusList = null;
	// private HashMap<String, List<LocalImage>> mBackgroundList = null;
	
	/**
	 * 私有构造函数
	 * 
	 * @param context
	 */
	private LocalImageFactory(Context context)
	{
		this.mContext = context;
		this.mFocusList = new ArrayList<LocalImage>();
		this.mBackgroundList = new ArrayList<LocalImage>();
	}

	/**
	 * 返回LocalImageFactory实例
	 * 
	 * @return
	 */
	public static LocalImageFactory getInstance(Context application)
	{
		if (mInstance == null)
		{
			synchronized (mLock)
			{
				if (mInstance == null)
				{
					mInstance = new LocalImageFactory(application);
				}
			}
		}

		return mInstance;
	}
	
	/**
	 * 返回关注的List.
	 * 
	 * @return
	 */
	public List<LocalImage> getFocusList()
	{
		if (mFocusList == null || mFocusList.isEmpty())
		{
			synchronized (mFocusLock)
			{
				if (mFocusList == null || mFocusList.isEmpty())
				{
					mFocusItemList = loadFocusItemList();
					mFocusList = loadFocusList(mFocusItemList);
				}
			}
		}
		
		return mFocusList;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<FocusItemInfo> getFocusItemList()
	{
		if (mFocusItemList == null)
		{
			synchronized (mFocusLock)
			{
				mFocusItemList = loadFocusItemList();
			}
		}
		
		return mFocusItemList;
	}

	/**
	 * 
	 * @return
	 */
	private List<FocusItemInfo> loadFocusItemList()
	{
		LocalAlbumBiz biz = new LocalAlbumBiz(new UserDatabaseHelper(mContext));
		List<FocusItemInfo> koItemList = biz.getFocusList();
		
		return koItemList;
	}

	/**
	 * 策略中使用MediaStore.Images.Media.BUCKET_ID当作是相册ID进行分组, 返回所有的相册.
	 * 
	 * @return
	 */
	private List<LocalImage> loadFocusList(List<FocusItemInfo> focusItemInfo)
	{
		if (focusItemInfo == null)
		{
			return new ArrayList<LocalImage>();
		}
		
		List<LocalImage> imageList = new ArrayList<LocalImage>();
		List<LocalImage> tempList = new ArrayList<LocalImage>();
		
		int koItemCount = focusItemInfo.size();
		FocusItemInfo itemInfo = null;
		LocalImage albumItem = null;
		for (int i = 0; i < koItemCount; i++)
		{
			itemInfo = focusItemInfo.get(i);
			
			String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ? and " + MediaStore.Images.Media.DATA
					+ " like ? ";
			
			// 优先使用精准"=", 接着再次用模糊确认, 达到最终精准定位.
			String[] selectionArgs = new String[] { itemInfo.getBucket_display_name(), "%" + itemInfo.getData() + "%" };
			
			tempList = query(selection, selectionArgs);
			
			if (tempList == null || tempList.isEmpty())
			{
				continue;
			}
			
			albumItem = tempList.get(0);
			albumItem.setImageCount(tempList.size());
			albumItem.setBeautifulName(itemInfo.getBeautiful_name());
			albumItem.setItemInfo(itemInfo);
			
			imageList.add(albumItem);
		}
		
		return imageList;
	}
	
	/**
	 * 是否已经存在当前关注的Item.
	 * 
	 * @param itemInfo
	 * @return
	 */
	public boolean isFocusItemExists(FocusItemInfo itemInfo)
	{
		if (mFocusItemList == null || mFocusItemList.isEmpty())
		{
			return false;
		}
		
		int focusItemCount = mFocusItemList.size();
		FocusItemInfo tempItem = null;
		for (int i = 0; i < focusItemCount; i++)
		{
			tempItem = mFocusItemList.get(i);
			if (itemInfo.getData().equalsIgnoreCase(tempItem.getData()))
			{
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 添加至当前关注
	 * 
	 * @param itemInfo
	 * @return
	 */
	public boolean addFocusItem(FocusItemInfo itemInfo)
	{
		// 要去重复的地址
		LocalAlbumBiz biz = new LocalAlbumBiz(new UserDatabaseHelper(mContext));
		if (biz.create(itemInfo) > 0)
		{
			// 删除成功后, 需要更新当前缓存
			synchronized (mFocusLock)
			{
				// 添加在第一位
				mFocusItemList.add(0, itemInfo);
				
				// 清空缓存的相册列表
				mFocusList.clear();
			}
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * 删除当前关注的相册
	 * 
	 * @param itemInfo
	 * @return
	 */
	public boolean removeFocusItem(FocusItemInfo itemInfo)
	{
		LocalAlbumBiz biz = new LocalAlbumBiz(new UserDatabaseHelper(mContext));
		if (biz.delete(itemInfo) > 0)
		{
			// 删除成功后, 需要更新当前缓存
			synchronized (mFocusLock)
			{
				//mFocusItemList.clear();
				mFocusItemList.remove(itemInfo);
				mFocusList.clear();
			}
			
			return true;
		}
		
		return false;
	}
	
	
	
	
	
	
	
	/**
	 * 策略中使用MediaStore.Images.Media.BUCKET_ID当作是相册ID进行分组, 返回所有的相册.
	 * 
	 * @return
	 */
	public List<LocalImage> queryAlbum()
	{
		String selection = " 1=1) group by (" + MediaStore.Images.Media.BUCKET_ID + "";

		return query(selection, null);
	}
	
	/**
	 * 策略中使用MediaStore.Images.Media.BUCKET_ID当作是相册ID进行分组, 返回所有的相册.
	 * 
	 * @return
	 */
	public List<LocalImage> getCustomAlbumList_BAK()
	{
		LocalAlbumBiz biz = new LocalAlbumBiz(new UserDatabaseHelper(mContext));
		List<FocusItemInfo> koItemList = biz.getFocusList();
		
		List<LocalImage> imageList = new ArrayList<LocalImage>();
		List<LocalImage> tempList = new ArrayList<LocalImage>();
		
		int koItemCount = koItemList.size();
		FocusItemInfo itemInfo = null;
		LocalImage albumItem = null;
		for (int i = 0; i < koItemCount; i++)
		{
			itemInfo = koItemList.get(i);
			
			String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ? and " + MediaStore.Images.Media.DATA
					+ " like ? " + " ) group by (" + MediaStore.Images.Media.BUCKET_ID + "";
			
			// 优先使用精准"=", 接着再次用模糊确认, 达到最终精准定位.
			String[] selectionArgs = new String[] { itemInfo.getBucket_display_name(), "%" + itemInfo.getData() + "%" };
			
			tempList = query(selection, selectionArgs);
			
			if (tempList == null || tempList.isEmpty())
			{
				continue;
			}
			
			int tempLength = tempList.size();
			for (int j = 0; j < tempLength; j++)
			{
				albumItem = tempList.get(j);
				albumItem.setBeautifulName(itemInfo.getBeautiful_name());
				albumItem.setItemInfo(itemInfo);
			}
			
			imageList.addAll(tempList);
		}
		
		return imageList;
	}

	/**
	 * 策略中使用MediaStore.Images.Media.BUCKET_ID当作是相册ID进行分组, 返回当前相册下所有照片.
	 * 
	 * @param albumId
	 * @return
	 */
	public List<LocalImage> queryImagesByAlbumId(String albumId)
	{
		String selection = MediaStore.Images.Media.BUCKET_ID + " =? ";
		String[] selectionArgs = new String[] { albumId };

		return query(selection, selectionArgs);
	}

	/**
	 * 返回所有照片信息
	 * 
	 * @return
	 */
	public List<LocalImage> queryAllImages()
	{
		return query(null, null);
	}

	/**
	 * 根据参数返回照片信息, 为空返回所有照片信息.
	 * 
	 * @return
	 */
	private List<LocalImage> query(String selection, String[] selectionArgs)
	{
		List<LocalImage> imageList = new ArrayList<LocalImage>();
		
		Cursor cursor = mContext.getContentResolver().query
		(
			MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
			STORE_IMAGES,
			selection, 
			selectionArgs, 
			SORT_ORDER
		);
		
		if (cursor != null)
		{
			LocalImage imageInfo = null;
			while (cursor.moveToNext())
			{
				imageInfo = new LocalImage();

				long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID));
				String data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
				String displayName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
				String title = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.TITLE));
				String mimeType = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));
				long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.SIZE));
				String bucketDisplayName = cursor.getString(cursor
						.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
				String bucketId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
				double latitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LATITUDE));
				double longitude = cursor.getDouble(cursor.getColumnIndex(MediaStore.Images.Media.LONGITUDE));

				imageInfo.setId(id);
				imageInfo.setData(data);
				imageInfo.setDisplayName(displayName);
				imageInfo.setTitle(title);
				imageInfo.setMimeType(mimeType);
				imageInfo.setSize(size);
				imageInfo.setBucketDisplayName(bucketDisplayName);
				imageInfo.setBucketId(bucketId);
				imageInfo.setLatitude(latitude);
				imageInfo.setLongitude(longitude);

				imageList.add(imageInfo);
			}

			cursor.close();
		}
		
		return imageList;
	}
	
	
//	public HashMap<String, List<LocalImage>> getHashMap()
//	{
//		List<LocalImage> imageList = query();
//		
//		LocalImage imageInfo = null;
//		int count = imageList.size();
//		for (int i = 0; i < count; i++)
//		{
//			imageInfo = imageList.get(i);
//			
//			if(mCurrentList.containsKey(imageInfo.getBucketId()))
//			{
//				// 已经添加过了.
//				
//			}
//			
//			
//		}
//		
//		Collections.
//	}

}
