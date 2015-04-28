package com.wo2b.wrapper.component.anim;

import android.app.Activity;
import android.content.Intent;

/**
 * 切换Activty动画
 * 
 * <pre>
 * 其他方案有
 * 1. findViewById(android.R.id.content)找到View, 再执行动画切换
 * 2. 通过截取上一个Activity界面
 * 
 * </pre>
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-08-19
 */
public class SwitchActivity
{

	/**
	 * 
	 * @param activity
	 * @param intent
	 * @param switchType
	 */
	public static void startActivity(Activity activity, Intent intent, SwitchType switchType)
	{
		intent.putExtra("switch_animation", switchType);
		activity.startActivity(intent);
		activity.overridePendingTransition(switchType.getOpenEnter(), switchType.getOpenExit());
	}

}
