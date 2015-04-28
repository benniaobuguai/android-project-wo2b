package com.wo2b.tu123.ui.localalbum;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.view.pulltorefresh.PullToRefreshGridView;
import com.wo2b.wrapper.view.dialog.DialogUtils;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.localalbum.LocalImageFactory;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.model.localalbum.LocalImage;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.image.AbsListViewBaseActivity;

/**
 * Local album.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-12
 */
public class LocalAlbumActivity extends AbsListViewBaseActivity
{

	private ImageAdapter mImageAdapter;
	private List<AlbumInfo> mAlbumInfos = new ArrayList<AlbumInfo>();

	private static final int MSG_LOAD_PHOTOS = 1;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_album_grid_list);
		initView();
		setDefaultValues();
		bindEvents();
	}

	@Override
	protected void initView()
	{
		getSupportActionBar().setTitle(R.string.local_album);

		pullToRefreshGridView = (PullToRefreshGridView) findViewById(R.id.pullToRefreshGridView);
		gridView = pullToRefreshGridView.getRefreshableView();
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
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_edit, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.edit:
			{
				Intent intent = new Intent();
				intent.setClass(getContext(), LocalAlbumFocusListActivity.class);
				startActivity(intent);
				return true;
			}
		}

		return super.onOptionsItemSelected(item);
	}

	Dialog progressDialog;

	@Override
	public void onResume()
	{
		super.onResume();

		progressDialog.show();
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
				AlbumInfo albumInfo = (AlbumInfo) parent.getItemAtPosition(position);
				
				Intent intent = new Intent(getContext(), LocalImageGridActivity.class);
				intent.putExtra(RockyIntent.EXTRA_ALBUM, albumInfo);
				startActivity(intent);
			}
		});
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
				mAlbumInfos.clear();
				
				LocalImageFactory factory = LocalImageFactory.getInstance(getApplicationContext());
				List<LocalImage> albums = factory.getFocusList();

				AlbumInfo albumInfo = null;
				LocalImage localImage = null;

				int size = albums.size();
				for (int i = 0; i < size; i++)
				{
					localImage = albums.get(i);

					albumInfo = new AlbumInfo();
					albumInfo.setAlbumid(localImage.getBucketId());
					albumInfo.setPicnum(localImage.getImageCount());
					albumInfo.setName(localImage.getBeautifulName());
					albumInfo.setCoverurl("file:///" + localImage.getData());

					mAlbumInfos.add(albumInfo);
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
			return mAlbumInfos.size();
		}
		
		@Override
		public AlbumInfo getItem(int position)
		{
			return mAlbumInfos.get(position);
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
				convertView = getLayoutInflater().inflate(R.layout.local_album_grid_list_item, parent, false);
			}
			ImageView image = ViewUtils.get(convertView, R.id.image);
			ImageView background = ViewUtils.get(convertView, R.id.background);
			TextView name_number = ViewUtils.get(convertView, R.id.name_number);
			
			AlbumInfo albumInfo = getItem(position);
			name_number.setText(getString(R.string.name_number, albumInfo.getName(), albumInfo.getPicnum()));
			background.setBackgroundResource(IMAGE_BACKGROUND[position % IMAGE_BACKGROUND.length]);
			mImageLoader.displayImage(albumInfo.getCoverurl(), image, mOptions);

			return convertView;
		}
		
		private final int[] IMAGE_BACKGROUND = new int[] 
		{ 
			R.drawable.gallery_album_frame,
			R.drawable.gallery_album_frame1,
			R.drawable.gallery_album_frame2
		};
	}
	
}