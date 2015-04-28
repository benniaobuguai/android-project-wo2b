package com.wo2b.wrapper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.wo2b.wrapper.R;
import com.wo2b.wrapper.preference.XPreferenceManager;

/**
 * XPreferenceExtra
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class XPreferenceExtra extends XPreference
{
	
	private Context mContext;
	
	/** XPreference changed callback, as checkbox */
	private OnXPreferenceChangeListener mOnXPreferenceChangeListener;
	
	/** SharedPreferences item key */
	private String mPrefsKey;
	/** SharedPreferences default value */
	private String mPrefsDefault;
	/** SharedPreferences current value */
	private String mPrefsValue;
	
	public XPreferenceExtra(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		initView(attrs);
		setDefaultValues();
		bindEvents();
	}
	
	/**
	 * Init view.
	 * 
	 * @param attrs
	 */
	private void initView(AttributeSet attrs)
	{
		TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.x_preference);
		
		CharSequence prefsKey = typedArray.getText(R.styleable.x_preference_prefs_key);
		CharSequence prefsDefault = typedArray.getText(R.styleable.x_preference_prefs_default);
		if (!TextUtils.isEmpty(prefsKey))
		{
			mPrefsKey = prefsKey.toString();
		}
		if (!TextUtils.isEmpty(prefsDefault))
		{
			mPrefsDefault = prefsDefault.toString();
		}
		
		typedArray.recycle();
	}
	
	private void bindEvents()
	{
		
	}
	
	private void setDefaultValues()
	{
		if (isNeedSaved())
		{
			if (getState() == STATE_CHECKBOX)
			{
				boolean isChecked = Boolean.parseBoolean(mPrefsDefault);
				isChecked = XPreferenceManager.getInstance().getBoolean(mPrefsKey, isChecked);
				if (isChecked)
				{
					setIndicatorSelected(isChecked);
					XPreferenceManager.getInstance().putBoolean(mPrefsKey, isChecked);

					mPrefsValue = isChecked + "";
				}
			}
		}
	}
	
	
	@Override
	public void onPreferenceClick(XPreference rootView, int state)
	{
		//super.onPreferenceClick(rootView, state);
		if (state == STATE_ARROW)
		{
			if (mOnClickListener != null)
			{
				mOnClickListener.onClick(rootView);
			}
		}
		else if (state == STATE_CHECKBOX)
		{
			if (mOnXPreferenceChangeListener != null)
			{
				// Save current setting.
				boolean isSelected = isIndicatorSelected();
				if (isNeedSaved())
				{
					XPreferenceManager.getInstance().putBoolean(mPrefsKey, !isSelected);
					mPrefsValue = !isSelected + "";
				}

				mOnXPreferenceChangeListener.onXPreferenceChanged(rootView, isSelected);
			}
		}
	}
	
	/**
	 * Need to save preferece.
	 * 
	 * @return
	 */
	public boolean isNeedSaved()
	{
		return !TextUtils.isEmpty(mPrefsKey);
	}
	
	/**
	 * Set OnXPreferenceChangeListener, do anything you want.
	 * 
	 * @param l
	 */
	public void setOnXPreferenceChangeListener(OnXPreferenceChangeListener l)
	{
		this.mOnXPreferenceChangeListener = l;
	}
	
	/**
	 * Changed the background for the Indicator.
	 * 
	 */
	public static abstract class OnXPreferenceSelectListener implements OnXPreferenceChangeListener
	{

		public void onXPreferenceChanged(XPreference preference, boolean isChecked)
		{
			TextView checkbox = preference.getIndicator();
			if (onXPreferenceSelected(preference, isChecked) && !isChecked)
			{
				checkbox.setSelected(true);
			}
			else
			{
				checkbox.setSelected(false);
			}
		}

		public abstract boolean onXPreferenceSelected(XPreference preference, boolean isChecked);
	}

	/**
	 * OnXPreferenceChangeListener
	 * 
	 */
	public static interface OnXPreferenceChangeListener
	{
		void onXPreferenceChanged(XPreference preference, boolean isChecked);
	}

	
	
	
	public String getPrefsKey()
	{
		return mPrefsKey;
	}

	public void setPrefsKey(String prefsKey)
	{
		this.mPrefsKey = prefsKey;
	}

	public String getPrefsDefault()
	{
		return mPrefsDefault;
	}

	public void setPrefsDefault(String prefsDefault)
	{
		this.mPrefsDefault = prefsDefault;
	}

	public String getPrefsValue()
	{
		return mPrefsValue;
	}

	public void setPrefsValue(String prefsValue)
	{
		this.mPrefsValue = prefsValue;
	}

}
