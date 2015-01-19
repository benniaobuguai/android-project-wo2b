package com.wo2b.wrapper.component.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import opensource.component.pulltorefresh.PullToRefreshBase.Mode;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo2b.sdk.common.download.DownloadManager;
import com.wo2b.sdk.common.util.AppHelper;
import com.wo2b.sdk.common.util.Utils;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.common.util.io.FileUtils;
import com.wo2b.sdk.core.cache.RockyCacheFactory;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyListFragmentActivity;
import com.wo2b.wrapper.component.image.DisplayImageBuilder;
import com.wo2b.xxx.webapp.manager.app.AppInfo;
import com.wo2b.xxx.webapp.manager.app.AppManager;

/**
 * Rocky作品集
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class Wo2bAppListActivity extends RockyListFragmentActivity<AppInfo>
{

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	private PackageManager mPackageManager;

	private AppManager mAppManager = new AppManager();
	
	private final int[] APP_LEVEL_ICON_ARRAY = {
		R.drawable.icon_level_yellow_1, R.drawable.icon_level_yellow_1,
		R.drawable.icon_level_yellow_2, R.drawable.icon_level_yellow_2,
		R.drawable.icon_level_yellow_3, R.drawable.icon_level_yellow_3,
		R.drawable.icon_level_yellow_4, R.drawable.icon_level_yellow_4,
		R.drawable.icon_level_yellow_5, R.drawable.icon_level_yellow_5,
	};

	/**
	 * 文件保存目录
	 */
	private String mSaveDirectory = RockyCacheFactory.getWo2bDownloadDir();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_wo2b_app_list);
		
		initView();
		
		mPackageManager = getPackageManager();
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = DisplayImageBuilder.getDefault()
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(6))
			.build();
		
		realExecuteFirstTime(null);
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.rocky_works);
		setPullMode(Mode.PULL_FROM_START);
	}

	@Override
	protected void realOnPostExecute()
	{
		super.realOnPostExecute();
	}

	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.wrapper_wo2b_app_list_item, parent, false);
		}
		ImageView image = ViewUtils.get(convertView, R.id.image);
		ImageView icon_app_level = ViewUtils.get(convertView, R.id.icon_app_level);
		TextView name = ViewUtils.get(convertView, R.id.name);
		TextView download_count = ViewUtils.get(convertView, R.id.download_count);
		TextView size = ViewUtils.get(convertView, R.id.size);
		TextView status = ViewUtils.get(convertView, R.id.status);
		LinearLayout operation = ViewUtils.get(convertView, R.id.operation);

		AppInfo appInfo = realGetItem(position);
		name.setText(getString(R.string.position_title, position + 1, appInfo.getAppname()));
		download_count.setText(formatDownloadCount(2000));
		size.setText(FileUtils.formatByte(appInfo.getSize()));

		// 服务器端为10分制, 分1-2, 3-4, 5-6, 7-8, 9-10共五个等级.
		icon_app_level.setImageResource(APP_LEVEL_ICON_ARRAY[appInfo.getRatings() - 1]);
		
		Drawable drawable = null;
		if (appInfo.isInstall())
		{
			// 已安装
			if (appInfo.isSelf())
			{
				// 该app为当前使用的app
				drawable = getResources().getDrawable(R.drawable.apk_used_now);
				status.setText(R.string.running_now);
			}
			else
			{
				drawable = getResources().getDrawable(R.drawable.apk_open);
				status.setText(R.string.open);
			}
		}
		else if (appInfo.isDownloaded())
		{
			// 已下载
			drawable = getResources().getDrawable(R.drawable.apk_install);
			status.setText(R.string.click_to_install);
		}
		else
		{
			// 待下载
			drawable = getResources().getDrawable(R.drawable.apk_download);
			status.setText(R.string.download);
		}

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		status.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 5));
		status.setCompoundDrawables(null, drawable, null, null);
		
		operation.setOnClickListener(new OnOperationClickListener(appInfo));

		mImageLoader.displayImage(appInfo.getLogo(), image, mOptions);

		return convertView;
	}

	/**
	 * 格式化下载次数的显示
	 * 
	 * @param count
	 * @return
	 */
	public String formatDownloadCount(long count)
	{
		if (count < 10000)
		{
			return getString(R.string.download_count1, count);
		}
		else
		{
			return getString(R.string.download_count2, count);
		}
	}
	
	private class OnOperationClickListener implements OnClickListener
	{
		
		private AppInfo mAppInfo;
		
		public OnOperationClickListener(AppInfo appInfo)
		{
			this.mAppInfo = appInfo;
		}
		
		@Override
		public void onClick(View v)
		{
			onOperationClick(mAppInfo);
		}
	}

	/**
	 * 子类重写此方法, 自行处理操作信息
	 */
	protected void onOperationClick(AppInfo appInfo)
	{
		if (appInfo.isInstall())
		{
			// 已安装
			if (appInfo.isSelf())
			{
				// 该app为当前使用的app, 提示正在使用
				// showToast(getString(R.string.running_app_now, appInfo.getAppname()));
				showToast(R.string.running_app_now);
			}
			else
			{
				// 执行打开操作
				try
				{
					AppHelper.launchApplication(this, appInfo.getPkgname());
				}
				catch (Exception e)
				{
					showToastOnUiThread(R.string.hint_install_failed);
				}
			}
		}
		else if (appInfo.isDownloaded())
		{
			// 已下载, 执行安装命令
			AppHelper.install(this, mSaveDirectory + appInfo.getAppname() + ".apk");
		}
		else
		{
			// 待下载
			DownloadManager.newInstance().download(this, appInfo.getUrl(), mSaveDirectory,
					appInfo.getAppname() + ".apk");
		}
	}

	@Override
	protected XModel<AppInfo> realOnPullDown(RockyParams params)
	{
		if (!Utils.hasInternet(this))
		{
			return XModel.netError();
		}

		List<AppInfo> cloudList = mAppManager.getRockyApps(mOffset, mCount);
		if (cloudList == null || cloudList.isEmpty())
		{
			return XModel.empty();
		}
		
		List<AppInfo> visibleList = new ArrayList<AppInfo>();
		final int totalCount = cloudList.size();
		AppInfo cloudAppInfo = null;
		for (int i = 0; i < totalCount; i++)
		{
			cloudAppInfo = cloudList.get(i);

			if (cloudAppInfo.isVisible())
			{
				try
				{
					PackageInfo packageInfo = mPackageManager.getPackageInfo(cloudAppInfo.getPkgname(),
							PackageManager.GET_ACTIVITIES);
					if (packageInfo == null)
					{
						cloudAppInfo.setInstall(false);
					}
					else
					{
						// 已经安装了, 进而判断是否为当前使用的app.
						if (this.getPackageName().equalsIgnoreCase(cloudAppInfo.getPkgname()))
						{
							cloudAppInfo.setSelf(true);
						}

						cloudAppInfo.setInstall(true);
					}
				}
				catch (NameNotFoundException e)
				{
					cloudAppInfo.setInstall(false);
				}

				File file = new File(mSaveDirectory + cloudAppInfo.getAppname() + ".apk");
				if (file.exists())
				{
					// 已经存在于本地
					cloudAppInfo.setDownloaded(true);
				}

				visibleList.add(cloudAppInfo);
			}
		}

		return XModel.list(visibleList);
	}

	@Override
	protected XModel<AppInfo> realOnPullUp(RockyParams params)
	{
		if (!Utils.hasInternet(this))
		{
			return XModel.netError();
		}
		
		return XModel.empty();
	}
	
}
