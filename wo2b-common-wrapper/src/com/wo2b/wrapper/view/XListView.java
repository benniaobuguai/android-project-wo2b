package com.wo2b.wrapper.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;

import com.wo2b.wrapper.R;
import com.wo2b.sdk.assistant.log.Log;

/**
 * ListView implements by LinearLayout.
 * 
 * @author 笨鸟不乖
 * 
 */
public class XListView extends LinearLayout
{
	
	private final static String TAG = "XListView";
	
	private Context mContext = null;
	
	private Adapter mAdapter = null;
	
	private boolean mNeedFooterView = true;
	
	private View mFooterView = null;
	
	private OnItemClickListener mOnItemClickListener;
	
	private OnClickListener mOnFooterClickListener;
	
	private Drawable mDivider;
	private int mDividerHeight = 1;
	private int mDividerPadding = 0;
	
	@SuppressLint("NewApi")
	public XListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		initView(attrs);
	}
	
	public XListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initView(attrs);
	}
	
//	public XListView(Context context)
//	{
//		super(context);
//		initView(null);
//	}
	
	private void initView(AttributeSet attrs)
	{
		mContext = getContext();
		TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.rocky);
		mDivider = typedArray.getDrawable(R.styleable.rocky_rocky_divider);
		mDividerHeight = typedArray.getInt(R.styleable.rocky_rocky_dividerHeight, 1);
		mDividerPadding = typedArray.getInt(R.styleable.rocky_rocky_dividerPadding, 0);
		
		typedArray.recycle();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b)
	{
		super.onLayout(changed, l, t, r, b);
	}
	
	public Drawable getDivider()
	{
		return mDivider;
	}
	
	public void setDivider(Drawable divider)
	{
		this.mDivider = divider;
	}
	
	public int getDividerHeight()
	{
		return mDividerHeight;
	}
	
	public void setDividerHeight(int dividerHeight)
	{
		this.mDividerHeight = dividerHeight;
	}
	
	public int getDividerPadding()
	{
		return mDividerPadding;
	}
	
	public void setDividerPadding(int dividerPadding)
	{
		this.mDividerPadding = dividerPadding;
	}

	public void setLayout()
	{
		if (mAdapter == null)
		{
			Log.W(TAG, "Adapter is null.");
			return;
		}
		//final int orientation = this.getOrientation();
		
		this.removeAllViews();
		
		int count = mAdapter.getCount();
		for (int i = 0; i < count; i++)
		{
			final View view = mAdapter.getView(i, null, null);
			if (view == null)
			{
				// 路过为空的Item.
				continue;
			}
			final int position = i;
			final long itemId = mAdapter.getItemId(i);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1);
			//lp.setMargins(0, 0, 0, ViewUtils.dip2px(getContext(), 5));
			view.setLayoutParams(lp);
			view.setClickable(true);
			view.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (mOnItemClickListener != null)
					{
						mOnItemClickListener.onItemClick(mAdapter, view, position, itemId);
					}
				}
			});
			this.addView(view);
			
			if (this.getDivider() != null)
			{
				View divider = new View(getContext());
				divider.setBackgroundDrawable(this.getDivider());
				this.addView(divider);
			}
		}
		
		//if (count > 0)
		//{
			//if (mNeedFooterView)
			//{
				// 有数据并且需要显示FooterView时, 才进行显示.
				//WidgetTools.addHorizontalLine(getContext(), this);
				
				//if (mFooterView != null)
				//{
				//	this.addView(mFooterView);
				//	return;
				//}
				
				//this.addView(getDefaultFooterView(mOnFooterClickListener));
			//}
		//}
		
	}
	
	public Adapter getAdapter()
	{
		return mAdapter;
	}
	
	public void setAdapter(Adapter adapter)
	{
		this.mAdapter = adapter;
		setLayout();
	}
	
	public void setOnItemClickListener(OnItemClickListener listener)
	{
		this.mOnItemClickListener = listener;
	}
	
	public static interface OnItemClickListener
	{
		
		void onItemClick(Adapter adapter, View view, int position, long id);
	}
	
	public void setFooterView(View view)
	{
		this.mFooterView = view;
	}
	
	public void setNeedFooterView(boolean needFooter)
	{
		this.mNeedFooterView = needFooter;
	}
	
	public void setOnFooterClickListener(OnClickListener listener)
	{
		this.mOnFooterClickListener = listener;
	}
	
}
