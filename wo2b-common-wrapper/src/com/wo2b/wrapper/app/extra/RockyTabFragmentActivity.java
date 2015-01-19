package com.wo2b.wrapper.app.extra;

import java.util.ArrayList;
import java.util.List;

import opensource.component.viewpagerindicator.IconPagerAdapter;
import opensource.component.viewpagerindicator.PageIndicator;
import opensource.component.viewpagerindicator.UnderlinePageIndicator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyFragmentActivity;

/**
 * Rocky Tab Fragment Activity
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-2-9
 */
public class RockyTabFragmentActivity extends RockyFragmentActivity
{

	public static final String TAG = "Rocky.TabActivity";

	private View mRootView;
	private ViewPager mPager;
	private LinearLayout mRockyTabHost;
	private LinearLayout mTabSecondary;
	private PageIndicator mIndicator;

	private TabFragmentPagerAdapter mTabFragmentPagerAdapter;
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private ArrayList<TabItem> mTabItemList = new ArrayList<TabItem>();

	// 索引位置
	private int mSelectIndex = 0;

	// 选项卡数量
	private int mTabCount = 0;

	// 标题选中颜色
	private int mSelectedColor = -1;

	// 标题未选中颜色
	private int mUnselectedColor = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rocky_top_tab);

		setUpView();
	}

	private void setUpView()
	{
		mRootView = findViewById(R.id.container);
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setOffscreenPageLimit(2);// TabCount-1
		mRockyTabHost = (LinearLayout) findViewById(R.id.rocky_tabhos);
		mTabSecondary = (LinearLayout) findViewById(R.id.tab_secondary);

		// 设置选项卡字体的默认颜色
		setSelectedColor(getResources().getColor(R.color.tab_selector_green));
		setUnSelectedColor(getResources().getColor(R.color.font_dark));

		mTabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mTabFragmentPagerAdapter);

		UnderlinePageIndicator pageIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		pageIndicator.setSelectedColor(getSelectedColor());
		pageIndicator.setViewPager(mPager);
		pageIndicator.setFades(false);
		mIndicator = pageIndicator;

		pageIndicator.setOnPageChangeListener(new OnPageChangeListener()
		{

			@Override
			public void onPageSelected(int position)
			{
				int size = mTabItemList.size();
				TextView tabItemView = null;
				for (int i = 0; i < size; i++)
				{
					tabItemView = (TextView) mRockyTabHost.getChildAt(i);

					if (i == position)
					{
						mSelectIndex = position;
						tabItemView.setTextColor(getSelectedColor());
					}
					else
					{
						tabItemView.setTextColor(getUnSelectedColor());
					}
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});

	}

	/**
	 * 返回ViewPager对象
	 * 
	 * @return
	 */
	protected View getViewPager()
	{
		return this.mPager;
	}

	/**
	 * 添加Tab标签
	 * 
	 * @param tabId
	 * @param title
	 * @param icon
	 * @param fragment
	 */
	public void addTab(int tabId, int title, int icon, Fragment fragment)
	{
		mTabCount++;

		TabItem tab = new TabItem(tabId, title, icon);
		mTabItemList.add(tab);
		mFragmentList.add(fragment);
	}
	
	/**
	 * 填充布局
	 */
	public void fillTabLayout()
	{
		int size = mTabItemList.size();
		TabItem tabItem = null;
		TextView tabItemView = null;
		for (int i = 0; i < size; i++)
		{
			tabItem = mTabItemList.get(i);
			tabItemView = (TextView) getLayoutInflater().inflate(R.layout.rocky_top_tab_view, null);
			tabItemView.setText(tabItem.title);

			if (i == mSelectIndex)
			{
				tabItemView.setTextColor(getSelectedColor());
			}
			else
			{
				tabItemView.setTextColor(getUnSelectedColor());
			}

			tabItemView.setOnClickListener(new OnTabItemClickListener(tabItemView, tabItem, i));

			mRockyTabHost.addView(tabItemView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		}
		mRockyTabHost.setWeightSum(size);

		mTabFragmentPagerAdapter.notifyDataSetChanged();
	}

	/**
	 * 数据适配器
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 2.0.0
	 * @date 2014-12-4
	 */
	class TabFragmentPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter
	{

		public TabFragmentPagerAdapter(FragmentManager fm)
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position)
		{
			return mFragmentList.get(position);
		}

		@Override
		public int getCount()
		{
			return mTabItemList.size();
		}

		@Override
		public CharSequence getPageTitle(int position)
		{
			return getResources().getString(mTabItemList.get(position % getCount()).title);
		}

		@Override
		public int getIconResId(int index)
		{
			return mTabItemList.get(index % getCount()).icon;
		}

		public void setCount(int count)
		{
			if (count > 0 && count <= 10)
			{
				notifyDataSetChanged();
			}
		}
	}

	/**
	 * TabItem 点击监听器
	 * 
	 * @author Rocky
	 * @email ixueyongjia@gmail.com
	 * @version 2.0.0
	 * @date 2014-12-4
	 */
	private class OnTabItemClickListener implements View.OnClickListener
	{

		private View tabItemView;
		private TabItem tabItem;
		private int position;

		/**
		 * 构造函数
		 * 
		 * @param tabItemView
		 * @param tabItem
		 * @param position
		 */
		public OnTabItemClickListener(View tabItemView, TabItem tabItem, int position)
		{
			this.tabItemView = tabItemView;
			this.tabItem = tabItem;
			this.position = position;
		}

		@Override
		public void onClick(View v)
		{
			mIndicator.setCurrentItem(position);
			onTabChangedClick(tabItemView, tabItem);
		}
	}

	/**
	 * 点击选项卡时触发回调
	 * 
	 * @param tabItemView
	 * @param tabItem
	 */
	public void onTabChangedClick(View tabItemView, TabItem tabItem)
	{

	}

	/**
	 * 返回根视图
	 * 
	 * @return
	 */
	public View getTabRootView()
	{
		return mRootView;
	}

	/**
	 * 设置背景颜色
	 * 
	 * @param resid
	 */
	public void setTabFragmentBackground(int resid)
	{
		mRootView.setBackgroundResource(resid);
	}

	/**
	 * 被选中的索引位置
	 * 
	 * @return
	 */
	public int getSelectIndex()
	{
		return this.mSelectIndex;
	}

	/**
	 * 返回选项卡数量
	 * 
	 * @return
	 */
	public int getTabCount()
	{
		return this.mTabCount;
	}

	/**
	 * 选项卡选中的文本颜色
	 * 
	 * @param color
	 */
	public void setSelectedColor(int color)
	{
		this.mSelectedColor = color;
	}

	/**
	 * 选项卡选中的文本颜色
	 * 
	 * @return
	 */
	public int getSelectedColor()
	{
		return this.mSelectedColor;
	}

	/**
	 * 选项卡未选中的文本颜色
	 * 
	 * @param color
	 */
	public void setUnSelectedColor(int color)
	{
		this.mUnselectedColor = color;
	}

	/**
	 * 选项卡未选中的文本颜色
	 * 
	 * @return
	 */
	public int getUnSelectedColor()
	{
		return this.mUnselectedColor;
	}

}
