package com.wo2b.sdk.common.util.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密工具类
 * 
 * @author Rocky
 * 
 */
public class AESUtil
{

	/**
	 * 加密
	 * 
	 * @param text 要加密的字符串
	 * @param iv 初始化向量参数
	 * @param password 密钥
	 * @return
	 */
	public static String AESEncrypt(String text, String iv, String password)
	{
		String result = AESEncrypt(text, iv, password, "AES/CBC/PKCS5Padding");
		return result;
	}

	/**
	 * 加密
	 * 
	 * @param text 要加密的字符串
	 * @param iv 初始化向量参数
	 * @param password 密钥
	 * @return
	 */
	public static String AESEncrypt(String text, String iv, String password, String mode)
	{
		byte[] results = new byte[0];
		try
		{
			Cipher cipher = Cipher.getInstance(mode);

			// setup key
			byte[] keyBytes = new byte[16];
			byte[] b = password.getBytes("UTF-8");
			int len = b.length;
			if (len > keyBytes.length)
				len = keyBytes.length;
			System.arraycopy(b, 0, keyBytes, 0, len);

			SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

			IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));

			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
			results = cipher.doFinal(text.getBytes("UTF-8"));
		}
		catch (Exception e)
		{
			//throw new RuntimeException(e.getMessage(), e);
		}
		
		return Base64.encode(results);
	}

	/**
	 * 解密
	 * 
	 * @param text 要加密的字符串
	 * @param iv 初始化向量参数
	 * @param password 密钥
	 * @return
	 */
	public static String AESDecrypt(String text, String iv, String password)
	{
		try
		{
			String result = AESDecrypt(text, iv, password, "AES/CBC/PKCS5Padding");
			return result;
		}
		catch (Exception e)
		{
			//e.printStackTrace();
		}

		return "";
	}

	/**
	 * 加密
	 * 
	 * @param text 要加密的字符串
	 * @param iv 初始化向量参数
	 * @param password 密钥
	 * @return
	 */
	public static String AESDecrypt(String text, String iv, String password, String mode) throws Exception
	{
		Cipher cipher = Cipher.getInstance(mode);

		byte[] keyBytes = new byte[16];
		byte[] b = password.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length)
			len = keyBytes.length;
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
		IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

		byte[] results = cipher.doFinal(Base64.decode(text));
		return new String(results, "UTF-8");
	}

//	public static void main(String[] args) throws Exception
//	{
//		final String url = "http://img1.bdstatic.com/img/image/904d50735fae6cd7b89e3046ace0d2442a7d9330e16.jpg";
//		String iv = RandomString.getRandomString(16);
//		String test_text = AESEncrypt(url, iv, "*uz^3T_!M6v@");
//		System.out.println(test_text);
//
//		long s = System.currentTimeMillis();
//
//		int length = 10000;
//		for (int i = 0; i < length; i++)
//		{
//			AESDecrypt(test_text, iv, "*uz^3T_!M6v@");
//		}
//
//		long e = System.currentTimeMillis();
//
//		System.out.println("Cost: " + (e - s));
//	}

}
