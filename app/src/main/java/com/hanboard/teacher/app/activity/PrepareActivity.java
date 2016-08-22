package com.hanboard.teacher.app.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.PreareSubjectAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.view.LoadingDialog;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Elements;
import com.hanboard.teacher.entity.PrepareSelectCourse;
import com.hanboard.teacher.model.impl.GetPrepareCourse;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrepareActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.activity_prepare_content)
    GridView mGridView;
    @BindView(R.id.top)
    LinearLayout topView;
    private LoadingDialog mLoadingDialog;
    public static final String TEXTBOOK_ID = "vid";
    public static final String TEXTBOOK_TITLE = "title";
    public  static  final String SUIT_AGE="suitage";
    private PrepareSelectCourse mCourse;
    private List<PrepareSelectCourse> subjects = new ArrayList<>();
   private PreareSubjectAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_prepare);
        ButterKnife.bind(this);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        initData();
        mGridView.setOnItemClickListener(this);
    }
    private void initData() {
        mAdapter = new PreareSubjectAdapter(this, R.layout.item_prepare_content, subjects);
        mGridView.setAdapter(mAdapter);
        mLoadingDialog = new LoadingDialog(this, "获取课本中..");
        GetPrepareCourse getPrepareCourse = new GetPrepareCourse();
        String accountId = (String) SharedPreferencesUtils.getParam(getApplication(), "id", "null");
        getPrepareCourse.getPrepareCourse(accountId, "1", new IDataCallback<Domine>() {
            @Override
            public void onSuccess(Domine data) {
                if(data instanceof Elements) {
                    List<PrepareSelectCourse> elements = ((Elements<PrepareSelectCourse>) data).elements;
                    subjects.addAll(elements);
                    mAdapter.notifyDataSetChanged();
                    mLoadingDialog.dismiss();
                }
            }
            @Override
            public void onError(String msg, int code) {
                ToastUtils.showShort(getApplication(), msg + code);
                mLoadingDialog.dismiss();
            }
        });
    }
    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void handler(Message msg) {

    }
    public void back(View view){
        onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle=new Bundle();
        bundle.putString(SUIT_AGE,subjects.get(i).getSuitObjectName());
        bundle.putString(TEXTBOOK_ID,subjects.get(i).getTeachBookId());
        startActivity(ChapterActivity.class,bundle);
    }
}
