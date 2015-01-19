package com.wo2b.wrapper.component.user;

import opensource.component.de.greenrobot.event.EventBus;
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
import android.widget.EditText;

import com.wo2b.sdk.assistant.event.RockyEvent;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.fragment.RockyFragment;

/**
 * 登录片段
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class UserLoginFragment extends RockyFragment implements View.OnClickListener
{

	private static final int MENU_GROUP = 100;
	private static final int MENU_ITEM_REGISTER = 1001;

	private EditText et_user_name;
	private EditText et_password;
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
		
		EventBus.getDefault().register(this);
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

		et_user_name = (EditText) view.findViewById(R.id.et_user_name);
		et_password = (EditText) view.findViewById(R.id.et_password);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		forget_password = (Button) view.findViewById(R.id.forget_password);

		
		User localUser = mUserManager.getLocalUser();
		if (localUser != null)
		{
			et_user_name.setText(localUser.getUsername());
			et_password.setText(localUser.getPassword());
		}
	}

	@Override
	protected void bindEvents()
	{
		btn_login.setOnClickListener(this);
		forget_password.setOnClickListener(this);
	}
	
	@Override
	public void onDestroy()
	{
		EventBus.getDefault().unregister(this);
		super.onDestroy();
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

	
	/**
	 * EventBus主线程回调
	 * 
	 * @param msg
	 */
	public void onEventMainThread(Message msg)
	{
		if (msg.what == RockyEvent.USER_LOGIN_OK)
		{
			isLogging = false;

			// 隐藏输入法
			ViewUtils.hideSoftInput(getRockyActivity());
		}
		else if (msg.what == RockyEvent.USER_LOGIN_FAIL)
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

		final String username = et_user_name.getText().toString();
		final String password = et_password.getText().toString();

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
		Message msg = new Message();
		msg.what = RockyEvent.USER_LOGIN_CMD;
		EventBus.getDefault().post(msg);

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
		Message msg = new Message();
		msg.what = RockyEvent.USER_RESET_PWD_CMD;
		EventBus.getDefault().post(msg);
	}

}