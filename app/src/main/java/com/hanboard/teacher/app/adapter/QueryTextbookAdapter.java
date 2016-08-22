package com.hanboard.teacher.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.TextBook;
import com.hanboard.teacherhd.lib.common.adapter.CommonAdapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonViewHolder;

import java.util.List;

/**
 * 项目名称：Teacher1.0
 * 类描述：
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/16 0016 16:39
 */
public class QueryTextbookAdapter extends CommonAdapter<TextBook> {

    private int clickTemp = -1;
    //标识选择的Item
    public void setSeclection(int position) {
        clickTemp = position;
    }

    /**
     * @param context         Context
     * @param itemLayoutResId
     * @param dataSource      数据源
     */
    public QueryTextbookAdapter(Context context, int itemLayoutResId, List<TextBook> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, TextBook item) {
        if (clickTemp == position) {
            viewHolder.getContentView().setBackgroundResource(R.drawable.border_line_on_bg);
            ((TextView)viewHolder.getContentView().findViewById(R.id.tv_query_subject_name)).setTextColor(Color.parseColor("#ffffff"));
            ((TextView)viewHolder.getContentView().findViewById(R.id.tv_query_date)).setTextColor(Color.parseColor("#ffffff"));
            ((TextView)viewHolder.getContentView().findViewById(R.id.tv_query_versionName)).setTextColor(Color.parseColor("#ffffff"));
        } else {
            viewHolder.getContentView().setBackgroundResource(R.drawable.border_line_bg);
            ((TextView)viewHolder.getContentView().findViewById(R.id.tv_query_subject_name)).setTextColor(Color.parseColor("#595a5a"));
            ((TextView)viewHolder.getContentView().findViewById(R.id.tv_query_date)).setTextColor(Color.parseColor("#595a5a"));
            ((TextView)viewHolder.getContentView().findViewById(R.id.tv_query_versionName)).setTextColor(Color.parseColor("#595a5a"));
        }
//        viewHolder.setTextForTextView(R.id.tv_query_subject_name,item.subjectName+"（"+item.title.substring(item.title.indexOf("（") + 1,item.title.indexOf("）"))+"）");
        viewHolder.setTextForTextView(R.id.tv_query_subject_name,item.subjectName);
        viewHolder.setTextForTextView(R.id.tv_query_date,item.publishDate);
        viewHolder.setTextForTextView(R.id.tv_query_versionName,item.versionName);
    }
}
