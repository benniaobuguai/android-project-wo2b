package com.wo2b.xxx.webapp.ad;

/**
 * 广告类型
 * 
 * @author Alec
 * @email ixueyongjia@gmail.com
 */
public enum AdvertisementType
{

	/**
	 * WO2B应用程序推荐广告
	 */
	COMMON_WO2B_APP(20000, "COMMON_WO2B_APP"),

	/**
	 * 图界传说专属广告
	 */
	COMMON_WO2B_TU123_APP(30000, "COMMON_WO2B_TU123_APP"),

	/**
	 * 魔兽你好专属广告
	 */
	COMMON_WO2B_WAR3_APP(50000, "COMMON_WO2B_WAR3_APP");

	/**
	 * 广告类型ID
	 */
	private int categoryId;

	/**
	 * 广告名称
	 */
	private String category;

	/**
	 * 广告类型私有构造函数
	 * 
	 * @param categoryId
	 * @param category
	 */
	private AdvertisementType(int categoryId, String category)
	{
		this.categoryId = categoryId;
		this.category = category;
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

}
