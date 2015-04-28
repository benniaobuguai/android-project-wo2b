package com.wo2b.sdk.core.exception;

import java.net.SocketTimeoutException;

/**
 * 连接超时
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class SdkSocketTimeoutException extends SocketTimeoutException
{

	private static final long serialVersionUID = 1692935934995221036L;

	public SdkSocketTimeoutException()
	{
		super();
	}

	public SdkSocketTimeoutException(String detailMessage)
	{
		super(detailMessage);
	}

}
