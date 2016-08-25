package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.LinearLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class MedioPlayer extends AppCompatActivity {
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.mediaplayer)
    JCVideoPlayerStandard mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_medio_player);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        ButterKnife.bind(this);
        initData();
    }


    private void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        String type=intent.getStringExtra("type");
//      mPlayer.setUp("http://flv.bn.netease.com/videolib3/1601/13/RzAQP8148/HD/RzAQP8148-mobile.mp4","测试视频");
        mPlayer.setUp(url,JCVideoPlayer.SCREEN_LAYOUT_LIST,title);
       // Picasso.with(this).load(Uri.parse("http://img2.3lian.com/2014/f2/149/d/30.jpg")).into(mPlayer.ivThumb);
        if (type.equals("5"))
        mPlayer.thumbImageView.setImageResource(R.mipmap.img_startclass);
        else         mPlayer.thumbImageView.setImageResource(R.mipmap.img_prepare);

    }



    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JCVideoPlayer.releaseAllVideos();
    }

    /*@Override
        public void onBackPressed() {
            mPlayer.release();
            JCVideoPlayer.releaseAllVideos();
            try {
                super.onBackPressed();
            } catch (Exception e) {
                System.out.println("播放异常：" + e.toString());
                startActivity(HomeActivity.class);

            }

        }*/
    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
