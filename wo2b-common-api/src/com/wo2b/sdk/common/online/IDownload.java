package com.wo2b.sdk.common.online;

/**
 * 下载接口
 * 
 * @author 笨鸟不乖
 * 
 */
@Deprecated
public interface IDownload<T>
{

	/**
	 * 下载
	 * 
	 * @param requestUrl
	 *            请求下载地址
	 * @param savePath
	 *            提供保存地址, 如果为NULL则表示不需要保存
	 * @return
	 * @throws HttpRequestException
	 */
	T download(String requestUrl, String savePath) throws Exception;

}
