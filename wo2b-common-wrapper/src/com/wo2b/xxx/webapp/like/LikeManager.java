package com.wo2b.xxx.webapp.like;

import android.content.Context;

import com.wo2b.sdk.core.RockySdk;
import com.wo2b.wrapper.R;
import com.wo2b.xxx.webapp.Wo2bCode;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.localcache.LocalDbCacheHelper;
import com.wo2b.xxx.webapp.openapi.impl.LikeOpenApi;

public class LikeManager
{

	private LikeOpenApi mLikeOpenApi = new LikeOpenApi();

	/**
	 * 添加或更新当前主题的点赞次数
	 * 
	 * @param pkgname
	 * @param module
	 * @param titleId
	 * @param title
	 * @param wo2bResHandler
	 */
	public void addLike(String pkgname, String module, String titleId, String title,
			final Wo2bResHandler<Void> wo2bResHandler)
	{
		Context context = RockySdk.getInstance().getContext();
		LocalDbCacheHelper dbCacheHelper = new LocalDbCacheHelper(context);
		final LikeDao likeDao = new LikeDao(dbCacheHelper);

		boolean isLike = likeDao.isLike(pkgname, module, titleId, title);
		if (isLike)
		{
			// 已经赞过的不允许重复赞
			wo2bResHandler.onFailure(Wo2bCode.C10000, context.getResources().getString(R.string.api_hint_like_repeat),
					null);

			return;
		}

		final Like like = new Like();
		like.setPkgname(pkgname);
		like.setModule(module);
		like.setTitleId(titleId);
		like.setTitle(title);
		like.setLikeCount(0);

		mLikeOpenApi.addLike(pkgname, module, titleId, title, new Wo2bResHandler<Void>()
		{

			@Override
			public void onSuccess(int code, Void result)
			{
				// 成功返回后, 保存至数据库
				likeDao.create(like);
				wo2bResHandler.onSuccess(code, result);
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				wo2bResHandler.onFailure(code, msg, throwable);
			}
		});
	}

	/**
	 * 查询点赞次数
	 * 
	 * @param pkgname
	 * @param module
	 * @param titleId
	 * @param title
	 * @param wo2bResHandler
	 * @return
	 */
	public void findLike(String pkgname, String module, String titleId, String title,
			Wo2bResHandler<Like> wo2bResHandler)
	{
		mLikeOpenApi.findLike(pkgname, module, titleId, title, wo2bResHandler);
	}

}
