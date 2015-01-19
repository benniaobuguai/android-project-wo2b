package com.wo2b.tu123.model.image;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

/**
 * Album Info.
 * 
 * @author Rocky
 * 
 */
public class AlbumInfo implements Serializable
{
	
	private static final long serialVersionUID = 1L;

	@DatabaseField(generatedId = true)
	private Long id; // id
	
	@DatabaseField
	private String albumid;// 相册ID
	@DatabaseField
	private String classid;// 相册分类ID
	@DatabaseField
	private Date createtime;// 相册创建时间
	@DatabaseField
	private String desc;// 相册描述
	@DatabaseField
	private String name;// 相册名称
	@DatabaseField
	private String coverurl;// 相册封面照片地址
	@DatabaseField
	private int picnum;// 照片数
	
	@DatabaseField
	private String module;// 模块名, 确定用于展示的目标位置

	@DatabaseField
	private int index;// 索引
	@DatabaseField
	private int orderby;// 排序号
	@DatabaseField
	private String category;// 分组名
	
	private int albumnum;// 相册总数
	private long ret;// 返回码，正常为0
	private String msg;// 错误消息
	
	public AlbumInfo()
	{
		
	}
	
	public long getRet()
	{
		return ret;
	}
	
	public void setRet(long ret)
	{
		this.ret = ret;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getAlbumid()
	{
		return albumid;
	}
	
	public void setAlbumid(String albumid)
	{
		this.albumid = albumid;
	}
	
	public String getClassid()
	{
		return classid;
	}
	
	public void setClassid(String classid)
	{
		this.classid = classid;
	}
	
	public Date getCreatetime()
	{
		return createtime;
	}
	
	public void setCreatetime(Date createtime)
	{
		this.createtime = createtime;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public void setDesc(String desc)
	{
		this.desc = desc;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getCoverurl()
	{
		return coverurl;
	}
	
	public void setCoverurl(String coverurl)
	{
		this.coverurl = coverurl;
	}
	
	public int getPicnum()
	{
		return picnum;
	}
	
	public void setPicnum(int picnum)
	{
		this.picnum = picnum;
	}
	
	public int getAlbumnum()
	{
		return albumnum;
	}
	
	public void setAlbumnum(int albumnum)
	{
		this.albumnum = albumnum;
	}
	
	public String getModule()
	{
		return module;
	}

	public void setModule(String module)
	{
		this.module = module;
	}
	
	public int getIndex()
	{
		return index;
	}
	
	public void setIndex(int index)
	{
		this.index = index;
	}
	
	public int getOrderby()
	{
		return orderby;
	}
	
	public void setOrderby(int orderby)
	{
		this.orderby = orderby;
	}
	
	public String getCategory()
	{
		return category;
	}
	
	public void setCategory(String category)
	{
		this.category = category;
	}
	
	public String getMsg()
	{
		return msg;
	}
	
	public void setMsg(String msg)
	{
		this.msg = msg;
	}
	
}
