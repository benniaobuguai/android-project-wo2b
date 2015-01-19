package com.wo2b.xxx.webapp.openapi.impl;

import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.like.Like;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class LikeOpenApi extends OpenApi
{

	/**
	 * 添加或更新当前主题的点赞次数
	 * 
	 * @param pkgname
	 * @param module
	 * @param titleId
	 * @param title
	 * @throws HttpRequestException
	 */
	public void addLike(String pkgname, String module, String titleId, String title, Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("pkgname", pkgname);
		params.put("module", module);
		params.put("titleId", titleId);
		params.put("title", title);

		post("/mobile/Like_addLike", params, wo2bResHandler);
	}

	/**
	 * 查询点赞次数
	 * 
	 * @param pkgname
	 * @param module
	 * @param titleId
	 * @param title
	 * @return
	 * @throws HttpRequestException
	 */
	public void findLike(String pkgname, String module, String titleId, String title,
			Wo2bResHandler<Like> wo2bResHandler)
	{
		final RequestParams params = new RequestParams();
		params.put("pkgname", pkgname);
		params.put("module", module);
		params.put("titleId", titleId);
		params.put("title", title);
		post("/mobile/Like_findLike", params, wo2bResHandler);
	}

}
