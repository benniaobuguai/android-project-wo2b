package com.wo2b.xxx.webapp.manager.comment;

import java.util.Date;

public class Comment implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;
	private String pkgname;
	private String module;
	private String titleId;
	private String title;
	private long userId;
	private String username;
	private String comment;
	private long parentId;
	private Integer praiseCount;
	private Date creationDate;
	private String createdBy;

	public Comment()
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

	public String getPkgname()
	{
		return pkgname;
	}

	public void setPkgname(String pkgname)
	{
		this.pkgname = pkgname;
	}

	public String getModule()
	{
		return this.module;
	}

	public void setModule(String module)
	{
		this.module = module;
	}

	public String getTitleId()
	{
		return this.titleId;
	}

	public void setTitleId(String titleId)
	{
		this.titleId = titleId;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public long getUserId()
	{
		return this.userId;
	}

	public void setUserId(long userId)
	{
		this.userId = userId;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getComment()
	{
		return this.comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public long getParentId()
	{
		return this.parentId;
	}

	public void setParentId(long parentId)
	{
		this.parentId = parentId;
	}

	public Integer getPraiseCount()
	{
		return this.praiseCount;
	}

	public void setPraiseCount(Integer praiseCount)
	{
		this.praiseCount = praiseCount;
	}

	public Date getCreationDate()
	{
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate)
	{
		this.creationDate = creationDate;
	}

	public String getCreatedBy()
	{
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

}