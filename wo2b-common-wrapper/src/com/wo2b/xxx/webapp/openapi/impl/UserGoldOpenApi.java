package com.wo2b.xxx.webapp.openapi.impl;

import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.UserGold;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 金币接口
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class UserGoldOpenApi extends OpenApi
{

	/**
	 * 查询金币
	 * 
	 * @param wo2bResHandler
	 */
	public void findGold(Wo2bResHandler<UserGold> wo2bResHandler)
	{
		post("/user/UserGold_findGold", wo2bResHandler);
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
		RequestParams params = new RequestParams();
		params.put("gold", gold);
		params.put("remark", remark);
		post("/user/UserGold_awardGold", params, wo2bResHandler);
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
		RequestParams params = new RequestParams();
		params.put("gold", gold);
		params.put("remark", remark);
		post("/user/UserGold_spendGold", params, wo2bResHandler);
	}

}
