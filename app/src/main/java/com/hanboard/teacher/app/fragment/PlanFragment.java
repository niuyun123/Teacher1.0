package com.hanboard.teacher.app.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.activity.DetialsActivity;
import com.hanboard.teacher.app.adapter.PlanAdapter;
import com.hanboard.teacher.common.base.BaseFragment;
import com.hanboard.teacher.entity.LessonPlan;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends BaseFragment {
    @BindView(R.id.plan_lv_content)
    ListView mPlanLvContent;
    private List<Integer> titles = new ArrayList<>();
    private PlanAdapter mAdapter;
    private LessonPlan mPlans;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_plan, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void initData() {
        for (int i = 0; i < 5; i++) {
            titles.add(i+1);
        }
        mPlans = (LessonPlan) getArguments().getSerializable(DetialsActivity.TEACHINGPLAN);
        mAdapter = new PlanAdapter(context, R.layout.item_plan_content, titles, mPlans);
        mPlanLvContent.setAdapter(mAdapter);

    }

}
