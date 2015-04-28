package com.wo2b.wrapper.component.anim;

import com.wo2b.wrapper.R;

/**
 * 切换类型
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-08-19
 */
public enum SwitchType
{

	LEFT_IN_HOLD(R.anim.slide_left_in, R.anim.hold, R.anim.hold, R.anim.slide_left_out),

	RIGHT_IN_HOLD(R.anim.slide_right_in, R.anim.hold, R.anim.hold, R.anim.slide_right_out),

	BOTTOM_IN_HOLD(R.anim.slide_bottom_in, R.anim.hold, R.anim.hold, R.anim.slide_bottom_out),

	LEFT_IN_RIGHT_OUT(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out),

	;

	private int openEnter;

	private int openExit;

	private int closeEnter;

	private int closeExit;

	private SwitchType(int openEnter, int openExit, int closeEnter, int closeExit)
	{
		this.openEnter = openEnter;
		this.openExit = openExit;
		this.closeEnter = closeEnter;
		this.closeExit = closeExit;
	}

	public int getOpenEnter()
	{
		return openEnter;
	}

	public int getOpenExit()
	{
		return openExit;
	}

	public int getCloseEnter()
	{
		return closeEnter;
	}

	public int getCloseExit()
	{
		return closeExit;
	}

}
