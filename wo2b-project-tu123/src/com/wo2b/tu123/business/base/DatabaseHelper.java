package com.wo2b.tu123.business.base;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wo2b.sdk.assistant.log.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.model.image.PhotoInfo;

/**
 * 
 * @author Rocky
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper 
{
	
	private static final String TAG = DatabaseHelper.class.getSimpleName();
	
	public static final String DATABASE_NAME = "wo2b_tujie.db";
	
	public static final int DATABASE_VERSION = 1;
	
	private RuntimeExceptionDao<AlbumInfo, Long> albumDao = null;
	private RuntimeExceptionDao<PhotoInfo, Long> mPhotoDao;
	
	public DatabaseHelper(Context context)
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
			
			TableUtils.createTable(connectionSource, AlbumInfo.class);
			TableUtils.createTable(connectionSource, PhotoInfo.class);
			
			// 数据初始化
			initDatabase(db, connectionSource);
		}
		catch (SQLException e)
		{
			Log.E(TAG, "Can't create database.", e);
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * 数据初始化
	 * 
	 * @param db
	 * @param connectionSource
	 */
	private void initDatabase(SQLiteDatabase db, ConnectionSource connectionSource)
	{
		
	}
	
	/**
	 * 处理数据库版本升级
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion)
	{
		Log.I(TAG, "--------------------- onUpgrade ---------------------");
		int version = oldVersion;
		if (version < 4)
		{
			upgradeFor2(db);
			version = 3;
		}
		
		if (version != DATABASE_VERSION)
		{
			Log.W(TAG, "Destroying all old data.");
			try
			{
				Log.I(TAG, "--------------------- onUpgrade ---------------------");
				
				TableUtils.dropTable(connectionSource, AlbumInfo.class, true);
				TableUtils.dropTable(connectionSource, PhotoInfo.class, true);
				
				onCreate(db, connectionSource);
			}
			catch (SQLException e)
			{
				Log.E(TAG, "Can't drop databases", e);
				throw new RuntimeException(e);
			}
		}
		
	}
	
	/**
	 * 数据库升级至2.0
	 * 
	 * @param db
	 */
	private void upgradeFor2(SQLiteDatabase db)
	{
		db.beginTransaction();
		try
		{
			db.setTransactionSuccessful();
		}
		catch (android.database.SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			db.endTransaction();
		}
		
	}
	
	// ----------------------------------------------------------------------------------------------------------
	
	public RuntimeExceptionDao<AlbumInfo, Long> getAlbumDao()
	{
		if (albumDao == null)
		{
			albumDao = getRuntimeExceptionDao(AlbumInfo.class);
		}
		
		return albumDao;
	}
	
	public RuntimeExceptionDao<PhotoInfo, Long> getPhotoDao()
	{
		if (mPhotoDao == null)
		{
			mPhotoDao = getRuntimeExceptionDao(PhotoInfo.class);
		}
		
		return mPhotoDao;
	}
	
	/**
	 * 返回DatabaseHelper对象
	 * 
	 * @param context
	 * @return
	 */
	public static DatabaseHelper getDatabaseHelper(Context context)
	{
		return OpenHelperManager.getHelper(context, DatabaseHelper.class);
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