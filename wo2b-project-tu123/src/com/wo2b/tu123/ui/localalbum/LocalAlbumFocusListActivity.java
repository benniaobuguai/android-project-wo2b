package com.wo2b.tu123.ui.localalbum;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.BusinessData;
import com.wo2b.tu123.business.localalbum.LocalImageFactory;
import com.wo2b.tu123.model.localalbum.FocusItemInfo;
import com.wo2b.tu123.ui.fileexplorer.FileExplorerActivity;
import com.wo2b.tu123.ui.global.RockyIntent;

/**
 * 关注列表
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class LocalAlbumFocusListActivity extends BaseFragmentActivity
{
	
	private ListView listView;
	
	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	
	private FocusItemListAdapter mFocusItemListAdapter = null;
	List<FocusItemInfo> mFocusItemList = new ArrayList<FocusItemInfo>();
	private int mUserCount = 0;
	
	private static final int RC_FILE_EXPLORER = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local_album_focus_list);
		
		initView();
		setDefaultValues();
		bindEvents();
	}
	
	@Override
	protected void initView()
	{
		final String title = getString(R.string.local_focus_album, mUserCount, BusinessData.MAX_LOCAL_FOCUS_COUNT);
		getSupportActionBar().setTitle(title);
		
		listView = (ListView) findViewById(R.id.listview);
		mFocusItemListAdapter = new FocusItemListAdapter();
		listView.setAdapter(mFocusItemListAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_add, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActionBarAddClick()
	{
		if (mUserCount >= BusinessData.MAX_LOCAL_FOCUS_COUNT)
		{
			// 花心已经到了极限
			showToast(R.string.hint_not_support_more_focus);
			return;
		}

		gotoFileExplorerActivity();
	}
	
	@Override
	protected void setDefaultValues()
	{
		mImageLoader = ImageLoader.getInstance();
		
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			//.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(6))
			.build();
		
		new PluginAsyncTask().execute();
	}

	/**
	 * 更新标题
	 * 
	 * @param systemCount
	 * @param userCount
	 */
	private void refreshTitle(int userCount)
	{
		final String title = getString(R.string.local_focus_album, userCount, BusinessData.MAX_LOCAL_FOCUS_COUNT);
		getSupportActionBar().setTitle(title);
		//xTitleBar.setRightEnabled(true);
	}
	
	private void refreshFocusItem()
	{
		// 更新标题显示
		mUserCount = mFocusItemList.size();
		refreshTitle(mUserCount);

		mFocusItemListAdapter.notifyDataSetChanged();
		listView.setAdapter(mFocusItemListAdapter);
	}

	@Override
	protected void bindEvents()
	{
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				
			}
		});
	}
	
	/**
	 * Go to file explorer activity.
	 */
	private void gotoFileExplorerActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, FileExplorerActivity.class);
		startActivityForResult(intent, RC_FILE_EXPLORER);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			if (requestCode == RC_FILE_EXPLORER)
			{
				final String directory = data.getStringExtra(RockyIntent.EXTRA_DIRECTORY);
				final String title = data.getStringExtra(RockyIntent.EXTRA_TITLE);
				
				FocusItemInfo itemInfo = new FocusItemInfo();
				itemInfo.setSystem(false);
				itemInfo.setBucket_display_name(title);
				itemInfo.setBeautiful_name(title);
				itemInfo.setData(directory);
				itemInfo.setIcon("");
				// mFocusItemList.add(0, itemInfo);
				
				// 添加至内存中
				LocalImageFactory.getInstance(getApplicationContext()).addFocusItem(itemInfo);
				new PluginAsyncTask().execute();
				
				return;
			}
		}
	}
	
	private class PluginAsyncTask extends AsyncTask<Void, Void, List<FocusItemInfo>>
	{
		
		@Override
		protected List<FocusItemInfo> doInBackground(Void... params)
		{
			List<FocusItemInfo> focusItemList = new ArrayList<FocusItemInfo>();
			focusItemList.addAll(LocalImageFactory.getInstance(getApplicationContext()).getFocusItemList());
			
			return focusItemList;
		}
		
		@Override
		protected void onPostExecute(List<FocusItemInfo> focusItemList)
		{
			super.onPostExecute(focusItemList);
			if (focusItemList == null)
			{
				focusItemList = new ArrayList<FocusItemInfo>();
			}
			
			mFocusItemList = focusItemList;
			
			// Refresh focus item list.
			refreshFocusItem();
		}

	}
	
	public class FocusItemListAdapter extends BaseAdapter
	{
		
//		private List<FocusItemInfo> mFocusItemInfoList;
		
//		public FocusItemListAdapter(List<FocusItemInfo> focusItemList)
//		{
//			this.mFocusItemInfoList = focusItemList;
//		}
		
		@Override
		public int getCount()
		{
			return mFocusItemList.size();
		}
		
		@Override
		public FocusItemInfo getItem(int position)
		{
			return mFocusItemList.get(position);
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.local_album_focus_list_item, parent,
						false);
			}
			ImageView image = ViewUtils.get(convertView, R.id.image);
			TextView name = ViewUtils.get(convertView, R.id.name);
			TextView supported_by = ViewUtils.get(convertView, R.id.supported_by);
			TextView data = ViewUtils.get(convertView, R.id.data);
			TextView operation = ViewUtils.get(convertView, R.id.operation);
			
			FocusItemInfo focusItem = getItem(position);
			name.setText(focusItem.getBeautiful_name());
			data.setText(focusItem.getData());
			operation.setOnClickListener(new OnKOClickListener(focusItem, position));
			
			if (focusItem.isSystem())
			{
				supported_by.setText(R.string.supported_by_system);
			}
			else
			{
				supported_by.setText(R.string.supported_by_user);
			}
			
			if (TextUtils.isEmpty(focusItem.getIcon()))
			{
				focusItem.setIcon("assets://album_focus_icons/user_images.png");
			}
			mImageLoader.displayImage(focusItem.getIcon(), image, mOptions);
			
			return convertView;
		}
		
		private class OnKOClickListener implements OnClickListener
		{
			
			private FocusItemInfo mFocusItemInfo;
			private int mPosition;
			
			public OnKOClickListener(FocusItemInfo itemInfo, int position)
			{
				this.mFocusItemInfo = itemInfo;
				this.mPosition = position;
			}
			
			@Override
			public void onClick(View v)
			{
				if (LocalImageFactory.getInstance(getApplicationContext()).removeFocusItem(mFocusItemInfo))
				{
					mFocusItemList.remove(mPosition);
					// 更新UI
					notifyDataSetChanged();
					if (!mFocusItemInfo.isSystem())
					{
						// 减少1
						refreshTitle(--mUserCount);
					}

					showToast(R.string.hint_local_remove_ok);
				}
			}
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
	
}
