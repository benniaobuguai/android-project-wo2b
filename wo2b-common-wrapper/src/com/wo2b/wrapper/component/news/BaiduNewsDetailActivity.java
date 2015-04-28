package com.wo2b.wrapper.component.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.webkit.WebSettings;

import com.wo2b.wrapper.app.BaseWebViewActivity;

/**
 * 百度新闻详情
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-3
 */
public class BaiduNewsDetailActivity extends BaseWebViewActivity
{

	private String mNewsTitle;

	private String mNewsUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		mNewsTitle = intent.getStringExtra("news_title");
		mNewsUrl = intent.getStringExtra("news_url");

		setActionBarTitle(mNewsTitle);

		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		switch (screenDensity)
		{
			case DisplayMetrics.DENSITY_LOW:
			{
				zoomDensity = WebSettings.ZoomDensity.CLOSE;
				break;
			}
			case DisplayMetrics.DENSITY_MEDIUM:
			{
				zoomDensity = WebSettings.ZoomDensity.MEDIUM;
				break;
			}
			case DisplayMetrics.DENSITY_HIGH:
			{
				zoomDensity = WebSettings.ZoomDensity.FAR;
				break;
			}
		}
		// 设置默认缩放方式尺寸
		getWebview().getSettings().setDefaultZoom(zoomDensity);
		getWebview().getSettings().setBuiltInZoomControls(true);
		getWebview().loadUrl(mNewsUrl);
	}

}
