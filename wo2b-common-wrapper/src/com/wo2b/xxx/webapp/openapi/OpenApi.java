package com.wo2b.xxx.webapp.openapi;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wo2b.sdk.common.util.http.AsyncHttpClient;
import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.sdk.common.util.http.extra.SyncHttpResponse;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.xxx.webapp.Res;
import com.wo2b.xxx.webapp.Wo2bAsyncHttpClient;
import com.wo2b.xxx.webapp.Wo2bResHandler;

/**
 * 开放平台接口
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class OpenApi
{

	public static final String TAG = "OpenApi";

	protected static final String BASE_URL = OpenApiUrl.getBaseUrl();

	protected AsyncHttpClient asyncHttpClient = Wo2bAsyncHttpClient.newAsyncHttpClient();

	/**
	 * 网络异步请求
	 * 
	 * @param path 请求地址
	 * @param params 参数
	 * @param wo2bResHandler 回调
	 */
	public <Result> void get(String path, RequestParams params, Wo2bResHandler<Result> wo2bResHandler)
	{
		params.put("pkgname", RockySdk.getInstance().getClientInfo().getPkgname());
		asyncHttpClient.get(BASE_URL + path, params, wo2bResHandler);
	}

	/**
	 * 网络异步请求
	 * 
	 * @param path 请求地址
	 * @param wo2bResHandler 回调
	 */
	public <Result> void post(String path, Wo2bResHandler<Result> wo2bResHandler)
	{
		asyncHttpClient.post(BASE_URL + path, wo2bResHandler);
	}

	/**
	 * 网络异步请求
	 * 
	 * @param path 请求地址
	 * @param params 参数
	 * @param wo2bResHandler 回调
	 */
	public <Result> void post(String path, RequestParams params, Wo2bResHandler<Result> wo2bResHandler)
	{
		params.put("pkgname", RockySdk.getInstance().getClientInfo().getPkgname());
		asyncHttpClient.post(BASE_URL + path, params, wo2bResHandler);
	}

	/**
	 * 返回一个对象
	 * 
	 * @param path 请求地址
	 * @param params 参数
	 * @param clazz 指定的对象
	 * @return
	 */
	public Object postSyncGetObject(String path, RequestParams params, Class<?> clazz)
	{
		Res res = postSync(path, params);
		if (res != null && res.isOK())
		{
			Object object = JSON.parseObject(res.getData(), clazz);

			return object;
		}

		return null;
	}

	/**
	 * 返回一个集合
	 * 
	 * @param path 请求地址
	 * @param params 参数
	 * @param clazz 指定的对象
	 * @return
	 */
	public List<?> postSyncGetList(String path, RequestParams params, Class<?> clazz)
	{
		Res res = postSync(path, params);
		if (res != null && res.isOK())
		{
			List<?> appList = JSON.parseArray(res.getDataJSONArrayString(), clazz);

			return appList;
		}

		return null;
	}

	/**
	 * 同步请求, 源请求
	 * 
	 * @param path 请求地址
	 * @param parameters 参数
	 * @return
	 */
	public Res postSync(String path, RequestParams params)
	{
		params.put("pkgname", RockySdk.getInstance().getClientInfo().getPkgname());

		SyncHttpResponse httpResponse = com.wo2b.sdk.common.util.http.extra.SyncHttpClient.postSync(asyncHttpClient,
				BASE_URL + path, params);

		if (httpResponse != null && httpResponse.isOK())
		{
			// 得到正常的响应
			String jsonString = httpResponse.getContent();
			Res res = JSONObject.parseObject(jsonString, Res.class);

			return res;
		}

		return null;
	}

}
