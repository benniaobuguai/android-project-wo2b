package com.wo2b.xxx.webapp.openapi.impl;

import java.util.List;

import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.game.GameRanking;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 评论开放接口
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class GameOpenApi extends OpenApi
{

	/**
	 * 新纪录
	 * 
	 * @param score
	 * @param gameAppId
	 * @param gameName
	 * @param gameVersion
	 * @param userName
	 * @param nickname
	 * @param wo2bResHandler
	 */
	public void newRecordGUID(long score, String gameAppId, String gameName, String gameVersion, String guid, int type,
			Wo2bResHandler<GameRanking> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("score", score);
		params.put("gameAppId", gameAppId);
		params.put("gameName", gameName);
		params.put("gameVersion", gameVersion);
		params.put("guid", guid);
		params.put("type", type);

		post("/mobile/Game_addNewRecordGUID", params, wo2bResHandler);
	}

	/**
	 * 新纪录
	 * 
	 * @param score
	 * @param gameAppId
	 * @param gameName
	 * @param gameVersion
	 * @param userName
	 * @param nickname
	 * @param wo2bResHandler
	 */
	@Deprecated
	public void newRecord(long score, String gameAppId, String gameName, String gameVersion, String userName,
			String nickname, Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("score", score);
		params.put("gameAppId", gameAppId);
		params.put("gameName", gameName);
		params.put("gameVersion", gameVersion);
		params.put("userName", userName);
		params.put("nickname", nickname);

		post("/mobile/Game_addNewRecord", params, wo2bResHandler);
	}

	/**
	 * 修改用户昵称
	 * 
	 * @param gameAppId
	 * @param guid
	 * @param nickname
	 * @param wo2bResHandler
	 */
	public void modifyNickname(String gameAppId, String guid, String nickname, Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("gameAppId", gameAppId);
		params.put("nickname", nickname);
		params.put("guid", guid);

		post("/mobile/Game_modifyNickname", params, wo2bResHandler);
	}

	/**
	 * 异步获取某个游戏某个用户的积分信息
	 * 
	 * @param gameAppId
	 * @param userName
	 * @param wo2bResHandler
	 */
	public void findGameRanking(String gameAppId, String guid, int type, Wo2bResHandler<GameRanking> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("gameAppId", gameAppId);
		params.put("guid", guid);
		params.put("type", type);

		post("/mobile/Game_findGameRanking", params, wo2bResHandler);
	}

	/**
	 * 获取排行榜信息
	 * 
	 * @param gameAppId
	 * @param offset
	 * @param count
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<GameRanking> findGameRanking(String gameAppId, int offset, int count, int type)
	{
		RequestParams params = new RequestParams();
		params.put("gameAppId", gameAppId);
		params.put("offset", offset);
		params.put("count", count);
		params.put("type", type);

		return (List<GameRanking>) postSyncGetList("/mobile/Game_findGameRankingList", params, GameRanking.class);
	}

}
