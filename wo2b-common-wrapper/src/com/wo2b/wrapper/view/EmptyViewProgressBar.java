package com.wo2b.wrapper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wo2b.wrapper.R;

/**
 * 空视图
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-9-13
 */
public class EmptyViewProgressBar extends RelativeLayout
{

	private RelativeLayout mRootView;
	
	private RelativeLayout mEmptyView;

	private ProgressBar mProgressBar;

	private ImageView mBackground;

	private TextView mMessage;

	public EmptyViewProgressBar(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setUp(context);
	}

	public EmptyViewProgressBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setUp(context);
	}

	public EmptyViewProgressBar(Context context)
	{
		super(context);
		setUp(context);
	}

	private void setUp(Context context)
	{
		mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.wrapper_empty_view, this);
		mEmptyView = (RelativeLayout) mRootView.findViewById(R.id.container);
		mProgressBar = (ProgressBar) mRootView.findViewById(R.id.progressBar);
		mBackground = (ImageView) mRootView.findViewById(R.id.background);
		mMessage = (TextView) mRootView.findViewById(R.id.message);

		mEmptyView.setVisibility(View.GONE);
	}

	/**
	 * 设置监听
	 * 
	 * @param listener
	 */
	public void setOnEmptyViewClickListener(final OnEmptyViewClickListener listener)
	{
		final EmptyViewProgressBar emptyView = this;

		//emptyViewClick(emptyView, mBackground, listener);
		//emptyViewClick(emptyView, mMessage, listener);
		emptyViewClick(emptyView, mEmptyView, listener);
	}

	/**
	 * 绑定监听至指定的控件
	 * 
	 * @param emptyView
	 * @param view
	 * @param listener
	 */
	private void emptyViewClick(final EmptyViewProgressBar emptyView, final View view, final OnEmptyViewClickListener listener)
	{
		view.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// 加入判断逻辑

				listener.onEmptyViewClick(emptyView);
			}
		});
	}

	/**
	 * 点击事件
	 * 
	 * @author 笨鸟不乖
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-10-18
	 */
	public static interface OnEmptyViewClickListener
	{

		public void onEmptyViewClick(EmptyViewProgressBar emptyView);

	}

	/**
	 * 加载状态
	 */
	public void onLoad()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}

		mBackground.setImageResource(R.drawable.empty_view_loading_bg);
		mProgressBar.setVisibility(View.VISIBLE);
		mMessage.setText(R.string.hint_loading);
	}

	/**
	 * 没有数据
	 */
	public void onDataEmpty()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}

		mBackground.setImageResource(R.drawable.empty_view_data_empty);
		mProgressBar.setVisibility(View.GONE);
		mMessage.setText(R.string.hint_listview_load_not_data);
	}

	/**
	 * 网络异常
	 */
	public void onNetworkError()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}

		mBackground.setImageResource(R.drawable.empty_view_network_error);
		mProgressBar.setVisibility(View.GONE);
		mMessage.setText(R.string.hint_listview_network_error);
	}

	/**
	 * 不明异常
	 */
	public void onUnknownError()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}

		mBackground.setImageResource(R.drawable.empty_view_unknown_error);
		mProgressBar.setVisibility(View.GONE);
		mMessage.setText(R.string.hint_unknown);
	}

	/**
	 * 自定义异常处理
	 */
	public void onShow(int icon, String text)
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}

		mBackground.setImageResource(icon);
		mProgressBar.setVisibility(View.GONE);
		mMessage.setText(text);
	}

}
