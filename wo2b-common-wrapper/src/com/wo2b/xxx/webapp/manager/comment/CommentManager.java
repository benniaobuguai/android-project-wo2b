package com.wo2b.xxx.webapp.manager.comment;

import java.util.List;

import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.openapi.impl.CommentOpenApi;

/**
 * 评论开放接口
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class CommentManager
{

	private CommentOpenApi mCommentOpenApi = new CommentOpenApi();

	/**
	 * 发表评论
	 * 
	 * @param comment
	 * @return
	 * @throws HttpRequestException
	 */
	public void comment(Comment comment, Wo2bResHandler<Void> wo2bResHandler)
	{
		mCommentOpenApi.addComment(comment, wo2bResHandler);
	}

	/**
	 * 查询当前主题的评论内容
	 * 
	 * @param pkgname
	 * @param module
	 * @param title
	 * @param offset
	 * @param count
	 * @return
	 */
	public List<Comment> findComment(String pkgname, String module, String title, int offset, int count)
	{
		return mCommentOpenApi.findComment(pkgname, module, title, offset, count);
	}

}
