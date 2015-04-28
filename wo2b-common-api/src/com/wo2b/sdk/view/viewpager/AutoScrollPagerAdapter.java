package com.wo2b.sdk.view.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-3-22
 * @Modify 2015-3-22
 * @param <T>
 */
public class AutoScrollPagerAdapter<T> extends PagerAdapter
{
	
	private List<T> mList = new ArrayList<T>();
	
	private IPagerAdapter mIPagerAdapter;
	
	/**
	 * 
	 * @param list
	 * @param callback
	 */
	public AutoScrollPagerAdapter(List<T> list, IPagerAdapter callback)
	{
		if (list != null)
		{
			this.mList = list;
		}
		this.mIPagerAdapter = callback;
	}
	
	public void addItem(T item)
	{
		this.mList.add(item);
		notifyDataSetChanged();
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		if (mIPagerAdapter != null)
		{
			mIPagerAdapter.destroyItem(container, position, object);
		}
	}
	
	/**
	 * 返回{@link Integer#MAX_VALUE}
	 */
	@Override
	public int getCount()
	{
		// return mList.size();
		return Integer.MAX_VALUE;
	}
	
	/**
	 * 返回真实数据大小
	 * 
	 * @return
	 */
	public int getCountReal()
	{
		return this.mList.size();
	}

	@Override
	public Object instantiateItem(ViewGroup view, final int position)
	{
		if (mIPagerAdapter != null)
		{
			return mIPagerAdapter.instantiateItem(view, position);
		}
		
		throw new NullPointerException("Returns a <NULL> representing the new page.");
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		if (mIPagerAdapter != null)
		{
			return mIPagerAdapter.isViewFromObject(view, object);
		}
		
		return false;
	}
	
	@Override
	public void restoreState(Parcelable state, ClassLoader loader)
	{
		if (mIPagerAdapter != null)
		{
			mIPagerAdapter.restoreState(state, loader);
		}
	}
	
	@Override
	public Parcelable saveState()
	{
		if (mIPagerAdapter != null)
		{
			return mIPagerAdapter.saveState();
		}
		
		return super.saveState();
	}
	
}