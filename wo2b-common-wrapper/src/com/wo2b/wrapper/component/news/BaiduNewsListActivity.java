package com.wo2b.wrapper.component.news;

import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyListFragmentActivity;
import com.wo2b.wrapper.component.image.DisplayImageBuilder;

/**
 * 新闻列表
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-2
 */
public class BaiduNewsListActivity extends RockyListFragmentActivity<News>
{

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;

	private int mPage = 1;

	private String mTitle = null;
	private String mKeyword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_news_main);

		Intent intent = getIntent();
		if (intent != null)
		{
			mTitle = intent.getStringExtra("title");
			mKeyword = intent.getStringExtra("keyword");
		}

		if (!TextUtils.isEmpty(mTitle))
		{
			setActionBarTitle(mTitle);
		}
		
		mImageLoader = ImageLoader.getInstance();
		mOptions = DisplayImageBuilder.getDefault()
					.cacheOnDisc(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.displayer(new RoundedBitmapDisplayer(6))
					.build();

		doSearch(mKeyword, mPage);
	}

	/**
	 * 执行搜索
	 * 
	 * @param keyword 关键字
	 * @param page 第几页
	 */
	protected void doSearch(String keyword, int page)
	{
		RockyParams params = new RockyParams();
		params.setNumber1(page);
		params.setText1(keyword);

		realExecuteFirstTime(params);
	}

	@Override
	protected XModel<News> realOnPullDown(RockyParams params)
	{
		List<News> list = NewsManager.getNewsList(params.getText1(), params.getNumber1());
		params.setNumber1(++mPage);

		return XModel.list(list);
	}

	@Override
	protected XModel<News> realOnPullUp(RockyParams params)
	{
		List<News> list = NewsManager.getNewsList(params.getText1(), params.getNumber1());
		params.setNumber1(++mPage);

		return XModel.list(list);
	}

	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = getLayoutInflater().inflate(R.layout.wrapper_news_main_list_item, parent, false);
		}

		TextView tvTitle = ViewUtils.get(convertView, R.id.title);
		TextView tvSource = ViewUtils.get(convertView, R.id.source);
		TextView tvDate = ViewUtils.get(convertView, R.id.date);
		ImageView ivNewsPic = ViewUtils.get(convertView, R.id.newsPic);

		News news = realGetItem(position);

		tvTitle.setText(news.getTitle());
		tvSource.setText(news.getSource());
		tvDate.setText(news.getDate());

		if (TextUtils.isEmpty(news.getPhotoUrl()))
		{
			ivNewsPic.setVisibility(View.GONE);
		}
		else
		{
			ivNewsPic.setVisibility(View.VISIBLE);
			mImageLoader.displayImage(news.getPhotoUrl(), ivNewsPic, mOptions);
		}

		return convertView;
	}

	@Override
	protected void realOnItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		News news = (News) parent.getItemAtPosition(position);
		Intent intent = new Intent();
		intent.setClass(this, BaiduNewsDetailActivity.class);
		intent.putExtra("news_title", news.getTitle());
		intent.putExtra("news_url", news.getUrl());

		startActivity(intent);
	}

	/**
	 * 进入百度新闻搜索列表
	 * 
	 * @param context
	 * @param title
	 * @param keyword
	 */
	public static final void gotoBaiduNewsListActivity(Context context, String title, String keyword)
	{
		Intent intent = new Intent();
		intent.setClass(context, BaiduNewsListActivity.class);
		intent.putExtra("title", title);
		intent.putExtra("keyword", keyword);

		context.startActivity(intent);
	}
	
}
