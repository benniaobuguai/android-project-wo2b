package com.wo2b.tu123.model.image;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

/**
 * Photo Info.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class PhotoInfo implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId = true)
	private Long id; // id
	@DatabaseField
	private String sloc; // 照片的小图ID。照片采用双ID结构，根据任一ID都可获得照片信息
	@DatabaseField
	private String lloc; // 照片的大图ID。照片采用双ID结构，根据任一ID都可获得照片信息
	@DatabaseField
	private String name; // 照片的标题
	@DatabaseField
	private String desc; // 照片的描述信息
	@DatabaseField
	private Date updatedTime; // 照片上次被修改的时间
	@DatabaseField
	private Date uploadedTime; // 照片的上传时间
	@DatabaseField
	private String smallUrl; // 照片的小图的url
	
	@DatabaseField
	private String largeUrl; // 照片的大图的url
	@DatabaseField
	private int height; // 照片的大图的高度。单位：像素
	@DatabaseField
	private int width; // 照片的大图的高度。单位：像素
	
	@DatabaseField
	private String albumid;// 相册ID
	
	private boolean isSelected; // 是否选中
	
	@Override
	public String toString()
	{
		return "PhotoInfo [name=" + name + ", desc=" + desc + ", albumid=" + albumid + "]";
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getSloc()
	{
		return sloc;
	}
	
	public void setSloc(String sloc)
	{
		this.sloc = sloc;
	}
	
	public String getLloc()
	{
		return lloc;
	}
	
	public void setLloc(String lloc)
	{
		this.lloc = lloc;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public Date getUpdatedTime()
	{
		return updatedTime;
	}
	
	public void setUpdatedTime(Date updatedTime)
	{
		this.updatedTime = updatedTime;
	}
	
	public Date getUploadedTime()
	{
		return uploadedTime;
	}
	
	public void setUploadedTime(Date uploadedTime)
	{
		this.uploadedTime = uploadedTime;
	}
	
	public String getSmallUrl()
	{
		return smallUrl;
	}
	
	public void setSmallUrl(String smallUrl)
	{
		this.smallUrl = smallUrl;
	}
	
	public String getLargeUrl()
	{
		return largeUrl;
	}
	
	public void setLargeUrl(String largeUrl)
	{
		this.largeUrl = largeUrl;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public void setWidth(int width)
	{
		this.width = width;
	}
	
	public String getAlbumid()
	{
		return albumid;
	}
	
	public void setAlbumid(String albumid)
	{
		this.albumid = albumid;
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void setSelected(boolean isSelected)
	{
		this.isSelected = isSelected;
	}
}
