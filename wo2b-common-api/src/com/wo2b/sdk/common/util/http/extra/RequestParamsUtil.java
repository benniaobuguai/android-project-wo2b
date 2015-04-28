package com.wo2b.sdk.common.util.http.extra;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.wo2b.sdk.common.util.http.RequestParams;

/**
 * 辅助工具, 主要针对{@link RequestParams}部分方法, 在外部无法访问
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class RequestParamsUtil
{

	private static final String TAG = "RequestParamsUtil";

	/**
	 * 
	 * @param requestParams
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<BasicNameValuePair> getParamsList(RequestParams requestParams)
	{
		try
		{
			// getDeclaredMethod*()获取的是类自身声明的所有方法，包含public、protected和private方法。
			// getMethod*()获取的是类的所有共有方法，这就包括自身的所有public方法，和从基类继承的、从接口实现的所有public方法。
			Method method = requestParams.getClass().getDeclaredMethod("getParamsList");
			method.setAccessible(true);

			return (List<BasicNameValuePair>) method.invoke(requestParams);
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace(); // Can't happen in this version
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace(); // Can't happen in this version
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace(); // Can't happen in this version
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace(); // Can't happen in this version
		}

		return new ArrayList<BasicNameValuePair>();
	}

	/**
	 * 返回一个FormEntity
	 * 
	 * @param requestParams
	 * @return
	 */
	public static HttpEntity createFormEntity(RequestParams requestParams)
	{
		try
		{
			Method method = requestParams.getClass().getDeclaredMethod("createFormEntity");
			method.setAccessible(true);
			return (HttpEntity) method.invoke(requestParams);
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
			Log.e(TAG, "Check RequestParams code");
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
			Log.e(TAG, "Check RequestParams code");
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			Log.e(TAG, "Check RequestParams code");
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
			Log.e(TAG, "Check RequestParams code");
		}

		return null;
	}

}
