package com.wo2b.sdk.common.util.http.extra;

import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

/**
 * HTTP请求的结果
 * 
 * @author 笨鸟不乖
 * 
 */
public class SyncHttpResponse
{
	
	private int statusCode;
	
	private Header[] headers;
	
	private String content;
	
	public int getStatusCode()
	{
		return statusCode;
	}
	
	public void setStatusCode(int statusCode)
	{
		this.statusCode = statusCode;
	}
	
	public Header[] getHeaders()
	{
		return headers;
	}
	
	public void setHeaders(Header[] headers)
	{
		this.headers = headers;
	}
	
	public String getContent()
	{
		return content;
	}
	
	public void setContent(String content)
	{
		this.content = content;
	}
	
	// ----------------- 扩展 -----------------
	/**
	 * 返回指定name的Header对象.
	 * 
	 * @param name
	 * @return
	 */
	public Header getHeader(String name)
	{
		int length = this.headers.length;
		for (int i = 0; i < length; i++)
		{
			if (headers[i] != null && headers[i].getName().equalsIgnoreCase(name))
			{
				return headers[i];
			}
		}
		
		return null;
	}
	
	public boolean isOK()
	{
		if (getStatusCode() == HttpStatus.SC_OK)
		{
			return true;
		}
		
		return false;
	}

	@Override
	public String toString()
	{
		return "HttpRequestResult [statusCode=" + statusCode + ", headers=" + Arrays.toString(headers) + ", content="
				+ content + "]";
	}
	
}
