package com.wo2b.tu123.business.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import android.content.Context;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.tu123.business.base.DatabaseHelper;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class RestoreManager implements IRestore
{
	
	private static final String TAG = "Rocky.Restore";
	
	/** 目标文件 */
	private File mTargetDatabase;
	
	/**
	 * 构造函数
	 * 
	 * @param context
	 */
	public RestoreManager(Context context)
	{
		mTargetDatabase = context.getDatabasePath(DatabaseHelper.DATABASE_NAME);
	}
	
	@Override
	public void restore(File source, File target) throws IOException
	{
		copy(source, target);
	}
	
	public void restore(InputStream is, File target) 
	{
		FileOutputStream fos = null;
		try
		{
			if (!target.exists())
			{
				File directory = target.getParentFile();
				if (!directory.isDirectory())
				{
					directory.mkdirs();
				}
				
				target.createNewFile();
			}

			fos = new FileOutputStream(target);
			
			int length = 0;
			byte[] buffer = new byte[1024];
			while ((length = is.read(buffer)) != -1)
			{
				fos.write(buffer, 0, length);
			}
			
			fos.flush();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (is != null)
			{
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	public void copy(File source, File target) throws IOException
	{
		// FileInputStream sourceStream = null;
		// FileInputStream targetStream = null;
		
		FileChannel inChannel = new FileInputStream(source).getChannel();
		FileChannel outChannel = new FileOutputStream(target).getChannel();
		try
		{
			inChannel.transferTo(0, inChannel.size(), outChannel);
		}
		catch (IOException e)
		{
			Log.E(TAG, "Failed to copy file. " + e.toString());
			e.printStackTrace();
		}
		finally
		{
			if (inChannel != null)
			{
				inChannel.close();
			}
			if (outChannel != null)
			{
				outChannel.close();
			}
			
			// if (targetStream != null)
			// {
			// targetStream.close();
			// }
			// if (sourceStream != null)
			// {
			// sourceStream.close();
			// }
		}
	}
	
	public File getTargetDatabase()
	{
		return mTargetDatabase;
	}
	
	public void setTargetDatabase(File targetDatabase)
	{
		this.mTargetDatabase = targetDatabase;
	}
	
}
