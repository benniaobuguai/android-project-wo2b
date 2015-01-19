package com.wo2b.xxx.webapp.manager.game;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.wo2b.sdk.database.RockyDao;

/**
 * 游戏排名业务类
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-30
 */
public class GameRankingBiz extends RockyDao<GameRanking>
{

	public GameRankingBiz(OrmLiteSqliteOpenHelper sqliteOpenHelper)
	{
		super(sqliteOpenHelper);
	}

	/**
	 * 排行榜, 按积分排序
	 * 
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<GameRanking> getScoreListOrderByScore(long offset, long count)
	{
		QueryBuilder<GameRanking, ?> qb = getDao().queryBuilder();
		try
		{
			// qb.where().eq("user_name", userName);
			qb.offset(offset);
			qb.limit(count);
			qb.orderBy("score", true);
			return qb.query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 排行榜, 按时间排序
	 * 
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<GameRanking> getScoreListOrderByDate(long offset, long count)
	{
		QueryBuilder<GameRanking, ?> qb = getDao().queryBuilder();
		try
		{
			// qb.where().eq("user_name", userName);
			qb.offset(offset);
			qb.limit(count);
			qb.orderBy("record_date", false);

			return qb.query();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 返回最高得分
	 * 
	 * @return
	 */
	public GameRanking getTopScore()
	{
		QueryBuilder<GameRanking, ?> qb = getDao().queryBuilder();
		try
		{
			// qb.where().eq("user_name", userName);
			qb.orderBy("score", false);
			return qb.queryForFirst();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 把没有用户的数据更新为当前用户的信息
	 * 
	 * @param userName
	 */
	public int updateRecordToNewUser(String userName)
	{
		UpdateBuilder<GameRanking, ?> ub = getDao().updateBuilder();
		try
		{
			ub.updateColumnValue("user_name", userName);
			ub.updateColumnValue("nickname", userName);
			ub.where().isNull("user_name").or().eq("user_name", "");

			return ub.update();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

}
