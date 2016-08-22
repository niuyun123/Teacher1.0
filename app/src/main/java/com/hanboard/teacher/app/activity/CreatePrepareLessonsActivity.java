package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.TextBookRecyleAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.view.CustomDialog;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Elements;
import com.hanboard.teacher.entity.MData;
import com.hanboard.teacher.entity.PrepareSelectCourse;
import com.hanboard.teacher.entity.Status;
import com.hanboard.teacher.entity.TextBook;
import com.hanboard.teacher.entity.tree.Node;
import com.hanboard.teacher.model.IGetPrepareCourse;
import com.hanboard.teacher.model.ISelectTextBookModel;
import com.hanboard.teacher.model.impl.GetPrepareCourse;
import com.hanboard.teacher.model.impl.SelectTextBookModelImpl;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreatePrepareLessonsActivity extends BaseActivity implements IDataCallback<Domine>,TextBookRecyleAdapter.OnRecyclerViewItemClickListener,TextBookRecyleAdapter.MyItemLongClickListener {
    @BindView(R.id.img_create_lessons_add_textbook)
    ImageView mImgCreateLessonsAddTextbook;
    @BindView(R.id.top)
    LinearLayout topView;
    private IGetPrepareCourse mIGetPrepareCourse;
    private List<PrepareSelectCourse> textBooks = new ArrayList<>();
    @BindView(R.id.create_textbook_rcw)
    RecyclerView mCreateTextbookRcw;
    @BindView(R.id.tv_create_lessons)
    TextView mTvCreateLessons;
    private TextBookRecyleAdapter mAdapter;
    private String mTextBookId;
    public static int REQUEST_CODE = 200;
    private String mTextBookName;
    private ISelectTextBookModel iSubjectModel;
    private Node chapter;
    private TextBook t;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_create_prepare_lessons);
        ButterKnife.bind(me);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        mIGetPrepareCourse = new GetPrepareCourse();
        iSubjectModel = new SelectTextBookModelImpl();
        loadData();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(me);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mCreateTextbookRcw.setLayoutManager(linearLayoutManager);
    }
    private void loadData() {
        showProgress("正在加载...");
        String accountId = (String) SharedPreferencesUtils.getParam(me, "id", "");
        mIGetPrepareCourse.getPrepareCourse(accountId, "1",this);
        mAdapter = new TextBookRecyleAdapter(textBooks, me);
    }
    @Override
    protected void handler(Message msg) {
        switch (msg.what) {
            case 0:
                mAdapter.notifyDataSetChanged();
                mCreateTextbookRcw.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(this);
                mAdapter.setOnItemLongClickListener(this);
                break;
            default:
                break;
        }
    }
    @Override
    public void onSuccess(Domine data) {
        if (data instanceof Elements) {
            disProgress();
            textBooks.addAll(((Elements<PrepareSelectCourse>) data).elements);
            handler.sendEmptyMessage(0);
        } else if (data instanceof Status) {
            disProgress();
            Toast.makeText(me, ((Status) data).message, Toast.LENGTH_SHORT).show();
            PrepareSelectCourse p = new PrepareSelectCourse();
            p.setSubjectName(t.subjectName);
            p.setSuitObjectName(t.suitObjectName);
            p.setSectionName(t.sectionName);
            p.setPublishDate(t.publishDate);
            p.setTeachBookId(t.id);
            p.setVersionName(t.versionName);
            p.setTeachBookName(t.title);
            textBooks.add(p);
            mAdapter.notifyDataSetChanged();
            mCreateTextbookRcw.setAdapter(mAdapter);
        }
    }
    @Override
    public void onError(String msg, int code) {
        disProgress();
        ToastUtils.showShort(me,msg);
    }
    @Override
    public void onItemClick(View view, PrepareSelectCourse data) {
        mAdapter.notifyDataSetChanged();
        mAdapter.setSeclection(textBooks.indexOf(data));
        mTextBookId = data.getTeachBookId();
        mTextBookName = data.getSuitObjectName() + data.getSubjectName() + data.getVersionName();
        mTvCreateLessons.setText("请选择章节");
        SharedPreferencesUtils.setParam(me,"textbookId",mTextBookId);
    }
    @OnClick({R.id.tv_create_lessons,R.id.img_create_lessons_add_textbook,R.id.create_lessons_ok,R.id.back})
    void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_create_lessons:
                if (mTvCreateLessons.getText().toString().equals("请先选择教材")) {
                    ToastUtils.showShort(me, "您还未选择教材");
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("tid", mTextBookId);
                    intent.putExtra("tname", mTextBookName);
                    intent.setClass(me, SelectChapterActivity.class);
                    startActivityForResult(intent, 300);
                }
                break;
            case R.id.img_create_lessons_add_textbook:
                Intent intent = new Intent();
                intent.setClass(me, SelectTextBookActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.create_lessons_ok:
                if(null==chapter){
                    ToastUtils.showShort(me,"您还未选择章节");
                }else {
                    SharedPreferencesUtils.setParam(me,"chapterId",chapter.getCid());
                    Intent in = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("chapter", chapter);
                    bundle.putString("textbookId",mTextBookId);
                    in.putExtras(bundle);
                    in.setClass(me,CreateLessonsDatilsActivity.class);
                    startActivity(in);
                }
                break;
            case R.id.back:
                finish();
                break;
        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == REQUEST_CODE) {
            Bundle b = data.getExtras();
            String textbookJson = b.getString("textbook");
            t = JsonUtil.fromJson(textbookJson, TextBook.class);
            String accountId = (String) SharedPreferencesUtils.getParam(me, "id", "");
            iSubjectModel.addTextbook(accountId,t, this);
        } else if (requestCode == 300 && resultCode == 300) {
            chapter = (Node) data.getSerializableExtra("chapter");
            mTvCreateLessons.setText(chapter.getName());
        }
    }
    private CustomDialog b;
    @Override
    public void onItemLongClick(View view, final PrepareSelectCourse p) {
        CustomDialog.Builder s = new CustomDialog.Builder(me, "提示信息！", "您是确定删除" + p.getSuitObjectName()+p.getSubjectName()+p.getVersionName() + "教材?", new CustomDialog.Builder.LeaveDialogListener() {
            @Override
            public void cancel(View v) {
                b.dismiss();
            }
            @Override
            public void confirm(View v) {
                iSubjectModel.deleteTextbook(p.id, new IDataCallback<MData<String>>() {
                    @Override
                    public void onSuccess(MData<String> data) {
                        textBooks.remove(p);
                        mAdapter.notifyDataSetChanged();
                        ToastUtils.showShort(me,"删除成功");
                        b.dismiss();
                    }
                    @Override
                    public void onError(String msg, int code) {
                        ToastUtils.showShort(me,msg);
                        b.dismiss();
                    }
                });
            }
        });
        b = s.create();
        b.show();
    }
}
