package com.wo2b.xxx.webapp.manager.app;

import java.util.List;

import com.wo2b.xxx.webapp.openapi.impl.AppsOpenApi;

/**
 * 个人推荐/第三方接入推荐
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class AppManager
{

	private AppsOpenApi mAppsOpenApi = new AppsOpenApi();

	/**
	 * Rocky个人作品
	 * 
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<AppInfo> getRockyApps(int offset, int count)
	{
		return mAppsOpenApi.getRockyApps(offset, count);
	}

}
