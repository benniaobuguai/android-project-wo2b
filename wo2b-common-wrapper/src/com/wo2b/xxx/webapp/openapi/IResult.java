package com.wo2b.xxx.webapp.openapi;

/**
 * 请求结果返回
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @param <Model>
 */
public interface IResult<Model>
{

	/**
	 * 出错接口回调
	 * 
	 * @param code 错误码
	 * @param message 错误信息
	 */
	public void onError(int code, String message);

	/**
	 * 请求得到目标响应结果
	 * 
	 * @param model 返回的数据对象
	 */
	public void onSuccess(Model model);

}
