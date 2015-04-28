package com.wo2b.tu123.ui.fileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.localalbum.LocalImageFactory;
import com.wo2b.tu123.model.localalbum.FocusItemInfo;
import com.wo2b.tu123.ui.global.RockyIntent;

/**
 * 文件浏览器
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class FileExplorerActivity extends BaseFragmentActivity implements OnItemClickListener
{
	
	private static final String TAG = "FileExplorerActivity";
	
	//private static final String ROOT_DIRECTORY = "/mnt/sdcard";
	
	private static final String ITEM_KEY = "key";
	private static final String ITEM_IMAGE = "image";
	private static final String ITEM_SELECTABLE = "selectable";
	
	private static final int MSG_GET_FILE_LIST = 1;
	private static final int RC_SET_FILE_ROOT = 1;

	private class FileNamesComparatorFoldersUp implements Comparator<File>
	{
		public int compare(File left, File right)
		{
			if (left.isDirectory())
			{
				if (right.isDirectory())
				{
					return left.compareTo(right);
				}

				return -1;
			}

			return right.isDirectory() ? 1 : left.compareTo(right);
		}
	}
	
	private class FileNamesComparatorFoldersNotUp implements Comparator<File>
	{
		public int compare(File left, File right)
		{
			return left.compareTo(right);
		}
	}

	private class GetFilesThread extends Thread
	{		
		public void run()
		{
			int result = _showDirectoryContents();
			
			Message message = new Message();
			message.what = MSG_GET_FILE_LIST;
			message.arg1 = result;
			
			getUiHandler().sendMessage(message);
		}
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		if (msg.what == MSG_GET_FILE_LIST)
		{
			if (msg.arg1 == 0)
			{
				showDirectoryContentsUI();
				thread = null;
			}
			else if (msg.arg1 == 1)
			{
				Log.d(TAG, "Folder is empty: " + loadingPathname);
				Toast.makeText(getBaseContext(), R.string.folder_is_empty, Toast.LENGTH_LONG).show();
				thread = null;
			}
			else if (msg.arg1 == 2)
			{
				Log.d(TAG, "Folder isn't readable: " + loadingPathname);
				Toast.makeText(getBaseContext(), R.string.folder_isnt_readable, Toast.LENGTH_LONG).show();
				thread = null;
			}
		}
		
		return super.uiHandlerCallback(msg);
	}
	
	protected String sdcard_root_path;
	protected String currentPath;

	private boolean initialized;
	
	private TextView folderNameText;
	private ListView listView;
	
	protected boolean allowMenuKey;
	protected boolean showPlainFiles;
	
	private ArrayList<HashMap<String, Object>> filesList;
	
	private BaseAdapter adapterList;
	
	private FileNamesComparatorFoldersUp comparatorFoldersUp;
	private FileNamesComparatorFoldersNotUp comparatorFoldersNotUp;
	private boolean foldersUp;
		
	private Thread thread;
	
	private String loadingPathname;
	private List<File> iChilds;

	private HashMap<String, Integer> selectionMap; // 记录上级目录的索引位置
	
	@Override
	public void onCreate(Bundle saved_instance_state) 
	{
		super.onCreate(saved_instance_state);
		setContentView(R.layout.rocky_file_explorer);
		
		getSupportActionBar().setTitle(R.string.file_explorer);
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			File sdcard_dir = Environment.getExternalStorageDirectory();
			sdcard_root_path = sdcard_dir.getPath();
		}
		
		
		//iCurrentPath = PreferenceManager.getDefaultSharedPreferences(this).getString("root_folder", ROOT_DIRECTORY);
		currentPath = sdcard_root_path;

		thread = null;
		
		iChilds = new ArrayList<File>();
		
		initialized = false;
		
		// 不支持Menu键进入设置
		allowMenuKey = false;
		showPlainFiles = true;
		
		folderNameText = (TextView) findViewById(R.id.folder_name);
		
		listView = (ListView) findViewById (R.id.files_listview);
		listView.setOnItemClickListener(this);
		
		filesList = new ArrayList<HashMap<String, Object>>();
		
		comparatorFoldersUp = new FileNamesComparatorFoldersUp();
		comparatorFoldersNotUp = new FileNamesComparatorFoldersNotUp();
		
		adapterList = new FileExplorerAdapter();
		
		// 根目录的选中位置为0
		selectionMap = new HashMap<String, Integer>();
		selectionMap.put(sdcard_root_path, 0);
	}

	protected class FileExplorerAdapter extends BaseAdapter
	{
		
		@Override
		public int getCount()
		{
			return filesList.size();
		}
		
		@Override
		public HashMap<String, Object> getItem(int position)
		{
			return filesList.get(position);
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
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.rocky_file_list_item, null);
			}
			
			TextView tvFilename = ViewUtils.get(convertView, R.id.filename);
			ImageView ivIcon = ViewUtils.get(convertView, R.id.icon);
			LinearLayout operate_area = ViewUtils.get(convertView, R.id.operate_area);
			final TextView tvSelect = ViewUtils.get(convertView, R.id.select);
			
			final HashMap<String, Object> fileInfo = getItem(position);
			final String filename = fileInfo.get(ITEM_KEY).toString();
			final int drawableId = Integer.parseInt(fileInfo.get(ITEM_IMAGE).toString());
			final boolean selectalbe = (Boolean) fileInfo.get(ITEM_SELECTABLE);
			tvSelect.setBackgroundResource(R.drawable.checkbox_round_normal);
			
			tvFilename.setText(filename);
			ivIcon.setImageResource(drawableId);
			
			if (selectalbe)
			{
				operate_area.setVisibility(View.VISIBLE);
				operate_area.setOnClickListener(new OnClickListener()
				{
					
					@Override
					public void onClick(View v)
					{
						tvSelect.setBackgroundResource(R.drawable.checkbox_round_checked);
						
						String directory = currentPath + "/" + filename;
						
						// 需要对根路径进行截取, 只保存我们关注的关键路径.
						int length = sdcard_root_path.length();
						String focus_path = directory.substring(length);
						
						FocusItemInfo itemInfo = new FocusItemInfo();
						itemInfo.setData(focus_path);
						if (!LocalImageFactory.getInstance(getApplicationContext()).isFocusItemExists(itemInfo))
						{
							onDirectorySelected(focus_path, filename);
							
							finish();
						}
						else
						{
							showToast(R.string.hint_exists);
							
							tvSelect.setBackgroundResource(R.drawable.checkbox_round_normal);
						}
					}
				});
			}
			else
			{
				operate_area.setVisibility(View.GONE);
			}
			
			return convertView;
		}
		
	}
	
	/**
	 * 选择目录
	 * 
	 * @param directory
	 * @param title
	 */
	private void onDirectorySelected(String directory, String title)
	{
		Intent intent = new Intent();
		intent.putExtra(RockyIntent.EXTRA_DIRECTORY, directory);
		intent.putExtra(RockyIntent.EXTRA_TITLE, title);
		setResult(RESULT_OK, intent);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (!initialized)
		{
			initialized = true;
			showDirectoryContents(currentPath);
		}
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}

	/**
	 * 返回父级目录
	 * 
	 * @param pathname
	 * @return
	 */
	private String getParent(String pathname)
	{
		int index = pathname.lastIndexOf("/");
		if (index <= 0)
		{
			return "";
		}
		
		return pathname.substring(0, index);
	}
	
	private HashMap<String, Object> createListViewItem(String name, int image, boolean selectable)
	{
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_KEY, name);
		item.put(ITEM_IMAGE, image);
		item.put(ITEM_SELECTABLE, selectable);
		
		return item;
	}

	private void showDirectoryContentsUI()
	{
		currentPath = loadingPathname;
		folderNameText.setText(currentPath + "/");
		
		// File list should clear.
		filesList.clear();
		
		if (!currentPath.equals(""))
		{
			if (!sdcard_root_path.equalsIgnoreCase(currentPath))
			{
				// FIXME: 暂时仅提供支持选择SDCard的内容, 后期进行修复此bug时,
				// 直接去掉当前的!sdcard_root_path.equalsIgnoreCase(currentPath)即可.
				filesList.add(createListViewItem(getResources().getString(R.string.folder_up), R.drawable.folder_up,
						false));
			}
		}

		for (File child : iChilds)
		{
			if (child.isDirectory())
			{
				// Folder cant be selected.
				filesList.add(createListViewItem(child.getName(), R.drawable.folder, true));
			}
			else
			{
				filesList.add(createListViewItem(child.getName(), R.drawable.file_default, false));
			}
		}
		
		adapterList.notifyDataSetChanged();
		listView.setAdapter(adapterList);
		
		// 判断是否已经记录了访问的位置信息
		if (selectionMap.containsKey(currentPath))
		{
			int selection = selectionMap.get(currentPath);

			listView.setSelection(selection);
		}
		
	}
		
	private int _showDirectoryContents()
	{
		File folder = new File(loadingPathname + "/");
		File[] childs = folder.listFiles();
		
		if ((childs == null) || (childs.length == 0))
		{
			if (folder.canRead())
			{
				return 1;
			}
			else
			{
				return 2;
			}
			
		}
		else
		{
			for (File child : childs)
			{
				if ((showPlainFiles) || (child.isDirectory()))
					iChilds.add(child);
			}
			
			if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("list_folders_first", true))
			{
				Collections.sort(iChilds, comparatorFoldersUp);
				foldersUp = true;
			}
			else
			{
				Collections.sort(iChilds, comparatorFoldersNotUp);
				foldersUp = false;
			}
			
			return 0;
		}
	}
	
	/**
	 * 显示
	 * 
	 * @param pathname
	 */
	private synchronized void showDirectoryContents(String pathname)
	{
		if (thread == null)
		{
			loadingPathname = pathname;
			iChilds.clear();
			thread = new GetFilesThread();
			thread.start();
		}
	}
	
	/**
	 * 是否是一个文件夹
	 * 
	 * @param pathname
	 * @return
	 */
	public boolean isFolder(String pathname)
	{
		File file = new File(pathname);
		return file.isDirectory();
	}
	
	/**
	 * 列表单击
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
	{
		String filename = null;
		
		Log.i(TAG, String.format("Pulsado elemento en la posición %d", position));
		
		if (position == 0)
		{
			if (currentPath.equals(""))
			{
				filename = String.format("%s/%s", currentPath, ((HashMap<String, Object>) filesList.get(position))
						.get(ITEM_KEY).toString());
			}
			else
			{
				// filename = getParent(currentPath);

				// 加入了迭代的返回保存上一级所处于的位置.
				onReturnPrevLayer(currentPath);
				return;
			}
		}
		else
		{
			filename = String.format("%s/%s", currentPath,
					((HashMap<String, Object>) filesList.get(position)).get(ITEM_KEY).toString());
			if (!isFolder(filename))
			{
				Toast.makeText(this, R.string.is_not_a_folder, Toast.LENGTH_LONG).show();
				return;
			}
		}
		
		showDirectoryContents(filename);
		
		
		//final String parentPath = filename.substring(0, filename.lastIndexOf("/"));
		final String parentPath = getParent(filename);
		
		// 保存位置信息
		if (selectionMap.containsKey(parentPath))
		{
			selectionMap.remove(parentPath);
			selectionMap.put(parentPath, position);
		}
		else
		{
			selectionMap.put(parentPath, position);
		}
	}
	
	/**
	 * 返回上一层
	 */
	private void onReturnPrevLayer(final String currentPath)
	{
		showDirectoryContents(getParent(currentPath));

		if (selectionMap.containsKey(currentPath))
		{
			selectionMap.remove(currentPath);
		}
	}
	
	/**
	 * 拦截Menu/KeyBack事件
	 */
	@Override
	public boolean onKeyDown(int key_code, KeyEvent event)
	{
		if (key_code == KeyEvent.KEYCODE_BACK)
		{

			if (currentPath.equalsIgnoreCase(sdcard_root_path))
			{
				return super.onKeyDown(key_code, event);
			}
			else if (!currentPath.equals(""))
			{
				// showDirectoryContents(getParent(currentPath));
				onReturnPrevLayer(currentPath);
			}

			return true;
		}
		else if ((key_code == KeyEvent.KEYCODE_MENU) && (allowMenuKey))
		{
			Intent intent = new Intent(this, FileExplorerSettingsActivity.class);
			startActivityForResult(intent, RC_SET_FILE_ROOT);
			return true;
		}
		else
		{
			return super.onKeyDown(key_code, event);
		}
	}
	
	/**
	 * 设置根目录回调
	 */
	@Override
	public void onActivityResult(int request_code, int result_code, Intent intent)
	{
		if (request_code == RC_SET_FILE_ROOT)
		{
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			boolean value = preferences.getBoolean("list_folders_first", true);
			if (value != foldersUp)
			{
				showDirectoryContents(currentPath);
			}
		}
	}
	
}