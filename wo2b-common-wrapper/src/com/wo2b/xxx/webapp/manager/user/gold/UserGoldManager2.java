package com.wo2b.xxx.webapp.manager.user.gold;
//package com.wo2b.xxx.webapp.manager.user.gold;
//
//import com.wo2b.sdk.common.httpextra.HttpRequestException;
//import com.wo2b.xxx.webapp.manager.user.UserGold;
//import com.wo2b.xxx.webapp.manager.user.UserManager;
//
///**
// * 金币系统门面, 同时封装本地和服务器积分控制, 控制实际处理逻辑.
// * 
// * @author 笨鸟不乖
// * @email ixueyongjia@gmail.com
// */
//public class UserGoldManager2 implements GoldHandler
//{
//
//	private LocalGoldHandler mLocalHandler;
//	private ServerGoldHandler mServerHandler;
//
//	private UserGold mUserGold = new UserGold();
//
//	public UserGoldManager2()
//	{
//		mLocalHandler = new LocalGoldHandler();
//		mServerHandler = new ServerGoldHandler();
//	}
//
//	public UserGold getUserGold()
//	{
//		return mUserGold;
//	}
//
//	public void setGold(UserGold userGold)
//	{
//		this.mUserGold = userGold;
//	}
//
//	/**
//	 * 对于未登录的用户, 初始设定的金币数量
//	 */
//	private static final int DEFAULT_GOLD = 500;
//
//	/**
//	 * 自动产生用户金币信息
//	 */
//	public UserGold autoUserGoldGen()
//	{
//		final UserGold userGold = new UserGold();
//		userGold.setGold(DEFAULT_GOLD);
//		userGold.setTotalGold(DEFAULT_GOLD);
//
//		// 保存至本地
//		new Thread(new Runnable()
//		{
//
//			@Override
//			public void run()
//			{
//				mLocalHandler.writeToLocal(userGold);
//			}
//
//		}).start();
//
//		this.mUserGold = userGold;
//
//		return userGold;
//	}
//	
//	/**
//	 * 刷新用户金币信息, 如: 用户登录/登出
//	 */
//	public void notifyUserGoldChanged()
//	{
//		this.mUserGold = getFromLocal();
//
//		// TODO: 同时需要发送广播, 同步更新各个应用的金币信息.
//	}
//
//	/**
//	 * 返回当前金币数
//	 */
//	@Override
//	public int queryGold()
//	{
//		if (mUserGold == null)
//		{
//			return 0;
//		}
//
//		return mUserGold.getGold();
//	}
//
//	@Override
//	public boolean awardGold(final int gold)
//	{
//		if (mUserGold == null)
//		{
//			return false;
//		}
//
//		mUserGold.setGold(mUserGold.getGold() + gold);
//		mUserGold.setTotalGold(mUserGold.getTotalGold() + gold);
//		saveToLocalInBackground();
//		
//		if (UserManager.getInstance().isLogin())
//		{
//			new Thread(new Runnable()
//			{
//
//				@Override
//				public void run()
//				{
//					try
//					{
//						ServerGoldHandler.awardGold(gold, "");
//					}
//					catch (HttpRequestException e)
//					{
//						e.printStackTrace();
//					}
//				}
//
//			}).start();
//		}
//		
//
//		return true;
//	}
//
//	@Override
//	public boolean spendGold(final int gold)
//	{
//		if (mUserGold == null || mUserGold.getGold() < gold)
//		{
//			return false;
//		}
//
//		mUserGold.setGold(mUserGold.getGold() - gold);
//		saveToLocalInBackground();
//
//		
//		if (UserManager.getInstance().isLogin())
//		{
//			new Thread(new Runnable()
//
//			{
//
//				@Override
//				public void run()
//				{
//					try
//					{
//						ServerGoldHandler.spendGold(gold, "");
//					}
//					catch (HttpRequestException e)
//					{
//						e.printStackTrace();
//					}
//				}
//
//			}).start();
//		}
//		
//		return true;
//	}
//
//	/**
//	 * 保存至本地
//	 */
//	private synchronized void saveToLocalInBackground()
//	{
//		new Thread(new Runnable()
//		{
//
//			@Override
//			public void run()
//			{
//				saveToLocal(mUserGold);
//			}
//
//		}).start();
//	}
//	
//	/**
//	 * 同步至服务器
//	 */
//	private synchronized void synchToServerInBackground(final int gold, final int type)
//	{
//		
//	}
//
//	/**
//	 * 保存至本地
//	 * 
//	 * @param userGold
//	 */
//	public boolean saveToLocal(UserGold userGold)
//	{
//		if (userGold == null)
//		{
//			this.mUserGold = new UserGold();
//		}
//
//		this.mUserGold = userGold;
//		return mLocalHandler.writeToLocal(userGold);
//	}
//
//	/**
//	 * 返回金币信息
//	 * 
//	 * @return
//	 */
//	public UserGold getFromLocal()
//	{
//		this.mUserGold = mLocalHandler.readFromLocal();
//		return this.mUserGold;
//	}
//
//	/**
//	 * 加载本地的金币信息
//	 * 
//	 * @return
//	 */
//	public UserGold loadLocalUserGold()
//	{
//		this.mUserGold = mLocalHandler.readFromLocal();
//		return this.mUserGold;
//	}
//
//}
