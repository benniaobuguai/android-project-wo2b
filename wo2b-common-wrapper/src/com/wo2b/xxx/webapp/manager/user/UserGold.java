package com.wo2b.xxx.webapp.manager.user;

/**
 * 用户登录
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class UserGold implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private long userId;
	private int gold;
	private int totalGold;

	public UserGold()
	{
	}

	public UserGold(long userId)
	{
		this.userId = userId;
	}

	public UserGold(long userId, int gold, int totalGold)
	{
		this.userId = userId;
		this.gold = gold;
		this.totalGold = totalGold;
	}

	public long getUserId()
	{
		return userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public int getGold()
	{
		return gold;
	}

	public void setGold(int gold)
	{
		this.gold = gold;
	}

	/**
	 * 只增加不减少
	 * 
	 * @return
	 */
	public int getTotalGold()
	{
		return totalGold;
	}

	/**
	 * 只增加不减少
	 * 
	 * @param totalGold
	 */
	public void setTotalGold(int totalGold)
	{
		this.totalGold = totalGold;
	}

}