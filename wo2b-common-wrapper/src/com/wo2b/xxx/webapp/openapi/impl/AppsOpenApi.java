package com.wo2b.xxx.webapp.openapi.impl;

import java.util.List;

import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.manager.app.AppInfo;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 个人推荐/第三方接入推荐
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class AppsOpenApi extends OpenApi
{

	/**
	 * Rocky个人作品
	 * 
	 * @param offset
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final List<AppInfo> getRockyApps(int offset, int count)
	{
		RequestParams params = new RequestParams();
		params.put("offset", offset);
		params.put("count", count);

		return (List<AppInfo>) postSyncGetList("/mobile/Apps_rocky", params, AppInfo.class);
	}

}
