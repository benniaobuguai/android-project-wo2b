package com.wo2b.sdk.umeng;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

/**
 * 代理友盟行为统计
 * 
 * @author 笨鸟不乖
 * 
 */
public final class MobclickAgentProxy
{

	private static final String TAG = "Umeng.MobclickAgentProxy";

	public static boolean DEBUG = false;

	public static void setAutoLocation(boolean arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.setAutoLocation(arg0);
		}
	}

	public static void setWrapper(String arg0, String arg1)
	{
		if (!DEBUG)
		{
			MobclickAgent.setWrapper(arg0, arg1);
		}
	}

	public static void setSessionContinueMillis(long time)
	{
		if (!DEBUG)
		{
			MobclickAgent.setSessionContinueMillis(time);
		}
	}

	public static void setEnableEventBuffer(boolean arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.setEnableEventBuffer(arg0);
		}
	}

	public static void setOnlineConfigureListener(UmengOnlineConfigureListener arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.setOnlineConfigureListener(arg0);
		}
	}

	public static void setOpenGLContext(GL10 arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.setOpenGLContext(arg0);
		}
	}

	public static void openActivityDurationTrack(boolean arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.openActivityDurationTrack(arg0);
		}
	}

	public static void onPageStart(String arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.onPageStart(arg0);
		}
	}

	public static void onPageEnd(String arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.onPageEnd(arg0);
		}
	}

	public static void setDebugMode(boolean arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.setDebugMode(arg0);
		}
	}

	public static void onResume(Context arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.onResume(arg0);
		}
	}

	public static void onPause(Context arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.onPause(arg0);
		}
	}

	public static void onResume(Context arg0, String arg1, String arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onResume(arg0, arg1, arg2);
		}
	}

	public static void reportError(Context arg0, String arg1)
	{
		if (!DEBUG)
		{
			MobclickAgent.reportError(arg0, arg1);
		}
	}

	public static void reportError(Context arg0, Throwable arg1)
	{
		if (!DEBUG)
		{
			MobclickAgent.reportError(arg0, arg1);
		}
	}

	public static void flush(Context arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.flush(arg0);
		}
	}

	public static void onEvent(Context arg0, String arg1)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEvent(arg0, arg1);
		}
	}

	public static void onEvent(Context arg0, String arg1, int arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEvent(arg0, arg1, arg2);
		}
	}

	public static void onEvent(Context arg0, String arg1, String arg2, int arg3)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEvent(arg0, arg1, arg2, arg3);
		}
	}

	public static void onEvent(Context arg0, String arg1, String arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEvent(arg0, arg1, arg2);
		}
	}

	public static void onEvent(Context arg0, String arg1, HashMap<String, String> arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEvent(arg0, arg1, arg2);
		}
	}

	public static void onEventDuration(Context arg0, String arg1, long arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventDuration(arg0, arg1, arg2);
		}
	}

	public static void onEventDuration(Context arg0, String arg1, String arg2, long arg3)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventDuration(arg0, arg1, arg2, arg3);
		}
	}

	public static void onEventDuration(Context arg0, String arg1, HashMap<String, String> arg2, long arg3)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventDuration(arg0, arg1, arg2, arg3);
		}
	}

	public static void onEventBegin(Context arg0, String arg1)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventBegin(arg0, arg1);
		}
	}

	public static void onEventEnd(Context arg0, String arg1)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventEnd(arg0, arg1);
		}
	}

	public static void onEventBegin(Context arg0, String arg1, String arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventBegin(arg0, arg1, arg2);
		}
	}

	public static void onEventEnd(Context arg0, String arg1, String arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onEventEnd(arg0, arg1, arg2);
		}
	}

	public static void onKVEventBegin(Context arg0, String arg1, HashMap<String, String> arg2, String arg3)
	{
		if (!DEBUG)
		{
			MobclickAgent.onKVEventBegin(arg0, arg1, arg2, arg3);
		}
	}

	public static void onKVEventEnd(Context arg0, String arg1, String arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.onKVEventEnd(arg0, arg1, arg2);
		}
	}

	public static String getConfigParams(Context arg0, String arg1)
	{
		if (!DEBUG)
		{
			return MobclickAgent.getConfigParams(arg0, arg1);
		}
		else
		{
			Log.w(TAG, "Not support without release mode.");
			return null;
		}
	}

	public static void updateOnlineConfig(Context arg0, String arg1, String arg2)
	{
		if (!DEBUG)
		{
			MobclickAgent.updateOnlineConfig(arg0, arg1, arg2);
		}
	}

	public static void updateOnlineConfig(Context arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.updateOnlineConfig(arg0);
		}
	}

	public static void onKillProcess(Context arg0)
	{
		if (!DEBUG)
		{
			MobclickAgent.onKillProcess(arg0);
		}
	}

}
