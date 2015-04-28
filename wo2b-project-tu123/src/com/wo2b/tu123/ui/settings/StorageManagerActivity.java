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
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.common.util.io.FileUtils;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.AppCacheFactory;
import com.wo2b.tu123.model.storage.FileInfo;
import com.wo2b.wrapper.app.BaseFragmentListActivity;
import com.wo2b.wrapper.component.security.SecurityTu123;
import com.wo2b.wrapper.view.dialog.DialogText;

/**
 * 图片缓存管理
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 2.0.0
 * @date 2015-4-11
 */
public class StorageManagerActivity extends BaseFragmentListActivity<FileInfo>
{

	/** 缓存文件总大小 */
	private long mTotalLength = 0;
	/** 缓存文件总数量 */
	private int mTotalCount = 0;

	private static final int MSG_DELETE_FILE = 2;
	private static final int MSG_DELETE_FILE_OK = 3;
	private static final int MSG_DELETE_FILE_FAIL = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uc_setting_storage_mgr);
		initView();

		realExecuteFirstTime(null);
	}

	@Override
	protected void initView()
	{
		setActionBarTitle(R.string.storage_manager);
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_DELETE_FILE_OK:
			{
				int position = msg.arg1;
				realRemoveItem(position);

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

	@Override
	protected void realOnItemClick(final AdapterView<?> parent, View view, final int position, final long id)
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
	
	@SuppressLint("NewApi")
	@Override
	protected BaseFragmentListActivity.XModel<FileInfo> realOnPullDown(BaseFragmentListActivity.RockyParams params)
	{
		List<FileInfo> fileInfos = new ArrayList<FileInfo>();
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

				fileInfos.add(info);
			}

			mTotalCount = fileCount;
			mTotalLength = totalLength;
		}

		return XModel.list(fileInfos);
	}

	@Override
	protected BaseFragmentListActivity.XModel<FileInfo> realOnPullUp(BaseFragmentListActivity.RockyParams params)
	{
		return null;
	}

	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.uc_setting_storage_list_item, parent, false);
		}

		ImageView image = ViewUtils.get(convertView, R.id.image);
		TextView nameTV = (TextView) convertView.findViewById(R.id.name);
		TextView descTV = (TextView) convertView.findViewById(R.id.desc);
		TextView picnumTV = (TextView) convertView.findViewById(R.id.picnum);

		FileInfo info = realGetItem(position);

		nameTV.setText(info.getName());
		descTV.setText(getString(R.string.size_1, info.getDisplaySize()));
		picnumTV.setText(getString(R.string.file_count_1, info.getFileCount()));

		return convertView;
	}

}
