package com.hanboard.teacher.app.adapter;

import android.content.Context;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.PrepareSelectCourse;
import com.hanboard.teacherhd.lib.common.adapter.CommonAdapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/8/16.
 */
public class PreareSubjectAdapter extends CommonAdapter<PrepareSelectCourse> {
    /**
     * @param context         Context
     * @param itemLayoutResId
     * @param dataSource      数据源
     */
    public PreareSubjectAdapter(Context context, int itemLayoutResId, List<PrepareSelectCourse> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, PrepareSelectCourse item) {
         viewHolder.setTextForTextView(R.id.prepare_tv_subject,item.getSubjectName());
         viewHolder.setTextForTextView(R.id.prepare_tv_class,item.getSuitObjectName());

    }
}
