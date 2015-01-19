package com.wo2b.tu123.ui.baike;

import java.util.ArrayList;
import java.util.List;

import opensource.component.imageloader.core.DisplayImageOptions;
import opensource.component.imageloader.core.ImageLoader;
import opensource.component.imageloader.core.SaveImageOptions;
import opensource.component.imageloader.core.display.RoundedBitmapDisplayer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.wrapper.app.fragment.RockyFragment;
import com.wo2b.wrapper.view.XImageSwitcher;
import com.wo2b.wrapper.view.XListView;
import com.wo2b.wrapper.view.XListView.OnItemClickListener;
import com.wo2b.tu123.R;
import com.wo2b.tu123.global.AppCacheFactory.ExtraDir;
import com.wo2b.tu123.global.provider.ADSData;
import com.wo2b.tu123.global.provider.DataProvider;
import com.wo2b.tu123.model.image.AlbumInfo;
import com.wo2b.tu123.ui.global.RockyIntent;
import com.wo2b.tu123.ui.image.ImageGridActivity;
import com.wo2b.tu123.ui.view.ImageRecommendView.RecommendItem;
import com.wo2b.xxx.webapp.Wo2bResListHandler;
import com.wo2b.xxx.webapp.ad.Advertisement;
import com.wo2b.xxx.webapp.ad.AdvertisementManager;
import com.wo2b.xxx.webapp.ad.AdvertisementType;
import com.wo2b.wrapper.component.common.Wo2bAppListActivity;


/**
 * Baike Fragment.
 * 
 * @author Rocky
 * 
 */
public class BaikeFragment extends RockyFragment implements OnClickListener, ViewFactory
{
	
	private static final String TAG = "BaikeFragment";
	
	private XImageSwitcher recommend_view;
	private XListView listView;
	//private LinearLayout third_party_ads;
	
	//private TextView acticle_01;
	//private TextView acticle_02;
	//private TextView acticle_03;
	//private TextView acticle_04;
	
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
	
	// 网络状态连接监听器
	private BroadcastReceiver mConnectionReceiver = new BroadcastReceiver()
	{

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			Log.I(TAG, "Network state changed, action: " + action);

			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			try
			{
				if (mobileNetInfo.isConnected() && wifiNetInfo.isConnected())
				{
					Log.I(TAG, "Mobile/Wifi Network is connected.");

					hideWarningBar();
				}
				else if (mobileNetInfo.isConnected())
				{
					Log.I(TAG, "Mobile Network is connected.");

					hideWarningBar();
				}
				else if (wifiNetInfo.isConnected())
				{
					Log.I(TAG, "Wifi Network is connected.");

					hideWarningBar();
				}
				else if (!mobileNetInfo.isConnected() && !wifiNetInfo.isConnected())
				{
					// 所有网络都没有连接至网络
					Log.D(TAG, "Network is not connected.");

					showWarningBar("Network is not connected.");
				}
				else
				{
					// 所有网络都没有连接至网络
					Log.D(TAG, "Network is not connected.");

					showWarningBar("ELSE-->Network is not connected.");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	};
		
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.I(TAG, "onCreate--------------------->>>");
		
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
		
		// 网络监听
		registerNetworkBroadcastReceiver();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		Log.I(TAG, "onActivityCreated--------------------->>>");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		Log.I(TAG, "onCreateView--------------------->>>");
		
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
			setDefaultValues();
		}
		catch (InflateException e)
		{
			/* map is already there, just return view as it is */
		}
		
		return root;
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		Log.I(TAG, "onResume--------------------->>>");
		
		recommend_view.resumeScroll();
	}

	@Override
	public void onPause()
	{
		super.onPause();
		Log.I(TAG, "onPause--------------------->>>");
		
		recommend_view.stopScroll();
	}
	
	@Override
	public void onDestroy()
	{
		unregisterNetworkBroadcastReceiver();
		super.onDestroy();
	}
	
	@Override
	protected void initView(View view)
	{
		recommend_view = (XImageSwitcher) view.findViewById(R.id.recommend_view);
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

		// 设置Factory
		recommend_view.setFocusable(true);
		recommend_view.setFocusableInTouchMode(true);
		recommend_view.setFactory(this);
		recommend_view.setDisplayImageOptions(displayImageOptions);
		recommend_view.setLoadingImage(R.drawable.poster_default);
		recommend_view.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// Log.D(TAG, "Current Position: " + recommend_view.getCurrentPosition());
				Intent intent = new Intent();
				intent.setClass(getActivity(), Wo2bAppListActivity.class);
				startActivity(intent);
			}
		});
		
		
		listView = (XListView) view.findViewById(R.id.listview);
		//third_party_ads = (LinearLayout) view.findViewById(R.id.third_party_ads);
		//third_party_ads.setVisibility(View.GONE);
		
		
		//acticle_01 = (TextView) view.findViewById(R.id.acticle_01);
		//acticle_02 = (TextView) view.findViewById(R.id.acticle_02);
		//acticle_03 = (TextView) view.findViewById(R.id.acticle_03);
		//acticle_04 = (TextView) view.findViewById(R.id.acticle_04);
		
		warning_bar = (LinearLayout) view.findViewById(R.id.warning_bar);
		warning_homepage = (TextView) view.findViewById(R.id.warning_homepage);
		warning_close = (TextView) view.findViewById(R.id.warning_close);
		warning_homepage.setText("网络信息异常, 请检查您的网络状况");
		hideWarningBar();

		bindArticle();
	}
	
	@Override
	public View makeView()
	{
		final ImageView i = new ImageView(getActivity());
		i.setBackgroundColor(0xff000000);
		//i.setScaleType(ImageView.ScaleType.CENTER_CROP);
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

		return i;
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
	
	/**
	 * 绑定文章到网格
	 */
	private void bindArticle()
	{
//		int articleCount = ArticleProvider.ARTICLE_STORE.length;
//		String[] body = null;
//		for (int i = 0; i < articleCount && i < 4; i++)
//		{
//			body = ArticleProvider.ARTICLE_STORE[i];
//			if (body != null && body.length == 2)
//			{
//				if (i == 0)
//				{
//					bindArticle2TextView(acticle_01, body[0], body[1]);
//				}
//				else if (i == 1)
//				{
//					bindArticle2TextView(acticle_02, body[0], body[1]);
//				}
//				else if (i == 2)
//				{
//					bindArticle2TextView(acticle_03, body[0], body[1]);
//				}
//				else if (i == 3)
//				{
//					bindArticle2TextView(acticle_04, body[0], body[1]);
//				}
//			}
//		}
	}

//	private void bindArticle2TextView(TextView tv, String title, String address)
//	{
//		tv.setText(title);
//		tv.setTag(address);
//	}

	@Override
	protected void setDefaultValues()
	{
		getSubHandler().sendEmptyMessage(MSG_LOAD);
		getSubHandler().sendEmptyMessage(MSG_ADS_LOAD);

		AdvertisementManager adManager = new AdvertisementManager();
		adManager.findByAdType(AdvertisementType.COMMON_WO2B_APP.getCategoryId(), new Wo2bResListHandler<Advertisement>()
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
		
		
		//acticle_01.setOnClickListener(this);
		//acticle_02.setOnClickListener(this);
		//acticle_03.setOnClickListener(this);
		//acticle_04.setOnClickListener(this);
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
			//case R.id.acticle_01:
			//case R.id.acticle_02:
			//case R.id.acticle_03:
			//case R.id.acticle_04:
			//{
			//	onArticleClick(v);
			//	break;
			//}
		}
	}
	
//	/**
//	 * 文章点击事件
//	 * 
//	 * @param v
//	 */
//	private void onArticleClick(View v)
//	{
//		TextView articleView = (TextView) v;
//
//		Intent intent = new Intent();
//		intent.setClass(getActivity(), ArticleReaderActivity.class);
//		intent.putExtra(ArticleReaderActivity.ARTICLE_TITLE, articleView.getText().toString());
//		intent.putExtra(ArticleReaderActivity.ARTICLE_ADDRESS, articleView.getTag().toString());
//		startActivity(intent);
//	}
	
	@Override
	protected boolean uiHandlerCallback(Message msg)
	{
		switch (msg.what)
		{
			case MSG_LOAD:
			{
				//mAdapter.notifyDataSetChanged();
				mAdapter = new ImageListAdapter();
				listView.setAdapter(mAdapter);
				
				//third_party_ads.setVisibility(View.VISIBLE);
				break;
			}
			case MSG_ADS_LOAD:
			{
				List<String> imageList = new ArrayList<String>();
				// String path = null;
				for (RecommendItem item : mRecommendItems)
				{
					imageList.add(item.image);
				}

				recommend_view.addImagePath(imageList);
				recommend_view.startAutoScroll(5 * 1000);
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
				// mFragmentListView.refreshData(mAlbumInfos);
				getUiHandler().sendEmptyMessage(MSG_LOAD);
				break;
			}
			case MSG_ADS_LOAD:
			{
				int length = ADSData.ADS.length;
				mRecommendItems = new RecommendItem[length];
				for (int i = 0; i < length; i++)
				{
					mRecommendItems[i] = new RecommendItem();
					mRecommendItems[i].image = "drawable://" + ADSData.ADS[i];
					mRecommendItems[i].drawableId = ADSData.ADS[i];
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
	
	/**
	 * 注册网络连接广播接收器
	 */
	private void registerNetworkBroadcastReceiver()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		// intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		getActivity().registerReceiver(mConnectionReceiver, intentFilter);
	}

	/**
	 * 取消网络连接广播接收器
	 */
	private void unregisterNetworkBroadcastReceiver()
	{
		if (mConnectionReceiver != null)
		{
			getActivity().unregisterReceiver(mConnectionReceiver);
		}
	}
	
}
