package com.wo2b.sdk.view.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-10
 */
public class PullToRefreshViewPager extends opensource.component.pulltorefresh.extras.viewpager.PullToRefreshViewPager
{

	public PullToRefreshViewPager(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		bindRefreshViewNothing();
	}

	public PullToRefreshViewPager(Context context)
	{
		super(context);
		bindRefreshViewNothing();
	}

	private void bindRefreshViewNothing()
	{
		PullToRefreshHelper.bindRefreshViewNothing(this, Mode.BOTH);
	}
}
