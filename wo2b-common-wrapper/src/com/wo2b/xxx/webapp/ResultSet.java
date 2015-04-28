package com.wo2b.xxx.webapp;

import java.util.List;

/**
 * 结果集
 * 
 * @author 笨鸟不乖
 * 
 * @param <T>
 */
public class ResultSet<T>
{
	
	private List<T> data;
	
	private int startIndex;
	
	private int total;
	
	public List<T> getData()
	{
		return data;
	}
	
	public void setData(List<T> data)
	{
		this.data = data;
	}
	
	public int getStartIndex()
	{
		return startIndex;
	}
	
	public void setStartIndex(int startIndex)
	{
		this.startIndex = startIndex;
	}
	
	public int getTotal()
	{
		return total;
	}
	
	public void setTotal(int total)
	{
		this.total = total;
	}
	
	public boolean isEmpty()
	{
		if (getData() == null || getData().isEmpty())
		{
			return true;
		}
		
		return false;
	}
	
}
