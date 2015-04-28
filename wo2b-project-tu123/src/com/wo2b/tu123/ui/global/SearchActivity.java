package com.wo2b.tu123.ui.global;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import opensource.component.pulltorefresh.PullToRefreshBase.Mode;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.wrapper.app.BaseSearchActivity;
import com.wo2b.tu123.R;
import com.wo2b.tu123.business.base.DatabaseHelper;
import com.wo2b.tu123.business.image.AlbumBiz;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.ui.image.ImageGridActivity;

/**
 * Search Activity.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class SearchActivity extends BaseSearchActivity<AlbumInfo> implements OnClickListener
{
	
	private static final String TAG = "SearchActivity";

	private ImageLoader mImageLoader = null;
	private DisplayImageOptions mOptions = null;
	private SaveImageOptions mSaveOptions = null;

	private AlbumBiz albumBiz = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_main_global);

		// 设置不可以上下拉动
		getRockyListView().setMode(Mode.DISABLED);
		
		mImageLoader = ImageLoader.getInstance();
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("COMMON_SEARCH")
			.extraDir(ExtraDir.ALBUM)	// 存储在相册路径下
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
		
		albumBiz = new AlbumBiz(DatabaseHelper.getDatabaseHelper(this));
	}

	@Override
	protected void realOnItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		AlbumInfo albumInfo = (AlbumInfo) parent.getItemAtPosition(position);
		Intent intent = new Intent(getContext(), ImageGridActivity.class);
		intent.putExtra(RockyIntent.EXTRA_ALBUM, albumInfo);
		startActivity(intent);
	}
	
	@Override
	public void onClick(View v)
	{
		
	}

	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.blossom_album_list_item, parent, false);
		}
		ImageView image = ViewUtils.get(convertView, R.id.image);
		TextView name = ViewUtils.get(convertView, R.id.name);
		TextView desc = ViewUtils.get(convertView, R.id.desc);
		TextView picnum = ViewUtils.get(convertView, R.id.picnum);

		AlbumInfo albumInfo = realGetItem(position);
		name.setText(albumInfo.getName());
		desc.setText(albumInfo.getDesc());
		picnum.setText(albumInfo.getPicnum() + "");
		mImageLoader.displayImage(albumInfo.getCoverurl(), image, mOptions);

		return convertView;
	}

	@Override
	protected XModel<AlbumInfo> realOnPullDown(RockyParams params)
	{
		if (params == null)
		{
			return XModel.empty();
		}

		String keyword = params.getText1();
		if (TextUtils.isEmpty(keyword))
		{
			return XModel.empty();
		}

		return XModel.list(albumBiz.queryByAlbumName(keyword));
	}

	@Override
	protected XModel<AlbumInfo> realOnPullUp(RockyParams params)
	{
		return null;
	}

}
