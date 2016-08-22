package com.hanboard.teacher.app.adapter;

import android.content.Context;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.PrepareChapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonAdapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonViewHolder;
import com.hanboard.teacherhd.lib.common.utils.TimeUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CourseAdapter extends CommonAdapter<PrepareChapter> {
    private String mSuitName;

    public CourseAdapter(Context context, int itemLayoutResId, List<PrepareChapter> dataSource, String mSuitName) {
        super(context, itemLayoutResId, dataSource);
        this.mSuitName = mSuitName;
    }

    /**
     * @param context         Context
     * @param itemLayoutResId
     * @param dataSource      数据源
     */
    public CourseAdapter(Context context, int itemLayoutResId, List<PrepareChapter> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    protected void fillItemData(CommonViewHolder viewHolder, int position, PrepareChapter item) {
        viewHolder.setTextForTextView(R.id.course_subject,item.content.getContentObject());
        viewHolder.setTextForTextView(R.id.course_title,item.getTitle());
        viewHolder.setTextForTextView(R.id.course_classhour,"课时 ("+item.content.getCourseHour()+")");
        viewHolder.setTextForTextView(R.id.course_createtime, "创建时间:  "+ TimeUtils.getTime(item.getCreateTime(), TimeUtils.DATE_FORMAT_DATE));
        viewHolder.setTextForTextView(R.id.course_modifyime, "修改时间:  "+ TimeUtils.getTime( item.content.getUpdateTime(), TimeUtils.DATE_FORMAT_DATE));
        viewHolder.setTextForTextView(R.id.course_suitage,mSuitName);
    }
}
