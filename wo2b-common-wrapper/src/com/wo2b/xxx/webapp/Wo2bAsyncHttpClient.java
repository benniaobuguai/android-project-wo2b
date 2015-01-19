package com.wo2b.xxx.webapp;

import org.apache.http.params.CoreProtocolPNames;

import com.wo2b.sdk.common.util.http.AsyncHttpClient;
import com.wo2b.sdk.core.RockySdk;

/**
 * 针对www.wo2b.com网站所有请求的封装
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public final class Wo2bAsyncHttpClient
{

	/**
	 * 保证只有一个实例
	 */
	private static volatile AsyncHttpClient asyncHttpClient;

	static
	{
		asyncHttpClient = new AsyncHttpClient();
	}

	/**
	 * 全局唯一的AsyncHttpClient对象
	 * 
	 * @return
	 */
	public static AsyncHttpClient newAsyncHttpClient()
	{
		String userAgent = null;
		if (RockySdk.getInstance().getClientInfo() != null)
		{
			userAgent = RockySdk.getInstance().getClientInfo().getUserAgent();
		}
		if (userAgent == null)
		{
			userAgent = "Mozilla/5.0 (Linux; U; Android 4.3; zh-cn; GT-I9300 Build/JSS15J) "
					+ "AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.3022";
		}

		asyncHttpClient.getHttpClient().getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent + ".10");

		return asyncHttpClient;
	}

}
