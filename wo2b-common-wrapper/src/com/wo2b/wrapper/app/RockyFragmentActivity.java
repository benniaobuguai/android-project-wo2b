package com.wo2b.wrapper.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wo2b.wrapper.R;
import com.wo2b.sdk.umeng.MobclickAgentProxy;

/**
 * Rocky FragmentActivity
 * 
 * @author Rocky
 * 
 */
public class RockyFragmentActivity extends RockyAbsFragmentActivity
{

	public static final String TAG = "Rocky.RockyFragmentActivity";

	// --------------------------------------------------------------------
	// ------------------------- Common message code ----------------------
	/** Request popup menu. */
	public static final int MENU_REQUEST_CODE = 10000;

	// --------------------------------------------------------------------
	
	
	private View rocky_rootView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.rocky_base_activity);
		rocky_rootView = findViewById(R.id.rocky_rootView);
		
		if (getSupportActionBar() != null)
		{
			// 设置图标
			int icon = getResources().getIdentifier("actionbar_icon", "drawable", this.getPackageName());
			// getSupportActionBar().setIcon(R.drawable.actionbar_icon);
			if (icon > 0)
			{
				getSupportActionBar().setIcon(icon);
			}
		}
	}

	@Override
	public void setContentView(int layoutResID)
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		LinearLayout parentView = ((LinearLayout) findViewById(R.id.layout_base_content));
		inflater.inflate(layoutResID, parentView);
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Bottom Popup Menu Start &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
	/**
	 * 返回底部菜单栏
	 * 
	 * @return
	 */
	public LinearLayout getBottomMenu()
	{
		return (LinearLayout) findViewById(R.id.bottom_menu_container);
	}
	
	/**
	 * 设置底部菜单栏的背景颜色
	 * 
	 * @param resId
	 */
	public void setBottomPopupMenuBackground(int resId)
	{
		getBottomMenu().setBackgroundResource(resId);
	}
	
	public void setBottomPopupMenuVisibility(int visibility)
	{
		getBottomMenu().setVisibility(visibility);
	}
	
	/**
	 * 显示底部菜单栏
	 * 
	 * @param popupWindow
	 */
	public void showBottomPopupMenu(final PopupWindow popupWindow, final int background)
	{
		popupWindow.showAtLocation(rocky_rootView, Gravity.BOTTOM, 0, 0);
		getUiHandler().postDelayed(new Runnable()
		{
			
			@Override
			public void run()
			{
				setBottomPopupMenuBackground(background);
				setBottomPopupMenuVisibility(View.VISIBLE);
			}
		}, 500);
	}
	
	/**
	 * 隐藏底部菜单栏
	 * 
	 * @param popupWindow
	 */
	public void dismissBottomPopupMenu(final PopupWindow popupWindow)
	{
		setBottomPopupMenuVisibility(View.GONE);
		getUiHandler().post(new Runnable()
		{
			
			@Override
			public void run()
			{
				popupWindow.dismiss();
			}
		});
	}
	// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& Bottom Popup Menu End &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

	@Override
	public void startActivity(Intent intent)
	{
		super.startActivity(intent);
		//this.overridePendingTransition(R.anim.slide_open_enter_r2l, R.anim.slide_close_exit_l2r);
	}
	
//	/**
//	 * Show menu in popup style.
//	 * 
//	 * @param xMenu
//	 */
//	public void showPopupMenu(XMenu xMenu)
//	{
//		Bundle bundle = new Bundle();
//		bundle.putSerializable("x_menu", xMenu);
//		
//		Intent intent = new Intent();
//		intent.setClass(getContext(), MenuMultiLineActivity.class);
//		intent.putExtras(bundle);
//		startActivityForResult(intent, MENU_REQUEST_CODE);
//	}
//	
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data)
//	{
//		super.onActivityResult(requestCode, resultCode, data);
//		if (resultCode == RESULT_OK)
//		{
//			if (requestCode == MENU_REQUEST_CODE)
//			{
//				XMenuItem menuItem = data.getParcelableExtra("menu_item");
//				if (menuItem == null)
//				{
//					throw new NullPointerException("XMenuItem can not be null.");
//				}
//
//				onPopupMenuItemClick(menuItem);
//
//				return;
//			}
//		}
//	}
//
//	/**
//	 * Callback when PopupMenu item click.
//	 * 
//	 * @param item
//	 */
//	protected void onPopupMenuItemClick(XMenuItem item)
//	{
//
//	}

}
