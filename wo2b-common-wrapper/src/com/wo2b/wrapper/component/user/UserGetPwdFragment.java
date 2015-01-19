package com.wo2b.wrapper.component.user;

import opensource.component.de.greenrobot.event.EventBus;
import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.wo2b.sdk.assistant.event.RockyEvent;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.fragment.RockyFragment;

/**
 * 找回密码
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-10-29
 */
public class UserGetPwdFragment extends RockyFragment
{

	private EditText et_user_name;
	private EditText et_password;
	private Button user_reset_pwd;

	private UserManager mUserManager;
	
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
		View view = inflater.inflate(R.layout.wrapper_user_get_pwd, container, false);
		initView(view);

		return view;
	}

	@Override
	protected void initView(View view)
	{
		setActionBarTitle(R.string.user_reset_pwd);

		et_user_name = (EditText) view.findViewById(R.id.et_user_name);
		et_password = (EditText) view.findViewById(R.id.et_password);
		user_reset_pwd = (Button) view.findViewById(R.id.user_reset_pwd);

		User localUser = mUserManager.getLocalUser();
		if (localUser != null)
		{
			et_user_name.setText(localUser.getUsername());
			et_password.setText(localUser.getPassword());
		}

		user_reset_pwd.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				onResetPwd(v);
			}
		});
	}

	/**
	 * 重置密码
	 * 
	 * @param view
	 */
	private void onResetPwd(View view)
	{
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

		getActionBarActivity().setSupportProgressBarIndeterminateVisibility(true);
		mUserManager.resetPwd(getActivity(), username, password, new Wo2bResHandler<Void>()
		{

			@Override
			public void onSuccess(int code, Void result)
			{
				// 隐藏输入法
				ViewUtils.hideSoftInput(getRockyActivity());

				Message msg = new Message();
				msg.what = RockyEvent.USER_RESET_PWD_OK;
				EventBus.getDefault().post(msg);
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				showToast(msg);
			}

		});
	}

}