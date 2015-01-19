package com.wo2b.tu123.ui.settings;

import android.net.TrafficStats;
import android.os.Bundle;

import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.tu123.R;

/**
 * TrafficStatisticsActivity.
 * 
 * @author Rocky
 * 
 */
public class TrafficStatisticsActivity extends RockyFragmentActivity
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
