package com.wo2b.sdk.view;

import java.lang.reflect.Method;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

public class VerticalTextView extends TextView
{
	public static final int LAYER_TYPE_SOFTWARE = 1;
	private int mHeight;
	private Paint mPaint = new Paint();
	private Path mPath;
	
	public VerticalTextView(Context paramContext, AttributeSet paramAttributeSet)
	{
		super(paramContext, paramAttributeSet);
		this.mPaint.setTextSize(getTextSize());
		this.mPaint.setColor(getTextColors().getDefaultColor());
		this.mPaint.setAntiAlias(true);
		this.mPaint.setTextAlign(Paint.Align.CENTER);
		this.mPath = new Path();
		if (Build.VERSION.SDK_INT > 10)
			;
		try
		{
			Class<?> localClass = getClass();
			Class<?>[] arrayOfClass = new Class[2];
			arrayOfClass[0] = Integer.TYPE;
			arrayOfClass[1] = Paint.class;
			Method localMethod = localClass.getMethod("setLayerType", arrayOfClass);
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = Integer.valueOf(1);
			arrayOfObject[1] = null;
			localMethod.invoke(this, arrayOfObject);
			return;
		}
		catch (Exception localException)
		{
		}
	}
	
	private void drawText(Canvas canvas, String text)
	{
		this.mHeight = getHeight();
		this.mPath.moveTo(getWidth() - (2.0F * getResources().getDisplayMetrics().density), this.mHeight);
		this.mPath.lineTo(getWidth() - (2.0F * getResources().getDisplayMetrics().density), 0.0F);
		canvas.drawTextOnPath(text, this.mPath, 0.0F, 0.0F, this.mPaint);
	}
	
	protected void onDraw(Canvas canvas)
	{
		drawText(canvas, getText().toString());
	}
	
	public void setTextAlignBottom()
	{
		this.mPaint.setTextAlign(Paint.Align.LEFT);
	}
	
	public void setTextColor(int paramInt)
	{
		super.setTextColor(paramInt);
		this.mPaint.setColor(paramInt);
	}
}