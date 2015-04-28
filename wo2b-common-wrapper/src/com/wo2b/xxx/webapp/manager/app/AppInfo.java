package com.wo2b.xxx.webapp.manager.app;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用的信息
 * 
 * @author 笨鸟不乖
 * 
 */
public class AppInfo implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long id;

	/** 包名, 应用唯一标识 */
	private String pkgname;

	/** 应用名称 */
	private String appname;

	/** 应用图标, 大小最好为96x96 */
	private String logo;

	/** 更新时间 */
	private Date updateTime;

	/** 作者 */
	private String author;

	/** 大小 */
	private long size;

	/** 应用版本 */
	private int versionCode;

	/** 应用版本名称 */
	private String versionName;

	/** 应用描述 */
	private String remark;

	/** 排序 */
	private int orderIndex;

	/** 分组, 应用的类型 */
	private String groupname;

	/** 新应用标识 */
	private boolean isNew;

	/** 热门标识 */
	private boolean isHot;

	/** 推荐指数(10分制度) */
	private int ratings;

	/** 下载地址 */
	private String url;

	/** 是否可见 */
	private boolean isVisible;

	// ---------------------------------------------------
	private boolean isInstall; // 是否已经安装
	private boolean isSelf; // 是否当前App
	private boolean isDownloaded; // 下载的标志

	public AppInfo()
	{

	}

	public AppInfo(String pkgname, String appname)
	{
		this.pkgname = pkgname;
		this.appname = appname;
	}

	public long getId()
	{
		return id;
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

	public String getAppname()
	{
		return appname;
	}

	public void setAppname(String appname)
	{
		this.appname = appname;
	}

	public String getLogo()
	{
		return logo;
	}

	public void setLogo(String logo)
	{
		this.logo = logo;
	}

	public Date getUpdateTime()
	{
		return updateTime;
	}

	public void setUpdateTime(Date updateTime)
	{
		this.updateTime = updateTime;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	public int getVersionCode()
	{
		return versionCode;
	}

	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}

	public String getVersionName()
	{
		return versionName;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

	public int getOrderIndex()
	{
		return orderIndex;
	}

	public void setOrderIndex(int order)
	{
		this.orderIndex = order;
	}

	public String getGroupname()
	{
		return groupname;
	}

	public void setGroupname(String groupname)
	{
		this.groupname = groupname;
	}

	public boolean isNew()
	{
		return isNew;
	}

	public void setNew(boolean isNew)
	{
		this.isNew = isNew;
	}

	public boolean isHot()
	{
		return isHot;
	}

	public void setHot(boolean isHot)
	{
		this.isHot = isHot;
	}

	public int getRatings()
	{
		return ratings;
	}

	public void setRatings(int ratings)
	{
		this.ratings = ratings;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public boolean isVisible()
	{
		return isVisible;
	}

	public void setVisible(boolean isVisible)
	{
		this.isVisible = isVisible;
	}

	public boolean isInstall()
	{
		return isInstall;
	}

	public void setInstall(boolean isInstall)
	{
		this.isInstall = isInstall;
	}

	public boolean isDownloaded()
	{
		return isDownloaded;
	}

	public void setDownloaded(boolean isDownloaded)
	{
		this.isDownloaded = isDownloaded;
	}

	public boolean isSelf()
	{
		return isSelf;
	}

	public void setSelf(boolean isSelf)
	{
		this.isSelf = isSelf;
	}

}
