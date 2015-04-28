package com.wo2b.wrapper.view;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.wo2b.wrapper.R;

/**
 * Image Switcher
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2014-07-15
 */
public class XImageSwitcher extends ImageSwitcher
{

	/**
	 * minimum distance
	 */
	private static final int MIN_X = 20;
	private static final int MIN_Y = 80;
	
	private static final int SCROLL_PERIOD_DEFAULT = 3 * 1000;
	private long mScrollPeriod = SCROLL_PERIOD_DEFAULT;
	
	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions mOptions;
	protected SaveImageOptions mSaveOptions;
	
	private List<String> mImageList = new ArrayList<String>();

	private float mDownX = 0f;
	private float mDownY = 0f;
	private int mCurrentPosition = 0;
	private boolean mAutoScroll = false;
	
	/**
	 * 是否被停止
	 */
	private boolean mStopped = false;
	
	private Handler mHandler = new Handler();

	private Runnable mScrollRunnable = new Runnable()
	{

		@Override
		public void run()
		{
			scrollSelf();

			// Log.d("XImageSwitcher", "Auto Scroll...");
			mHandler.postDelayed(this, mScrollPeriod);
		}
	};
	
	public XImageSwitcher(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		setUp();
	}

	public XImageSwitcher(Context context)
	{
		super(context);
		setUp();
	}
	
	public void setUp()
	{
//		try
//		{
//			getParent().requestDisallowInterceptTouchEvent(true);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
	}
	
	/**
	 * Set default image background
	 * 
	 * @param resid
	 */
	public void setLoadingImage(int resid)
	{
		setImageResource(resid);
	}
	
	/**
	 * Add new image path
	 * 
	 * @param path
	 */
	public void addImagePath(String path)
	{
		this.mImageList.add(path);
	}

	/**
	 * Add set of image path
	 * 
	 * @param pathList
	 */
	public void addImagePath(List<String> pathList)
	{
		if (pathList == null || pathList.isEmpty())
		{
			return;
		}

		if (this.mImageList != null && this.mImageList.isEmpty())
		{
			// Add the path for the first time.
			this.mImageList.addAll(pathList);
			//setImageUrl(pathList.get(0));
		}
		else
		{
			this.mImageList.addAll(pathList);
		}
	}

	/**
	 * How to display images
	 * 
	 * @param options
	 */
	public void setDisplayImageOptions(DisplayImageOptions options)
	{
		this.mOptions = options;
	}

	/**
	 * Auto Scroll
	 * 
	 * @param duration
	 */
	public void startAutoScroll(int duration)
	{
		startAutoScroll(duration, 0);
	}

	/**
	 * Auto Scroll
	 * 
	 * @param duration
	 */
	public void startAutoScroll(int duration, int position)
	{
		this.mScrollPeriod = duration;
		this.mCurrentPosition = position;
		this.mAutoScroll = true;

		if (mImageList != null && !mImageList.isEmpty())
		{
			setImageUrl(mImageList.get(mCurrentPosition));
		}
		
		mHandler.removeCallbacks(mScrollRunnable);
		mHandler.postDelayed(mScrollRunnable, mScrollPeriod);
	}
	
	/**
	 * Resume Scroll
	 */
	public void resumeScroll()
	{
		if (mStopped)
		{
			mHandler.removeCallbacks(mScrollRunnable);
			mHandler.postDelayed(mScrollRunnable, mScrollPeriod);
		}
	}
	
	/**
	 * Change Scroll Period
	 * 
	 * @param duration
	 */
	public void changeScrollPeriod(int duration)
	{
		this.mScrollPeriod = duration;
	}

	/**
	 * Stop Scroll
	 */
	public void stopScroll()
	{
		mStopped = true;
		mHandler.removeCallbacks(mScrollRunnable);
	}

	/**
	 * Scroll itself
	 */
	protected void scrollSelf()
	{
		if (mImageList.isEmpty())
		{
			return;
		}

		setInAnimation(loadAnimation(R.anim.slide_right_in, 1000));
		setOutAnimation(loadAnimation(R.anim.slide_left_out, 1000));

		if (mCurrentPosition < mImageList.size() - 1)
		{
			mCurrentPosition++;
			setImageUrl(mImageList.get(mCurrentPosition % mImageList.size()));
		}
		else
		{
			mCurrentPosition = 0;
			setImageUrl(mImageList.get(mCurrentPosition % mImageList.size()));
		}
	}
	
	@Override
	public void setImageResource(int resid)
	{
		super.setImageResource(resid);
	}

	/**
	 * Use {@link #setImageUrl(String)} instead of it.
	 */
	@Deprecated
	@Override
	public void setImageURI(Uri uri)
	{
		// super.setImageURI(uri);
		ImageView image = (ImageView) this.getNextView();
		String url = uri.getPath();

		mImageLoader.displayImage(url, image, mOptions);

		showNext();
	}
	
	/**
	 * Set image url
	 * 
	 * @param url
	 */
	public void setImageUrl(String url)
	{
		ImageView image = (ImageView) this.getNextView();
		mImageLoader.displayImage(url, image, mOptions);

		showNext();
	}

	/**
	 * Returns current item
	 * 
	 * @param position
	 * @return
	 */
	public String getCurrentImagePath(int position)
	{
		return this.mImageList.get(position);
	}

	/**
	 * Returns current position
	 * 
	 * @return
	 */
	public int getCurrentPosition()
	{
		return mCurrentPosition;
	}

	/**
	 * Scroll items count
	 * 
	 * @return
	 */
	public int getCount()
	{
		return mImageList.size();
	}

	/**
	 * Load anim and set duration
	 * 
	 * @param anim
	 * @param duration
	 * @return
	 */
	private Animation loadAnimation(int anim, int duration)
	{
		Animation animation = AnimationUtils.loadAnimation(getContext(), anim);
		animation.setDuration(duration);

		return animation;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		Log.i("info", "event: " + event);
		
		
		switch (event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
			{
				// coordinate x
				mDownX = event.getX();
				mDownY = event.getY();
				
				return super.onTouchEvent(event);
				//break;
			}
			case MotionEvent.ACTION_UP:
			{
				float lastX = event.getX();
				float lastY = event.getY();

				Log.i("info", "lastX: " + lastX + ", lastY: " + lastY);
				Log.i("info", "lastY: " + lastY + ", lastY: " + lastY);
				Log.i("info", "Math.abs(lastY - mDownY): " + Math.abs(lastY - mDownY));

				Log.i("info", "--------------===");
				
				if (Math.abs(lastY - mDownY) > MIN_Y)
				{
					// Y轴要小于20像素才执行切换
					
					return true;
				}

				if (lastX - mDownX > MIN_X && lastY - mDownY < MIN_Y)
				{
					if (mAutoScroll)
					{
						stopScroll();
					}

					if (mCurrentPosition > 0)
					{
						// Set animations.
						this.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_in));
						this.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_out));
						mCurrentPosition--;
						setImageUrl(mImageList.get(mCurrentPosition % mImageList.size()));
					}
					else
					{
						// Toast.makeText(getContext(), "The first!", Toast.LENGTH_SHORT).show();

						// Rolling cycle
						mCurrentPosition = mImageList.size() - 1;
						setImageUrl(mImageList.get(mCurrentPosition % mImageList.size()));
					}

					if (mAutoScroll)
					{
						resumeScroll();
					}

					return true;
				}

				if (mDownX - lastX > MIN_X)
				{
					if (mAutoScroll)
					{
						stopScroll();
					}

					// Set animations.
					this.setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_right_in));
					this.setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_left_out));

					if (mCurrentPosition < mImageList.size() - 1)
					{
						mCurrentPosition++;
						setImageUrl(mImageList.get(mCurrentPosition % mImageList.size()));
					}
					else
					{
						// Toast.makeText(getContext(), "The end!", Toast.LENGTH_SHORT).show();

						// Rolling cycle
						mCurrentPosition = 0;
						setImageUrl(mImageList.get(mCurrentPosition % mImageList.size()));
					}

					if (mAutoScroll)
					{
						resumeScroll();
					}

					return true;
				}

				break;
			}
			case MotionEvent.ACTION_CANCEL:
			{
				getParent().requestDisallowInterceptTouchEvent(false);
				
				return true;
			}
			case MotionEvent.ACTION_MOVE:
			{
				float lastX = event.getX();
				float lastY = event.getY();
				if (Math.abs(lastY - mDownY) > MIN_Y)
				{
					// Y轴要小于20像素才执行切换
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				else
				{
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				
				return true;
			}
		}

		return true;
		//return super.onTouchEvent(event);
	}
	
//	@Override
//    public boolean onTouchEvent(MotionEvent event) {
//
//        // If this View is not enabled, don't allow for touch interactions.
//        if (!isEnabled()) {
//            return false;
//        }
//
//        switch (event.getAction()) {
//
//            case MotionEvent.ACTION_DOWN:
//                onActionDown(event.getX(), event.getY());
//                return true;
//
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                getParent().requestDisallowInterceptTouchEvent(false);
//                onActionUp();
//                return true;
//
//            case MotionEvent.ACTION_MOVE:
//                onActionMove(event.getX(), event.getY());
//                getParent().requestDisallowInterceptTouchEvent(true);
//                return true;
//
//            default:
//                return false;
//        }
//    }

}
