package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.HomeListAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.callback.UpdateCallback;
import com.hanboard.teacher.common.tools.ClearSdCard;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.tools.VersionUtils;
import com.hanboard.teacher.common.view.AboutUsDialog;
import com.hanboard.teacher.common.view.CircleImageView;
import com.hanboard.teacher.entity.Account;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Version;
import com.hanboard.teacher.model.IUserModel;
import com.hanboard.teacher.model.IVersionModel;
import com.hanboard.teacher.model.impl.UserModelImpl;
import com.hanboard.teacher.model.impl.VersionModelImpl;
import com.hanboard.teacherhd.lib.common.utils.AppManager;
import com.hanboard.teacherhd.lib.common.utils.SDCardHelper;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;

public class HomeActivity extends BaseActivity implements UpdateCallback, IDataCallback<Domine> {
    //用户头像
    //功能列表
    @BindView(R.id.home_lv_function)
    ListView mHomeLvFunction;
    @BindView(R.id.home_layout)
    DrawerLayout mHomeLayout;
    @BindView(R.id.home_iv_usericon)
    ImageView mHomeIvUsericon;
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.home_setting_usericon)
    CircleImageView mSettingUsericon;
    @BindView(R.id.home_tv_vsion)
    TextView mVsion;

    //检测登录跟新的接口
    private IVersionModel iVersionModel;
    private IUserModel iUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        iVersionModel = new VersionModelImpl();
        iUserModel = new UserModelImpl();
        iUserModel.getUserIno((String) SharedPreferencesUtils.getParam(me, "id", ""), this);
        checkUpdate();
        //设置菜单的点击
        mHomeIvUsericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeLayout.openDrawer(Gravity.LEFT);
            }
        });
        initData();


    }

    private void initData() {
        mVsion.setText("v:"+VersionUtils.getVersionName(me));
        Picasso.with(this).load((String) SharedPreferencesUtils.getParam(this, "userImg", "")).into(mHomeIvUsericon);
        Picasso.with(this).load((String) SharedPreferencesUtils.getParam(this, "userImg", "")).into(mSettingUsericon);
        List<Integer> imgs = new ArrayList<>();
        imgs.add(R.mipmap.prapareimg);
        imgs.add(R.mipmap.wordimg);
        imgs.add(R.mipmap.startclassimg);
        imgs.add(R.mipmap.imgcoursemanager);
        HomeListAdapter adapter = new HomeListAdapter(this);
        mHomeLvFunction.setAdapter(adapter);
        //具体的点击事件在adpter里面
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void handler(Message msg) {

    }

    private void checkUpdate() {
        iVersionModel.checkVersion(this);
    }

    //跟新版本接口
    @Override
    public void onVersion(Version version) {
        int versionCode = VersionUtils.getVersionCode(me);
        if (versionCode < version.versionCode) {
            Intent intent = new Intent();
            intent.putExtra("version", version);
            intent.setClass(this, VersionActivity.class);
            startActivity(intent);
        }
        ToastUtils.showShort(me, "最新版本");
    }

    @OnClick({R.id.home_setting_usericon, R.id.home_bt_clear, R.id.home_update,R.id.home_aboutus, R.id.home_suggestion, R.id.home_exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_setting_usericon:
                startActivityForResult(new Intent(me, MineActivity.class), 800);
                break;
            case R.id.home_bt_clear:
                ToastUtils.showShort(me, "清除缓存");
                String path = SDCardHelper.getSDCardPath() + File.separator + "temp" + File.separator + "Hanboard";
                ClearSdCard.clearData(path, me);
                break;
            case R.id.home_update:
                ToastUtils.showShort(me, "正在检查更新");
                checkUpdate();
                break;
            case R.id.home_suggestion:
                startActivity(SugestionActivity.class);
                break;
            case R.id.home_aboutus:
                AboutUsDialog aboutUsDialog = new AboutUsDialog.Builder(this).create();
                aboutUsDialog.show();
                break;
             case R.id.home_exit:
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 800) {
            iUserModel.getUserIno((String) SharedPreferencesUtils.getParam(me, "id", ""), this);
        }
    }

    @Override
    public void onSuccess(Domine data) {
        if (data instanceof Account) {
            Picasso.with(me).load(((Account) data).avatarUrl).into(mSettingUsericon);
            Picasso.with(me).load(((Account) data).avatarUrl).into(mHomeIvUsericon);

        }
    }

    @Override
    public void onError(String msg, int code) {

    }
}
