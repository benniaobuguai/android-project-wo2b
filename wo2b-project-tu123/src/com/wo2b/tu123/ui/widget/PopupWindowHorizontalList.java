package com.wo2b.tu123.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.wo2b.sdk.view.Transparency;
import com.wo2b.wrapper.view.XListView;
import com.wo2b.wrapper.view.XListView.OnItemClickListener;
import com.wo2b.tu123.R;

/**
 * Command List PopupWindow.
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public class PopupWindowHorizontalList extends PopupWindow
{
	
	private static final String TAG = "PopupWindowHorizontalList";
	
	private View mContentView;
	
	private boolean mFocusable;
	private boolean mTouchable;
	private boolean mOutsideTouchable;
	
	private Transparency mTransparency;
	
	private int[] mCommandList;
	
	private PopupWindowHorizontalList(Builder builder)
	{
		super(builder.mContentView, builder.mWidth, builder.mHeight);
		
		this.mContentView = builder.mContentView;
		
		this.mFocusable = builder.mFocusable;
		this.mTouchable = builder.mTouchable;
		this.mOutsideTouchable = builder.mOutsideTouchable;
		
		this.mTransparency = builder.mTransparency;
		
		this.mCommandList = builder.mCommandList;
		
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

	public int[] getCommandList()
	{
		return mCommandList;
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
		
		private int[] mCommandList;
		
		private OnPopupWindowClickListener mPopupWindowClickListener;
		
		public Builder(Context context)
		{
			this.mContext = context;
			this.mTransparency = Transparency.LEVEL_3;
		}
		
		public Builder setCommandList(int[] cmdArrays)
		{
			this.mCommandList = cmdArrays;
			return this;
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
		
		public Builder setOnPopupWindowClickListener(OnPopupWindowClickListener listener)
		{
			this.mPopupWindowClickListener = listener;
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
			this.mContentView = LayoutInflater.from(mContext).inflate(R.layout.x_pop_cmd_horizontal_list, null);
			
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
			
			final XListView commandListView = (XListView) mContentView.findViewById(R.id.commandListView);
			final PopupWindow cmdPopupWindow = new PopupWindowHorizontalList(this);
			cmdPopupWindow.setOnDismissListener(new OnDismissListener()
			{
				
				@Override
				public void onDismiss()
				{
					//mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_out));
				}
			});
			
			// 点击关闭对话框
			this.mContentView.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					cmdPopupWindow.dismiss();
					
//					ObjectAnimator animator = ObjectAnimator.ofFloat(mContentView, "y", 0, mContentView.getHeight()).setDuration(1000);
//					animator.start();
//					animator.addListener(new AnimatorListener()
//					{
//						
//						@Override
//						public void onAnimationStart(Animator animation)
//						{
//						}
//						
//						@Override
//						public void onAnimationRepeat(Animator animation)
//						{
//						}
//						
//						@Override
//						public void onAnimationEnd(Animator animation)
//						{
//							cmdPopupWindow.dismiss();
//						}
//						
//						@Override
//						public void onAnimationCancel(Animator animation)
//						{
//							cmdPopupWindow.dismiss();
//						}
//					});
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
						
			if (mCommandList != null && mCommandList.length > 0)
			{
				List<Map<String, String>> data = new ArrayList<Map<String, String>>();
				
				int cmdCount = mCommandList.length;
				HashMap<String, String> map = null;
				for (int i = 0; i < cmdCount; i++)
				{
					map = new HashMap<String, String>();
					map.put("cmd_id", mCommandList[i] + "");
					map.put("cmd_name", mContext.getString(mCommandList[i]));
					
					data.add(map);
				}
				
				String[] from = new String[] { "cmd_name" };
				int[] to = new int[] { R.id.command };
				SimpleAdapter adapter = new SimpleAdapter(mContext, data, R.layout.x_pop_cmd_list_item, from, to);
				
				commandListView.setOnItemClickListener(new OnItemClickListener()
				{
					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(Adapter adapter, View view, int position, long id)
					{
						if (mPopupWindowClickListener != null)
						{
							Map<String, String> map = (Map<String, String>) adapter.getItem(position);
							final int cmd_id = Integer.parseInt(map.get("cmd_id"));
							mPopupWindowClickListener.onClick(cmdPopupWindow, cmd_id);
						}
					}
				});
				
				commandListView.setAdapter(adapter);
			}
			
			return cmdPopupWindow;
		}
		
	}
	
}