package com.hanboard.teacher.app.adapter;

import android.content.Context;
import android.text.Html;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.LessonPlan;
import com.hanboard.teacherhd.lib.common.adapter.CommonAdapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class PlanAdapter extends CommonAdapter<Integer> {
    /**
     * @param context         Context
     * @param itemLayoutResId
     * @param dataSource      数据源
     */
    private LessonPlan plans;
    public static final String TEACHING_T = "教学目标";
    public static final String TEACHING_I = "教学重点";
    public static final String TEACHING_P = "教学准备";
    public static final String TEACHING_G = "教学过程";
    public static final String TEACHING_H = "作业布置";

    public PlanAdapter(Context context, int itemLayoutResId, List<Integer> dataSource, LessonPlan plans) {
        super(context, itemLayoutResId, dataSource);
        this.plans = plans;
    }

    public PlanAdapter(Context context, int itemLayoutResId, List<Integer> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, Integer item) {
        switch (item) {
            case 1:
                viewHolder.setTextForTextView(R.id.plan_tv_title, TEACHING_T);
                if (plans.lessonPlanGoal!=null){
                    viewHolder.setTextForTextView(R.id.plan_tv_content, Html.fromHtml(plans.lessonPlanGoal));
                }else viewHolder.setTextForTextView(R.id.plan_tv_content,"没有预备信息");
                break;
            case 2:
                viewHolder.setTextForTextView(R.id.plan_tv_title, TEACHING_I);
                if (plans.lessonPlanKeyPoint!=null){
                    viewHolder.setTextForTextView(R.id.plan_tv_content, Html.fromHtml(plans.lessonPlanKeyPoint));
                }else viewHolder.setTextForTextView(R.id.plan_tv_content,"没有预备信息");

                break;
            case 3:
                viewHolder.setTextForTextView(R.id.plan_tv_title, TEACHING_P);
                if (plans.lessonPlanPrepare!=null){
                    viewHolder.setTextForTextView(R.id.plan_tv_content, Html.fromHtml(plans.lessonPlanPrepare));
                }else viewHolder.setTextForTextView(R.id.plan_tv_content,"没有预备信息");
                break;
            case 4:
                viewHolder.setTextForTextView(R.id.plan_tv_title, TEACHING_G);
                if (plans.lessonPlanProcess!=null){
                    viewHolder.setTextForTextView(R.id.plan_tv_content, Html.fromHtml(plans.lessonPlanProcess));
                }else viewHolder.setTextForTextView(R.id.plan_tv_content,"没有预备信息");
                break;
            case 5:
                viewHolder.setTextForTextView(R.id.plan_tv_title, TEACHING_H);
                if (plans.lessonPlanWord!=null){
                    viewHolder.setTextForTextView(R.id.plan_tv_content, Html.fromHtml(plans.lessonPlanWord));
                }else viewHolder.setTextForTextView(R.id.plan_tv_content,"没有预备信息");
                break;
        }
    }
}
