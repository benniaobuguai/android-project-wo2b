package com.wo2b.tu123.ui.extra;

import android.os.Bundle;

import com.wo2b.wrapper.app.RockyWebViewActivity;
import com.wo2b.tu123.R;

/**
 * 开发者主页
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class DeveloperHomeActivity extends RockyWebViewActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rocky_developer_home);

		initView();
		getWebview().loadUrl("file:///android_asset/www/developer_home.html");
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.support_developer);
	}

}
