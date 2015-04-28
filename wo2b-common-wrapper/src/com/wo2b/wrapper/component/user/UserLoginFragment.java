package com.wo2b.wrapper.component.user;

import opensource.component.otto.Subscribe;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wo2b.sdk.bus.GBus;
import com.wo2b.sdk.bus.GEvent;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.fragment.BaseFragment;
import com.wo2b.wrapper.view.LabelEditText;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 登录
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-13
 */
public class UserLoginFragment extends BaseFragment implements View.OnClickListener
{

	private static final int MENU_GROUP = 100;
	private static final int MENU_ITEM_REGISTER = 1001;

	private LabelEditText username_let;
	private LabelEditText password_let;
	private Button btn_login;
	private Button forget_password;

	private UserManager mUserManager;
	private boolean isLogging = false;

	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mUserManager = UserManager.getInstance();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.wrapper_user_login, container, false);
		initView(view);
		bindEvents();
		
		return view;
	}

	@Override
	protected void initView(View view)
	{
		setActionBarTitle(R.string.user_login);

		username_let = (LabelEditText) view.findViewById(R.id.username_let);
		password_let = (LabelEditText) view.findViewById(R.id.password_let);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		forget_password = (Button) view.findViewById(R.id.forget_password);

		User localUser = mUserManager.getLocalUser();
		if (localUser != null)
		{
			username_let.setText(localUser.getUsername());
			password_let.setText(localUser.getPassword());
		}
		username_let.editTextFocus();
	}

	@Override
	protected void bindEvents()
	{
		btn_login.setOnClickListener(this);
		forget_password.setOnClickListener(this);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		MenuItem menuItem = menu.add(MENU_GROUP, MENU_ITEM_REGISTER, 0, R.string.register);
		MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == MENU_ITEM_REGISTER)
		{
			onRegisterClick();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btn_login)
		{
			onLoginClick();
		}
		else if (v.getId() == R.id.forget_password)
		{
			onGetPwd();
		}
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
		if (msg.what == GEvent.USER_LOGIN_OK)
		{
			isLogging = false;

			// 隐藏输入法
			ViewUtils.hideSoftInput(getRockyActivity());
		}
		else if (msg.what == GEvent.USER_LOGIN_FAIL)
		{
			isLogging = false;
		}
	}
	
	/**
	 * 登录
	 */
	private void onLoginClick()
	{
		if (isLogging)
		{
			showToast(R.string.user_logging);
			return;
		}

		final String username = username_let.getText().toString();
		final String password = password_let.getText().toString();

		if (TextUtils.isEmpty(username))
		{
			showToast(R.string.hint_input_username);
			return;
		}
		if (TextUtils.isEmpty(password))
		{
			showToast(R.string.hint_input_password);
			return;
		}

		onLoginCmd(username, password);
	}

	/**
	 * 登录
	 */
	private void onLoginCmd(final String username, final String password)
	{
		GBus.post(GEvent.USER_LOGIN_CMD);
		
		isLogging = true; // 正在登录
		mUserManager.login(username, password);
	}

	/**
	 * 注册
	 */
	private void onRegisterClick()
	{
		Intent intent = new Intent();
		intent.setClass(getRockyActivity(), UserRegisterActivity.class);
		startActivity(intent);

		getRockyActivity().finish();
	}

	/**
	 * 找回密码
	 * 
	 * @param v
	 */
	private void onGetPwd()
	{
		GBus.post(GEvent.USER_RESET_PWD_CMD);
	}

}