package com.wo2b.tu123.model.storage;

/**
 * 文件信息
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class FileInfo
{

	/** 文件名称 */
	private String name;

	/** 文件路径 */
	private String path;

	/** 大小 */
	private long size;

	private String displaySize;

	/** 文件数量 */
	private int fileCount;

	/** 文件夹数量 */
	private int folderCount;

	/** 最后修改日期 */
	private long lastModified;

	/** 总空间 */
	private long totalSpace;

	/** 剩余空间 */
	private long freeSpace;

	/** 已用空间 */
	private long usableSpace;

	@Override
	public String toString()
	{
		return "FileInfo [name=" + name + ", size=" + size + ", fileCount=" + fileCount + "]";
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPath()
	{
		return path;
	}

	public void setPath(String path)
	{
		this.path = path;
	}

	public long getSize()
	{
		return size;
	}

	public void setSize(long size)
	{
		this.size = size;
	}

	public long getLastModified()
	{
		return lastModified;
	}

	public void setLastModified(long lastModified)
	{
		this.lastModified = lastModified;
	}

	public long getTotalSpace()
	{
		return totalSpace;
	}

	public void setTotalSpace(long totalSpace)
	{
		this.totalSpace = totalSpace;
	}

	public long getFreeSpace()
	{
		return freeSpace;
	}

	public void setFreeSpace(long freeSpace)
	{
		this.freeSpace = freeSpace;
	}

	public long getUsableSpace()
	{
		return usableSpace;
	}

	public void setUsableSpace(long usableSpace)
	{
		this.usableSpace = usableSpace;
	}

	public String getDisplaySize()
	{
		return displaySize;
	}

	public void setDisplaySize(String displaySize)
	{
		this.displaySize = displaySize;
	}

	public int getFileCount()
	{
		return fileCount;
	}

	public void setFileCount(int fileCount)
	{
		this.fileCount = fileCount;
	}

	public int getFolderCount()
	{
		return folderCount;
	}

	public void setFolderCount(int folderCount)
	{
		this.folderCount = folderCount;
	}

}
