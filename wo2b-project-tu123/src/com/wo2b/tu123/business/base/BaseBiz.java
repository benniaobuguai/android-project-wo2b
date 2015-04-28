package com.wo2b.tu123.business.base;

import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;


/**
 * 基础的逻辑层, 提供单表的CRUD; 后续对接云端, 构造函数可能需要调整
 * 
 * @author 笨鸟不乖
 * 
 * @param <Model>
 */
public abstract class BaseBiz<Model>
{
	
	private OrmLiteSqliteOpenHelper mOpenHelper;
	
	/**
	 * 构造函数
	 * 
	 * @param helper
	 */
	public BaseBiz(OrmLiteSqliteOpenHelper helper)
	{
		this.mOpenHelper = helper;
	}
	
	/**
	 * 添加成功返回记录条数,失败返回-1
	 * 
	 * @param model
	 * @return
	 */
	public int create(Model model)
	{
		return -1;
	}
	
	/**
	 * 更新成功返回记录条数,失败返回-1
	 * 
	 * @param model
	 * @return
	 */
	public int update(Model model)
	{
		return -1;
	}
	
	/**
	 * 删除成功返回记录条数,失败返回-1
	 * 
	 * @param model
	 * @return
	 */
	public int delete(Model model)
	{
		return -1;
	}
	
	/**
	 * 
	 * @return
	 */
	public int deleteAll()
	{
		return -1;
	}
	
	/**
	 * 查询返回实体列表数据
	 * 
	 * @return
	 */
	public List<Model> queryForAll()
	{
		return null;
	}
	
	/**
	 * 查询返回实体列表数据
	 * 
	 * @param args
	 *            不定长字符串
	 * @return
	 */
	public List<Model> queryForEq(String arg0, Object arg1)
	{
		return null;
	}
	
	/**
	 * 通过id获取实体对象实例
	 * 
	 * @param id
	 * @return
	 */
	public Model getModel(long id)
	{
		return null;
	}
	
	/**
	 * 返回记录总数
	 * 
	 * @return
	 */
	public long getCount()
	{
		return 0L;
	}

	/**
	 * 返回DatabaseHelper对象
	 * 
	 * @return
	 */
	public OrmLiteSqliteOpenHelper getDatabaseHelper()
	{
		return mOpenHelper;
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
