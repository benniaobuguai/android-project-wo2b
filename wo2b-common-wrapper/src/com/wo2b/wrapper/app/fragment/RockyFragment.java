package com.wo2b.wrapper.app.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.wrapper.R;

/**
 * RockyFragment
 * 
 * @author Rocky
 * 
 */
public abstract class RockyFragment extends Fragment
{

	private static final String TAG = "Rocky.Fragment";

	// --------------------------------------------------------------------
	// ------------------------- Common message code ----------------------

	// --------------------------------------------------------------------

	protected Context mContext;
	
	private Activity mRockyActivity;

	private Toast mToast;

	/** Ui Handler. */
	private Handler mUiHandler;
	
	/** Sub/Worker Handler. */
	private Handler mSubHandler;
	
	/** Sub/Worker Thread. */
	private HandlerThread mSubThread;

	private byte[] mAttachActivityLock = new byte[0];
	
	@Override
	public void onAttach(Activity activity)
	{
		super.onAttach(activity);
		synchronized (mAttachActivityLock)
		{
			Log.I("info", "onAttach-->activity: " + activity);
			mRockyActivity = activity;
		}
	}

	@Override
	public void onDetach()
	{
		Log.I("info", "onDetach-->activity: " + mRockyActivity);
		super.onDetach();
		mRockyActivity = null;
	}

	public final Activity getRockyActivity()
	{
		Log.I("info", "getRockyActivity1-->activity: " + mRockyActivity);
		synchronized (mAttachActivityLock)
		{
			Log.I("info", "getRockyActivity2-->activity: " + mRockyActivity);
			return mRockyActivity;
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext = getActivity();

		mSubThread = new HandlerThread(this.getClass().getName());
		// mSubThread.setPriority(Thread.MIN_PRIORITY); //设置线程优先级
		mSubThread.start();

		mUiHandler = new Handler(getActivity().getMainLooper(), mUiHandlerCallback);

		mSubHandler = new Handler(mSubThread.getLooper(), mSubHandlerCallback);
	}
	
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ActionBar Handler &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

	/**
	 * 返回ActionBarActivity
	 * 
	 * @return
	 */
	public ActionBarActivity getActionBarActivity()
	{
		return ((ActionBarActivity) getActivity());
	}

	/**
	 * 设置标题
	 * 
	 * @param resId
	 */
	public void setActionBarTitle(int resId)
	{
		getActionBarActivity().getSupportActionBar().setTitle(resId);
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setActionBarTitle(CharSequence title)
	{
		getActionBarActivity().getSupportActionBar().setTitle(title);
	}

	/**
	 * 设置Home图标
	 * 
	 * @param resId
	 */
	public void setActionBarIcon(int resId)
	{
		getActionBarActivity().getSupportActionBar().setIcon(resId);
	}

	/**
	 * 设置Home图标
	 * 
	 * @param icon
	 */
	public void setActionBarIcon(Drawable icon)
	{
		getActionBarActivity().getSupportActionBar().setIcon(icon);
	}

	/**
	 * 设置显示方式
	 * 
	 * @param options
	 * @param mask
	 */
	public void setActionBarDisplayOptions(int options, int mask)
	{
		getActionBarActivity().getSupportActionBar().setDisplayOptions(options, mask);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (item.getItemId() == android.R.id.home)
		{
			// 默认对Home区域做"返回"处理.
			onActionBarBackClick();
		}
		else if (item.getItemId() == R.id.setting)
		{
			onActionBarSettingClick();
		}
		else if (item.getItemId() == R.id.add)
		{
			onActionBarAddClick();
		}
		else if (item.getItemId() == R.id.edit)
		{
			onActionBarEditClick();
		}
		else if (item.getItemId() == R.id.ok)
		{
			onActionBarOkClick();
		}
		else if (item.getItemId() == R.id.menu)
		{
			onActionBarMenuClick();
		}
		else if (item.getItemId() == R.id.search)
		{
			onActionBarSearchClick();
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * ActionBar的返回操作, 依赖于是否已经重写onActionBarHomeClick().
	 * onActionBarHomeClick()返回true, 则表示已经消费引事件, 反之, 则由父类统一处理.
	 */
	protected void onActionBarBackClick()
	{
		if (!onActionBarHomeClick())
		{
			getActionBarActivity().finish();
		}
	}

	/**
	 * 点击Home
	 */
	protected boolean onActionBarHomeClick()
	{
		return false;
	}

	/**
	 * 点击设置
	 */
	protected void onActionBarSettingClick()
	{

	}

	/**
	 * 点击添加
	 */
	protected void onActionBarAddClick()
	{

	}

	/**
	 * 点击编辑
	 */
	protected void onActionBarEditClick()
	{

	}

	/**
	 * 点击确认
	 */
	protected void onActionBarOkClick()
	{

	}

	/**
	 * 点击菜单
	 */
	protected void onActionBarMenuClick()
	{

	}

	/**
	 * 点击搜索
	 */
	protected void onActionBarSearchClick()
	{

	}

	/**
	 * 是否需要显示ActionBar, hide()后会延时一会再完全消失.
	 * 
	 * @return
	 */
	protected boolean hasActionBar()
	{
		return true;
	}

	/**
	 * Common Resource会包含子系统的通用功能模块, 需要获取子应用的预先设定的图标; 如果子系统没有设定好图标, 则使用默认Logo.
	 * 
	 * @return
	 */
	public int getApplicationActionBarIcon()
	{
		int drawableId = -1;
		try
		{
			drawableId = this.getResources().getIdentifier("actionbar_icon", "drawable", getActivity().getPackageName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			drawableId = R.drawable.wo2b_logo;
		}

		return drawableId;
	}

	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& ActionBar Handler &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

	@Override
	public void onResume()
	{
		super.onResume();
	}

	@Override
	public void onPause()
	{
		super.onPause();
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
	}

	@Override
	public void onDestroy()
	{
		if (mSubThread != null)
		{
			mSubThread.quit();
			mSubThread = null;
		}

		super.onDestroy();
	}

	public Context getContext()
	{
		return mContext;
	}

	protected Handler getUiHandler()
	{
		return mUiHandler;
	}

	protected Handler getSubHandler()
	{
		return mSubHandler;
	}

	private Handler.Callback mUiHandlerCallback = new Handler.Callback()
	{

		@Override
		public boolean handleMessage(Message msg)
		{
			return uiHandlerCallback(msg);
		}
	};

	private Handler.Callback mSubHandlerCallback = new Handler.Callback()
	{

		@Override
		public boolean handleMessage(Message msg)
		{
			return subHandlerCallback(msg);
		}
	};

	/**
	 * UI更新请求消息处理函数
	 * 
	 * 子类可重载此函数，已处理自己的私有消息
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean uiHandlerCallback(Message msg)
	{
		Log.D(TAG, "uiHandlerCallback");
		return false;
	}

	/**
	 * 与界面无关的操作消息处理函数
	 * 
	 * 子类可重载此函数，已处理自己的私有消息
	 * 
	 * @param msg
	 * @return
	 */
	protected boolean subHandlerCallback(Message msg)
	{
		Log.D(TAG, "subHandlerCallback");
		return false;
	}

	/**
	 * Init view.
	 */
	protected void initView(View view)
	{

	}

	/**
	 * Set default value to target view, or other object.
	 */
	protected void setDefaultValues()
	{

	}

	/**
	 * Bind events to target view.
	 */
	protected void bindEvents()
	{

	}

	public void showToast(String info)
	{
		if (mToast == null)
		{
			mToast = Toast.makeText(getActivity(), info, Toast.LENGTH_SHORT);
		}
		else
		{
			mToast.setText(info);
		}

		mToast.show();
	}

	public void showToast(int resId)
	{
		if (mToast == null)
		{
			mToast = Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT);
		}
		else
		{
			mToast.setText(resId);
		}

		mToast.show();
	}

	public void showToastOnUiThread(final int resId)
	{
		getActivity().runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				showToast(resId);
			}
		});
	}

	public void showToastOnUiThread(final String info)
	{
		getActivity().runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				showToast(info);
			}
		});
	}
	
	public void showToastOnUiThread(final int code, final String message)
	{
		getActivity().runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				showToast("[" + code + ", " + message + "]");
			}
		});
	}
	
	// --------------- Normal Functions End ---------------------------------
//	protected XSearchBox getSearchBox()
//	{
//		return new XSearchBox(mContext);
//	}
	
}
