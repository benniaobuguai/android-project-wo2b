package com.wo2b.xxx.webapp.manager.game;

import java.util.List;

import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.game.GameRanking;
import com.wo2b.xxx.webapp.openapi.impl.GameOpenApi;

/**
 * 游戏开放接口
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-30
 */
public class GameManager
{

	private GameOpenApi mGameOpenApi = new GameOpenApi();

	/**
	 * 新纪录
	 * 
	 * @param score
	 * @param gameAppId
	 * @param gameName
	 * @param gameVersion
	 * @param guid
	 * @param wo2bResHandler
	 */
	public void addNewRecordGUID(long score, String gameAppId, String gameName, String gameVersion, String guid,
			Wo2bResHandler<GameRanking> wo2bResHandler)
	{
		mGameOpenApi.newRecordGUID(score, gameAppId, gameName, gameVersion, guid, wo2bResHandler);
	}

	/**
	 * 新纪录, 不建议使用. 请使用新方法{@link #addNewRecordGUID(long, String, String, String, String, Wo2bResHandler)}
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
	public void upLoadNewRecord(long score, String gameAppId, String gameName, String gameVersion, String userName,
			String nickname, Wo2bResHandler<Void> wo2bResHandler)
	{
		mGameOpenApi.newRecord(score, gameAppId, gameName, gameVersion, userName, nickname, wo2bResHandler);
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
		mGameOpenApi.modifyNickname(gameAppId, guid, nickname, wo2bResHandler);
	}
	
	/**
	 * 异步获取某个游戏某个用户的积分信息
	 * 
	 * @param gameAppId
	 * @param userName
	 * @param wo2bResHandler
	 */
	public void findGameRanking(String gameAppId, String guid, Wo2bResHandler<GameRanking> wo2bResHandler)
	{
		mGameOpenApi.findGameRanking(gameAppId, guid, wo2bResHandler);
	}

	/**
	 * 获取排行榜信息
	 * 
	 * @param gameAppId
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<GameRanking> findGameRanking(String gameAppId, int offset, int count)
	{
		return mGameOpenApi.findGameRanking(gameAppId, offset, count);
	}

}
