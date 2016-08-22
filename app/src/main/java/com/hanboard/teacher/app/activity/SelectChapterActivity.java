package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.TextBookAllChapterAdapter;
import com.hanboard.teacher.app.adapter.TreeListViewAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.view.CustomDialog;
import com.hanboard.teacher.entity.Chapter;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.listentity.ListChapter;
import com.hanboard.teacher.entity.tree.Node;
import com.hanboard.teacher.model.IPrepareLessonsModel;
import com.hanboard.teacher.model.impl.PrepareLessonsModelImpl;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectChapterActivity extends BaseActivity implements IDataCallback<Domine>,TreeListViewAdapter.OnTreeNodeClickListener {
    @BindView(R.id.top)
    RelativeLayout topView;
    @BindView(R.id.lv_select_textbook_chapter)
    ListView mLvSelectTextbookChapter;
    @BindView(R.id.tv_select_lessons_title)
    TextView mTvSelectLessonsTitle;
    private IPrepareLessonsModel mIPrepareLessonsModel;
    private String mTextbookId;
    private TextBookAllChapterAdapter<Chapter> mAdapter;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_chapter);
        mIPrepareLessonsModel = new PrepareLessonsModelImpl();
        ButterKnife.bind(this);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        Intent intent = getIntent();
        mTextbookId = intent.getStringExtra("tid");
        mTvSelectLessonsTitle.setText(intent.getStringExtra("tname"));
        loadData();
    }
    private void loadData() {
        showProgress("玩命加载中...");
        mIPrepareLessonsModel.getChapterList("2", mTextbookId, null, null, this);
    }
    @Override
    protected void handler(Message msg) {

    }
    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }
    @Override
    public void onSuccess(Domine data) {
        disProgress();
        if (data instanceof ListChapter) {
            try {
                mAdapter = new TextBookAllChapterAdapter<Chapter>(mLvSelectTextbookChapter, me, ((ListChapter) data).chapters, 0);
                mLvSelectTextbookChapter.setAdapter(mAdapter);
                mAdapter.setOnTreeNodeClickListener(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String msg, int code) {
        disProgress();
    }
    private CustomDialog b;
    @Override
    public void onClick(final Node node, int position) {
        if(node.getLevel()!=0) {
            CustomDialog.Builder s = new CustomDialog.Builder(me, "提示信息！", "您是否需要添加" + node.getName() + "章节?", new CustomDialog.Builder.LeaveDialogListener() {
                @Override
                public void cancel(View v) {
                    b.dismiss();
                }
                @Override
                public void confirm(View v) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chapter",node);
                    intent.putExtras(bundle);
                    setResult(300,intent);
                    finish();
                    b.dismiss();
                }
            });
            b = s.create();
            b.show();
        }
    }
}
