package com.wo2b.xxx.webapp;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 基于与http://www.wo2b.com接口交互约定数据格式, 比较好处理方式.<br />
 * 
 * <ul>
 * <li>1. 常规对象, 直接指定POJO对象, 属性值与服务器端的保存一致.</li>
 * <li>2. 如果想自行解释, 直接指定JSONObject即可.</li>
 * <li>3. 不需要返回值的, 直接使用Void.</li>
 * 
 * </ul>
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public abstract class Wo2bResListHandler<Result> extends Wo2bResHandler<Result>
{

	/**
	 * Fired when a request returns successfully, override to handle in your own code
	 * 
	 * @param code
	 * @param result
	 */
	public abstract void onSuccess(int code, List<Result> result);

	/**
	 * Fired when a request fails to complete, override to handle in your own code
	 * 
	 * @param code
	 * @param msg
	 */
	public abstract void onFailure(int code, String msg, Throwable throwable);

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBytes)
	{
		String responseString = getResponseString(responseBytes, getCharset());

		Res res = null;
		try
		{
			res = JSONObject.parseObject(responseString, Res.class);
		}
		catch (Exception e)
		{
			// FIXME: 偶发异常, 暂未明确异常
			// e.printStackTrace();
		}

		if (res == null)
		{
			return;
		}

		if (res != null && res.isOK())
		{
			Class<Result> resultClass = (Class<Result>) ((ParameterizedType) getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
			List<Result> result = JSON.parseArray(res.getDataJSONArrayString(), resultClass);

			onSuccess(res.getCode(), result);
		}
		else if (res.getCode() == Wo2bCode.C203)
		{
			// FIXME: 没有登录, 全局异常处理暂时出现问题.
			// throw new SdkNoLoginException("Can't access [" + getRequestURI() + "] without login!!!");

			sessionTimeoutOrNoLogin();
		}
		else
		{
			// 其它情况, 全表示操作失败
			onFailure(res.getCode(), res.getMsg(), new UnknownError("服务器错误"));
		}
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable)
	{
		// 异常统一由父类处理
		super.onFailure(statusCode, headers, responseBytes, throwable);
	}

	// -------------------------------------------------------------------------------------------
	// ----------------------- Implements abstract method, but do nothing. -----------------------
	// -------------------------------------------------------------------------------------------
	@Override
	public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable)
	{

	}

	@Override
	public void onSuccess(int statusCode, Header[] headers, String responseString)
	{

	}

	@Override
	public void onSuccess(int code, Result result)
	{

	}

}
