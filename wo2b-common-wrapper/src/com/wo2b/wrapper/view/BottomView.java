package com.wo2b.wrapper.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

import com.wo2b.sdk.view.Transparency;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.view.XListView.OnItemClickListener;

/**
 * Custom Bottom View
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class BottomView
{

	private int mTheme;
	private Dialog dialog;
	private int animationStyle;
	private boolean isTop = false;
	
	
	private Context mContext;
	private View mContentView;
	
	private boolean mFocusable;
	private boolean mTouchable;
	private boolean mOutsideTouchable;
	
	private Transparency mTransparency;
	
	private int[] mCommandList;

	private BottomView(Builder builder)
	{
		this.mContext = builder.mContext;
		this.mContentView = builder.mContentView;
		this.mTheme = builder.mTheme;
	}

//	public BottomView(Context context, int layoutResId)
//	{
//		this(context, R.style.bottomView_theme_default, layoutResId);
//	}
//	
//	public BottomView(Context context, int theme, int layoutResId)
//	{
//		this(context, theme, View.inflate(context, layoutResId, null));
//	}
//	
//	public BottomView(Context context, int theme, View contentView)
//	{
//		this.theme = theme;
//		this.context = context;
//		this.contentView = contentView;
//	}
	
	public void setTopIfNecessary()
	{
		this.isTop = true;
	}

	public void setAnimation(int animationStyle)
	{
		this.animationStyle = animationStyle;
	}

	public View getContentView()
	{
		return this.mContentView;
	}

	
	@SuppressWarnings("deprecation")
	public void showBottomView(boolean onTouchOutsideable)
	{
		if (this.mTheme == 0)
		{
			this.dialog = new Dialog(mContext);
		}
		else
		{
			this.dialog = new Dialog(mContext, mTheme);
		}

		this.dialog.setCanceledOnTouchOutside(onTouchOutsideable);
		this.dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		this.dialog.setContentView(this.mContentView);
		
		this.mContentView.setFocusable(true);
		this.mContentView.setFocusableInTouchMode(true);
		this.mContentView.setOnKeyListener(new OnKeyListener()
		{

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event)
			{
				if (keyCode == KeyEvent.KEYCODE_MENU && event.getAction() == KeyEvent.ACTION_DOWN && dialog.isShowing())
				{
					dialog.dismiss();

					return true;
				}

				return false;
			}
		});
		

		Window wm = this.dialog.getWindow();
		WindowManager m = wm.getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = wm.getAttributes();
		p.width = (d.getWidth() * 1);
		if (this.isTop)
		{
			p.gravity = Gravity.TOP;
		}
		else
		{
			p.gravity = Gravity.BOTTOM;
		}

		if (this.animationStyle != 0)
		{
			wm.setWindowAnimations(this.animationStyle);
		}

		wm.setAttributes(p);
		this.dialog.show();
	}

	public void dismissBottomView()
	{
		if (this.dialog != null)
		{
			this.dialog.dismiss();
		}
	}
	
	
	
	
	public static class Builder
	{

		private Context mContext;
		private int mTheme;
		
		private View mContentView;
		private int mWidth;
		private int mHeight;
		private boolean mFocusable = false;
		private boolean mTouchable = true;
	    private boolean mOutsideTouchable = false;
	    
		private Transparency mTransparency;
		
		private int[] mCommandList;
		
		private OnBottomViewItemClick mOnBottomViewItemClick;
		
		public Builder(Context context)
		{
			this.mContext = context;
			this.mTheme = R.style.bottomView_theme_default;
			this.mTransparency = Transparency.LEVEL_3;
		}
		
		public Builder setTheme(int theme)
		{
			this.mTheme = theme;
			return this;
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
		
		public Builder setOnBottomViewItemClick(OnBottomViewItemClick listener)
		{
			this.mOnBottomViewItemClick = listener;
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
		
		public BottomView create()
		{
			mContentView = LayoutInflater.from(mContext).inflate(R.layout.api_bottom_view, null);
			
			final XListView commandListView = (XListView) mContentView.findViewById(R.id.commandListView);
			final BottomView bottomView = new BottomView(this);
			
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
				SimpleAdapter adapter = new SimpleAdapter(mContext, data, R.layout.api_bottom_view_cmd_list_item, from, to);
				
				
				
				commandListView.setOnItemClickListener(new OnItemClickListener()
				{
					@SuppressWarnings("unchecked")
					@Override
					public void onItemClick(Adapter adapter, View view, int position, long id)
					{
						if (mOnBottomViewItemClick != null)
						{
							Map<String, String> map = (Map<String, String>) adapter.getItem(position);
							final int cmd_id = Integer.parseInt(map.get("cmd_id"));
							mOnBottomViewItemClick.onClick(bottomView, cmd_id);
						}
					}
				});
				
				commandListView.setAdapter(adapter);
			}
			
			return bottomView;
		}
		
	}
	
	
	/**
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * 
	 */
	public static interface OnBottomViewItemClick
	{

		public void onClick(BottomView bottomView, int which);
	}

}