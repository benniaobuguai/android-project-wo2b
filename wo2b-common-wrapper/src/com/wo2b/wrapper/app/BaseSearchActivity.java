package com.wo2b.wrapper.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.wrapper.R;

/**
 * Rocky SearchActivity
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * 
 */
public abstract class BaseSearchActivity<Model> extends BaseFragmentListActivity<Model>
{
	
	public static final String TAG = "Rocky.SearchActivity";

	private View actionbar_search_view = null;
	private TextView search_box = null;
	private TextView actionbar_item_back = null;
	private TextView actionbar_item_operate = null;
	private ImageView remove_all = null;
	
	private String mKeyword = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setUp();
	}

	protected void setUp()
	{
		// 自定义显示, 用于标题居中
		getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionbar_search_view = LayoutInflater.from(this).inflate(R.layout.actionbar_search_view, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		
		getSupportActionBar().setCustomView(actionbar_search_view, params);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayShowHomeEnabled(false);

		search_box = (TextView) actionbar_search_view.findViewById(R.id.search_box);
		actionbar_item_back = (TextView) actionbar_search_view.findViewById(R.id.actionbar_item_back);
		actionbar_item_operate = (TextView) actionbar_search_view.findViewById(R.id.actionbar_item_operate);
		remove_all = (ImageView) actionbar_search_view.findViewById(R.id.remove_all);
		remove_all.setVisibility(View.GONE); // Default is invisible
		
		
		search_box.addTextChangedListener(new TextWatcher()
		{

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				if (s != null && !TextUtils.isEmpty(s.toString()))
				{
					remove_all.setVisibility(View.VISIBLE);
				}
				else
				{
					remove_all.setVisibility(View.GONE);
				}
				
				mKeyword = s.toString();
				doSearch(mKeyword);
			}
		});
		
		OnSearchViewItemClick onClickListener = new OnSearchViewItemClick();
		actionbar_item_back.setOnClickListener(onClickListener);
		actionbar_item_operate.setOnClickListener(onClickListener);
		remove_all.setOnClickListener(onClickListener);
	}

	/**
	 * 搜索框事件
	 */
	public class OnSearchViewItemClick implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			if (v.getId() == R.id.actionbar_item_back)
			{
				finish();
			}
			else if (v.getId() == R.id.actionbar_item_operate)
			{
				finish();
			}
			else if (v.getId() == R.id.remove_all)
			{
				onRemoveAll(v);
			}
		}
	}
	
	/**
	 * Remove the text from the search box.
	 * 
	 * @param v
	 */
	private void onRemoveAll(View v)
	{
		search_box.setText("");
		search_box.requestFocus();

		remove_all.setVisibility(View.GONE);
	}

	/**
	 * 重写此方法, 执行与搜索意思更接近的方法. 属性text1即为搜索关键字
	 */
	@Override
	protected XModel realOnPullDown(RockyParams params)
	{
		return null;
	}

	/**
	 * 搜索关键字
	 * 
	 * @return
	 */
	public String getKeyword()
	{
		return this.mKeyword;
	}

	private void doSearch(String keyword)
	{
		// 执行清空所有数据先
		realClearData();

		RockyParams params = new RockyParams();
		params.setText1(keyword);

		realExecuteTask(params);
	}

}
