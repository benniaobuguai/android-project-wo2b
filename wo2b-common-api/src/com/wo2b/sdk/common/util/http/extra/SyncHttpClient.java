package com.wo2b.sdk.common.util.http.extra;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.wo2b.sdk.common.util.http.AsyncHttpClient;
import com.wo2b.sdk.common.util.http.RequestParams;

/**
 * Sync http client
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class SyncHttpClient
{

	private static final String TAG = "SyncHttpClient";

	/**
	 * 同步网络请求, 不能在UI线程直接使用, 请在工作线程使用.
	 * 
	 * @param path 请求地址
	 * @param params 参数
	 */
	public static SyncHttpResponse getSync(AsyncHttpClient asyncHttpClient, String path, RequestParams params)
	{
		HttpGet httpGet = new HttpGet(path);
//		if (params != null)
//		{
//			HttpEntity entity = RequestParamsUtil.createFormEntity(params);
//			httpGet.setParams(params);
//		}

		try
		{
			HttpResponse httpResponse = asyncHttpClient.getHttpClient().execute(httpGet);
			SyncHttpResponse httpRequestResult = getHttpRequestResult(httpResponse);

			return httpRequestResult;
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 同步网络请求, 不能在UI线程直接使用, 请在工作线程使用.
	 * 
	 * @param path 请求地址
	 * @param params 参数
	 */
	public static SyncHttpResponse postSync(AsyncHttpClient asyncHttpClient, String path, RequestParams params)
	{
		HttpPost httpPost = new HttpPost(path);
		if (params != null)
		{
			HttpEntity entity = RequestParamsUtil.createFormEntity(params);
			httpPost.setEntity(entity);
		}

		try
		{
			HttpResponse httpResponse = asyncHttpClient.getHttpClient().execute(httpPost);
			SyncHttpResponse httpRequestResult = getHttpRequestResult(httpResponse);

			return httpRequestResult;
		}
		catch (ClientProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 封装HTTP响应为HttpRequestResult对象
	 * 
	 * @param httpResponse
	 * @return
	 */
	private static SyncHttpResponse getHttpRequestResult(HttpResponse httpResponse)
	{
		if (httpResponse == null)
		{
			Log.w(TAG, "responseToHttpResult: httpResponse is null.");
			// 为了方便上层判断, 直接返回一个新的对象.
			return new SyncHttpResponse();
		}

		SyncHttpResponse httpResult = new SyncHttpResponse();
		httpResult.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		httpResult.setHeaders(httpResponse.getAllHeaders());

		HttpEntity httpEntity = httpResponse.getEntity();
		if (httpEntity != null)
		{
			try
			{
				// NOTICE: 如果编码有变化, 请修改这里
				String content = EntityUtils.toString(httpEntity, HTTP.UTF_8);
				httpResult.setContent(content);
			}
			catch (ParseException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		return httpResult;
	}

}
