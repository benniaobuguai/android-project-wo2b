package com.wo2b.tu123.ui.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.tu123.R;
import com.wo2b.tu123.ui.global.SplashActivity;
import com.wo2b.wrapper.app.BaseFragmentActivity;
import com.wo2b.wrapper.preference.RockyKeyValues;
import com.wo2b.wrapper.preference.XPreferenceManager;

/**
 * 锁屏UI
 * 
 * @author 笨鸟不乖
 * 
 */
public class LockViewActivity extends BaseFragmentActivity
{

	private GridView grid_number;

	private LinearLayout pwd_content;
	private EditText[] pwd_items;

	private int mIndex = 0;

	/** 解密 */
	public static final String ACTION_LOCK_DECODE = "com.wo2b.action.lock_decode";
	/** 加密 */
	public static final String ACTION_LOCK_ENCODE = "com.wo2b.action.lock_encode";
	private static final int LOCK_MODE_DECODE = 0x1;
	private static final int LOCK_MODE_ENCODE = 0x2;
	private static final int LOCK_MODE_CHANGED = 0x4;
	
	
	private String mAction;
	
	/**
	 * 001: 加密; 010： 解密; 100： 重置密码;
	 */
	private int mMode = 0x7;
	
	private String mOldPwd = null;
	
	private static final int[] KEY_NUMBER = {
		R.string.one,
		R.string.two,
		R.string.three,
		R.string.four,
		R.string.five,
		R.string.six,
		R.string.seven,
		R.string.eight,
		R.string.nine,
		R.string.clear,
		R.string.zero,
		R.string.number_null
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tu_lockview);
		Intent intent = getIntent();
		mAction = intent.getAction();
		
		initView();
		setDefaultValues();
		bindEvents();
	}

	@Override
	protected void initView()
	{
		grid_number = (GridView) findViewById(R.id.grid_number);

		generateKeyboard();

		mOldPwd = XPreferenceManager.getString(RockyKeyValues.Keys.ENTRY_PASSWORD, "");
		

		pwd_content = (LinearLayout) findViewById(R.id.pwd_content);
		final int pwd_count = pwd_content.getChildCount();
		pwd_items = new EditText[pwd_count];
		for (int i = 0; i < pwd_count; i++)
		{
			pwd_items[i] = (EditText) pwd_content.getChildAt(i);
			if (mMode == LOCK_MODE_DECODE)
			{
				// 解密
				pwd_items[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
			}
			else
			{
				pwd_items[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			}

			ViewUtils.hideSoftInput(this, pwd_items[i]);
		}

		pwd_items[0].requestFocus();
		pwd_items[pwd_count - 1].addTextChangedListener(new OnTextInputCompleted());
	}
	
	/**
	 * 生成键盘
	 */
	private void generateKeyboard()
	{
		// 生成数字键盘
		String[] from = { "text" };
		int[] to = { R.id.text };
		List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();

		int count = KEY_NUMBER.length;
		Map<String, String> map = null;
		for (int i = 0; i < count; i++)
		{
			map = new HashMap<String, String>();
			map.put("text", getString(KEY_NUMBER[i]));

			data.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.tu_lockview_keyboard_item, from, to);
		
		adapter.setViewBinder(new ViewBinder()
		{

			@Override
			public boolean setViewValue(View view, Object data, String textRepresentation)
			{
				if (data != null && TextUtils.isEmpty(data.toString()))
				{
					return false;
				}

				TextView tv = (TextView) view;
				String text = data.toString();
				if (getString(R.string.clear).equalsIgnoreCase(text))
				{
					// 清空
					tv.setTextSize(14);
					tv.getPaint().setFakeBoldText(false);
				}

				return false;
			}
		});
		
		
		grid_number.setAdapter(adapter);
	}
	
	private class OnTextInputCompleted implements TextWatcher
	{
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count)
		{
			if (count == 0)
			{
				return;
			}

			if (checkPassword(pwd_items))
			{
				StringBuffer sb = new StringBuffer();
				
				int length = pwd_items.length;
				for (int i = 0; i < length; i++)
				{
					sb.append(pwd_items[i].getText().toString());
				}
				
				String newPwd = sb.toString();
				
				switch (mMode)
				{
					case LOCK_MODE_DECODE:
					{
						// 解锁
						if (newPwd.equalsIgnoreCase(mOldPwd))
						{
							gotoSplashActivity();
						}
						else
						{
							showToast(R.string.hint_pwd_error);
							inputAgain(); // 清空密码框
						}
						
						break;
					}
					case LOCK_MODE_ENCODE:
					{
						// 加密
						if (XPreferenceManager.putString(RockyKeyValues.Keys.ENTRY_PASSWORD, newPwd))
						{
							showToast(R.string.hint_pwd_set_complete);
						}

						break;
					}
					case LOCK_MODE_CHANGED:
					{
						// 修改密码
						if (newPwd.equalsIgnoreCase(mOldPwd))
						{
							// 输入的旧密码正确, 改变状态
							mMode = LOCK_MODE_ENCODE;
							
							boolean isRemoved = XPreferenceManager.remove(RockyKeyValues.Keys.ENTRY_PASSWORD);
							if (isRemoved)
							{
								inputAgain(); // 清空密码框
								showToast(R.string.hint_pwd_input_new);
							}
						}
						else
						{
							showToast(R.string.hint_pwd_error);
							inputAgain(); // 清空密码框
						}
						
						break;
					}
				}
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after)
		{
		}

		@Override
		public void afterTextChanged(Editable s)
		{
		}
	}

	@Override
	protected void bindEvents()
	{
		grid_number.setOnItemClickListener(new OnItemClickListener()
		{

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Map<String, String> map = (Map<String, String>) parent.getItemAtPosition(position);

				String text = map.get("text");
				if (TextUtils.isEmpty(text))
				{
					return;
				}
				if (getString(R.string.clear).equalsIgnoreCase(text))
				{
					// 清空
					inputAgain();

					return;
				}

				// 判断text的内容, 如果是数字就输入到文本框

				int length = pwd_items.length;
				for (int i = 0; i < length; i++)
				{
					if (i == mIndex)
					{
						// 索引自增
						mIndex = (++mIndex) % length;
						pwd_items[mIndex].requestFocus();

						// 索引之后的Item要清空
						if (mIndex > 0)
						{
							for (int j = mIndex; j < length; j++)
							{
								pwd_items[j].setText("");
							}
						}

						pwd_items[i].setText(text);

						break;
					}
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if (ACTION_LOCK_ENCODE.equalsIgnoreCase(mAction))
		{
			if (TextUtils.isEmpty(mOldPwd))
			{
				// 加密: 没有保存过密码
				getSupportActionBar().setTitle(R.string.lock_encode);
				mMode = LOCK_MODE_ENCODE;
			}
			else
			{
				// 改密码
				getSupportActionBar().setTitle(R.string.lock_changed);
				mMode = LOCK_MODE_CHANGED;
			}

			MenuItem menuItem = menu.add(R.string.lock_decode);
			MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
		}
		else if (ACTION_LOCK_DECODE.equalsIgnoreCase(mAction))
		{
			// 解密
			// setTitle(R.string.lock_encode);
			getUiHandler().postDelayed(new Runnable()
			{

				@Override
				public void run()
				{
					getSupportActionBar().hide();
				}
			}, 1000);
			
			mMode = LOCK_MODE_DECODE;
		}
		else
		{
			// 默认加密
			MenuItem menuItem = menu.add(R.string.lock_encode);
			MenuItemCompat.setShowAsAction(menuItem, MenuItemCompat.SHOW_AS_ACTION_ALWAYS);
			
			mMode = LOCK_MODE_ENCODE;
		}
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		if (getString(R.string.lock_decode).equalsIgnoreCase(item.getTitle() + ""))
		{
			if (XPreferenceManager.remove(RockyKeyValues.Keys.ENTRY_PASSWORD))
			{
				inputAgain(); // 清空密码框
				showToast(R.string.hint_pwd_clear);
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Go to splash activity.
	 */
	private void gotoSplashActivity()
	{
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.setClass(this, SplashActivity.class);
		intent.setAction(SplashActivity.ACTION_UNCHECK_LOCK);
		startActivity(intent);

		finish();
	}

	/**
	 * 重新输入
	 */
	private void inputAgain()
	{
		int length = pwd_items.length;
		for (int i = 0; i < length; i++)
		{
			pwd_items[i].setText("");
		}

		pwd_items[0].requestFocus();
		mIndex = 0;
	}

	/**
	 * 检测密码的正确性
	 * 
	 * @param pwd
	 * @return
	 */
	private boolean checkPassword(EditText[] pwds)
	{
		if (pwds == null || pwds.length == 0)
		{
			showToastOnUiThread(R.string.hint_pwd_input);
			return false;
		}

		int length = pwds.length;
		for (int i = 0; i < length; i++)
		{
			if (TextUtils.isEmpty(pwds[i].getText().toString()))
			{
				showToastOnUiThread(R.string.hint_pwd_input);
				return false;
			}
		}

		return true;
	}
	
	@Override
	protected boolean hasActionBar()
	{
		return false;
	}

}
