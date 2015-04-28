package com.wo2b.sdk.view.viewpager;
//package com.wo2b.sdk.view;
//
//import android.content.Context;
//import android.support.v4.view.ViewPager;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//
///**
// * 
// * @author 笨鸟不乖
// * @email ixueyongjia@gmail.com
// * @version 1.0.0
// * @date 2014-11-10
// */
//public class ViewPagerHacky extends ViewPager
//{
//
//	public ViewPagerHacky(Context context)
//	{
//		super(context);
//	}
//
//	public ViewPagerHacky(Context context, AttributeSet attrs)
//	{
//		super(context, attrs);
//	}
//
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev)
//	{
//		try
//		{
//			return super.onInterceptTouchEvent(ev);
//		}
//		catch (IllegalArgumentException e)
//		{
//			// 多点触控异常
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//}
