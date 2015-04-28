package com.wo2b.sdk.view.viewpager;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Auto Scroller
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-3-21
 * @Modify 2015-3-21
 */
public class AutoScroller extends Scroller
{
	
	/**
	 * {@link Scroller#DEFAULT_DURATION}
	 */
	public static final int DEFAULT_DURATION = 250;
	
	/**
	 * 时长因子
	 */
	public static final int FACTOR_LONG = 6;
	/**
	 * 时长因子
	 */
	public static final int FACTOR_SHORT = 1;
	
	/**
	 * 时长
	 */
	private int mDurationX = DEFAULT_DURATION;
	
	/**
	 * 因子
	 */
	private double mFactor = 1;
	
	/**
	 * 
	 * @param context
	 */
	public AutoScroller(Context context)
	{
		super(context);
	}
	
	/**
	 * 
	 * @param context
	 * @param interpolator
	 */
	public AutoScroller(Context context, Interpolator interpolator)
	{
		super(context, interpolator);
	}
	
	@Override
	public void startScroll(int startX, int startY, int dx, int dy, int duration)
	{
		super.startScroll(startX, startY, dx, dy, mDurationX);
	}
	
	@Override
	public void startScroll(int startX, int startY, int dx, int dy)
	{
		super.startScroll(startX, startY, dx, dy, mDurationX);
	}
	
	/**
	 * 设置时长
	 * 
	 * @param duration
	 */
	public void setDurationX(int duration)
	{
		this.mDurationX = duration;
	}
	
	/**
	 * 设置时长
	 * 
	 * @return
	 */
	public int getDurationX()
	{
		return mDurationX;
	}
	
	/**
	 * 设置时长因子, 依赖于{@link AutoScroller#DEFAULT_DURATION}.
	 * 
	 * <pre>
	 * Duration = DEFAULT_DURATION * factor;
	 * 
	 * </pre>
	 * 
	 * @param factor
	 */
	public void setFactor(double factor)
	{
		this.mFactor = factor;
		this.mDurationX = (int) (DEFAULT_DURATION * factor);
	}
	
	/**
	 * 设置时长因子
	 * 
	 * @return
	 */
	public double getFactor()
	{
		return this.mFactor;
	}
	
}