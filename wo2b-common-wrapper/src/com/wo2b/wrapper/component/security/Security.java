package com.wo2b.wrapper.component.security;

import com.wo2b.sdk.common.util.encrypt.AESUtil;

public class Security
{

	/**
	 * 加密
	 * 
	 * @param text 要加密的字符串
	 * @param iv 初始化向量参数
	 * @param password 密钥
	 * @return
	 */
	public static String encrypt(String text, String iv, String password)
	{
		return AESUtil.AESEncrypt(text, iv, password);
	}

	/**
	 * 解密
	 * 
	 * @param text 要加密的字符串
	 * @param iv 初始化向量参数
	 * @param password 密钥
	 * @return
	 */
	public static String decrypt(String text, String iv, String password)
	{
		return AESUtil.AESDecrypt(text, iv, password);
	}

	/**
	 * 文本解密
	 * 
	 * @param url
	 * @return
	 */
	public static native String encodeText(String text);

	/**
	 * 文本解密
	 * 
	 * @param url
	 * @return
	 */
	public static native String decodeText(String text);

	/**
	 * 初始化
	 * 
	 * @return
	 */
	public static native String nativeInit();

	static
	{
		System.loadLibrary("wo2b_sdk");
	}

}
