package com.wo2b.tu123.ui.uc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.view.Transparency;
import com.wo2b.sdk.view.pulltorefresh.PullToRefreshGridView;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.UserDatabaseHelper;
import com.wo2b.tu123.business.image.MyFavoritesBiz;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.model.image.MyFavorites;
import com.wo2b.tu123.model.image.PhotoInfo;
import com.wo2b.tu123.model.image.PhotoInfoSet;
import com.wo2b.tu123.ui.global.RockyIntentUtils;
import com.wo2b.tu123.ui.image.AbsListViewBaseActivity;
import com.wo2b.tu123.ui.image.ImageHelper;
import com.wo2b.tu123.ui.widget.PopupWindow4Delete;
import com.wo2b.tu123.ui.widget.PopupWindow4Delete.OnPopupDeleteClickListener;

/**
 * 我的最爱
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class MyFavoritesActivity extends AbsListViewBaseActivity
{
	
	private ImageAdapter mImageAdapter;
	private PhotoInfoSet mPhotoInfoSet;
	private AlbumInfo mAlbumInfo;
	private List<MyFavorites> mPhotoInfos = new ArrayList<MyFavorites>();
	
	private static final int MSG_LOAD_PHOTOS = 1;
	
	private MyFavoritesBiz mMyFavoritesBiz;
	
	private PopupWindow mEditPopupMenu;
	private boolean isSelectAll = false;
	
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
		
		mAlbumInfo = new AlbumInfo();
		mAlbumInfo.setAlbumid("my_favorites_albumid");
		mAlbumInfo.setName(getString(R.string.my_favorites));
		
		displayTitle(mAlbumInfo.getName(), mAlbumInfo.getPicnum());
	}
	
	private void displayTitle(String album, int number)
	{
		getSupportActionBar().setTitle(getString(R.string.name_number, album, number));
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_edit, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActionBarEditClick()
	{
		final boolean editMode = mImageAdapter.getEditMode();
		// mImageAdapter.setEditMode(!editMode);
		if (editMode)
		{
			entryViewMode();
		}
		else
		{
			entryEditMode();
		}

		mImageAdapter.notifyDataSetChanged();
	}
	
	@Override
	protected void setDefaultValues()
	{
		mMyFavoritesBiz = new MyFavoritesBiz(UserDatabaseHelper.getUserDatabaseHelper(mContext));
		
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("MY_FAVORITES")
			.extraDir(ExtraDir.USERS + mAlbumInfo.getName())
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
	protected void onPause()
	{
		super.onPause();
		if (mEditPopupMenu != null && mEditPopupMenu.isShowing())
		{
			// 避免PopupWindow的泄漏, has leaked window android.widget.PopupWindow$PopupViewContainer
			dismissBottomPopupMenu(mEditPopupMenu);
		}
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	/**
	 * 选择单个
	 * 
	 * @param position
	 */
	private void selectSingle(int position)
	{
		if (selectAble())
		{
			mImageAdapter.getItem(position).setSelected(!mImageAdapter.getItem(position).isSelected());
		}
	}
	
	/**
	 * 选择全部
	 */
	private void selectAll()
	{
		if (selectAble())
		{
			final int count = mImageAdapter.getCount();
			
			for (int position = 0; position < count; position++)
			{
				mImageAdapter.getItem(position).setSelected(true);
			}
			
			mImageAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 取消选择全部
	 */
	private void selectNone()
	{
		if (selectAble())
		{
			final int count = mImageAdapter.getCount();
			for (int position = 0; position < count; position++)
			{
				mImageAdapter.getItem(position).setSelected(false);
			}
			
			mImageAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * 是否可选择
	 * 
	 * @return
	 */
	private boolean selectAble()
	{
		if (mImageAdapter != null && mImageAdapter.getCount() > 0)
		{
			return true;
		}
		
		return false;
	}
	
	private boolean deleteFavorites()
	{
		final int count = mImageAdapter.getCount();
		for (int position = 0; position < count; position++)
		{
			if (mImageAdapter.getItem(position).isSelected())
			{
				final MyFavorites favourite = (MyFavorites) mImageAdapter.getItem(position);
				mMyFavoritesBiz.delete(favourite);
				
				// 开启新线程进行删除
				new Thread(new Runnable()
				{

					@Override
					public void run()
					{
						File file = ImageHelper.getCacheFile(mImageLoader.getDiscCache().getExtraDir().toString() + "/"
								+ mSaveOptions.getExtraDir(), favourite.getLargeUrl());
						if (file.exists())
						{
							file.delete();
						}
					}
				}, "Favourite-Delete").start();

				// mPhotoInfos.remove(position);
			}
		}
		
		//entryViewMode();
		
		getSubHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
		
		return true;
	}
	
	@Override
	protected void bindEvents()
	{
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				ImageAdapter adapter = (ImageAdapter) parent.getAdapter();
				if (adapter.getEditMode())
				{
					adapter.getItem(position).setSelected(!adapter.getItem(position).isSelected());
					adapter.notifyDataSetChanged();
				}
				else
				{
					startImagePagerActivity(position);
				}
			}
		});
		
	}
	
	/**
	 * 退出编辑模式
	 */
	private void entryViewMode()
	{
		mImageAdapter.setEditMode(false);
		dismissBottomPopupMenu(mEditPopupMenu);
	}
	
	/**
	 * 进入编辑模式
	 */
	private void entryEditMode()
	{
		mImageAdapter.setEditMode(true);
		
		// 弹出CommandListPopupWindow
		if (mEditPopupMenu == null)
		{
			mEditPopupMenu = new PopupWindow4Delete.Builder(getContext())
			.setTransparency(Transparency.LEVEL_1)
			.setOutsideTouchable(false)
			.setOnPopupDeleteClickListener(new OnPopupDeleteClickListener()
			{
				
				@Override
				public void onSeleteAll(TextView v)
				{
					if (!isSelectAll)
					{
						selectAll();
						isSelectAll = true;
					}
					else
					{
						selectNone();
						isSelectAll = false;
					}
				}
				
				@Override
				public void onDelete()
				{
					deleteFavorites();
				}
			})
			.create();
		}
		
		showBottomPopupMenu(mEditPopupMenu, R.color.white);
	}

	private void startImagePagerActivity(int position)
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
				displayTitle(mAlbumInfo.getName(), mPhotoInfos.size());
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
				mPhotoInfos = mMyFavoritesBiz.queryForAll();
				
				mPhotoInfoSet = new PhotoInfoSet();
				mPhotoInfoSet.setAlbumname(mAlbumInfo.getName());
				mPhotoInfoSet.setRet(0);
				mPhotoInfoSet.setData(favorites2PhotoInfo(mPhotoInfos));
				
				getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
				return true;
			}
		}
		
		return super.subHandlerCallback(msg);
	}
	
	private List<PhotoInfo> favorites2PhotoInfo(List<MyFavorites> favorites)
	{
		List<PhotoInfo> photoInfos = new ArrayList<PhotoInfo>();
		if (favorites != null && !favorites.isEmpty())
		{
			int count = favorites.size();
			PhotoInfo photoInfo = null;
			for (int i = 0; i < count; i++)
			{
				photoInfo = favorites.get(i);
				photoInfos.add(photoInfo);
			}
		}
		
		return photoInfos;
	}
	
	public class ImageAdapter extends BaseAdapter
	{
		
		private boolean mEditMode;
		
		public void setEditMode(boolean editMode)
		{
			this.mEditMode = editMode;
		}
		
		public boolean getEditMode()
		{
			return mEditMode;
		}
		
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.image_grid_list_item, parent, false);
			}
			ImageView image = ViewUtils.get(convertView, R.id.image);
			
			PhotoInfo photoInfo = getItem(position);
			
			if (mEditMode)
			{
				ImageView tag = ViewUtils.get(convertView, R.id.tag);
				tag.setVisibility(View.VISIBLE);
				if (photoInfo.isSelected())
				{
					tag.setImageResource(R.drawable.checkbox_round_checked);
				}
				else
				{
					tag.setImageResource(R.drawable.checkbox_round_normal);
				}
			}
			else
			{
				ImageView tag = ViewUtils.get(convertView, R.id.tag);
				tag.setVisibility(View.GONE);
			}
			
			mImageLoader.displayImage(photoInfo.getLargeUrl(), image, mOptions);
			
			return convertView;
		}
	}
	
}