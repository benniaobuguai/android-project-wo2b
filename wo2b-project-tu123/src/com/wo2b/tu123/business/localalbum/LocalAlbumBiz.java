package com.wo2b.tu123.business.localalbum;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.wo2b.tu123.business.base.BaseBiz;
import com.wo2b.tu123.business.base.UserDatabaseHelper;
import com.wo2b.tu123.model.localalbum.FocusItemInfo;

/**
 * Local album biz.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class LocalAlbumBiz extends BaseBiz<FocusItemInfo>
{
	
	private RuntimeExceptionDao<FocusItemInfo, Long> mFocusItemInfoDao;
	
	public LocalAlbumBiz(UserDatabaseHelper helper)
	{
		super(helper);
		mFocusItemInfoDao = helper.getFocusItemInfoDao();
	}
	
	@Override
	public int create(FocusItemInfo model)
	{
		return mFocusItemInfoDao.create(model);
	}
	
	@Override
	public int delete(FocusItemInfo model)
	{
		return mFocusItemInfoDao.delete(model);
	}
	
	@Override
	public int deleteAll()
	{
		try
		{
			DeleteBuilder<FocusItemInfo, Long> deleteBuilder = mFocusItemInfoDao.deleteBuilder();
			deleteBuilder.where().isNotNull("id");
			return deleteBuilder.delete();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return super.deleteAll();
	}
	
	@Override
	public List<FocusItemInfo> queryForAll()
	{
		return mFocusItemInfoDao.queryForAll();
	}
	
	/**
	 * 根据模块返回搜索结果
	 * 
	 * @param module
	 * @return
	 */
	public List<FocusItemInfo> queryByModule(String module)
	{
		List<FocusItemInfo> koItemInfoList = null;
		try
		{
			QueryBuilder<FocusItemInfo, Long> queryBuilder = mFocusItemInfoDao.queryBuilder();
			koItemInfoList = queryBuilder.where().eq("module", module).query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return koItemInfoList;
	}
	
	/**
	 * 返回完整Focus List
	 * 
	 * @param module
	 * @return
	 */
	public List<FocusItemInfo> getFocusList()
	{
		List<FocusItemInfo> koItemInfoList = null;
		try
		{
			QueryBuilder<FocusItemInfo, Long> queryBuilder = mFocusItemInfoDao.queryBuilder();
			// queryBuilder.groupBy("category");
			queryBuilder.orderBy("order_by", true);
			koItemInfoList = queryBuilder.query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return koItemInfoList;
	}
	
	/**
	 * 返回格式为 " "aaa", "bbb" "
	 * 
	 * @return
	 */
	public String getFocusListString()
	{
		List<FocusItemInfo> koItemList = getFocusList();
		
		if (koItemList == null || koItemList.isEmpty())
		{
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		
		FocusItemInfo koItem = null;
		int count = koItemList.size();
		for (int i = 0; i < count; i++)
		{
			koItem = koItemList.get(i);
			sb.append("\"");
			
			sb.append(koItem.getBucket_display_name());
			
		}
		
		return null;
	}
	
}
