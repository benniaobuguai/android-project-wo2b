package com.wo2b.sdk.assistant.upgrade;

import com.wo2b.sdk.core.exception.SdkParseException;

/**
 * 默认的版本信息解析器
 * 
 * @author Rocky
 * 
 */
public class VersionInfoDefaultParser extends VersionInfoParser
{
	
	@Override
	protected VersionInfo parseVersionInfoSelf(String versionString) throws SdkParseException
	{
		return null;
	}
	
	@Override
	protected boolean isParseSelf()
	{
		// 使用默认的解析
		return false;
	}
	
}
