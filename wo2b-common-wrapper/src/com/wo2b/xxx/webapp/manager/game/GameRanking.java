package com.wo2b.xxx.webapp.manager.game;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 游戏得分
 * 
 * <pre>
 * 支持匿名成绩
 * 
 * 1. 匿名积分, 显示为匿名
 * 2.
 * 
 * 
 * 
 * </pre>
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-23
 */
@DatabaseTable(tableName = "t_game_ranking")
public class GameRanking
{

	@DatabaseField(generatedId = true, columnName = "_id")
	private int id;

	/**
	 * 游戏唯一ID
	 */
	@DatabaseField(columnName = "game_app_id")
	private String gameAppId;

	/**
	 * 游戏名称
	 */
	@DatabaseField(columnName = "game_name")
	private String gameName;

	/**
	 * 游戏版本
	 */
	@DatabaseField(columnName = "game_version")
	private String gameVersion;

	/**
	 * 等级: 默认为0, 即表示此游戏无等级制度
	 */
	@DatabaseField(columnName = "game_level")
	private int gameLevel = 0;

	/**
	 * 得分
	 */
	@DatabaseField(columnName = "score")
	private long score;

	/**
	 * 用户名称
	 */
	@DatabaseField(columnName = "user_name")
	private String userName;

	/**
	 * 昵称
	 */
	@DatabaseField(columnName = "nickname")
	private String nickname;

	/**
	 * 创建人
	 */
	@DatabaseField(columnName = "record_date")
	private Date recordDate;

	/**
	 * 同步标志
	 */
	@DatabaseField(columnName = "synch_tag")
	private boolean isSynch;

	/**
	 * 名次
	 */
	private long ranking;

	/**
	 * 唯一键
	 */
	private String guid;

	/**
	 * 名次, 所在位置
	 */
	private int position;

	public GameRanking()
	{

	}

	/**
	 * 构造函数
	 * 
	 * @param gameAppId 游戏ID
	 * @param gameName 游戏名称
	 */
	public GameRanking(String gameAppId, String gameName)
	{
		this.gameAppId = gameAppId;
		this.gameName = gameName;
	}

	@Override
	public String toString()
	{
		return "GameRanking [gameName=" + gameName + ", score=" + score + ", userName=" + userName + ", nickname="
				+ nickname + "]";
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getGameAppId()
	{
		return gameAppId;
	}

	public void setGameAppId(String gameAppId)
	{
		this.gameAppId = gameAppId;
	}

	public String getGameName()
	{
		return gameName;
	}

	public void setGameName(String gameName)
	{
		this.gameName = gameName;
	}

	public String getGameVersion()
	{
		return gameVersion;
	}

	public void setGameVersion(String gameVersion)
	{
		this.gameVersion = gameVersion;
	}

	public int getGameLevel()
	{
		return gameLevel;
	}

	public void setGameLevel(int gameLevel)
	{
		this.gameLevel = gameLevel;
	}

	public long getScore()
	{
		return score;
	}

	public void setScore(long score)
	{
		this.score = score;
	}

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void setNickname(String nickname)
	{
		this.nickname = nickname;
	}

	public Date getRecordDate()
	{
		return recordDate;
	}

	public void setRecordDate(Date recordDate)
	{
		this.recordDate = recordDate;
	}

	public boolean isSynch()
	{
		return isSynch;
	}

	public void setSynch(boolean isSynch)
	{
		this.isSynch = isSynch;
	}

	public long getRanking()
	{
		return ranking;
	}

	public void setRanking(long ranking)
	{
		this.ranking = ranking;
	}

	public String getGuid()
	{
		return guid;
	}

	public void setGuid(String guid)
	{
		this.guid = guid;
	}

	public int getPosition()
	{
		return position;
	}

	public void setPosition(int position)
	{
		this.position = position;
	}

}
