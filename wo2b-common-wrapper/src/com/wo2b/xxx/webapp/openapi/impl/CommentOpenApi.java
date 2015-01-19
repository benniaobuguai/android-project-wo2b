package com.wo2b.xxx.webapp.openapi.impl;

import java.util.List;

import com.wo2b.sdk.common.util.http.RequestParams;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.comment.Comment;
import com.wo2b.xxx.webapp.openapi.OpenApi;

/**
 * 评论开放接口
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class CommentOpenApi extends OpenApi
{

	/**
	 * 发表评论
	 * 
	 * @param comment
	 * @param wo2bResHandler
	 */
	public void addComment(Comment comment, Wo2bResHandler<Void> wo2bResHandler)
	{
		RequestParams params = new RequestParams();
		params.put("pkgname", comment.getPkgname());
		params.put("module", comment.getModule());
		params.put("titleId", comment.getTitleId());
		params.put("title", comment.getTitle());
		params.put("comment", comment.getComment());
		params.put("parentId", comment.getParentId());

		post("/mobile/Comment_add2Comment", params, wo2bResHandler);
	}

	/**
	 * 查询当前主题的评论内容
	 * 
	 * @param pkgname
	 * @param module
	 * @param title
	 * @param offset
	 * @param count
	 */
	@SuppressWarnings("unchecked")
	public List<Comment> findComment(String pkgname, String module, String title, int offset, int count)
	{
		RequestParams params = new RequestParams();
		params.put("pkgname", pkgname);
		params.put("module", module);
		params.put("title", title);
		params.put("offset", offset);
		params.put("count", count);

		return (List<Comment>) postSyncGetList("/mobile/Comment_findComment", params, Comment.class);
	}

}
