package com.wo2b.tu123.model.localalbum;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * Local album focus item.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class FocusItemInfo implements Serializable
{
	
	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId = true)
	private Long id; // id
	@DatabaseField
	private String data; // 目录, 结合isSystem使用, 用户自行添加的为完整路径.反之, 为关键路径
	@DatabaseField
	private boolean isSystem; // 是否为系统自带的
	@DatabaseField
	private String bucket_display_name; // 真实显示名称
	@DatabaseField
	private String beautiful_name; // 根据真实名称进行适配后的名称
	@DatabaseField
	private int order_by; // 排序
	@DatabaseField
	private String icon; // 图标, 为空使用缺省图标
	
	@Override
	public String toString()
	{
		return "KoItemInfo [data=" + data + ", bucket_display_name=" + bucket_display_name + ", beautiful_name="
				+ beautiful_name + "]";
	}
	
	public Long getId()
	{
		return id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getData()
	{
		return data;
	}
	
	public void setData(String data)
	{
		this.data = data;
	}
	
	public boolean isSystem()
	{
		return isSystem;
	}
	
	public void setSystem(boolean isSystem)
	{
		this.isSystem = isSystem;
	}
	
	public String getBucket_display_name()
	{
		return bucket_display_name;
	}
	
	public void setBucket_display_name(String bucket_display_name)
	{
		this.bucket_display_name = bucket_display_name;
	}
	
	public String getBeautiful_name()
	{
		return beautiful_name;
	}
	
	public void setBeautiful_name(String beautiful_name)
	{
		this.beautiful_name = beautiful_name;
	}
	
	public int getOrder_by()
	{
		return order_by;
	}
	
	public void setOrder_by(int order_by)
	{
		this.order_by = order_by;
	}
	
	public String getIcon()
	{
		return icon;
	}
	
	public void setIcon(String icon)
	{
		this.icon = icon;
	}
	
}