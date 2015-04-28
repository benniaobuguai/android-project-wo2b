package com.wo2b.tu123.business.image;

import java.sql.SQLException;
import java.util.List;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.wrapper.component.security.SecurityTu123;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.wo2b.tu123.business.base.BaseBiz;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.model.image.AlbumInfo;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class AlbumBiz extends BaseBiz<AlbumInfo>
{
	
	private RuntimeExceptionDao<AlbumInfo, Long> mAlbumDao;
	
	/**
	 * 加密模式
	 */
	public boolean encrypt_mode = true;
	
	public AlbumBiz(DatabaseHelper helper)
	{
		super(helper);
		mAlbumDao = helper.getAlbumDao();
	}
	
	@Override
	public int create(AlbumInfo model)
	{
		return mAlbumDao.create(model);
	}
	
	@Override
	public int update(AlbumInfo model)
	{
		return mAlbumDao.update(model);
	}

	@Override
	public int delete(AlbumInfo model)
	{
		return mAlbumDao.delete(model);
	}
	
	@Override
	public int deleteAll()
	{
		try
		{
			DeleteBuilder<AlbumInfo, Long> deleteBuilder = mAlbumDao.deleteBuilder();
			deleteBuilder.where().isNotNull("albumid");
			return deleteBuilder.delete();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return super.deleteAll();
	}
	
	@Override
	public List<AlbumInfo> queryForAll()
	{
		return mAlbumDao.queryForAll();
	}
	
//	/**
//	 * 根据模块返回搜索结果
//	 * 
//	 * @param module
//	 * @return
//	 */
//	public List<AlbumInfo> queryByModule(Module module)
//	{
//		List<AlbumInfo> albumInfoList = null;
//		try
//		{
//			QueryBuilder<AlbumInfo, String> queryBuilder = mAlbumDao.queryBuilder();
//			albumInfoList = queryBuilder.where().eq("module", module.value).query();
//		}
//		catch (SQLException e)
//		{
//			e.printStackTrace();
//		}
//		
//		return albumInfoList;
//	}
	
	/**
	 * 根据模块返回搜索结果
	 * 
	 * @param module
	 * @return
	 */
	public List<AlbumInfo> queryByModule(Module module)
	{
		List<AlbumInfo> albumInfoList = null;
		try
		{
			QueryBuilder<AlbumInfo, Long> queryBuilder = mAlbumDao.queryBuilder();
			queryBuilder.orderBy("orderby", true);
			albumInfoList = queryBuilder.where().eq("module", module.value).query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if (encrypt_mode)
		{
			if (albumInfoList == null || albumInfoList.isEmpty())
			{
				return null;
			}

			int size = albumInfoList.size();
			for (int i = 0; i < size; i++)
			{
				albumInfoList.get(i).setCoverurl(SecurityTu123.decodeImageUrl(albumInfoList.get(i).getCoverurl()));
			}
		}
		
		return albumInfoList;
	}
	
	/**
	 * 根据模块返回搜索结果, 具有分组性质.
	 * 
	 * @param module
	 * @return
	 */
	public List<AlbumInfo> queryAllCategory(Module module)
	{
		List<AlbumInfo> albumInfoList = null;
		try
		{
			QueryBuilder<AlbumInfo, Long> queryBuilder = mAlbumDao.queryBuilder();
			queryBuilder.groupBy("category");
			queryBuilder.orderBy("orderby", true);
			albumInfoList = queryBuilder.where().eq("module", module.value).query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if (encrypt_mode)
		{
			if (albumInfoList == null || albumInfoList.isEmpty())
			{
				return null;
			}

			int size = albumInfoList.size();
			for (int i = 0; i < size; i++)
			{
				albumInfoList.get(i).setCoverurl(SecurityTu123.decodeImageUrl(albumInfoList.get(i).getCoverurl()));
			}
		}
		
		return albumInfoList;
	}
	
	
	/**
	 * 根据模块返回搜索结果
	 * 
	 * @param module
	 * @return
	 */
	public List<AlbumInfo> queryByCategory(String category)
	{
		List<AlbumInfo> albumInfoList = null;
		try
		{
			QueryBuilder<AlbumInfo, Long> queryBuilder = mAlbumDao.queryBuilder();
			queryBuilder.orderBy("orderby", true);
			albumInfoList = queryBuilder.where().eq("category", category).query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		if (encrypt_mode)
		{
			if (albumInfoList == null || albumInfoList.isEmpty())
			{
				return null;
			}

			int size = albumInfoList.size();
			for (int i = 0; i < size; i++)
			{
				albumInfoList.get(i).setCoverurl(SecurityTu123.decodeImageUrl(albumInfoList.get(i).getCoverurl()));
			}
		}
		
		return albumInfoList;
	}
	
	/**
	 * 根据相册名称去查询
	 * 
	 * @param name
	 * @return
	 */
	public List<AlbumInfo> queryByAlbumName(String name)
	{
		List<AlbumInfo> albumInfoList = null;
		try
		{
			QueryBuilder<AlbumInfo, Long> queryBuilder = mAlbumDao.queryBuilder();
			queryBuilder.orderBy("name", true);
			albumInfoList = queryBuilder.where().like("name", "%" + name + "%").query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		if (encrypt_mode)
		{
			if (albumInfoList == null || albumInfoList.isEmpty())
			{
				return null;
			}

			int size = albumInfoList.size();
			for (int i = 0; i < size; i++)
			{
				albumInfoList.get(i).setCoverurl(SecurityTu123.decodeImageUrl(albumInfoList.get(i).getCoverurl()));
			}
		}
		
		return albumInfoList;
	}
	
	
	
	@Override
	public List<AlbumInfo> queryForEq(String arg0, Object arg1)
	{
		return super.queryForEq(arg0, arg1);
	}
	
}
