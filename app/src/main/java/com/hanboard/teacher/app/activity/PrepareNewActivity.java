package com.hanboard.teacher.app.activity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.CourseAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.DisplayUtil;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.view.PopWinShare;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Elements;
import com.hanboard.teacher.entity.PrepareChapter;
import com.hanboard.teacher.model.INewCuorseModel;
import com.hanboard.teacher.model.impl.NewCourseiml;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrepareNewActivity extends BaseActivity implements IDataCallback<Domine>,AdapterView.OnItemClickListener{
    @BindView(R.id.preparenew_menu)
    ImageView preparenewMenu;
    @BindView(R.id.preparenew_content)
    GridView mCourseGv;
    @BindView(R.id.top)
    LinearLayout topView;
    private PopWinShare popWinShare;
    private Elements<PrepareChapter> mElements;
    private List<PrepareChapter> chapters = new ArrayList<PrepareChapter>();
    private INewCuorseModel iNewCuorseModel;
    private CourseAdapter mAdpter;
    public static String CONTENTID = "contentId";
    public static String CONTENTTITLE = "contentTitle";
    private String mSuitName=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_prepare_new);
        ButterKnife.bind(this);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        initData();
    }
    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            mSuitName = bundle.getString(PrepareActivity.SUIT_AGE,"");
            String bookId = bundle.getString(PrepareActivity.TEXTBOOK_ID);
            mAdpter = new CourseAdapter(this, R.layout.item_course, chapters, mSuitName);
            mCourseGv.setAdapter(mAdpter);
            mCourseGv.setOnItemClickListener(this);
            iNewCuorseModel = new NewCourseiml();
            showProgress("正在加载中....");
            iNewCuorseModel.getNewCourse((String) SharedPreferencesUtils.getParam(me, "id", "null"), bookId, "1", this);
        }else {
            mAdpter = new CourseAdapter(this, R.layout.item_course, chapters, mSuitName);
            mCourseGv.setAdapter(mAdpter);
            mCourseGv.setOnItemClickListener(this);
            iNewCuorseModel = new NewCourseiml();
            showProgress("正在加载中....");
            iNewCuorseModel.getNewCourse((String) SharedPreferencesUtils.getParam(me, "id", "null"),"", "1", this);
        }
    }
    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void handler(Message msg) {

    }

    @OnClick({R.id.preparenew_back, R.id.preparenew_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.preparenew_back:
                onBackPressed();
                break;
            case R.id.preparenew_menu:
                if (popWinShare == null) {
                    //自定义的单击事件
                    OnClickLintener paramOnClickListener = new OnClickLintener();
                    popWinShare = new PopWinShare(PrepareNewActivity.this, paramOnClickListener, DisplayUtil.dip2px(me, 140), DisplayUtil.dip2px(me, 300));
                    //监听窗口的焦点事件，点击窗口外面则取消显示
                    popWinShare.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (!hasFocus) {
                                popWinShare.dismiss();
                            }
                        }
                    });
                }
                //设置默认获取焦点
                popWinShare.setFocusable(true);
                //以某个控件的x和y的偏移量位置开始显示窗口
                popWinShare.showAsDropDown(preparenewMenu, -250, 0);
                //如果窗口存在，则更新
                popWinShare.update();
                break;
        }
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
            Log.i("=========", "doResultInt: ==下载成功了" + Thread.currentThread().getName()+chapters.size());
        }
    }
    @Override
    public void onError(String msg, int code) {
        disProgress();
        mElements = null;
        chapters.clear();
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
    ////////////////////
    class OnClickLintener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pop_touying:
                    ToastUtils.showShort(me, "进入投影");
                    break;
                case R.id.pop_record:
                    ToastUtils.showShort(me, "进入备课");

                    break;
                case R.id.pop_baiban:
                    ToastUtils.showShort(me, "进入录像");

                    break;
                case R.id.pop_prepare:
                    ToastUtils.showShort(me, "进入白板");

                    break;
                case R.id.pop_exit:
                    ToastUtils.showShort(me, "退出");

                    break;
                default:
                    break;
            }

        }

    }
}
