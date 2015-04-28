package com.wo2b.tu123.business.image;

import java.sql.SQLException;
import java.util.List;

import com.wo2b.wrapper.component.security.SecurityTu123;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.wo2b.tu123.business.base.BaseBiz;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.model.image.PhotoInfo;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public class PhotoBiz extends BaseBiz<PhotoInfo>
{
	
	private RuntimeExceptionDao<PhotoInfo, Long> mPhotoDao;
	
	public PhotoBiz(DatabaseHelper helper)
	{
		super(helper);
		mPhotoDao = helper.getPhotoDao();
	}

	@Override
	public int create(PhotoInfo model)
	{
		return mPhotoDao.create(model);
	}

	@Override
	public int update(PhotoInfo model)
	{
		return mPhotoDao.update(model);
	}
	
	@Override
	public int delete(PhotoInfo model)
	{
		return mPhotoDao.delete(model);
	}
	
	/**
	 * 
	 * @param albumid
	 * @return
	 */
	public int deleteByAlbumid(String albumid)
	{
		try
		{
			DeleteBuilder<PhotoInfo, Long> deleteBuilder = mPhotoDao.deleteBuilder();
			deleteBuilder.where().eq("albumid", albumid);
			return deleteBuilder.delete();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return super.deleteAll();
	}
	
	public boolean encrypt_mode = true;
	
	public List<PhotoInfo> queryByAlbumid(String albumid)
	{
		List<PhotoInfo> photoInfos = null;
		try
		{
			QueryBuilder<PhotoInfo, Long> queryBuilder = mPhotoDao.queryBuilder();
			photoInfos = queryBuilder.where().eq("albumid", albumid).query();

			if (encrypt_mode)
			{
				int size = photoInfos.size();
				for (int i = 0; i < size; i++)
				{
					photoInfos.get(i).setLargeUrl(SecurityTu123.decodeImageUrl(photoInfos.get(i).getLargeUrl()));
				}
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return photoInfos;
	}

	@Override
	public List<PhotoInfo> queryForAll()
	{
		return mPhotoDao.queryForAll();
	}
	
	@Override
	public List<PhotoInfo> queryForEq(String arg0, Object arg1)
	{
		return super.queryForEq(arg0, arg1);
	}
	
}
