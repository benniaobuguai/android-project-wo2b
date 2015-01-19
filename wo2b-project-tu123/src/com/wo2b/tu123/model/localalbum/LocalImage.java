package com.wo2b.tu123.model.localalbum;

import java.io.Serializable;

/**
 * 
 * @author Rocky
 * 
 */
public class LocalImage implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	private long _id;
	private String data;
	private String displayName;
	private String title;
	private String mimeType;
	private long size;
	private String bucketDisplayName;
	private String bucketId;
	private double latitude;
	private double longitude;
	
	// 特别适配后的名字
	/** 显示用的名字 */
	private String beautifulName;
	
	private FocusItemInfo itemInfo;
	private int imageCount;
	
	@Override
	public String toString()
	{
		return "LocalImage [bucketDisplayName=" + bucketDisplayName + ", bucketId=" + bucketId + ", imageCount="
				+ imageCount + "]";
	}
	
	public int getImageCount()
	{
		return imageCount;
	}
	
	public void setImageCount(int imageCount)
	{
		this.imageCount = imageCount;
	}
	
	public FocusItemInfo getItemInfo()
	{
		return itemInfo;
	}
	
	public void setItemInfo(FocusItemInfo itemInfo)
	{
		this.itemInfo = itemInfo;
	}
	
	public long getId()
	{
		return _id;
	}
	
	public void setId(long id)
	{
		this._id = id;
	}
	
	public String getData()
	{
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;
	}
	
	public String getDisplayName()
	{
		return displayName;
	}
	
	public void setDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getMimeType()
	{
		return mimeType;
	}
	
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}
	
	public long getSize()
	{
		return size;
	}
	
	public void setSize(long size)
	{
		this.size = size;
	}
	
	public String getBucketDisplayName()
	{
		return bucketDisplayName;
	}
	
	public void setBucketDisplayName(String bucketDisplayName)
	{
		this.bucketDisplayName = bucketDisplayName;
	}
	
	public String getBucketId()
	{
		return bucketId;
	}
	
	public void setBucketId(String bucketId)
	{
		this.bucketId = bucketId;
	}
	
	public double getLatitude()
	{
		return latitude;
	}
	
	public void setLatitude(double latitude)
	{
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return longitude;
	}
	
	public void setLongitude(double longitude)
	{
		this.longitude = longitude;
	}
	
	public String getBeautifulName()
	{
		return beautifulName;
	}
	
	public void setBeautifulName(String beautifulName)
	{
		this.beautifulName = beautifulName;
	}
	
}
