package com.wo2b.tu123.ui.global;

import java.util.Random;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.umeng.MobclickAgentProxy;
import com.wo2b.sdk.umeng.UmengUpdateAgentProxy;
import com.wo2b.wrapper.app.extra.RockyTabFragmentActivity;
import com.wo2b.wrapper.view.BottomView;
import com.wo2b.wrapper.view.BottomView.OnBottomViewItemClick;
import com.wo2b.wrapper.view.dialog.DialogUtils;
import com.wo2b.tu123.R;
import com.wo2b.tu123.ui.baike.BaikeFragment;
import com.wo2b.tu123.ui.blossom.BlossomHomeFragment;
import com.wo2b.tu123.ui.settings.SettingsActivity;
import com.wo2b.tu123.ui.uc.UserCenterFragment;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * Home Activity
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-9
 */
public class HomeActivity extends RockyTabFragmentActivity
{

	private static final String TAG = "Rocky.HomeActivity";

	private BaikeFragment mBaikeFragment;
	private BlossomHomeFragment mBlossomHomeFragment;
	private UserCenterFragment mUserCenterFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tu_home);
		
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar_icon);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		mBaikeFragment = new BaikeFragment();
		mBlossomHomeFragment = new BlossomHomeFragment();
		mUserCenterFragment = new UserCenterFragment();

		addTab(0, R.string.home_bottom_bar_tu123, R.drawable.demo, mBaikeFragment);
		addTab(1, R.string.home_bottom_bar_blossom, R.drawable.demo, mBlossomHomeFragment);
		addTab(2, R.string.home_bottom_bar_user_center, R.drawable.demo, mUserCenterFragment);
		fillTabLayout();

		autoLogin();

		initUmeng();
	}

	/**
	 * 自动登录
	 */
	private void autoLogin()
	{
		UserManager userManager = UserManager.getInstance();
		if (!userManager.isLogin())
		{
			final User user = userManager.getLocalUser();
			if (user != null)
			{
				// 存在登录的用户数据
				userManager.login(user.getUsername(), user.getPassword());
			}
		}
	}

	/**
	 * Umeng初始化
	 */
	private void initUmeng()
	{
		// Umeng--Android平台的数据发送策略有两种方式
		// MobclickAgent.setDebugMode(true);// 数据实时上传Umeng
		MobclickAgentProxy.updateOnlineConfig(this);
		// Umeng自动更新
		UmengUpdateAgentProxy.update(this);
	}

	@Override
	public void finish()
	{
		// super.finish();
		moveTaskToBack(true);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyBackIntercept())
		{
			if (keyCode == KeyEvent.KEYCODE_BACK)
			{
				if ((System.currentTimeMillis() - mExitTime) > 2000)
				{
					showToast(R.string.hint_goto_launcher);
					mExitTime = System.currentTimeMillis();

					return true;
				}
				else
				{
					finish();
				}
			}
		}

		if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_MENU)
		{
			bottomView = createBottomView();
			bottomView.showBottomView(true);
		}

		return super.onKeyDown(keyCode, event);
	}
	
	private BottomView bottomView = null;
	
	private BottomView createBottomView()
	{
		BottomView bottomView = new BottomView.Builder(this)
			//.setTransparency(Transparency.LEVEL_3)
			.setFocusable(true)
			.setTouchable(true)
			.setOutsideTouchable(false)
			.setCommandList(new int[]{R.string.quit})
			.setOnBottomViewItemClick(new OnBottomViewItemClick()
			{
				
				@Override
				public void onClick(final BottomView bottomView, int which)
				{
					switch (which)
					{
						case R.string.quit:
						{
							String[] warns = getResources().getStringArray(R.array.exit_warns);
							String warn = warns[new Random().nextInt(warns.length - 1)];
							Dialog exitDialog = DialogUtils.createLoadingDialog(mContext, warn);
							exitDialog.setCancelable(true);
							//exitDialog.setCanceledOnTouchOutside(false);
							exitDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
							{
								
								@Override
								public void onCancel(DialogInterface dialog)
								{
									getUiHandler().removeCallbacks(mExitRunnable);
								}
							});
							exitDialog.setOnShowListener(new OnShowListener()
							{
								
								@Override
								public void onShow(DialogInterface dialog)
								{
									bottomView.dismissBottomView();
								}
							});
							exitDialog.show();
							
							getUiHandler().postDelayed(mExitRunnable, 1357);
							break;
						}
					}
				}
			})
			.create();
		
		bottomView.setAnimation(R.style.bottomView_animation);
		
		return bottomView;
	}
	
	private long mExitTime = 0;  
	  
	private Runnable mExitRunnable = new Runnable()
	{
		
		@Override
		public void run()
		{
			exit();
		}
	};

	private void exit()
	{
		Log.W(TAG, "Exit the application.");
		
		// TODO: 结束广告
		//Wo2bApplication.ad_shutdown(getApplicationContext());
		
		
		 
		super.finish();
		//overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		overridePendingTransition(R.anim.hold, R.anim.zoom_out);
		
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.home_menu, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActionBarSearchClick()
	{
		Intent intent = new Intent();
		intent.setClass(this, SearchActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onActionBarSettingClick()
	{
		Intent intent = new Intent();
		intent.setClass(this, SettingsActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected boolean onActionBarHomeClick()
	{
		return true;
	}
	
	/**
	 * 拦截返回事件
	 * 
	 * @return
	 */
	protected boolean keyBackIntercept()
	{
		return false;
	}

}
