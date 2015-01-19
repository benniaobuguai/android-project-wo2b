package com.wo2b.xxx.webapp.openapi;

/**
 * 服务器地址
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class OpenApiUrl
{

	/**
	 * 开发模式标志, 正式发布时, 请修改为false.
	 */
	public static final boolean DEBUG = false;

	/**
	 * 本地开发环境, 不建议直接访问, 请使用{@link OpenApiUrl#getBaseUrl()} .
	 */
	@Deprecated
	public static final String BASE_URL_LOCAL = "http://192.168.1.100:8080/wo2b-main";
	// private static final String BASE_URL_LOCAL = "http://192.168.1.100";

	/**
	 * 京东云服务器, 不建议直接访问, 请使用{@link OpenApiUrl#getBaseUrl()} .
	 */
	// private static final String BASE_URL_JD = "http://wo2b01.jd-app.com";

	/**
	 * 阿里云服务器, 不建议直接访问, 请使用{@link OpenApiUrl#getBaseUrl()} .
	 */
	@Deprecated
	public static final String BASE_URL_ALIYUN = "http://www.wo2b.com/wo2b-main";

	/**
	 * 官网网站, 不建议直接访问, 请使用{@link OpenApiUrl#getIndexUrl()} .
	 */
	@Deprecated
	public static final String WO2B_INDEX_LOCAL = "http://192.168.1.100:8080/wo2b-main";

	/**
	 * 官网网站, 不建议直接访问, 请使用{@link OpenApiUrl#getIndexUrl()} .
	 */
	@Deprecated
	public static final String WO2B_INDEX_ALIYUN = "http://www.wo2b.com";

	/**
	 * 返回服务器的地址
	 * 
	 * @return
	 */
	public static final String getBaseUrl()
	{
		if (DEBUG)
		{
			return BASE_URL_LOCAL;
		}
		else
		{
			return BASE_URL_ALIYUN;
		}
	}

	/**
	 * 返回官网主页地址
	 * 
	 * @return
	 */
	public static final String getIndexUrl()
	{
		if (DEBUG)
		{
			return WO2B_INDEX_LOCAL;
		}
		else
		{
			return WO2B_INDEX_ALIYUN;
		}
	}

}
