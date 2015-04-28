package com.wo2b.sdk.view.pulltorefresh;

import opensource.component.pulltorefresh.PullToRefreshBase;
import opensource.component.pulltorefresh.PullToRefreshBase.Mode;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-10
 */
public class PullToRefreshHelper
{

	/**
	 * 
	 * @param refreshView
	 */
	@SuppressWarnings("rawtypes")
	public static void bindRefreshViewNothing(PullToRefreshBase refreshView, Mode mode)
	{
		refreshView.setMode(mode);

		refreshView.getLoadingLayoutProxy().setPullLabel("");
		refreshView.getLoadingLayoutProxy().setRefreshingLabel("");
		refreshView.getLoadingLayoutProxy().setReleaseLabel("");
		refreshView.getLoadingLayoutProxy().setLoadingDrawable(null);
	}

}
