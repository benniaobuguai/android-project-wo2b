package com.wo2b.wrapper.component.download;

import java.io.File;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.MimeTypeMap;

/**
 * 下载器, 不推荐使用. 推荐使用api包下的DownloadManager类
 * 
 * @author Rocky
 * 
 */
@Deprecated
public class DownloadFactory
{
	
	private static final String TAG = "Rocky.Download";

	private static byte[] mLock = new byte[0];

	private static DownloadFactory mInstance = null;

	private DownloadManager mDownloadManager = null;
	private HashMap<Long, String> mTask = null;
	
	private DownloadFactory(Context context)
	{
		mTask = new HashMap<Long, String>();

		mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static DownloadFactory getInstance(Context application)
	{
		if (mInstance == null)
		{
			synchronized (mLock)
			{
				if (mInstance == null)
				{
					mInstance = new DownloadFactory(application);
				}
			}
		}

		return mInstance;
	}
	
	public DownloadManager getDownloadManager()
	{
		return mDownloadManager;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void downloadApk(String requestUrl, String dir, String filename)
	{
		Uri resource = Uri.parse(requestUrl);
		DownloadManager.Request request = new DownloadManager.Request(resource);
		request.setAllowedNetworkTypes(Request.NETWORK_MOBILE | Request.NETWORK_WIFI);
		request.setAllowedOverRoaming(false);

		//设置文件类型  
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(requestUrl));
		request.setMimeType(mimeString);

		//在通知栏中显示   
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
		{
			request.setShowRunningNotification(true);
		}
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}

		// 显示下载界面
		request.setVisibleInDownloadsUi(true);
		//sdcard的目录下的download文件夹  /**AppConfig.DIR_APK*/
		request.setDestinationInExternalPublicDir("/download/", filename + ".apk");
		request.setTitle(filename + "");

		long id = mDownloadManager.enqueue(request);
		mTask.put(id, filename + "");
	}

	/**
	 * 把任务添加进队列
	 * 
	 * @param taskId
	 * @param title
	 */
	public void addTask(long taskId, String title)
	{
		this.mTask.put(taskId, title);
	}

	/**
	 * 返回任务队列
	 * 
	 * @return
	 */
	public HashMap<Long, String> getTask()
	{
		return mTask;
	}

	/**
	 * 是否在任务队列中
	 * 
	 * @param taskId
	 * @return
	 */
	public boolean containsTask(long taskId)
	{
		return mTask.containsKey(taskId);
	}

	/**
	 * 移除当前任务
	 * 
	 * @param taskId
	 */
	public void remove(long taskId)
	{
		this.mTask.remove(taskId);
	}

	/**
	 * 清空任务队列
	 */
	public void clear()
	{
		this.mTask.clear();
	}
	
	/**
	 * 安装APK.
	 * 
	 * @param context
	 * @param file
	 */
	public static void installApk(Context context, File file)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

}
