package com.wo2b.xxx.webapp.like;

import com.j256.ormlite.field.DatabaseField;

/**
 * 点赞
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class Like implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId = true)
	private long id; // ID
	@DatabaseField
	private String pkgname; // 包名
	@DatabaseField
	private String module; // 模块
	@DatabaseField
	private String titleId; // 标题ID
	@DatabaseField
	private String title; // 标题
	@DatabaseField
	private Integer likeCount = 0; // 点赞次数

	public Like()
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

	public Integer getLikeCount()
	{
		return likeCount;
	}

	public void setLikeCount(Integer likeCount)
	{
		this.likeCount = likeCount;
	}

	@Override
	public String toString()
	{
		return "Like [module=" + module + ", title=" + title + ", likeCount=" + likeCount + "]";
	}

}