package com.wo2b.wrapper.app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.common.download.DownloadManager;
import com.wo2b.sdk.common.util.CommonUtils;
import com.wo2b.sdk.core.cache.RockyCacheFactory;
import com.wo2b.wrapper.R;

/**
 * WebView Activity.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class RockyWebViewActivity extends RockyFragmentActivity implements OnClickListener
{

	public static final String EXTRA_LOAD_URL = "load_url";
	public static final String EXTRA_TITLE_BAR_FLAG = "title_bar_flag";
	public static final String EXTRA_BOTTOM_BAR_FLAG = "bottom_bar_flag";
	public static final String EXTRA_LAYER_TYPE = "layer_type";
	
	private WebView mWebView;			// WebView
	private ProgressBar mProgressbar;	// 加载进度条
	private ViewStub mViewStubBottom;	// 底部
	private ImageButton mBrowserClose;		// 关闭
	private ImageButton mBrowserBack;		// 返回
	private ImageButton mBrowserForward;	// 前进
	private ImageButton mBrowserRefresh;	// 刷新

	/**
	 * 进度条长度
	 */
	public final int PROGRESS_MAX = 100;

	/**
	 * 没标题栏
	 */
	public static final int FEATURE_NO_TITLE = 1;

	/**
	 * 没底部菜单栏
	 */
	public static final int FEATURE_NO_BOTTOM = 2;

	/**
	 * 默认没有标题栏
	 */
	public static final int DEFAULT_FEATURES = FEATURE_NO_TITLE;

	/**
	 * 特性
	 */
	private int mFeatures = DEFAULT_FEATURES;
	
	/**
	 * 引入此要素, 主要原因是播放视频时, 需要使用到硬件加速, 常规内容网站不需要硬件回事.
	 * 部分机型, 有可能开户硬件加速无法正常显示网页内容, 出现白屏现象.
	 */
	public static final int LAYER_TYPE_NONE = View.LAYER_TYPE_NONE;
	public static final int LAYER_TYPE_SOFTWARE = View.LAYER_TYPE_SOFTWARE;
	public static final int LAYER_TYPE_HARDWARE = View.LAYER_TYPE_HARDWARE;

	public static final int DEFAULT_LAYER_TYPE = LAYER_TYPE_SOFTWARE;

	private int mLayerType;
	
	/**
	 * 当前访问的地址
	 */
	private String mLoadUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.wrapper_webview);

		Intent intent = getIntent();
		mLoadUrl = intent.getStringExtra(EXTRA_LOAD_URL);
		boolean mHasTitleBar = intent.getBooleanExtra(EXTRA_TITLE_BAR_FLAG, true);
		boolean mHasBottomBar = intent.getBooleanExtra(EXTRA_BOTTOM_BAR_FLAG, false);
		mLayerType = intent.getIntExtra(EXTRA_LAYER_TYPE, DEFAULT_LAYER_TYPE);

		if (mHasTitleBar)
		{
			mFeatures |= (1 << FEATURE_NO_TITLE);
		}
		if (mHasBottomBar)
		{
			mFeatures |= (1 << FEATURE_NO_BOTTOM);
		}

		setUpViews();
		setUpEvents();

		if (!TextUtils.isEmpty(mLoadUrl))
		{
			mWebView.loadUrl(mLoadUrl);
		}
	}
	
	protected void setUpViews()
	{
		mWebView = (WebView) findViewById(R.id.rocky_webview);
		mProgressbar = (ProgressBar) findViewById(R.id.progressBar);
		mViewStubBottom = (ViewStub) findViewById(R.id.viewstub_bottom);

		final int features = mFeatures;
		if ((features & (1 << FEATURE_NO_TITLE)) != 0)
		{
			// Has ActionBar

		}
		else
		{
			// Has not ActionBar
			hideActionBar(getSupportActionBar());
		}

		if ((features & (1 << FEATURE_NO_BOTTOM)) != 0)
		{
			// Has BottomBar
			View bottomView = mViewStubBottom.inflate();
			mBrowserClose = (ImageButton) bottomView.findViewById(R.id.browser_close);
			mBrowserBack = (ImageButton) bottomView.findViewById(R.id.browser_back);
			mBrowserForward = (ImageButton) bottomView.findViewById(R.id.browser_forward);
			mBrowserRefresh = (ImageButton) bottomView.findViewById(R.id.browser_refresh);
		}
		else
		{
			// Has not BottomBar
			hideBottomBar(mViewStubBottom);
		}
		
		mProgressbar.setMax(PROGRESS_MAX);
		WebSettings settings = mWebView.getSettings();
		settings.setDefaultTextEncodingName("utf-8");
		// 支持JS
		settings.setJavaScriptEnabled(true);
		// 设置可以支持缩放
		settings.setSupportZoom(true);
		// 设置默认缩放方式尺寸是far
		//settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
		// 设置出现缩放工具
		// settings.setBuiltInZoomControls(true);

		String packageName = getPackageName();
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setDatabasePath("/data/data/" + packageName + "/databases");
		settings.setPluginState(PluginState.ON);//设置webview支持插件, 如flash插件等
		settings.setUseWideViewPort(true);
	
		// 显示放大或缩小按钮
		// settings.setBuiltInZoomControls(true);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		settings.setLoadWithOverviewMode(true);
		settings.setSavePassword(true);
		settings.setSaveFormData(true);
		// enable navigator.geolocation
		// settings.setGeolocationEnabled(true);
		// settings.setGeolocationDatabasePath("/data/data/" + packageName + ".html5webview/databases/");
		// enable Web Storage: localStorage, sessionStorage
		settings.setDomStorageEnabled(true);
		
		if (android.os.Build.VERSION.SDK_INT >= 11)
		{
			if (mLayerType == LAYER_TYPE_SOFTWARE || mLayerType == LAYER_TYPE_HARDWARE || mLayerType == LAYER_TYPE_NONE)
			{
				mWebView.setLayerType(mLayerType, null);
			}
			else
			{
				// 默认不启用GPU, 会造成部分页面无法加载.
				mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
		}
		
		
		// 垂直滚动条总是显示白色轨迹底图(无法消掉)
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		mWebView.requestFocus();
		mWebView.setWebViewClient(new WebViewClient()
		{

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon)
			{
				super.onPageStarted(view, url, favicon);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
			}
		});

		mWebView.setWebChromeClient(new MyWebChromeClient(this));

		// 添加桥
		mWebView.addJavascriptInterface(new AndroidBridge(), "Android");

		// 下载
		mWebView.setDownloadListener(new DownloadListener()
		{

			@Override
			public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
					long contentLength)
			{
				if (WebViewDownloadSettings.downloadUnknownAble(url))
				{
					String saveDirectory = RockyCacheFactory.getWo2bDownloadDir() + "www.wo2b.com";
					DownloadManager.newInstance().download(getApplicationContext(), url, saveDirectory, null);
				}
				else
				{
					// 在低版本的操作系统, 发现点击播放优酷视频, 也会回调此接口, 因此, 在未测试充分前, 暂时屏蔽提示, 会更友好一些.
					// showToast(R.string.hint_not_supported_download_tp);
				}
			}
		});
		
	}

	/**
	 * Request Feature
	 * 
	 * @param featureId
	 * @return
	 */
	public boolean realRequestFeature(int featureId)
	{
		Log.D(TAG, "[requestFeature] featureId: " + featureId);
		switch (featureId)
		{
			case FEATURE_NO_TITLE:
			{
				// mFeatures |= (1 << featureId);
				getIntent().putExtra(EXTRA_TITLE_BAR_FLAG, false);
				return true;
			}
			case FEATURE_NO_BOTTOM:
			{
				getIntent().putExtra(EXTRA_BOTTOM_BAR_FLAG, false);
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
	
	protected void setUpEvents()
	{
		int features = mFeatures;
		if ((features & (1 << FEATURE_NO_BOTTOM)) != 0)
		{
			mBrowserClose.setOnClickListener(this);
			mBrowserBack.setOnClickListener(this);
			mBrowserForward.setOnClickListener(this);
			mBrowserRefresh.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.browser_close)
		{
			onBrowserClose();
		}
		if (v.getId() == R.id.browser_back)
		{
			onBrowserBack();
		}
		else if (v.getId() == R.id.browser_forward)
		{
			onBrowserForward();
		}
		else if (v.getId() == R.id.browser_refresh)
		{
			onBrowserRefresh();
		}
	}

	/**
	 * 关闭
	 * 
	 * @return
	 */
	public void onBrowserClose()
	{
		finish();
	}

	/**
	 * 后退
	 * 
	 * @return
	 */
	public void onBrowserBack()
	{
		if (mWebView.canGoBack())
		{
			mWebView.goBack();
		}
	}

	/**
	 * 前进
	 */
	public void onBrowserForward()
	{
		if (mWebView.canGoForward())
		{
			mWebView.goForward();
		}
	}

	/**
	 * 刷新
	 */
	public void onBrowserRefresh()
	{
		mWebView.reload();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mWebView.onResume();
	}
	
	/**
	 * 捕捉物理返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if (event.getAction() == KeyEvent.ACTION_DOWN)
			{
				if (mWebView.canGoBack())
				{
					mWebView.goBack();

					return true;
				}
				else
				{
					// 不能返回直接退出
					finish();
				}
			}
		}

		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 返回WebView对象
	 * 
	 * @return
	 */
	public WebView getWebview()
	{
		return mWebView;
	}
	
	@Override
	public void finish()
	{
		super.finish();
		// resetWebView();
	}

	public void resetWebView()
	{
		mWebView.pauseTimers();
		mWebView.stopLoading();
		mWebView.loadData("<a></a>", "text/html", "utf-8");
	}

	/**
	 * 解析BottomBar
	 */
	private void inflateBottomBar()
	{

	}

	/**
	 * 隐藏ActionBar
	 * 
	 * @param actionBar
	 */
	private void hideActionBar(ActionBar actionBar)
	{
		actionBar.hide();
	}

	/**
	 * 隐藏BottomBar
	 */
	private void hideBottomBar(View view)
	{

	}
	
	// ===============================================================================================================
	// ===============================================================================================================
	// ### WebChromeClient 接口的实现, 提取出外部方便由子类去实现

	/**
	 * 进度变化
	 * 
	 * @param view
	 * @param newProgress
	 */
	public void onProgressChanged(WebView view, int newProgress)
	{
		if (newProgress == PROGRESS_MAX)
		{
			mProgressbar.setVisibility(View.GONE);
		}
		else
		{
			if (mProgressbar.getVisibility() != View.VISIBLE)
			{
				mProgressbar.setVisibility(View.VISIBLE);
			}

			mProgressbar.setProgress(newProgress);
		}
	}

	/**
	 * 接收到HTML标题
	 * 
	 * @param view
	 * @param title
	 */
	public void onReceivedTitle(WebView view, String title)
	{
		if (!TextUtils.isEmpty(title))
		{
			if (getSupportActionBar().isShowing())
			{
				getSupportActionBar().setTitle(title);
			}
		}
	}

	public void onReceivedIcon(WebView view, Bitmap icon)
	{
	}

	public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed)
	{
	}

	public boolean onJsAlert(WebView view, String url, String message, JsResult result)
	{
		return false;
	}

	public boolean onJsConfirm(WebView view, String url, String message, JsResult result)
	{
		return false;
	}

	public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result)
	{
		return false;
	}

	public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result)
	{
		return false;
	}

	public boolean onJsTimeout()
	{
		return true;
	}

	
	private View myView = null;
	private CustomViewCallback myCallback = null;

	public View getVideoLoadingProgressView()
	{
		return null;
	}
	
	public void onShowCustomView(View view, CustomViewCallback callback)
	{
		if (myCallback != null)
		{
			myCallback.onCustomViewHidden();
			myCallback = null;
			return;
		}

		long id = Thread.currentThread().getId();
		Log.I("WidgetChromeClient", "rong debug in showCustomView Ex: " + id);

		ViewGroup parent = (ViewGroup) mWebView.getParent();
		String s = parent.getClass().getName();
		Log.I("WidgetChromeClient", "rong debug Ex: " + s);
		parent.removeView(mWebView);
		parent.addView(view);
		myView = view;
		myCallback = callback;
		// chromeClient = this ;
	}

	public void onHideCustomView()
	{
		long id = Thread.currentThread().getId();
		Log.I("WidgetChromeClient", "rong debug in hideCustom Ex: " + id);

		if (myView != null)
		{
			if (myCallback != null)
			{
				myCallback.onCustomViewHidden();
				myCallback = null;
			}

			ViewGroup parent = (ViewGroup) myView.getParent();
			parent.removeView(myView);
			parent.addView(mWebView);
			myView = null;
		}
	}
	
	// ### End WebChromeClient
	// ===============================================================================================================
	
	/**
	 * 组合代替继承
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-9-24
	 */
	public static class MyWebChromeClient extends WebChromeClient
	{

		private RockyWebViewActivity mWebViewActivity;

		public MyWebChromeClient(RockyWebViewActivity webViewActivity)
		{
			this.mWebViewActivity = webViewActivity;
		}

		@Override
		public void onReceivedTitle(WebView view, String title)
		{
			this.mWebViewActivity.onReceivedTitle(view, title);
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress)
		{
			mWebViewActivity.onProgressChanged(view, newProgress);
		}

		@Override
		public View getVideoLoadingProgressView()
		{
			Log.I(TAG, "getVideoLoadingProgressView--> Callback()");

			return this.mWebViewActivity.getVideoLoadingProgressView();
		}

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback)
		{
			this.mWebViewActivity.onShowCustomView(view, callback);
		}

		@Override
		public void onHideCustomView()
		{
			this.mWebViewActivity.onHideCustomView();
		}

	}
	
	/**
	 * JS桥
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-9-26
	 */
	private class AndroidBridge
	{

		@JavascriptInterface
		public void testCallback()
		{

		}

	}

	/**
	 * 为保障安全, 仅能下载www.wo2b.com主机的
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-10-20
	 */
	public static class WebViewDownloadSettings
	{

		/**
		 * 启用下载文件过滤器
		 */
		private static boolean DOWNLOAD_FILTER_ENABLE = true;

		/**
		 * 是否允许下载当前文件
		 * 
		 * @param url 下载文件地址
		 * @return
		 */
		public static boolean downloadUnknownAble(String url)
		{
			if (DOWNLOAD_FILTER_ENABLE)
			{
				return CommonUtils.isWo2bHost(url);
			}

			// 不启用, 即所有网站的所有资源都可以回调下载接口.
			return true;
		}

	}

}
