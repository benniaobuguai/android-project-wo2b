package com.wo2b.xxx.webapp.manager.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import opensource.component.de.greenrobot.event.EventBus;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wo2b.sdk.assistant.event.RockyEvent;
import com.wo2b.sdk.assistant.log.Log;
import com.wo2b.sdk.common.util.DeviceInfoManager;
import com.wo2b.sdk.common.util.io.IOUtils;
import com.wo2b.sdk.core.RockyConfig;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.sdk.core.security.RockySecurity;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.user.gold.UserGoldManager;
import com.wo2b.xxx.webapp.openapi.impl.UserOpenApi;
import com.wo2b.xxx.webapp.openapi.security.Wo2bSecurity;

/**
 * 用户管理类, 登录成功后, 发送一个广播.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class UserManager
{
	
	private static final String TAG = "User.UserManager";

	private static volatile UserManager mInstance = null;

	private User mUserInfo = null;

	private boolean mIsLogin = false;

	private UserGoldManager mUserGoldManager = null;

	private UserOpenApi mUserOpenApi;

	private UserManager()
	{
		mUserGoldManager = new UserGoldManager();
		mUserOpenApi = new UserOpenApi();
	}
	
	public static final UserManager getInstance()
	{
		if (mInstance == null)
		{
			synchronized (UserManager.class)
			{
				if (mInstance == null)
				{
					mInstance = new UserManager();
				}
			}
		}

		return mInstance;
	}

	/**
	 * 发送广播
	 * 
	 * @param action
	 */
	public void sendBroadcast(String action)
	{
		Intent intent = new Intent();
		intent.setAction(action);

		RockySdk.getInstance().getContext().sendBroadcast(intent);
	}

	/**
	 * 通知各个应用系统金币发生变化
	 * 
	 * @param userGold
	 */
	public void sendIntegralBroadcast(UserGold userGold)
	{

	}

	/**
	 * 用户信息接口
	 * 
	 * @param userId 用户ID
	 * @return
	 */
	public User getUserInfo(long userId)
	{
		return null;
	}
	
	/**
	 * 登录接口
	 * 
	 * @param pkgname 当前应用程序的包名
	 * @param username 用户名
	 * @param password 密码
	 */
	public void login(String username, String password)
	{
		login(username, password, null);
	}

	/**
	 * 登录接口
	 * 
	 * @param username
	 * @param password
	 * @param loginCallback
	 */
	public void login(String username, String password, final Wo2bResHandler<User> loginCallback)
	{
		mUserOpenApi.login(username, password, new Wo2bResHandler<User>()
		{

			@Override
			public void onProgress(int bytesWritten, int totalSize)
			{
				super.onProgress(bytesWritten, totalSize);
				// 正在登录
			}

			@Override
			public void onSuccess(int code, User result)
			{
				// 登录成功
				mIsLogin = true;
				mUserInfo = result;

				// 保存用户信息至本地
				saveToLocal(mUserInfo);

				// 保存金币信息到本地
				// UserGold userGold = mUserInfo.getUserGold();
				// mUserGoldManager.saveToLocal(userGold);

				Message message = new Message();
				message.what = RockyEvent.USER_LOGIN_OK;
				message.obj = mUserInfo;
				EventBus.getDefault().post(message);

				if (loginCallback != null)
				{
					loginCallback.onSuccess(code, result);
				}
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				Message message = new Message();
				message.what = RockyEvent.USER_LOGIN_FAIL;
				message.arg1 = code;
				message.obj = msg;
				EventBus.getDefault().post(message);

				if (loginCallback != null)
				{
					loginCallback.onFailure(code, msg, throwable);
				}
			}

		});
	}

	/**
	 * 登出接口
	 * 
	 */
	public void logout()
	{
		mUserOpenApi.logout(new Wo2bResHandler<Void>()
		{

			@Override
			public void onSuccess(int code, Void result)
			{
				// 登出
				mIsLogin = false;
				mUserInfo = null;

				// 删除本地的用户记录
				deleteUserInfoFile();

				Message message = new Message();
				message.what = RockyEvent.USER_LOGOUT_OK;
				EventBus.getDefault().post(message);
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				Log.D(TAG, "Logout failed!!!");
				// 登出
				mIsLogin = false;
				mUserInfo = null;

				// 删除本地的用户记录
				deleteUserInfoFile();

				Message message = new Message();
				message.what = RockyEvent.USER_LOGOUT_OK;
				EventBus.getDefault().post(message);
			}
		});
	}

	/**
	 * 修改用户个人信息接口
	 * 
	 * @param nickname 昵称
	 * @param email 邮箱
	 * @param tel 手机号码
	 * @return
	 */
	public User modifyUserInfo(String nickname, String email, String tel)
	{
		return null;
	}

	/**
	 * 返回内存中的用户信息
	 * 
	 * @return
	 */
	public User getMemoryUser()
	{
		return mUserInfo;
	}

	/**
	 * 返回内存中的用户名
	 * 
	 * @return
	 */
	public String getMemoryUserName()
	{
		return mUserInfo != null ? mUserInfo.getUsername() : "";
	}

	/**
	 * 设置用户信息
	 * 
	 * @param userInfo
	 */
	public void setUserInfo(User userInfo)
	{
		this.mUserInfo = userInfo;
	}

	/**
	 * 用户是否已经登录
	 * 
	 * @return
	 */
	public boolean isLogin()
	{
		return mIsLogin;
	}

	/**
	 * 设置用户登录状态
	 * 
	 * @param isLogin
	 */
	public void setLogin(boolean isLogin)
	{
		this.mIsLogin = isLogin;
	}

	// -------------------------------------------------------------------------------------------
	// ---------------------- 金币逻辑处理部分
	
	public UserGoldManager getUserGoldManager()
	{
		return mUserGoldManager;
	}
	
	/**
	 * 返回当前金币信息
	 * 
	 * @return
	 */
	@Deprecated
	public UserGold getUserGold()
	{
		UserGold userGold = null;
		if (isLogin())
		{
			// userGold = mUserGoldManager.getUserGold();
		}
		else
		{
			// userGold = mUserGoldManager.getUserGold();
		}

		if (userGold == null)
		{
			// 方便上层UI使用
			userGold = new UserGold();
		}

		return userGold;
	}

//	public void findUserGold(OnGoldChangedListener listener)
//	{
//		if (isLogin())
//		{
//			// 登录金币直接在用户信息里面获取
//			listener.onGoldChanged(mUserGoldManager.getUserGold());
//
//			return;
//		}
//		else
//		{
//			// 没有登录的, 直接获取本地金币信息
//			UserGold userGold = mUserGoldManager.getFromLocal();
//			listener.onGoldChanged(userGold);
//		}
//	}
//
//	/**
//	 * 同步用户金币
//	 */
//	public void syncUserGold()
//	{
//
//	}
//	
//	/**
//	 * 奖励金币
//	 * 
//	 * @param gold
//	 */
//	public boolean awardGold(int gold)
//	{
//		return mUserGoldManager.awardGold(gold);
//	}
//
//	/**
//	 * 消耗金币
//	 * 
//	 * @param gold
//	 * @return
//	 */
//	public boolean spendGold(int gold)
//	{
//		return mUserGoldManager.spendGold(gold);
//	}
	
	// -------------------------------------------------------------------------------------------
	// ---------------------- 文件信息
	/**
	 * 获取本地用户信息
	 * 
	 * @return
	 */
	public User getLocalUser()
	{
		File file = getUserInfoFile();

		InputStream in = null;
		ObjectInputStream ois = null;
		try
		{

			if (!file.exists())
			{
				return null;
			}

			in = new FileInputStream(file);
			ois = new ObjectInputStream(in);
			String encode_json = ois.readUTF();

			// 解密
			String decode_json = RockySecurity.decode_user_info(encode_json);
			User user = JSONObject.parseObject(decode_json, User.class);

			return user;
		}
		// catch (FileNotFoundException e)
		// catch (IOException e)
		catch (Exception e)
		{
			Log.D(TAG, "Read User Local File Error!!!");
			// 防止用户非法操作, 生成用户信息文件出错.
			// 出现异常, 直接删除当前用户文件.
			if (file.exists())
			{
				file.delete();
			}
		}
		finally
		{
			IOUtils.close(ois);
			IOUtils.close(in);
		}

		return null;
	}

	/**
	 * 返回本地的用户名
	 * 
	 * @return
	 */
	public String getLocalUserName()
	{
		User localUser = getLocalUser();

		return localUser != null ? localUser.getUsername() : "";
	}

	/**
	 * 需要进行加密存储用户信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean saveToLocal(User user)
	{
		OutputStream out = null;
		ObjectOutputStream oos = null;
		try
		{
			File file = getUserInfoFile();
			if (!file.exists())
			{
				File parentDir = file.getParentFile();
				if (!parentDir.exists())
				{
					parentDir.mkdirs();
				}

				file.createNewFile();
			}

			out = new FileOutputStream(file);
			oos = new ObjectOutputStream(out);

			String json = JSON.toJSONString(user);
			String encode_json = RockySecurity.encode_user_info(json);
			oos.writeUTF(encode_json);
			// oos.writeObject(user);

			return true;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			IOUtils.close(oos);
			IOUtils.close(out);
		}

		return true;
	}

	/**
	 * 删除本地用户信息文件
	 */
	public boolean deleteUserInfoFile()
	{
		File file = getUserInfoFile();
		if (file.exists())
		{
			return file.delete();
		}

		return false;
	}

	/**
	 * 用户信息存放的文件
	 * 
	 * @return
	 */
	public File getUserInfoFile()
	{
		return new File(RockyConfig.getWo2bTempDir() + "/14fkklk7r0s77uiio41r86vut");
	}
	
	
	// -----------------------------------------------------------------------------
	// -------------------------- 注册模块
	/**
	 * 用户注册接口
	 * 
	 * @param username 用户名
	 * @param pwd1 密码
	 * @param pwd2 确认密码
	 * @param email 邮箱地址
	 * @param pkgname 当前应用程序的包名
	 * @param mac Mac地址
	 * @param deviceId 设备唯一ID
	 */
	public void register(final String username, final String pwd1, final String pwd2, final String email,
			String mac, String deviceId, Wo2bResHandler<Void> wo2bResHandler)
	{
		mUserOpenApi.register(username, pwd1, pwd2, email, mac, deviceId, wo2bResHandler);
	}

	/**
	 * 重置密码
	 * 
	 * @param username
	 * @param password
	 * @param wo2bResHandler
	 */
	public void resetPwd(Context context, String username, String password, Wo2bResHandler<Void> wo2bResHandler)
	{
		String mac = Wo2bSecurity.encodeText(DeviceInfoManager.getMacAddress(context));
		String deviceId = Wo2bSecurity.encodeText(DeviceInfoManager.getDeviceId(context));
		mUserOpenApi.resetPwd(username, password, mac, deviceId, wo2bResHandler);
	}

	/**
	 * 发送邮件找回密码
	 * 
	 * @param email
	 * @param wo2bResHandler
	 */
	public void sendResetPwdEmail(String email, Wo2bResHandler<Void> wo2bResHandler)
	{
		mUserOpenApi.sendResetPwdEmail(email, wo2bResHandler);
	}

}
