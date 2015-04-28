package com.wo2b.xxx.webapp.ad;

import com.wo2b.xxx.webapp.Wo2bResListHandler;
import com.wo2b.xxx.webapp.openapi.impl.AdCenterOpenApi;

/**
 * 广告管理类
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class AdvertisementManager
{

	private AdCenterOpenApi mAdCenterOpenApi = new AdCenterOpenApi();

	/**
	 * 获取广告信息
	 * 
	 * @param adType
	 * @param wo2bResHandler
	 */
	public void findByAdType(int adType, Wo2bResListHandler<Advertisement> wo2bResHandler)
	{
		mAdCenterOpenApi.findByAdType(adType, wo2bResHandler);
	}

}
