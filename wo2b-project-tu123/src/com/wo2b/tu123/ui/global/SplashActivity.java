package com.wo2b.tu123.ui.global;

import java.io.File;
import java.io.InputStream;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Window;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.business.data.RestoreManager;
import com.wo2b.tu123.global.GApplication;
import com.wo2b.tu123.global.provider.DataProvider;
import com.wo2b.tu123.ui.settings.LockViewActivity;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.app.service.DaemonService;
import com.wo2b.wrapper.preference.RockyKeyValues;
import com.wo2b.wrapper.preference.XPreferenceManager;

/**
 * SplashActivity
 * 
 * @author 笨鸟不乖
 * 
 */
public class SplashActivity extends BaseFragmentActivity
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		final String action = intent.getAction();
		if (ACTION_UNCHECK_LOCK.equalsIgnoreCase(action))
		{
			// 不检查锁
			if (GApplication.isEntry())
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
			mSavedPwd = XPreferenceManager.getString(RockyKeyValues.Keys.ENTRY_PASSWORD, "");
			
			if (TextUtils.isEmpty(mSavedPwd))
			{
				// UnLocked.
				if (GApplication.isEntry())
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
	protected void setDefaultValues()
	{
		mStartTime = SystemClock.elapsedRealtime();
		// Start System init.
		getSubHandler().post(mSystemInit);
	}
	
	@Override
	protected boolean hasActionBar()
	{
		return false;
	}
	
	/**
	 * Load system configuration, and other initialize works.
	 */
	private final Runnable mSystemInit = new Runnable()
	{
		
		@Override
		public void run()
		{
			GApplication.setEntry(true);
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
			provider.init(GApplication.getRockyApplication());
			provider.loadCategoryList(getContext());
			provider.loadChapterList(getContext());
		}

		private void systemLoader()
		{
			// Record the use total count.
			long loginCount = XPreferenceManager.getUseTotalCount();
			XPreferenceManager.putUseTotalCount(loginCount + 1); // Login count++

			// Record the use day count
			XPreferenceManager.putUseDayCount(XPreferenceManager.getUseDayCount() + 1);

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
			
			
			final int oldVersion = XPreferenceManager.getAppVersion();
			final int latestVersion = RockySdk.getInstance().getClientInfo().getVersionCode();
			
			getUiHandler().postDelayed(new Runnable()
			{
				public void run()
				{
					//if (latestVersion > oldVersion || latestVersion <= oldVersion)
					if (latestVersion > oldVersion || XPreferenceManager.getUseDayCount() % 30 == 0)
					{
						// 第一次安装时, 或者是使用30天的时候, 会进入引导面.
						XPreferenceManager.putAppVersion(latestVersion);
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
