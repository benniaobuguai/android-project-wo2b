package com.wo2b.wrapper.component.common;

import android.os.Bundle;

import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyWebViewActivity;

/**
 * 声明
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-10-15
 */
public class StatementActivity extends RockyWebViewActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();

		getWebview().loadUrl("file:///android_asset/www/disclaimer.html");
	}

	@Override
	protected void initView()
	{
		super.initView();
		setActionBarTitle(R.string.disclaimer);
	}

}
