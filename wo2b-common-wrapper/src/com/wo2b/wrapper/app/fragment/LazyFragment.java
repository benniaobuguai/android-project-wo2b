package com.wo2b.wrapper.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wo2b.sdk.assistant.log.Log;

/**
 * 
 * @author 笨鸟不乖
 * 
 */
public abstract class LazyFragment extends BaseFragment
{

	private static final String TAG = "LazyFragment";
	
	private boolean mViewShow = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (!mViewShow)
		{
			doUserVisibleHint();
		}

		return super.onCreateView(inflater, container, savedInstanceState);
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
			if (!mViewShow && getView() != null)
			{
				mViewShow = true;

				doUserVisibleHint();
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
	public abstract void doUserVisibleHint();

	/**
	 * {@link #setUserVisibleHint(boolean)} 第一次回调时, 调用 {@link #doUserVisibleHint()}方法. 此后每次回调
	 * {@link #setUserVisibleHint(boolean)}后, 都直接回调此方法.
	 * 
	 * POINTAT: 暂无具体应用场景, 仅提供可用接口.
	 */
	public void lazyLoadAfter()
	{

	}

}
