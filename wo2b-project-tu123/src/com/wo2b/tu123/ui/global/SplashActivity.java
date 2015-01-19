package com.wo2b.tu123.ui.global;

import java.io.File;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.assistant.upgrade.VersionInfo;
import com.wo2b.sdk.common.util.DateUtils;
import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.common.util.ManifestTools;
import com.wo2b.sdk.config.SdkConfig;
import com.wo2b.sdk.core.ClientInfo;
import com.wo2b.sdk.core.RockyConfig;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.wrapper.app.background.DaemonService;
import com.wo2b.wrapper.preference.RockyKeyValues;
import com.wo2b.wrapper.preference.XPreferenceManager;
import com.wo2b.tu123.R;
import com.wo2b.tu123.Wo2bApplication;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.business.data.RestoreManager;
import com.wo2b.tu123.global.provider.DataProvider;
import com.wo2b.tu123.ui.settings.LockViewActivity;

/**
 * SplashActivity
 * 
 * @author Rocky
 * 
 */
public class SplashActivity extends RockyFragmentActivity
{
	
	private static final String TAG = "Rocky.SplashActivity";
	
	/** Kept a few minute later. */
	private static final long MINI_WAIT_TIME = 3 * 1000;
	
	/** Start time. */
	private long mStartTime = 0;
	/** End time. */
	private long mEndTime = 0;
	
	public static final String ACTION_UNCHECK_LOCK = "com.rocky.action.uncheck_lock";
	private String mSavedPwd = null;
	//private String mAction = null;
	
	private XPreferenceManager mXPref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mXPref = XPreferenceManager.getInstance();
		Intent intent = getIntent();
		final String action = intent.getAction();
		if (ACTION_UNCHECK_LOCK.equalsIgnoreCase(action))
		{
			// 不检查锁
			if (Wo2bApplication.isEntry())
			{
				gotoHomeActivity();
			}
			else
			{
				setContentView(R.layout.splash_main);
				initView();
				setDefaultValues();
			}
		}
		else
		{
			// 检查锁
			mSavedPwd = mXPref.getString(RockyKeyValues.Keys.ENTRY_PASSWORD, "");
			
			if (TextUtils.isEmpty(mSavedPwd))
			{
				// UnLocked.
				if (Wo2bApplication.isEntry())
				{
					gotoHomeActivity();
				}
				else
				{
					setContentView(R.layout.splash_main);
					initView();
					setDefaultValues();
				}
			}
			else
			{
				// Locked.
				gotoLockViewActivity();
			}
		}
		Intent service = new Intent(this, DaemonService.class);
		startService(service);
	}
	
	@Override
	protected void initView()
	{
		
	}

	@Override
	protected boolean hasActionBar()
	{
		return false;
	};

	@Override
	protected void setDefaultValues()
	{
		mStartTime = SystemClock.elapsedRealtime();
		// Start System init.
		getSubHandler().post(mSystemInit);
	}
	
	@Override
	protected void bindEvents()
	{
		
	}
	
	/**
	 * Load system configuration, and other initialize works.
	 */
	private final Runnable mSystemInit = new Runnable()
	{
		
		@Override
		public void run()
		{
			Wo2bApplication.setEntry(true);
			systemLoader();
			// init data.
			initData();
			loadData();
			mEndTime = SystemClock.elapsedRealtime();
			afterSystemLoaded(mStartTime, mEndTime);
		}
		
		/**
		 * Init data.
		 */
		private void initData()
		{
			File target = getDatabasePath(DatabaseHelper.DATABASE_NAME);
			
			if (!target.exists())
			{
				// Database is not exist.
				RestoreManager manager = new RestoreManager(mContext);
				
				InputStream is = getContext().getResources().openRawResource(R.raw.wo2b_tujie);
				manager.restore(is, target);
			}
		}
		
		/**
		 * Load data.
		 */
		private void loadData()
		{
			DataProvider provider = DataProvider.getInstance();
			provider.init(Wo2bApplication.getRockyApplication());
			provider.loadCategoryList(getContext());
			provider.loadChapterList(getContext());
		}

		private void systemLoader()
		{
//			Context application = getApplicationContext();
//			VersionInfo versionInfo = ManifestTools.getVersionInfo(application);
//			String userAgent = mWebSettings.getUserAgentString();
//			
//			// Structure the ClientInfo.
//			ClientInfo clientInfo = new ClientInfo(application.getPackageName());
//			clientInfo.setAppicon(R.drawable.ic_launcher);
//			clientInfo.setAppname(getResources().getString(R.string.app_name));
//			clientInfo.setDeviceType(SdkConfig.Device.PHONE);
//			clientInfo.setDeviceName(android.os.Build.MODEL);
//			clientInfo.setAlias(android.os.Build.MODEL);
//			clientInfo.setSdkVersion(android.os.Build.VERSION.SDK_INT);
//			clientInfo.setMac(DeviceInfoManager.getMacAddress(application));
//			
//			// Webkit user-agent
//			clientInfo.setUserAgent(userAgent);
//			
//			if (versionInfo != null)
//			{
//				clientInfo.setVersionCode(versionInfo.getVersionCode());
//				clientInfo.setVersionName(versionInfo.getVersionName());
//			}
//			
//			// FIXME: Take attention...
//			clientInfo.addFlags(ClientInfo.FLAG_DEBUG | ClientInfo.FLAG_RELEASE);
//			// clientContext.addFlags(ClientContext.FLAG_DEBUG);
//			
//			// TODO: 广告
//			RockyConfig config = new RockyConfig.Builder(application)
//				.clientInfo(clientInfo)
//				.hasAdBanner(false)		// 显示积分Banner
//				.hasAdPointsWall(true)	// 显示积分墙
//				.build();
//			
//			RockySdk.getInstance().init(config);
			
			// Record the use total count.
			long loginCount = mXPref.getUseTotalCount();
			mXPref.putUseTotalCount(loginCount + 1); // Login count++
			
			// Record the use day count
			mXPref.putUseDayCount(mXPref.getUseDayCount() + 1);			
			
			Log.I(TAG, String.format("Entry %1s %2s Times.", getString(R.string.app_name), loginCount));
		}
		
		/**
		 * Do something after system loaded completed.
		 * 
		 * @param startTime
		 * @param endTime
		 */
		private void afterSystemLoaded(long startTime, long endTime)
		{
			long timeout = endTime - startTime - MINI_WAIT_TIME;
			Log.D(TAG, "Timeout: " + timeout);
			long delayMillis = 0;
			if (timeout < 0)
			{
				delayMillis = Math.abs(timeout);
			}
			Log.D(TAG, "DelayMillis: " + delayMillis);
			
			//showToast("DayCount: " + mXPref.getUseDayCount() + ", Date: "
			//		+ DateUtils.toString(mXPref.getUseDayCountDate()));
			
			
			final int oldVersion = mXPref.getAppVersion();
			final int latestVersion = RockySdk.getInstance().getClientInfo().getVersionCode();
			
			getUiHandler().postDelayed(new Runnable()
			{
				public void run()
				{
					//if (latestVersion > oldVersion || latestVersion <= oldVersion)
					if (latestVersion > oldVersion || mXPref.getUseDayCount() % 30 == 0)
					{
						// 第一次安装时, 或者是使用30天的时候, 会进入引导面.
						mXPref.putAppVersion(latestVersion);
						gotoWelcomeActivity();
					}
					else
					{
						gotoHomeActivity();
					}
					
					//overridePendingTransition(R.anim.one2zero, R.anim.zero2m_one);
				}
			}, delayMillis);
		}
		
	};
	
	/**
	 * Go to guide activity.
	 */
	private void gotoWelcomeActivity()
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, WelcomeActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	/**
	 * Go to home activity.
	 */
	private void gotoHomeActivity()
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, HomeActivity.class);
		startActivity(intent);
		
		finish();
	}
	
	/**
	 * Go to lock activity.
	 */
	private void gotoLockViewActivity()
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, LockViewActivity.class);
		intent.setAction(LockViewActivity.ACTION_LOCK_DECODE);
		startActivity(intent);
		
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			// intercept back press.
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	protected void setBackground(Window window)
	{
		// Do nothing.
	}
	
}
