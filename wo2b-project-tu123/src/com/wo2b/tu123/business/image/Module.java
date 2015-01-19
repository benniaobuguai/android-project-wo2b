package com.wo2b.tu123.business.image;

/**
 * 图片模块
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public enum Module
{
	
	A("A"), // 图界精品
	H("H"), // 爱图说
	L("L"); // 本地图片
	
	public String value;
	
	private Module(String module)
	{
		this.value = module;
	}
	
}
