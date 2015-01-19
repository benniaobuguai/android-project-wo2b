package com.wo2b.tu123.ui.view;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.wo2b.tu123.R;


/**
 * 自定义单行的EditText
 * 
 * @author Rocky
 * 
 */
public class SingleLineEditText extends EditText
{
	
	private Context mContext;
	
	private WindowManager mWindowManager;
	
	private boolean mIsSoftInputAuto = false;
	
	public SingleLineEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(context);
	}
	
	public SingleLineEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(context);
	}
	
	public SingleLineEditText(Context context)
	{
		super(context);
		initView(context);
	}
	
	private void initView(Context context)
	{
		this.mContext = context;
		this.mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		
		// 相关属性
		this.setBackgroundResource(R.drawable.rocky_selector_edittext);
		this.setSingleLine(true);
		this.setImeOptions(EditorInfo.IME_ACTION_DONE);
		//this.setSelectAllOnFocus(true);
		//this.setFocusable(true);
	}
	
	@Override
	protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect)
	{
		if(mIsSoftInputAuto)
		{
			// 100毫秒后自动调出软键盘
			showSoftInputDelay(100);
		}
		
		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}
	
	/**
	 * 设置是否自动弹出软键盘, 如果需要在XML中实现此功能, 可能需要加入自定义属性.
	 * 
	 * @param isAuto
	 */
	public void setSoftInputAuto(boolean isAuto)
	{
		this.mIsSoftInputAuto = isAuto;
	}
	
	/**
	 * 显示软键盘
	 * 
	 * @param millisecond
	 */
	private void showSoftInputDelay(long millisecond)
	{
		Timer timer = new Timer();
		timer.schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(0,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, millisecond);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		DisplayMetrics dm = new DisplayMetrics();
		mWindowManager.getDefaultDisplay().getMetrics(dm);
		
		final int fontHeight = getFontHeight(this.getTextSize());
		int heigth = fontHeight + 20;// 字体高度再加20px.
		
		int width = dm.widthPixels;
		
		int measuredWidth = adjustSize(width, widthMeasureSpec);
		int measuredHeight = adjustSize(heigth, heightMeasureSpec);
		
		setMeasuredDimension(measuredWidth, measuredHeight);
	}
	
	/**
	 * 返回字体高度
	 * 
	 * @param textSize
	 * @return
	 */
	public int getFontHeight(float textSize)
	{
		Paint paint = new Paint();
		paint.setTextSize(textSize);
		FontMetrics fm = paint.getFontMetrics();
		
		return (int) Math.ceil(fm.descent - fm.top) + 2;
	}
	
	/**
	 * 返回适配后的尺寸
	 * 
	 * @param size
	 * @param measureSpec
	 * @return
	 */
	public static int adjustSize(int size, int measureSpec)
	{
		int result = size;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		
		switch (specMode)
		{
			case MeasureSpec.UNSPECIFIED:
			{
				result = size;
				break;
			}
			case MeasureSpec.AT_MOST:
			{
				result = Math.min(size, specSize);
				break;
			}
			case MeasureSpec.EXACTLY:
			{
				result = specSize;
				break;
			}
		}
		
		return result;
	}
	
}
