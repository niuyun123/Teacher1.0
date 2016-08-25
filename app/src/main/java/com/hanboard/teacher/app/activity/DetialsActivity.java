package com.hanboard.teacher.app.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.fragment.CouseWareFragment;
import com.hanboard.teacher.app.fragment.ExerciseFragment;
import com.hanboard.teacher.app.fragment.PlanFragment;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.entity.CoursewareInfo;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.LessonPlan;
import com.hanboard.teacher.model.IClassShowCouseModel;
import com.hanboard.teacher.model.impl.ClassShowCouseModelImp;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetialsActivity extends BaseActivity implements IDataCallback<Domine> {
    @BindView(R.id.detials_title)
    TextView mTitle;
    @BindView(R.id.detials_tv_left)
    TextView mTvLeft;
    @BindView(R.id.detials_tv_center)
    TextView mTvCenter;
    @BindView(R.id.detials_tv_right)
    TextView mTvRight;
    @BindView(R.id.detials_layout_content)
    FrameLayout montent;
    @BindView(R.id.top)
    LinearLayout topView;
    private String mCourseId;
    private PlanFragment mPlanFragment;
    private CouseWareFragment mCouseWareFragment;
    private ExerciseFragment mExerciseFragment;
    private Fragment currentFragment;
    private IClassShowCouseModel iClassShowCouseModel;
    private static final String TAG = "ClassActivity";
    private CoursewareInfo mCoursewareInfo;
    public static String TEACHINGPLAN = "teachplan";
    public static String COURSEWARES = "coursewares";
    public static String EXERCISES = "exercise";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detials);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        if (savedInstanceState!=null){
            Bundle extras = savedInstanceState.getBundle("temp");
            String mCourseId = extras.getString(CourseActivity.CONTENTID);
            mTitle.setText(extras.getString(CourseActivity.CONTENTTITLE));
            ToastUtils.showShort(this, mCourseId);
            iClassShowCouseModel = new ClassShowCouseModelImp();
            showProgress("正在加载中...");
            iClassShowCouseModel.getAllCouseWareInfo(mCourseId, this, this);
        }else
        initData();
    }
    private void initData() {
        Bundle extras = getIntent().getExtras();
        String mCourseId = extras.getString(CourseActivity.CONTENTID);
        mTitle.setText(extras.getString(CourseActivity.CONTENTTITLE));
        iClassShowCouseModel = new ClassShowCouseModelImp();
        showProgress("正在加载中...");
        iClassShowCouseModel.getAllCouseWareInfo(mCourseId, this, this);
    }
    //初始化fragment
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initFragment() {
        mTvLeft.setBackground(this.getResources().getDrawable(R.drawable.bg_detials_textviewleftback));
        if (mPlanFragment == null) {
            mPlanFragment = new PlanFragment();
        }
        if (!mPlanFragment.isAdded()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            LessonPlan lessonPlan = mCoursewareInfo.lessonPlan;
            Bundle bundle = new Bundle();
            bundle.putSerializable(TEACHINGPLAN, lessonPlan);
            mPlanFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.detials_layout_content, mPlanFragment).commit();
            currentFragment = mPlanFragment;
        }
    }
    //切换fragment的方法
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if (currentFragment == fragment)
            return;
        if (!fragment.isAdded()) {
            transaction.hide(currentFragment).add(R.id.detials_layout_content, fragment);
            if (fragment instanceof CouseWareFragment) {
                String courseWareJson = JsonUtil.toJson(mCoursewareInfo.courseWares);
                Bundle bundle = new Bundle();
                bundle.putString(COURSEWARES, courseWareJson);
                fragment.setArguments(bundle);
            } else if (fragment instanceof ExerciseFragment) {
                String courseWareJson = JsonUtil.toJson(mCoursewareInfo.exercises);
                Bundle bundle = new Bundle();
                bundle.putString(EXERCISES, courseWareJson);
                fragment.setArguments(bundle);
            }
            transaction.commit();
        } else {
            transaction.hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
    }
    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }
    @Override
    protected void handler(Message msg) {

    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @OnClick({R.id.detials_tv_left, R.id.detials_tv_center, R.id.detials_tv_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.detials_tv_left:
                resetButtonBackColor();
                mTvLeft.setBackground(this.getResources().getDrawable(R.drawable.bg_detials_textviewleftback));
                if (mPlanFragment == null) {
                    mPlanFragment = new PlanFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), mPlanFragment);
                break;
            case R.id.detials_tv_center:
                resetButtonBackColor();
                mTvCenter.setBackgroundResource(R.color.theme_color);
                if (mCouseWareFragment == null) {
                    mCouseWareFragment = new CouseWareFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), mCouseWareFragment);
                break;
            case R.id.detials_tv_right:
                resetButtonBackColor();
                mTvRight.setBackground(this.getResources().getDrawable(R.drawable.bg_detials_textviewrightback));
                if (mExerciseFragment == null) {
                    mExerciseFragment = new ExerciseFragment();
                }
                addOrShowFragment(getSupportFragmentManager().beginTransaction(), mExerciseFragment);
                break;
        }
    }
    //重置背景颜色
    @TargetApi(16)
    private void resetButtonBackColor() {
        mTvLeft.setBackground(this.getResources().getDrawable(R.drawable.bg_detials_textviewleft));
        mTvRight.setBackground(this.getResources().getDrawable(R.drawable.bg_detials_textviewright));
        mTvCenter.setBackground(this.getResources().getDrawable(R.drawable.bg_detials_textviewcenter));
    }
    @Override
    public void onSuccess(Domine data) {
        disProgress();
        if (data instanceof CoursewareInfo) {
            mCoursewareInfo = ((CoursewareInfo) data);
            initFragment();
        }
    }

    @Override
    public void onError(String msg, int code) {
        disProgress();
        ToastUtils.showShort(this,msg);
        finish();
    }
    public void back(View view){
        onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("temp",getIntent().getExtras());
    }
}
