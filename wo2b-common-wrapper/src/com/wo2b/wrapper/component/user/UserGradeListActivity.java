package com.wo2b.wrapper.component.user;

import opensource.component.pulltorefresh.PullToRefreshBase.Mode;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wo2b.sdk.common.util.ViewUtils;
import com.wo2b.wrapper.R;
import com.wo2b.wrapper.app.RockyListFragmentActivity;
import com.wo2b.xxx.webapp.manager.user.Grade;
import com.wo2b.xxx.webapp.manager.user.GradeManager;
import com.wo2b.xxx.webapp.manager.user.User;
import com.wo2b.xxx.webapp.manager.user.UserManager;

/**
 * 用户所有等级信息
 * 
 * @author Rocky
 * @email ixueyongjia@gmail.com
 * 
 */
public class UserGradeListActivity extends RockyListFragmentActivity<Grade> implements View.OnClickListener
{

	private UserManager mUserManager;

	private int mCurrentExp = 0;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wrapper_user_grade_list);
		setActionBarTitle(R.string.wo2b_grade);
		setPullMode(Mode.DISABLED);

		mUserManager = UserManager.getInstance();
		if (mUserManager.isLogin())
		{
			User user = mUserManager.getMemoryUser();
			mCurrentExp = (int) user.getExp();
		}

		RockyParams params = new RockyParams();
		realExecuteFirstTime(params);
	}

	@Override
	public void onClick(View v)
	{
		// 增加获取积分的显示
	}

	@Override
	protected XModel<Grade> realOnPullDown(RockyParams params)
	{
		return XModel.list(GradeManager.getGradeList());
	}

	@Override
	protected XModel<Grade> realOnPullUp(RockyParams params)
	{
		return null;
	}

	@Override
	protected View realGetView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = getLayoutInflater().inflate(R.layout.wrapper_user_grade_list_item, parent, false);
		}

		ImageView iv_level_icon = ViewUtils.get(convertView, R.id.iv_level_icon);
		TextView tv_grade_name = ViewUtils.get(convertView, R.id.tv_grade_name);
		TextView tv_point_range = ViewUtils.get(convertView, R.id.tv_point_range);
		ProgressBar pb_exp = ViewUtils.get(convertView, R.id.pb_exp);

		Grade grade = realGetItem(position);
		String range_string = getString(R.string.grade_exp_range, grade.getOffset(), grade.getEnd());
		int level_icon = getResources().getIdentifier("level_" + (++position), "drawable", this.getPackageName());
		iv_level_icon.setImageResource(level_icon);

		tv_grade_name.setText(grade.getGradeName());
		tv_point_range.setText(range_string);

		if (mCurrentExp >= grade.getOffset() && mCurrentExp <= grade.getEnd())
		{
			pb_exp.setVisibility(View.VISIBLE);
			pb_exp.setMax(grade.getEnd() - grade.getOffset());
			pb_exp.setProgress(mCurrentExp - grade.getOffset());

			tv_grade_name.getPaint().setFakeBoldText(true);
		}
		else
		{
			pb_exp.setVisibility(View.GONE);

			tv_grade_name.getPaint().setFakeBoldText(false);
		}
			
		
		// ImageView iv_selected_flag = ViewUtils.get(convertView, R.id.iv_selected_flag);
		// if (grade.getGradeName().equalsIgnoreCase(mCurrentGrade))
		// {
		// iv_selected_flag.setVisibility(View.VISIBLE);
		// }
		// else
		// {
		// iv_selected_flag.setVisibility(View.GONE);
		// }

		return convertView;
	}

}