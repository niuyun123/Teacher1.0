package com.hanboard.teacher.app.adapter;

import android.content.Context;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.CourseWare;
import com.hanboard.teacher.entity.Exercises;
import com.hanboard.teacherhd.lib.common.adapter.CommonAdapter;
import com.hanboard.teacherhd.lib.common.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2016/8/17.
 */
public class CousewareAndExerciseAdapter<T> extends CommonAdapter<T> {
    /**
     * @param context         Context
     * @param itemLayoutResId
     * @param dataSource      数据源
     */
    public CousewareAndExerciseAdapter(Context context, int itemLayoutResId, List<T> dataSource) {
        super(context, itemLayoutResId, dataSource);
    }

    @Override
    protected void fillItemData(CommonViewHolder viewHolder, int position, T item) {
        if (item != null) {
            if (item instanceof Exercises){
                String exercisesTitle = ((Exercises) item).exercisesTitle;
                String exercisesTypeString = ((Exercises) item).exercisesType;
                int exercisesTypeInt=Integer.parseInt(exercisesTypeString);
                viewHolder.setTextForTextView(R.id.file_title,exercisesTitle);
                if (exercisesTypeInt==1)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_ppt);
                else if (exercisesTypeInt==2)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_doc);
                else if (exercisesTypeInt==4)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_xls);
                else if (exercisesTypeInt==5)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_mp3);
                else if (exercisesTypeInt==6)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_mp4);
                else if (exercisesTypeInt==7){
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_photo);
                }
                else  if (exercisesTypeInt==8)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_pdf);
                else  viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_pdf);
            }else if (item instanceof CourseWare){
                String courseWareTitle = ((CourseWare) item).courseWareTitle;
                String courseWareTypeString = ((CourseWare) item).courseWareType;
                int courseWareTypeInt=Integer.parseInt(courseWareTypeString);
                viewHolder.setTextForTextView(R.id.file_title,courseWareTitle);
                if (courseWareTypeInt==1)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_ppt);
                else if (courseWareTypeInt==2)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_word);
                else if (courseWareTypeInt==4)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_xls);
                else if (courseWareTypeInt==5)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_mp3);
                else if (courseWareTypeInt==6)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_mp4);
                else if (courseWareTypeInt==7){
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_photo);
                }
                else  if (courseWareTypeInt==8)
                    viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_pdf);
                else  viewHolder.setImageForView(R.id.file_imglog, R.mipmap.log_pdf);
            }
        }else  viewHolder.setTextForTextView(R.id.file_title,"还没有上传资料");
    }
}
