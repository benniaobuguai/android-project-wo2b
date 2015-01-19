package com.wo2b.sdk.view;

/**
 * 透明度, 分五个等级
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public enum Transparency
{
	
	LEVEL_1(0x00000000), LEVEL_2(0x44000000), LEVEL_3(0x88000000), LEVEL_4(0xbb000000), LEVEL_5(0xff000000);
	
	public int color;
	
	/**
	 * 构造器默认也只能是private, 从而保证构造函数只能在内部使用
	 * 
	 * @param color
	 */
	Transparency(int color)
	{
		this.color = color;
	}
	
}
