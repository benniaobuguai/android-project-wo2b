package com.wo2b.tu123.ui.settings;

import opensource.component.pulltorefresh.PullToRefreshBase.Mode;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.assistant.upgrade.VersionInfo;
import com.wo2b.sdk.common.util.AppHelper;
import com.wo2b.sdk.common.util.ManifestTools;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.sdk.umeng.UmengUpdateAgentProxy;
import com.wo2b.sdk.view.pulltorefresh.PullToRefreshScrollView;
import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.wrapper.view.XPreference;
import com.wo2b.wrapper.view.dialog.DialogText;
import com.wo2b.tu123.R;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.global.WelcomeActivity;

/**
 * AboutActivity.
 * 
 * @author Rocky
 * 
 */
public class AboutActivity extends RockyFragmentActivity implements OnClickListener
{

	private static final String TAG = "Rocky.About";
	
	private XPreference xp_give_mark;
	private XPreference xp_welcome_page;
	private XPreference xp_share_to_friends;
	private XPreference xp_feedback;
	//private XPreference xp_tujie_story;
	private XPreference xp_check_version;
	private LinearLayout ll_copyright;

	
	private FeedbackAgent mFeedbackAgent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_setting_about);
		
		initView();
		bindEvents();
		setDefaultValues();
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.about);
		PullToRefreshScrollView pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pullToRefreshScrollView);
		pullToRefreshScrollView.setMode(Mode.PULL_FROM_START);

		xp_give_mark = (XPreference) findViewById(R.id.xp_give_mark);
		xp_welcome_page = (XPreference) findViewById(R.id.xp_welcome_page);
		xp_share_to_friends = (XPreference) findViewById(R.id.xp_share_to_friends);
		xp_feedback = (XPreference) findViewById(R.id.xp_feedback);
		xp_check_version = (XPreference) findViewById(R.id.xp_check_version);
		ll_copyright = (LinearLayout) findViewById(R.id.ll_copyright);
		// xp_tujie_story = (XPreference) findViewById(R.id.xp_tujie_story);

		xp_check_version.setRightText(RockySdk.getInstance().getClientInfo().getVersionName());
	}

	@Override
	protected void bindEvents()
	{
		xp_give_mark.setOnClickListener(this);
		xp_welcome_page.setOnClickListener(this);
		xp_share_to_friends.setOnClickListener(this);
		xp_feedback.setOnClickListener(this);
		// xp_tujie_story.setOnClickListener(this);
		xp_check_version.setOnClickListener(this);
		ll_copyright.setOnClickListener(this);
	}

	@Override
	protected void setDefaultValues()
	{
		mFeedbackAgent = new FeedbackAgent(this);
		mFeedbackAgent.sync();
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.xp_give_mark:
			{
				onGiveMarkClick(v);
				break;
			}
			case R.id.xp_welcome_page:
			{
				onWelcomePageClick(v);
				break;
			}
			case R.id.xp_share_to_friends:
			{
				onShareToFriends(v);
				break;
			}
			case R.id.xp_feedback:
			{
				onFeedbackClick(v);
				break;
			}
			//case R.id.xp_tujie_story:
			//{
			//	onTuJieStoryClick(v);
			//	break;
			//}
			case R.id.xp_check_version:
			{
				onUpgradeClick(v);
				break;
			}
			case R.id.ll_copyright:
			{
				onCopyRightClick(v);
				break;
			}
		}
	}

	/**
	 * 去评分
	 * 
	 * @param v
	 */
	private void onGiveMarkClick(View v)
	{
		AppHelper.launchAppMarket(this, getPackageName());
	}

	/**
	 * 去欢迎页
	 * 
	 * @param v
	 */
	private void onWelcomePageClick(View v)
	{
		Intent intent = new Intent();
		intent.setClass(AboutActivity.this, WelcomeActivity.class);
		intent.putExtra(RockyIntent.EXTRA_MODE, WelcomeActivity.MODE_PERSONAL_TAILOR);
		startActivity(intent);
	}

	/**
	 * 分享给好友
	 * 
	 * @param v
	 */
	private void onShareToFriends(View v)
	{
		Intent intent = new Intent(Intent.ACTION_SEND);
		// intent.setType("image/*");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "应用分享");
		intent.putExtra(Intent.EXTRA_TEXT, "亲，分享一款用心的应用『图界传说』。下载地址：http://www.wo2b.com");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(Intent.createChooser(intent, getTitle()));
	}
	
	/**
	 * 检测更新
	 * 
	 * @param v
	 */
	private void onUpgradeClick(View v)
	{
		// 检查更新
		UmengUpdateAgentProxy.forceUpdate(getApplicationContext(), new UmengUpdateListener()
		{

			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse)
			{
				switch (updateStatus)
				{
					case 0:
					{
						// has update
						//UmengUpdateAgent.showUpdateDialog(mContext, updateResponse);
						break;
					}
					case 1:
					{
						// has no update
						showToast(R.string.update_not_new_version);
						break;
					}
					case 2:
					{
						// none wifi
						Log.D(TAG, "" + getString(R.string.update_only_wifi));
						break;
					}
					case 3:
					{
						// time out
						showToast(R.string.update_time_out);
						break;
					}
				}
			}
		});
	}

	/**
	 * 意见反馈
	 * 
	 * @param v
	 */
	private void onFeedbackClick(View v)
	{
		mFeedbackAgent.startFeedbackActivity();
	}

	private int mClickCount = 0;

	/**
	 * 版权信息
	 * 
	 * @param v
	 */
	private void onCopyRightClick(View v)
	{
		if (mClickCount < 10)
		{
			++mClickCount;
		}
		else
		{
			mClickCount = 0;

			VersionInfo versionInfo = ManifestTools.getVersionInfo(this);
			String umengChannel = ManifestTools.getApplicationMetaData(getApplicationContext(), "UMENG_CHANNEL")
					.toString();

			StringBuffer sb = new StringBuffer();
			sb.append("VersionCode: ").append(versionInfo.getVersionCode()).append("\n");
			sb.append("VersionName: ").append(versionInfo.getVersionName()).append("\n");
			sb.append("Channel: ").append(umengChannel).append("\n");

			DialogText dialog = new DialogText(this);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(true);
			dialog.setMessage(sb.toString());
			dialog.show();
		}
	}

}
