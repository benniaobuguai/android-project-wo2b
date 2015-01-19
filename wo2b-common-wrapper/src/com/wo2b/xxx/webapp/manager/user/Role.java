package com.wo2b.xxx.webapp.manager.user;

/**
 * 用户角色信息
 */
public class Role implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	/**
	 * 普通用户, 默认
	 */
	public static final int NORMAL = 1000;

	/**
	 * VIP用户
	 */
	public static final int VIP = 2000;

	private int id;
	private String role;
	private String name;
	private String remark;

	public Role()
	{
	}

	public int getId()
	{
		return this.id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getRole()
	{
		return this.role;
	}

	public void setRole(String role)
	{
		this.role = role;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRemark()
	{
		return this.remark;
	}

	public void setRemark(String remark)
	{
		this.remark = remark;
	}

}