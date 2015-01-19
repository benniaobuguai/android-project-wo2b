package com.wo2b.wrapper.component.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.wo2b.sdk.common.util.Utils;
import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.sdk.core.RockySdk;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyListFragmentActivity;
import com.wo2b.wrapper.component.user.UserActivity;
import com.wo2b.xxx.webapp.Wo2bResHandler;
import com.wo2b.xxx.webapp.manager.comment.Comment;
import com.wo2b.xxx.webapp.manager.comment.CommentManager;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 评论
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 */
public class CommentActivity extends RockyListFragmentActivity<Comment> implements View.OnClickListener
{

	public static final String EXTRA_MODULE = "module";
	public static final String EXTRA_TITLEID = "titleId";
	public static final String EXTRA_TITLE = "title";
	
	
	private TextView send_comment;
	private EditText et_content;
	
	
	/** 内容所属模块 */
	private String mModule;
	/** 主题ID */
	private String mTitleId;
	/** 主题名称 */
	private String mTitle;
	/** 包名 */
	private String mPkgname;
	
	
	private CommentManager mCommentManager = new CommentManager();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_comment_main);

		Intent intent = getIntent();
		mModule = intent.getStringExtra(EXTRA_MODULE);
		mTitleId = intent.getStringExtra(EXTRA_TITLEID);
		mTitle = intent.getStringExtra(EXTRA_TITLE);
		mPkgname = RockySdk.getInstance().getClientInfo().getPkgname();

		initView();

		realExecuteFirstTime(null);
	}

	@Override
	protected void initView()
	{
		if (!TextUtils.isEmpty(mTitle))
		{
			setActionBarTitle(mTitle);
		}

		send_comment = (TextView) findViewById(R.id.send_comment);
		et_content = (EditText) findViewById(R.id.et_content);
		send_comment.setOnClickListener(this);
		et_content.setSelected(false);
		et_content.clearFocus();
		
		//ViewUtils.hideSoftInput(this);
		//et_content.clearFocus();
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.send_comment)
		{
			comment();
		}
	}

	/**
	 * 把控制评论放在发布时检查
	 */
	private void comment()
	{
		// 隐藏输入法
		//ViewUtils.hideSoftInput(this, et_content);
		if (!UserManager.getInstance().isLogin())
		{
			gotoLoginActivity();
			return;
		}

		String commentString = et_content.getText().toString();
		if (commentString == null || TextUtils.isEmpty(commentString.trim()))
		{
			showToast(R.string.comment_not_null);
			et_content.setText(R.string.null_text);
			return;
		}

		final int limit = 250;
		if (commentString.length() > limit)
		{
			showToast(getString(R.string.comment_too_long, limit));
			return;
		}
		
		// The Comment Message
		final Comment comment = new Comment();
		comment.setPkgname(RockySdk.getInstance().getClientInfo().getPkgname());
		comment.setModule(mModule);
		comment.setTitleId(mTitleId);
		comment.setTitle(mTitle);
		comment.setComment(commentString);
		comment.setCreationDate(new Date());
		comment.setUserId(UserManager.getInstance().getMemoryUser().getId());
		comment.setUsername(UserManager.getInstance().getMemoryUser().getUsername());
		comment.setId(-1);
		
		mCommentManager.comment(comment, new Wo2bResHandler<Void>()
		{

			@Override
			public void onSuccess(int code, Void result)
			{
				// Add it to ListView
				realAddNewItem(comment);
				et_content.setText(R.string.null_text);
				
				showToastOnUiThread(R.string.comment_ok);
			}

			@Override
			public void onFailure(int code, String msg, Throwable throwable)
			{
				showToast(msg);
			}

		});
		
	}

	/**
	 * 去登录
	 */
	private void gotoLoginActivity()
	{
		Intent intent = new Intent();
		intent.setClass(this, UserActivity.class);
		startActivity(intent);
	}

	/**
	 * 进入评论界面
	 * 
	 * @param context 上下文
	 * @param module 模块
	 * @param titleId 评论主题ID
	 * @param title 评论主题
	 */
	public static final void entryComment(Context context, String module, String titleId, String title)
	{
		Intent intent = new Intent();
		intent.setClass(context, CommentActivity.class);
		intent.putExtra(CommentActivity.EXTRA_MODULE, module);
		intent.putExtra(CommentActivity.EXTRA_TITLEID, titleId);
		intent.putExtra(CommentActivity.EXTRA_TITLE, title);
		context.startActivity(intent);
	}

	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = getLayoutInflater().inflate(R.layout.wrapper_comment_main_list_item, parent, false);
		}

		ImageView icon = ViewUtils.get(convertView, R.id.icon);
		TextView username = ViewUtils.get(convertView, R.id.username);
		TextView date = ViewUtils.get(convertView, R.id.date);
		TextView comment = ViewUtils.get(convertView, R.id.comment);

		Comment c = realGetItem(position);
		icon.setImageResource(R.drawable.gravatar_round);
		username.setText(c.getUsername());
		date.setText(mDateFormat.format(c.getCreationDate()));
		comment.setText(c.getComment());

		return convertView;
	}

	@Override
	protected XModel<Comment> realOnPullDown(RockyParams params)
	{
		if (!Utils.hasInternet(this))
		{
			return XModel.netError();
		}

		List<Comment> comments = new ArrayList<Comment>();
		comments = mCommentManager.findComment(mPkgname, mModule, mTitle, mOffset, mCount);

		return XModel.list(comments);
	}

	@Override
	protected XModel<Comment> realOnPullUp(RockyParams params)
	{
		if (!Utils.hasInternet(this))
		{
			return XModel.netError();
		}

		List<Comment> comments = new ArrayList<Comment>();
		comments = mCommentManager.findComment(mPkgname, mModule, mTitle, mOffset, mCount);

		return XModel.list(comments);
	}

}
