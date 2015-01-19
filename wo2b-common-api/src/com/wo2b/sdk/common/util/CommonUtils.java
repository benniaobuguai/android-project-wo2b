package com.wo2b.sdk.common.util;

import android.net.Uri;

/**
 * 通用工具库
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-13
 */
public class CommonUtils
{

	/**
	 * 是否来源于www.wo2b.com的主机, 因文件放在七牛云服务器上, 域名不能保证为wo2b.com
	 * 
	 * @param url 网址
	 * @return
	 */
	public static boolean isWo2bHost(String url)
	{
		String host = getHost(url);
		if (host != null && host.indexOf("wo2b.com") != -1)
		// if (host != null && host.indexOf("wo2b") != -1)
		{
			return true;
		}

		return false;
	}

	/**
	 * 返回主机地址
	 * 
	 * @param url 网址
	 * @return
	 */
	public static String getHost(String url)
	{
		if (url == null || url.length() == 0)
		{
			return "<Unknown>";
		}

		return Uri.parse(url).getHost();
	}

}
