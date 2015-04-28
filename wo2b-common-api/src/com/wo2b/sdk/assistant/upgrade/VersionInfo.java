package com.wo2b.sdk.assistant.upgrade;

import java.io.Serializable;

/**
 * 版本信息
 * 
 * @author 笨鸟不乖
 * 
 */
public class VersionInfo implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	private String appName;
	
	private int versionCode;
	
	private String versionName;
	
	private String updateUrl;
	
	public String getAppName()
	{
		return appName;
	}
	
	public void setAppName(String appName)
	{
		this.appName = appName;
	}
	
	public int getVersionCode()
	{
		return versionCode;
	}
	
	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}
	
	public String getVersionName()
	{
		return versionName;
	}
	
	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}
	
	public String getUpdateUrl()
	{
		return updateUrl;
	}
	
	public void setUpdateUrl(String updateUrl)
	{
		this.updateUrl = updateUrl;
	}
	
}
