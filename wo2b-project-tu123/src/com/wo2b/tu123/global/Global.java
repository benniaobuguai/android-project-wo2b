package com.wo2b.tu123.global;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.wo2b.sdk.assistant.upgrade.VersionInfo;
import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.common.util.ManifestTools;
import com.wo2b.sdk.config.SdkConfig;
import com.wo2b.sdk.core.ClientInfo;
import com.wo2b.sdk.core.RockyConfig;
import com.wo2b.sdk.core.RockySdk;

/**
 * 全局信息及控制等
 * 
 * @author 笨鸟不乖
 * 
 */
public class Global
{

	/**
	 * 调试信息
	 */
	public static boolean Debug = true;

	/**
	 * 在此时间间隔内的连续操作只取第一次操作, 后续操作均会忽略.
	 */
	public static final int VIEW_REPEAT_CLICK_TIME_SPAN = 800;

	/**
	 * 应用版本, 默认为免费版
	 */
	public static Version mVersion = Version.Defualt;
	
	/**
	 * 系统缓存标识符
	 */
	public static final String SYSTEM_CACHE_PREFIX = "g_s_";

	/**
	 * 全局缓存标识符
	 */
	public static final String GLOBAL_CACHE_PREFIX = "g_c_";

	/**
	 * 生成系统缓存索引值
	 * 
	 * @param key
	 * @return
	 */
	public static String systemCacheKey(String key)
	{
		return SYSTEM_CACHE_PREFIX + key;
	}

	/**
	 * 生成全局缓存索引值
	 * 
	 * @param key
	 * @return
	 */
	public static String globalCacheKey(String key)
	{
		return GLOBAL_CACHE_PREFIX + key;
	}
	
	/**
	 * 系统初始化
	 * 
	 * @param context
	 */
	public static void init(Context context)
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

	/**
	 * 应用版本
	 * 
	 * <pre>
	 * 0-默认版; 1-其它版本
	 * 
	 * </pre>
	 */
	public static enum Version
	{

		/**
		 * 默认版
		 */
		Defualt(0, "default", "com.wo2b.tu123");

		/**
		 * 
		 */
		public int mType;

		/**
		 * 唯一标识
		 */
		public String mAppId;

		/**
		 * 包名
		 */
		public String mPkgName;

		Version(int type, String appId, String pkgName)
		{
			this.mType = type;
			this.mAppId = appId;
			this.mPkgName = pkgName;
		}

	}

}
