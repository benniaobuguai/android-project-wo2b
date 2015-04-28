package com.wo2b.tu123.ui.settings;

import android.net.TrafficStats;
import android.os.Bundle;

import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.tu123.R;

/**
 * TrafficStatisticsActivity.
 * 
 * @author 笨鸟不乖
 * 
 */
public class TrafficStatisticsActivity extends BaseFragmentActivity
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_setting_traffic_statistics);
		
		initView();
	}
	
	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.traffic_statistics);
		
		TrafficStats.getTotalTxBytes();
	}
	
}
