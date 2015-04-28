package com.wo2b.wrapper.component.anim;

import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * View动画构造器
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-08-19
 */
public class ViewAnimCreator
{

	/**
	 * 抖动动画
	 * 
	 * @param count 次数
	 * @return
	 */
	public static Animation shakeAnimation(int count)
	{
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
		translateAnimation.setInterpolator(new CycleInterpolator(count));
		translateAnimation.setDuration(600);

		return translateAnimation;
	}

}
