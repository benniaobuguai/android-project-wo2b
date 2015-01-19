package com.wo2b.wrapper.app.extra;

/**
 * Tab Item Info.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class TabItem
{

	public int itemId;

	public int title;

	public int icon;

	public TabItem(int itemId, int title)
	{
		this(itemId, title, 0);
	}

	public TabItem(int itemId, int title, int icon)
	{
		this.itemId = itemId;
		this.title = title;
		this.icon = icon;
	}

}
