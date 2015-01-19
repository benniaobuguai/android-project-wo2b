package com.wo2b.sdk.database;

import java.io.File;
import java.io.InputStream;

import android.content.Context;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.common.util.io.FileUtils;

/**
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-7
 */
public class DatabaseLoader
{

	private static final String TAG = "Rocky.DatabaseLoader";

	public DatabaseLoader(Context context)
	{

	}

	/**
	 * 返回数据库文件
	 * 
	 * @param dbname
	 * @return
	 */
	public static File getDatabaseFile(Context context, String dbname)
	{
		return context.getDatabasePath(dbname);
	}

	/**
	 * 读取输入流, 创建数据库文件.
	 * 
	 * @param context 上下文
	 * @param targetDbName 目标数据库名称
	 * @param is 文件输入流
	 */
	@Deprecated
	public static void loadDefault(Context context, String targetDbName, InputStream is)
	{
		File file = context.getDatabasePath(targetDbName);
		if (file.exists())
		{
			// 文件存在, 直接路过此步骤.
			return;
		}

		Log.I(TAG, "Load default database [" + targetDbName + "]");

		FileUtils.writeFile(file, is, false);
	}

	/**
	 * 读取输入流, 创建数据库文件.
	 * 
	 * @param context 上下文
	 * @param rawResId 资源id
	 * @param targetDbName 目标数据库名称
	 */
	public static void loadDefault(Context context, int rawResId, String targetDbName)
	{
		File file = context.getDatabasePath(targetDbName);
		if (file.exists())
		{
			// 文件存在, 直接路过此步骤.
			return;
		}

		InputStream is = context.getResources().openRawResource(rawResId);
		Log.I(TAG, "Load default database [" + targetDbName + "]");

		FileUtils.writeFile(file, is, false);
	}

}
