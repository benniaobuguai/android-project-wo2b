package com.wo2b.wrapper.preference;

/**
 * 
 * @author Rocky
 * 
 */
public interface RockyKeyValues
{

	public interface Values
	{

		public static final String PREF_SLEEP_MODE_QUIT = "exit";
		public static final String PREF_SLEEP_MODE_STOP = "stop";
		public static final String PREF_SLEEP_MODE_DEFAULT = PREF_SLEEP_MODE_QUIT;

	}

	public interface Keys
	{
		
		/** 进入密码 */
		public static final String ENTRY_PASSWORD = "entry_password";

		/** 睡眠模式 */
		public static final String PREF_SLEEP_MODE = "sleep_mode";

		/** 使用次数 */
		public static final String PREF_USE_COUNT = "use_count";
		
		
		
		
		/** 皮肤 */
		public static final String PREF_BG = "sys_bg";

		/** 皮肤标题 */
		public static final String PREF_BG_TITLE = "sys_bg_title";

		/** 皮肤标志 */
		public static final String PREF_BG_FLAG = "sys_bg_flag";

	}

}