package com.wo2b.tu123.ui.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.widget.ImageView;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.common.util.io.IOUtils;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.CommonUtils;

/**
 * Wallpaper dispose.
 * 
 * @author 笨鸟不乖 
 * <code> 
 * ----------------------------- 关键分析 -----------------------------
 * Intent intent = new Intent("com.android.camera.action.CROP");
 * intent.setDataAndType(source, "image/*");
 * intent.putExtra("crop", "true");
 * intent.putExtra("aspectX", aspectX);
 * intent.putExtra("aspectY", aspectY);
 * intent.putExtra("outputX", outputX);
 * intent.putExtra("outputY", outputY);
 * intent.putExtra("scale", true);
 * intent.putExtra(MediaStore.EXTRA_OUTPUT, target);
 * intent.putExtra("return-data", false);
 * intent.putExtra("outputFormat",
 * Bitmap.CompressFormat.PNG.toString());
 * intent.putExtra("noFaceDetection", true); // no face detection
 * 
 * startActivityForResult(intent, requestCode);
 * 使用第三方截图工具时, 返回的图片大小并不确定.
 * 1. 使用三星S3截图完成后，返回的是截图框中的图片的大小, 并不是outputX和outputY.
 * 2. 使用快图截图后, 截图框得到的图片将会被缩放至目标的outputX和outputY.
 * 3. 因此需要有一个适配的方案, 无论得到的图片是多大, 都需要把图片缩放至目标的outputX和outputY.
 * 
 * 壁纸约定规则：假设手机分辨率为1280x720, 一般需要提供的大小为：高度不变, 宽度x2.
 * 然后系统会对最终的图片进行裁剪, 从提供的图片, 取中间部分.
 * 优化方案: 得到的截图A, 最终生成三个图片A1-A2-A3, 取中间部分, 仍然为之前裁剪的图片.
 * ----------------------------- 关键分析-----------------------------
 * </code>
 */
public class WallpaperDisposeActivity extends BaseFragmentActivity
{

	private static final String TAG = "WallpaperDisposeActivity";
	
	private static final int MSG_LOAD_BITMAP = 1;
	private static final int MSG_SET_WALLPAPER = 2;
	
	private static final int MSG_UI_PREVIEW_WALLPAPER = 1;
	
	private ImageView previewContainer;
	
	private Uri mWallpaperUri;
	private Bitmap mCropedBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_viewer_wallpaper);

		Intent intent = getIntent();
		mWallpaperUri = intent.getData();

		initView();
		bindEvents();
		setDefaultValues();
	}
	
	@Override
	protected void initView()
	{
		getSupportActionBar().setTitle(R.string.preview);
		previewContainer = (ImageView) findViewById(R.id.image);
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
		getSubHandler().sendEmptyMessage(MSG_SET_WALLPAPER);
	}
	
	@Override
	protected void setDefaultValues()
	{
		getSubHandler().sendEmptyMessage(MSG_LOAD_BITMAP);
	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_UI_PREVIEW_WALLPAPER:
			{
				// Preview wallpaper
				//Bitmap bitmap = (Bitmap) msg.obj;
				
				previewContainer.setImageBitmap(mCropedBitmap);
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
			case MSG_LOAD_BITMAP:
			{
				// Load bitmap
				File file = new File(mWallpaperUri.getPath());
				if (!file.exists())
				{
					// 加载不到壁纸文件, 提示并退出.
					Log.D(TAG, "Wallpaper not found!!! Path--> " + file.getPath());
					showToastOnUiThread(R.string.hint_wallpaper_not_found);
					finish();
					return true;
				}
				
				Bitmap source = null;
				try
				{
					FileInputStream fis = new FileInputStream(file);
					
					byte[] data = IOUtils.getByteArray(fis);
					source = BitmapFactory.decodeByteArray(data, 0, data.length);
				}
				catch (IOException e)
				{
					// 加载图片异常, 提示并退出.
					e.printStackTrace();
					showToastOnUiThread(R.string.hint_wallpaper_load_error);
					finish();
					return true;
				}
				
				if (source == null)
				{
					Log.D(TAG, "Wallpaper is null!!!");
					return true;
				}
				
				Log.D(TAG, "Source bitmap: [width=" + source.getWidth() + ", height=" + source.getHeight() + "]");
				
				mCropedBitmap = source;
				getUiHandler().sendEmptyMessage(MSG_UI_PREVIEW_WALLPAPER);
				break;
			}
			case MSG_SET_WALLPAPER:
			{
				try
				{
					setWallpaperNow(mCropedBitmap);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					showToastOnUiThread(R.string.hint_wallpaper_fail);
				}
				
				break;
			}
		}

		return super.subHandlerCallback(msg);
	}

	/**
	 * Set wallpaper
	 */
	private void setWallpaperNow(Bitmap source) throws IOException
	{
		DisplayMetrics dm = CommonUtils.getDisplayMetrics(this);
		int status_bar_height = CommonUtils.getStatusBarHeight(this);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels - status_bar_height;
		
		int bitmapWidth = source.getWidth();
		int bitmapHeight = source.getHeight();
		
		float scaleX = (screenWidth + 0.0f) / bitmapWidth;
		float scaleY = (screenHeight + 0.0f) / bitmapHeight;
		
		Log.D(TAG, "[scaleX=" + scaleX + ", scaleY=" + scaleY + "]");
		
		Bitmap tempBitmap = null;
		Bitmap targetBitmap = null;
		try
		{
			// 按比例缩放达到适配手机屏幕
			// targetBitmap = Bitmap.createBitmap(screenWidth, screenHeight,
			// Config.ARGB_8888);
			
			Matrix matrix = new Matrix();
			matrix.postScale(scaleX, scaleY);
			tempBitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
			
			Log.D(TAG, "TargetBitmap: [width=" + tempBitmap.getWidth() + ", height=" + tempBitmap.getHeight() + "]");
			
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
			targetBitmap = Bitmap.createBitmap(screenWidth * 3, screenHeight, Config.ARGB_8888);
			Canvas canvas = new Canvas(targetBitmap);
			canvas.drawColor(Color.RED);
			canvas.drawBitmap(tempBitmap, 0, 0, null);
			canvas.drawBitmap(tempBitmap, screenWidth, 0, null);
			canvas.drawBitmap(tempBitmap, screenWidth * 2, 0, null);
			
			wallpaperManager.setBitmap(targetBitmap);
			
			showToastOnUiThread(R.string.hint_wallpaper_ok);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (tempBitmap != null && !tempBitmap.isRecycled())
			{
				tempBitmap.recycle();
				tempBitmap = null;
			}
			if (targetBitmap != null && !targetBitmap.isRecycled())
			{
				targetBitmap.recycle();
				targetBitmap = null;
			}
		}
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (mCropedBitmap != null && !mCropedBitmap.isRecycled())
		{
			mCropedBitmap.recycle();
		}
	}

}
