package com.wo2b.tu123.business.image;

import java.sql.SQLException;
import java.util.List;

import com.wo2b.wrapper.component.security.SecurityTu123;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.wo2b.tu123.business.base.BaseBiz;
import com.wo2b.tu123.business.base.UserDatabaseHelper;
import com.wo2b.tu123.model.image.MyFavorites;

/**
 * 
 * @author Rocky
 * 
 */
public class MyFavoritesBiz extends BaseBiz<MyFavorites>
{
	
	private RuntimeExceptionDao<MyFavorites, Long> mMyFavoritesDao;
	
	public MyFavoritesBiz(UserDatabaseHelper helper)
	{
		super(helper);
		mMyFavoritesDao = helper.getMyFavoritesDao();
	}
	
	/**
	 * 是否存在
	 * 
	 * @param largeUrl
	 * @return
	 */
	public boolean exists(String largeUrl)
	{
		try
		{
			QueryBuilder<MyFavorites, Long> queryBuilder = mMyFavoritesDao.queryBuilder();
			// queryBuilder.where().eq("largeUrl", largeUrl);
			// 加密判断是否存在
			queryBuilder.where().eq("largeUrl", SecurityTu123.encodeImageUrl(largeUrl));

			return queryBuilder.countOf() > 0 ? true : false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public int create(MyFavorites model)
	{
		// 加密保存
		model.setLargeUrl(SecurityTu123.encodeImageUrl(model.getLargeUrl()));
		return mMyFavoritesDao.create(model);
	}
	
	@Override
	public int delete(MyFavorites model)
	{
		return mMyFavoritesDao.delete(model);
	}
	
	@Override
	public int deleteAll()
	{
		try
		{
			DeleteBuilder<MyFavorites, Long> deleteBuilder = mMyFavoritesDao.deleteBuilder();
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
	public List<MyFavorites> queryForAll()
	{
		List<MyFavorites> photoInfos = mMyFavoritesDao.queryForAll();

		if (photoInfos != null && !photoInfos.isEmpty())
		{
			int size = photoInfos.size();
			for (int i = 0; i < size; i++)
			{
				photoInfos.get(i).setLargeUrl(SecurityTu123.decodeImageUrl(photoInfos.get(i).getLargeUrl()));
			}
		}

		return photoInfos;
	}
	
	@Override
	public List<MyFavorites> queryForEq(String arg0, Object arg1)
	{
		return super.queryForEq(arg0, arg1);
	}
	
}
