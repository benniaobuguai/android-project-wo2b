package com.wo2b.sdk.common.download;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wo2b.sdk.R;
import com.wo2b.sdk.common.util.CommonUtils;
import com.wo2b.sdk.common.util.http.AsyncHttpClient;
import com.wo2b.sdk.common.util.http.FileAsyncHttpResponseHandler;
import com.wo2b.sdk.common.util.io.FileUtils;
import com.wo2b.sdk.core.cache.RockyCacheFactory;

/**
 * 后台下载服务
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2014-10-10
 * @Modify 2015-4-28
 */
public class DownloadService extends Service
{

	public static final String TAG = "Rocky.DownloadService";

	/**
	 * 最多同时支持三个文件的下载
	 */
	public static final int MAX_TASK_COUNT = 3;

	/**
	 * 临时文件的扩展名
	 */
	public static final String TEMP_FILE_EXTENSION = "tmp";

	/**
	 * 开始
	 */
	public final static int EVENT_START = 0x1;

	/**
	 * 暂停
	 */
	public final static int EVENT_PAUSE = 0x2;

	/**
	 * 继续
	 */
	public final static int EVENT_RESUME = 0x3;

	/**
	 * 停止
	 */
	public final static int EVENT_CANCEL = 0x4;

	/**
	 * 就绪
	 */
	public final static int STATUS_PENDING = 1 << 0;

	/**
	 * 运行
	 */
	public final static int STATUS_RUNNING = 1 << 1;

	/**
	 * 暂停
	 */
	public final static int STATUS_PAUSED = 1 << 2;

	/**
	 * 成功
	 */
	public final static int STATUS_SUCCESSFUL = 1 << 3;

	/**
	 * 失败
	 */
	public final static int STATUS_FAILED = 1 << 4;

	/**
	 * 取消
	 */
	public final static int STATUS_CANCELED = 1 << 5;

	/**
	 * 不明确的错误
	 */
	public final static int ERROR_UNKNOWN = 1000;

    /**
     * Value of {@link #COLUMN_REASON} when a storage issue arises which doesn't fit under any
     * other error code. Use the more specific {@link #ERROR_INSUFFICIENT_SPACE} and
     * {@link #ERROR_DEVICE_NOT_FOUND} when appropriate.
     */
    public final static int ERROR_FILE_ERROR = 1001;

    /**
     * Value of {@link #COLUMN_REASON} when an HTTP code was received that download manager
     * can't handle.
     */
    public final static int ERROR_UNHANDLED_HTTP_CODE = 1002;

    /**
     * Value of {@link #COLUMN_REASON} when an error receiving or processing data occurred at
     * the HTTP level.
     */
    public final static int ERROR_HTTP_DATA_ERROR = 1004;

    /**
     * Value of {@link #COLUMN_REASON} when there were too many redirects.
     */
    public final static int ERROR_TOO_MANY_REDIRECTS = 1005;

    /**
     * Value of {@link #COLUMN_REASON} when there was insufficient storage space. Typically,
     * this is because the SD card is full.
     */
    public final static int ERROR_INSUFFICIENT_SPACE = 1006;

    /**
     * Value of {@link #COLUMN_REASON} when no external storage device was found. Typically,
     * this is because the SD card is not mounted.
     */
    public final static int ERROR_DEVICE_NOT_FOUND = 1007;

    /**
     * Value of {@link #COLUMN_REASON} when some possibly transient error occurred but we can't
     * resume the download.
     */
    public final static int ERROR_CANNOT_RESUME = 1008;

    /**
     * Value of {@link #COLUMN_REASON} when the requested destination file already exists (the
     * download manager will not overwrite an existing file).
     */
    public final static int ERROR_FILE_ALREADY_EXISTS = 1009;

    /**
     * Value of {@link #COLUMN_REASON} when the download has failed because of
     * {@link NetworkPolicyManager} controls on the requesting application.
     *
     * @hide
     */
    public final static int ERROR_BLOCKED = 1010;

    /**
     * Value of {@link #COLUMN_REASON} when the download is paused because some network error
     * occurred and the download manager is waiting before retrying the request.
     */
    public final static int PAUSED_WAITING_TO_RETRY = 1;

    /**
     * Value of {@link #COLUMN_REASON} when the download is waiting for network connectivity to
     * proceed.
     */
    public final static int PAUSED_WAITING_FOR_NETWORK = 2;

    /**
     * Value of {@link #COLUMN_REASON} when the download exceeds a size limit for downloads over
     * the mobile network and the download manager is waiting for a Wi-Fi connection to proceed.
     */
    public final static int PAUSED_QUEUED_FOR_WIFI = 3;

    /**
     * Value of {@link #COLUMN_REASON} when the download is paused for some other reason.
     */
    public final static int PAUSED_UNKNOWN = 4;
	
	
	
	// &&& Extra Key
	/**
	 * 下载地址
	 */
	public static final String EXTRA_DOWNLOAD_URL = "download_url";

	/**
	 * 下载文件名称
	 */
	public static final String EXTRA_FILE_NAME = "file_name";

	/**
	 * 文件类型
	 */
	public static final String EXTRA_MEDIA_TYPE = "media_type";

	/**
	 * 文件保存地址
	 */
	public static final String EXTRA_DESTINATION = "destination";

	/**
	 * 操作类型
	 */
	public static final String EXTRA_EVENT_TYPE = "event";

	/**
	 * 是否通知
	 */
	public static final String EXTRA_SHOW_NOTIFICATION = "show_notification";

	/**
	 * 进度条大小
	 */
	private static final int PROGRESSBAR_MAX = 100;

	private NotificationManager mNotificationManager;
	
	/**
	 * 存放历史下载历史记录
	 */
	private HashMap<String, Integer> mHistory = new HashMap<String, Integer>();

	private HashMap<String, Request> mRequestHashMap = new HashMap<String, Request>();

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.i(TAG, "DownloadService.onStartCommand()");

		if (intent != null)
		{

			// 根据不同的Intent, 执行相应的命令
			int eventType = intent.getIntExtra(DownloadService.EXTRA_EVENT_TYPE, -1);
			String downloadUrl = intent.getStringExtra(DownloadService.EXTRA_DOWNLOAD_URL);

			if (eventType == DownloadService.EVENT_START)
			{
				String filename = intent.getStringExtra(EXTRA_FILE_NAME);
				String destination = intent.getStringExtra(EXTRA_DESTINATION);
				// TODO: 文件保存地址
				startDownload(this, filename, downloadUrl, destination);
			}
			else if (eventType == DownloadService.EVENT_CANCEL)
			{
				cancelDownload(downloadUrl);
			}
			else if (eventType == DownloadService.EVENT_RESUME)
			{
				resumeDonwload();
			}
			else
			{
				Log.e(TAG, "Unknown download flag: " + eventType);
			}
		}

		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 开始下载
	 * 
	 * @param context 上下文
	 * @param fileName 由上层提供的文件名称, 如不提供直接根据下载地址生成文件名称
	 * @param downloadUrl 下载地址
	 * @param destination 保存地址
	 */
	public void startDownload(final Context context, final String fileName, final String downloadUrl,
			final String destination)
	{
		Log.i(TAG, "DownloadService.startDownload()");
		if (mRequestHashMap.containsKey(downloadUrl))
		{
			// 已经存在此任务
			Toast.makeText(getApplicationContext(), R.string.api_download_url_repeat, Toast.LENGTH_SHORT).show();
			return;
		}

		if (TextUtils.isEmpty(downloadUrl))
		{
			// 下载地址为空, 即无效的下载地址
			Toast.makeText(getApplicationContext(), R.string.api_download_url_invalid, Toast.LENGTH_SHORT).show();

			return;
		}

		
		final String realFileName = pickFileName(fileName, downloadUrl);
		
		// Modify: 临时修改方案, 直接针对生成的文件名, 根据时间去生成. 避免有可能出现的android EBUSY (Device or resource busy)异常.
		String saveFileName = "";
		int lastIndex = realFileName.lastIndexOf(".");
		if (lastIndex > 0)
		{
			saveFileName = realFileName.substring(0, lastIndex) + "_" + System.currentTimeMillis()
					+ realFileName.substring(lastIndex);
		}
		
		//final File file = createNewTempFile(realFileName, downloadUrl, destination);
		final File file = createNewTempFile(saveFileName, downloadUrl, destination);

		final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		// String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36";
		// asyncHttpClient.getHttpClient().getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent);
		
		asyncHttpClient.get(downloadUrl, new FileAsyncHttpResponseHandler(file)
		{

			Request mRequest;

			long mStartTime;

			/**
			 * 开始下载
			 */
			@Override
			public void onStart()
			{
				// 发送通知
				mRequest = new Request();
				mRequest.mStatus = STATUS_PENDING;
				mRequest.mDownloadUrl = downloadUrl;
				mRequest.mTitle = realFileName;
				mRequest.mAsyncHttpClient = asyncHttpClient;
				mRequestHashMap.put(downloadUrl, mRequest);

				notifyDownloadStart(context, mRequest);
			}

			/**
			 * 下载进度变化
			 */
			@Override
			public void onProgress(int bytesWritten, int totalSize)
			{
				super.onProgress(bytesWritten, totalSize);

				if (System.currentTimeMillis() - mStartTime > 1000)
				{
					mStartTime = System.currentTimeMillis();
					mRequest.mStatus = STATUS_RUNNING;
					notifyDownloadProgress(context, mRequest, bytesWritten, totalSize);
				}
			}

			/**
			 * 下载成功
			 */
			@Override
			public void onSuccess(int statusCode, Header[] headers, File file)
			{
				if (mRequest.mStatus == STATUS_RUNNING)
				{
					completeDownload(context, mRequest, file);
				}
			}

			/**
			 * 下载失败
			 */
			@Override
			public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file)
			{
				if (statusCode == HttpStatus.SC_FORBIDDEN)
				{
					// 403文件找不到

				}

				Toast.makeText(context, "下载失败", Toast.LENGTH_LONG).show();
			}

		});
	}
	
	/**
	 * 成功下载
	 * 
	 * @param context 上下文
	 * @param request 请求对象
	 * @param tempFile 临时已经下载完成的文件
	 */
	private void completeDownload(Context context, Request request, File tempFile)
	{
		// 下载完成后, 在任务栈中移除任务
		mRequestHashMap.remove(request.mDownloadUrl);
		
		File newFile = null;
		if (tempFile.exists())
		{
			String srcFileName = FileUtils.getFileNameWithoutExtension(tempFile.getPath());
			newFile = new File(tempFile.getParent(), srcFileName);

			tempFile.renameTo(newFile);
		}
		else
		{
			// 一般情况下, 不可能发生的情况
			Toast.makeText(context, R.string.api_download_file_not_found, Toast.LENGTH_LONG).show();

			return;
		}
		
		notifyDownloadCompleted(context, request, newFile);

		// 因下载过程取消时, 会回调onSuccess, 故此监听状态
		String fileName = newFile.getName();
		String extName = FileUtils.getFileExtension(fileName);
		if ("apk".equalsIgnoreCase(extName))
		{
			// 下载为apk文件
			Toast.makeText(context, "下载完成: " + newFile.getName(), Toast.LENGTH_LONG).show();
		}
		else
		{
			Toast.makeText(context, "下载完成", Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 取消下载
	 * 
	 * @param downloadUrl
	 */
	private void cancelDownload(String downloadUrl)
	{
		Request request = mRequestHashMap.get(downloadUrl);
		int notificationId = request.mNotificationId;
		if (request.mAsyncHttpClient != null)
		{
			request.mAsyncHttpClient.getHttpClient().getConnectionManager().shutdown();
			request.mAsyncHttpClient = null;
			request.mStatus = STATUS_CANCELED;

		}
		mRequestHashMap.remove(downloadUrl);

		notifyDownloadCancel(notificationId);
	}

	/**
	 * 继续下载, 暂不支持
	 */
	@Deprecated
	private void resumeDonwload()
	{

	}
	
	/**
	 * 生成文件名, 如果上层提供的文件名, 则直接返回文
	 * 
	 * @param fileName 由上层提供的文件名
	 * @param downloadUrl
	 * @return
	 */
	private String pickFileName(String fileName, String downloadUrl)
	{
		String finalFileName = fileName;
		if (TextUtils.isEmpty(fileName))
		{
			// 没有提供文件名, 直接取下载地址的文件名
			finalFileName = FileUtils.getFileName(downloadUrl);
		}

		return finalFileName;
	}

	/**
	 * 生成下载过程中使用的临时文件
	 * 
	 * @param fileName 文件名称
	 * @param downloadUrl 下载文件地址
	 * @param destination 保存文件的位置
	 * @return
	 */
	private File createNewTempFile(String fileName, String downloadUrl, String destination)
	{
		String tempFileName = fileName + "." + TEMP_FILE_EXTENSION;
		File file = null;
		if (TextUtils.isEmpty(destination))
		{
			// 保存地址为空, 使用默认的地址
			file = new File(RockyCacheFactory.getWo2bDownloadDir(), tempFileName);
		}
		else
		{
			file = new File(destination, tempFileName);
		}

		// 创建文件
		autoCreateFile(file);

		return file;
	}

	/**
	 * 如果不存在, 自动创建文件夹及文件; 如果存在, 直接返回.
	 * 
	 * @param file
	 */
	public void autoCreateFile(File file)
	{
		if (!file.exists())
		{
			File dir = file.getParentFile();
			if (!dir.exists())
			{
				dir.mkdirs();
			}

			try
			{
				file.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 根据文件类型返回图标资源id
	 * 
	 * @param extname
	 * @return
	 */
	public int getFileIconByFileType(String extname)
	{
		int resId = R.drawable.noti_file_default_icon;

		if ("apk".equalsIgnoreCase(extname))
		{
			resId = R.drawable.noti_file_default_icon;
		}
		else if ("zip".equalsIgnoreCase(extname))
		{
			resId = R.drawable.noti_file_default_icon;
		}

		return resId;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	
	// ===================================================================================================
	// ===================================================================================================
	/**
	 * 开始下载
	 * 
	 * @param context
	 * @param title
	 * @param icon
	 */
	private void notifyDownloadStart(Context context, Request request)
	{
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.api_download_notification);
		remoteViews.setProgressBar(R.id.noti_progressBar, PROGRESSBAR_MAX, 0, false);
		remoteViews.setImageViewResource(R.id.noti_icon, R.drawable.notification_remote_icon);
		remoteViews.setTextViewText(R.id.noti_file_name, request.mTitle);
		
		String host = CommonUtils.getHost(request.mDownloadUrl);
		if (CommonUtils.isWo2bHost(request.mDownloadUrl))
		{
			remoteViews.setTextViewText(R.id.noti_progressBarLeft, "www.wo2b.com");
		}
		else
		{
			remoteViews.setTextViewText(R.id.noti_progressBarLeft, host);
		}
		
		
		// 执行取消操作的PendingIntent, 向DownloadService发起取消下载的命令
		Intent cancelIntent = new Intent();
		cancelIntent.setClass(context, DownloadService.class);
		cancelIntent.putExtra(DownloadService.EXTRA_EVENT_TYPE, DownloadService.EVENT_CANCEL);
		cancelIntent.putExtra(DownloadService.EXTRA_DOWNLOAD_URL, request.mDownloadUrl);

		PendingIntent cancelPendingIntent = PendingIntent.getService(context, 100, cancelIntent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.noti_cancel, cancelPendingIntent);
		
		// 消息信息设置
		Notification notification = new Notification();
		notification.tickerText = request.mTitle;
		notification.icon = R.drawable.notification_icon;
		notification.contentView = remoteViews;
		// notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.flags = Notification.FLAG_ONGOING_EVENT;

		// 点击通知栏
		Intent intent = new Intent();
		intent.setAction("com.wo2b.download.AActivity");
		// intent.setClass(context, Download.class);

		notification.contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		
		// 生成通知ID
		int notificationId = new Random().nextInt(10000);

		mNotificationManager.notify(notificationId, notification);
		
		request.mNotification = notification;
		request.mNotificationId = notificationId;

	}
	
	/**
	 * 下载进度变化
	 */
	private void notifyDownloadProgress(final Context context, final Request request, final int bytesWritten,
			final int totalSize)
	{
		int progress = bytesWritten * 100 / totalSize;
		request.mNotification.contentView.setProgressBar(R.id.noti_progressBar, PROGRESSBAR_MAX, progress, false);
		request.mNotification.contentView.setTextViewText(R.id.noti_progressBarRight, progress + "%");
		
		mNotificationManager.notify(request.mNotificationId, request.mNotification);
	}

	/**
	 * 下载完成, 发送下载完成的广播
	 */
	private void notifyDownloadCompleted(final Context context, final Request request, final File file)
	{
		// 取消旧上一个通知
		int preNotificationId = request.mNotificationId;
		mNotificationManager.cancel(preNotificationId);

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.api_download_notification_ok);
		remoteViews.setImageViewResource(R.id.noti_icon, R.drawable.notification_icon);
		remoteViews.setTextViewText(R.id.noti_file_name, request.mTitle);
		remoteViews.setTextViewText(R.id.noti_progressBarLeft, context.getString(R.string.api_download_ok));
		remoteViews.setTextViewText(R.id.noti_progressBarRight, PROGRESSBAR_MAX + "%");
		remoteViews.setProgressBar(R.id.noti_progressBar, PROGRESSBAR_MAX, PROGRESSBAR_MAX, false);

		// 消息信息设置
		Notification notification = new Notification();
		notification.tickerText = request.mTitle;
		notification.icon = R.drawable.notification_icon;
		notification.contentView = remoteViews;
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		// 点击通知栏
		String extName = FileUtils.getFileExtension(file.getName());
		Intent intent = new Intent();
		if ("apk".equalsIgnoreCase(extName))
		{
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		else
		{
			intent.setAction("com.wo2b.download.AActivity");
		}
		notification.contentIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		mNotificationManager.notify(preNotificationId, notification);
	}

	/**
	 * 下载完成, 发送下载完成的广播
	 */
	private void notifyDownloadCancel(int notificationId)
	{
		mNotificationManager.cancel(notificationId);
	}
	
	/**
	 * 下载具体的请求类
	 * 
	 * @author 笨鸟不乖
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-10-11
	 */
	public static class Request
	{

		/**
		 * {@link AsyncHttpClient}对象
		 */
		public AsyncHttpClient mAsyncHttpClient;

		/**
		 * 当前通知对象
		 */
		public Notification mNotification;

		/**
		 * 当前通知的ID
		 */
		public int mNotificationId;

		/**
		 * 当前下载所处的状态
		 */
		public int mStatus;

		/**
		 * 文件下载地址
		 */
		public String mDownloadUrl;

		/**
		 * 标题
		 */
		public CharSequence mTitle;
		
		/**
		 * 文件
		 */
		//public File mFile;

	}

	/**
	 * 下载查询
	 * 
	 * @author 笨鸟不乖
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-10-11
	 */
	@Deprecated
	public static class Query
	{

	}

}
