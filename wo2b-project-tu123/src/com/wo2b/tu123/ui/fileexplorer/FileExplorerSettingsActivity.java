package com.wo2b.tu123.ui.fileexplorer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.wo2b.tu123.R;

/**
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class FileExplorerSettingsActivity extends PreferenceActivity implements OnPreferenceClickListener
{
	public void onCreate(Bundle saved_instance_state)
	{
		super.onCreate(saved_instance_state);
		addPreferencesFromResource(R.xml.preferences);
		findPreference("root_folder").setOnPreferenceClickListener(this);
		
		setResult(Activity.RESULT_OK);
	}
	
	public void onDestroy()
	{
		super.onDestroy();
	}
	
	public boolean onPreferenceClick(Preference preference)
	{
		Intent intent = new Intent(this, SetRootFolderActivity.class);
		intent.setAction("select_folder");
		intent.putExtra("root_folder", PreferenceManager.getDefaultSharedPreferences(this).getString("root_folder", ""));
		
		startActivityForResult(intent, 1);
		
		return true;
	}
	
	public void onActivityResult(int request_code, int result_code, Intent intent)
	{
		if (request_code == 1)
		{
			if (result_code == Activity.RESULT_OK)
			{
				String root_folder = intent.getStringExtra("root_folder");
				SharedPreferences.Editor preferences_editor = PreferenceManager.getDefaultSharedPreferences(this)
						.edit();
				preferences_editor.putString("root_folder", root_folder);
				preferences_editor.commit();
			}
		}
	}
}
