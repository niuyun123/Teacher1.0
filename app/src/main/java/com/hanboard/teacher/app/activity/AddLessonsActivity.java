package com.hanboard.teacher.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.view.ClearEditText;
import com.hanboard.teacher.common.view.WheelView;
import com.hanboard.teacher.entity.Pickers;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class AddLessonsActivity extends BaseActivity {
    @BindView(R.id.add_lessons_title)
    ClearEditText addLessonsTitle;
    @BindView(R.id.add_lessons_kexing)
    TextView addLessonsKexing;
    @BindView(R.id.add_lessons_keshi)
    ClearEditText addLessonsKeshi;
    private String mTitle;
    private String mKeshi;
    private String mKexing;
    private List<Pickers> list = new ArrayList<>();
    private WheelView mWlwSelect;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_lessons);
        ButterKnife.bind(this);
        Pickers p = new Pickers("新授课","1");
        Pickers p1 = new Pickers("练习课","2");
        Pickers p2 = new Pickers("复习课","3");
        Pickers p3 = new Pickers("讲评课","3");
        Pickers p4 = new Pickers("实验课","3");
        list.add(p);
        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
    }
    @Override
    protected void handler(Message msg) {

    }
    @OnClick({R.id.back, R.id.add_lessons_next_ok,R.id.lnl_add_lessons_kexing})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.add_lessons_next_ok:
                mTitle = addLessonsTitle.getText().toString();
                mKexing = addLessonsKexing.getText().toString();
                mKeshi = addLessonsKeshi.getText().toString();
                if(mTitle.equals("")){
                    ToastUtils.showShort(me,"请输入标题");
                }else if(mKexing.equals("请选择课型")){
                    ToastUtils.showShort(me,"请选择课型");
                }else if(mKeshi.equals("")){
                    ToastUtils.showShort(me,"请输入课时");
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("title",mTitle);
                    intent.putExtra("keshi",mKeshi);
                    intent.putExtra("kexing",mKexing);
                    intent.setClass(me,AddLessonsDatilsActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.lnl_add_lessons_kexing:
                View outerView = LayoutInflater.from(me).inflate(R.layout.wheel_view, null);
                mWlwSelect = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
                TextView t = (TextView) outerView.findViewById(R.id.wheel_view_tv);
                t.setText("请选择课型");
                mWlwSelect.setOffset(1);
                mWlwSelect.setItems(list);
                Dialog dialog = new AlertDialog.Builder(this)
                        .setView(outerView)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addLessonsKexing.setText(mWlwSelect.getSeletedItem().getShowConetnt());
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
        }
    }

}
