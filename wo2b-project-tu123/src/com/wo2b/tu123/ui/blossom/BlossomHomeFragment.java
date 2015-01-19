package com.wo2b.tu123.ui.blossom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import opensource.component.pulltorefresh.PullToRefreshExpandableListView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.wrapper.app.fragment.RockyFragment;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.global.provider.DataProvider;
import com.wo2b.tu123.model.global.XGroup;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.image.ImageGridActivity;

/**
 * 百花齐放
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-12
 */
public class BlossomHomeFragment extends RockyFragment
{

	private PullToRefreshExpandableListView ptrExpandableListView;
	private ExpandableListView expandableListView;
	private ImageGroupListAdapter mAdapter;

	private List<XGroup> mGroupList = new ArrayList<XGroup>();
	private List<HashMap<String, List<AlbumInfo>>> mItemList = new ArrayList<HashMap<String, List<AlbumInfo>>>();

	private static final int MSG_LOAD = 1;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.blossom_main, container, false);
		initView(view);
		bindEvents();

		return view;
	}

	@Override
	protected void initView(View view)
	{
		ptrExpandableListView = (PullToRefreshExpandableListView) view.findViewById(R.id.ptrExpandableListView);
		expandableListView = ptrExpandableListView.getRefreshableView();
		mAdapter = new ImageGroupListAdapter(getActivity(), mGroupList, mItemList);
		expandableListView.setAdapter(mAdapter);

		if (mGroupList != null && !mGroupList.isEmpty())
		{
			notifyDataSetChanged(mGroupList, mItemList);
		}
		else
		{
			getSubHandler().sendEmptyMessage(MSG_LOAD);
		}
	}

	@Override
	protected void bindEvents()
	{
		expandableListView.setOnChildClickListener(new OnChildClickListener()
		{

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
			{
				AlbumInfo albumInfo = (AlbumInfo) parent.getExpandableListAdapter().getChild(groupPosition,
						childPosition);

				Intent intent = new Intent(getContext(), ImageGridActivity.class);
				intent.putExtra(RockyIntent.EXTRA_ALBUM, albumInfo);
				startActivity(intent);

				return false;
			}
		});
	}

	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD:
			{
				notifyDataSetChanged(mGroupList, mItemList);
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
			case MSG_LOAD:
			{
				DataProvider provider = DataProvider.getInstance();
				mGroupList = provider.getChapterList(getActivity());
				mItemList = provider.getChapterContentList();
				getUiHandler().sendEmptyMessage(MSG_LOAD);
				break;
			}
		}

		return super.subHandlerCallback(msg);
	}

	/**
	 * 更新UI
	 */
	public void notifyDataSetChanged(List<XGroup> groupList, List<HashMap<String, List<AlbumInfo>>> itemList)
	{
		if (mAdapter != null)
		{
			mAdapter.notifyDataSetChanged(mGroupList, mItemList);
			int groupCount = mAdapter.getGroupCount();
			for (int i = 0; i < groupCount; i++)
			{
				expandableListView.expandGroup(i);
			}
		}
	}
	
	private static class ImageGroupListAdapter extends BaseExpandableListAdapter
	{

		private ImageLoader mImageLoader;
		private DisplayImageOptions mOptions;
		private SaveImageOptions mSaveOptions;
		
		private Context mContext;
		private LayoutInflater mLayoutInflater;
		private List<XGroup> mGroupList;
		private List<HashMap<String, List<AlbumInfo>>> mItemList;

		public ImageGroupListAdapter(Context context, List<XGroup> groupList,
				List<HashMap<String, List<AlbumInfo>>> albumMapList)
		{
			this.mContext = context;
			this.mLayoutInflater = LayoutInflater.from(mContext);
			fillIn(groupList, albumMapList);
			
			mImageLoader = ImageLoader.getInstance();
			mSaveOptions = new SaveImageOptions.Builder()
				.medule("Baihua_Album_List")
				.extraDir(ExtraDir.ALBUM)
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
		}

		/**
		 * 数据发生变化时, 更新UI
		 * 
		 * @param groupList
		 * @param itemList
		 */
		public void notifyDataSetChanged(List<XGroup> groupList, List<HashMap<String, List<AlbumInfo>>> itemList)
		{
			fillIn(groupList, itemList);
		}

		/**
		 * 填充数据
		 * 
		 * @param groupList
		 * @param itemList
		 */
		private void fillIn(List<XGroup> groupList, List<HashMap<String, List<AlbumInfo>>> itemList)
		{
			if (groupList == null)
			{
				groupList = new ArrayList<XGroup>();
			}
			if (itemList == null)
			{
				itemList = new ArrayList<HashMap<String, List<AlbumInfo>>>();
			}

			this.mGroupList = groupList;
			this.mItemList = itemList;
		}

		@Override
		public int getGroupCount()
		{
			return mGroupList.size();
		}

		@Override
		public int getChildrenCount(int groupPosition)
		{
			HashMap<String, List<AlbumInfo>> hashMap = mItemList.get(groupPosition);
			String groupName = mGroupList.get(groupPosition).getGroupName();

			return hashMap.get(groupName).size();
		}

		@Override
		public XGroup getGroup(int groupPosition)
		{
			return mGroupList.get(groupPosition);
		}

		@Override
		public AlbumInfo getChild(int groupPosition, int childPosition)
		{
			HashMap<String, List<AlbumInfo>> hashMap = mItemList.get(groupPosition);
			String groupName = mGroupList.get(groupPosition).getGroupName();

			return hashMap.get(groupName).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition)
		{
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition)
		{
			return childPosition;
		}

		@Override
		public boolean hasStableIds()
		{
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		{
			GroupHolder holder = null;
			if (convertView == null)
			{
				holder = new GroupHolder();
				convertView = mLayoutInflater.inflate(R.layout.blossom_album_list_group_item, parent, false);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(holder);
			}
			else
			{
				holder = (GroupHolder) convertView.getTag();
			}
			XGroup album = getGroup(groupPosition);
			holder.name.setText(album.getGroupName());

			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent)
		{
			ChildHolder holder = null;
			if (convertView == null)
			{
				holder = new ChildHolder();
				convertView = mLayoutInflater.inflate(R.layout.blossom_album_list_item, parent, false);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				holder.picnum = (TextView) convertView.findViewById(R.id.picnum);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ChildHolder) convertView.getTag();
			}

			AlbumInfo album = getChild(groupPosition, childPosition);
			holder.name.setText(album.getName());
			holder.desc.setText(album.getDesc());
			holder.picnum.setText(album.getPicnum() + "");

			mImageLoader.displayImage(album.getCoverurl(), holder.image, mOptions);

			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition)
		{
			return true;
		}

		class GroupHolder
		{

			TextView name;
			TextView desc;
		}

		class ChildHolder
		{

			ImageView image;
			TextView name;
			TextView desc;
			TextView picnum;
		}
	}

}
