package com.wo2b.wrapper.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wo2b.wrapper.R;

/**
 * Empty View.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2014-9-13
 * @Modify 2015-4-20
 */
public class EmptyView extends RelativeLayout
{

	private RelativeLayout mRootView;
	private RelativeLayout mEmptyView;
	private ImageView mGifView;
	private ImageView mGifShadowView;
	private ImageView mIndicatorView;
	private TextView mMessageView;
	
	// Animation
	private AnimationSet animationSet;
	private TranslateAnimation top2Bottom;
	private TranslateAnimation bottom2Top;
	private RotateAnimation rotateAnimation1;
	private RotateAnimation rotateAnimation2;
	private int duration = 700;
	
	private int index = 0;
	private int[] drawables =
	{
			R.drawable.empty_1, R.drawable.empty_2, R.drawable.empty_3
	};
	
	public EmptyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		setUp(context);
	}
	
	public EmptyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setUp(context);
	}
	
	public EmptyView(Context context)
	{
		super(context);
		setUp(context);
	}
	
	private void setUp(Context context)
	{
		mRootView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.wrapper_empty_view, this);
		mEmptyView = (RelativeLayout) mRootView.findViewById(R.id.empty_container);
		mGifView = (ImageView) mRootView.findViewById(R.id.empty_gif);
		mGifShadowView = (ImageView) mRootView.findViewById(R.id.empty_gif_panel);
		mIndicatorView = (ImageView) mRootView.findViewById(R.id.empty_indicator);
		mMessageView = (TextView) mRootView.findViewById(R.id.message);
		
		mEmptyView.setVisibility(View.GONE);
	}
	
	/**
	 * 设置监听
	 * 
	 * @param listener
	 */
	public void setOnEmptyViewClickListener(final OnEmptyViewClickListener listener)
	{
		final EmptyView emptyView = this;
		
		emptyViewClick(emptyView, mEmptyView, listener);
	}
	
	/**
	 * 绑定监听至指定的控件
	 * 
	 * @param emptyView
	 * @param view
	 * @param listener
	 */
	private void emptyViewClick(final EmptyView emptyView, final View view, final OnEmptyViewClickListener listener)
	{
		view.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				// 加入判断逻辑
				if (listener != null)
				{
					listener.onEmptyViewClick(emptyView);
				}
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
		
		public void onEmptyViewClick(EmptyView emptyView);
		
	}
	
	/**
	 * 加载状态
	 */
	public synchronized void onLoad()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}
		
		mStatus = STATUS.OK;
		mIndicatorView.setVisibility(View.GONE);
		mGifView.setVisibility(View.VISIBLE);
		mGifShadowView.setVisibility(View.VISIBLE);
		
		loadAnimation();
		mMessageView.setText(R.string.hint_loading);
	}
	
	/**
	 * 没有数据
	 */
	public synchronized void onDataEmpty()
	{
		mStatus = STATUS.NOT_DATA;
	}
	
	/**
	 * 网络异常
	 */
	public synchronized void onNetworkError()
	{
		mStatus = STATUS.NOT_INTERNET;
	}
	
	/**
	 * 不明异常
	 */
	public synchronized void onUnknownError()
	{
		mStatus = STATUS.UNKNOWN;
	}
	
	/**
	 * 自定义异常处理
	 */
	public synchronized void onShow(int icon, String text)
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}
		
		cancelAnimation();
		mGifView.setVisibility(View.GONE);
		mGifShadowView.setVisibility(View.GONE);
		
		mIndicatorView.setVisibility(View.VISIBLE);
		mIndicatorView.setImageResource(icon);
		mMessageView.setText(text);
	}
	
	/**
	 * 加载动画效果
	 */
	private void loadAnimation()
	{
		// mGifShadowView.setVisibility(View.VISIBLE);
		ScaleAnimation scaleAnimation = new ScaleAnimation(0.2f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setDuration(duration);
		scaleAnimation.setRepeatCount(0);
		scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		
		mGifShadowView.setAnimation(scaleAnimation);
		scaleAnimation.startNow();
		
		// scaleAnimation.setAnimationListener(new AnimationListener()
		// {
		//
		// @Override
		// public void onAnimationStart(Animation animation)
		// {
		// }
		//
		// @Override
		// public void onAnimationRepeat(Animation animation)
		// {
		// }
		//
		// @Override
		// public void onAnimationEnd(Animation animation)
		// {
		// ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.2f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
		// Animation.RELATIVE_TO_SELF, 0.5f);
		// scaleAnimation.setDuration(duration);
		// scaleAnimation.setRepeatCount(0);
		// scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		//
		// mGifPanelView.setAnimation(scaleAnimation);
		// scaleAnimation.startNow();
		// }
		// });
		
		// Load
		// mGifView.setVisibility(View.VISIBLE);
		rotateAnimation1 = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation1.setDuration(duration);
		rotateAnimation1.setRepeatCount(0);
		
		rotateAnimation2 = new RotateAnimation(90, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotateAnimation2.setDuration(duration);
		rotateAnimation2.setRepeatCount(0);
		
		// final AnimationSet animationSet = new AnimationSet(false);
		animationSet = new AnimationSet(false);
		top2Bottom = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
				Animation.RELATIVE_TO_PARENT, 0.1f, Animation.RELATIVE_TO_PARENT, 0.75f);
		top2Bottom.setDuration(duration);
		top2Bottom.setRepeatCount(0);
		top2Bottom.setInterpolator(new AccelerateInterpolator(1.2f));
		// image.setImageResource(drawables[++index % drawables.length]);
		
		animationSet.addAnimation(rotateAnimation2);
		animationSet.addAnimation(top2Bottom);
		mGifView.setAnimation(animationSet);
		
		// set1.setDuration(3000);
		animationSet.setFillAfter(true);
		animationSet.setFillEnabled(true);
		animationSet.setRepeatCount(0);
		animationSet.startNow();
		
		animationSet.setAnimationListener(new AnimationListener()
		{
			
			@Override
			public void onAnimationStart(Animation animation)
			{
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				if (!STATUS.OK.equals(mStatus))
				{
					onLoadAnimationEnd();
					
					return;
				}
				
				// GifShadowView伸缩动画
				ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.2f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				scaleAnimation.setDuration(duration);
				scaleAnimation.setRepeatCount(0);
				scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
				
				mGifShadowView.setAnimation(scaleAnimation);
				scaleAnimation.startNow();
				
				// GifView组合动画
				animationSet = new AnimationSet(false);
				
				bottom2Top = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
						Animation.RELATIVE_TO_PARENT, 0.75f, Animation.RELATIVE_TO_PARENT, 0.1f);
				bottom2Top.setDuration(duration);
				bottom2Top.setRepeatCount(0);
				bottom2Top.setInterpolator(new DecelerateInterpolator(2));
				mGifView.setImageResource(drawables[++index % drawables.length]);
				
				animationSet.addAnimation(rotateAnimation1);
				animationSet.addAnimation(bottom2Top);
				mGifView.setAnimation(animationSet);
				
				// set2.setDuration(3000);
				animationSet.setFillAfter(true);
				animationSet.setFillEnabled(true);
				animationSet.setRepeatCount(0);
				animationSet.startNow();
				
				animationSet.setAnimationListener(new AnimationListener()
				{
					
					@Override
					public void onAnimationStart(Animation animation)
					{
					}
					
					@Override
					public void onAnimationRepeat(Animation animation)
					{
					}
					
					@Override
					public void onAnimationEnd(Animation animation)
					{
						loadAnimation();
					}
				});
			}
		});
		
	}
	
	/**
	 * 动画完成触发此方法
	 */
	private void onLoadAnimationEnd()
	{
		switch (mStatus)
		{
			case NOT_DATA:
			{
				onDataEmptyCallback();
				break;
			}
			case NOT_INTERNET:
			{
				onNetworkErrorCallback();
				break;
			}
			case NOT_SDCARD:
			{
				// TODO: For the new demand.
				break;
			}
			case OK:
			{
				break;
			}
			case UNKNOWN:
			{
				onUnknownErrorCallback();
				break;
			}
		}
	}
	
	/**
	 * 没有数据
	 */
	private void onDataEmptyCallback()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}
		
		cancelAnimation();
		mGifView.setVisibility(View.GONE);
		mGifShadowView.setVisibility(View.GONE);
		
		mIndicatorView.setVisibility(View.VISIBLE);
		mIndicatorView.setImageResource(R.drawable.empty_view_data_empty);
		mMessageView.setText(R.string.hint_listview_load_not_data);
	}
	
	/**
	 * 网络异常
	 */
	private void onNetworkErrorCallback()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}
		
		cancelAnimation();
		mGifView.setVisibility(View.GONE);
		mGifShadowView.setVisibility(View.GONE);
		
		mIndicatorView.setVisibility(View.VISIBLE);
		mIndicatorView.setImageResource(R.drawable.empty_view_network_error);
		mMessageView.setText(R.string.hint_listview_network_error);
	}
	
	/**
	 * 不明异常
	 */
	private void onUnknownErrorCallback()
	{
		if (mEmptyView.getVisibility() != View.VISIBLE)
		{
			mEmptyView.setVisibility(View.VISIBLE);
		}
		
		cancelAnimation();
		mGifView.setVisibility(View.GONE);
		mGifShadowView.setVisibility(View.GONE);
		
		mIndicatorView.setVisibility(View.VISIBLE);
		mIndicatorView.setImageResource(R.drawable.empty_view_unknown_error);
		mMessageView.setText(R.string.hint_unknown);
	}
	
	/**
	 * 取消动画效果
	 */
	private void cancelAnimation()
	{
		if (animationSet != null)
		{
			animationSet.cancel();
			
			animationSet = null;
		}
		
		mGifView.clearAnimation();
	}
	
	/**
	 * 状态
	 */
	private STATUS mStatus = STATUS.OK;
	
	public static enum STATUS
	{
		/** 正常状态 */
		OK,
		/** 没有数据 */
		NOT_DATA,
		/** 没有网络 */
		NOT_INTERNET,
		/** 没有SD卡 */
		NOT_SDCARD,
		/** 未知状态 */
		UNKNOWN;
	}
	
}
