package com.wo2b.tu123.ui.settings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.io.FileUtils;
import com.wo2b.wrapper.app.RockyFragmentActivity;
import com.wo2b.wrapper.component.security.SecurityTu123;
import com.wo2b.wrapper.view.dialog.DialogText;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.AppCacheFactory;
import com.wo2b.tu123.model.storage.FileInfo;

/**
 * StorageManagerActivity.
 * 
 * @author Rocky
 * 
 */
public class StorageManagerActivity extends RockyFragmentActivity
{

	private ListView listview;

	private FileListAdapter mAdapter;
	private List<FileInfo> mFileInfos = new ArrayList<FileInfo>();
	
	/** 缓存文件总大小 */
	private long mTotalLength = 0;
	/** 缓存文件总数量 */
	private int mTotalCount = 0;
	
	private static final int MSG_LOAD_FILE = 1;
	private static final int MSG_DELETE_FILE = 2;
	private static final int MSG_DELETE_FILE_OK = 3;
	private static final int MSG_DELETE_FILE_FAIL = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_setting_storage_mgr);
		initView();
		bindEvents();
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.storage_manager);
		listview = (ListView) findViewById(R.id.listview);
		mAdapter = new FileListAdapter(new ArrayList<FileInfo>());
		listview.setAdapter(mAdapter);
		
		getSubHandler().sendEmptyMessage(MSG_LOAD_FILE);
	}
	
	@Override
	protected void bindEvents()
	{
		listview.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
			{
				final FileInfo fileInfo = (FileInfo) parent.getItemAtPosition(position);

				DialogText dialog = new DialogText(mContext);
				dialog.setMessage(getString(R.string.hint_confirm_delete, fileInfo.getName()));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setPositiveButtonListener(R.string.ok, new AlertDialog.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						Message msg = Message.obtain(getSubHandler(), MSG_DELETE_FILE);
						msg.arg1 = position;
						msg.obj = fileInfo;
						getSubHandler().sendMessage(msg);

						dialog.dismiss();
					}
				});
				dialog.setNegativeButtonListener(R.string.no, new AlertDialog.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				dialog.show();
			}
		});
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_FILE:
			{
				mAdapter.notifyDataSetChanged(mFileInfos);
				return true;
			}
			case MSG_DELETE_FILE_OK:
			{
				int position = msg.arg1;
				mFileInfos.remove(position);
				mAdapter.notifyDataSetChanged(mFileInfos);
				mTotalCount--;
				
				FileInfo fileInfo = (FileInfo) msg.obj;
				showToast(getString(R.string.delete_ok, fileInfo.getName()));
				return true;
			}
			case MSG_DELETE_FILE_FAIL:
			{
				FileInfo fileInfo = (FileInfo) msg.obj;
				showToast(getString(R.string.delete_fail, fileInfo.getName()));
				return true;
			}
		}

		return super.uiHandlerCallback(msg);
	}

	@SuppressLint("NewApi")
	@Override
	protected boolean subHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD_FILE:
			{
				List<File> folders = FileUtils.getFolderList(new AppCacheFactory().getAppImageDir());

				if (folders != null && !folders.isEmpty())
				{
					final int folderCount = folders.size();
					int fileCount = 0;
					int totalLength = 0;

					File folder = null;
					FileInfo info = null;
					int[] array = null;
					String folderName = null;
					for (int i = 0; i < folderCount; i++)
					{
						info = new FileInfo();
						folder = folders.get(i);
						
						folderName = SecurityTu123.decodeText(folder.getName());
						
						info.setName(folderName);
						info.setSize(FileUtils.getFolderSize(folder));
						info.setDisplaySize(FileUtils.formatByte(info.getSize()));
						info.setPath(folder.getPath());
						info.setLastModified(folder.lastModified());
						
						if (VERSION.SDK_INT > 8)
						{
							info.setTotalSpace(folder.getTotalSpace());
							info.setFreeSpace(folder.getFreeSpace());
							info.setUsableSpace(folder.getUsableSpace());
						}
						
						
						array = FileUtils.fileAndFolderCount(folder.getPath());
						info.setFileCount(array[0]);
						info.setFolderCount(array[1]);

						fileCount += array[0];
						totalLength += info.getSize();
						
						mFileInfos.add(info);
					}

					mTotalCount = fileCount;
					mTotalLength = totalLength;
				}

				getUiHandler().sendEmptyMessage(MSG_LOAD_FILE);
				return true;
			}
			case MSG_DELETE_FILE:
			{
				int position = msg.arg1;
				FileInfo fileInfo = (FileInfo) msg.obj;
				// execute delete cmd.
				File file = new File(fileInfo.getPath());
				boolean isOk = false;
				if (file.isDirectory())
				{
					isOk = FileUtils.deleteDirectory(file);
				}
				
				if (isOk)
				{
					Message newMsg = getUiHandler().obtainMessage(MSG_DELETE_FILE_OK);
					newMsg.obj = fileInfo;
					newMsg.arg1 = position;

					newMsg.sendToTarget();
				}
				else
				{
					Message newMsg = getUiHandler().obtainMessage(MSG_DELETE_FILE_FAIL);
					newMsg.obj = fileInfo;
					newMsg.arg1 = position;

					newMsg.sendToTarget();
				}
				
				return true;
			}
		}

		return super.subHandlerCallback(msg);
	}
	
	public class FileListAdapter extends BaseAdapter
	{

		private List<FileInfo> mFileInfos;
		
		public FileListAdapter(List<FileInfo> fileInfos)
		{
			this.mFileInfos = fileInfos;
		}
		
		public void notifyDataSetChanged(List<FileInfo> fileInfos)
		{
			this.mFileInfos = fileInfos;
			notifyDataSetChanged();
		}

		@Override
		public int getCount()
		{
			return mFileInfos.size();
		}

		@Override
		public FileInfo getItem(int position)
		{
			return mFileInfos.get(position);
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewUtils holder = null;
			if (convertView == null)
			{
				holder = new ViewUtils();
				convertView = LayoutInflater.from(mContext).inflate(R.layout.uc_setting_storage_list_item, parent, false);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				holder.picnum = (TextView) convertView.findViewById(R.id.picnum);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewUtils) convertView.getTag();
			}

			FileInfo info = getItem(position);

			holder.name.setText(info.getName());
			holder.desc.setText(getString(R.string.size_1, info.getDisplaySize()));
			holder.picnum.setText(getString(R.string.file_count_1, info.getFileCount()));

			//imageLoader.displayImage(album.getCoverurl(), holder.image, options);

			return convertView;
		}

		class ViewUtils
		{
			ImageView image;
			TextView name;
			TextView desc;
			TextView picnum;
		}
	}

}
