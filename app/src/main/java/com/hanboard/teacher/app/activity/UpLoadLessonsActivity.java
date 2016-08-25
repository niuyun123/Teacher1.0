package com.hanboard.teacher.app.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.ResAddGridViewAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.JsonCallback;
import com.hanboard.teacher.common.config.BaseMap;
import com.hanboard.teacher.common.config.CoderConfig;
import com.hanboard.teacher.common.config.Constants;
import com.hanboard.teacher.common.config.Urls;
import com.hanboard.teacher.common.view.DowloadDialog;
import com.hanboard.teacher.entity.LoadRes;
import com.hanboard.teacher.entity.MData;
import com.hanboard.teacher.entity.RequestInfo;
import com.hanboard.teacher.model.impl.WpsModel;
import com.hanboard.teacherhd.lib.common.http.okhttp.callback.StringCallback;
import com.hanboard.teacherhd.lib.common.utils.DESCoder;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.request.BaseRequest;
import com.nononsenseapps.filepicker.FilePickerActivity;

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

public class UpLoadLessonsActivity extends BaseActivity {
    @BindView(R.id.teaching_plan_tab)
    TextView teachingPlanTab;
    @BindView(R.id.course_tab)
    TextView courseTab;
    @BindView(R.id.exercise_tab)
    TextView exerciseTab;
    @BindView(R.id.teaching_plan_content)
    LinearLayout teachingPlanContent;
    @BindView(R.id.course_content)
    LinearLayout courseContent;
    @BindView(R.id.exercise_content)
    LinearLayout exerciseContent;


    @BindView(R.id.add_lessons_teachingplan_mubiao)
    EditText addLessonsTeachingplanMubiao;
    @BindView(R.id.add_lessons_teachingplan_zhongdian)
    EditText addLessonsTeachingplanZhongdian;
    @BindView(R.id.add_lessons_teachingplan_zhunbei)
    EditText addLessonsTeachingplanZhunbei;
    @BindView(R.id.add_lessons_teachingplan_guocheng)
    EditText addLessonsTeachingplanGuocheng;
    @BindView(R.id.add_lessons_teachingplan_zuoyebuzhi)
    EditText addLessonsTeachingplanZuoyebuzhi;
    private String mubiao;
    private String zhongdian;
    private String zhunbei;
    private String guocheng;
    private String zuoyebuzhi;
    private String kexing;
    private String keshi;
    private String title;

    @BindView(R.id.gd_add_courseware)
    GridView addLessonsCoursewareGridview;
    public List<LoadRes> courseList = new ArrayList<>();
    private ResAddGridViewAdapter courseAdapter;
    private boolean isCourseShowDelete=false;

    @BindView(R.id.gd_add_exercises)
    GridView addLessonsExercisesGridview;
    public List<LoadRes> exercisesList = new ArrayList<>();
    private ResAddGridViewAdapter exercisesAdapter;
    private boolean isexercisesShowDelete=false;
    private DowloadDialog mProgress;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_up_load_lessons);
        ButterKnife.bind(this);
        initTab();
        init();
    }
    private void init() {
        Intent in = getIntent();
        title = in.getStringExtra("title");
        keshi = in.getStringExtra("keshi");
        kexing = in.getStringExtra("kexing");
        addLessonsCoursewareGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isCourseShowDelete) {
                    isCourseShowDelete = false;
                } else {
                    isCourseShowDelete = true;
                    courseAdapter.setIsShowDelete(isCourseShowDelete);
                    addLessonsCoursewareGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            deleteCourse(position);
                            courseAdapter = new ResAddGridViewAdapter(UpLoadLessonsActivity.this, courseList);
                            addLessonsCoursewareGridview.setAdapter(courseAdapter);
                            courseAdapter.notifyDataSetChanged();
                        }
                    });
                }
                courseAdapter.setIsShowDelete(isCourseShowDelete);
                return true;
            }
        });
        addLessonsCoursewareGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoadRes cL = (LoadRes) parent.getAdapter().getItem(position);
                openFile(cL.path);
            }
        });
        addLessonsExercisesGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (isexercisesShowDelete) {
                    isexercisesShowDelete = false;
                } else {
                    isexercisesShowDelete = true;
                    exercisesAdapter.setIsShowDelete(isexercisesShowDelete);
                    addLessonsExercisesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            deleteExercises(position);
                            exercisesAdapter = new ResAddGridViewAdapter(UpLoadLessonsActivity.this, exercisesList);
                            addLessonsExercisesGridview.setAdapter(exercisesAdapter);
                            exercisesAdapter.notifyDataSetChanged();
                        }
                    });
                }
                exercisesAdapter.setIsShowDelete(isexercisesShowDelete);
                return true;
            }
        });
        addLessonsExercisesGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LoadRes eL = (LoadRes) parent.getAdapter().getItem(position);
                openFile(eL.path);
            }
        });
    }
    private void deleteExercises(int position) {
        List<LoadRes> newList = new ArrayList<>();
        if(isexercisesShowDelete){
            exercisesList.remove(position);
            isexercisesShowDelete=false;
        }
        newList.addAll(exercisesList);
        exercisesList.clear();
        exercisesList.addAll(newList);
    }
    private void deleteCourse(int position) {
        List<LoadRes> newList = new ArrayList<>();
        if(isCourseShowDelete){
            courseList.remove(position);
            isCourseShowDelete=false;
        }
        newList.addAll(courseList);
        courseList.clear();
        courseList.addAll(newList);
    }

    /*设置默认Fragment*/
    private void initTab() {
        reset();
        teachingPlanContent.setVisibility(View.VISIBLE);
        teachingPlanTab.setBackgroundResource(R.drawable.bg_detials_textviewleftback);
    }

    private void reset() {
        teachingPlanContent.setVisibility(View.GONE);
        courseContent.setVisibility(View.GONE);
        exerciseContent.setVisibility(View.GONE);
        teachingPlanTab.setBackgroundResource(R.drawable.bg_detials_textviewleft);
        courseTab.setBackgroundResource(R.drawable.bg_detials_textviewcenter);
        exerciseTab.setBackgroundResource(R.drawable.bg_detials_textviewright);
    }
    @Override
    protected void handler(Message msg) {

    }

    @OnClick({
            R.id.teaching_plan_tab,
            R.id.course_tab,
            R.id.exercise_tab,
            R.id.img_select_courseware,
            R.id.img_select_exercises,
            R.id.lessons_upload,
            R.id.back
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.teaching_plan_tab:
                reset();
                teachingPlanContent.setVisibility(View.VISIBLE);
                teachingPlanTab.setBackgroundResource(R.drawable.bg_detials_textviewleftback);
                break;
            case R.id.course_tab:
                reset();
                courseContent.setVisibility(View.VISIBLE);
                courseTab.setBackgroundResource(R.color.theme_color);
                break;
            case R.id.exercise_tab:
                reset();
                exerciseContent.setVisibility(View.VISIBLE);
                exerciseTab.setBackgroundResource(R.drawable.bg_detials_textviewrightback);
                break;
            case R.id.img_select_courseware:
                Intent i = new Intent(this, FilePickerActivity.class);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(i,888);
                break;
            case R.id.img_select_exercises:
                Intent j = new Intent(this, FilePickerActivity.class);
                j.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                j.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                j.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
                j.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                startActivityForResult(j,999);
                break;
            case R.id.lessons_upload:
                upload();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
    private void upload() {
        mubiao = addLessonsTeachingplanMubiao.getText().toString();
        zhongdian = addLessonsTeachingplanZhongdian.getText().toString();
        zhunbei = addLessonsTeachingplanZhunbei.getText().toString();
        guocheng = addLessonsTeachingplanGuocheng.getText().toString();
        zuoyebuzhi = addLessonsTeachingplanZuoyebuzhi.getText().toString();
        List<File> xitiFiles = new ArrayList<>();
        if (exercisesList != null && exercisesList.size() > 0) {
            for (int a = 0; a < exercisesList.size(); a++) {
                xitiFiles.add(new File(exercisesList.get(a).path));
            }
        }
        List<File> kejianFiles = new ArrayList<>();
        if (courseList != null && courseList.size() > 0) {
            for (int b = 0; b < courseList.size(); b++) {
                kejianFiles.add(new File(courseList.get(b).path));
            }
        }
        try {
            /**章节ID*/
            String chapterId = (String) SharedPreferencesUtils.getParam(UpLoadLessonsActivity.this,"chapterId","");
            /**课本ID*/
            String textbookId = (String) SharedPreferencesUtils.getParam(UpLoadLessonsActivity.this,"textbookId","");
            /*账户ID*/
            String accountId = (String)SharedPreferencesUtils.getParam(UpLoadLessonsActivity.this,"id","");
            Map<String,String> params = new BaseMap().initMap();
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
                        ToastUtils.showShort(UpLoadLessonsActivity.this,e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        MData<String> res = JsonUtil.fromJson(response,new TypeToken<MData<String>>(){}.getType());
                        if(res.code.equals(Constants.CODE_SUCCESS)) {
                            AddLessonsActivity.instance.finish();
                            CreateLessonsDatilsActivity.instance.finish();
                            finish();
                            ToastUtils.showShort(UpLoadLessonsActivity.this,"添加成功");
                        }else {
                            ToastUtils.showShort(UpLoadLessonsActivity.this,"添加失败,"+res.message);
                        }
                    }
                });
            }else {
                OkHttpUtils.post(Urls.URL_ADDLESSONSINFO)
                        .tag(this)
                        .params(params)
                        .addFileParams("file", kejianFiles)
                        .addFileParams("files", xitiFiles)
                        .execute(new ProgressUpCallBack<>(this,RequestInfo.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888 && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();
                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            //文件路径
                            String path = getRealFilePath(uri);
                            //文件格式
                            String format = path.substring(path.indexOf(".")+1, path.length());
                            File tempFile = new File(path.trim());
                            if(format.equals("ppt")||format.equals("pptx")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="PPT";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                courseList.add(loadRes);
                            }else if(format.equals("doc")||format.equals("docx")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="WORD";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                courseList.add(loadRes);
                            }else if(format.equals("xlsx")||format.equals("xls")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="EXCEL";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                courseList.add(loadRes);
                            }else if(format.equals("mp3")||format.equals("wav")||format.equals("wma")||format.equals("ape")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="AUDIO";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                courseList.add(loadRes);
                            }else if(format.equals("mkv")||format.equals("mp4")||format.equals("avi")||format.equals("rm")||format.equals("rmvb")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="VIDEO";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                courseList.add(loadRes);
                            }else if(format.equals("pdf")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="PDF";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                courseList.add(loadRes);
                            }
                        }
                        courseAdapter =  new ResAddGridViewAdapter(this,courseList);
                        addLessonsCoursewareGridview.setAdapter(courseAdapter);
                    }
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
                    if (paths != null) {
                        for (String path: paths) {
                            Uri uri = Uri.parse(path);
                        }
                    }
                }
            } else {
                Uri uri = data.getData();
            }
        }else if(requestCode == 999 && resultCode == Activity.RESULT_OK){
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();
                    if (clip != null) {
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();
                            //文件路径
                            String path = getRealFilePath(uri);
                            //文件格式
                            String format = path.substring(path.indexOf(".")+1, path.length());
                            File tempFile = new File(path.trim());
                            if(format.equals("ppt")||format.equals("pptx")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="PPT";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                exercisesList.add(loadRes);
                            }else if(format.equals("doc")||format.equals("docx")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="WORD";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                exercisesList.add(loadRes);
                            }else if(format.equals("xlsx")||format.equals("xls")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="EXCEL";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                exercisesList.add(loadRes);
                            }else if(format.equals("mp3")||format.equals("wav")||format.equals("wma")||format.equals("ape")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="AUDIO";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                exercisesList.add(loadRes);
                            }else if(format.equals("mkv")||format.equals("mp4")||format.equals("avi")||format.equals("rm")||format.equals("rmvb")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="VIDEO";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                exercisesList.add(loadRes);
                            }else if(format.equals("pdf")){
                                LoadRes loadRes = new LoadRes();
                                loadRes.format="PDF";
                                loadRes.name = tempFile.getName();
                                loadRes.path = path;
                                exercisesList.add(loadRes);
                            }
                        }
                        exercisesAdapter =  new ResAddGridViewAdapter(this,exercisesList);
                        addLessonsExercisesGridview.setAdapter(exercisesAdapter);
                    }
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
                    if (paths != null) {
                        for (String path: paths) {
                            Uri uri = Uri.parse(path);
                        }
                    }
                }
            } else {
                Uri uri = data.getData();
            }
        }
    }
    /**
     * @param uri
     * @return the file path or null
     */
    private String getRealFilePath(final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    /**
     * 用wps打开相应文件
     *
     * @param path
     * @return
     */
    private boolean openFile(String path) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(WpsModel.OPEN_MODE, WpsModel.OpenMode.NORMAL); // 打开模式
        bundle.putBoolean(WpsModel.SEND_CLOSE_BROAD, true); // 关闭时是否发送广播
        bundle.putString(WpsModel.THIRD_PACKAGE, Constants.NORMAL); // 第三方应用的包名，用于对改应用合法性的验证
        bundle.putBoolean(WpsModel.CLEAR_TRACE, true);// 清除打开记录
        // bundle.putBoolean(CLEAR_FILE, true); //关闭后删除打开文件
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setClassName(WpsModel.PackageName.NORMAL, WpsModel.ClassName.NORMAL);
        File file = new File(path);
        if (file == null || !file.exists()) {
            ToastUtils.showShort(this, "找不到该文件");
            return false;
        }
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        intent.putExtras(bundle);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            System.out.println("打开wps异常：请检查是否安装WPS" + e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private class ProgressUpCallBack<T> extends JsonCallback<T> {
        public ProgressUpCallBack(Activity activity, Class<T> clazz) {
            super(clazz);
        }
        @Override
        public void onResponse(boolean isFromCache, T t, Request request, @Nullable Response response) {
            //上传完成
            ToastUtils.showShort(UpLoadLessonsActivity.this,response.message());
            AddLessonsActivity.instance.finish();
            CreateLessonsDatilsActivity.instance.finish();
            finish();
        }
        @Override
        public void onBefore(BaseRequest request) {
            super.onBefore(request);
            mProgress = new DowloadDialog(UpLoadLessonsActivity.this, "正在上传课件，请稍等...");
        }
        @Override
        public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
            super.onError(isFromCache, call, response, e);
            mProgress.dismiss();
            //上传错误
            ToastUtils.showShort(UpLoadLessonsActivity.this,"上传失败");
        }
        @Override
        public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
            super.upProgress(currentSize, totalSize, progress, networkSpeed);
            String downloadLength = Formatter.formatFileSize(UpLoadLessonsActivity.this, currentSize);
            String totalLength = Formatter.formatFileSize(UpLoadLessonsActivity.this, totalSize);
            String netSpeed = Formatter.formatFileSize(UpLoadLessonsActivity.this, networkSpeed);
            mProgress.setPercent(Math.round(Math.round(progress * 10000) * 1.0f / 100));
            mProgress.setDownloadLength(downloadLength+"/");
            mProgress.setTotalLength(totalLength);
            mProgress.setnNtSpeed(netSpeed+"/s");
        }
    }

}
