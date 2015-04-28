package com.wo2b.sdk.bus;

/**
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-2
 */
public class GEvent
{

	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// ### System Module Event
	public static final int SYSTEM_FIRST = 1000000;

	public static final int CONNECT_COMPLETE = SYSTEM_FIRST + 1;

	/**
	 * 网络状态检测
	 */
	public static final int NETWORK_STATUS = SYSTEM_FIRST + 100;
	
	
	
	
	
	
	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// ### User Module Event
	public static final int USER_FIRST = 8000000;

	public static final int USER_LOGIN_CMD = USER_FIRST + 1;
	public static final int USER_LOGIN_OK = USER_FIRST + 2;
	public static final int USER_LOGIN_FAIL = USER_FIRST + 3;
	public static final int USER_LOGOUT_CMD = USER_FIRST + 4;
	public static final int USER_LOGOUT_OK = USER_FIRST + 5;
	public static final int USER_RESET_PWD_CMD = USER_FIRST + 6;
	public static final int USER_RESET_PWD_FAIL = USER_FIRST + 7;
	public static final int USER_RESET_PWD_OK = USER_FIRST + 8;


}
