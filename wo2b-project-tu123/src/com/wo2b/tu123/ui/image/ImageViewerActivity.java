package com.wo2b.tu123.ui.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.cache.disc.naming.Md5FileNameGenerator;
import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.assist.FailReason;
import opensource.component.imageloader.core.assist.ImageScaleType;
import opensource.component.imageloader.core.assist.SimpleImageLoadingListener;
import opensource.component.imageloader.core.display.FadeInBitmapDisplayer;
import opensource.component.photoview.PhotoView;
import opensource.component.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.WindowCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;

import com.wo2b.sdk.common.util.DateUtils;
import com.wo2b.sdk.common.util.io.FileUtils;
import com.wo2b.sdk.view.viewpager.ViewPagerCompat;
import com.wo2b.sdk.view.pulltorefresh.PullToRefreshViewPager;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.UserDatabaseHelper;
import com.wo2b.tu123.business.image.MyFavoritesBiz;
import com.wo2b.tu123.global.AppCacheFactory;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.model.image.MyFavorites;
import com.wo2b.tu123.model.image.PhotoInfo;
import com.wo2b.tu123.model.image.PhotoInfoSet;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.widget.VerticalPopupMenu;
import com.wo2b.tu123.ui.widget.VerticalPopupMenu.OnPopupMenuClickListener;
import com.wo2b.tu123.ui.widget.VerticalPopupMenu.PopupMenuItem;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.component.image.CropperActivity;
import com.wo2b.wrapper.component.image.ImageAutoPlayActivity;
import com.wo2b.wrapper.preference.XPreferenceManager;

/**
 * 大图浏览模式
 * 
 * @author 笨鸟不乖
 * 
 */
public class ImageViewerActivity extends BaseFragmentActivity
{

	private static final String TAG = "ImageViewerActivity";
	
	/**
	 * 前7天使用都自动显示ActionBar
	 */
	public static final int NOTICE_MAX = 7;

	/**
	 * 循环周期, 自动显示ActionBar, 便于提醒用户具备此功能点
	 */
	public static final int NOTICE_CYCLE = 10;
	
	
	private static final int RC_SET_WALLPAPER = 100;
	
	
	private static final int MENU_MORE = 100;
	private static final int MENU_MORE_GROUP = 100;
	private static final int MENU_AUTO_PLAY = 1001;
	private static final int MENU_ADD_FAVORITE = 1002;
	private static final int MENU_SET_WALLPAPER = 1003;
	private static final int MENU_SHARE_TO_FRIEND = 1004;
	
	private Menu menuMore = null;
	
	
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private DisplayImageOptions mOptions;
	private String mExtraDirectory;
	private String mCacheDir;
	private boolean mCacheOnDisc = true;
	private Uri mWallpaperBaseUri;
	private Uri mCurrentUri;
	
	private PullToRefreshViewPager mPullToRefreshViewPager;
	private ViewPagerCompat mViewPager;
	private ImageView mFavoriteView;
	
	private static final String STATE_POSITION = "STATE_POSITION";
	
	private List<PhotoInfo> mPhotoList;
	
	public void onCreate(Bundle savedInstanceState)
	{
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		supportRequestWindowFeature(WindowCompat.FEATURE_ACTION_BAR_OVERLAY);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer);
		
		getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg_translucent));
		

		final long useDayCount = XPreferenceManager.getUseDayCount();
		if (useDayCount <= NOTICE_MAX || useDayCount % NOTICE_CYCLE == 0)
		{
			if (!getSupportActionBar().isShowing())
			{
				getSupportActionBar().show();
			}
		}
		
		Bundle bundle = getIntent().getExtras();
		PhotoInfoSet photoInfoSet = (PhotoInfoSet) bundle.getSerializable(RockyIntent.EXTRA_IMAGE_SET);
		int pagerPosition = bundle.getInt(RockyIntent.EXTRA_POSITION, 0);
		mExtraDirectory = bundle.getString(RockyIntent.EXTRA_DIRECTORY);
		String title = photoInfoSet.getAlbumname();
		getSupportActionBar().setTitle(title);
		//getToolbar().setBackgroundResource(R.drawable.actionbar_bg_translucent);
		
		mPhotoList = photoInfoSet.getData();
		
		// boolean isCacheOnDisc = true;
		if (TextUtils.isEmpty(mExtraDirectory))
		{
			// 没有缓存路径, 不进行缓存.
			mCacheOnDisc = false;
		}

		if (savedInstanceState != null)
		{
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		SaveImageOptions saveOptions = new SaveImageOptions.Builder()
			.medule("IMAGE_VIEWER")
			.extraDir(mExtraDirectory)
			.build();

		mWallpaperBaseUri = Uri.parse("file://" + new AppCacheFactory().getWallpaper());
		mCacheDir = "file://" + mImageLoader.getDiscCache().getExtraDir().toString() + "/" + mExtraDirectory;
		
		mOptions = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(mCacheOnDisc)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.saveImageOptions(saveOptions)
			.build();
		
		mFavoriteView = (ImageView) findViewById(R.id.favorite_tips_iv);
		mFavoriteView.setVisibility(View.GONE);
		mPullToRefreshViewPager = (PullToRefreshViewPager) findViewById(R.id.pull_refresh_viewpager);
		mViewPager = mPullToRefreshViewPager.getRefreshableView();
		mViewPager.setAdapter(new ImagePagerAdapter(mPhotoList));
		mViewPager.setCurrentItem(pagerPosition);
		
		initView();
	}
	
	@Override
	protected void initView()
	{
		
	}
	
	@Override
	protected boolean hasActionBar()
	{
		return false;
	}

	@Override
	public void onSaveInstanceState(Bundle outState)
	{
		outState.putInt(STATE_POSITION, mViewPager.getCurrentItem());
	}
	
	private Menu mMenu;
	private View mActionBarMenuItem;
	private PopupWindow mActionBarMenu;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.image_viewer_more, menu);
		
//		mMenu = menu;
//		
//		SubMenu subMenu = menu.addSubMenu(0, MENU_MORE, 0, R.string.menu);
//		subMenu.add(MENU_MORE_GROUP, MENU_AUTO_PLAY, 0, R.string.auto_play);
//		subMenu.add(MENU_MORE_GROUP, MENU_ADD_FAVORITE, 0, R.string.add_favorite);
//		subMenu.add(MENU_MORE_GROUP, MENU_SET_WALLPAPER, 0, R.string.set_wallpaper);
//
//		MenuItem subMenuItem = subMenu.getItem();
//		subMenuItem.setIcon(R.drawable.selector_actionbar_menu_btn);
//		subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//		
//		menuMore = menu;

		return super.onCreateOptionsMenu(menu);
	}
	
	
	@Override
	protected void onActionBarMoreClick()
	{
		if (mActionBarMenu == null)
		{
			mActionBarMenu = new VerticalPopupMenu.Builder(this)
				.addItem(new PopupMenuItem(MENU_AUTO_PLAY, R.string.menu_item_auto_play))
				.addItem(new PopupMenuItem(MENU_ADD_FAVORITE, R.string.menu_item_add_favorite))
				.addItem(new PopupMenuItem(MENU_SET_WALLPAPER, R.string.menu_item_set_wallpaper))
				.addItem(new PopupMenuItem(MENU_SHARE_TO_FRIEND, R.string.menu_item_share))
				.setOnPopupMenuClickListener(new OnPopupMenuClickListener()
				{
					
					@Override
					public void onClick(PopupWindow popup, View itemView, int itemId)
					{
						switch (itemId)
						{
							case MENU_AUTO_PLAY:
							{
								if (mPhotoList == null || mPhotoList.isEmpty())
								{
									return;
								}
								
								ArrayList<String> imageList = new ArrayList<String>();
								for (PhotoInfo photoInfo : mPhotoList)
								{
									imageList.add(photoInfo.getLargeUrl());
								}
								
								String title = null;
								String cacheDir = mExtraDirectory;
								ImageAutoPlayActivity.startImageAutoPlay(ImageViewerActivity.this, imageList, title, cacheDir, mViewPager.getCurrentItem());
								
								// 消耗10金币
								//UserManager.getInstance().spendGold(GoldStore.PRICE_AUTO_PLAY);

								break;
							}
							case MENU_SET_WALLPAPER:
							{
								int position = mViewPager.getCurrentItem();
								PhotoInfo photo = mPhotoList.get(position);

								String requestUrl = photo.getLargeUrl();
								Md5FileNameGenerator md5 = new Md5FileNameGenerator();
								String filename = md5.generate(requestUrl);
								Uri output = Uri.parse(mCacheDir + "/" + filename);
								
								CropperActivity.startCropperActivity(ImageViewerActivity.this, RC_SET_WALLPAPER, photo.getName(), output);
								
								//(ImageViewerActivity.this, RC_SET_WALLPAPER, photo.getName(), imageUri, null);
								
//								mCurrentUri = Uri.parse(imageUri);
//
//								DisplayMetrics dm = CommonUtils.getDisplayMetrics(getContext());
//								int status_bar_height = CommonUtils.getStatusBarHeight(getContext());
//								getWindowManager().getDefaultDisplay().getMetrics(dm);
//								int screenWidth = dm.widthPixels;
//								int screenHeight = dm.heightPixels - status_bar_height;
//
//								mWallpaperUri = Uri.withAppendedPath(mWallpaperBaseUri, filename);
//								File file = new File(mWallpaperUri.getPath());
//								file.getParentFile().mkdirs();
//								if (!file.exists())
//								{
//									try
//									{
//										file.createNewFile();
//									}
//									catch (IOException e)
//									{
//										e.printStackTrace();
//									}
//								}
//
//								gotoCropActivity(mCurrentUri, mWallpaperUri, screenWidth, screenHeight, RC_SET_WALLPAPER);
								break;
							}
							case MENU_ADD_FAVORITE:
							{
								try
								{
									addFavorite();
								}
								catch (IOException e)
								{
									showToastOnUiThread(R.string.hint_add_favorite_failed);
									e.printStackTrace();
								}
								break;
							}
							case MENU_SHARE_TO_FRIEND:
							{
								//
								showToastOnUiThread("Share to friends...");
								break;
							}
						}
					}
				})
				.create();
			
			mActionBarMenuItem = (View) findViewById(R.id.more);
		}
		
		mActionBarMenu.showAsDropDown(mActionBarMenuItem, 0, 0);
		
		//menu.showAtLocation(mActionBarMenuItem, Gravity.CENTER, 0, 0);
		//menu.showAsDropDown(mActionBarMenuItem, 0, getSupportActionBar().getHeight());
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_UP)
		{
			switch (keyCode)
			{
				case KeyEvent.KEYCODE_MENU:
				{
					if (getSupportActionBar().isShowing() && menuMore != null && menuMore.findItem(MENU_MORE) != null)
					{
						menuMore.performIdentifierAction(MENU_MORE, 0);

						return true;
					}
				}
			}
		}
		
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * 裁剪图片
	 * 
	 * @param source
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 */
	private void gotoCropActivity(Uri source, Uri target, int width, int height, int requestCode)
	{
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(source, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", width);
		intent.putExtra("aspectY", height);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, target);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK)
		{
			switch (requestCode)
			{
				case RC_SET_WALLPAPER:
				{
					Uri uri = data.getData();
					gotoWallpaperActivity(uri);
					break;
				}
			}
		}
	}
	
	private void gotoWallpaperActivity(Uri uri)
	{
		Intent intent = new Intent();
		intent.setClass(this, WallpaperDisposeActivity.class);
		intent.setData(uri);
		startActivity(intent);
	}
	
	private class ImagePagerAdapter extends PagerAdapter
	{

		private List<PhotoInfo> mPhotoInfos = new ArrayList<PhotoInfo>();
		private LayoutInflater mInflater;

		ImagePagerAdapter(List<PhotoInfo> photoInfos)
		{
			this.mInflater = getLayoutInflater();
			if (photoInfos != null)
			{
				this.mPhotoInfos = photoInfos;
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		@Override
		public int getCount()
		{
			return mPhotoInfos.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, final int position)
		{
			final View imageLayout = mInflater.inflate(R.layout.image_viewer_item, view, false);
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
			
			// 添加单击事件, 用于显示菜单.
			imageView.setOnPhotoTapListener(new OnPhotoTapListener()
			{
				
				@Override
				public void onPhotoTap(View view, float x, float y)
				{
					if (getSupportActionBar().isShowing())
					{
						getSupportActionBar().hide();
					}
					else
					{
						getSupportActionBar().show();
					}
				}
			});
			
			
			final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);

			mImageLoader.displayImage(mPhotoInfos.get(position).getLargeUrl(), imageView, mOptions,
					new SimpleImageLoadingListener()
					{
						@Override
						public void onLoadingStarted(String imageUri, View view)
						{
							spinner.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason)
						{
							int errorResId = R.string.hint_unknown;
							switch (failReason.getType())
							{
								case IO_ERROR:
									errorResId = R.string.hint_image_load_error;
									break;
								case DECODING_ERROR:
									errorResId = R.string.hint_image_decode_error;
									break;
								case NETWORK_DENIED:
									errorResId = R.string.hint_network_error;
									break;
								case OUT_OF_MEMORY:
									errorResId = R.string.hint_image_too_large;
									break;
								case UNKNOWN:
									errorResId = R.string.hint_unknown;
									break;
							}
							showToastOnUiThread(errorResId);

							spinner.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
						{
							spinner.setVisibility(View.GONE);
						}
					});

			view.addView(imageLayout, 0);
			return imageLayout;
		}

		@Override
		public boolean isViewFromObject(View view, Object object)
		{
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader)
		{
		}

		@Override
		public Parcelable saveState()
		{
			return null;
		}
	}
	
	/**
	 * 添加至我的最爱, 至多收藏123张.
	 * 
	 * @throws IOException
	 */
	private void addFavorite() throws IOException
	{
		int position = mViewPager.getCurrentItem();
		PhotoInfo photoInfo = mPhotoList.get(position);

		MyFavoritesBiz favoritesBiz = new MyFavoritesBiz(UserDatabaseHelper.getUserDatabaseHelper(mContext));
		MyFavorites favorites = new MyFavorites();
		favorites.photoInfo2Favorites(photoInfo);
		favorites.setCreateDate(DateUtils.newDate());

		// 判断是否已经收藏
		if (favoritesBiz.exists(favorites.getLargeUrl()))
		{
			// showToastOnUiThread(R.string.hint_favorite_exists);
			animationAddFavorite();
			return;
		}

		// copy file to favourite directory.
		if (copy2Favorite(photoInfo))
		{

		}

		if (favoritesBiz.create(favorites) > 0)
		{
			// showToast(R.string.hint_add_favorite_succeed);
			animationAddFavorite();
		}
		else
		{
			showToast(R.string.hint_add_favorite_failed);
		}
	}
	
	/**
	 * 复制至[我的最爱]目录下
	 * 
	 * @param photo
	 * @return
	 * @throws IOException
	 */
	private boolean copy2Favorite(PhotoInfo photo) throws IOException
	{
		File file = ImageHelper.getCacheFile(Uri.parse(mCacheDir).getPath(), photo.getLargeUrl());

		File directory = new File(new AppCacheFactory().getAppDir() + ExtraDir.USERS + getString(R.string.my_favorites));
		FileUtils.copy(file, directory);

		return true;
	}
	
	/**
	 * 添加至我的最爱
	 */
	private void animationAddFavorite()
	{
		AnimationSet animationset = new AnimationSet(false);

		ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		scaleAnimation.setInterpolator(new OvershootInterpolator(5F));// 弹出再回来的动画的效果
		scaleAnimation.setDuration(700);
		AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
		alphaAnimation.setDuration(500);
		alphaAnimation.setStartOffset(700);

		animationset.addAnimation(scaleAnimation);
		animationset.addAnimation(alphaAnimation);
		animationset.setFillAfter(true);

		animationset.setAnimationListener(new AnimationListener()
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
				mFavoriteView.setVisibility(View.GONE);
			}
		});
		mFavoriteView.setVisibility(View.VISIBLE);
		mFavoriteView.startAnimation(animationset);
	}

}