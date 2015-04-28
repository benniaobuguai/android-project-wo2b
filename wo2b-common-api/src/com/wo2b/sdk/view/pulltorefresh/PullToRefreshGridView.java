package com.wo2b.sdk.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-11
 */
public class PullToRefreshGridView extends opensource.component.pulltorefresh.PullToRefreshGridView
{

	public PullToRefreshGridView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		bindRefreshViewNothing();
	}

	public PullToRefreshGridView(Context context, opensource.component.pulltorefresh.PullToRefreshBase.Mode mode,
			opensource.component.pulltorefresh.PullToRefreshBase.AnimationStyle style)
	{
		super(context, mode, style);
		bindRefreshViewNothing();
	}

	public PullToRefreshGridView(Context context, opensource.component.pulltorefresh.PullToRefreshBase.Mode mode)
	{
		super(context, mode);
		bindRefreshViewNothing();
	}

	public PullToRefreshGridView(Context context)
	{
		super(context);
		bindRefreshViewNothing();
	}

	private void bindRefreshViewNothing()
	{
		PullToRefreshHelper.bindRefreshViewNothing(this, Mode.BOTH);
	}

}
