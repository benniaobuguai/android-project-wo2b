package com.wo2b.tu123.ui.uc;

import opensource.component.de.greenrobot.event.EventBus;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.wo2b.sdk.assistant.event.RockyEvent;
import com.wo2b.wrapper.app.fragment.RockyFragment;
import com.wo2b.wrapper.component.common.StatementActivity;
import com.wo2b.wrapper.component.common.Wo2bHomeActivity;
import com.wo2b.wrapper.view.XPreference;
import com.wo2b.tu123.R;
import com.wo2b.tu123.ui.extra.DeveloperHomeActivity;
import com.wo2b.tu123.ui.localalbum.LocalAlbumActivity;
import com.wo2b.tu123.ui.settings.AboutActivity;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;
import com.wo2b.wrapper.component.common.Wo2bAppListActivity;
import com.wo2b.wrapper.component.user.UserActivity;

/**
 * User Center Home.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-15
 */
public class UserCenterFragment extends RockyFragment implements OnClickListener
{

	private XPreference xp_user_info;
	private XPreference xp_my_favorites;
	private XPreference xp_local_album;
	private XPreference xp_statement;
	private XPreference xp_about;
	private XPreference xp_wo2b;
	private XPreference xp_support_developer;
	private XPreference xp_recommend_apps;
	
	private UserManager mUserManager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		
		mUserManager = UserManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.uc_home, container, false);
		initView(view);
		bindEvents();

		return view;
	}
	
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
	}
	
	/**
	 * 登录回调
	 * 
	 * @param msg
	 */
	public void onEventMainThread(Message msg)
	{
		if (msg.what == RockyEvent.USER_LOGIN_OK)
		{
			onLoginSuccess((User) msg.obj);
		}
		else if (msg.what == RockyEvent.USER_LOGIN_FAIL)
		{
			onLoginError(msg.arg1, msg.obj + "");
		}
		else if (msg.what == RockyEvent.USER_LOGOUT_OK)
		{
			onLogout();
		}
	}

	private void onLoginSuccess(final User user)
	{
		xp_user_info.setTitle(user.getNickname());
		xp_user_info.setRightText(user.getUserGold().getGold() + "");
		xp_user_info.setItemIndicatorRight(R.drawable.xp_icon_gold2);
	}

	private void onLoginError(final int code, final String desc)
	{
		xp_user_info.setTitle(R.string.user_nologin);
		xp_user_info.setRightText("");
	}

	private void onLogout()
	{
		xp_user_info.setTitle(R.string.user_nologin);
		xp_user_info.setRightText("");
	}

	@Override
	protected void initView(View view)
	{
		xp_user_info = (XPreference) view.findViewById(R.id.xp_user_info);
		xp_my_favorites = (XPreference) view.findViewById(R.id.xp_my_favorites);
		xp_local_album = (XPreference) view.findViewById(R.id.xp_local_album);
		xp_statement = (XPreference) view.findViewById(R.id.xp_statement);
		xp_about = (XPreference) view.findViewById(R.id.xp_about);
		xp_wo2b = (XPreference) view.findViewById(R.id.xp_wo2b);
		xp_support_developer = (XPreference) view.findViewById(R.id.xp_support_developer);
		xp_recommend_apps = (XPreference) view.findViewById(R.id.xp_wo2b_app);

		xp_user_info.setTitle(R.string.user_nologin);
		xp_user_info.setRightText("");

		xp_statement.bindBadgeViewWarning("statement_click");
	}

	@Override
	protected void bindEvents()
	{
		xp_user_info.setOnClickListener(this);
		xp_my_favorites.setOnClickListener(this);
		xp_local_album.setOnClickListener(this);
		xp_statement.setOnClickListener(this);
		xp_about.setOnClickListener(this);
		xp_wo2b.setOnClickListener(this);
		xp_support_developer.setOnClickListener(this);
		xp_recommend_apps.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume()
	{
		super.onResume();

		if (mUserManager.isLogin())
		{
			User memoryUser = mUserManager.getMemoryUser();
			xp_user_info.setTitle(memoryUser.getNickname());
			xp_user_info.setRightText(memoryUser.getUserGold().getGold() + "");
			xp_user_info.setItemIndicatorRight(R.drawable.xp_icon_gold2);
		}
		else
		{
			User localUser = mUserManager.getLocalUser();
			if (localUser != null)
			{
				xp_user_info.setTitle(localUser.getNickname());
				xp_user_info.setRightText(localUser.getUserGold().getGold() + "");
				xp_user_info.setItemIndicatorRight(R.drawable.xp_icon_gold2);
			}
			else
			{
				xp_user_info.setTitle(R.string.user_nologin);
				xp_user_info.setRightText("");
			}
		}
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.xp_user_info:
			{
				onUserInfoClick(v);
				break;
			}
			case R.id.xp_my_favorites:
			{
				onMyFavoriteClick(v);
				break;
			}
			case R.id.xp_local_album:
			{
				onLocalAlbumClick(v);
				break;
			}
			case R.id.xp_support_developer:
			{
				onSupportDeveloperClick(v);
				break;
			}
			case R.id.xp_wo2b:
			{
				onWo2bHomeClick(v);
				break;
			}
			case R.id.xp_wo2b_app:
			{
				onWo2bAppClick(v);
				break;
			}
			case R.id.xp_statement:
			{
				onStatementClick(v);
				break;
			}
			case R.id.xp_about:
			{
				onAboutClick(v);
				break;
			}
		}
	}

	/**
	 * 用户登录-->个人信息, 用户未登录-->登录界面
	 * 
	 * @param view
	 */
	private void onUserInfoClick(View view)
	{
		Intent intent = new Intent();
		intent.setClass(getContext(), UserActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 我的最爱
	 * 
	 * @param view
	 */
	private void onMyFavoriteClick(View view)
	{
		Intent intent = new Intent(getContext(), MyFavoritesActivity.class);
		startActivity(intent);
	}

	/**
	 * 本地相册
	 * 
	 * @param view
	 */
	private void onLocalAlbumClick(View view)
	{
		Intent intent = new Intent(getContext(), LocalAlbumActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 支持开发者
	 * 
	 * @param v
	 */
	private void onSupportDeveloperClick(View v)
	{
		Intent intent = new Intent(getContext(), DeveloperHomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 官网首页
	 * 
	 * @param view
	 */
	private void onWo2bHomeClick(View view)
	{
		Intent intent = new Intent(getContext(), Wo2bHomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 官方出品
	 * 
	 * @param v
	 */
	private void onWo2bAppClick(View v)
	{
		Intent intent = new Intent(getContext(), Wo2bAppListActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 声明
	 * 
	 * @param v
	 */
	private void onStatementClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(getContext(), StatementActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

	/**
	 * 关于
	 * 
	 * @param v
	 */
	private void onAboutClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(getContext(), AboutActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		startActivity(intent);
	}

}
