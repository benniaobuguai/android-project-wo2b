package com.wo2b.sdk.myapp;

import java.util.HashMap;

/**
 * 应用程序的相关信息, 暂时主要是用于判断用户的来源
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 *        FIXME: 需要优化
 */
@Deprecated
public class MyAppCenter
{

	// ----------------------------------------------------------------------------
	// -------------------- 应用程序名称
	// public static final String WO2B_UNKNOWN = "<Unknown>";

	public static HashMap<String, String> APP_LIST = new HashMap<String, String>();

	static
	{
		APP_LIST.put("com.wo2b.tu123", "图界传说");
		APP_LIST.put("com.wo2b.war3", "魔兽你好");
		APP_LIST.put("com.wo2b.game.x2048", "2048百变版");
		APP_LIST.put("com.wo2b.tuku", "极简图库");
	}

	/**
	 * 通过包名返回应用名称
	 * 
	 * @param pkgname
	 * @return
	 */
	public static final String getAppNameByPkgname(String pkgname)
	{
		if (APP_LIST.containsKey(pkgname))
		{
			return APP_LIST.get(pkgname);
		}
		else
		{
			return "<Unknown>";
		}
	}

}
