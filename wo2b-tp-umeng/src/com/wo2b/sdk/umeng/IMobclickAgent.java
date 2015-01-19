package com.wo2b.sdk.umeng;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;

/**
 * 所有接口来源于友盟MobclickAgent.java
 * 
 * @author Rocky
 * 
 */
public interface IMobclickAgent
{
	
	void setAutoLocation(boolean paramBoolean);
	
	void setWrapper(String paramString1, String paramString2);
	
	void setSessionContinueMillis(long paramLong);
	
	void setEnableEventBuffer(boolean paramBoolean);
	
	void setOnlineConfigureListener(UmengOnlineConfigureListener paramUmengOnlineConfigureListener);
	
	void setOpenGLContext(GL10 paramGL10);
	
	void openActivityDurationTrack(boolean paramBoolean);
	
	void onPageStart(String paramString);
	
	void onPageEnd(String paramString);
	
	void setDebugMode(boolean paramBoolean);
	
	void onPause(Context paramContext);
	
	void onResume(Context paramContext);
	
	void onResume(Context paramContext, String paramString1, String paramString2);
	
	void onError(Context paramContext);
	
	void onError(Context paramContext, String paramString);
	
	void reportError(Context paramContext, String paramString);
	
	void reportError(Context paramContext, Throwable paramThrowable);
	
	void flush(Context paramContext);
	
	void onEvent(Context paramContext, String paramString);
	
	void onEvent(Context paramContext, String paramString, int paramInt);
	
	void onEvent(Context paramContext, String paramString1, String paramString2, int paramInt);
	
	void onEvent(Context paramContext, String paramString1, String paramString2);
	
	void onEvent(Context paramContext, String paramString, HashMap<String, String> paramHashMap);
	
	void onEventDuration(Context paramContext, String paramString, long paramLong);
	
	void onEventDuration(Context paramContext, String paramString1, String paramString2, long paramLong);
	
	void onEventDuration(Context paramContext, String paramString, HashMap<String, String> paramHashMap, long paramLong);
	
	void onEventBegin(Context paramContext, String paramString);
	
	void onEventEnd(Context paramContext, String paramString);
	
	void onEventBegin(Context paramContext, String paramString1, String paramString2);
	
	void onEventEnd(Context paramContext, String paramString1, String paramString2);
	
	void onKVEventBegin(Context paramContext, String paramString1, HashMap<String, String> paramHashMap,
			String paramString2);
	
	void onKVEventEnd(Context paramContext, String paramString1, String paramString2);
	
	String getConfigParams(Context paramContext, String paramString);
	
	void updateOnlineConfig(Context paramContext, String paramString1, String paramString2);
	
	void updateOnlineConfig(Context paramContext);
	
	// void setGender(Context paramContext, Gender paramGender);
	
	// void setAge(Context paramContext, int paramInt);
	
	// void setUserID(Context paramContext, String paramString1, String
	// paramString2);
	
	void onKillProcess(Context paramContext);
	
}