package com.wo2b.sdk.assistant.upgrade;


/**
 * 应用升级接口
 * 
 * @author Rocky
 * 
 */
public interface IUpgrade
{
	
	/**
	 * 获取应用当前的版本信息
	 * 
	 * @return
	 */
	VersionInfo getClientVersionInfo();
	
	/**
	 * 获取服务器最新版本信息
	 * 
	 * @param requestUrl
	 * @return
	 * @throws HttpRequestException
	 */
	VersionInfo getServerVersionInfo(String requestUrl) throws Exception;
	
	/**
	 * 返回是否需要升级
	 * 
	 * @param client
	 * @param server
	 * @return
	 */
	boolean isUpgrade(VersionInfo client, VersionInfo server);
	
}
