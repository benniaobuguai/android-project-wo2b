package com.wo2b.wrapper.component.common;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyListFragmentActivity;
import com.wo2b.xxx.webapp.manager.plugin.PluginInfo;

/**
 * 插件库, 采用动态加载的方式, 根据不同的App, 可以动态定制不一样的插件库.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class PluginCenterActivity extends RockyListFragmentActivity<PluginInfo>
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_wo2b_plugin_list);

		realExecuteFirstTime(null);

		setActionBarTitle(getString(R.string.plugin_center));
	}

	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		return null;
	}

	@Override
	protected XModel<PluginInfo> realOnPullDown(RockyParams params)
	{
		return null;
	}

	@Override
	protected XModel<PluginInfo> realOnPullUp(RockyParams params)
	{
		return null;
	}

}
