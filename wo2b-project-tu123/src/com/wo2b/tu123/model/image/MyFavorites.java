package com.wo2b.tu123.model.image;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

/**
 * My Favorites
 * 
 * @author 笨鸟不乖
 * 
 */
public class MyFavorites extends PhotoInfo
{
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField
	private Date createDate; // 创建时间
	
	public Date getCreateDate()
	{
		return createDate;
	}
	
	public void setCreateDate(Date createDate)
	{
		this.createDate = createDate;
	}
	
	public void photoInfo2Favorites(PhotoInfo photoInfo)
	{
		setId(photoInfo.getId());
		setSloc(photoInfo.getSloc());
		setLloc(photoInfo.getLloc());
		setName(photoInfo.getName());
		setDesc(photoInfo.getDesc());
		setUpdatedTime(photoInfo.getUpdatedTime());
		setUploadedTime(photoInfo.getUploadedTime());
		setSmallUrl(photoInfo.getSmallUrl());
		setLargeUrl(photoInfo.getLargeUrl());
		setHeight(photoInfo.getHeight());
		setWidth(photoInfo.getWidth());
		setAlbumid(photoInfo.getAlbumid());
	}

}
