package com.wo2b.wrapper.component.user;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.common.util.RegexUtil;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.view.LabelEditText;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 用户注册
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-13
 */
public class UserRegisterActivity extends BaseFragmentActivity implements View.OnClickListener
{

	private LabelEditText username_let;
	private LabelEditText password_let;
	private LabelEditText password2_let;
	private LabelEditText email_phone_let;
	private Button btn_ok;

	private UserManager mUserManager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_user_register);
		initView();

		mUserManager = UserManager.getInstance();
	}

	protected void initView()
	{
		setActionBarTitle(R.string.user_register);

		username_let = findViewByIdExt(R.id.username_let);
		password_let = findViewByIdExt(R.id.password_let);
		password2_let = findViewByIdExt(R.id.password2_let);
		email_phone_let = findViewByIdExt(R.id.email_phone_let);
		username_let.editTextFocus();

		btn_ok = (Button) findViewById(R.id.btn_ok);
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
		final String username = username_let.getText().toString();
		final String password = password_let.getText().toString();
		final String password2 = password2_let.getText().toString();
		final String email = email_phone_let.getText().toString();

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