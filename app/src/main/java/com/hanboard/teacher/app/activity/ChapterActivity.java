package com.hanboard.teacher.app.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.TextBookAllChapterAdapter;
import com.hanboard.teacher.app.adapter.TreeListViewAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.entity.Chapter;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.listentity.ListChapter;
import com.hanboard.teacher.entity.tree.Node;
import com.hanboard.teacher.model.IPrepareLessonsModel;
import com.hanboard.teacher.model.impl.PrepareLessonsModelImpl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChapterActivity extends BaseActivity implements IDataCallback<Domine>, TreeListViewAdapter.OnTreeNodeClickListener {
    @BindView(R.id.chapter_lv_content)
    ListView mLvChapterList;
    @BindView(R.id.top)
    LinearLayout topView;
    private IPrepareLessonsModel iPrepareLessonsModel;
    private TreeListViewAdapter mAdapter;
    private String mBookId;
    private String mSuitName;
    public static final String CHAPTERID = "chapterid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chapter);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        iPrepareLessonsModel = new PrepareLessonsModelImpl();
        Bundle bundle = getIntent().getExtras();
        mBookId = bundle.getString(PrepareActivity.TEXTBOOK_ID);
        mSuitName = bundle.getString(PrepareActivity.SUIT_AGE);
        showProgress("正在加载...");
        iPrepareLessonsModel.getChapterList("2", mBookId, "", "", this);

    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void handler(Message msg) {

    }

    public void back(View view) {
        onBackPressed();
    }

    ////////////////////////////////////////////
    @Override
    public void onSuccess(Domine data) {
        if (data instanceof ListChapter) {
            try {
                List<Chapter> chapters = ((ListChapter) data).chapters;
                mAdapter = new TextBookAllChapterAdapter<Chapter>(mLvChapterList, this, chapters, 0);
                mLvChapterList.setAdapter(mAdapter);
                mAdapter.setOnTreeNodeClickListener(this);
                disProgress();
                //BookAndChapterId bookAndChapterId = new BookAndChapterId(this, chapters.get(0).getId());
                //SharedPreferencesUtils.setParam(this, BOOKANDCHAPTERID, bookAndChapterId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(String msg, int code) {
        disProgress();
    }

    @Override
    public void onClick(Node node, int position) {
        if (node.isLeaf()) {
            Bundle bundle = new Bundle();
            bundle.putString(CHAPTERID, node.getCid());
            bundle.putString(PrepareActivity.SUIT_AGE, mSuitName);
            bundle.putString(PrepareActivity.TEXTBOOK_ID, mBookId);
            startActivity(CourseActivity.class, bundle);
        }

    }

    @OnClick(R.id.home_newcourse)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putString(PrepareActivity.SUIT_AGE, mSuitName);
        bundle.putString(PrepareActivity.TEXTBOOK_ID, mBookId);
        startActivity(PrepareNewActivity.class, bundle);
    }
}
