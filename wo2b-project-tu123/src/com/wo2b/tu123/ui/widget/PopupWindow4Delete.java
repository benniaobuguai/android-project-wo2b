package com.wo2b.tu123.ui.widget;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.view.Transparency;
import com.wo2b.tu123.R;

/**
 * 底部显示的删除控件.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class PopupWindow4Delete extends PopupWindow
{
	
	private static final String TAG = "PopupWindow4Delete";
	
	private View mContentView;
	
	private boolean mFocusable;
	private boolean mTouchable;
	private boolean mOutsideTouchable;
	
	private Transparency mTransparency;
	
	private boolean mSelected;
	
	private PopupWindow4Delete(Builder builder)
	{
		super(builder.mContentView, builder.mWidth, builder.mHeight);
		
		this.mContentView = builder.mContentView;
		
		this.mFocusable = builder.mFocusable;
		this.mTouchable = builder.mTouchable;
		this.mOutsideTouchable = builder.mOutsideTouchable;
		
		this.mTransparency = builder.mTransparency;
		
		this.mSelected = builder.mSelected;
		
		setUpView();
	}

	private void setUpView()
	{
		this.setFocusable(mFocusable);
		this.setTouchable(mTouchable);
		this.setOutsideTouchable(mOutsideTouchable);

		this.setBackgroundDrawable(new ColorDrawable(mTransparency.color));
		//this.setAnimationStyle(R.style.anim_popup_fade);
	}
	
	public View getContentView()
	{
		return mContentView;
	}
	
	public View getItemView()
	{
		return mContentView.findViewById(0);
	}

	public boolean isFocusable()
	{
		return mFocusable;
	}

	public boolean isTouchable()
	{
		return mTouchable;
	}

	public boolean isOutsideTouchable()
	{
		return mOutsideTouchable;
	}

	public Transparency getTransparency()
	{
		return mTransparency;
	}

	public boolean isSelected()
	{
		return mSelected;
	}

	public static class Builder
	{

		private Context mContext;
		
		private View mContentView;
		private int mWidth;
		private int mHeight;
		private boolean mFocusable = false;
		private boolean mTouchable = true;
	    private boolean mOutsideTouchable = false;
	    
		private Transparency mTransparency;
		
		private OnPopupDeleteClickListener mOnPopupDeleteClickListener;
		
		
		private boolean mSelected = false;
		
		public Builder(Context context)
		{
			this.mContext = context;
			this.mTransparency = Transparency.LEVEL_3;
		}
		
		public Builder setContentView(View contentView)
		{
			this.mContentView = contentView;
			return this;
		}
		
		public Builder setFocusable(boolean focusable)
		{
			this.mFocusable = focusable;
			return this;
		}
		
		public Builder setTouchable(boolean touchable)
		{
			this.mTouchable = touchable;
			return this;
		}
		
		public Builder setOutsideTouchable(boolean outsideTouchable)
		{
			this.mOutsideTouchable = outsideTouchable;
			return this;
		}

		public Builder setSelected(boolean selected)
		{
			this.mSelected = selected;
			return this;
		}
		
		public Builder setOnPopupDeleteClickListener(OnPopupDeleteClickListener listener)
		{
			this.mOnPopupDeleteClickListener = listener;
			return this;
		}
		
		/**
		 * 设置透明度
		 * 
		 * @param transparency
		 * @return
		 */
		public Builder setTransparency(Transparency transparency)
		{
			this.mTransparency = transparency;
			return this;
		}
		
		public PopupWindow create()
		{
			this.mContentView = LayoutInflater.from(mContext).inflate(R.layout.x_pop_cmd_delete, null);
			
			if (Transparency.LEVEL_1.equals(this.mTransparency))
			{
				// 全屏显示, 内容+全透明遮罩层, 遮罩层事件自动分发至底层.
				this.mWidth = LayoutParams.MATCH_PARENT;
				this.mHeight = LayoutParams.WRAP_CONTENT;
			}
			else
			{
				// 全屏显示, 内容+半透明遮罩层.
				this.mWidth = LayoutParams.MATCH_PARENT;
				this.mHeight = LayoutParams.MATCH_PARENT;
			}
			
			final TextView cmd_delete = (TextView) mContentView.findViewById(R.id.cmd_delete);
			final TextView cmd_selete_all = (TextView) mContentView.findViewById(R.id.cmd_selete_all);
			final PopupWindow cmdPopupWindow = new PopupWindow4Delete(this);
			
			// 点击关闭对话框
			this.mContentView.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					cmdPopupWindow.dismiss();
				}
			});
			
			this.mContentView.setFocusableInTouchMode(true);
			this.mContentView.setOnKeyListener(new OnKeyListener()
			{
				
				@Override
				public boolean onKey(View v, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN && cmdPopupWindow.isShowing())
					{
						cmdPopupWindow.dismiss();
						
						return true;
					}

					return false;
				}
			});
			
			mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_open_enter_bottom));
			//contentView.setClickable(true);// 消费事件

			cmd_delete.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (mOnPopupDeleteClickListener != null)
					{
						mOnPopupDeleteClickListener.onDelete();
					}
				}
			});
			
			cmd_selete_all.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (mOnPopupDeleteClickListener != null)
					{
						mSelected = !mSelected;

						Drawable drawable = null;
						if (mSelected)
						{

							drawable = mContext.getResources().getDrawable(R.drawable.checkbox_round_checked);

						}
						else
						{
							drawable = mContext.getResources().getDrawable(R.drawable.checkbox_round_normal);
						}
						drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
						cmd_selete_all.setCompoundDrawablePadding(ViewUtils.dip2px(mContext, 0));
						cmd_selete_all.setCompoundDrawables(drawable, null, null, null);

						mOnPopupDeleteClickListener.onSeleteAll((TextView) v);
					}
				}
			});
			
			return cmdPopupWindow;
		}
		
	}
	
	public static interface OnPopupDeleteClickListener
	{

		void onDelete();

		void onSeleteAll(TextView v);

	}

}