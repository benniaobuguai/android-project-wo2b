package com.wo2b.tu123.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.wo2b.tu123.R;

/**
 * Custom search box.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class XSearchBox extends RelativeLayout
{
	
	private Context mContext;
	
	private EditText search_box;
	
	public XSearchBox(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		this.mContext = context;
		initView();
	}
	
	public XSearchBox(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		initView();
	}
	
	public XSearchBox(Context context)
	{
		super(context);
		this.mContext = context;
		initView();
	}
	
	private void initView()
	{
		View view = LayoutInflater.from(mContext).inflate(R.layout.x_search_box, this);
		search_box = (EditText) view.findViewById(R.id.search_box);
	}
	
	public void setHint(CharSequence hint)
	{
		search_box.setHint(hint);
	}
	
	public void setHint(int resid)
	{
		search_box.setHint(resid);
	}
	
	public void setText(CharSequence text)
	{
		search_box.setText(text);
	}
	
	public void setText(int resid)
	{
		search_box.setText(resid);
	}
	
}
