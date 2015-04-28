package com.wo2b.xxx.webapp.manager.user;

/**
 * 等级信息
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class Grade implements java.io.Serializable
{

	private static final long serialVersionUID = 1L;

	private int gradeId;
	private String gradeName;
	private int levelExp;
	private String remark;
	private int offset;
	private int end;

	public Grade()
	{
	}

	public Grade(int gradeId, String gradeName, int offset, int end)
	{
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.offset = offset;
		this.end = end;
	}

	public Grade(int gradeId, String gradeName, int levelExp, int offset, int end, String remark)
	{
		this.gradeId = gradeId;
		this.gradeName = gradeName;
		this.levelExp = levelExp;
		this.offset = offset;
		this.end = end;
		this.remark = remark;
	}

	public int getGradeId()
	{
		return this.gradeId;
	}

	public void setGradeId(int gradeId)
	{
		this.gradeId = gradeId;
	}

	public String getGradeName()
	{
		return this.gradeName;
	}

	public void setGradeName(String gradeName)
	{
		this.gradeName = gradeName;
	}

	public int getLevelExp()
	{
		return this.levelExp;
	}

	public void setLevelExp(int levelExp)
	{
		this.levelExp = levelExp;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getEnd()
	{
		return end;
	}

	public void setEnd(int end)
	{
		this.end = end;
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