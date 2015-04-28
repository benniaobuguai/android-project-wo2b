package com.wo2b.xxx.webapp.manager.user;

import java.util.Date;

/**
 * 金币消费记录
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class UserGoldRecord implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;
	private long userId;
	private int gold;
	private String pkgname;
	private String remark;
	private String createdBy;
	private Date creationDate;

	public UserGoldRecord()
	{
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public long getUserId()
	{
		return this.userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public int getGold()
	{
		return this.gold;
	}

	public void setGold(int gold)
	{
		this.gold = gold;
	}

	public String getPkgname()
	{
		return this.pkgname;
	}

	public void setPkgname(String pkgname)
	{
		this.pkgname = pkgname;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public String getCreatedBy()
	{
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getCreationDate()
	{
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

}