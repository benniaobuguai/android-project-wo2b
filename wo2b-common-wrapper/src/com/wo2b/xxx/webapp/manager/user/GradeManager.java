package com.wo2b.xxx.webapp.manager.user;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户等级
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-16
 */
public final class GradeManager
{

	private static final int GRADE_1_LIMIT = 999;
	private static final int GRADE_2_LIMIT = 1999;
	private static final int GRADE_3_LIMIT = 2999;
	private static final int GRADE_4_LIMIT = 3999;
	private static final int GRADE_5_LIMIT = 4999;
	private static final int GRADE_6_LIMIT = 6999;
	private static final int GRADE_7_LIMIT = 9999;
	private static final int GRADE_8_LIMIT = 14999;
	private static final int GRADE_9_LIMIT = 29999;
	private static final int GRADE_10_LIMIT = 80000000;

	private GradeManager()
	{

	}

	/**
	 * 根据积分返回用户等级信息
	 * 
	 * @param exp
	 * @return
	 */
	public static final String getGradeByUserExp(long exp)
	{
		String name = "";

		if (exp >= 0 && exp <= GRADE_1_LIMIT)
		{
			name = "二货学徒";
		}
		else if (exp > GRADE_1_LIMIT && exp <= GRADE_2_LIMIT)
		{
			name = "初级二货";
		}
		else if (exp > GRADE_2_LIMIT && exp <= GRADE_3_LIMIT)
		{
			name = "中级二货";
		}
		else if (exp > GRADE_3_LIMIT && exp <= GRADE_4_LIMIT)
		{
			name = "高级二货";
		}
		else if (exp > GRADE_4_LIMIT && exp <= GRADE_5_LIMIT)
		{
			name = "金牌二货";
		}
		else if (exp > GRADE_5_LIMIT && exp <= GRADE_6_LIMIT)
		{
			name = "二货大乘";
		}
		else if (exp > GRADE_6_LIMIT && exp <= GRADE_7_LIMIT)
		{
			name = "二货神王";
		}
		else if (exp > GRADE_7_LIMIT && exp <= GRADE_8_LIMIT)
		{
			name = "二货天尊";
		}
		else if (exp > GRADE_8_LIMIT && exp <= GRADE_9_LIMIT)
		{
			name = "待定";
		}
		else if (exp > GRADE_9_LIMIT && exp <= GRADE_10_LIMIT)
		{
			name = "待定";
		}
		else
		{
			name = "不存在";
		}

		return name;
	}

	/**
	 * 返回用户所有等级信息
	 * 
	 * @return
	 */
	public static final List<Grade> getGradeList()
	{
		List<Grade> gradeList = new ArrayList<Grade>();
		gradeList.add(new Grade(1, "二货学徒", 0, 0, GRADE_1_LIMIT, "普通注册用户"));
		gradeList.add(new Grade(2, "初级二货", GRADE_1_LIMIT + 1, GRADE_1_LIMIT + 1, GRADE_2_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(3, "中级二货", GRADE_2_LIMIT + 1, GRADE_2_LIMIT + 1, GRADE_3_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(4, "高级二货", GRADE_3_LIMIT + 1, GRADE_3_LIMIT + 1, GRADE_4_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(5, "金牌二货", GRADE_4_LIMIT + 1, GRADE_4_LIMIT + 1, GRADE_5_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(6, "二货大乘", GRADE_5_LIMIT + 1, GRADE_5_LIMIT + 1, GRADE_6_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(7, "二货神王", GRADE_6_LIMIT + 1, GRADE_6_LIMIT + 1, GRADE_7_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(8, "二货天尊", GRADE_7_LIMIT + 1, GRADE_7_LIMIT + 1, GRADE_8_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(9, "待定", GRADE_8_LIMIT + 1, GRADE_8_LIMIT + 1, GRADE_9_LIMIT, "达到等级积分"));
		gradeList.add(new Grade(10, "待定", GRADE_9_LIMIT + 1, GRADE_9_LIMIT + 1, GRADE_10_LIMIT, "达到等级积分"));

		return gradeList;
	}

}
