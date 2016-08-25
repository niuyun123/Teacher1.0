package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.CreateLessonsLvAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Lessons;
import com.hanboard.teacher.entity.MData;
import com.hanboard.teacher.entity.listentity.LessonsList;
import com.hanboard.teacher.entity.tree.Node;
import com.hanboard.teacher.model.ISelectTextBookModel;
import com.hanboard.teacher.model.impl.SelectTextBookModelImpl;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateLessonsDatilsActivity extends BaseActivity implements IDataCallback<Domine> {
    @BindView(R.id.tv_create_lessons_chapter)
    TextView tvCreateLessonsChapter;
    @BindView(R.id.top)
    RelativeLayout topView;
    @BindView(R.id.lv_create_lessons_datils)
    SlideAndDragListView lvCreateLessonsDatils;
    private ISelectTextBookModel iSelectTextBookModel;
    private Node chapter;
    private String accountId;
    private String mTextBookId;
    private List<Lessons> res = new ArrayList<>();
    private CreateLessonsLvAdapter mAdapter;
    public static CreateLessonsDatilsActivity instance = null;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_lessons_datils);
        ButterKnife.bind(this);
        instance = this;
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        iSelectTextBookModel = new SelectTextBookModelImpl();
        Intent intent = getIntent();
        chapter = (Node) intent.getSerializableExtra("chapter");
        mTextBookId = intent.getStringExtra("textbookId");
        tvCreateLessonsChapter.setText(chapter.getName());
        initMenu();
        accountId = (String) SharedPreferencesUtils.getParam(me, "id", "");
        iSelectTextBookModel.getPrepareLessons(chapter.getCid(), accountId, mTextBookId, String.valueOf(1), this);
    }
    private void initMenu() {
        Menu menu = new Menu(true, true, 0);
        menu.addItem(new MenuItem.Builder().setWidth(300)
                .setBackground(new ColorDrawable(Color.parseColor("#3c8ac3")))
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setText("删除")
                .setTextColor(Color.WHITE)
                .setTextSize(15)
                .build());
        lvCreateLessonsDatils.setMenu(menu);
        lvCreateLessonsDatils.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, final int itemPosition, int buttonPosition, int direction) {
                iSelectTextBookModel.deleteLessons(res.get(itemPosition).contentId, new IDataCallback<MData<String>>() {
                    @Override
                    public void onSuccess(MData<String> data) {
                        ToastUtils.showShort(me,data.message);
                        res.remove(itemPosition);
                        mAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onError(String msg, int code) {
                        ToastUtils.showShort(me,msg);
                    }
                });
                return 0;
            }
        });
    }
    @Override
    protected void handler(Message msg) {

    }
    @OnClick({R.id.back, R.id.tv_add_lessons})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_add_lessons:
                startActivity(AddLessonsActivity.class);
                break;
        }
    }

    @Override
    public void onSuccess(Domine data) {
        res.clear();
        if(data instanceof LessonsList){
            res = ((LessonsList) data).elements;
            mAdapter = new CreateLessonsLvAdapter(me,R.layout.create_lessons_datils_item,res);
            lvCreateLessonsDatils.setAdapter(mAdapter);
        }
    }
    @Override
    public void onError(String msg, int code) {

    }
}
