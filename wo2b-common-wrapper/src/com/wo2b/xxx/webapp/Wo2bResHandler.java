package com.wo2b.xxx.webapp;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.ConnectException;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.common.util.http.TextHttpResponseHandler;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

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
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public abstract class Wo2bResHandler<Result> extends TextHttpResponseHandler
{

	private static final String TAG = "Wo2bResHandler";
	
	/**
	 * Fired when a request returns successfully, override to handle in your own code
	 * 
	 * @param code
	 * @param result
	 */
	public abstract void onSuccess(int code, Result result);

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
			Result result = JSON.parseObject(res.getData(), resultClass);

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

	/**
	 * 访问登录的接口, 发现当前并没有处于登录状态
	 */
	protected void sessionTimeoutOrNoLogin()
	{
		final Context context = RockySdk.getInstance().getContext();
		final User memoryUser = UserManager.getInstance().getMemoryUser();
		if (memoryUser != null)
		{
			// 内存中存在用户信息, 则说明用户之前成功登录过.
			sessionTimeout(context, memoryUser.getUsername(), memoryUser.getPassword());
		}
		else
		{
			noLogin(context);
		}
	}
	
	/**
	 * 会话超时
	 */
	protected void sessionTimeout(final Context context, final String username, final String password)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				Looper.prepare();
				Toast.makeText(context, "会话丢失, 正在为您重新登录.", Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}).start();

		// 在内存中找到用户信息, 后台自动为其登录
		UserManager.getInstance().login(username, password, new Wo2bResHandler<User>()
		{

			@Override
			public void onSuccess(int code, User result)
			{
				Log.D(TAG, "Session timeout, login success when catch SdkNoLoginException.");
				Toast.makeText(context, "登录成功", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				Log.D(TAG, "Session timeout, login onFailure when catch SdkNoLoginException.");
				Toast.makeText(context, "登录失败", Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * 没有登录, 直接导航至
	 * 
	 * @param context
	 */
	protected void noLogin(final Context context)
	{
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				Looper.prepare();
				Toast.makeText(context, "您还没有登录, 请先登录.", Toast.LENGTH_LONG).show();
				Looper.loop();

				Intent intent = new Intent();
				intent.setAction("com.wo2b.user.LOGIN");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addCategory("android.intent.category.DEFAULT");
				context.startActivity(intent);
			}
		}).start();
	}

	@Override
	public void onFailure(int statusCode, Header[] headers, byte[] responseBytes, Throwable throwable)
	{
		if (statusCode >= 400 && statusCode < 500)
		{
			onFailure(statusCode, "[" + statusCode + ", 请求错误]", throwable);

			return;
		}
		else if (statusCode >= 500 && statusCode < 600)
		{
			onFailure(statusCode, "[" + statusCode + ", 服务器错误]", throwable);

			return;
		}

		if (throwable == null)
		{
			String responseString = getResponseString(responseBytes, getCharset());
			onFailure(statusCode, responseString, throwable);
		}
		else
		{
			if (throwable instanceof ConnectTimeoutException)
			{
				onFailure(statusCode, "连接超时, 请检查你的网络", throwable);
			}
			else if (throwable instanceof ConnectException)
			{
				onFailure(statusCode, "网络出错, 请检查你的网络", throwable);
			}
			else if (throwable instanceof IOException)
			{
				if (throwable.getMessage() != null && throwable.getMessage().indexOf("UnknownHostException") == 0)
				{
					onFailure(statusCode, "网络异常", throwable);
				}
			}
			else
			{
				// onFailure(statusCode, "未知异常, 请及时反馈给开发者", throwable);
				onFailure(statusCode, "未知异常", throwable);
			}
		}
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

}
