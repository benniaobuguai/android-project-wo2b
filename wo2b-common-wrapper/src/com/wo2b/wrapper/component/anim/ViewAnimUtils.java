package com.wo2b.wrapper.component.anim;

import android.view.View;
import android.view.animation.Animation;

/**
 * View动画工具类
 * 
 * <pre>
 * TODO: 待补充扩展
 * 
 * </pre>
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-12-20
 */
public class ViewAnimUtils
{

	/**
	 * 绑定动画至控件, 并执行动画
	 * 
	 * @param view 目标控件
	 * @param count 抖动次数
	 */
	public static void bindShakeAnimation(View view, int count)
	{
		Animation animation = ViewAnimCreator.shakeAnimation(count);
		view.startAnimation(animation);
	}

}
