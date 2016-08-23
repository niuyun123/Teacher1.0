package com.hanboard.teacher.app.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.MainAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.view.InterceptorFrameLayout;
import com.hanboard.teacher.entity.Banner;
import com.hanboard.teacher.holder.ViewHolder;
import com.hanboard.teacher.model.IAppModel;
import com.hanboard.teacher.model.impl.AppModelImpl;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener,IDataCallback<List<Banner>> {
    // 用来存放Listview的条目文本内容的集合
    private List<String> items = Arrays.asList("教师备课","教师授课");
    private List<Integer> imgs= Arrays.asList(R.mipmap.img_prepare,R.mipmap.img_startclass);
    // 要显示的ListView
    private ListView mListView;
    // 用来存放轮播图图片的id
   /* private List<Integer> resList = Arrays.asList(R.drawable.a, R.drawable.b,
            R.drawable.c, R.drawable.d, R.drawable.e);*/
    private List<Banner> resList=new ArrayList<>();
    private IAppModel iAppModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        iAppModel = new AppModelImpl();
        mListView = new ListView(this);
        iAppModel.getBanner(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        ToastUtils.showShort(  MainActivity.this, "item" + (position - mListView.getHeaderViewsCount()) + "被点击了！");
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void handler(Message msg) {

    }
    @Override
    public void onSuccess(List<Banner> data) {
        resList=data;
        ViewHolder holder = new ViewHolder(resList);
        View mHeaderView = holder.getRootView();
        mListView.addHeaderView(mHeaderView);
        mListView.setAdapter(new MainAdapter(items,imgs, mListView
                .getHeaderViewsCount()));
        mListView.setOnItemClickListener(this);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        InterceptorFrameLayout ifl = new InterceptorFrameLayout(
                MainActivity.this);

        // ifl.setLayoutParams(new FrameLayout.LayoutParams(
        // LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        ifl.addView(mListView);
        ifl.addInterceptorView(mHeaderView,
                InterceptorFrameLayout.ORIENTATION_LEFT
                        | InterceptorFrameLayout.ORIENTATION_RIGHT);
        rl.addView(ifl);
    }

    @Override
    public void onError(String msg, int code) {

    }

}
