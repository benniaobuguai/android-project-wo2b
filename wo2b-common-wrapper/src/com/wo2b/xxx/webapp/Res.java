package com.wo2b.xxx.webapp;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 */
public class Res implements Serializable
{

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private String data;

	/**
	 * 状态码, 200 表示操作成功.
	 * 
	 * @return
	 */
	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	/**
	 * 对应于状态码的描述
	 */
	public String getMsg()
	{
		return msg;
	}

	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * 返回json中data部分的字符串内容
	 * 
	 * @return
	 */
	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

	/**
	 * 返回列表数据, 反之返回空.
	 * 
	 * @return
	 */
	public JSONArray getDataJSONArray()
	{
		if (data != null)
		{
			JSONObject jsonObject = JSONObject.parseObject(data);
			return jsonObject.getJSONArray("list");
		}

		return null;
	}

	/**
	 * 返回列表数据, 反之返回空.
	 * 
	 * @return
	 */
	public String getDataJSONArrayString()
	{
		JSONArray jsonArray = getDataJSONArray();
		if (jsonArray != null)
		{
			return jsonArray.toString();
		}

		return null;
	}

	/**
	 * 操作成功
	 * 
	 * @return
	 */
	public boolean isOK()
	{
		return this.code == 200;
	}

}
