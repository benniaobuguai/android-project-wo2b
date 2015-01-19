package com.wo2b.sdk.core.exception;

/**
 * 权限异常类
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class SdkAuthorizedException extends RuntimeException
{

	private static final long serialVersionUID = -8009209578172932607L;

	public SdkAuthorizedException()
	{
		super();
	}

	public SdkAuthorizedException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public SdkAuthorizedException(String detailMessage)
	{
		super(detailMessage);
	}

	public SdkAuthorizedException(Throwable throwable)
	{
		super(throwable);
	}

}
