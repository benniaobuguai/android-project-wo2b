package com.wo2b.wrapper.component.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.wo2b.sdk.common.util.RegexUtil;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.UserManager;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.fragment.RockyFragment;

/**
 * 通过邮件找回密码
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-10-29
 */
public class UserGetPwdByEmailFragment extends RockyFragment
{

	private EditText et_register_email;
	private TextView tv_result;
	private Button btn_send_email;

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
		View view = inflater.inflate(R.layout.wrapper_user_get_pwd_email, container, false);
		initView(view);

		return view;
	}

	@Override
	protected void initView(View view)
	{
		setActionBarTitle(R.string.user_reset_pwd);

		et_register_email = (EditText) view.findViewById(R.id.et_register_email);
		tv_result = (TextView) view.findViewById(R.id.tv_result);
		btn_send_email = (Button) view.findViewById(R.id.btn_send_email);

		btn_send_email.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				onSendEmail(v);
			}
		});
	}

	/**
	 * 重置密码
	 * 
	 * @param view
	 */
	private void onSendEmail(View view)
	{
		final String registerEmail = et_register_email.getText().toString();
		if (!RegexUtil.isEmail(registerEmail))
		{
			showToast(R.string.hint_input_email_warn);
			return;
		}
		tv_result.setText("");
		btn_send_email.setText(R.string.send_email_now);
		btn_send_email.setEnabled(false);
		mUserManager.sendResetPwdEmail(registerEmail, new Wo2bResHandler<Void>()
		{

			@Override
			public void onSuccess(int code, Void result)
			{
				// 隐藏输入法
				ViewUtils.hideSoftInput(getRockyActivity());

				tv_result.setText(R.string.hint_send_email_ok);
				btn_send_email.setText(R.string.send_email);
				btn_send_email.setEnabled(true);
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				tv_result.setText(msg);
				btn_send_email.setText(R.string.send_email);
				btn_send_email.setEnabled(true);
			}

		});
	}

}