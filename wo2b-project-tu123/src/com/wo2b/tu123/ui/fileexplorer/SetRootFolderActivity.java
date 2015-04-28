package com.wo2b.tu123.ui.fileexplorer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.wo2b.tu123.R;

/**
 * 设置根目录
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class SetRootFolderActivity extends FileExplorerActivity
{

	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);

		allowMenuKey = false;
		showPlainFiles = false;
		if (getIntent().getAction().equals("select_folder"))
		{
			currentPath = getIntent().getStringExtra("root_folder");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.common_ok, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActionBarOkClick()
	{
		Intent intent = new Intent();
		intent.putExtra("root_folder", currentPath);

		setResult(Activity.RESULT_OK, intent);
		finish();
	}

}