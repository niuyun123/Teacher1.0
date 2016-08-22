package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.Window;
import android.widget.LinearLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.fragment.CouseWareFragment;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import it.sephiroth.android.library.picasso.Picasso;

public class MedioPlayer extends BaseActivity {
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.mediaplayer)
    JCVideoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_medio_player);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(CouseWareFragment.COURSEWAREURL);
        String title = intent.getStringExtra(CouseWareFragment.COURSEWARETITLE);
//        mPlayer.setUp("http://flv.bn.netease.com/videolib3/1601/13/RzAQP8148/HD/RzAQP8148-mobile.mp4","测试视频");
        mPlayer.setUp(url,title);
        Picasso.with(this).load(Uri.parse("http://img2.3lian.com/2014/f2/149/d/30.jpg")).into(mPlayer.ivThumb);
    }


    @Override
    protected void handler(Message msg) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        mPlayer.release();
        JCVideoPlayer.releaseAllVideos();
        try {
            super.onBackPressed();
        } catch (Exception e) {
            System.out.println("播放异常：" + e.toString());
            startActivity(HomeActivity.class);

        }

    }
}
