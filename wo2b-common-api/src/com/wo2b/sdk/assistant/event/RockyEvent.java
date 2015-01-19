package com.wo2b.sdk.assistant.event;

/**
 * 消息中心
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class RockyEvent
{

	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// ### System Module Event
	public static final int SYSTEM_FIRST = 1000000;
	
	
	
	
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
