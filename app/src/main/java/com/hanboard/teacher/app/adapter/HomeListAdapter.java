package com.hanboard.teacher.app.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.activity.CreatePrepareLessonsActivity;
import com.hanboard.teacher.app.activity.PrepareActivity;

/**
 * Created by Administrator on 2016/8/16.
 */
public class HomeListAdapter extends BaseAdapter {
    private Context context;
    public HomeListAdapter(Context context) {
        this.context = context;
    }

    private int[] pics={R.mipmap.prapareimg, R.mipmap.startclassimg, R.mipmap.wordimg, R.mipmap.imgcoursemanager};
    @Override
    public int getCount() {
        return pics.length;
    }

    @Override
    public Object getItem(int i) {
        return pics[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        if (view==null){
            holder=new ViewHolder();
            view= LayoutInflater.from(context).inflate(R.layout.item_home_function,viewGroup,false);
            TextView img = (TextView) view.findViewById(R.id.home_im_function);
            holder.homeFunction=img;
            view.setTag(holder);
        }holder= (ViewHolder) view.getTag();
        holder.homeFunction.setBackground(context.getResources().getDrawable(pics[i]));
        holder.homeFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (i==0){
                    context.startActivity(new Intent(context,CreatePrepareLessonsActivity.class));
                }else if(i==1){
                    Intent in=new Intent(context, PrepareActivity.class);
                    context.startActivity(in);
                }else if(i==2){


                }else {


                }


            }
        });


        return view;
    }
    public  class ViewHolder{
        TextView homeFunction;
    }

}
