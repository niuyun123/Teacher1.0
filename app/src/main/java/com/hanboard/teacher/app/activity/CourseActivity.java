package com.hanboard.teacher.app.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.CourseAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Elements;
import com.hanboard.teacher.entity.PrepareChapter;
import com.hanboard.teacher.model.IPrepareLessonsModel;
import com.hanboard.teacher.model.impl.PrepareLessonsModelImpl;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.quanke.app.libs.emptylayout.EmptyLayout;

public class CourseActivity extends BaseActivity implements IDataCallback<Domine>, AdapterView.OnItemClickListener {
    @BindView(R.id.course_gv_content)
    GridView mCourseGv;
    @BindView(R.id.top)
    LinearLayout topView;
    private Elements<PrepareChapter> mElements;
    private List<PrepareChapter> chapters = new ArrayList<PrepareChapter>();
    private IPrepareLessonsModel iPrepareLessonsModel;
    private CourseAdapter mAdpter;
    private String mSuitName;
    public static String CONTENTID = "contentId";
    public static String CONTENTTITLE = "contentTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_course);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        String chapterId = bundle.getString(ChapterActivity.CHAPTERID);
        String bookId = bundle.getString(PrepareActivity.TEXTBOOK_ID);
        mSuitName=bundle.getString(PrepareActivity.SUIT_AGE);
        mAdpter = new CourseAdapter(this, R.layout.item_course, chapters,mSuitName);
        mCourseGv.setAdapter(mAdpter);
        mCourseGv.setOnItemClickListener(this);
        iPrepareLessonsModel = new PrepareLessonsModelImpl();
        showProgress("正在加载中....");
        iPrepareLessonsModel.getChapterDetials((String) SharedPreferencesUtils.getParam(me, "id", "null"),chapterId, bookId, "1", this);

    }
    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }
    @Override
    protected void handler(Message msg) {

    }
    @Override
    public void onSuccess(Domine data) {
        disProgress();
        if (data instanceof Elements) {
            mElements = null;
            chapters.clear();
            mElements = (Elements<PrepareChapter>) data;
            chapters.addAll(mElements.elements);
            mAdpter.notifyDataSetChanged();
        }
    }
    @Override
    public void onError(String msg, int code) {
        disProgress();
        mElements = null;
        chapters.clear();
        EmptyLayout emptyLayout = new EmptyLayout(me);
        emptyLayout.showEmpty();
        setContentView(emptyLayout);
    }
    public void back(View view) {
        onBackPressed();
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle = new Bundle();
        String contentId = mElements.elements.get(i).getContentId();
        String contentTitle=mElements.elements.get(i).getTitle();
        bundle.putString(CONTENTID, contentId);
        bundle.putString(CONTENTTITLE, contentTitle);
        startActivity(DetialsActivity.class,bundle);
    }
}
