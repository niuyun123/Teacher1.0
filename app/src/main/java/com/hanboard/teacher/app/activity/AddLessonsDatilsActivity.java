package com.hanboard.teacher.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.reflect.TypeToken;
import com.hanboard.teacher.R;
import com.hanboard.teacher.app.fragment.CreateCourseFragment;
import com.hanboard.teacher.app.fragment.CreateExercisesFragment;
import com.hanboard.teacher.app.fragment.CreateTeachingPlanFragment;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.JsonCallback;
import com.hanboard.teacher.common.config.BaseMap;
import com.hanboard.teacher.common.config.CoderConfig;
import com.hanboard.teacher.common.config.Constants;
import com.hanboard.teacher.common.config.Urls;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.view.DowloadDialog;
import com.hanboard.teacher.entity.LoadRes;
import com.hanboard.teacher.entity.MData;
import com.hanboard.teacher.entity.RequestInfo;
import com.hanboard.teacherhd.lib.common.http.okhttp.callback.StringCallback;
import com.hanboard.teacherhd.lib.common.utils.DESCoder;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.BaseRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class AddLessonsDatilsActivity extends BaseActivity {
    @BindView(R.id.tl_3)
    SegmentTabLayout tl;
    @BindView(R.id.vp_2)
    ViewPager vp;
    @BindView(R.id.top)
    LinearLayout topView;
    private String[] mTitles = {"教案", "课件", "习题"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private String title;
    private String keshi;
    private String kexing;
    private DowloadDialog mProgress;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add_lessons_datils);
        ButterKnife.bind(this);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        getData();
        init();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void handler(Message msg) {

    }

    private void getData() {
        Intent in = getIntent();
        title = in.getStringExtra("title");
        keshi = in.getStringExtra("keshi");
        kexing = in.getStringExtra("kexing");
        SharedPreferencesUtils.setParam(me, "title", title);
        SharedPreferencesUtils.setParam(me, "keshi", keshi);
        SharedPreferencesUtils.setParam(me, "kexing", kexing);
    }

    private void init() {
        mFragments.add(new CreateTeachingPlanFragment());
        mFragments.add(new CreateCourseFragment());
        mFragments.add(new CreateExercisesFragment());
        tl();
    }


    @OnClick(R.id.lessons_upload)
    public void onClick() {
        /**章节ID*/
        String chapterId = (String) SharedPreferencesUtils.getParam(me,"chapterId","");
        /**课本ID*/
        String textbookId = (String) SharedPreferencesUtils.getParam(me,"textbookId","");
        /*账户ID*/
        String accountId = (String)SharedPreferencesUtils.getParam(me,"id","");
        /*教案*/
        String mubiao = (String) SharedPreferencesUtils.getParam(me,"mubiao","");
        String guocheng = (String) SharedPreferencesUtils.getParam(me,"guocheng","");
        String zhongdian = (String) SharedPreferencesUtils.getParam(me,"zhongdian","");
        String zhunbei = (String) SharedPreferencesUtils.getParam(me,"zhunbei","");
        String zuoyebuzhi = (String) SharedPreferencesUtils.getParam(me,"zuoyebuzhi","");
        String kexing = (String) SharedPreferencesUtils.getParam(me,"kexing","");
        String keshi = (String) SharedPreferencesUtils.getParam(me,"keshi","");
        String title = (String) SharedPreferencesUtils.getParam(me,"title","");
        /*习题*/
        String xitisJson = (String) SharedPreferencesUtils.getParam(me,"exercisesJson","");
        /*课件*/
        String kejiansJson = (String) SharedPreferencesUtils.getParam(me,"courseJson","");
        List<LoadRes> xitis = JsonUtil.fromJson(xitisJson, new TypeToken<List<LoadRes>>() {}.getType());
        List<LoadRes> kejians = JsonUtil.fromJson(kejiansJson, new TypeToken<List<LoadRes>>() {}.getType());
        Map<String, String> params = null;
        List<File> xitiFiles = new ArrayList<>();
        if (xitis != null && xitis.size() > 0) {
            for (int i = 0; i < xitis.size(); i++) {
                xitiFiles.add(new File(xitis.get(i).path));
                Log.e("TAG", xitis.get(i).path);
            }
        }
        List<File> kejianFiles = new ArrayList<>();
        if (kejians != null && kejians.size() > 0) {
            for (int i = 0; i < kejians.size(); i++) {
                kejianFiles.add(new File(kejians.get(i).path));
                Log.e("TAG", kejians.get(i).path);
            }
        }
        try {
            params = new BaseMap().initMap();
            params.put("lessonPlanGoal",new DESCoder(CoderConfig.CODER_CODE).encrypt(mubiao));
            params.put("lessonPlanKeyPoint",new DESCoder(CoderConfig.CODER_CODE).encrypt(zhongdian));
            params.put("lessonPlanPrepare",new DESCoder(CoderConfig.CODER_CODE).encrypt(zhunbei));
            params.put("lessonPlanProcess",new DESCoder(CoderConfig.CODER_CODE).encrypt(guocheng));
            params.put("lessonPlanWord",new DESCoder(CoderConfig.CODER_CODE).encrypt(zuoyebuzhi));
            params.put("accountId",new DESCoder(CoderConfig.CODER_CODE).encrypt(accountId));
            params.put("contentObject",new DESCoder(CoderConfig.CODER_CODE).encrypt(kexing));
            params.put("chapterId",new DESCoder(CoderConfig.CODER_CODE).encrypt(chapterId));
            params.put("teachBookId",new DESCoder(CoderConfig.CODER_CODE).encrypt(textbookId));
            params.put("contentTitle",new DESCoder(CoderConfig.CODER_CODE).encrypt(title));
            params.put("courseHour",keshi);
            if(kejianFiles.size()==0&&xitiFiles.size()==0){
                com.hanboard.teacherhd.lib.common.http.okhttp.OkHttpUtils
                        .post()
                        .url(Urls.URL_ADDLESSONSINFO_NOTFILE)
                        .params(params)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showShort(me,e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        MData<String> res = JsonUtil.fromJson(response,new TypeToken<MData<String>>(){}.getType());
                        if(res.code.equals(Constants.CODE_SUCCESS)) {
                            clearSharedPreferences();
                            Intent intent = new Intent();
                            setResult(300, intent);
                            finish();
                            ToastUtils.showShort(me,"添加成功");
                        }else {
                            ToastUtils.showShort(me,"添加失败");
                        }
                    }
                });
            }else {
                OkHttpUtils.post(Urls.URL_ADDLESSONSINFO)
                        .tag(this)
                        .params(params)
                        .addFileParams("file", kejianFiles)
                        .addFileParams("files", xitiFiles)
                        .execute(new ProgressUpCallBack<>(this, RequestInfo.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearSharedPreferences() {
            /*教案*/
        SharedPreferencesUtils.setParam(me,"mubiao","");
        SharedPreferencesUtils.setParam(me,"guocheng","");
        SharedPreferencesUtils.setParam(me,"zhongdian","");
        SharedPreferencesUtils.setParam(me,"zhunbei","");
        SharedPreferencesUtils.setParam(me,"zuoyebuzhi","");
        SharedPreferencesUtils.setParam(me,"kexing","");
        SharedPreferencesUtils.setParam(me,"keshi","");
        SharedPreferencesUtils.setParam(me,"title","");
            /*习题*/
        SharedPreferencesUtils.setParam(me,"xiti","");
            /*课件*/
        SharedPreferencesUtils.setParam(me,"kejian","");
    }

    private class ProgressUpCallBack<T> extends JsonCallback<T> {
        public ProgressUpCallBack(Activity activity, Class<T> clazz) {
            super(clazz);
        }
        @Override
        public void onResponse(boolean isFromCache, T t, Request request, @Nullable Response response) {
            mProgress.dismiss();
            //上传完成
            ToastUtils.showShort(me,"上传成功："+response.message());
            clearSharedPreferences();
        }
        @Override
        public void onBefore(BaseRequest request) {
            super.onBefore(request);
            mProgress = new DowloadDialog(me, "正在上传课件，请稍等...");
        }
        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            mProgress.dismiss();
            //上传错误
            ToastUtils.showShort(me,response.message());
        }
        @Override
        public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
            super.upProgress(currentSize, totalSize, progress, networkSpeed);
            String downloadLength = Formatter.formatFileSize(me, currentSize);
            String totalLength = Formatter.formatFileSize(me, totalSize);
            String netSpeed = Formatter.formatFileSize(me, networkSpeed);
            mProgress.setPercent(Math.round(Math.round(progress * 10000) * 1.0f / 100));
            mProgress.setDownloadLength(downloadLength+"/");
            mProgress.setTotalLength(totalLength);
            mProgress.setnNtSpeed(netSpeed+"/s");
        }
    }
    private void tl() {
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tl.setTabData(mTitles);
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tl.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vp.setCurrentItem(0);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
