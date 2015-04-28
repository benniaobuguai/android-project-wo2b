package com.wo2b.wrapper.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import com.wo2b.wrapper.R;

/**
 * 系统所有的Dialog的基类, 此类控制所有Dialog的主题风格等.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-3
 */
public class RockyDialog extends Dialog
{

	private String mMessage;
	
	public RockyDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
	}

	public RockyDialog(Context context, int theme)
	{
		super(context, theme);
	}

	public RockyDialog(Context context)
	{
		this(context, R.style.Rocky_Dialog_NoTitle);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
	}

	/**
	 * 提示信息
	 * 
	 * @param text
	 */
	public void setMessage(int text)
	{
		setMessage(getResources().getString(text));
	}

	/**
	 * 提示信息
	 * 
	 * @param text
	 */
	public void setMessage(String text)
	{
		this.mMessage = text;
	}

	/**
	 * 返回提示信息
	 * 
	 * @return
	 */
	public String getMessage()
	{
		return mMessage;
	}

	/**
	 * Return a Resources instance for your application's package.
	 * 
	 * @return
	 */
	protected Resources getResources()
	{
		return getContext().getResources();
	}

	@Override
	public boolean isShowing()
	{
		return super.isShowing();
	}

	@Override
	public void dismiss()
	{
		super.dismiss();
	}

	@Override
	public void setCancelable(boolean flag)
	{
		super.setCancelable(flag);
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancel)
	{
		super.setCanceledOnTouchOutside(cancel);
	}

}
