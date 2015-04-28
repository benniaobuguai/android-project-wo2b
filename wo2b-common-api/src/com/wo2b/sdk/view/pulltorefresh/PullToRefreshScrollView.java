package com.wo2b.sdk.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 系统性上下拉控件
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-10
 */
public class PullToRefreshScrollView extends opensource.component.pulltorefresh.PullToRefreshScrollView
{

	public PullToRefreshScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		bindRefreshViewNothing();
	}

	public PullToRefreshScrollView(Context context, opensource.component.pulltorefresh.PullToRefreshBase.Mode mode,
			opensource.component.pulltorefresh.PullToRefreshBase.AnimationStyle style)
	{
		super(context, mode, style);
		bindRefreshViewNothing();
	}

	public PullToRefreshScrollView(Context context, opensource.component.pulltorefresh.PullToRefreshBase.Mode mode)
	{
		super(context, mode);
		bindRefreshViewNothing();
	}

	public PullToRefreshScrollView(Context context)
	{
		super(context);
		bindRefreshViewNothing();
	}

	private void bindRefreshViewNothing()
	{
		PullToRefreshHelper.bindRefreshViewNothing(this, Mode.BOTH);
	}

}
