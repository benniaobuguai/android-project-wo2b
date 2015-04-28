package com.wo2b.wrapper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo2b.wrapper.R;

/**
 * Lable + EditText
 * 
 * @author 笨鸟不乖
 * @email benniaobuguai@gmail.com
 * @version 1.0.0
 * @date 2015-4-12
 */
public class LabelEditText extends LinearLayout
{

	private LabelEditText mRootView;

	private TextView mLabelView;

	private EditText mEditText;

	public LabelEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs)
	{
		mRootView = (LabelEditText) LayoutInflater.from(context).inflate(R.layout.global_lable_edittext, this, true);

		mLabelView = (TextView) mRootView.findViewById(R.id.label);
		mEditText = (EditText) mRootView.findViewById(R.id.editText);

		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.label_edittext);
		CharSequence label = typedArray.getText(R.styleable.label_edittext_wo2b_label);
		int label_ems = typedArray.getInt(R.styleable.label_edittext_wo2b_label_ems, 0);
		CharSequence hint = typedArray.getText(R.styleable.label_edittext_wo2b_hint);
		boolean password = typedArray.getBoolean(R.styleable.label_edittext_wo2b_password, false);

		if (label != null)
		{
			mLabelView.setText(label);
		}
		if (hint != null)
		{
			mEditText.setHint(hint);
			// mEditText.setHintTextColor(Color.parseColor("#ffd2d2d2"));
		}
		if (password)
		{
			mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		if (label_ems > 0)
		{
			// 设置TextView的宽度为N个字符的宽度
			mLabelView.setEms(label_ems);
		}


		typedArray.recycle();

		mRootView.setBackgroundResource(R.drawable.g_input_normal);
		mEditText.setOnFocusChangeListener(new OnFocusChangeListener()
		{

			@Override
			public void onFocusChange(View v, boolean hasFocus)
			{
				if (hasFocus)
				{
					mRootView.setBackgroundResource(R.drawable.g_input_green_focus);
					mLabelView.setTextColor(Color.parseColor("#2ec54b"));
				}
				else
				{
					mRootView.setBackgroundResource(R.drawable.g_input_normal);
					mLabelView.setTextColor(Color.parseColor("#6b6b6b"));
				}
			}
		});

	}

	/**
	 * 
	 * @return
	 */
	public CharSequence getText()
	{
		return this.mEditText.getText();
	}

	/**
	 * 
	 * @param text
	 */
	public void setText(CharSequence text)
	{
		this.mEditText.setText(text);
	}
	
	
	public boolean editTextFocus()
	{
		return this.mEditText.requestFocus();
	}

}
