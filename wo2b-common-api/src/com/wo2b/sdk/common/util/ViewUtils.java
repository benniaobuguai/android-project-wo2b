package com.wo2b.sdk.common.util;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 常用视图工具类
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-10-22
 */
public class ViewUtils
{

	/**
	 * 常用于ListView|GridView获取ListItem的视图
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T get(View view, int id)
	{
		SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
		if (viewHolder == null)
		{
			viewHolder = new SparseArray<View>();
			view.setTag(viewHolder);
		}
		View childView = viewHolder.get(id);
		if (childView == null)
		{
			childView = view.findViewById(id);
			viewHolder.put(id, childView);
		}

		return (T) childView;
	}
	
	/**
	 * findViewById
	 * 
	 * @param view
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T findViewById(View view, int id)
	{
		return (T) view.findViewById(id);
	}

	// ========================================================================================================================
	// ### 软键盘常用API
	// ========================================================================================================================
	/**
	 * 100毫秒后, 弹出软件盘.
	 * 
	 * @param uiHandler
	 * @param context
	 * @param tv
	 */
	public static void showSoftInputFast(final Handler uiHandler, final Context context, final TextView tv)
	{
		showSoftInputDelay(uiHandler, context, tv, 100);
	}

	/**
	 * 0毫秒后, 弹出软件盘.
	 * 
	 * @param uiHandler
	 * @param context
	 * @param tv
	 */
	public static void showSoftInputFastest(final Handler uiHandler, final Context context, final TextView tv)
	{
		showSoftInputDelay(uiHandler, context, tv, 0);
	}

	/**
	 * 自定义delayMillis后, 弹出软件盘.
	 * 
	 * @param uiHandler
	 * @param context
	 * @param tv
	 * @param delayMillis
	 */
	public static void showSoftInputDelay(final Handler uiHandler, final Context context, final TextView tv,
			long delayMillis)
	{
		uiHandler.postDelayed(new Runnable()
		{

			@Override
			public void run()
			{
				if (isTextViewEmpty(tv))
				{
					showSoftInput(context, tv);
				}
			}
		}, delayMillis);
	}

	/**
	 * 弹出软件盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void showSoftInput(Context context, View view)
	{
		InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.showSoftInput(view, 0);
	}

	/**
	 * 屏蔽输入法, 对于当前输入框(要进行深入的测试)
	 * 
	 * @param activity
	 * @param editText
	 */
	@Deprecated
	public static void hideSoftInput(Activity activity, EditText editText)
	{
		if (android.os.Build.VERSION.SDK_INT <= 100)
		{
			// 4.0
			editText.setInputType(InputType.TYPE_NULL);
		}
		else
		{
			activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try
			{
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(editText, false);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 隐藏输入法, 在Activity的维度
	 * 
	 * @param activity
	 */
	public static void hideSoftInput(Activity activity)
	{
		try
		{
			((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
					activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		catch (Exception e)
		{
			// 避免可能不对手机的兼容异常
			e.printStackTrace();
		}
	}
	
	/**
	 * 开启屏蔽的输入法
	 * 
	 * @param activity
	 * @param editText
	 */
	public static void openSoftInput(Activity activity, EditText editText)
	{
		if (android.os.Build.VERSION.SDK_INT <= 10)
		{
			// 4.0
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
		}
		else
		{
			activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
			try
			{
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(editText, true);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 判断TextView/EditView是否内容是否空
	 * 
	 * @param tv
	 * @return
	 */
	public static boolean isTextViewEmpty(TextView tv)
	{
		if (tv == null)
		{
			throw new NullPointerException("TextView can not be null.");
		}
		if (tv.getText() != null && TextUtils.isEmpty(tv.getText().toString()))
		{
			return true;
		}

		return false;
	}

	// ### End 软键盘常用API

	// ========================================================================================================================
	// ### 单位转换
	// ========================================================================================================================
	/**
	 * dip转换成px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转换成dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale（DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale（DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (spValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale（DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(float pxValue, float scale)
	{
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale（DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(float dipValue, float scale)
	{
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale（DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale)
	{
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale（DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale)
	{
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 返回字体高度
	 * 
	 * @param textSize
	 * @return
	 */
	public static int getFontHeight(float textSize)
	{
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		FontMetrics fm = paint.getFontMetrics();

		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}
	// ### End 单位转换

	/**
	 * 计算ListView的高度, 重置ListView的高度.
	 * 
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView)
	{
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
		{
			return;
		}

		View listItem = null;
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++)
		{
			listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

}
