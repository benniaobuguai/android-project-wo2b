package com.wo2b.sdk.view.viewpager;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * {@link PagerAdapter}
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-4-24
 * @Modify 2015-4-24
 */
public interface IPagerAdapter
{
	
	public void destroyItem(ViewGroup container, int position, Object object);
	
	public int getCount();
	
	public Object instantiateItem(ViewGroup view, final int position);
	
	public boolean isViewFromObject(View view, Object object);
	
	public void restoreState(Parcelable state, ClassLoader loader);
	
	public Parcelable saveState();
	
}