package com.wo2b.sdk.view.viewpager;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.assist.FailReason;
import opensource.component.imageloader.core.assist.SimpleImageLoadingListener;
import opensource.component.photoview.PhotoView;
import opensource.component.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.wo2b.sdk.R;

/**
 * Auto scroll poster, need a DisplayImageOptions.
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @since 2015-3-21
 * @Modify 2015-3-22
 */
public class AutoScrollPoster extends AutoScrollableView<String>
{
	
	private static final String TAG = "AutoScrollPoster";
	
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions mDisplayImageOptions;
	private ScaleType mScaleType = null;
	private boolean mNeedLoadAnimation = true;
	private Drawable mLoadIndeterminateDrawable = null;
	
	private LayoutInflater mLayoutInflater;
	
	public AutoScrollPoster(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	
	public AutoScrollPoster(Context context)
	{
		super(context);
		init();
	}
	
	private void init()
	{
		mLayoutInflater = LayoutInflater.from(getContext());
	}
	
	/**
	 * How to display images
	 * 
	 * @param options
	 */
	public void setDisplayImageOptions(DisplayImageOptions options)
	{
		this.mDisplayImageOptions = options;
	}
	
	/**
	 * Options for scaling the bounds of an image to the bounds of this view.
	 * 
	 * @param scaleType
	 */
	public void setScaleType(ScaleType scaleType)
	{
		this.mScaleType = scaleType;
	}
	
	/**
	 * Needs a load animation.
	 * 
	 * @param needAnimation
	 */
	public void needLoadAnimation(boolean needAnimation)
	{
		this.mNeedLoadAnimation = needAnimation;
	}
	
	/**
	 * {@link ProgressBar#setIndeterminateDrawable(android.graphics.drawable.Drawable)}
	 */
	public void setLoadIndeterminateDrawable(Drawable drawable)
	{
		this.mLoadIndeterminateDrawable = drawable;
	}

	/**
	 * item view click listener.
	 */
	private OnItemViewClickListener mOnItemViewClickListener;
	
	public void setOnItemViewClickListener(OnItemViewClickListener listener)
	{
		this.mOnItemViewClickListener = listener;
	}
	
	/**
	 * ItemView click
	 * 
	 * @author 笨鸟不乖
	 * @email benniaobuguai@gmail.com
	 * @version 1.0.0
	 * @since 2015-4-24
	 * @Modify 2015-4-23
	 */
	public static interface OnItemViewClickListener
	{
		
		void onItemViewClick(View view, Object object);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.wo2b.sdk.view.viewpager.IPagerAdapter#instantiateItem(android.view.ViewGroup, int)
	 */
	@Override
	public Object instantiateItem(ViewGroup view, int position)
	{
		final View imageLayout = mLayoutInflater.inflate(R.layout.wrapper_cn_image_autoplay_item, view, false);
		PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
		if (mScaleType != null)
		{
			imageView.setScaleType(mScaleType);
		}
		
		final String item = getItem(position);
		
		// 添加单击事件, 用于显示菜单.
		imageView.setOnPhotoTapListener(new OnPhotoTapListener()
		{
			
			@Override
			public void onPhotoTap(View view, float x, float y)
			{
				if (mOnItemViewClickListener != null)
				{
					mOnItemViewClickListener.onItemViewClick(view, item);
				}
			}
		});
		
		final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.progressBar);
		mImageLoader.displayImage(item, imageView, mDisplayImageOptions, new SimpleImageLoadingListener()
		{
			
			@Override
			public void onLoadingStarted(String imageUri, View view)
			{
				if (mNeedLoadAnimation)
				{
					progressBar.setVisibility(View.VISIBLE);
					if (mLoadIndeterminateDrawable != null)
					{
						progressBar.setIndeterminateDrawable(mLoadIndeterminateDrawable);
					}
				}
				else
				{
					progressBar.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason)
			{
				progressBar.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
			{
				progressBar.setVisibility(View.GONE);
			}
			
		});

		view.addView(imageLayout, 0);
		return imageLayout;
	}
	
}
