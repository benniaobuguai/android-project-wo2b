package com.wo2b.tu123.ui.image;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.view.pulltorefresh.PullToRefreshGridView;
import com.wo2b.wrapper.component.security.SecurityTu123;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.business.image.Module;
import com.wo2b.tu123.business.image.PhotoBiz;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.model.image.PhotoInfo;
import com.wo2b.tu123.model.image.PhotoInfoSet;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.global.RockyIntentUtils;
import com.wo2b.wrapper.component.common.CommentActivity;

/**
 * Image Grid List Preview.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class ImageGridActivity extends AbsListViewBaseActivity
{

	private ImageAdapter mImageAdapter = null;
	private PhotoInfoSet mPhotoInfoSet = null;
	private AlbumInfo mAlbumInfo = null;
	private List<PhotoInfo> mPhotoInfos = new ArrayList<PhotoInfo>();
	
	private PhotoBiz mPhotoBiz = null;
	
	private static final int MSG_LOAD_PHOTOS = 1;
	
	// ---------------------------------------------------------------------
	// ---------------------------------------------------------------------
	// ClientContext flags (see flags variable).
	// <-------> <--> <---------------------->
	// 0100 0000 0000 0000 0000 0000 0000 0000
	// 注: 第一位为符号位.
	// 举例: (0x40000000 | 0x8000000) 表示显示标题并且可编辑.
	private int flags;
	
	/** 标题显示标识 */
	public static final int FLAG_TITLE_DISPLAY = 0x40000000;
	/** 可编辑标识 */
	public static final int FLAG_EDIT_SUPPORT = 0x8000000;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_grid_list);
		initView();
		setDefaultValues();
		bindEvents();
	}
	
	@Override
	protected void initView()
	{
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshGridView);
		gridView = pullToRefreshGridView.getRefreshableView();
		Intent intent = getIntent();
		mAlbumInfo = (AlbumInfo) intent.getSerializableExtra(RockyIntent.EXTRA_ALBUM);
		
		switch (Module.valueOf(mAlbumInfo.getModule()))
		{
			case A:
			{
				addFlags(FLAG_TITLE_DISPLAY);
				break;
			}
			case H:
			{
				
				break;
			}
			default:
			{
				
				break;
			}
		}
		
		setActionBarTitle(getString(R.string.name_number, mAlbumInfo.getName(), mAlbumInfo.getPicnum()));
		
//		adLayout = (LinearLayout) findViewById(R.id.adLayout);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig)
	{
//		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//		{
//			// 横屏不显示广告
//			adLayout.setVisibility(View.GONE);
//		}
//		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//		{
//			// 竖屏显示广告
//			adLayout.setVisibility(View.VISIBLE);
//		}

		super.onConfigurationChanged(newConfig);
	}

//	private LinearLayout adLayout = null;

//	/**
//	 * 当前需要显示广告
//	 */
//	private void showAdBanner()
//	{
//		if (RockySdk.getInstance().getRockyConfig().hasAdBanner())
//		{
//			// TODO: 当前需要显示广告
//			// 将广告条adView添加到需要展示的layout控件中
//			
//			// AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//			AdView adView = new AdView(this, AdSize.FIT_SCREEN);
//			adLayout.addView(adView);
//
//			// 监听广告条接口
//			adView.setAdListener(new AdViewListener()
//			{
//
//				@Override
//				public void onSwitchedAd(AdView arg0)
//				{
//					Log.D("Wo2b.Ad", "广告条切换");
//				}
//
//				@Override
//				public void onReceivedAd(AdView arg0)
//				{
//					Log.D("Wo2b.Ad", "请求广告成功");
//
//				}
//
//				@Override
//				public void onFailedToReceivedAd(AdView arg0)
//				{
//					Log.D("Wo2b.Ad", "请求广告失败");
//				}
//			});
//		}
//	}
	
	@Override
	protected void setDefaultValues()
	{
		mPhotoBiz = new PhotoBiz(DatabaseHelper.getDatabaseHelper(mContext));
		
		final String encodeAlbumName = SecurityTu123.encodeText(mAlbumInfo.getName());

		mSaveOptions = new SaveImageOptions.Builder()
			.medule("IMAGE_GRID_LIST")
			.extraDir(ExtraDir.IMAGE + encodeAlbumName)
			.build();
		
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(10))
			.saveImageOptions(mSaveOptions)
			.build();
		
		mImageAdapter = new ImageAdapter();
		((GridView) gridView).setAdapter(mImageAdapter);
		
		getSubHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
	}
	
	@Override
	protected void bindEvents()
	{
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				startImageViewerActivity(position);
			}
		});
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// menu.add(1, 100, 0, "评论").setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		getMenuInflater().inflate(R.menu.common_comment, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.comment:
			{
				// 评论
				CommentActivity.entryComment(this, mAlbumInfo.getModule(), mAlbumInfo.getAlbumid(),
						mAlbumInfo.getName());
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}
	
	
	// -----------------------------------------------------------------------------------
	public int getFlags()
	{
		return flags;
	}
	
	public ImageGridActivity setFlags(int flags)
	{
		this.flags = flags;
		return this;
	}
	
	public ImageGridActivity addFlags(int flags)
	{
		this.flags |= flags;
		return this;
	}
	
	/**
	 * 进入播放界面
	 * 
	 * @param position
	 */
	private void startImageViewerActivity(int position)
	{
		RockyIntentUtils.gotoImageViewerActivity(this, mPhotoInfoSet, position, mSaveOptions.getExtraDir());
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_PHOTOS:
			{
				mImageAdapter.notifyDataSetChanged();
				
				break;
			}
		}
		
		return super.uiHandlerCallback(msg);
	}
	
	@Override
	protected boolean subHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_PHOTOS:
			{
				mPhotoInfoSet = new PhotoInfoSet();
				mPhotoInfoSet.setRet(0);
				mPhotoInfoSet.setData(mPhotoBiz.queryByAlbumid(mAlbumInfo.getAlbumid()));
				if (mPhotoInfoSet.getRet() == 0)
				{
					mPhotoInfoSet.setAlbumname(mAlbumInfo.getName());
					mPhotoInfos = mPhotoInfoSet.getData();

					getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
				}

				return true;
			}
		}

		return super.subHandlerCallback(msg);
	}

	public class ImageAdapter extends BaseAdapter
	{
		@Override
		public int getCount()
		{
			return mPhotoInfos.size();
		}
		
		@Override
		public PhotoInfo getItem(int position)
		{
			return mPhotoInfos.get(position);
		}
		
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.image_grid_list_item, parent, false);
			}
			
			ImageView imageView = ViewUtils.get(convertView, R.id.image);
			LinearLayout desc_line = ViewUtils.get(convertView, R.id.desc_line);
			
			PhotoInfo photoInfo = getItem(position);
			if ((FLAG_TITLE_DISPLAY & getFlags()) == FLAG_TITLE_DISPLAY)
			{
				desc_line.setVisibility(View.VISIBLE);
				
				TextView desc = ViewUtils.get(convertView, R.id.desc);
				desc.setText(photoInfo.getName());
			}
			else
			{
				desc_line.setVisibility(View.GONE);
			}
			
			mImageLoader.displayImage(photoInfo.getLargeUrl(), imageView, mOptions);
			
			return convertView;
		}
	}
	
}