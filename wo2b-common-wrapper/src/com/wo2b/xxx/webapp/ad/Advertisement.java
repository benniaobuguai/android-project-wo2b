package com.wo2b.xxx.webapp.ad;

import java.io.Serializable;

/**
 * 广告
 * 
 * @author Alec
 * @email ixueyongjia@gmail.com
 */
public class Advertisement implements Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private long id;

	/**
	 * 广告类型id
	 */
	private int categoryId;

	/**
	 * 广告类型
	 */
	private String category;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容
	 */
	private String content;

	/**
	 * 封面图片
	 */
	private String coverUrl;

	/**
	 * 图片宽度
	 */
	private int width;

	/**
	 * 图片高度
	 */
	private int height;

	/**
	 * 广告的地址信息, 由客户端决定如何处理.
	 */
	private String detailUrl;

	/**
	 * 排序, 同一批次的广告会有多个
	 */
	private int orderBy;

	/**
	 * 备注
	 */
	private String remark;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public int getCategoryId()
	{
		return categoryId;
	}

	public void setCategoryId(int categoryId)
	{
		this.categoryId = categoryId;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getCoverUrl()
	{
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl)
	{
		this.coverUrl = coverUrl;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
	}

	public String getDetailUrl()
	{
		return detailUrl;
	}

	public void setDetailUrl(String detailUrl)
	{
		this.detailUrl = detailUrl;
	}

	public int getOrderBy()
	{
		return orderBy;
	}

	public void setOrderBy(int orderBy)
	{
		this.orderBy = orderBy;
	}

	public String getRemark()
	{
		return remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

}
