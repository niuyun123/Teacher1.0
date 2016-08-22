package com.hanboard.teacher.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.hanboard.teacher.R;
import com.hanboard.teacher.common.base.BaseFragment;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 项目名称：Teacher1.0
 * 类描述：
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/17 0017 15:35
 */
public class CreateTeachingPlanFragment extends BaseFragment {
    @BindView(R.id.add_lessons_teachingplan_mubiao)
    EditText addLessonsTeachingplanMubiao;
    @BindView(R.id.add_lessons_teachingplan_zhongdian)
    EditText addLessonsTeachingplanZhongdian;
    @BindView(R.id.add_lessons_teachingplan_zhunbei)
    EditText addLessonsTeachingplanZhunbei;
    @BindView(R.id.add_lessons_teachingplan_guocheng)
    EditText addLessonsTeachingplanGuocheng;
    @BindView(R.id.add_lessons_teachingplan_zuoyebuzhi)
    EditText addLessonsTeachingplanZuoyebuzhi;
    private String mubiao;
    private String zhongdian;
    private String zhunbei;
    private String guocheng;
    private String zuoyebuzhi;
    private String kexing;
    private String keshi;
    private String title;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_add_teaching_plan, container, false);
    }
    @Override
    protected void initData() {

    }
    public void save() {
        mubiao = addLessonsTeachingplanMubiao.getText().toString();
        zhongdian = addLessonsTeachingplanZhongdian.getText().toString();
        zhunbei = addLessonsTeachingplanZhunbei.getText().toString();
        guocheng = addLessonsTeachingplanGuocheng.getText().toString();
        zuoyebuzhi = addLessonsTeachingplanZuoyebuzhi.getText().toString();
        SharedPreferencesUtils.setParam(context, "mubiao", mubiao);
        SharedPreferencesUtils.setParam(context, "guocheng", guocheng);
        SharedPreferencesUtils.setParam(context, "zhongdian", zhongdian);
        SharedPreferencesUtils.setParam(context, "zhunbei", zhunbei);
        SharedPreferencesUtils.setParam(context, "zuoyebuzhi", zuoyebuzhi);
        ToastUtils.showShort(context,"教案本地保存成功");
    }
    @OnClick(R.id.save_teaching_plan)
    public void onClick() {
        save();
    }
}
