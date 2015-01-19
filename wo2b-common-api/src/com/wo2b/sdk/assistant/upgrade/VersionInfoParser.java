package com.wo2b.sdk.assistant.upgrade;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.core.exception.SdkParseException;

/**
 * 版本解析器
 * 
 * @author Rocky
 * 
 */
public abstract class VersionInfoParser
{
	
	private final String TAG = "VersionInfoParser";
	
	/**
	 * 解析服务器的版本信息. 如果你自定义服务器版本信息结构, 需要进行重写此类, 同时重写
	 * {@link VersionInfoParser#parseVersionInfoSelf(String)} 和
	 * {@link VersionInfoParser#parseVersionInfo(String)} 这两个方法. 此方法不能被重写.
	 * 
	 * @param versionString
	 * @return
	 * @throws SdkParseException
	 */
	public final VersionInfo parseVersionInfo(String versionString) throws SdkParseException
	{
		if (!isParseSelf())
		{
			return parseVersionXml(versionString);
		}
		
		return parseVersionInfoSelf(versionString);
	}
	
	/**
	 * 解析自己定义的版本信息结构.
	 * 
	 * @param versionString
	 * @return
	 * @throws SdkParseException
	 */
	protected abstract VersionInfo parseVersionInfoSelf(String versionString) throws SdkParseException;
	
	/**
	 * 是否自行处理解析请求.
	 * 
	 * @return
	 */
	protected abstract boolean isParseSelf();
	
	/**
	 * 提供模板的解析方式, 指定服务器的版本信息结构体. 模板如下:</br>
	 * <hr>
	 * 
	 * <code>
	 * <?xml version="1.0" encoding="utf-8"?>
	 * <version>
	 * 		<application>kkdt.apk</application>
	 * 		<versionCode>10</versionCode>
	 * 		<versionName>2.0.1</versionName>
	 * 		<url>https://raw.github.com/rocky/AppUpdate/master/AppUpdate/apk/benbird.music.apk</url>
	 * </version>
	 * </code>
	 * <hr>
	 * 
	 * @param versionXml
	 * @return
	 * @throws SdkParseException
	 */
	private VersionInfo parseVersionXml(String versionXml) throws SdkParseException
	{
		VersionInfo info = new VersionInfo();
		ByteArrayInputStream bais = new ByteArrayInputStream(versionXml.getBytes());
		XmlPullParser parser = Xml.newPullParser();
		
		try
		{
			parser.setInput(bais, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType)
				{
					// At the beginning of document and nothing read yet, do something you want.
					case XmlPullParser.START_DOCUMENT:
					{
						break;
					}
					case XmlPullParser.START_TAG:
					{
						String name = parser.getName();
						
						if ("version".equals(name))
						{
							Log.D(TAG, "start tag.");
						}
						else if ("application".equals(name))
						{
							info.setAppName(parser.nextText());
						}
						else if ("versionCode".equals(name))
						{
							info.setVersionCode(Integer.valueOf(parser.nextText()));
						}
						else if ("versionName".equals(name))
						{
							info.setVersionName(parser.nextText());
						}
						else if ("url".equals(name))
						{
							info.setUpdateUrl(parser.nextText());
						}
						break;
					}
					case XmlPullParser.END_TAG:
					{
						String name = parser.getName();
						if ("version".equals(name))
						{
							Log.D(TAG, "end tag.");
							return info;
						}
						break;
					}
				}
				
				eventType = parser.next();
			}
			
		}
		catch (XmlPullParserException e)
		{
			throw new SdkParseException("Parse version info failed./n" + e.toString());
		}
		catch (IOException e)
		{
			throw new SdkParseException("Parse version info failed./n" + e.toString());
		}
		finally
		{
			if (bais != null)
			{
				try
				{
					bais.close();
				}
				catch (IOException e)
				{
					throw new SdkParseException("Parse version info failed./n" + e.toString());
				}
			}
		}
		
		return null;
	}
	
}
