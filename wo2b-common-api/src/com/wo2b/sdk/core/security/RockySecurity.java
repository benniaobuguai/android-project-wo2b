package com.wo2b.sdk.core.security;

import com.wo2b.sdk.common.util.encrypt.AESUtil;

/**
 * 数据安全, 考虑使用JNI进行调用.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public final class RockySecurity
{

	// ---------------------------------------------------------------------------------------
	// ----------------------------- 用户模块
	/**
	 * 用户加密基本数据
	 */
	private static final String USER_IV = "xMIow4061pcZhMqf";
	private static final String USER_PWD = "_P$#&668%u#";

	/**
	 * 加密用户基本信息
	 * 
	 * @return
	 */
	public static final String encode_user_info(String string)
	{
		return AESUtil.AESEncrypt(string, USER_IV, USER_PWD);
	}

	/**
	 * 解密用户基本信息
	 * 
	 * @return
	 */
	public static final String decode_user_info(String string)
	{
		return AESUtil.AESDecrypt(string, USER_IV, USER_PWD);
	}

	/**
	 * 加密用户金币信息
	 * 
	 * @return
	 */
	public static final String encode_user_gold(String string)
	{
		return AESUtil.AESEncrypt(string, USER_IV, USER_PWD);
	}

	/**
	 * 解密用户金币信息
	 * 
	 * @return
	 */
	public static final String decode_user_gold(String string)
	{
		return AESUtil.AESDecrypt(string, USER_IV, USER_PWD);
	}
	// #######################################################################################

}
