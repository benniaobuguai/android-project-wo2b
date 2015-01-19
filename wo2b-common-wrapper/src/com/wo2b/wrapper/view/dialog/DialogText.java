package com.wo2b.wrapper.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wo2b.wrapper.R;

/**
 * 显示文本的对话框
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-3
 */
public class DialogText extends RockyDialog
{
	
	private Dialog mDialog;
	
	protected DialogInterface.OnClickListener mPositiveButtonListener;
	protected DialogInterface.OnClickListener mNegativeButtonListener;
	protected DialogInterface.OnClickListener mNeutralButtonListener;

	protected String mPositiveButtonText;
	protected String mNegativeButtonText;
	protected String mNeutralButtonText;

	private TextView mTextView;

	public DialogText(Context context)
	{
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_text);
		mDialog = this;
		
		initButton();

		mTextView = (TextView) findViewById(R.id.text);
		// mTextView.setGravity(Gravity.LEFT);
		mTextView.setText("");
	}

//	public void show(int text)
//	{
//		super.show();
//		mTextView.setText(text);
//	}
//
//	public void show(String text)
//	{
//		super.show();
//		mTextView.setText(text);
//	}

	/**
	 * 提示信息
	 * 
	 * @param text
	 */
	public void setMessage(String text)
	{
		super.setMessage(text);
		if (mTextView != null)
		{
			mTextView.setText(text);
		}
	}

	@Override
	public void show()
	{
		super.show();
		if (getMessage() != null)
		{
			mTextView.setText(getMessage());
		}
	}
	
	public void setPositiveButtonListener(int title, final OnClickListener listener)
	{
		mPositiveButtonText = getResources().getString(title);
		mPositiveButtonListener = listener;
	}

	public void setNegativeButtonListener(int title, final OnClickListener listener)
	{
		mNegativeButtonText = getResources().getString(title);
		mNegativeButtonListener = listener;
	}

	public void setNeutralButtonListener(int title, final OnClickListener listener)
	{
		mNeutralButtonText = getResources().getString(title);
		mNeutralButtonListener = listener;
	}

	public void setPositiveButtonListener(String title, final OnClickListener listener)
	{
		mPositiveButtonText = title;
		mPositiveButtonListener = listener;
	}

	public void setNegativeButtonListener(String title, final OnClickListener listener)
	{
		mNegativeButtonText = title;
		mNegativeButtonListener = listener;
	}

	public void setNeutralButtonListener(String title, final OnClickListener listener)
	{
		mNeutralButtonText = title;
		mNeutralButtonListener = listener;
	}

	/**
	 * Init Button.
	 */
	public void initButton()
	{
		if (mPositiveButtonText != null)
		{
			Button btnPositive = (Button) findViewById(R.id.btn_positive);
			btnPositive.setText(mPositiveButtonText);
			if (mPositiveButtonListener != null)
			{
				btnPositive.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						mPositiveButtonListener.onClick(mDialog, DialogInterface.BUTTON_POSITIVE);
					}
				});
			}
		}
		else
		{
			((Button) findViewById(R.id.btn_positive)).setVisibility(View.GONE);
		}

		if (mNeutralButtonText != null)
		{
			Button btnNeutral = (Button) findViewById(R.id.btn_neutral);
			btnNeutral.setText(mNeutralButtonText);
			if (mNeutralButtonListener != null)
			{
				btnNeutral.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						mNeutralButtonListener.onClick(mDialog, DialogInterface.BUTTON_NEUTRAL);
					}
				});
			}
		}
		else
		{
			((Button) findViewById(R.id.btn_neutral)).setVisibility(View.GONE);
		}

		if (mNegativeButtonText != null)
		{
			Button btnNegative = (Button) findViewById(R.id.btn_negative);
			btnNegative.setText(mNegativeButtonText);
			if (mNegativeButtonListener != null)
			{
				btnNegative.setOnClickListener(new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						mNegativeButtonListener.onClick(mDialog, DialogInterface.BUTTON_NEGATIVE);
					}
				});
			}
		}
		else
		{
			((Button) findViewById(R.id.btn_negative)).setVisibility(View.GONE);
		}
	}

	// ===============================================================================
	// ===============================================================================
	// 不推荐直接使用下面的方法
	public void setPositiveButtonListener(DialogInterface.OnClickListener listener)
	{
		this.mPositiveButtonListener = listener;
	}

	public void setNegativeButtonListener(DialogInterface.OnClickListener listener)
	{
		this.mNegativeButtonListener = listener;
	}

	public void setNeutralButtonListener(DialogInterface.OnClickListener listener)
	{
		this.mNeutralButtonListener = listener;
	}

	public String getPositiveButtonText()
	{
		return mPositiveButtonText;
	}

	public void setPositiveButtonText(String text)
	{
		this.mPositiveButtonText = text;
	}

	public String getNegativeButtonText()
	{
		return mNegativeButtonText;
	}

	public void setNegativeButtonText(String text)
	{
		this.mNegativeButtonText = text;
	}

	public String getNeutralButtonText()
	{
		return mNeutralButtonText;
	}

	public void setNeutralButtonText(String text)
	{
		this.mNeutralButtonText = text;
	}
	
}
