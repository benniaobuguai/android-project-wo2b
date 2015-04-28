package com.wo2b.sdk.view.viewpager;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Auto scroll viewpager
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-3-22
 * @Modify 2015-3-22
 */
public abstract class AutoScrollableView<T> extends AutoScrollViewPager implements IPagerAdapter
{
	
	private static final String TAG = "AutoScrollableView";
	
	private ArrayList<T> mItemList = new ArrayList<T>();
	private AutoScrollPagerAdapter<T> mScrollAdapter;
	
	/**
	 * 
	 * @param context
	 */
	public AutoScrollableView(Context context)
	{
		super(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param attrs
	 */
	public AutoScrollableView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/**
	 * Add new item.
	 * 
	 * @param path
	 */
	public void addItem(T item)
	{
		this.mItemList.add(item);
		
		mScrollAdapter.addItem(item);
	}
	
	/**
	 * Add set of item.
	 * 
	 * @param pathList
	 */
	public void addItems(List<T> items)
	{
		if (items == null || items.isEmpty())
		{
			return;
		}
		
		if (mItemList != null && mItemList.isEmpty())
		{
			// Add the path for the first time.
			mItemList.addAll(items);
			// setImageUrl(pathList.get(0));
		}
		else
		{
			mItemList.addAll(items);
		}
		
		mScrollAdapter = new AutoScrollPagerAdapter<T>(mItemList, this);
		this.setAdapter(mScrollAdapter);
	}
	
	/**
	 * Returns list.
	 * 
	 * @return
	 */
	public List<T> getItemList()
	{
		return this.mItemList;
	}
	
	/**
	 * Returns item of list;
	 * 
	 * @param position
	 * @return
	 */
	public T getItem(int position)
	{
		return getItemList().get(position % mItemList.size());
	}

	/* (non-Javadoc)
	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#destroyItem(android.view.ViewGroup, int, java.lang.Object)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView((View) object);
	}

	/* (non-Javadoc)
	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#getCount()
	 */
	@Override
	public int getCount()
	{
		return 0;
	}

//	/* (non-Javadoc)
//	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#instantiateItem(android.view.ViewGroup, int)
//	 */
//	@Override
//	public Object instantiateItem(ViewGroup view, int position)
//	{
//		return null;
//	}

	/* (non-Javadoc)
	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
	 */
	@Override
	public boolean isViewFromObject(View view, Object object)
	{
		return view.equals(object);
	}

	/* (non-Javadoc)
	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#restoreState(android.os.Parcelable, java.lang.ClassLoader)
	 */
	@Override
	public void restoreState(Parcelable state, ClassLoader loader)
	{
		
	}

	/* (non-Javadoc)
	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#saveState()
	 */
	@Override
	public Parcelable saveState()
	{
		return null;
	}
	
}
