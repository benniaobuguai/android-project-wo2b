package com.wo2b.tu123.ui.baike;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import opensource.component.otto.Subscribe;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.wo2b.sdk.bus.GEvent;
import com.wo2b.sdk.network.NetworkStatus;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.global.GAdTempData;
import com.wo2b.tu123.global.provider.DataProvider;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.image.ImageGridActivity;
import com.wo2b.tu123.ui.view.ImageRecommendView.RecommendItem;
import com.wo2b.wrapper.app.fragment.LazyFragment;
import com.wo2b.wrapper.app.service.DaemonService;
import com.wo2b.wrapper.component.common.Wo2bAppListActivity;
import com.wo2b.wrapper.view.XListView;
import com.wo2b.wrapper.view.XListView.OnItemClickListener;
import com.wo2b.sdk.view.viewpager.AutoScrollPoster;
import com.wo2b.sdk.view.viewpager.AutoScrollPoster.OnItemViewClickListener;
import com.wo2b.xxx.webapp.Wo2bResListHandler;
import com.wo2b.xxx.webapp.ad.Advertisement;
import com.wo2b.xxx.webapp.ad.AdvertisementManager;
import com.wo2b.xxx.webapp.ad.AdvertisementType;


/**
 * Baike Fragment.
 * 
 * @author 笨鸟不乖
 * 
 */
public class BaikeFragment extends LazyFragment implements OnClickListener
{
	
	private static final String TAG = "BaikeFragment";
	
	private AutoScrollPoster mPosterView;
	private XListView listView;
	private LinearLayout warning_bar;
	private TextView warning_homepage;
	private TextView warning_close;

	private ImageLoader mImageLoader;
	private DisplayImageOptions mOptions;
	private SaveImageOptions mSaveOptions;

	private BaseAdapter mAdapter;
	private List<AlbumInfo> mAlbumInfos = new ArrayList<AlbumInfo>();
	private RecommendItem[] mRecommendItems;

	private static final int MSG_LOAD = 1;
	private static final int MSG_ADS_LOAD = 2;
	
	private View root;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		mImageLoader = ImageLoader.getInstance();
		mSaveOptions = new SaveImageOptions.Builder()
			.medule("Baike_Album_List")
			.extraDir(ExtraDir.ALBUM)
			.build();
		mOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.warn_image_loading)
			.showImageForEmptyUri(R.drawable.warn_image_empty)
			.showImageOnFail(R.drawable.warn_image_error)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(6))
			.saveImageOptions(mSaveOptions)
			.build();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		
		if (root != null)
		{
			ViewGroup parent = (ViewGroup) root.getParent();
			if (parent != null)
			{
				parent.removeView(root);
			}
		}
		try
		{
			root = inflater.inflate(R.layout.tu_baike_main, container, false);
			initView(root);
			bindEvents();
		}
		catch (InflateException e)
		{
			/* map is already there, just return view as it is */
		}
		
		return root;
	}
	
	protected boolean busEventEnable()
	{
		return true;
	}

	/**
	 * 网络监听
	 * 
	 * @param msg
	 */
	@Subscribe
	public void networkMonitor(Message msg)
	{
		if (msg.what == GEvent.NETWORK_STATUS)
		{
			NetworkStatus ns = (NetworkStatus) msg.obj;
			switch (ns)
			{
				case CONNECTED:
				case MOBILE:
				case WIFI:
				{
					hideWarningBar();
					break;
				}
				case DISCONNECTED:
				{
					showWarningBar(getString(R.string.hint_network_check));
					break;
				}
			}
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		mPosterView.resumeScroll();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		mPosterView.stopScroll();
	}

	@Override
	protected void initView(View view)
	{
		mPosterView = findViewByIdExt(view, R.id.rocky_viewpager);
		SaveImageOptions saveOptions = new SaveImageOptions.Builder()
			.medule("Baike_Recommend")
			.extraDir(ExtraDir.ADS)
			.build();

		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.poster_default)
			.showImageForEmptyUri(R.drawable.poster_default)
			.showImageOnFail(R.drawable.poster_default)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.saveImageOptions(saveOptions)
			.build();
		
		mPosterView.setDisplayImageOptions(displayImageOptions);
		mPosterView.setScaleType(ScaleType.FIT_XY);
		mPosterView.needLoadAnimation(false);
		mPosterView.setOnItemViewClickListener(new OnItemViewClickListener()
		{
			
			@Override
			public void onItemViewClick(View view, Object item)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), Wo2bAppListActivity.class);
				startActivity(intent);
			}
		});

		listView = (XListView) view.findViewById(R.id.listview);
		warning_bar = (LinearLayout) view.findViewById(R.id.warning_bar);
		warning_homepage = (TextView) view.findViewById(R.id.warning_homepage);
		warning_close = (TextView) view.findViewById(R.id.warning_close);

		// 网络状态
		Message msg = new Message();
		msg.what = GEvent.NETWORK_STATUS;
		msg.obj = DaemonService.mNetworkStatus;
		networkMonitor(msg);
	}
	
	/**
	 * 在首页显示警告或通知类信息, 如: 网络异常, 应用新版本等.
	 * 
	 * @param warning
	 */
	public void showWarningBar(CharSequence warning)
	{
		warning_bar.setVisibility(View.VISIBLE);
		warning_homepage.setText(warning);
	}

	/**
	 * 隐藏警告栏
	 */
	public void hideWarningBar()
	{
		warning_bar.setVisibility(View.GONE);
	}
	
	@Override
	protected void bindEvents()
	{
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(Adapter adapter, View view, int position, long id)
			{
				Intent intent = new Intent(getContext(), ImageGridActivity.class);
				intent.putExtra(RockyIntent.EXTRA_ALBUM, (AlbumInfo) adapter.getItem(position));
				startActivity(intent);
			}

		});

		warning_close.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.warning_close:
			{
				// 隐藏警告栏
				hideWarningBar();
				break;
			}
		}
	}

	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD:
			{
				mAdapter = new ImageListAdapter();
				listView.setAdapter(mAdapter);
				break;
			}
			case MSG_ADS_LOAD:
			{
				List<String> imageList = new ArrayList<String>();
				for (RecommendItem item : mRecommendItems)
				{
					imageList.add(item.image);
				}
				
				mPosterView.addItems(imageList);
				mPosterView.startAutoScroll(5 * 1000);
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
			case MSG_LOAD:
			{
				mAlbumInfos = DataProvider.getInstance().getCategoryList(getActivity());
				getUiHandler().sendEmptyMessage(MSG_LOAD);
				break;
			}
			case MSG_ADS_LOAD:
			{
				int length = GAdTempData.DefaultAdList.length;
				mRecommendItems = new RecommendItem[length];
				for (int i = 0; i < length; i++)
				{
					mRecommendItems[i] = new RecommendItem();
					mRecommendItems[i].image = "drawable://" + GAdTempData.DefaultAdList[i];
					mRecommendItems[i].drawableId = GAdTempData.DefaultAdList[i];
				}

				getUiHandler().sendEmptyMessage(MSG_ADS_LOAD);
				break;
			}
		}

		return super.subHandlerCallback(msg);
	}

	public void onImageListClick(View view)
	{
	}
	
	public void onImageGridClick(View view)
	{
	}
	
	public class ImageListAdapter extends BaseAdapter
	{
		
		public ImageListAdapter()
		{
		}
		
		@Override
		public int getCount()
		{
			return mAlbumInfos.size();
		}
		
		@Override
		public AlbumInfo getItem(int position)
		{
			return mAlbumInfos.get(position);
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.tu_baike_list_item, null);
				holder.image = (ImageView) convertView.findViewById(R.id.image);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.desc = (TextView) convertView.findViewById(R.id.desc);
				//holder.picnum = (TextView) convertView.findViewById(R.id.picnum);
				convertView.setTag(holder);
			}
			else
			{
				holder = (ViewUtils) convertView.getTag();
			}
			
			AlbumInfo album = getItem(position);
			
			holder.name.setText(album.getName());
			holder.desc.setText(album.getDesc());
			//holder.picnum.setText(album.getPicnum() + "");
			
			mImageLoader.displayImage(album.getCoverurl(), holder.image, mOptions);
			
			return convertView;
		}
		
		class ViewUtils
		{
			ImageView image;
			TextView name;
			TextView desc;
			//TextView picnum;
		}
	}
	
	@Override
	public void doUserVisibleHint()
	{
		getSubHandler().sendEmptyMessage(MSG_LOAD);
		getSubHandler().sendEmptyMessage(MSG_ADS_LOAD);

		AdvertisementManager adManager = new AdvertisementManager();
		adManager.findByAdType(AdvertisementType.COMMON_WO2B_APP.getCategoryId(),
				new Wo2bResListHandler<Advertisement>()
				{

					@Override
					public void onSuccess(int code, List<Advertisement> result)
					{
						System.out.println("code: " + code + ", ad: " + result);
					}

					@Override
					public void onFailure(int code, String msg, Throwable throwable)
					{
						System.out.println("code: " + code + ", msg: " + msg);
					}

				});
	}
	
}
