package com.wo2b.tu123.model.image;

import java.io.Serializable;
import java.util.List;

/**
 * PhotoInfo Set.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class PhotoInfoSet implements Serializable
{

	private static final long serialVersionUID = 1L;

	private long ret;// 返回码，正常为0
	private String msg; // 如果ret不为0，会有相应的错误信息提示，返回数据全部用UTF-8编码
	private String albumid;// 相册id
	private String albumname;// 相册name

	private int startIndex;
	private int total;

	private List<PhotoInfo> data;

	public boolean isEmpty()
	{
		if (getData() == null || getData().isEmpty())
		{
			return true;
		}

		return false;
	}

	public long getRet()
	{
		return ret;
	}

	public void setRet(long ret)
	{
		this.ret = ret;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public String getAlbumid()
	{
		return albumid;
	}

	public void setAlbumid(String albumid)
	{
		this.albumid = albumid;
	}

	public List<PhotoInfo> getData()
	{
		return data;
	}

	public void setData(List<PhotoInfo> data)
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

	public String getAlbumname()
	{
		return albumname;
	}

	public void setAlbumname(String albumname)
	{
		this.albumname = albumname;
	}

}
