package com.wo2b.sdk.database;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;


/**
 * 基础的逻辑层
 * 
 * @author Rocky
 * 
 * @param <Model>
 */
public abstract class RockyDao<Model>
{
	
	private static final String TAG = "Rocky.Dao";

	private OrmLiteSqliteOpenHelper mSqliteOpenHelper;

	private RuntimeExceptionDao<Model, ?> mRuntimeDao;

	/**
	 * 构造函数
	 * 
	 * @param helper
	 */
	@SuppressWarnings("unchecked")
	public RockyDao(OrmLiteSqliteOpenHelper sqliteOpenHelper)
	{
		// this.mOpenHelper = SdkDbHelper.getDatabaseHelper(context);

		this.mSqliteOpenHelper = sqliteOpenHelper;
		Class<Model> modelClass = (Class<Model>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		mRuntimeDao = mSqliteOpenHelper.getRuntimeExceptionDao(modelClass);
	}
	
	/**
	 * 返回指定的Dao对象
	 * 
	 * @return
	 */
	public RuntimeExceptionDao<Model, ?> getDao()
	{
		return this.mRuntimeDao;
	}
	
	/**
	 * 判断是否存在
	 * 
	 * @return
	 */
	public boolean exists(Map<String, Object> map)
	{
		if (map == null || map.isEmpty())
		{
			throw new NullPointerException("Map can not be null or empty!!!");
		}

		try
		{
			QueryBuilder<Model, ?> queryBuilder = mRuntimeDao.queryBuilder();
			queryBuilder.setCountOf(true);
			Where<Model, ?> where = null;

			Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
			Entry<String, Object> entry = null;
			boolean isFirst = true;
			while (iterator.hasNext())
			{
				entry = iterator.next();

				if (isFirst)
				{
					where = queryBuilder.where().eq(entry.getKey(), entry.getValue());
					isFirst = false;
				}
				else
				{
					where.and().eq(entry.getKey(), entry.getValue());
				}

				// 此法仅能保证一个
				// queryBuilder.where().eq(entry.getKey(), entry.getValue());
			}

			PreparedQuery<Model> preparedQuery = queryBuilder.prepare();
			long count = mRuntimeDao.countOf(preparedQuery);

			return count > 0 ? true : false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * 添加成功返回记录条数,失败返回-1
	 * 
	 * @param model
	 * @return
	 */
	public int create(Model model)
	{
		return mRuntimeDao.create(model);
	}
	
	/**
	 * 更新成功返回记录条数,失败返回-1
	 * 
	 * @param model
	 * @return
	 */
	public int update(Model model)
	{
		return mRuntimeDao.update(model);
	}
	
	/**
	 * 删除成功返回记录条数,失败返回-1
	 * 
	 * @param model
	 * @return
	 */
	public int delete(Model model)
	{
		return mRuntimeDao.delete(model);
	}

	/**
	 * 删除所有数据
	 * 
	 * @return
	 */
	public int deleteAll()
	{
		DeleteBuilder<Model, ?> deleteBuilder = mRuntimeDao.deleteBuilder();
		try
		{
			return deleteBuilder.delete();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * 返回所有数据
	 * 
	 * @return
	 */
	public List<Model> queryForAll()
	{
		return mRuntimeDao.queryForAll();
	}

	/**
	 * 查询返回实体列表数据
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public List<Model> queryForEq(String field, Object value)
	{
		return mRuntimeDao.queryForEq(field, value);
	}
	
	/**
	 * 根据Map对应的key(列名)和value(列属性值), 返回列表数据
	 * 
	 * @param map
	 * @return
	 */
	public List<Model> queryForEq(Map<String, Object> map)
	{
		if (map == null || map.isEmpty())
		{
			return null;
		}

		QueryBuilder<Model, ?> queryBuilder = mRuntimeDao.queryBuilder();
		Where<Model, ?> where = null;

		Iterator<Entry<String, Object>> iterator = map.entrySet().iterator();
		Entry<String, Object> entry = null;
		boolean isFirst = true;

		try
		{
			while (iterator.hasNext())
			{
				entry = iterator.next();

				if (isFirst)
				{
					where = queryBuilder.where().eq(entry.getKey(), entry.getValue());
					isFirst = false;
				}
				else
				{
					where.and().eq(entry.getKey(), entry.getValue());
				}
			}

			return queryBuilder.query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 通过id获取实体对象实例
	 * 
	 * @param id
	 * @return
	 */
	public Model getModel(Object id)
	{
		try
		{
			List<Model> list = mRuntimeDao.queryForEq("id", id);
			if (isEmpty(list))
			{
				return null;
			}

			return list.get(0);
		}
		catch (Exception e)
		{
			Log.e(TAG, "Check if there is [id] field of current model.");
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 返回记录总数
	 * 
	 * @return
	 */
	public long getCount()
	{
		QueryBuilder<Model, ?> queryBuilder = mRuntimeDao.queryBuilder();

		try
		{
			return queryBuilder.countOf();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return 0;
	}

	/**
	 * 返回OrmLiteSqliteOpenHelper对象
	 * 
	 * @return
	 */
	public OrmLiteSqliteOpenHelper getSqliteOpenHelper()
	{
		return mSqliteOpenHelper;
	}

	/**
	 * 关闭数据库连接
	 */
	public void close()
	{
		mSqliteOpenHelper.close();
	}

	/**
	 * 集合是否为空.
	 * 
	 * @param list
	 * @return
	 */
	public boolean isEmpty(List<?> list)
	{
		return list == null || list.isEmpty();
	}

}
