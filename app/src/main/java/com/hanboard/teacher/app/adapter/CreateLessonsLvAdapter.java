package com.hanboard.teacher.app.adapter;

import android.content.Context;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.Lessons;
import com.hanboard.teacherhd.lib.common.adapter.CommonAdapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonViewHolder;
import com.hanboard.teacherhd.lib.common.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 项目名称：Teacher1.0
 * 类描述：
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/17 0017 13:38
 */
public class CreateLessonsLvAdapter extends CommonAdapter<Lessons> {

    /**
     * @param context         Context
     * @param itemLayoutResId
     * @param dataSource      数据源
     */
    public CreateLessonsLvAdapter(Context context, int itemLayoutResId, List<Lessons> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }
    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, Lessons item) {
        viewHolder.setTextForTextView(R.id.create_lessons_title,item.content.contentTitle);
        viewHolder.setTextForTextView(R.id.ctrate_lesson_kexing,item.content.contentObject);
        viewHolder.setTextForTextView(R.id.ctrate_lesson_keshi,item.content.courseHour+"");
        viewHolder.setTextForTextView(R.id.ctrate_lesson_time, TimeUtils.getTime(item.content.createTime,new SimpleDateFormat("yyyy-MM-dd")));
    }
}
