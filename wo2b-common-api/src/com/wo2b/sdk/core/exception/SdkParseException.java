package com.wo2b.sdk.core.exception;

/**
 * 解析异常
 * 
 * @author Rocky
 * 
 */
public class SdkParseException extends Exception
{

	private static final long serialVersionUID = 1L;

	public SdkParseException()
	{
		super();
	}

	public SdkParseException(String detailMessage, Throwable throwable)
	{
		super(detailMessage, throwable);
	}

	public SdkParseException(String detailMessage)
	{
		super(detailMessage);
	}

	public SdkParseException(Throwable throwable)
	{
		super(throwable);
	}

}
