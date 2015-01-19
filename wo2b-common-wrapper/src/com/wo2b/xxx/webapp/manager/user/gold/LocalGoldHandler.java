package com.wo2b.xxx.webapp.manager.user.gold;
//package com.wo2b.xxx.webapp.manager.user.gold;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.wo2b.sdk.common.util.io.StreamUtils;
//import com.wo2b.sdk.core.RockyConfig;
//import com.wo2b.sdk.core.security.RockySecurity;
//import com.wo2b.xxx.webapp.manager.user.UserGold;
//import com.wo2b.xxx.webapp.manager.user.UserManager;
//
///**
// * 本地金币控制器
// * 
// * @author Rocky
// * @email ixueyongjia@gmail.com
// */
//public class LocalGoldHandler implements GoldHandler
//{
//	
//	private static final String TAG = "User.LocalGoldHandler";
//
//	@Override
//	public int queryGold()
//	{
//		UserGold userGold = readFromLocal();
//		if (userGold != null)
//		{
//			return userGold.getGold();
//		}
//
//		return 0;
//	}
//
//	@Override
//	public boolean awardGold(int gold)
//	{
//		UserGold localGold = readFromLocal();
//		if (localGold.getGold() < gold)
//		{
//			// 防止数据出错
//			localGold.setGold(0);
//		}
//
//		localGold.setGold(localGold.getGold() + gold);
//		localGold.setTotalGold(localGold.getTotalGold() + gold);
//
//		return true;
//	}
//
//	@Override
//	public boolean spendGold(int gold)
//	{
//		UserGold localGold = readFromLocal();
//		if (localGold == null || localGold.getGold() < gold)
//		{
//			// 金币不足
//			return false;
//		}
//
//		localGold.setGold(localGold.getGold() - gold);
//		localGold.setTotalGold(localGold.getTotalGold() - gold);
//
//		return true;
//	}
//
//	// -------------------------------------------------------------------------------------------
//	// ---------------------- 文件信息
//	/**
//	 * 获取本地用户信息
//	 * 
//	 * @return
//	 */
//	public UserGold readFromLocal()
//	{
//		try
//		{
//			String encode_string = StreamUtils.readContentFromFile(getLocalFile());
//			String decode_json = RockySecurity.decode_user_gold(encode_string);// 解密
//			UserGold userGold = JSONObject.parseObject(decode_json, UserGold.class);
//
//			return userGold;
//		}
//		catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		catch (Exception e)
//		{
//			// 读取本地用户金币信息出错, 可能是文件信息被篡改
//			e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	/**
//	 * 进行加密存储用户金币信息
//	 * 
//	 * @param userGold
//	 * @return
//	 */
//	public boolean writeToLocal(UserGold userGold)
//	{
//		String json = JSON.toJSONString(userGold);
//		String encode_json = RockySecurity.encode_user_gold(json);
//		try
//		{
//			return StreamUtils.writeContentToFile(getLocalFile(), encode_json);
//		}
//		catch (FileNotFoundException e)
//		{
//			e.printStackTrace();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//
//		return true;
//	}
//	
//	/**
//	 * 删除本地金币信息文件
//	 * 
//	 * @return
//	 */
//	public boolean deleteLocalFile()
//	{
//		File file = getLocalFile();
//		if (file.exists())
//		{
//			return file.delete();
//		}
//
//		return false;
//	}
//
//	/**
//	 * 返回金币信息文件, 登录与非登录金币信息保存在不同的文件中.
//	 * 
//	 * @return
//	 */
//	public File getLocalFile()
//	{
//		if (UserManager.getInstance().isLogin())
//		{
//			return getLocalFileLogin();
//		}
//		else
//		{
//			return getLocalFileLogout();
//		}
//	}
//
//	/**
//	 * 登录的金币信息文件
//	 * 
//	 * @return
//	 */
//	private File getLocalFileLogin()
//	{
//		// OK
//		return new File(RockyConfig.getRockyDataTemp() + "/7axzchjervizildmzb7s0k2ok");
//	}
//
//	/**
//	 * 没有登录的金币信息文件
//	 * 
//	 * @return
//	 */
//	private File getLocalFileLogout()
//	{
//		// NO
//		return new File(RockyConfig.getRockyDataTemp() + "/7axzchjervizildmzb7s0k2no");
//	}
//
//	// 本地积分存储
//
//	// 本地积分上传
//
//	// 同步用户账号积分, 需要更新本地
//
//}
