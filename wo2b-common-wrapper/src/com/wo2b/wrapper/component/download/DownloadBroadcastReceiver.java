package com.wo2b.wrapper.component.download;

import java.io.File;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.SystemClock;
import android.widget.Toast;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.wrapper.R;

/**
 * 下载广播接收器, 暂停使用, 后续完善下载功能时, 再打开.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2013-12-25
 */
@Deprecated
public class DownloadBroadcastReceiver extends BroadcastReceiver
{
	
	private static final String TAG = "Rocky.DownloadBroadcast";
	
	@Override
	public void onReceive(final Context context, Intent intent)
	{
		String action = intent.getAction();
		if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equalsIgnoreCase(action))
		{
			DownloadFactory utils = DownloadFactory.getInstance(context.getApplicationContext());
			long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
			
			if (utils.containsTask(downId))
			{
				Log.D("info", "Current down id is in download task list!!!");
				utils.remove(downId); // 从队列中移除
				
				DownloadManager.Query query = new DownloadManager.Query().setFilterById(downId);
				
				Cursor cursor = utils.getDownloadManager().query(query);
				if (cursor != null && cursor.moveToFirst())
				{
					// 只有一条记录
					final long id = cursor.getLong(cursor.getColumnIndex("_id"));
					final String local_filename = cursor.getString(cursor.getColumnIndex("local_filename"));
					final String mediaprovider_uri = cursor.getString(cursor.getColumnIndex("mediaprovider_uri"));
					final String destination = cursor.getString(cursor.getColumnIndex("destination"));
					final String title = cursor.getString(cursor.getColumnIndex("title"));
					final String description = cursor.getString(cursor.getColumnIndex("description"));
					final String uri = cursor.getString(cursor.getColumnIndex("uri"));
					final String status = cursor.getString(cursor.getColumnIndex("status"));
					final String hint = cursor.getString(cursor.getColumnIndex("hint"));
					final String media_type = cursor.getString(cursor.getColumnIndex("media_type"));
					final String total_size = cursor.getString(cursor.getColumnIndex("total_size"));
					final String last_modified_timestamp = cursor.getString(cursor.getColumnIndex("last_modified_timestamp"));
					final String bytes_so_far = cursor.getString(cursor.getColumnIndex("bytes_so_far"));
					final String local_uri = cursor.getString(cursor.getColumnIndex("local_uri"));
					final String reason = cursor.getString(cursor.getColumnIndex("reason"));
					
					// 关闭
					cursor.close();
					
					if ("application/vnd.android.package-archive".equalsIgnoreCase(media_type))
					{
						// 下载完成后直接打开安装界面
						final File file = new File(local_filename);
						final String message = context.getString(R.string.download_install, title + "");
						Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
						
						new Thread(new Runnable()
						{
							
							@Override
							public void run()
							{
								Log.I(TAG, "Ready to install [" + title + "]");
								SystemClock.sleep(200);
								DownloadFactory.installApk(context, file);
							}
						}).start();
					}
				}
			}
		}
	}
	
}