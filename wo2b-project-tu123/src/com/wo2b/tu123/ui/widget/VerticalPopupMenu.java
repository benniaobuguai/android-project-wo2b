package com.wo2b.tu123.ui.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.tu123.R;


/**
 * Command List PopupWindow.
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class VerticalPopupMenu extends PopupWindow
{
	
	private View mContentView;
	
	private boolean mFocusable;
	private boolean mTouchable;
	private boolean mOutsideTouchable;
	
	private ArrayList<PopupMenuItem> mPopupMenuItems;
	
	private VerticalPopupMenu(Builder builder)
	{
		super(builder.mContentView, builder.mWidth, builder.mHeight);
		
		this.mContentView = builder.mContentView;
		
		this.mFocusable = builder.mFocusable;
		this.mTouchable = builder.mTouchable;
		this.mOutsideTouchable = builder.mOutsideTouchable;
		
		this.mPopupMenuItems = builder.mPopupMenuItems;
		
		setUpView();
	}

	private void setUpView()
	{
		this.setFocusable(mFocusable);
		this.setTouchable(mTouchable);
		this.setOutsideTouchable(mOutsideTouchable);

		this.setBackgroundDrawable(new ColorDrawable(0x00000000));
	}
	
	public View getContentView()
	{
		return mContentView;
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

	public ArrayList<PopupMenuItem> getPopupMenuItems()
	{
		return mPopupMenuItems;
	}
	
	public static interface OnPopupMenuClickListener
	{

		void onClick(PopupWindow popup, View itemView, int itemId);

	}

	public static class Builder
	{

		private Context mContext;
		
		private View mContentView;
		private int mWidth;
		private int mHeight;
		private boolean mFocusable = false;
		private boolean mTouchable = true;
	    private boolean mOutsideTouchable = true;
	    
		// 暂时均为NoTitle模式, 不处理.
		private CharSequence mTitle;
		private Drawable mIcon;

		private ArrayList<PopupMenuItem> mPopupMenuItems = new ArrayList<VerticalPopupMenu.PopupMenuItem>();

		private OnPopupMenuClickListener mOnPopupMenuClickListener;

		public Builder(Context context)
		{
			this.mContext = context;
		}
		
		public Builder setTitle(int title)
		{
			this.mTitle = mContext.getString(title);
			return this;
		}
		
		public Builder setIcon(int drawableId)
		{
			this.mIcon = mContext.getResources().getDrawable(drawableId);
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

		public Builder addItem(PopupMenuItem menu)
		{
			this.mPopupMenuItems.add(menu);
			return this;
		}

		public Builder setOnPopupMenuClickListener(OnPopupMenuClickListener listener)
		{
			this.mOnPopupMenuClickListener = listener;
			return this;
		}
		
		public PopupWindow create()
		{
			this.mContentView = LayoutInflater.from(mContext).inflate(R.layout.vertical_popup_menu, null);
			//this.mContentView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_open_enter_bottom));
			
			this.mWidth = LayoutParams.WRAP_CONTENT;
			this.mHeight = LayoutParams.WRAP_CONTENT;
			
			if (mPopupMenuItems == null || mPopupMenuItems.isEmpty())
			{
				throw new IllegalArgumentException("Not found popup item.");
			}
			
			final ListView listView = (ListView) mContentView.findViewById(R.id.listView);
			BaseAdapter adapter = new PopupWindowListAdapter(mContext, mPopupMenuItems);
			listView.setAdapter(adapter);
			
			// PopupWindow generate.
			final PopupWindow popupWindow = new VerticalPopupMenu(this);
			
			listView.setOnItemClickListener(new OnItemClickListener()
			{

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					if (mOnPopupMenuClickListener != null)
					{
						popupWindow.dismiss();

						PopupMenuItem menu = (PopupMenuItem) parent.getAdapter().getItem(position);
						mOnPopupMenuClickListener.onClick(popupWindow, view, menu.getItemId());
					}
				}
			});
			popupWindow.setFocusable(true);
			
			return popupWindow;
		}
		
		
		public class PopupWindowListAdapter extends BaseAdapter
		{

			private Context mContext;
			private List<PopupMenuItem> mMenuItems;

			public PopupWindowListAdapter(Context context, List<PopupMenuItem> menuItems)
			{
				this.mContext = context;
				this.mMenuItems = menuItems;
			}

			@Override
			public int getCount()
			{
				return mMenuItems.size();
			}

			@Override
			public PopupMenuItem getItem(int position)
			{
				return mMenuItems.get(position);
			}

			@Override
			public long getItemId(int position)
			{
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent)
			{
				if (convertView == null)
				{
					convertView = LayoutInflater.from(mContext).inflate(R.layout.vertical_popup_menu_item, parent,
							false);
				}

				ImageView imageView = ViewUtils.get(convertView, R.id.icon);
				TextView textView = ViewUtils.get(convertView, R.id.title);

				PopupMenuItem menuItem = getItem(position);
				if (menuItem.getIcon() > 0)
				{
					imageView.setVisibility(View.VISIBLE);
					imageView.setImageResource(menuItem.getIcon());
				}
				else
				{
					imageView.setVisibility(View.GONE);
				}

				textView.setText(menuItem.getTitle());

				return convertView;
			}
			
		}
		
	}
	
	public static class PopupMenuItem
	{

		private int icon;

		private int title;

		private int itemId;

		public PopupMenuItem(int itemId, int title)
		{
			this(itemId, title, 0);
		}

		public PopupMenuItem(int itemId, int title, int icon)
		{
			this.itemId = itemId;
			this.icon = icon;
			this.title = title;
		}

		public int getIcon()
		{
			return icon;
		}

		public void setIcon(int icon)
		{
			this.icon = icon;
		}

		public int getTitle()
		{
			return title;
		}

		public void setTitle(int title)
		{
			this.title = title;
		}

		public int getItemId()
		{
			return itemId;
		}

		public void setItemId(int itemId)
		{
			this.itemId = itemId;
		}

	}
	
}