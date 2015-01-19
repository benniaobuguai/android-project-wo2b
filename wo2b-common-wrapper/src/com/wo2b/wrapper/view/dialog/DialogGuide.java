package com.wo2b.wrapper.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.wo2b.sdk.common.util.TDevice;
import com.wo2b.wrapper.R;

/**
 * 操作指引的对话框
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-12-13
 */
public class DialogGuide extends Dialog
{

	private DialogGuide mDialog;

	private ImageView mGuideImageView;
	private RelativeLayout mButtonBar;
	private Button mPreBtn;
	private Button mNextBtn;
	
	/**
	 * 帮助指引图片集合
	 */
	private int[] mImageArray = null;

	/**
	 * 步骤数
	 */
	private int mStep = 0;

	/**
	 * 索引
	 */
	private int mIndex = 0;

	/**
	 * 多图或单图模式, 默认是单图模式
	 */
	private boolean mSingleMode = true;

	public DialogGuide(Context context, int theme)
	{
		super(context, theme);
	}

	public DialogGuide(Context context)
	{
		this(context, R.style.Rocky_Dialog_NoTitle_Guide);
	}

	public void setImage(int[] images)
	{
		this.mImageArray = images;
	}

	public void setImage(int image)
	{
		this.mImageArray = new int[]
		{
			image
		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_guide);
		mDialog = this;

		mGuideImageView = (ImageView) findViewById(R.id.guide_imageView);
		mButtonBar = (RelativeLayout) findViewById(R.id.button_bar);
		
					mPreBtn = (Button) findViewById(R.id.pre_btn);
					mNextBtn = (Button) findViewById(R.id.next_btn);
					
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		lp.height = dm.heightPixels - TDevice.getStatuBarHeight(getContext());
		lp.width = dm.widthPixels;

		if (mImageArray != null && mImageArray.length > 0)
		{
			mGuideImageView.setImageResource(mImageArray[mIndex]);
		}

		if (mImageArray != null && mImageArray.length > 1)
		{
			mSingleMode = false;
		}

		this.mStep = mImageArray.length;
		if (mSingleMode)
		{
			mPreBtn.setVisibility(View.GONE);
			mNextBtn.setVisibility(View.VISIBLE);
			mNextBtn.setText(R.string.guide_ok);

			// 添加新的点击事件
			mNextBtn.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (mOnGuideFinishListener != null)
					{
						mOnGuideFinishListener.onGuideFinish(mDialog);
					}
					else
					{
						mDialog.dismiss();
					}
				}
			});
		}
		else
		{
			// 多图指引模式
			if (mIndex == 0)
			{
				mPreBtn.setVisibility(View.GONE);
				mNextBtn.setVisibility(View.VISIBLE);
			}

			mPreBtn.setOnClickListener(new OnGuideClickListener(Position.PRE));
			mNextBtn.setOnClickListener(new OnGuideClickListener(Position.NEXT));
		}
	}

	/**
	 * Return a Resources instance for your application's package.
	 * 
	 * @return
	 */
	protected Resources getResources()
	{
		return getContext().getResources();
	}

	/**
	 * 上一个
	 * 
	 * @param pre
	 */
	private void onPreClick(Button pre)
	{
		mIndex = (--mIndex) % mStep;
		if (mIndex == 0)
		{
			// 移动至第一项
			mPreBtn.setVisibility(View.GONE);
		}

		if (mIndex < mStep - 1 && mStep > 1)
		{
			// 移动至第一项, 并且只能一项
			mNextBtn.setVisibility(View.VISIBLE);
			mNextBtn.setText(R.string.guide_next);

			if (mIndex == mStep - 2)
			{
				mNextBtn.setOnClickListener(new OnGuideClickListener(Position.NEXT));
			}
		}

		mGuideImageView.setImageResource(mImageArray[mIndex]);
	}

	/**
	 * 下一个
	 * 
	 * @param next
	 */
	private void onNextClick(Button next)
	{
		mIndex = (++mIndex) % mStep;
		if (mIndex > 0)
		{
			mPreBtn.setVisibility(View.VISIBLE);
		}

		if (mIndex == mStep - 1)
		{
			// mPreBtn.setVisibility(View.GONE);
			mNextBtn.setVisibility(View.VISIBLE);
			mNextBtn.setText(R.string.guide_ok);

			// 添加新的点击事件
			mNextBtn.setOnClickListener(new View.OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (mOnGuideFinishListener != null)
					{
						mOnGuideFinishListener.onGuideFinish(mDialog);
					}
					else
					{
						mDialog.dismiss();
					}
				}
			});
		}

		mGuideImageView.setImageResource(mImageArray[mIndex]);
	}

	public class OnGuideClickListener implements View.OnClickListener
	{

		private Position mLR;

		public OnGuideClickListener(Position lr)
		{
			this.mLR = lr;
		}

		@Override
		public void onClick(View v)
		{
			if (Position.PRE.equals(mLR))
			{
				onPreClick((Button) v);
			}
			else if (Position.NEXT.equals(mLR))
			{
				onNextClick((Button) v);
			}
		}

	}

	public static enum Position
	{
		PRE, NEXT
	}
	
	
	private OnGuideFinishListener mOnGuideFinishListener;

	public void setOnGuideFinishListener(OnGuideFinishListener listener)
	{
		this.mOnGuideFinishListener = listener;
	}

	public static interface OnGuideFinishListener
	{

		public void onGuideFinish(DialogGuide dialog);

	}

}
