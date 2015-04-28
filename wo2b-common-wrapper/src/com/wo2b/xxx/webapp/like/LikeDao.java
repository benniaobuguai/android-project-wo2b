package com.wo2b.xxx.webapp.like;

import java.util.HashMap;
import java.util.Map;

import com.wo2b.sdk.database.RockyDao;
import com.wo2b.xxx.webapp.localcache.LocalDbCacheHelper;

/**
 * 数据访问对象
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class LikeDao extends RockyDao<Like>
{

	/**
	 * 默认构造函数
	 * 
	 * @param dbCacheHelper
	 */
	public LikeDao(LocalDbCacheHelper dbCacheHelper)
	{
		super(dbCacheHelper);
	}

	/**
	 * 是否已经点赞
	 * 
	 * @param pkgname 包名
	 * @param module 模块
	 * @param titleId 标题ID
	 * @param title 标题
	 * @return
	 */
	public boolean isLike(String pkgname, String module, String titleId, String title)
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("pkgname", pkgname);
		map.put("module", module);
		map.put("titleId", titleId);
		map.put("title", title);

		return exists(map);
	}

}
