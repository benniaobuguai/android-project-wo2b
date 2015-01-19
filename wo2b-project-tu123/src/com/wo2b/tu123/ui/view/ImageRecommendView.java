package com.wo2b.tu123.ui.view;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.assist.ImageScaleType;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;

/**
 * 
 *
 */
public class ImageRecommendView extends ViewPager
{
	
	private static final String TAG = "Rocky.Recommend";
	
	private static final String MEDULE = "RECOMMEND/ADS";
	private static final String EXTRA_DIR = ExtraDir.ADS + "Recommend";
	//private static final int HACK_COUNT = 10000;
	private int mTotalCount = 0;
	
	private RecommendAdapter mRecommendAdapter;
	private RecommendItem[] mRecommendItem = new RecommendItem[0];
	
	public static class RecommendItem
	{

		public String image;

		public String title;

		public String desc;

		public String linkurl;
		
		public int drawableId;

	}
	
	private long mDuration;
	private boolean mIsScroll = true;
	private Handler mUiHandler = new Handler();
	public Runnable mOnScroll = new Runnable()
	{
		public void run()
		{
			if (mIsScroll)
			{
				setCurrentItem((getCurrentItem() + 1) % getAdapter().getCount(), true);
				
				mUiHandler.postDelayed(this, mDuration);
			}
			
			Log.D("info", "Scroll...");
		}
	};
	
	public ImageRecommendView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView();
		setDefaultValues();
	}

	public ImageRecommendView(Context context)
	{
		super(context);
		initView();
		setDefaultValues();
	}

	private void initView()
	{
//		PageTransformer pageTransformer = new PageTransformer()
//		{
//
//			@Override
//			public void transformPage(View arg0, float arg1)
//			{
//				arg0.setAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right));
//			}
//		};
//		 
//		setPageTransformer(true, pageTransformer);
		
		if (Build.VERSION.SDK_INT >= 11)
		{
			setPageTransformer(true, new DepthPageTransformer());
			//setPageTransformer(true, new ZoomOutPageTransformer());
		}
	}

	private void setDefaultValues()
	{
		//mRecommendAdapter = new RecommendAdapter(getContext(), mRecommendItem);
		//setAdapter(mRecommendAdapter);
	}

	public void setRecommendItem(RecommendItem[] items)
	{
		mRecommendItem = items;
		mTotalCount = items.length * 1000000;
		mRecommendAdapter = new RecommendAdapter(getContext(), mRecommendItem, mTotalCount);
		setAdapter(mRecommendAdapter);
		
		//mRecommendAdapter.notifyDataSetChanged(items);
		
		setCurrentItem(mTotalCount / 2);
	}
	
	public void startScroll()
	{
		stopScroll();
		mUiHandler.postDelayed(mOnScroll, mDuration);
	}
	
	public void startScroll(long milliseconds)
	{
		this.mDuration = milliseconds;
		startScroll();
		//mUiHandler.postDelayed(mOnScroll, 0);
		
		setOnTouchListener(new OnTouchListener()
		{
			
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				switch (event.getAction())
				{
					case MotionEvent.ACTION_UP:
					{
						mIsScroll = true;
						
						stopScroll();
						startScroll();
						break;
					}
					
					case MotionEvent.ACTION_DOWN:
					{
						stopScroll();
						mIsScroll = false;
						break;
					}
				}
				
				return false;
			}
		});
	}
	
	public void stopScroll()
	{
		mUiHandler.removeCallbacks(mOnScroll);
	}
	
	public void recycle()
	{
		mUiHandler.removeCallbacks(mOnScroll);
	}
	
//	@Override
//	public void setPageTransformer(boolean arg0, PageTransformer arg1)
//	{
//		super.setPageTransformer(true, new DepthPageTransformer());
//	}
	
	public static class RecommendAdapter extends PagerAdapter
	{

		private ImageLoader mImageLoader;
		private DisplayImageOptions mOptions;
		
		private LayoutInflater mInflater;
		private RecommendItem[] mItems;
		private int mTotalCount;

		public RecommendAdapter(Context context, RecommendItem[] items, int totalCount)
		{
			this.mInflater = LayoutInflater.from(context);
			this.mItems = items;
			this.mTotalCount = totalCount;
			
			mImageLoader = ImageLoader.getInstance();
			SaveImageOptions saveOptions = new SaveImageOptions.Builder()
				.medule(MEDULE)
				.extraDir(EXTRA_DIR)
				.build();

			mOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.poster_default)
				.showImageForEmptyUri(R.drawable.warn_image_empty)
				.showImageOnFail(R.drawable.warn_image_error)
				//.resetViewBeforeLoading(true)
				.cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory(true)
				.considerExifParams(true)
				//.displayer(new FadeInBitmapDisplayer(300))
				//.displayer(new RoundedBitmapDisplayer(10))
				.saveImageOptions(saveOptions)
				.build();
		}
		
		public void notifyDataSetChanged(RecommendItem[] items)
		{
			this.mItems = items;
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			//return imageUrls.length;
			//return HACK_COUNT;
			return mTotalCount;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0.equals(arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			View imageLayout = mInflater.inflate(R.layout.tu_baike_ads_item, container, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);

			if (mItems.length > 0)
			{
				RecommendItem item = mItems[position % mItems.length];
				imageView.setImageResource(item.drawableId);
				// 每次生成的resId可能不一样, 如果进行缓存会发现结果会乱掉的.
//				mImageLoader.displayImage(item.image, imageView, mOptions, new SimpleImageLoadingListener()
//				{
//					@Override
//					public void onLoadingStarted(String imageUri, View view)
//					{
//						
//					}
//					
//					@Override
//					public void onLoadingFailed(String imageUri, View view, FailReason failReason)
//					{
//						
//					}
//					
//					@Override
//					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
//					{
//						
//					}
//				});
				
				container.addView(imageLayout, 0);
			}
			
			return imageLayout;
		}

	}
	
	
	/**
	 * 在你的transformPage()实现中，基于屏幕上页面的位置，来判断那个页面需要变换，以便创建自定义滑动动画。从transformPage()方法的position参数中可以获得页面的位置。
	 * position参数指明给定页面相对于屏幕中心的位置。它是一个动态属性，会随着页面的滚动而改变。当一个页面填充整个屏幕是，它的值是0，当一个页面刚刚离开屏幕的右边时，它的值是1。当两个也页面分别滚动到一半时，
	 * 其中一个页面的位置是-0.5，另一个页面的位置是0.5。基于屏幕上页面的位置，通过使用诸如setAlpha()、setTranslationX()、或setScaleY()方法来设置页面的属性，来创建自定义的滑动动画。
	 * 当有了一个PageTransformer接口实现时，就可以调用你实现的setPageTransformer()方法来应用自定义动画。
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 */
	public static class DepthPageTransformer implements ViewPager.PageTransformer
	{
		private static float MIN_SCALE = 0.75f;

		@SuppressLint("NewApi")
		public void transformPage(View view, float position)
		{
			int pageWidth = view.getWidth();

			if (position < -1)
			{
				// 页面正在向左离开屏幕 [-Infinity,-1)
				view.setAlpha(0);
			}
			else if (position <= 0)
			{
				// 使用正常的滑动效果，[-1,0]
				view.setAlpha(1);
				view.setScaleX(1);
				view.setScaleY(1);
				
				view.setTranslationX(pageWidth * -position);

			}
			else if (position <= 1)
			{
//				// 渐隐(0,1]
//				view.setAlpha(1 - position);
//
//				// 抵消默认的滑动
//				view.setTranslationX(pageWidth * -position);
//
//				// 按比例缩小图片
//				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
//				view.setScaleX(scaleFactor);
//				view.setScaleY(scaleFactor);
				
				view.setAlpha(1);
				view.setScaleX(1);
				view.setScaleY(1);
				view.setTranslationX(pageWidth * position);
				
			}
			else
			{
				// 页面向右离开屏幕(1,+Infinity]
				//view.setAlpha(0);
				
				view.setAlpha(1);
				view.setScaleX(1);
				view.setScaleY(1);
				view.setTranslationX(pageWidth * position);
			}
		}
	}

	
	public static class DepthPageTransformerDefault implements ViewPager.PageTransformer
	{
		private static float MIN_SCALE = 0.75f;

		@SuppressLint("NewApi")
		public void transformPage(View view, float position)
		{
			int pageWidth = view.getWidth();

			if (position < -1)
			{
				// 页面正在向左离开屏幕 [-Infinity,-1)
				view.setAlpha(0);

			}
			else if (position <= 0)
			{
				// 使用正常的滑动效果，[-1,0]
				view.setAlpha(1);
				view.setTranslationX(0);
				view.setScaleX(1);
				view.setScaleY(1);

			}
			else if (position <= 1)
			{
				// 渐隐(0,1]
				view.setAlpha(1 - position);

				// 抵消默认的滑动
				view.setTranslationX(pageWidth * -position);

				// 按比例缩小图片
				float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);
			}
			else
			{
				// 页面向右离开屏幕(1,+Infinity]
				view.setAlpha(0);
			}
		}
	}
	
	
	public static class ZoomOutPageTransformer implements ViewPager.PageTransformer
	{
		private static float MIN_SCALE = 0.85f;
		private static float MIN_ALPHA = 0.5f;

		@SuppressLint("NewApi")
		public void transformPage(View view, float position)
		{
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1)
			{
				// 这个页面正在向左远离屏幕[-Infinity,-1)
				view.setAlpha(0);

			}
			else if (position <= 1)
			{
				// 修改默认幻灯片动画，缩小页面[-1,1]
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0)
				{
					view.setTranslationX(horzMargin - vertMargin / 2);
				}
				else
				{
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}

				// 按比例缩小页面(比例在 MIN_SCALE 和 1之间)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// 相对于其尺寸进行渐隐
				view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

			}
			else
			{
				// 该页面正在向右远离屏幕, (1,+Infinity]
				view.setAlpha(0);
			}
		}
	}
	

}
