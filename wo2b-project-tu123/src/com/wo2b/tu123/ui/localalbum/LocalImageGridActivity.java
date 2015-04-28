package com.wo2b.tu123.ui.localalbum;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import com.wo2b.wrapper.view.dialog.DialogUtils;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.view.pulltorefresh.PullToRefreshGridView;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.localalbum.LocalImageFactory;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.model.image.PhotoInfo;
import com.wo2b.tu123.model.image.PhotoInfoSet;
import com.wo2b.tu123.model.localalbum.LocalImage;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.global.RockyIntentUtils;
import com.wo2b.tu123.ui.image.AbsListViewBaseActivity;

/**
 * Local image gridview layout.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class LocalImageGridActivity extends AbsListViewBaseActivity
{

	private ImageAdapter mImageAdapter;
//	private PhotoInfoSet mPhotoInfoSet;
	private AlbumInfo mAlbumInfo;
	private List<PhotoInfo> mPhotoInfos = new ArrayList<PhotoInfo>();
	
	private static final int MSG_LOAD_PHOTOS = 1;

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
		setActionBarTitle(R.string.local_album);
		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshGridView);
		gridView = pullToRefreshGridView.getRefreshableView();
		
		Intent intent = getIntent();
		mAlbumInfo = (AlbumInfo) intent.getSerializableExtra(RockyIntent.EXTRA_ALBUM);
		setActionBarTitle(getString(R.string.name_number, mAlbumInfo.getName(), mAlbumInfo.getPicnum()));
	}
	
	@Override
	protected void setDefaultValues()
	{
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("LOCAL_ALBUM")
			.title(getString(R.string.local_album))
			.extraDir(null)
			.build();
		
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			//.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(10))
			.saveImageOptions(mSaveOptions)
			.build();
		
		mImageAdapter = new ImageAdapter();
		((GridView) gridView).setAdapter(mImageAdapter);
		
		progressDialog = DialogUtils.createLoadingDialog(mContext, getString(R.string.hint_loading));
		progressDialog.show();
		getSubHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);
	}
	
	Dialog progressDialog;
	
	@Override
	protected void bindEvents()
	{
		gridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				startImagePagerActivity(position);
			}
		});
	}

	private void startImagePagerActivity(int position)
	{
		PhotoInfoSet photoInfoSet = new PhotoInfoSet();
		photoInfoSet.setAlbumid("local_album");
		photoInfoSet.setAlbumname(mSaveOptions.getTitle());
		photoInfoSet.setData(mPhotoInfos);
		photoInfoSet.setStartIndex(0);
		photoInfoSet.setTotal(mPhotoInfos.size());
		
		RockyIntentUtils.gotoImageViewerActivity(this, photoInfoSet, position, mSaveOptions.getExtraDir());
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_PHOTOS:
			{
				if (progressDialog.isShowing())
				{
					progressDialog.dismiss();
				}
				
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
				LocalImageFactory factory = LocalImageFactory.getInstance(getApplicationContext());
				List<LocalImage> albums = factory.queryImagesByAlbumId(mAlbumInfo.getAlbumid());

				PhotoInfo photoInfo = null;
				LocalImage album = null;

				int size = albums.size();
				for (int i = 0; i < size; i++)
				{
					album = albums.get(i);

					photoInfo = new PhotoInfo();
					photoInfo.setName(album.getTitle());
					photoInfo.setSmallUrl("file:///" + album.getData());
					photoInfo.setLargeUrl("file:///" + album.getData());

					mPhotoInfos.add(photoInfo);
				}

				getUiHandler().sendEmptyMessage(MSG_LOAD_PHOTOS);

				break;
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
			TextView desc = ViewUtils.get(convertView, R.id.desc);
			LinearLayout desc_line = ViewUtils.get(convertView, R.id.desc_line);
			
			mImageLoader.displayImage(getItem(position).getSmallUrl(), imageView, mOptions);
			
			return convertView;
		}
	}
	
}