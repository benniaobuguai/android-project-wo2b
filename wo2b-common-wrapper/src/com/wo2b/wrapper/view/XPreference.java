package com.wo2b.wrapper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.view.BadgeView;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.preference.XPreferenceManager;

/**
 * XPreference
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class XPreference extends RelativeLayout
{
	
	private Context mContext;
	
	private XPreference mRootView;
	
	/** container */
	private RelativeLayout mContainer;
	
	/** display icon and text */
	private TextView mTitle;
	
	/** display content */
	private TextView mContentView;

	/** right text */
	private TextView mRightText;
	private TextView mBadgeDefaultView;
	private ImageView mDivider;

	/** the indicator aling right */
	private TextView mIndicator;
	private int mState;
	protected static final int STATE_NONE = -1;
	protected static final int STATE_ARROW = STATE_NONE + 1;
	protected static final int STATE_CHECKBOX = STATE_ARROW + 1;
	protected static final int STATE_CUSTOM = 100;

	private boolean mEnabled;
	
	private LinearLayout mHintIcons;
	
	/** XPreference click callback */
	protected OnClickListener mOnClickListener;
	protected OnClickListener mIndicatorClickListener;

	public XPreference(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context;
		init(attrs);
	}
	
	
	public void onPreferenceClick(XPreference rootView, int state)
	{
		if (mOnClickListener != null)
		{
			mOnClickListener.onClick(rootView);
		}
	}
	
	/**
	 * Init view.
	 * 
	 * @param attrs
	 */
	private void init(AttributeSet attrs)
	{
		mRootView = (XPreference) LayoutInflater.from(mContext).inflate(R.layout.x_preference, this);
		mContainer = (RelativeLayout) mRootView.findViewById(R.id.container);
		
		mTitle = (TextView) mRootView.findViewById(R.id.title);
		mContentView = (TextView) mRootView.findViewById(R.id.content);
		mIndicator = (TextView) mRootView.findViewById(R.id.indicator);
		mHintIcons = (LinearLayout) mRootView.findViewById(R.id.hint_icons);
		mRightText = (TextView) mRootView.findViewById(R.id.right_text);
		mBadgeDefaultView = (TextView) this.findViewById(R.id.badge_view);
		mDivider = (ImageView) this.findViewById(R.id.divider);
		
		TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.x_preference);
		
		// Dont need more icon default.
		CharSequence label = typedArray.getText(R.styleable.x_preference_label);
		int label_ems = typedArray.getInteger(R.styleable.x_preference_label_ems, 0);
		if (label_ems > 0)
		{
			// 设置TextView的宽度为N个字符的宽度
			mTitle.setEms(label_ems);
		}

		Drawable icon = typedArray.getDrawable(R.styleable.x_preference_rocky_icon);
		Drawable indicator = typedArray.getDrawable(R.styleable.x_preference_indicator);
		// Drawable indicator = typedArray.getDrawable(R.styleable.x_preference_indicator);
		// <enum name="arrow" value="0" /> as default.
		mState = typedArray.getInt(R.styleable.x_preference_indicator_state, STATE_ARROW);
		// <enum name="one" value="5" /> as default.
		final int position = typedArray.getInt(R.styleable.x_preference_position, 5);
		// show notice or not?
		
		mEnabled = typedArray.getBoolean(R.styleable.x_preference_enabled, true);

		// 分隔线
		boolean divider = typedArray.getBoolean(R.styleable.x_preference_prefs_divider, false);
		if (divider)
		{
			mDivider.setVisibility(View.VISIBLE);
		}
		else
		{
			mDivider.setVisibility(View.GONE);
		}

		setTitle(label);
		setIcon(icon);
		
		setItemIndicator(mState, indicator);
		setItemBackground(mContainer, position);
		
		typedArray.recycle();
		
		mContainer.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				if (mEnabled)
				{
					onPreferenceClick(mRootView, mState);
				}
				else
				{
					//Toast.makeText(mContext, R.string.hint_not_cancle_power, Toast.LENGTH_SHORT).show();
				}
			}
		});
		mIndicator.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mIndicatorClickListener != null)
				{
					mIndicatorClickListener.onClick(v);
				}
			}
		});
	}

	/**
	 * Need to save preferece.
	 * 
	 * @return
	 */
	public boolean isNeedSaved()
	{
		return false;
	}
	
	/**
	 * Set the background to view depend on the position.
	 * 
	 * @param item
	 * @param position
	 */
	protected void setItemBackground(View item, final int position)
	{
		item.setBackgroundResource(R.drawable.selector_preference_grey);

		switch (position)
		{
			case 1:
			{
				// first
				mDivider.setVisibility(View.VISIBLE);
				break;
			}
			case 2:
			{
				// middle
				mDivider.setVisibility(View.VISIBLE);
				break;
			}
			case 3:
			{
				// last
				mDivider.setVisibility(View.GONE);
				break;
			}
			case 4:
			{
				// top
				mDivider.setVisibility(View.VISIBLE);
				break;
			}
			case 5:
			{
				// one
				mDivider.setVisibility(View.GONE);
				break;
			}
			case 6:
			{
				// single
				mDivider.setVisibility(View.GONE);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Illegal XPreference position: " + position);
			}
		}
	}
	
	/**
	 * Set the background to view depend on the position.
	 * 
	 * @param container
	 * @param position
	 */
	protected void setItemIndicator(final int state, Drawable indicator)
	{
		switch (state)
		{
			case STATE_ARROW:
			{
				Drawable drawable = getResources().getDrawable(R.drawable.selector_preference_arrow);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				this.mIndicator.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 4));
				this.mIndicator.setCompoundDrawables(null, null, drawable, null);
				break;
			}
			case STATE_CHECKBOX:
			{
				Drawable drawable = getResources().getDrawable(R.drawable.selector_preference_checkbox);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
				this.mIndicator.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 4));
				this.mIndicator.setCompoundDrawables(null, null, drawable, null);
				break;
			}
			case STATE_NONE:
			{
				this.mIndicator.setCompoundDrawables(null, null, null, null);
				break;
			}
			case STATE_CUSTOM:
			{
				indicator.setBounds(0, 0, indicator.getIntrinsicWidth(), indicator.getIntrinsicHeight());
				this.mIndicator.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 4));
				this.mIndicator.setCompoundDrawables(null, null, indicator, null);
				break;
			}
			default:
			{
				throw new IllegalArgumentException("Illegal XPreference state: " + state);
			}
		}
	}
	
	private SparseIntArray mHintIconArray = new SparseIntArray();
	
	public void addHintIcon(int drawableId)
	{
		if (this.mHintIconArray.indexOfKey(drawableId) == -1)
		{
			this.mHintIconArray.put(drawableId, drawableId);
			
			TextView tv = new TextView(mContext);
			tv.setTag(drawableId);
			tv.setGravity(Gravity.CENTER);
			ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			tv.setLayoutParams(lp);
			tv.setBackgroundResource(drawableId);
			tv.setIncludeFontPadding(false);
			
			this.mHintIcons.addView(tv);
		}
	}
	
	public void removeHintIcon(int hintId)
	{
		if (this.mHintIconArray.indexOfKey(hintId) != -1)
		{
			this.mHintIconArray.delete(hintId);
			this.mHintIcons.removeView(this.mHintIcons.findViewWithTag(hintId));
		}
	}
	
	/**
	 * Remove all hint icons.
	 */
	public void removeAllHintIcons()
	{
		this.mHintIconArray.clear();
		this.mHintIcons.removeAllViews();
	}
	
	/**
	 * Set the title.
	 * 
	 * @param title
	 */
	public void setTitle(CharSequence title)
	{
		this.mTitle.setText(title);
	}

	/**
	 * Set the title.
	 * 
	 * @param titleId
	 */
	public void setTitle(int titleId)
	{
		this.mTitle.setText(titleId);
	}

	/**
	 * Set the content.
	 * 
	 * @param content
	 */
	public void setContent(CharSequence content)
	{
		this.mContentView.setVisibility(View.VISIBLE);
		this.mContentView.setText(content);
	}

	/**
	 * Return content text.
	 * 
	 * @return
	 */
	public CharSequence getContent()
	{
		return this.mContentView.getText();
	}

	/**
	 * Set the content.
	 * 
	 * @param contentId
	 */
	public void setContent(int contentId)
	{
		this.mContentView.setVisibility(View.VISIBLE);
		this.mContentView.setText(contentId);
	}
	
	/**
	 * Set the content, bind "not data" to the empty content.
	 * 
	 * @param content
	 */
	public void setContent2(CharSequence content)
	{
		this.mContentView.setVisibility(View.VISIBLE);

		if (TextUtils.isEmpty(content))
		{
			this.mContentView.setText(R.string.hint_no_data);
		}
		else
		{
			this.mContentView.setText(content);
		}
	}
	
	
	/**
	 * Set XPreference Icon.
	 * 
	 * @param icon
	 */
	public void setIcon(int icon)
	{
		Drawable drawable = getResources().getDrawable(icon);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		this.mTitle.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 6));
		this.mTitle.setCompoundDrawables(drawable, null, null, null);
	}
	
	/**
	 * Set XPreference Icon.
	 * 
	 * @param drawable
	 */
	public void setIcon(Drawable drawable)
	{
		if (drawable != null)
		{
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
			this.mTitle.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 6));
			this.mTitle.setCompoundDrawables(drawable, null, null, null);
		}
		else
		{
			this.mTitle.setCompoundDrawables(null, null, null, null);
		}
	}
	
	/**
	 * 
	 * @param resId
	 */
	public void setRightText(int resId)
	{
		//this.mIndicator.setText(resId);
		this.mRightText.setText(resId);
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setRightText(CharSequence text)
	{
		//this.mIndicator.setText(text);
		this.mRightText.setText(text);
	}
	
	/**
	 * 
	 * @param text
	 */
	public CharSequence getRightText()
	{
		return this.mRightText.getText();
	}
	
	/**
	 * 设置字体颜色值
	 * 
	 * @param text
	 */
	public void setRightTextColor(int color)
	{
		this.mRightText.setTextColor(color);
	}
	
	/**
	 * 设置右侧图标, 默认仅提供箭头/单选/复选等
	 * 
	 * @param drawableId
	 */
	public void setItemIndicatorLeft(int drawableId)
	{
		Drawable drawable = getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		this.mIndicator.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 4));
		this.mIndicator.setCompoundDrawables(drawable, null, null, null);
	}

	/**
	 * 设置右侧图标, 默认仅提供箭头/单选/复选等
	 * 
	 * @param drawableId
	 */
	public void setItemIndicatorRight(int drawableId)
	{
		Drawable drawable = getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		this.mIndicator.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 4));
		this.mIndicator.setCompoundDrawables(null, null, drawable, null);
	}

	/**
	 * @return the mIndicator
	 */
	protected TextView getIndicator()
	{
		return mIndicator;
	}

	/**
	 * Changes the selection state of this view.
	 */
	public void setIndicatorSelected(boolean selected)
	{
		this.mIndicator.setSelected(selected);
	}
	
	/**
	 * Indicates the selection state of this view.
	 * 
	 * @return
	 */
	public boolean isIndicatorSelected()
	{
		return this.mIndicator.isSelected();
	}
	
	/**
	 * Indicator click event
	 * 
	 * @param l
	 */
	public void setIndicatorClickListener(OnClickListener l)
	{
		this.mIndicatorClickListener = l;
	}

	@Override
	public void setOnClickListener(OnClickListener l)
	{
		this.mOnClickListener = l;
	}

	public int getState()
	{
		return mState;
	}

	public void setState(int state)
	{
		this.mState = state;
	}
	
	public boolean getEnabled()
	{
		return mEnabled;
	}

	// ------------------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------------------

	/**
	 * 绑定标记到指定的视图, 默认显示两次
	 * 
	 * @param key 保存的Key
	 */
	public void bindBadgeViewWarning(final String key)
	{
		bindBadgeViewWarning(key, 1);
	}

	/**
	 * 绑定标记到指定的视图
	 * 
	 * @param key 保存的Key
	 * @param showTimes 需要显示的次数
	 */
	public void bindBadgeViewWarning(final String key, int showTimes)
	{
		bindBadgeViewWarning(mContentView, key, showTimes, R.drawable.xp_badge_warning);
	}

	/**
	 * 绑定至标题
	 * 
	 * @param key
	 * @param showTimes
	 */
	@Deprecated
	public void bindBadgeViewWarningRightText(final String key, int showTimes)
	{
		final int count = XPreferenceManager.getIntTemp(key, 0);

		if (count < showTimes)
		{
			BadgeView badgeView = bindBadgeView(mRightText, BadgeView.POSITION_CENTER_RIGHT, key, count,
					R.drawable.xp_badge_warning);
		}
	}

	/**
	 * 绑定标记到指定的视图
	 * 
	 * @param target
	 * @param key 保存的Key
	 * @param showTimes 需要显示的次数
	 * @param drawableId 资源图片
	 */
	public void bindBadgeViewWarning(View target, final String key, int showTimes, int drawableId)
	{
		final int count = XPreferenceManager.getIntTemp(key, 0);

		if (count < showTimes)
		{
			// 在指定的次数内都进行
			bindBadgeView(target, BadgeView.POSITION_RIGHT, key, count, drawableId);
		}
	}

	/**
	 * 
	 * @param target
	 * @param drawableId
	 * @return
	 */
	public BadgeView bindBadgeView(View target, int position, final String key, final int count, int drawableId)
	{
		final BadgeView badgeView = new BadgeView(getContext(), target);
		badgeView.setBackgroundResource(drawableId);
		badgeView.setBadgePosition(position);
		badgeView.setWidth(ViewUtils.dip2px(getContext(), 10));
		badgeView.setHeight(ViewUtils.dip2px(getContext(), 10));

		badgeView.show();

		mContainer.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				XPreferenceManager.putIntTemp(key, count + 1);
				if (mEnabled)
				{
					onPreferenceClick(mRootView, mState);
				}

				if (badgeView.isShown())
				{
					// badgeView.toggle(true);
					badgeView.hide(true);
				}
			}
		});

		return badgeView;
	}

	/**
	 * 绑定标记到默认的位置视图
	 * 
	 * @param drawableId
	 */
	public void bindBadgeViewForever(int drawableId)
	{
		final BadgeView badgeView = new BadgeView(getContext(), mBadgeDefaultView);
		badgeView.setBackgroundResource(drawableId);
		badgeView.setBadgePosition(BadgeView.POSITION_CENTER);
		// badgeView.setWidth(ViewUtils.dip2px(getContext(), 10));
		// badgeView.setHeight(ViewUtils.dip2px(getContext(), 10));

		badgeView.show();

		mContainer.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				if (mEnabled)
				{
					onPreferenceClick(mRootView, mState);
				}
			}
		});
	}

	/**
	 * 绑定标记到标题右侧的位置视图
	 * 
	 * @param key
	 * @param showTimes
	 * @param drawableId
	 */
	public void bindBadgeViewToTitle(final String key, final int showTimes, final int drawableId)
	{
		final int count = XPreferenceManager.getIntTemp(key, 0);

		if (count >= showTimes)
		{
			return;
		}

		final BadgeView badgeView = new BadgeView(getContext(), mContentView);
		badgeView.setBackgroundResource(drawableId);
		badgeView.setBadgePosition(BadgeView.POSITION_LEFT);

		badgeView.show();

		mContainer.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				XPreferenceManager.putIntTemp(key, count + 1);
				if (mEnabled)
				{
					onPreferenceClick(mRootView, mState);
				}

				if (badgeView.isShown())
				{
					badgeView.hide(true);
				}
			}
		});

		// bindBadgeViewWarning(mTitle, key, showTimes, drawableId);
	}

}
