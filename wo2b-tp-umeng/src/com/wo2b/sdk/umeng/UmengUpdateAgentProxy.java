package com.wo2b.sdk.umeng;

import android.content.Context;

import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;

/**
 * 更新代理
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-19
 */
public class UmengUpdateAgentProxy
{

	/**
	 * 自动更新检查
	 * 
	 * @param context
	 */
	public static void update(Context context)
	{
		UmengUpdateAgent.setDefault();
		UmengUpdateAgent.setDeltaUpdate(true);
		UmengUpdateAgent.update(context);
	}

	/**
	 * 强制更新
	 * 
	 * @param context
	 * @param listener
	 */
	public static void forceUpdate(Context context, UmengUpdateListener listener)
	{
		// 检查更新
		UmengUpdateAgent.forceUpdate(context);
		UmengUpdateAgent.setUpdateListener(listener);
	}

}
