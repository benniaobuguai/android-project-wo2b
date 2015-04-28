package com.wo2b.tu123.ui.image;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.assist.PauseOnScrollListener;
import android.os.Bundle;
import android.widget.AbsListView;

import com.wo2b.sdk.view.pulltorefresh.PullToRefreshGridView;
import com.wo2b.wrapper.app.BaseFragmentActivity;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-12
 */
public class AbsListViewBaseActivity extends BaseFragmentActivity
{

	protected static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
	protected static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";

	protected PullToRefreshGridView pullToRefreshGridView;
	protected AbsListView gridView;

	protected boolean pauseOnScroll = false;
	protected boolean pauseOnFling = true;

	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions mOptions;
	protected SaveImageOptions mSaveOptions;

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
		pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		applyScrollListener();
	}

	private void applyScrollListener()
	{
		pullToRefreshGridView.getRefreshableView().setOnScrollListener(
				new PauseOnScrollListener(mImageLoader, pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
		outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
	}

}
