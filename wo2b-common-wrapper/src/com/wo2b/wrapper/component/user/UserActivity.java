package com.wo2b.wrapper.component.user;

import opensource.component.otto.Subscribe;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.wo2b.sdk.bus.GEvent;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.view.dialog.DialogUtils;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 用户信息
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class UserActivity extends BaseFragmentActivity
{

	/**
	 * 需要回调时, 请设置此参数为true
	 */
	public static final String CALLBACK_FLAG = "callback_flag";
	
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

		setSupportProgressBarIndeterminate(false);
		mCallbackFlag = getIntent().getBooleanExtra(CALLBACK_FLAG, false);

		initView();
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
	
	@Override
	protected boolean busEventEnable()
	{
		return true;
	}

	/**
	 * EventBus主线程回调
	 * 
	 * @param msg
	 */
	@Subscribe
	public void onLoginCallback(Message msg)
	{
		if (msg.what == GEvent.USER_LOGIN_CMD)
		{
			onLoginCmd();
		}
		else if (msg.what == GEvent.USER_LOGIN_OK)
		{
			onLoginOK((User) msg.obj);
		}
		else if (msg.what == GEvent.USER_LOGIN_FAIL)
		{
			onLoginFail(msg.arg1, msg.obj + "");
		}
		else if (msg.what == GEvent.USER_LOGOUT_CMD)
		{
			onLogoutCmd();
		}
		else if (msg.what == GEvent.USER_LOGOUT_OK)
		{
			onLogout();
		}
		else if (msg.what == GEvent.USER_RESET_PWD_CMD)
		{
			resetPwdCmd();
		}
		else if (msg.what == GEvent.USER_RESET_PWD_OK)
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
	}
	
//	/**
//	 * 重写父类的方法, 使用Toolbar中的ProgressBar.
//	 */
//	@Override
//	public void setSupportProgressBarIndeterminate(boolean indeterminate)
//	{
//		if (mProgressBar == null)
//		{
//			return;
//		}
//
//		if (indeterminate)
//		{
//			mProgressBar.setVisibility(View.VISIBLE);
//		}
//		else
//		{
//			mProgressBar.setVisibility(View.GONE);
//		}
//	}

}