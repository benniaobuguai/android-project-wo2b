package com.wo2b.wrapper.app.fragment;

import android.os.Bundle;
import android.view.View;

import com.wo2b.sdk.assistant.log.Log;

/**
 * 
 * @author 笨鸟不乖
 * 
 * @param <Model>
 */
public abstract class LazyListFragment<Model> extends BaseListFragment<Model>
{

	private static final String TAG = "BaseListLazyFragment";

	private boolean mLoadNow = false;

	@Override
	public void onCreateViewCompleted(View view, Bundle savedInstanceState)
	{
		
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser)
	{
		Log.D(TAG, this.toString() + "setUserVisibleHint: " + isVisibleToUser);

		if (isVisibleToUser)
		{
			if (!mLoadNow)
			{
				mLoadNow = true;
				lazyLoadNow();
			}
			else
			{
				lazyLoadAfter();
			}
		}

		super.setUserVisibleHint(isVisibleToUser);
	}

	/**
	 * {@link #setUserVisibleHint(boolean)} 第一次回调时, 调用此方法. 此后每次回调 {@link #setUserVisibleHint(boolean)}后, 都直接回调
	 * {@link #lazyLoadAfter()}
	 */
	public abstract void lazyLoadNow();

	/**
	 * {@link #setUserVisibleHint(boolean)} 第一次回调时, 调用 {@link #lazyLoadNow()}方法. 此后每次回调
	 * {@link #setUserVisibleHint(boolean)}后, 都直接回调此方法.
	 * 
	 * POINTAT: 暂无具体应用场景, 仅提供可用接口.
	 */
	public void lazyLoadAfter()
	{

	}

}
