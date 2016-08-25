package com.hanboard.teacher.app.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hanboard.teacher.R;
import com.hanboard.teacher.app.activity.DetialsActivity;
import com.hanboard.teacher.app.activity.MedioPlayer;
import com.hanboard.teacher.app.adapter.CousewareAndExerciseAdapter;
import com.hanboard.teacher.common.base.BaseFragment;
import com.hanboard.teacher.common.tools.OpenFileUtil;
import com.hanboard.teacher.common.view.DowloadDialog;
import com.hanboard.teacher.entity.CourseWare;
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
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CouseWareFragment extends BaseFragment implements AdapterView.OnItemClickListener{
    @BindView(R.id.course)
    GridView mCourse;
    @BindView(R.id.fragment_detailempty)
    TextView mDetailempty;
    private DowloadDialog dowload;
    private List<CourseWare> mCurseType = new ArrayList<>();
    private CousewareAndExerciseAdapter<CourseWare> adapter;
    private CourseWare item;
    public static String COURSEWAREURL = "courseWareUrl";
    public static String COURSEWARETITLE = "courseWareTitle";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_couse_ware, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return null;
    }

    @Override
    protected void initData() {
        String couserWareJson = getArguments().getString(DetialsActivity.COURSEWARES, "");
        if (couserWareJson == null || couserWareJson.equals("")) {
            mDetailempty.setVisibility(View.VISIBLE);
        } else {
            mCurseType = JsonUtil.fromJson(couserWareJson, new TypeToken<List<CourseWare>>() {
            }.getType());
            if (mCurseType.size() == 0) {
                mDetailempty.setVisibility(View.VISIBLE);
            } else {
                mCurseType = JsonUtil.fromJson(couserWareJson, new TypeToken<List<CourseWare>>() {
                }.getType());
                adapter = new CousewareAndExerciseAdapter(context, R.layout.item_file, mCurseType);
                mCourse.setAdapter(adapter);
                mCourse.setOnItemClickListener(this);
            }
        }
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        item = (CourseWare)(adapterView.getAdapter().getItem(i));
        if (item.courseWareType.equals("5")||item.courseWareType.equals("6")){
            Intent intent=new Intent(context, MedioPlayer.class);
            intent.putExtra("url",item.courseWareUrl);
            intent.putExtra("title",item.courseWareTitle);
            intent.putExtra("type",item.courseWareType);
            startActivity(intent);
        }else {
            File file = new File(SDCardHelper.getSDCardPath() + File.separator + "temp"+File.separator+"Hanboard"
                    +File.separator+"CourseWare" + File.separator+item.courseWareTitle);
            if(file.exists()){
                OpenFileUtil.openAFile(file.getAbsoluteFile(),getActivity());
               /* boolean flag = OpenFileUtil.openFile(file.getAbsolutePath(), context);
                if (flag) ToastUtils.showShort(context,"打开文件成功");
                else ToastUtils.showShort(context,"请安装手机版WPS...");*/


            }else {
                dowload = new DowloadDialog(context,"正在下载中,请稍等...");
                OkHttpUtils.get(item.courseWareUrl)//
                        .tag(this)//
                        .execute(new FileCallback("/sdcard/temp/Hanboard/CourseWare/", item.courseWareTitle) {  //文件下载时，需要指定下载的文件目录和文件名
                            @Override
                            public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
                                // file 即为文件数据，文件保存在指定目录
                                dowload.dismiss();
                                dowload = null;
                                OpenFileUtil.openAFile(file.getAbsoluteFile(), getActivity());
                                ToastUtils.showShort(context,file.getName()+"下载成功,保存路径在:"+file.getAbsolutePath());
                            }
                            @Override
                            public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                                //这里回调下载进度(该回调在主线程,可以直接更新ui)
                                dowload.setPercent(Math.round(progress));
                                String downloadLength = Formatter.formatFileSize(context, currentSize);
                                String totalLength = Formatter.formatFileSize(context, totalSize);
                                String netSpeed = Formatter.formatFileSize(context, networkSpeed);
                                dowload.setPercent(Math.round(Math.round(progress * 10000) * 1.0f / 100));
                                dowload.setDownloadLength(downloadLength+"/");
                                dowload.setTotalLength(totalLength);
                                dowload.setnNtSpeed(netSpeed+"/s");
                            }

                            @Override
                            public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
                                super.onError(isFromCache, call, response, e);
                                ToastUtils.showShort(context,"下载失败...");
                                dowload.dismiss();
                                dowload = null;
                            }
                        });
            }

        }
    }
}


