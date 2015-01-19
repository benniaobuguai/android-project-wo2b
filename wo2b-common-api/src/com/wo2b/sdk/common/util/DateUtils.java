package com.wo2b.sdk.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;

import com.wo2b.sdk.assistant.log.Log;

/**
 * 日期格式化
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-2
 */
public final class DateUtils
{

	private static final String TAG = "DateUtils";

	private DateUtils()
	{

	}

	/**
	 * 返回日期
	 * 
	 * @return
	 */
	public static Date newDate()
	{
		return new Date();
	}

	/**
	 * 返回日期
	 * 
	 * @return
	 */
	public static Date toDate(long millisecond)
	{
		return new Date(millisecond);
	}

	/**
	 * 返回自定义格式的日期, 如: "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd HH:mm:ss",
	 * "yyyy-MM-dd".
	 * 
	 * @param date
	 * @param style
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDateByStyle(String date, String style) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(style, Locale.getDefault());
		return sdf.parse(date);
	}

	/**
	 * 返回自定义格"yyyy-MM-dd HH:mm:ss.SSS"的日期.
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date) throws ParseException
	{
		return parseDateByStyle(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 系统默认的日期格式: "yyyy-MM-dd"
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringDefault(Date date)
	{
		return getStringByStyle(date, "yyyy-MM-dd");
	}

	/**
	 * 日期格式: "yyyy-MM-dd HH:mm"
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringMinute(Date date)
	{
		return getStringByStyle(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 返回日期
	 * 
	 * @return
	 */
	public static String toString(long millisecond)
	{
		return getStringByStyle(new Date(millisecond), "yyyy-MM-dd HH:mm:ss.SSS");
	}

	/**
	 * 返回日期格式为: yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String getCurrentDateDefault()
	{
		return getCurrentDateByStyle("yyyy-MM-dd");
	}

	/**
	 * 返回自定义格式的日期, 如: "yyyy-MM-dd hh:mm:ss", "yyyy-MM-dd HH:mm:ss",
	 * "yyyy-MM-dd".
	 * 
	 * @param style
	 * @return
	 */
	public static String getCurrentDateByStyle(String style)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(style, Locale.getDefault());

		return sdf.format(new Date());
	}

	/**
	 * 系统默认的日期格式: "yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 * @return
	 */
	public static String getStringDefaultFull(Date date)
	{
		return getStringByStyle(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 系统默认的日期格式: "yyyy-MM-dd", {@link #getStringDefault(Date)}
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String getStringDefault(long milliseconds)
	{
		return getStringDefault(new Date(milliseconds));
	}

	/**
	 * 自定义的日期格式
	 * 
	 * @param date
	 * @param style
	 * @return
	 */
	public static String getStringByStyle(long milliseconds, String style)
	{
		return getStringByStyle(new Date(milliseconds), style);
	}

	/**
	 * 自定义的日期格式
	 * 
	 * @param date
	 * @param style
	 * @return
	 */
	public static String getStringByStyle(Date date, String style)
	{
		return new SimpleDateFormat(style, Locale.getDefault()).format(date);
	}

	
	// ======================================================================================================
	// ======================================================================================================
	/**
	 * 一天起始.
	 * 
	 * @param calendar
	 * @return
	 */
	public static Calendar getStartOfCalendar(Calendar calendar)
	{
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	/**
	 * 一天结束.
	 * 
	 * @param calendar
	 * @return
	 */
	public static Calendar getEndOfCalendar(Calendar calendar)
	{
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);

		return calendar;
	}

	/**
	 * 返回当天开始时刻.
	 * 
	 * @return
	 */
	public static long getStartOfToday()
	{
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		return today.getTimeInMillis();
	}

	/**
	 * 返回当天最后一刻
	 * 
	 * @return
	 */
	public static long getEndOfToday()
	{
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 24);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, -1);

		return today.getTimeInMillis();
	}

	/**
	 * 返回周的第一天
	 * 
	 * @return
	 */
	public static long getFirstDayOfWeek()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_WEEK, 1);
		Log.D(TAG, "getFirstDayOfWeek: " + calendar.getTime());

		return calendar.getTimeInMillis();
	}

	/**
	 * 返回周的最后一天
	 * 
	 * @return
	 */
	public static long getLastDayOfWeek()
	{
		Calendar calendar = new GregorianCalendar();

		calendar.set(Calendar.DAY_OF_WEEK, 7);
		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);

		Log.D(TAG, "getLastDayOfWeek: " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 返回月的第一天
	 * 
	 * @return
	 */
	public static long getFirstDayOfMonth()
	{
		Calendar calendar = new GregorianCalendar();

		calendar.set(Calendar.DAY_OF_MONTH, 1);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Log.D(TAG, "getFirstDayOfMonth: " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 返回月的最后一天
	 * 
	 * @return
	 */
	public static long getLastDayOfMonth()
	{
		Calendar calendar = new GregorianCalendar();

		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);

		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);

		Log.D(TAG, "getLastDayOfMonth: " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 返回季度的第一天
	 * 
	 * @return
	 */
	public static long getFirstDayOfSeason()
	{
		Calendar calendar = new GregorianCalendar();
		int month = getQuarterInMonth(calendar.get(Calendar.MONTH), true);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Log.D(TAG, "getFirstDayOfSeason: " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 返回季度的最后一天
	 * 
	 * @return
	 */
	public static long getLastDayOfSeason()
	{
		Calendar calendar = new GregorianCalendar();
		int month = getQuarterInMonth(calendar.get(Calendar.MONTH), false);
		calendar.set(Calendar.MONTH, month + 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);

		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);

		Log.D(TAG, "getLastDayOfSeason: " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 返回当前年的第一天
	 * 
	 * @return
	 */
	public static long getFirstDayOfYear()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.DAY_OF_YEAR, 1);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Log.D(TAG, "getFirstDayOfYear: " + calendar.getTime());
		return calendar.getTimeInMillis();
	}

	/**
	 * 返回当前年的最后一天
	 * 
	 * @return
	 */
	public static long getLastDayOfYear()
	{
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		calendar.set(calendar.get(Calendar.YEAR), 11, 31);

		calendar.set(Calendar.HOUR_OF_DAY, 24);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, -1);

		Log.D(TAG, "getLastDayOfYear: " + calendar.getTime());

		return calendar.getTimeInMillis();
	}

	/**
	 * 返回第几个月份，不是几月. 季度一年四季， 第一季度：2月-4月， 第二季度：5月-7月， 第三季度：8月-10月， 第四季度：11月-1月
	 */
	private static int getQuarterInMonth(int month, boolean isQuarterStart)
	{
		int months[] = { 1, 4, 7, 10 };
		if (!isQuarterStart)
		{
			months = new int[] { 3, 6, 9, 12 };
		}
		if (month >= 2 && month <= 4)
		{
			return months[0];
		}
		else if (month >= 5 && month <= 7)
		{
			return months[1];
		}
		else if (month >= 8 && month <= 10)
		{
			return months[2];
		}
		else
		{
			return months[3];
		}
	}
	
	
	//----------------------------
	/**
	 * 返回date当天开始时刻.
	 * 
	 * @param date
	 * @return
	 */
	public static long getStartOfDate(Date date)
	{
		Calendar today = Calendar.getInstance();
		today.setTime(date);

		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		return today.getTimeInMillis();
	}

	/**
	 * 返回date当天最后时刻.
	 * 
	 * @return
	 */
	public static long getEndOfDate(Date date)
	{
		Calendar today = Calendar.getInstance();
		today.setTime(date);

		today.set(Calendar.HOUR_OF_DAY, 24);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, -1);

		return today.getTimeInMillis();
	}
	
	
	/**
	 * 返回友好的时间提示, 如: 一分钟前
	 * 
	 * @throws ParseException
	 * 
	 * */
	@SuppressLint("SimpleDateFormat")
	public static String getFriendlyTime(Long time) throws ParseException
	{
		Calendar today = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		dateFormat.setTimeZone(TimeZone.getTimeZone("ETC/GMT-8"));
		Date expiry = new Date(time);
		String realDateString = dateFormat.format(expiry);

		long timeDiff = (today.getTimeInMillis() - new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(realDateString)
				.getTime()) / 1000;
		long topSeconds = 60;
		long topMin = topSeconds * 60;
		long topHours = topMin * 24;
		long topDays = topHours * 4;

		if (timeDiff < topSeconds)
		{
			return "一分钟前";
		}
		else if (timeDiff >= 60 && timeDiff < topMin)
		{
			return "" + (int) timeDiff / topSeconds + "分钟前";
		}
		else if (timeDiff >= topMin && timeDiff < topHours)
		{
			int hours = (int) (timeDiff / topMin);
			if (hours >= 2)
			{
				return "" + hours + "小时前";
			}
			else
			{
				return "有 " + hours + "小时";
			}
		}
		else if (timeDiff >= topHours && timeDiff < topDays)
		{
			int days = (int) (timeDiff / topHours);
			if (days >= 2)
			{
				return "" + days + "天前";
			}
			else
			{
				return "有 " + days + "天";
			}
		}
		else
		{
			return realDateString;
		}
	}
	
}
