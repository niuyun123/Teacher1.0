package com.hanboard.teacher.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.PrepareSelectCourse;

import java.util.List;

/**
 * 项目名称：Teacher1.0
 * 类描述：
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/16 0016 12:53
 */
public class TextBookRecyleAdapter extends RecyclerView.Adapter<TextBookRecyleAdapter.MyViewhoder> {
    private int clickTemp = -1;
    private List<PrepareSelectCourse> prepareSelectCourses;
    private Context context;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private MyItemLongClickListener mItemLongClickListener = null;

    public TextBookRecyleAdapter(List<PrepareSelectCourse> prepareSelectCourses, Context context) {
        this.prepareSelectCourses = prepareSelectCourses;
        this.context = context;
    }
    //标识选择的Item
    public void setSeclection(int position) {
        this.clickTemp = position;
    }
    @Override
    public MyViewhoder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewhoder(LayoutInflater.from(context).inflate(R.layout.create_textbook_item,parent,false));
    }

    @Override
    public void onBindViewHolder(final MyViewhoder holder, final int position) {
        holder.subject_name.setText(prepareSelectCourses.get(position).getSubjectName()/*+"（"+prepareSelectCourses.get(position).teachBookName+"）"*/);
        holder.version_name.setText(prepareSelectCourses.get(position).getVersionName());
        holder.nianji_name.setText(prepareSelectCourses.get(position).getSuitObjectName());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v,prepareSelectCourses.get(position));

                }
            }
        });
        holder.content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mItemLongClickListener!=null){
                    mItemLongClickListener.onItemLongClick(v,prepareSelectCourses.get(position));
                }
                return false;
            }
        });
        if(clickTemp == position){
            holder.content.setBackgroundResource(R.drawable.border_line_on_bg);
            holder.subject_name.setTextColor(Color.parseColor("#ffffff"));
            holder.version_name.setTextColor(Color.parseColor("#ffffff"));
            holder.nianji_name.setTextColor(Color.parseColor("#ffffff"));
        }else {
            holder.content.setBackgroundResource(R.drawable.border_line_bg);
            holder.subject_name.setTextColor(Color.parseColor("#4e4e4e"));
            holder.version_name.setTextColor(Color.parseColor("#4e4e4e"));
            holder.nianji_name.setTextColor(Color.parseColor("#4e4e4e"));
        }
    }
    @Override
    public int getItemCount() {
        return prepareSelectCourses.size();
    }
    static  class MyViewhoder extends RecyclerView.ViewHolder {
        TextView subject_name,version_name,nianji_name;
        RelativeLayout content;
        public MyViewhoder(View itemView) {
            super(itemView);
            subject_name= (TextView) itemView.findViewById(R.id.subject_name);
            version_name= (TextView) itemView.findViewById(R.id.version_name);
            nianji_name= (TextView) itemView.findViewById(R.id.nianji_name);
            content= (RelativeLayout) itemView.findViewById(R.id.create_textbook_content);
        }
    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view ,PrepareSelectCourse p);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    public static interface MyItemLongClickListener {
        void onItemLongClick(View view ,PrepareSelectCourse p);
    }
    public void setOnItemLongClickListener(MyItemLongClickListener listener){
        this.mItemLongClickListener = listener;
    }
}

