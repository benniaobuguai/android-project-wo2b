package com.wo2b.wrapper.component.user;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wo2b.sdk.bus.GBus;
import com.wo2b.sdk.bus.GEvent;
import com.wo2b.sdk.myapp.MyAppCenter;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.fragment.BaseFragment;
import com.wo2b.wrapper.view.XPreference;
import com.wo2b.wrapper.view.dialog.DialogText;
import com.wo2b.xxx.webapp.manager.user.GradeManager;
import com.wo2b.xxx.webapp.manager.user.Role;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 个人信息详情
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class UserDetailFragment extends BaseFragment implements View.OnClickListener
{

	private XPreference xp_username;
	private XPreference xp_nickname;
	private XPreference xp_register_app;
	private XPreference xp_grade_name;
	private XPreference xp_user_exp;
	private XPreference xp_user_gold;
	private XPreference xp_email;
	private XPreference xp_tel;
	
	private Button btn_exit_login;

	private UserManager mUserManager;
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		//mOnLoginListener = (UserManager.OnLogoutListener) activity;
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
		View view = inflater.inflate(R.layout.wrapper_user_detail, container, false);
		initView(view);
		bindEvents();

		return view;
	}

	@Override
	protected void initView(View view)
	{
		setActionBarTitle(R.string.personal_detail);

		xp_username = (XPreference) view.findViewById(R.id.xp_username);
		xp_nickname = (XPreference) view.findViewById(R.id.xp_nickname);
		xp_grade_name = (XPreference) view.findViewById(R.id.xp_grade_name);
		xp_user_exp = (XPreference) view.findViewById(R.id.xp_user_exp);
		xp_user_gold = (XPreference) view.findViewById(R.id.xp_user_gold);
		xp_email = (XPreference) view.findViewById(R.id.xp_email);
		xp_tel = (XPreference) view.findViewById(R.id.xp_tel);
		xp_register_app = (XPreference) view.findViewById(R.id.xp_register_app);

		xp_grade_name.bindBadgeViewWarning("warning_grade_desc", 2);

		btn_exit_login = (Button) view.findViewById(R.id.btn_exit_login);
	}

	@Override
	protected void bindEvents()
	{
		xp_nickname.setOnClickListener(this);
		xp_grade_name.setOnClickListener(this);
		xp_email.setOnClickListener(this);
		xp_tel.setOnClickListener(this);
		btn_exit_login.setOnClickListener(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
	{
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		udpateUI(mUserManager.isLogin());
	}

	private void udpateUI(boolean isLogin)
	{
		if (isLogin)
		{
			User user = mUserManager.getMemoryUser();
			if (user != null)
			{
				// 显示的时候, 可在名称右侧添加 "钻石" 标志
				xp_username.setRightText(user.getUsername());
				if (Role.VIP == user.getRoleId())
				{
					xp_username.setItemIndicatorRight(R.drawable.xp_icon_role_vip);
				}
				else
				{
					xp_username.setItemIndicatorRight(R.drawable.xp_icon_role_normal);
				}
				xp_nickname.setRightText(user.getNickname());
				
				xp_grade_name.setRightText(GradeManager.getGradeByUserExp(user.getExp()));
				xp_user_exp.setRightText(user.getExp() + "");
				xp_user_exp.setItemIndicatorRight(R.drawable.xp_icon_exp);
				
				xp_email.setRightText(user.getEmail());
				xp_tel.setRightText(user.getTel());
				String from = MyAppCenter.getAppNameByPkgname(user.getPkgname());
				xp_register_app.setRightText(from);

				// UserGold useGold = mUserManager.getUserGold();
				xp_user_gold.setRightText(user.getUserGold().getGold() + "");
				xp_user_gold.setItemIndicatorRight(R.drawable.xp_icon_gold);
			}
		}
		else
		{
			xp_username.setRightText(R.string.user_nologin);

			xp_nickname.setRightText("");
			xp_register_app.setRightText("");
			xp_email.setRightText("");
			xp_tel.setRightText("");

			xp_grade_name.setRightText("");
			xp_user_gold.setRightText("");
			xp_user_exp.setRightText("");
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.xp_nickname)
		{
			onNicknameClick(v);
		}
		if (v.getId() == R.id.xp_grade_name)
		{
			// 等级信息介绍
			onGradeNameClick(v);
		}
		if (v.getId() == R.id.xp_email)
		{
			onEmailClick(v);
		}
		if (v.getId() == R.id.xp_tel)
		{
			onTelClick(v);
		}
		if (v.getId() == R.id.btn_exit_login)
		{
			onLogOutClick(v);
		}
	}

	/**
	 * 修改昵称
	 * 
	 * @param view
	 */
	private void onNicknameClick(View view)
	{
		DialogText dialog = new DialogText(getActivity());
		dialog.setMessage(R.string.hint_not_support_nickname);
		dialog.setPositiveButtonListener(R.string.ok, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 等级信息
	 * 
	 * @param v
	 */
	private void onGradeNameClick(View v)
	{
		Intent intent = new Intent(getContext(), UserGradeListActivity.class);
		startActivity(intent);
	}

	/**
	 * 绑定或修改邮箱
	 * 
	 * @param v
	 */
	private void onEmailClick(View v)
	{
		DialogText dialog = new DialogText(getActivity());
		dialog.setMessage(R.string.hint_not_support_email);
		dialog.setPositiveButtonListener(R.string.ok, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 绑定或修改手机号码
	 * 
	 * @param view
	 */
	private void onTelClick(View view)
	{
		DialogText dialog = new DialogText(getActivity());
		dialog.setMessage(R.string.hint_not_support_phonenum);
		dialog.setPositiveButtonListener(R.string.ok, new OnClickListener()
		{

			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/**
	 * 安全退出
	 * 
	 * @param v
	 */
	private void onLogOutClick(View v)
	{
		mUserManager.logout();

		GBus.post(GEvent.USER_LOGOUT_CMD);
	}

}