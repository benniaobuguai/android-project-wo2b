package com.wo2b.sdk.common.util;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class AnimationUtil
{

	/**
	 * Load anim and set duration
	 * 
	 * @param anim
	 * @param duration
	 * @return
	 */
	public static Animation loadAnimation(Context context, int anim, int duration)
	{
		Animation animation = AnimationUtils.loadAnimation(context, anim);
		animation.setDuration(duration);

		return animation;
	}

}
