package com.hanboard.teacher.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.reflect.TypeToken;
import com.hanboard.teacher.R;
import com.hanboard.teacher.app.activity.DetialsActivity;
import com.hanboard.teacher.app.activity.MedioPlayer;
import com.hanboard.teacher.app.adapter.CousewareAndExerciseAdapter;
import com.hanboard.teacher.common.base.BaseFragment;
import com.hanboard.teacher.common.tools.OpenFileUtil;
import com.hanboard.teacher.common.view.DowloadDialog;
import com.hanboard.teacher.entity.Exercises;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;
import com.hanboard.teacherhd.lib.common.utils.SDCardHelper;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;
import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.FileCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import name.quanke.app.libs.emptylayout.EmptyLayout;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public class ExerciseFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    public DowloadDialog mDialog;
    @BindView(R.id.exercise_content)
    GridView mContent;
    @BindView(R.id.emptLayout)
    EmptyLayout mEmptLayout;
    private CousewareAndExerciseAdapter<Exercises> adapter;
    public static String EXERCISESWAREURL = "courseWareUrl";
    public static String EXERCISESTITLE = "courseWareTitle";
    private static final String TAG = "ExercisesFragment";
    private List<Exercises> mExercises = new ArrayList<>();
    private Exercises item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void initData() {
        String exerciseJsonString = getArguments().getString(DetialsActivity.EXERCISES, "");
        if (exerciseJsonString == null || exerciseJsonString.equals("")) {
            mEmptLayout.showEmpty();
        } else {
            mExercises = JsonUtil.fromJson(exerciseJsonString, new TypeToken<List<Exercises>>() {
            }.getType());
            if (mExercises.size() == 0) {
                mEmptLayout.showEmpty();
            } else {
                mExercises = JsonUtil.fromJson(exerciseJsonString, new TypeToken<List<Exercises>>() {
                }.getType());
                adapter = new CousewareAndExerciseAdapter(context, R.layout.item_file, mExercises);
                mContent.setAdapter(adapter);
                mContent.setOnItemClickListener(this);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        item = (Exercises) (adapterView.getAdapter().getItem(i));
        if (item.exercisesType.equals("5") || item.exercisesType.equals("6")) {
            Intent intent = new Intent(context, MedioPlayer.class);
            intent.putExtra("url", item.exercisesUrl);
            intent.putExtra("title", item.exercisesTitle);
            intent.putExtra("type", item.exercisesType);
            startActivity(intent);
        } else {
            File file = new File(SDCardHelper.getSDCardPath() + File.separator + "temp" + File.separator
                    + "Hanboard" + File.separator + "Exercises" + File.separator + item.exercisesTitle);
            if (file.exists()) {
              /*  boolean flag = OpenFileUtil.openFile(file.getAbsolutePath(), context);
                if (flag) ToastUtils.showShort(context,"打开文件成功");
                else ToastUtils.showShort(context,"请安装手机版WPS...");*/
                OpenFileUtil.openAFile(file.getAbsoluteFile(), getActivity());

            } else {
                mDialog = new DowloadDialog(context, "正在下载中,请稍等...");
                OkHttpUtils.get(item.exercisesUrl)//
                        .tag(this)//
                        .execute(new FileCallback("/sdcard/temp/Hanboard/Exercises/", item.exercisesTitle) {  //文件下载时，需要指定下载的文件目录和文件名
                            @Override
                            public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                                // file 即为文件数据，文件保存在指定目录
                                mDialog.dismiss();
                                mDialog = null;
                                OpenFileUtil.openAFile(file.getAbsoluteFile(), getActivity());
                                ToastUtils.showShort(context, file.getName() + "下载成功,保存路径在:" + file.getAbsolutePath());
                            }

                            @Override
                            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                mDialog.setPercent(Math.round(progress));
                                String downloadLength = Formatter.formatFileSize(context, currentSize);
                                String totalLength = Formatter.formatFileSize(context, totalSize);
                                String netSpeed = Formatter.formatFileSize(context, networkSpeed);
                                mDialog.setPercent(Math.round(Math.round(progress * 10000) * 1.0f / 100));
                                mDialog.setDownloadLength(downloadLength + "/");
                                mDialog.setTotalLength(totalLength);
                                mDialog.setnNtSpeed(netSpeed + "/s");
                            }

                            @Override
                            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                                super.onError(isFromCache, call, response, e);
                                ToastUtils.showShort(context, "下载失败...");
                                mDialog.dismiss();
                                mDialog = null;
                            }
                        });
            }
        }
    }
}
