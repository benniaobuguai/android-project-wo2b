package com.wo2b.wrapper.view.dialog;

import android.app.Dialog;
import android.content.Context;

/**
 * 对话框工具类
 * 
 * @author 笨鸟不乖
 * @email ixueyongjia@gmail.com
 * @version 1.0.0
 * @date 2014-11-7
 */
public final class DialogUtils
{

	private DialogUtils()
	{

	}

	/**
	 * Returns a loading dialog which is cancelable.
	 * 
	 * @param context
	 * @param message
	 * @return
	 */
	public static DialogProgressBar createLoadingDialog(Context context, String message)
	{
		return createLoadingDialog(context, message, false);
	}

	/**
	 * 
	 * @param context
	 * @param message
	 * @param cancelable
	 * @return
	 */
	public static DialogProgressBar createLoadingDialog(Context context, String message, boolean cancelable)
	{
		DialogProgressBar progressBar = new DialogProgressBar(context);
		progressBar.setCancelable(cancelable);
		progressBar.setMessage(message);

		return progressBar;
	}

//	/**
//	 * 得到自定义的progressDialog
//	 * 
//	 * @param context
//	 * @param message
//	 * @return
//	 */
//	public static Dialog createLoadingDialog_bak(Context context, String message, boolean cancelable)
//	{
//		LayoutInflater inflater = LayoutInflater.from(context);
//		View rootView = inflater.inflate(R.layout.dialog_progress_loading, null);
//		ImageView image = (ImageView) rootView.findViewById(R.id.image);
//		TextView text = (TextView) rootView.findViewById(R.id.text);
//
//		Animation loadingAnimation = AnimationUtils.loadAnimation(context, R.anim.progress_dialog_loading);
//		image.startAnimation(loadingAnimation);
//		text.setText(message);
//
//		Dialog dialog = new Dialog(context, R.style.dialog_progress_loading);
//
//		dialog.setCancelable(cancelable);
//		//dialog.setCanceledOnTouchOutside(true);
//		dialog.setCanceledOnTouchOutside(cancelable);
//		dialog.setContentView(rootView);
//
//		Window dialogWindow = dialog.getWindow();
//		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//		//lp.x = 100; // 新位置X坐标
//		// lp.y = 100; // 新位置Y坐标
//		lp.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度
//		//lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度
//		lp.height = ViewUtils.dip2px(context, 80); // 高度
//		lp.alpha = 1.0f; // 透明度
//
//		// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
//		// dialog.onWindowAttributesChanged(lp);
//		dialogWindow.setAttributes(lp);
//
//		return dialog;
//	}
	
}
