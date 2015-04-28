package com.wo2b.tu123.business.data;

import java.io.File;
import java.io.IOException;

/**
 * 恢复接口
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public interface IRestore
{

	/**
	 * 恢复
	 * 
	 * @param source 被恢复文件
	 * @param target 目标文件
	 */
	void restore(File source, File target) throws IOException;

}
