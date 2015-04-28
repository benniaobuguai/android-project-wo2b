package com.wo2b.sdk.assistant.upgrade;

/**
 * 升级操作接口
 * 
 * @author 笨鸟不乖
 * 
 */
public interface UpgradeHandler
{
	
	/**
	 * 启动更新检查
	 */
	void startCheck();
	
	/**
	 * 找到新版本
	 * 
	 * @param clientVersion
	 * @param serverVersion
	 */
	void versionFound(VersionInfo clientVersion, VersionInfo serverVersion);
	
	/**
	 * 找不到新版本, 当前应用程序已经是最新版本了.
	 * 
	 * @param clientVersion
	 * @return
	 */
	void versionNotFound(VersionInfo clientVersion);
	
	/**
	 * 检查失败
	 * 
	 * @param message
	 */
	void checkOnError();
	
	/**
	 * 应用升级
	 * 
	 * @param cVersion
	 * @param sVersion
	 * @param upgradeUrl
	 */
	void upgrade(VersionInfo clientVersion, VersionInfo serverVersion);
	
}
