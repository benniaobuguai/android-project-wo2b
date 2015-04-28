package com.wo2b.tu123.ui.global;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.tu123.R;
import com.wo2b.wrapper.component.common.Wo2bAppListActivity;

/**
 * Welcome Activity.
 * 
 * @author 笨鸟不乖
 * 
 */
public class WelcomeActivity extends BaseFragmentActivity implements OnClickListener
{
	
	private static final int[] WELCOME_PAGES = new int[] { 
		R.drawable.welcome_1, 
		R.drawable.welcome_2,
		R.drawable.welcome_3, 
		R.drawable.welcome_4
	};
	
	private ViewPager bg_viewpager;
	private Button btn_each_page;
	
	public static final String MODE_WELCOME = "welcome";
	public static final String MODE_PERSONAL_TAILOR = "personal_tailor";
	private String mMode = MODE_WELCOME;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_welcome);
		
		initView();
		setDefaultValues();
		bindEvents();
	}
	
	@Override
	protected void initView()
	{
		Intent intent = getIntent();
		String mode = intent.getStringExtra(RockyIntent.EXTRA_MODE);
		if (!TextUtils.isEmpty(mode))
		{
			mMode = mode;
		}
		
		btn_each_page = (Button) findViewById(R.id.btn_each_page);
		bg_viewpager = (ViewPager) findViewById(R.id.bg_viewpager);
		bg_viewpager.setAdapter(new WelcomeAdapter(mContext));
		
		if (MODE_PERSONAL_TAILOR.equalsIgnoreCase(mMode))
		{
			btn_each_page.setVisibility(View.VISIBLE);
		}
		else
		{
			btn_each_page.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void setDefaultValues()
	{
		
	}
	
	@Override
	protected void bindEvents()
	{
		btn_each_page.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_each_page:
			{
				gotoWo2bAppListActivity();
				break;
			}
		}
	}
	
	private void gotoHomeActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void gotoWo2bAppListActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, Wo2bAppListActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	protected boolean hasActionBar()
	{
		return false;
	}

	public class WelcomeAdapter extends PagerAdapter
	{
		
		private LayoutInflater mInflater;
		
		public WelcomeAdapter(Context context)
		{
			this.mInflater = LayoutInflater.from(context);
		}
		
		@Override
		public int getCount()
		{
			return WELCOME_PAGES.length;
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0.equals(arg1);
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			View imageLayout = mInflater.inflate(R.layout.app_welcome_item, container, false);
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image);
			Button button = (Button) imageLayout.findViewById(R.id.button);
			if (MODE_WELCOME.equalsIgnoreCase(mMode) && position < WELCOME_PAGES.length - 1)
			{
				// Welcome mode.
				button.setVisibility(View.GONE);
			}
			else if (MODE_PERSONAL_TAILOR.equalsIgnoreCase(mMode))
			{
				// Personal tailor.
				button.setVisibility(View.GONE);
			}
			
			button.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if (MODE_WELCOME.equalsIgnoreCase(mMode))
					{
						// Welcome mode.
						gotoHomeActivity();
					}
					else if (MODE_PERSONAL_TAILOR.equalsIgnoreCase(mMode))
					{
						// Personal tailor.
						//gotoPersonalTailorActivity();
					}
				}
			});

			imageView.setBackgroundResource(WELCOME_PAGES[position]);
			container.addView(imageLayout, 0);
			
			return imageLayout;
		}
		
	}
	
}
