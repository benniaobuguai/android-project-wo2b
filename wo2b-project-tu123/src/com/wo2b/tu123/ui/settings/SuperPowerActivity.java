package com.wo2b.tu123.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.view.XPreference;
import com.wo2b.wrapper.view.XPreferenceExtra;
import com.wo2b.wrapper.view.XPreferenceExtra.OnXPreferenceSelectListener;
import com.wo2b.tu123.R;

/**
 * SuperPowerActivity.
 * 
 * @author 笨鸟不乖
 * 
 */
public class SuperPowerActivity extends BaseFragmentActivity
{

	private XPreferenceExtra xp_wallpaper;
	private XPreferenceExtra xp_auto_play;
	private XPreferenceExtra xp_cache_local;
	private XPreferenceExtra xp_image_download;
	private XPreferenceExtra xp_bg_music;
	private XPreference xp_open_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_setting_super_power);

		initView();
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.super_power);
		xp_wallpaper = (XPreferenceExtra) findViewById(R.id.xp_wallpaper);
		xp_auto_play = (XPreferenceExtra) findViewById(R.id.xp_auto_play);
		xp_cache_local = (XPreferenceExtra) findViewById(R.id.xp_cache_local);
		xp_image_download = (XPreferenceExtra) findViewById(R.id.xp_image_download);
		xp_bg_music = (XPreferenceExtra) findViewById(R.id.xp_bg_music);
		xp_open_lock = (XPreference) findViewById(R.id.xp_open_lock);

		xp_wallpaper.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_auto_play.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_cache_local.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_image_download.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_bg_music.setOnXPreferenceChangeListener(new OnXPreferenceSelectListenerImpl());
		xp_open_lock.setOnClickListener(new OnLockClickListener());
	}

	public class OnXPreferenceSelectListenerImpl extends OnXPreferenceSelectListener
	{

		@Override
		public boolean onXPreferenceSelected(XPreference preference, boolean isChecked)
		{
			if (!isChecked)
			{
				showToast("UnSelected, should do lots of things.");
			}
			else
			{
				showToast("Selected, do nothing!!!");
			}

			return true;
		}

	}
	
	/**
	 * 锁屏回调
	 * 
	 */
	public class OnLockClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			Intent intent = new Intent();
			intent.setClass(getContext(), LockViewActivity.class);
			intent.setAction(LockViewActivity.ACTION_LOCK_ENCODE);
			startActivity(intent);
		}

	}

}
