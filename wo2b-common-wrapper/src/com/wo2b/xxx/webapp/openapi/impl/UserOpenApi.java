package com.wo2b.xxx.webapp.openapi.impl;

import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 用户开放接口
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class UserOpenApi extends OpenApi
{

	/**
	 * 注册
	 * 
	 * @param username
	 * @param pwd1
	 * @param pwd2
	 * @param email
	 * @param mac
	 * @param deviceid
	 * @param wo2bResHandler
	 */
	public void register(String username, String pwd1, String pwd2, String email, String mac, String deviceid,
			Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", pwd1);
		params.put("password2", pwd2);
		params.put("email", email);
		params.put("mac", mac);
		params.put("deviceId", deviceid);

		post("/user/User_register", params, wo2bResHandler);
	}

	/**
	 * 登录
	 * 
	 * @param pkgname
	 * @param username
	 * @param password
	 * @param wo2bResHandler
	 */
	public void login(String username, String password, Wo2bResHandler<User> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);

		post("/user/User_login", params, wo2bResHandler);
	}

	/**
	 * 登出
	 * 
	 * @param pkgname
	 * @param username
	 * @param password
	 * @param wo2bResHandler
	 */
	public void logout(Wo2bResHandler<Void> wo2bResHandler)
	{
		post("/user/User_logout", wo2bResHandler);
	}

	/**
	 * 重置密码
	 * 
	 * @param username
	 * @param password
	 * @param mac
	 * @param deviceId
	 * @param wo2bResHandler
	 */
	public void resetPwd(String username, String password, String mac, String deviceId,
			Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		params.put("mac", mac);
		params.put("deviceId", deviceId);

		post("/user/User_resetPwd", params, wo2bResHandler);
	}

	/**
	 * 通过邮件找回密码
	 * 
	 * @param email 注册时用的邮箱地址
	 */
	public void sendResetPwdEmail(String email, Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("email", email);

		post("/Email_sendResetPwdEmail", params, wo2bResHandler);
	}

}
