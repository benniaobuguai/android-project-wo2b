package com.wo2b.wrapper.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.view.WindowCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wo2b.sdk.assistant.upgrade.VersionInfo;
import com.wo2b.sdk.bus.GBus;
import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.common.util.ManifestTools;
import com.wo2b.sdk.config.SdkConfig;
import com.wo2b.sdk.core.ClientInfo;
import com.wo2b.sdk.core.RockyConfig;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.sdk.umeng.MobclickAgentProxy;
import com.wo2b.wrapper.R;

/**
 * Rocky Abs FragmentActivity
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-10-25
 */
public class BaseFragmentActivity extends ActionBarActivity
{

	public static final String TAG = "Rocky.AbsFragmentActivity";

	/**
	 * 常规菜单组索引必须大于或等于此值, 否则可能引起与特别模块的键值冲突.
	 */
	public static final int MENU_GROUP_FIRST = 1000000;

	/**
	 * 常规菜单项索引必须大于或等于此值, 否则可能引起与特别模块的键值冲突.
	 */
	public static final int MENU_ITEM_FIRST = 100;
	public static final int MENU_ITEM_CONFIRM = MENU_ITEM_FIRST + 1;
	public static final int MENU_ITEM_ADD = MENU_ITEM_FIRST + 2;
	public static final int MENU_ITEM_EDIT = MENU_ITEM_FIRST + 3;
	public static final int MENU_ITEM_SEARCH = MENU_ITEM_FIRST + 4;
	
	

	// --------------------------------------------------------------------
	// ------------------------- Common message code ----------------------
	/** Request popup menu. */
	public static final int MENU_REQUEST_CODE = 10000;

	// --------------------------------------------------------------------

	protected Context mContext;

	private Toast mToast;

	/** Ui Handler. */
	private Handler mUiHandler;
	
	/** Sub/Worker Handler. */
	private Handler mSubHandler;
	
	/** Sub/Worker Thread. */
	private HandlerThread mSubThread;
	private Toolbar mToolbar = null;
	private ProgressBar mProgressBar = null;
	private Button mPositiveBtn = null;
	
	private View rocky_rootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.global_base_activity);
		mToolbar = findViewByIdExt(R.id.rocky_toolbar);
		setSupportActionBar(mToolbar);
		mProgressBar = findViewByIdExt(R.id.progress_spinner);
		mPositiveBtn = findViewByIdExt(R.id.positive_btn);
		setSupportProgressBarIndeterminate(false);
		
		rocky_rootView = findViewByIdExt(R.id.progress_spinner);

		mContext = this;
		
		if (!hasActionBar() && getSupportActionBar() != null)
		{
			getSupportActionBar().hide();
		}
		
		if (getSupportActionBar() != null)
		{
			// 设置图标
			getSupportActionBar().setIcon(R.drawable.wo2b_logo);
			// 设置Logo
			// getSupportActionBar().setLogo(R.drawable.ic_launcher);

			// 显示左上角"返回"图标
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			// 显示标题
			// getSupportActionBar().setDisplayShowTitleEnabled(false);
			// R.id.home区域可点击
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);

			getSupportActionBar().setHomeAsUpIndicator(R.drawable.selector_actionbar_back_btn);

			// 显示返回+title
			getSupportActionBar()
					.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_HOME_AS_UP /*| ActionBar.DISPLAY_SHOW_HOME*/);

			// 设置图标
			int icon = getResources().getIdentifier("actionbar_icon", "drawable", this.getPackageName());
			if (icon > 0)
			{
				getSupportActionBar().setIcon(icon);
			}
		}

		mSubThread = new HandlerThread(this.getClass().getName());
		// mSubThread.setPriority(Thread.MIN_PRIORITY); //设置线程优先级
		mSubThread.start();

		mUiHandler = new Handler(this.getMainLooper(), mUiHandlerCallback);

		mSubHandler = new Handler(mSubThread.getLooper(), mSubHandlerCallback);
	}
	
	@Override
	public void setContentView(int layoutResID)
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		FrameLayout parentView = ((FrameLayout) findViewById(R.id.global_root_content));
		inflater.inflate(layoutResID, parentView);

		if (!hasActionBar() || mFeatureId == WindowCompat.FEATURE_ACTION_BAR_OVERLAY)
		{
			// findViewByIdExt(R.id.fixed_height).setVisibility(View.GONE);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(0, 0, 0, 0);
			parentView.setLayoutParams(params);
		}
		else
		{
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(0, getResources().getDimensionPixelSize(R.dimen.theme_actionbar_height), 0, 0);
			parentView.setLayoutParams(params);
		}

		onSupportContentChangedExt();
	}

	private int mFeatureId = -1;

	@Override
	public boolean supportRequestWindowFeature(int featureId)
	{
		this.mFeatureId = featureId;
		return super.supportRequestWindowFeature(featureId);
	}
	
	/**
	 * 不推荐使用此方法, 可继承此 {@link #onSupportContentChangedExt()}实现一样的的效果.
	 */
	@Deprecated
	@Override
	public void onSupportContentChanged()
	{
		super.onSupportContentChanged();
	}

	public void onSupportContentChangedExt()
	{

	}
	
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ActionBar Handler &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}
	
		/**
	 * 重写父类的方法, 使用Toolbar中的ProgressBar.
	 */
	@Override
	public void setSupportProgressBarIndeterminate(boolean indeterminate)
	{
		if (mProgressBar == null)
		{
			return;
		}

		if (indeterminate)
		{
			mProgressBar.setVisibility(View.VISIBLE);
		}
		else
		{
			mProgressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public ActionBar getSupportActionBar()
	{
		return super.getSupportActionBar();
	}

	/**
	 * 
	 * @return
	 */
	public Toolbar getToolbar()
	{
		return this.mToolbar;
	}

	/**
	 * 设置标题
	 * 
	 * @param resId
	 */
	public void setActionBarTitle(int resId)
	{
		getSupportActionBar().setTitle(resId);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setActionBarTitle(CharSequence title)
	{
		getSupportActionBar().setTitle(title);
	}

	/**
	 * 设置Home图标
	 * 
	 * @param resId
	 */
	public void setActionBarIcon(int resId)
	{
		getSupportActionBar().setIcon(resId);
	}

	/**
	 * 设置Home图标
	 * 
	 * @param icon
	 */
	public void setActionBarIcon(Drawable icon)
	{
		getSupportActionBar().setIcon(icon);
	}
	
	/**
	 * 设置显示方式
	 * 
	 * @param options
	 * @param mask
	 */
	public void setActionBarDisplayOptions(int options, int mask)
	{
		getSupportActionBar().setDisplayOptions(options, mask);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			// 默认对Home区域做"返回"处理.
			onActionBarBackClick();
		}
		else if (item.getItemId() == R.id.setting)
		{
			onActionBarSettingClick();
		}
		else if (item.getItemId() == R.id.add)
		{
			onActionBarAddClick();
		}
		else if (item.getItemId() == R.id.edit)
		{
			onActionBarEditClick();
		}
		else if (item.getItemId() == R.id.ok)
		{
			onActionBarOkClick();
		}
		else if (item.getItemId() == R.id.menu)
		{
			onActionBarMenuClick();
		}
		else if (item.getItemId() == R.id.search)
		{
			onActionBarSearchClick();
		}
		else if (item.getItemId() == R.id.more)
		{
			onActionBarMoreClick();
		}
		else if (item.getItemId() == R.id.share)
		{
			onActionBarShareClick();
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * ActionBar的返回操作, 依赖于是否已经重写onActionBarHomeClick().
	 * onActionBarHomeClick()返回true, 则表示已经消费引事件, 反之, 则由父类统一处理.
	 */
	protected void onActionBarBackClick()
	{
		if (!onActionBarHomeClick())
		{
			finish();
		}
	}

	/**
	 * 点击Home
	 */
	protected boolean onActionBarHomeClick()
	{
		return false;
	}
	
	/**
	 * 点击设置
	 */
	protected void onActionBarSettingClick()
	{
		
	}
	
	/**
	 * 点击添加
	 */
	protected void onActionBarAddClick()
	{

	}

	/**
	 * 点击编辑
	 */
	protected void onActionBarEditClick()
	{

	}

	/**
	 * 点击确认
	 */
	protected void onActionBarOkClick()
	{

	}

	/**
	 * 点击菜单
	 */
	protected void onActionBarMenuClick()
	{

	}

	/**
	 * 点击搜索
	 */
	protected void onActionBarSearchClick()
	{
		
	}
	
	/**
	 * 点击更多
	 */
	protected void onActionBarMoreClick()
	{
		
	}
	
	/**
	 * 点击分享
	 */
	protected void onActionBarShareClick()
	{
		
	}
	
	/**
	 * 此方法的回调基于PositiveButton的可见性, 只有当PositiveButton设置为{@link View#VISIBLE}时, 才会回调此方法.
	 */
	protected void onPositiveButtonClick()
	{

	}

	/**
	 * 是否需要显示ActionBar, hide()后会延时一会再完全消失.
	 * 
	 * @return
	 */
	protected boolean hasActionBar()
	{
		return true;
	}

	/**
	 * Common Resource会包含子系统的通用功能模块, 需要获取子应用的预先设定的图标; 如果子系统没有设定好图标, 则使用默认Logo.
	 * 
	 * @return
	 */
	public int getApplicationActionBarIcon()
	{
		int drawableId = -1;
		try
		{
			drawableId = this.getResources().getIdentifier("actionbar_icon", "drawable", this.getPackageName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			drawableId = R.drawable.wo2b_logo;
		}

		return drawableId;
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ActionBar Handler &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	@Override
	protected void onResume()
	{
		super.onResume();

		systemLoader(this);

		if (busEventEnable())
		{
			try
			{
				GBus.register(this);
			}
			catch (IllegalArgumentException e)
			{
				// 避免重复注册时会发生异常
				// e.printStackTrace();
			}
		}

		// 正式版本才进行用户行为收集
		MobclickAgentProxy.onPageStart(this.getClass().getName());
		MobclickAgentProxy.onResume(this);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		
		// POINTAT: 待实质验证
//		if (busEventEnable())
//		{
//			GBus.unregister(this);
//		}
		
		// 正式版本才进行用户行为收集
		MobclickAgentProxy.onPageEnd(this.getClass().getName());
		MobclickAgentProxy.onPause(this);
	}
	
	@Override
	protected void onDestroy()
	{
		// POINTAT: 待实质验证
		if (busEventEnable())
		{
			GBus.unregister(this);
		}

		if (mSubThread != null)
		{
			mSubThread.quit();
			mSubThread = null;
		}

		super.onDestroy();
	}
	
	/**
	 * 是否启用事件总线, 默认不启用
	 * 
	 * @return
	 */
	protected boolean busEventEnable()
	{
		return false;
	}
	
	/**
	 * startActivity
	 * 
	 * @param clazz
	 */
	public void startActivityExt(Class<? extends Activity> clazz)
	{
		Intent intent = new Intent();
		intent.setClass(this, clazz);

		super.startActivity(intent);
	}

	/**
	 * startActivity and finish
	 * 
	 * @param clazz
	 */
	public void startActivityExtAndFinish(Class<? extends Activity> clazz)
	{
		startActivityExt(clazz);
		finish();
	}


	protected boolean isRootActivity()
	{
		return false;
	}
	
	public Context getContext()
	{
		return mContext;
	}

	protected Handler getUiHandler()
	{
		return mUiHandler;
	}

	protected Handler getSubHandler()
	{
		return mSubHandler;
	}

	private Handler.Callback mUiHandlerCallback = new Handler.Callback()
	{

		@Override
		public boolean handleMessage(Message msg)
		{
			return uiHandlerCallback(msg);
		}
	};

	private Handler.Callback mSubHandlerCallback = new Handler.Callback()
	{

		@Override
		public boolean handleMessage(Message msg)
		{
			return subHandlerCallback(msg);
		}
	};

	/**
	 * UI更新请求消息处理函数
	 * 
	 * 子类可重载此函数，已处理自己的私有消息
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean uiHandlerCallback(Message msg)
	{
		return false;
	}

	/**
	 * 与界面无关的操作消息处理函数
	 * 
	 * 子类可重载此函数，已处理自己的私有消息
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean subHandlerCallback(Message msg)
	{
		return false;
	}

	/**
	 * Init view.
	 */
	protected void initView()
	{

	}

	/**
	 * Set default value to target view, or other object.
	 */
	protected void setDefaultValues()
	{

	}

	/**
	 * Bind events to target view.
	 */
	protected void bindEvents()
	{

	}

	public void showToast(String info)
	{
		if (mToast == null)
		{
			mToast = Toast.makeText(this, info, Toast.LENGTH_SHORT);
		}
		else
		{
			mToast.setText(info);
		}

		mToast.show();
	}

	public void showToast(int resId)
	{
		if (mToast == null)
		{
			mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
		}
		else
		{
			mToast.setText(resId);
		}

		mToast.show();
	}

	public void showToastOnUiThread(final int resId)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				showToast(resId);
			}
		});
	}

	public void showToastOnUiThread(final String info)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				showToast(info);
			}
		});
	}
	
	public void showToastOnUiThread(final int code, final String message)
	{
		runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				showToast("[" + code + ", " + message + "]");
			}
		});
	}

	// --------------- Normal Functions End ---------------------------------
	
	// --------------- System Override Start ---------------------------------
	/**
	 * 扩展Android系统内部的部分方法, 方便编码.
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T findViewByIdExt(int id)
	{
		return (T) super.findViewById(id);
	}

	// --------------- System Override End ---------------------------------
	
	
	/**
	 * 系统加载
	 */
	protected void systemLoader(Context context)
	{
		if (RockySdk.getInstance().getContext() != null)
		{
			return;
		}
		
		Context application = context.getApplicationContext();
		
		// 用于获取浏览器代理
		WebView webview = new WebView(context);
		webview.layout(0, 0, 0, 0);
		WebSettings webSettings = webview.getSettings();
		
		ApplicationInfo applicationInfo = context.getApplicationInfo();
		
		VersionInfo versionInfo = ManifestTools.getVersionInfo(application);
		String userAgent = webSettings.getUserAgentString();
		
		// Structure the ClientInfo.
		ClientInfo clientInfo = new ClientInfo(application.getPackageName());
		//clientInfo.setAppicon(R.drawable.ic_launcher);
		clientInfo.setAppicon(applicationInfo.icon);
		clientInfo.setAppname(ManifestTools.getApplicationLable(application));
		clientInfo.setDeviceType(SdkConfig.Device.PHONE);
		clientInfo.setDeviceName(android.os.Build.MODEL);
		clientInfo.setAlias(android.os.Build.MODEL);
		clientInfo.setSdkVersion(android.os.Build.VERSION.SDK_INT);
		clientInfo.setMac(DeviceInfoManager.getMacAddress(application));
		
		// Webkit user-agent
		clientInfo.setUserAgent(userAgent);
		
		if (versionInfo != null)
		{
			clientInfo.setVersionCode(versionInfo.getVersionCode());
			clientInfo.setVersionName(versionInfo.getVersionName());
		}
		
		// FIXME: Take attention...
		clientInfo.addFlags(ClientInfo.FLAG_DEBUG | ClientInfo.FLAG_RELEASE);
		// clientContext.addFlags(ClientContext.FLAG_DEBUG);
		
		// TODO: 广告
		RockyConfig config = new RockyConfig.Builder(application)
			.clientInfo(clientInfo)
			.hasAdBanner(false)		// 显示积分Banner
			.hasAdPointsWall(true)	// 显示积分墙
			.build();
		
		RockySdk.getInstance().init(config);
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Bottom Popup Menu Start &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
		/**
		 * 返回底部菜单栏
		 * 
		 * @return
		 */
		public LinearLayout getBottomMenu()
		{
			return (LinearLayout) findViewById(R.id.bottom_menu_container);
		}
		
		/**
		 * 设置底部菜单栏的背景颜色
		 * 
		 * @param resId
		 */
		public void setBottomPopupMenuBackground(int resId)
		{
			getBottomMenu().setBackgroundResource(resId);
		}
		
		public void setBottomPopupMenuVisibility(int visibility)
		{
			getBottomMenu().setVisibility(visibility);
		}
		
		/**
		 * 显示底部菜单栏
		 * 
		 * @param popupWindow
		 */
		public void showBottomPopupMenu(final PopupWindow popupWindow, final int background)
		{
			popupWindow.showAtLocation(rocky_rootView, Gravity.BOTTOM, 0, 0);
			getUiHandler().postDelayed(new Runnable()
			{
				
				@Override
				public void run()
				{
					setBottomPopupMenuBackground(background);
					setBottomPopupMenuVisibility(View.VISIBLE);
				}
			}, 500);
		}
		
		/**
		 * 隐藏底部菜单栏
		 * 
		 * @param popupWindow
		 */
		public void dismissBottomPopupMenu(final PopupWindow popupWindow)
		{
			setBottomPopupMenuVisibility(View.GONE);
			getUiHandler().post(new Runnable()
			{
				
				@Override
				public void run()
				{
					popupWindow.dismiss();
				}
			});
		}
		// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Bottom Popup Menu End &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	
	
}
