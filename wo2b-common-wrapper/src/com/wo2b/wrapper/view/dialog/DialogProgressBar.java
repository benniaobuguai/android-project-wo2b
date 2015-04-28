package com.wo2b.wrapper.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.wrapper.R;

/**
 * 显示文本的对话框
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 2.0.0
 * @date 2014-11-3
 */
public class DialogProgressBar extends RockyDialog
{

	private ImageView mImageView;

	private TextView mTextView;

	private Animation mLoadingAnimation;
	
	private boolean mAutoWidth = false;

	public DialogProgressBar(Context context)
	{
		super(context, R.style.Rocky_Dialog_Progress_Loading);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_dialog_progress_loading);

		if (mAutoWidth)
		{
			setMinimumWidth(0);
		}
		
		mLoadingAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.progress_dialog_loading);

		mImageView = (ImageView) findViewById(R.id.image);
		mTextView = (TextView) findViewById(R.id.text);
		// mTextView.setGravity(Gravity.LEFT);
		mTextView.setText("");

		Window dialogWindow = this.getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.x = 100; // 新位置X坐标
		// lp.y = 100; // 新位置Y坐标
		lp.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度
		// lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
		lp.height = ViewUtils.dip2px(getContext(), 80); // 高度
		lp.alpha = 1.0f; // 透明度

		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
		// dialog.onWindowAttributesChanged(lp);
		dialogWindow.setAttributes(lp);
	}

	/**
	 * 设置内容最小宽度
	 * 
	 * @param flag
	 */
	private void setMinimumWidth(int minWidth)
	{
		findViewById(R.id.dialog_content).setMinimumWidth(minWidth);
	}

	/**
	 * 设置文本随内容变化
	 * 
	 * @param autoWidth
	 */
	public void setAutoWidth(boolean autoWidth)
	{
		this.mAutoWidth = autoWidth;
	}

	/**
	 * 提示信息
	 * 
	 * @param text
	 */
	public void setMessage(String text)
	{
		super.setMessage(text);
		if (mTextView != null)
		{
			mTextView.setText(text);
		}
	}

	@Override
	public void show()
	{
		super.show();

		startLoadingAnimation();
		if (getMessage() != null)
		{
			mTextView.setText(getMessage());
		}
	}

	@Override
	public void onDetachedFromWindow()
	{
		stopLoadingAnimation();
		super.onDetachedFromWindow();
	}

	/**
	 * 播放动画
	 */
	private void startLoadingAnimation()
	{
		mImageView.startAnimation(mLoadingAnimation);
	}

	/**
	 * 停止动画
	 */
	private void stopLoadingAnimation()
	{
		mImageView.startAnimation(mLoadingAnimation);
	}

}
