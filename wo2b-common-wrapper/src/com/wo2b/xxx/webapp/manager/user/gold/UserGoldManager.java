package com.wo2b.xxx.webapp.manager.user.gold;

import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.UserGold;
import com.wo2b.xxx.webapp.openapi.impl.UserGoldOpenApi;

/**
 * 服务器金币控制器
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class UserGoldManager
{

	private UserGoldOpenApi mGoldOpenApi = new UserGoldOpenApi();

	/**
	 * 查询金币
	 * 
	 * @param wo2bResHandler
	 */
	public void findGold(Wo2bResHandler<UserGold> wo2bResHandler)
	{
		mGoldOpenApi.findGold(wo2bResHandler);
	}

	/**
	 * 奖励金币
	 * 
	 * @param gold
	 * @param remark
	 * @param wo2bResHandler
	 */
	public void awardGold(int gold, String remark, Wo2bResHandler<UserGold> wo2bResHandler)
	{
		mGoldOpenApi.awardGold(gold, remark, wo2bResHandler);
	}

	/**
	 * 消耗金币
	 * 
	 * @param gold
	 * @param remark
	 * @param wo2bResHandler
	 */
	public void spendGold(int gold, String remark, Wo2bResHandler<UserGold> wo2bResHandler)
	{
		mGoldOpenApi.spendGold(gold, remark, wo2bResHandler);
	}

}
