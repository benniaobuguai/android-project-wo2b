package com.wo2b.sdk.bus;

import opensource.component.otto.Bus;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * 事件总线
 * 
 * @author 笨鸟不乖
 * 
 */
public class GBus
{

	private static volatile GBus mInstance = null;

	private Bus mEventBus = null;

	private Handler mHandler = null;

	private GBus()
	{
		mEventBus = new Bus();
		mHandler = new Handler();
	}

	private static GBus getInstance()
	{
		if (mInstance == null)
		{
			synchronized (GBus.class)
			{
				if (mInstance == null)
				{
					mInstance = new GBus();
				}
			}
		}

		return mInstance;
	}

	private Bus getEventBus()
	{
		return this.mEventBus;
	}

	/**
	 * Handler
	 * 
	 * @return
	 */
	private Handler getHandler()
	{
		return this.mHandler;
	}

	/**
	 * Registers all handler methods on {@code object} to receive events and producer methods to provide events.
	 * 
	 * @param object
	 */
	public static void register(Object object)
	{
		getInstance().getEventBus().register(object);
	}

	/**
	 * Unregisters all producer and handler methods on a registered {@code object}.
	 * 
	 * @param object
	 */
	public static void unregister(Object object)
	{
		getInstance().getEventBus().unregister(object);
	}

	/**
	 * Posts an event to all registered handlers
	 * 
	 * @param msg
	 */
	public static void post(final Message msg)
	{
		if (Looper.myLooper() == Looper.getMainLooper())
		{
			getInstance().getEventBus().post(msg);
		}
		else
		{
			if (getInstance().getHandler() != null)
			{
				getInstance().getHandler().post(new Runnable()
				{

					@Override
					public void run()
					{
						post(msg);
					}
				});
			}

		}
	}

	/**
	 * Posts an event to all registered handlers
	 * 
	 * @param what
	 */
	public static void post(int what)
	{
		Message msg = new Message();
		msg.what = what;
		post(msg);
	}

	/**
	 * Posts an event to all registered handlers
	 * 
	 * @param what
	 * @param obj
	 */
	public static void post(int what, Object obj)
	{
		Message msg = new Message();
		msg.what = what;
		msg.obj = obj;
		post(msg);
	}

	/**
	 * Posts an event to all registered handlers
	 * 
	 * @param what
	 * @param eo
	 */
	public static void post(int what, EO eo)
	{
		Message msg = new Message();
		msg.what = what;
		msg.obj = eo;
		post(msg);
	}

	/**
	 * Event Object
	 * 
	 * @author 笨鸟不乖
	 * 
	 */
	public static class EO
	{

		public int intKey;

		public String strKey;

		public Object object;

		/**
		 * 
		 * @param intKey 事件唯一标识
		 * @param object 数据对象
		 */
		public EO(int intKey, Object object)
		{
			this.intKey = intKey;
			this.object = object;
		}

		/**
		 * 
		 * @param strKey 事件唯一标识
		 * @param object 数据对象
		 */
		public EO(String strKey, Object object)
		{
			this.strKey = strKey;
			this.object = object;
		}

	}

}
