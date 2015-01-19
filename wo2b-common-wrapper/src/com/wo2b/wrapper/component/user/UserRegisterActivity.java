package com.wo2b.wrapper.component.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.common.util.RegexUtil;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 用户注册
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class UserRegisterActivity extends RockyFragmentActivity implements View.OnClickListener
{

	private EditText et_user_name;
	private EditText et_password;
	private EditText et_password2;
	private EditText et_email;
	private Button btn_ok;

	private UserManager mUserManager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_user_register);
		initView();
		bindEvents();

		mUserManager = UserManager.getInstance();
	}

	protected void initView()
	{
		setActionBarTitle(R.string.user_register);

		et_user_name = (EditText) findViewById(R.id.et_user_name);
		et_password = (EditText) findViewById(R.id.et_password);
		et_password2 = (EditText) findViewById(R.id.et_password2);
		et_email = (EditText) findViewById(R.id.et_email);
		btn_ok = (Button) findViewById(R.id.btn_ok);
	}

	@Override
	protected void bindEvents()
	{
		btn_ok.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btn_ok)
		{
			onRegisterClick(v);
		}
	}

	private void onRegisterClick(View view)
	{
		final String username = et_user_name.getText().toString();
		final String password = et_password.getText().toString();
		final String password2 = et_password2.getText().toString();
		final String email = et_email.getText().toString();

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
		if (!password.equals(password2))
		{
			showToast(R.string.hint_input_password2);
			return;
		}
		if (TextUtils.isEmpty(email))
		{
			showToast(R.string.hint_input_email_warn);
			return;
		}

		if (!RegexUtil.isEmail(email))
		{
			showToast(R.string.hint_input_email_warn);
			return;
		}

		// 隐藏输入法
		// ViewUtils.hideSoftInput(this, et_email);

		setSupportProgressBarIndeterminateVisibility(true);
		String mac = DeviceInfoManager.getMacAddress(this);
		String deviceId = DeviceInfoManager.getDeviceId(this);
		mUserManager.register(username, password, password2, email, mac, deviceId, new Wo2bResHandler<Void>()
		{

			@Override
			public void onSuccess(int code, Void result)
			{
				setSupportProgressBarIndeterminateVisibility(false);
				Intent intent = new Intent();
				intent.setClass(getContext(), UserActivity.class);
				intent.putExtra("username", username);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				setSupportProgressBarIndeterminateVisibility(false);
				showToastOnUiThread(msg);
			}
		});
	}

}