package com.wo2b.wrapper.component.user;

import opensource.component.de.greenrobot.event.EventBus;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.wo2b.sdk.assistant.event.RockyEvent;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.wrapper.view.dialog.DialogUtils;

/**
 * 用户信息
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class UserActivity extends RockyFragmentActivity
{

	/**
	 * 需要回调时, 请设置此参数为true
	 */
	public static final String CALLBACK_FLAG = "callback_flag";
	
	private Toolbar mToolbar = null;
	private ProgressBar mProgressBar = null;
	
	private UserDetailFragment mUserDetailFragment = null;
	private UserLoginFragment mUserLoginFragment = null;
	private UserGetPwdByEmailFragment mUserGetPwdFragment = null;

	private UserManager mUserManager = null;
	private Dialog mLogoutDialog;
	private long mLogoutStart = 0;
	
	/**
	 * 主动去点击登录, 默认都进入详情; 没有登录由系统级异常, 决定不进入显示详情.
	 */
	private boolean mShowDetail = true;

	private boolean mCallbackFlag = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_cn_user_main);

		mToolbar = findViewByIdExt(R.id.rocky_toolbar);
		setSupportActionBar(mToolbar);
		mProgressBar = findViewByIdExt(R.id.progress_spinner);
		setSupportProgressBarIndeterminate(false);

		// =============================================================================
		// ### 下面代码主要是基于Toolbar才添加的, 后面有时间, 直接全部替换成Toolbar
		if (!hasActionBar() && getSupportActionBar() != null)
		{
			getSupportActionBar().hide();
		}

		if (getSupportActionBar() != null)
		{
			// 设置图标
			getSupportActionBar().setIcon(R.drawable.wo2b_logo);
			// 设置Logo
			// getSupportActionBar().setLogo(R.drawable.ic_launcher);

			// 显示左上角"返回"图标
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			// 显示标题
			// getSupportActionBar().setDisplayShowTitleEnabled(false);
			// R.id.home区域可点击
			getSupportActionBar().setHomeButtonEnabled(true);
			// 如果有Logo, 则用Logo代替Icon.
			// getSupportActionBar().setDisplayUseLogoEnabled(true);

			getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_actionbar_back_btn);

			// 显示返回+title
			 getSupportActionBar().setDisplayOptions(
			 ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP /*| ActionBar.DISPLAY_SHOW_HOME*/);

			// 设置图标
			int icon = getResources().getIdentifier("actionbar_icon", "drawable", this.getPackageName());
			if (icon > 0)
			{
				getSupportActionBar().setIcon(icon);
			}
		}

		mCallbackFlag = getIntent().getBooleanExtra(CALLBACK_FLAG, false);

		initView();

		EventBus.getDefault().register(this);
	}

	protected void initView()
	{
		// setActionBarTitle(R.string.personal_detail);
		mUserManager = UserManager.getInstance();
		if (mUserManager.isLogin())
		{
			onLoginOK(mUserManager.getMemoryUser());
		}
		else
		{
			onLogout();
		}
	}

	@Override
	protected void bindEvents()
	{

	}

	/**
	 * EventBus主线程回调
	 * 
	 * @param msg
	 */
	public void onEventMainThread(Message msg)
	{
		if (msg.what == RockyEvent.USER_LOGIN_CMD)
		{
			onLoginCmd();
		}
		else if (msg.what == RockyEvent.USER_LOGIN_OK)
		{
			onLoginOK((User) msg.obj);
		}
		else if (msg.what == RockyEvent.USER_LOGIN_FAIL)
		{
			onLoginFail(msg.arg1, msg.obj + "");
		}
		else if (msg.what == RockyEvent.USER_LOGOUT_CMD)
		{
			onLogoutCmd();
		}
		else if (msg.what == RockyEvent.USER_LOGOUT_OK)
		{
			onLogout();
		}
		else if (msg.what == RockyEvent.USER_RESET_PWD_CMD)
		{
			resetPwdCmd();
		}
		else if (msg.what == RockyEvent.USER_RESET_PWD_OK)
		{
			onResetPwdOK();
		}
	}

	/**
	 * 接收到登录指令
	 */
	private void onLoginCmd()
	{
		setSupportProgressBarIndeterminate(true);
	}

	/**
	 * 登录成功
	 * 
	 * @param user
	 */
	private void onLoginOK(final User user)
	{
		setSupportProgressBarIndeterminate(false);

		// 直接退出
		if (mCallbackFlag)
		{
			Intent data = new Intent();
			data.putExtra("user", user);
			setResult(RESULT_OK, data);
			finish();

			return;
		}

		if (mShowDetail)
		{
			// 直接进入详情界面
			if (mUserDetailFragment == null)
			{
				mUserDetailFragment = new UserDetailFragment();
			}

			replaceFragment(mUserDetailFragment);
		}
		else
		{
			finish();
		}
	}

	/**
	 * 登录失败
	 * 
	 * @param code
	 * @param desc
	 */
	private void onLoginFail(final int code, final String desc)
	{
		setSupportProgressBarIndeterminate(false);
		showToastOnUiThread(desc);
	}

	/**
	 * 接收到发现登出的指令时
	 */
	private void onLogoutCmd()
	{
		setSupportProgressBarIndeterminate(true);

		mLogoutStart = System.currentTimeMillis();
		if (mLogoutDialog == null)
		{
			mLogoutDialog = DialogUtils.createLoadingDialog(this, getString(R.string.user_logouting), false);
		}

		mLogoutDialog.show();
	}

	/**
	 * 登出, 包括登录成功/登录失败
	 */
	private void onLogout()
	{
		if (mUserLoginFragment == null)
		{
			mUserLoginFragment = new UserLoginFragment();
		}
		replaceFragment(mUserLoginFragment);

		if (mLogoutDialog != null && mLogoutDialog.isShowing())
		{
			final long cost = System.currentTimeMillis() - mLogoutStart;
			long delay = 0;
			if (cost <= 500)
			{
				delay = 500 - cost;
			}

			getUiHandler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					setSupportProgressBarIndeterminate(false);
					mLogoutDialog.dismiss();
				}

			}, delay);
		}
		else
		{
			setSupportProgressBarIndeterminate(false);
		}
	}
	
	/**
	 * 重置密码
	 */
	private void resetPwdCmd()
	{
		if (mUserGetPwdFragment == null)
		{
			mUserGetPwdFragment = new UserGetPwdByEmailFragment();
		}

		replaceFragment(mUserGetPwdFragment);
	}

	/**
	 * 成功重置密码
	 */
	private void onResetPwdOK()
	{
		setSupportProgressBarIndeterminate(false);

		if (mUserLoginFragment == null)
		{
			mUserLoginFragment = new UserLoginFragment();
		}

		replaceFragment(mUserLoginFragment);
	}

	/**
	 * 替换Fragment
	 * 
	 * @param fragment
	 */
	private void replaceFragment(Fragment fragment)
	{
		// 切换时, 都将取消progressbar的显示
		setSupportProgressBarIndeterminate(false);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.fragment_container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();

		EventBus.getDefault().unregister(this);
	}
	
	/**
	 * 重写父类的方法, 使用Toolbar中的ProgressBar.
	 */
	@Override
	public void setSupportProgressBarIndeterminate(boolean indeterminate)
	{
		if (mProgressBar == null)
		{
			return;
		}

		if (indeterminate)
		{
			mProgressBar.setVisibility(View.VISIBLE);
		}
		else
		{
			mProgressBar.setVisibility(View.GONE);
		}
	}

}