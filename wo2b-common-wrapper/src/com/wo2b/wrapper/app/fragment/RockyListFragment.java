package com.wo2b.wrapper.app.fragment;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import opensource.component.pulltorefresh.PullToRefreshAdapterViewBase;
import opensource.component.pulltorefresh.PullToRefreshBase;
import opensource.component.pulltorefresh.PullToRefreshBase.Mode;
import opensource.component.pulltorefresh.PullToRefreshBase.OnLastItemVisibleListener;
import opensource.component.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.Rocky;
import com.wo2b.wrapper.view.EmptyView;
import com.wo2b.wrapper.view.EmptyView.OnEmptyViewClickListener;

/**
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-9-13
 * @param <Model> 实体
 */
public abstract class RockyListFragment<Model> extends RockyFragment
{

	/**
	 * 第一次请求数据的默认分页数
	 */
	public static final int PAGE_COUNT_DEFAULT = 20;

	protected int mOffset = 0;
	protected int mCount = PAGE_COUNT_DEFAULT;
	protected int mFirstQueryCount = PAGE_COUNT_DEFAULT;

	private RockyParams mRockyParams;

	private PullToRefreshAdapterViewBase<AbsListView> mRockyListView;
	private EmptyView mEmptyView;

	private Mode mPullMode = Mode.PULL_FROM_END;

	/**
	 * 新纪录-->体现在向下拉
	 */
	private boolean mHasNewData = false;

	/**
	 * 更多记录-->体现在向上拉
	 */
	private boolean mHasMoreData = false;

	/**
	 * 适配器视图
	 */
	private RockyListAdapter<Model> mRockyListAdapter;
	
	/**
	 * 数据集
	 */
	private LinkedList<Model> mListData = new LinkedList<Model>();

	/**
	 * 是否正在加载数据
	 */
	private boolean mIsLoading = false;

	/**
	 * 替代onCreateView()创建视图, 不要直接使用onCreateView()
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View onRealCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	/**
	 * 完成View加载后触发的事件
	 * 
	 * @param view
	 */
	public abstract void onCreateViewCompleted(View view, Bundle savedInstanceState);

	/**
	 * 实现此方法, 执行下拉操作, 默认第一次执行时, 会调用些方法.
	 * 
	 * @param params
	 * @return
	 */
	protected abstract XModel<Model> realOnPullDown(RockyParams params);

	/**
	 * 实现此方法, 执行上拉操作
	 * 
	 * @param params
	 * @return
	 */
	protected abstract XModel<Model> realOnPullUp(RockyParams params);

	/**
	 * 子类重写此方法, 用于绘制视图
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	protected abstract View realGetView(int position, View convertView, ViewGroup parent);

	/**
	 * 子线程获取数据
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 */
	public static abstract class GetDataTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>
	{

	}

	/**
	 * 数据适配器
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @param <ITEM>
	 */
	public static abstract class RockyListAdapter<ITEM> extends BaseAdapter
	{

		@Override
		public ITEM getItem(int position)
		{
			return null;
		}

	}
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	@Deprecated
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = onRealCreateView(inflater, container, savedInstanceState);
		onContentChanged(view);

		onCreateViewCompleted(view, savedInstanceState);

		return view;
	}
	
	@SuppressWarnings("unchecked")
	public void onContentChanged(View view)
	{
		mRockyListView = (PullToRefreshAdapterViewBase<AbsListView>) view.findViewById(R.id.rocky_listview);
		// Set empty view
		mEmptyView = new EmptyView(getContext());
		
		mEmptyView.setOnEmptyViewClickListener(new OnEmptyViewClickListener()
		{

			@Override
			public void onEmptyViewClick(EmptyView emptyView)
			{
				realExecuteFirstTime(mRockyParams);
			}
		});
		
		mRockyListView.setEmptyView(mEmptyView);
				
		// onContentChanged() execute immediately after setContentView()(execute in onCreate() normally).
		// It will be invalid first time. The best way to do this with xml.attribute.
		mRockyListView.setMode(mPullMode);
		
		mRockyListView.setOnRefreshListener(new OnRefreshListener2<AbsListView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<AbsListView> refreshView)
			{
				realOnPullDownToRefresh(refreshView, mRockyParams);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<AbsListView> refreshView)
			{
				realOnPullUpToRefresh(refreshView, mRockyParams);
			}
		});

		// Add an end-of-list listener
		mRockyListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener()
		{

			@Override
			public void onLastItemVisible()
			{
				realOnLastItemVisible();
			}
		});

		// ListView actualListView = rockyListView.getRefreshableView();
		AbsListView actualListView = mRockyListView.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
				
		mRockyListAdapter = new RockyListAdapter<Model>()
		{

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				return realGetView(position, convertView, parent);
			}

			@Override
			public long getItemId(int position)
			{
				return position;
			}

			@Override
			public Model getItem(int position)
			{
				return mListData.get(position);
			}

			@Override
			public int getCount()
			{
				return mListData.size();
			}
		};
		
		mRockyListView.setAdapter(mRockyListAdapter);
		
		// 点击事件
		mRockyListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				realOnItemClick(parent, view, position, id);
			}
		});
	}
	
	/**
	 * 
	 * @param refreshView
	 */
	public void realOnPullDownToRefresh(PullToRefreshBase<AbsListView> refreshView, RockyParams params)
	{
		// Set loading message
		mEmptyView.onLoad();

		String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
				| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		// Do work to refresh the list here.
		// executeOnPullDownGetDataTask(params);
		realOnPullDownExecuteTask(params);
	}

	/**
	 * 
	 * @param refreshView
	 */
	public void realOnPullUpToRefresh(PullToRefreshBase<AbsListView> refreshView, RockyParams params)
	{
		String label = DateUtils.formatDateTime(getContext(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
				| DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

		// Update the LastUpdatedLabel
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);

		// Do work to refresh the list here.
		realOnPullUpExecuteTask(params);
	}

	/**
	 * 第一次执行获取数据
	 * 
	 * @param params
	 */
	public void realExecuteFirstTime(RockyParams params)
	{
		if (mEmptyView != null)
		{
			mEmptyView.onLoad();
		}

		if (mListData.isEmpty() || mListData.size() < mFirstQueryCount)
		{
			// 第一次取到数据后, 只能点击屏幕获取数据; 取得的数据<分页数量, 不允许上下拉.
			mRockyListView.setMode(Mode.DISABLED);
		}

		mRockyParams = params;

		realOnPullDownExecuteTask(params);

		// 默认取一次
		// executeOnPullDownGetDataTask(params);
		// 首次加载未完成时, 不允许发起新的请求
		// rockyListView.setRefreshing(false);
	}

	/**
	 * 执行获取数据的任务
	 */
	private void executeOnPullDownGetDataTask(final RockyParams params)
	{
		new GetDataTask<RockyParams, Void, XModel<Model>>()
		{

			@Override
			protected XModel<Model> doInBackground(RockyParams... params)
			{
				XModel<Model> xModel = null;
				if (params == null || params.length == 0)
				{
					xModel = realOnPullDown(null);
				}
				else
				{
					xModel = realOnPullDown(params[0]);
				}

				if (xModel != null && !xModel.isEmpty() && xModel.mStatus == Rocky.STATUS_OK)
				{
					// 请求数据的起始位置发生变化
					mOffset += mCount;
				}

				return xModel;
			}

			@Override
			protected void onPostExecute(XModel<Model> result)
			{
				if (result == null)
				{
					mEmptyView.onDataEmpty();
				}
				else if (result.mStatus == Rocky.STATUS_NOT_INTERNET)
				{
					// 网络错误
					if (mListData == null || mListData.isEmpty())
					{
						mEmptyView.onNetworkError();
					}
					else
					{
						mEmptyView.onDataEmpty();
						mRockyListAdapter.notifyDataSetChanged();
					}
				}
				else if (result.mStatus == Rocky.STATUS_OK && result.mList != null)
				{
					// 正常取到结果, 有数据或没数据
					if (result.mList.isEmpty())
					{
						mEmptyView.onDataEmpty();
					}
					else
					{
						mListData.addAll(0, result.mList);

						if (mListData.isEmpty() || mListData.size() < mFirstQueryCount)
						{
							// 第一次取到数据后, 恢复ListView第一次加载时的模式, 没有取到时, 只能点击屏幕获取数据.
							// setPullMode(mPullMode);

							mRockyListView.setMode(Mode.DISABLED);
						}
						else
						{
							setPullMode(mPullMode);
						}

						mRockyListAdapter.notifyDataSetChanged();
					}
				}
				else
				{
					mEmptyView.onDataEmpty();
					mRockyListAdapter.notifyDataSetChanged();
				}

				// Call onRefreshComplete when the list has been refreshed.
				mRockyListView.onRefreshComplete();

				mRockyParams = params;

				// Call after get data
				realOnPostExecute();

				// 状态置为可加载状态
				mIsLoading = false;

				super.onPostExecute(result);
			}
		}.execute(params);
	}

	/**
	 * 执行获取数据的任务
	 */
	private void executeOnPullUpGetDataTask(final RockyParams params)
	{
		new GetDataTask<RockyParams, Void, XModel<Model>>()
		{

			@Override
			protected XModel<Model> doInBackground(RockyParams... params)
			{
				XModel<Model> xModel = null;
				if (params == null || params.length == 0)
				{
					xModel = realOnPullUp(null);
				}
				else
				{
					xModel = realOnPullUp(params[0]);
				}

				if (xModel != null && !xModel.isEmpty() && xModel.mStatus == Rocky.STATUS_OK)
				{
					// 请求数据的起始位置发生变化
					mOffset += mCount;
				}

				return xModel;
			}

			@Override
			protected void onPostExecute(XModel<Model> result)
			{
				if (result == null)
				{
					showToast(R.string.hint_no_more_data);
				}
				else if (result.mStatus == Rocky.STATUS_NOT_INTERNET)
				{
					// 网络错误
					if (mListData == null || mListData.isEmpty())
					{
						// 表示第一次取数据
						mEmptyView.onNetworkError();
					}
					else
					{
						mEmptyView.onDataEmpty();
						mRockyListAdapter.notifyDataSetChanged();
					}
				}
				else if (result.mStatus == Rocky.STATUS_OK && result.mList != null)
				{
					// 正常取到结果, 有数据或没数据
					mListData.addAll(result.mList);
					mRockyListAdapter.notifyDataSetChanged();
				}
				else
				{
					showToast(R.string.hint_no_more_data);
					mRockyListAdapter.notifyDataSetChanged();
				}

				// Call onRefreshComplete when the list has been refreshed.
				mRockyListView.onRefreshComplete();

				mRockyParams = params;

				// Call after get data
				realOnPostExecute();

				// 状态置为可加载状态
				mIsLoading = false;

				super.onPostExecute(result);
			}
		}.execute(params);
	}

	/**
	 * 由子类决定何时执行任务, 有任务在执行时, 直接返回, 不允许同一时间多次调用.
	 */
	public void realExecuteTask(RockyParams params)
	{
		if (mIsLoading)
		{
			return;
		}
		mIsLoading = true;

		executeOnPullDownGetDataTask(params);
	}

	/**
	 * 下拉执行的工作任务, 有任务在执行时, 直接返回, 不允许同一时间多次调用.
	 */
	public void realOnPullDownExecuteTask(RockyParams params)
	{
		if (mIsLoading)
		{
			return;
		}
		mIsLoading = true;

		executeOnPullDownGetDataTask(params);
	}

	/**
	 * 上拉执行的工作任务, 有任务在执行时, 直接返回, 不允许同一时间多次调用.
	 */
	public void realOnPullUpExecuteTask(RockyParams params)
	{
		if (mIsLoading)
		{
			return;
		}
		mIsLoading = true;

		executeOnPullUpGetDataTask(params);
	}
	
	/**
	 * 返回ListView对象
	 * 
	 * @return
	 */
	public PullToRefreshAdapterViewBase<? extends AbsListView> getRockyListView()
	{
		return mRockyListView;
	}

	/**
	 * 最后一项为可见的情况
	 */
	protected void realOnLastItemVisible()
	{

	}

	/**
	 * 后台取完数据之后, 回调此方法更新UI
	 */
	protected void realOnPostExecute()
	{

	}
	
	/**
	 * 某些情况下, 子类需要添加一个Item至当前已经存在的列表中
	 * 
	 * @param item
	 */
	protected void realAddNewItem(Model item)
	{
		++mOffset;
		mListData.add(0, item);
		mRockyListAdapter.notifyDataSetChanged();

		// Call onRefreshComplete when the list has been refreshed.
		// rockyListView.onRefreshComplete();
	}
	
	/**
	 * 返回当前选项的Item值
	 * 
	 * @param position
	 * @return
	 */
	protected Model realGetItem(int position)
	{
		return mRockyListAdapter.getItem(position);
	}

	/**
	 * 清空所有数据
	 */
	protected void realClearData()
	{
		if (mListData != null)
		{
			synchronized (mListData)
			{
				mListData.clear();
			}
		}
	}

	/**
	 * 返回数据对象
	 * 
	 * @return
	 */
	protected LinkedList<Model> realGetListData()
	{
		//synchronized (mLock)
		{
			return this.mListData;
		}
	}

	/**
	 * 点击事件
	 * 
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	protected void realOnItemClick(AdapterView<?> parent, View view, int position, long id)
	{

	}
	
	/**
	 * 设置刷新策略
	 * 
	 * @param mode
	 */
	public void setPullMode(Mode mode)
	{
		this.mPullMode = mode;
		this.mRockyListView.setMode(mode);
	}

	/**
	 * 返回刷新策略
	 * 
	 * @return
	 */
	public Mode getPullMode()
	{
		return mPullMode;
	}
	
	// ===========================================================================================================
	// ===========================================================================================================
	/**
	 * 交互参数, 具体参数意义, 由各子类与进行约定, 此对象将完整交由子类进行处理.
	 * 
	 * <pre>
	 * 特殊情况, 可直接继承此类进行重写.或者, 定义一个Params<--SimpleParams<--YourCustomParams
	 * 
	 * </pre>
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-10-30
	 */
	public static class XModel<DataType>
	{

		/**
		 * 状态码
		 */
		public int mStatus;

		/**
		 * 描述
		 */
		public String mDesc;

		/**
		 * 数据
		 */
		public List<DataType> mList;

		/**
		 * 
		 * @param status
		 * @param desc
		 * @param list
		 */
		private XModel(int status, String desc, List<DataType> list)
		{
			this.mStatus = status;
			this.mDesc = desc;
			this.mList = list;
		}

		/**
		 * 没数据时, 返回true.
		 * 
		 * @return
		 */
		public boolean isEmpty()
		{
			return this.mList == null || this.mList.isEmpty();
		}

		/**
		 * 空数据集
		 * 
		 * @return
		 */
		public static <T> XModel<T> empty()
		{
			return list(Rocky.STATUS_NOT_DATA, null);
		}

		/**
		 * 网络错误
		 * 
		 * @return
		 */
		public static <T> XModel<T> netError()
		{
			return list(Rocky.STATUS_NOT_INTERNET, null);
		}

		/**
		 * 不明错误
		 * 
		 * @return
		 */
		public static <T> XModel<T> unknown()
		{
			return list(Rocky.STATUS_UNKNOWN, null);
		}

		/**
		 * 正常返回数据
		 * 
		 * @param list
		 * @return
		 */
		public static <T> XModel<T> list(List<T> list)
		{
			return list(Rocky.STATUS_OK, list);
		}

		/**
		 * 返回状态及数据集, 无具体描述
		 * 
		 * @param status 状态
		 * @param list 数据集
		 * @return
		 */
		public static <T> XModel<T> list(int status, List<T> list)
		{
			return list(status, null, list);
		}

		/**
		 * 返回完整的数据结构
		 * 
		 * @param status 状态
		 * @param desc 描述
		 * @param list 数据集
		 * @return
		 */
		public static <T> XModel<T> list(int status, String desc, List<T> list)
		{
			return new XModel<T>(status, desc, list);
		}

	}
	
	/**
	 * 交互参数
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 1.0.0
	 * @date 2014-10-30
	 */
	public static class RockyParams
	{

		public static final int FIRST_QUERY_COUNT = 20;

		private int offset = 0;

		private int count = 20;

		private String text1;
		private String text2;
		private String text3;
		private String text4;
		private String text5;

		private Integer number1;
		private Integer number2;
		private Integer number3;
		private Integer number4;
		private Integer number5;

		private Date date1;
		private Date date2;
		private Date date3;
		private Date date4;
		private Date date5;

		public int getOffset()
		{
			return offset;
		}

		public void setOffset(int offset)
		{
			this.offset = offset;
		}

		public int getCount()
		{
			return count;
		}

		public void setCount(int count)
		{
			this.count = count;
		}

		public String getText1()
		{
			return text1;
		}

		public void setText1(String text1)
		{
			this.text1 = text1;
		}

		public String getText2()
		{
			return text2;
		}

		public void setText2(String text2)
		{
			this.text2 = text2;
		}

		public String getText3()
		{
			return text3;
		}

		public void setText3(String text3)
		{
			this.text3 = text3;
		}

		public String getText4()
		{
			return text4;
		}

		public void setText4(String text4)
		{
			this.text4 = text4;
		}

		public String getText5()
		{
			return text5;
		}

		public void setText5(String text5)
		{
			this.text5 = text5;
		}

		public Integer getNumber1()
		{
			return number1;
		}

		public void setNumber1(Integer number1)
		{
			this.number1 = number1;
		}

		public Integer getNumber2()
		{
			return number2;
		}

		public void setNumber2(Integer number2)
		{
			this.number2 = number2;
		}

		public Integer getNumber3()
		{
			return number3;
		}

		public void setNumber3(Integer number3)
		{
			this.number3 = number3;
		}

		public Integer getNumber4()
		{
			return number4;
		}

		public void setNumber4(Integer number4)
		{
			this.number4 = number4;
		}

		public Integer getNumber5()
		{
			return number5;
		}

		public void setNumber5(Integer number5)
		{
			this.number5 = number5;
		}

		public Date getDate1()
		{
			return date1;
		}

		public void setDate1(Date date1)
		{
			this.date1 = date1;
		}

		public Date getDate2()
		{
			return date2;
		}

		public void setDate2(Date date2)
		{
			this.date2 = date2;
		}

		public Date getDate3()
		{
			return date3;
		}

		public void setDate3(Date date3)
		{
			this.date3 = date3;
		}

		public Date getDate4()
		{
			return date4;
		}

		public void setDate4(Date date4)
		{
			this.date4 = date4;
		}

		public Date getDate5()
		{
			return date5;
		}

		public void setDate5(Date date5)
		{
			this.date5 = date5;
		}

	}



}
