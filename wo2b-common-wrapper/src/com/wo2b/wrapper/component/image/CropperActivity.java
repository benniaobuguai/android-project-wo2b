package com.wo2b.wrapper.component.image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import opensource.component.cropper.CropImageView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.alibaba.fastjson.util.IOUtils;
import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.core.RockyConfig;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyFragmentActivity;

/**
 * 图片裁剪, 不应该过分关注业务逻辑, 目标图片输出到哪里, 不需要考虑. 最终, 图片信息会返回给请求方.
 * <ul>
 * <li>1. 暂时不进行各种机型的适配, 直接使用所有尺寸由用户自行裁剪.</li>
 * <li></li>
 * <li></li>
 * </ul>
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class CropperActivity extends RockyFragmentActivity
{

	private static final String TAG = "Image.CropperActivity";

	public static final String ACTION_CROP_ANDROID = "com.android.camera.action.CROP";
	public static final String ACTION_CROP_WO2B = "com.wo2b.camera.action.CROP";
	public static final String DATA_AND_TYPE = "image/*";

	// ---------------------------------------------------------------------------------
	// 通用信息
	public static final String EXTRA_CROP = "crop";
	public static final String EXTRA_ASPECTX = "aspectX";
	public static final String EXTRA_ASPECTY = "aspectY";
	public static final String EXTRA_OUTPUTX = "outputX";
	public static final String EXTRA_OUTPUTY = "outputY";
	public static final String EXTRA_SCALE = "scale"; // 是否可放大
	public static final String EXTRA_OUTPUT = MediaStore.EXTRA_OUTPUT; // 输出路径
	public static final String EXTRA_RETURN_DATA = "return-data"; // 是否返回数据
	public static final String EXTRA_OUTPUTFORMAT = "outputFormat"; // 输出图片文件格式
	public static final String EXTRA_NOFACEDETECTION = "noFaceDetection"; // 人脸检测

	/**
	 * ActionBar的标题
	 */
	public static final String EXTRA_TITLE = "title";
	/**
	 * 裁剪后, 图片缓存的目录.
	 */
	public static final String CROPPED_CACHE_DIR = RockyConfig.getWo2bCacheDir();
	
	
	private boolean isCrop = true;
	private int aspectX = 0;
	private int aspectY = 0;
	private int outputX = 0;
	private int outputY = 0;
	private boolean isScale = true;
	private Uri output;
	private boolean isReturnData = false;
	private boolean noFaceDetection = true;
	private String outputFormat;
	private String title;
	
	private CropImageView cropImageView;
	private Bitmap mSourceBitmap;
	private Bitmap mCroppedBitmap;
	

	// Static final constants
	private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
	private static final int ROTATE_NINETY_DEGREES = 90;
	private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
	private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
	private static final int ON_TOUCH = 1;

	// Instance variables
	private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
	private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_cn_image_cropper);
		cropImageView = (CropImageView) findViewById(R.id.crop_imageview);
		// Sets initial aspect ratio to 10/10, for demonstration purposes
		//cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

		Intent intent = getIntent();
		isCrop = intent.getBooleanExtra(EXTRA_CROP, true);
		aspectX = intent.getIntExtra(EXTRA_ASPECTX, 0);
		aspectY = intent.getIntExtra(EXTRA_ASPECTY, 0);
		outputX = intent.getIntExtra(EXTRA_OUTPUTX, 0);
		outputY = intent.getIntExtra(EXTRA_OUTPUTY, 0);
		isScale = intent.getBooleanExtra(EXTRA_SCALE, true);
		output = intent.getParcelableExtra(EXTRA_OUTPUT);
		isReturnData = intent.getBooleanExtra(EXTRA_RETURN_DATA, false);
		noFaceDetection = intent.getBooleanExtra(EXTRA_NOFACEDETECTION, true);
		outputFormat = intent.getStringExtra(EXTRA_OUTPUTFORMAT);
		if (TextUtils.isEmpty(outputFormat))
		{
			outputFormat = Bitmap.CompressFormat.PNG.toString();
		}
		title = intent.getStringExtra(EXTRA_TITLE);

		String path = output.getPath();
		mSourceBitmap = BitmapFactory.decodeFile(path);
		cropImageView.setImageBitmap(mSourceBitmap);
	}
	
	// Saves the state upon rotating the screen/restarting the activity
	@Override
	protected void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
		bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
		
	}

	// Restores the state upon rotating the screen/restarting the activity
	@Override
	protected void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
		mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
	}
	
	private static final int MENU_GROUP = 100;
	private static final int MENU_ITEM_OK = 1001;
	private static final int MENU_ITEM_CANCLE = 1002;

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.add(MENU_GROUP, MENU_ITEM_CANCLE, 2, R.string.cancel).setIcon(R.drawable.selector_actionbar_cancel_btn)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.add(MENU_GROUP, MENU_ITEM_OK, 1, R.string.ok).setIcon(R.drawable.selector_actionbar_ok_btn)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (MENU_ITEM_CANCLE == item.getItemId())
		{
			releaseBitmap();
			finish();
			return true;
		}
		else if (MENU_ITEM_OK == item.getItemId())
		{
			crop_ok();
			finish();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void crop_ok()
	{
		mCroppedBitmap = cropImageView.getCroppedImage();
		CompressFormat format = CompressFormat.PNG;
		int quality = 100;

		File output_dir = new File(CROPPED_CACHE_DIR);
		if(!output_dir.exists())
		{
			// 新建目录
			output_dir.mkdirs();
		}
		String output_filename = new File(output.getPath()).getName();
		File output_file = new File(output_dir.getPath() + "/" + output_filename);
		FileOutputStream fos = null;
		try
		{
			if (!output_file.exists())
			{
				// 新建文件
				output_file.createNewFile();
			}

			fos = new FileOutputStream(output_file);
			mCroppedBitmap.compress(format, quality, fos);
		}
		catch (FileNotFoundException e)
		{
			Log.E(TAG, "File not found, cropped image save failed!!!");
		}
		catch (IOException e)
		{
			Log.E(TAG, "The cropped image can not be saved!!!");
		}
		finally
		{
			IOUtils.close(fos);
		}

		releaseBitmap();

		Uri data = Uri.fromFile(output_file);
		Intent intent = new Intent();
		intent.setData(data);
		setResult(RESULT_OK, intent);
	}
	
	private void releaseBitmap()
	{
		if (mSourceBitmap != null && !mSourceBitmap.isRecycled())
		{
			mSourceBitmap.recycle();
		}
		if (mCroppedBitmap != null && !mCroppedBitmap.isRecycled())
		{
			mCroppedBitmap.recycle();
		}
	}
	
	@Override
	public void finish()
	{
		super.finish();

		// Activity切换动画使用淡入淡出的效果
		this.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}

	/**
	 * Entry cropper
	 * 
	 * @param activity
	 * @param requestCode
	 * @param title
	 * @param imagePath
	 * @param savePath
	 */
	public static void startCropperActivity(Activity activity, int requestCode, String title, Uri output)
	{
		Intent intent = new Intent();
		intent.setClass(activity, CropperActivity.class);
		intent.putExtra(EXTRA_TITLE, title);
		intent.putExtra(EXTRA_OUTPUT, output);

		activity.startActivityForResult(intent, requestCode);

		// Activity切换动画使用淡入淡出的效果
		activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
	}
	
//	public static final String EXTRA_CROP = "crop";
//	public static final String EXTRA_ASPECTX = "aspectX";
//	public static final String EXTRA_ASPECTY = "aspectY";
//	public static final String EXTRA_OUTPUTX = "outputX";
//	public static final String EXTRA_OUTPUTY = "outputY";
//	public static final String EXTRA_SCALE = "scale"; // 是否可放大
//	public static final String EXTRA_OUTPUT = MediaStore.EXTRA_OUTPUT; // 输出路径
//	public static final String EXTRA_RETURN_DATA = "return-data"; // 是否返回数据
//	public static final String EXTRA_OUTPUTFORMAT = "outputFormat"; // 输出图片文件格式
//	public static final String EXTRA_NOFACEDETECTION = "noFaceDetection"; // 人脸检测
	
}
