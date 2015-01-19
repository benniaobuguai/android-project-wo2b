package com.wo2b.tu123.ui.global;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.wo2b.tu123.model.image.PhotoInfoSet;
import com.wo2b.tu123.ui.image.ImageViewerActivity;

/**
 * Activity间通讯的辅助类
 * 
 * @author Rocky
 * 
 */
public class RockyIntentUtils
{

	/**
	 * 
	 * @param activity
	 * @param photoInfoSet
	 * @param position
	 * @param cacheDir
	 */
	public static void gotoImageViewerActivity(Activity activity, PhotoInfoSet photoInfoSet, int position,
			String cacheDir)
	{
		Bundle bundle = new Bundle();
		bundle.putSerializable(RockyIntent.EXTRA_IMAGE_SET, photoInfoSet);
		bundle.putInt(RockyIntent.EXTRA_POSITION, position);
		bundle.putString(RockyIntent.EXTRA_DIRECTORY, cacheDir);

		Intent intent = new Intent(activity, ImageViewerActivity.class);
		intent.putExtras(bundle);
		activity.startActivity(intent);
	}

}
