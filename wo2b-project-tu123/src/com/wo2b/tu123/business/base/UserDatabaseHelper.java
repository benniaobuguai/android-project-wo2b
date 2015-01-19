package com.wo2b.tu123.business.base;

import java.io.File;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.wo2b.sdk.assistant.log.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wo2b.tu123.model.image.MyFavorites;
import com.wo2b.tu123.model.localalbum.FocusItemInfo;

/**
 * 用户数据
 * 
 */
public class UserDatabaseHelper extends OrmLiteSqliteOpenHelper
{
	
	private static final String TAG = UserDatabaseHelper.class.getSimpleName();
	
	public static final String DATABASE_NAME = "user.db";
	
	private static final int DATABASE_VERSION = 1;
	
	private RuntimeExceptionDao<MyFavorites, Long> myFavoritesDao;
	private RuntimeExceptionDao<FocusItemInfo, Long> focusItemInfo;
	
	public UserDatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	// -------------------------------------------------------------------------------------------------------------
	
	/**
	 * 创建数据库时会回调的接口，在这个方法里面完成对数据库表的创建
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		try
		{
			Log.I(TAG, "--------------------- onCreate ---------------------");
			TableUtils.createTable(connectionSource, MyFavorites.class);
			TableUtils.createTable(connectionSource, FocusItemInfo.class);
			
			initDatabase();
		}
		catch (SQLException e)
		{
			Log.E(TAG, "Can't create database", e);
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 数据初始化
	 */
	private void initDatabase_BAK()
	{
		RuntimeExceptionDao<FocusItemInfo, Long> koItemInfoDao = getFocusItemInfoDao();
		int length = BusinessData.LOCAL_FOCUS_PARENT_DIRECTORY.length;
		int itemCount = BusinessData.LOCAL_FOCUS_PARENT_DIRECTORY[0].length;

		String sdcard_root = null;

		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File sdcard_dir = Environment.getExternalStorageDirectory();
			sdcard_root = sdcard_dir.getPath();
		}
		
		FocusItemInfo itemInfo = null;
		for (int i = 0; i < length; i++)
		{
			itemInfo = new FocusItemInfo();
			itemInfo.setData(sdcard_root + BusinessData.LOCAL_ALBUM_FOCUS[i][0]);
			itemInfo.setBucket_display_name(BusinessData.LOCAL_ALBUM_FOCUS[i][1]);
			itemInfo.setBeautiful_name(BusinessData.LOCAL_ALBUM_FOCUS[i][1]);
			itemInfo.setIcon(BusinessData.LOCAL_ALBUM_FOCUS[i][2]);

			itemInfo.setSystem(true);
			// 系统自带索引1000开始, 用户添加的应当排在最前面.
			itemInfo.setOrder_by(1000 + i);

			koItemInfoDao.create(itemInfo);
		}
	}
	
	/**
	 * 数据初始化
	 */
	private void initDatabase()
	{
		RuntimeExceptionDao<FocusItemInfo, Long> koItemInfoDao = getFocusItemInfoDao();
		int length = BusinessData.LOCAL_ALBUM_FOCUS.length;
		int itemCount = BusinessData.LOCAL_ALBUM_FOCUS[0].length;
		
		FocusItemInfo itemInfo = null;
		for (int i = 0; i < length; i++)
		{
			itemInfo = new FocusItemInfo();
			itemInfo.setData(BusinessData.LOCAL_ALBUM_FOCUS[i][0]);
			itemInfo.setBucket_display_name(BusinessData.LOCAL_ALBUM_FOCUS[i][1]);
			itemInfo.setBeautiful_name(BusinessData.LOCAL_ALBUM_FOCUS[i][2]);
			itemInfo.setIcon(BusinessData.LOCAL_ALBUM_FOCUS[i][3]);
			
			itemInfo.setSystem(true);
			// 系统自带索引1000开始, 用户添加的应当排在最前面.
			itemInfo.setOrder_by(1000 + i);
			
			koItemInfoDao.create(itemInfo);
		}
	}
	
	/**
	 * 处理数据库版本升级
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		try
		{
			Log.I(TAG, "--------------------- onUpgrade ---------------------");
			TableUtils.dropTable(connectionSource, MyFavorites.class, true);
			TableUtils.dropTable(connectionSource, FocusItemInfo.class, true);
			
			onCreate(db, connectionSource);
		}
		catch (SQLException e)
		{
			Log.E(TAG, "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	/**
	 * 返回MyFavorites Dao
	 * 
	 * @return
	 */
	public RuntimeExceptionDao<MyFavorites, Long> getMyFavoritesDao()
	{
		if (myFavoritesDao == null)
		{
			myFavoritesDao = getRuntimeExceptionDao(MyFavorites.class);
		}
		
		return myFavoritesDao;
	}
	
	/**
	 * 返回KoItemInfo Dao
	 * 
	 * @return
	 */
	public RuntimeExceptionDao<FocusItemInfo, Long> getFocusItemInfoDao()
	{
		if (focusItemInfo == null)
		{
			focusItemInfo = getRuntimeExceptionDao(FocusItemInfo.class);
		}
		
		return focusItemInfo;
	}
	
	/**
	 * 返回UserDatabaseHelper对象
	 * 
	 * @param context
	 * @return
	 */
	public static UserDatabaseHelper getUserDatabaseHelper2(Context context)
	{
		return OpenHelperManager.getHelper(context, UserDatabaseHelper.class);
	}
	
	public static UserDatabaseHelper getUserDatabaseHelper(Context context)
	{
		return new UserDatabaseHelper(context);
	}
	
	/**
	 * 关闭数据库
	 */
	@Override
	public void close()
	{
		super.close();
	}
	
}