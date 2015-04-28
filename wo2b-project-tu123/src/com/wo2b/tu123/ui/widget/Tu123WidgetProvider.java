/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.wo2b.tu123.ui.widget;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DefaultConfigurationFactory;
import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.assist.ImageSize;
import opensource.component.imageloader.core.assist.SimpleImageLoadingListener;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RemoteViews;

import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.UserDatabaseHelper;
import com.wo2b.tu123.business.image.MyFavoritesBiz;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.model.image.MyFavorites;

/**
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class Tu123WidgetProvider extends AppWidgetProvider
{
	
	private static SaveImageOptions mSaveImageOptions;
	private static DisplayImageOptions mDisplayOptions;
	
	private MyFavoritesBiz mMyFavoritesBiz;
	private AlbumInfo mAlbumInfo;
	private List<MyFavorites> mFavorites = new ArrayList<MyFavorites>();
	
	private int mIndex = 0;
	
	static
	{
		
	}
	
	private void initData(Context context)
	{
		mAlbumInfo = new AlbumInfo();
		mAlbumInfo.setAlbumid("my_favorites_albumid");
		mAlbumInfo.setName(context.getString(R.string.my_favorites));
		
		mSaveImageOptions = new SaveImageOptions.Builder()
			.title("Tu123Widget")
			.medule("My Favorites")
			.extraDir(ExtraDir.IMAGE + mAlbumInfo.getName())
			.build();
		
		mDisplayOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(DefaultConfigurationFactory.createBitmapDisplayer())
			.saveImageOptions(mSaveImageOptions)
			.build();
		
		//mDisplayOptions = DisplayImageOptions.createSimple();
		
		mMyFavoritesBiz = new MyFavoritesBiz(UserDatabaseHelper.getUserDatabaseHelper(context));
		mFavorites = mMyFavoritesBiz.queryForAll();
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
	{
		final int widgetCount = appWidgetIds.length;
		
		// Load Data.
		if (mFavorites == null || mFavorites.isEmpty())
		{
			initData(context);
		}
		
		for (int i = 0; i < widgetCount; i++)
		{
			int appWidgetId = appWidgetIds[i];
			updateAppWidget(context, appWidgetManager, appWidgetId);
		}
	}
	
	private void updateAppWidget(Context context, final AppWidgetManager appWidgetManager, final int appWidgetId)
	{
		final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
		
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		final int width = displayMetrics.widthPixels / 2;
		final int height = displayMetrics.heightPixels / 2;
		
		ImageSize minImageSize = new ImageSize(width, height); // 70 - approximate size
														// of ImageView in
														// widget
		ImageLoader.getInstance().loadImage(mFavorites.get(0).getLargeUrl(), minImageSize, mDisplayOptions,
				new SimpleImageLoadingListener()
				{
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
					{
						views.setImageViewBitmap(R.id.image_left, loadedImage);
						appWidgetManager.updateAppWidget(appWidgetId, views);
					}
				});
		ImageLoader.getInstance().loadImage(mFavorites.get(1).getLargeUrl(), minImageSize, mDisplayOptions,
				new SimpleImageLoadingListener()
				{
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
					{
						views.setImageViewBitmap(R.id.image_right, loadedImage);
						appWidgetManager.updateAppWidget(appWidgetId, views);
					}
				});
	}
	
}
