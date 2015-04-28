package com.wo2b.sdk.common.util.io;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-9
 */
public class FileNameGenerator
{

	private static final String HASH_ALGORITHM = "MD5";

	private static final int RADIX = 10 + 26; // 10 digits + 26 letters

	/**
	 * 根据网络地址生成文件名, 因为有很多网络地址的名称比较怪异. 建议在设计过程中, 接口中考虑文件名. 此方法只是粗暴的解决方法.
	 * FIXME: 可优化
	 * 
	 * @param url
	 * @return
	 */
	public static String generateNameByNetUrl(String url)
	{
		byte[] md5 = getMD5(url.getBytes());
		BigInteger bi = new BigInteger(md5).abs();

		return bi.toString(RADIX);
	}

	/**
	 * 
	 * @param data
	 * @return
	 */
	private static byte[] getMD5(byte[] data)
	{
		byte[] hash = null;
		try
		{
			MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
			digest.update(data);
			hash = digest.digest();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}

		return hash;
	}

}
