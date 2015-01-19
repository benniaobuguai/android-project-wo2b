package com.wo2b.sdk.common.util.encrypt;

import java.util.Random;

/**
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class RandomString
{

	public static String getRandomString(int length)
	{
		// length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++)
		{
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static void main(String[] args)
	{

		System.out.println(RandomString.getRandomString(16));
	}
}
