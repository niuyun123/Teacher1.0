package com.hanboard.teacher.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.MainAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.callback.UpdateCallback;
import com.hanboard.teacher.common.tools.ClearSdCard;
import com.hanboard.teacher.common.tools.VersionUtils;
import com.hanboard.teacher.common.view.AboutUsDialog;
import com.hanboard.teacher.common.view.CircleImageView;
import com.hanboard.teacher.common.view.UserIconDialog;
import com.hanboard.teacher.entity.Account;
import com.hanboard.teacher.entity.Banner;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Version;
import com.hanboard.teacher.holder.ViewHolder;
import com.hanboard.teacher.model.IAppModel;
import com.hanboard.teacher.model.IUserModel;
import com.hanboard.teacher.model.IVersionModel;
import com.hanboard.teacher.model.impl.AppModelImpl;
import com.hanboard.teacher.model.impl.UserModelImpl;
import com.hanboard.teacher.model.impl.VersionModelImpl;
import com.hanboard.teacherhd.lib.common.utils.AppManager;
import com.hanboard.teacherhd.lib.common.utils.SDCardHelper;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;
import com.hanboard.teacherhd.lib.refreshview.XRefreshView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;

public class HomeActivity extends BaseActivity implements UpdateCallback, IDataCallback<Domine> ,AdapterView.OnItemClickListener{
    //用户头像
    //功能列表
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
    @BindView(R.id.home_refreshView)
    XRefreshView refreshView;
    //检测登录跟新的接口
    private IVersionModel iVersionModel;
    private IUserModel iUserModel;

    // 用来存放Listview的条目文本内容的集合
    private List<String> items = Arrays.asList("教师备课","教师授课","");
    private List<Integer> imgs= Arrays.asList(R.mipmap.img_prepare,R.mipmap.img_startclass,R.mipmap.img_black);
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
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
       // ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        refreshView.setMoveForHorizontal(true);
        // 设置是否可以下拉刷新
        refreshView.setPullRefreshEnable(false);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(false);

        iAppModel = new AppModelImpl();
        mListView = (ListView) findViewById(R.id.home_list);
        mListView.setDividerHeight(0);
        iAppModel.getBanner(new IDataCallback<List<Banner>>() {
            @Override
            public void onSuccess(List<Banner> data) {
                resList= (List<Banner>) data;
                ViewHolder holder = new ViewHolder(resList);
                View mHeaderView = holder.getRootView();
                mListView.addHeaderView(mHeaderView);
                mListView.setAdapter(new MainAdapter(items,imgs, mListView
                        .getHeaderViewsCount()));
                mListView.setOnItemClickListener(HomeActivity.this);
               /* RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
                InterceptorFrameLayout ifl = new InterceptorFrameLayout(
                        me);

                // ifl.setLayoutParams(new FrameLayout.LayoutParams(
                // LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

                ifl.addView(mListView);
                ifl.addInterceptorView(mHeaderView,
                        InterceptorFrameLayout.ORIENTATION_LEFT
                                | InterceptorFrameLayout.ORIENTATION_RIGHT);
                rl.addView(ifl);*/
            }

            @Override
            public void onError(String msg, int code) {
              ToastUtils.showShort(me,"获取数据失败");
                ViewHolder holder = new ViewHolder(resList);
                View mHeaderView = holder.getRootView();
                mListView.addHeaderView(mHeaderView);
                mListView.setAdapter(new MainAdapter(items,imgs, mListView
                        .getHeaderViewsCount()));
                mListView.setOnItemClickListener(HomeActivity.this);
            }
        });
        AppManager.getAppManager().addActivity(this);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

    @OnClick({R.id.home_newlycourse,R.id.home_setting_usericon, R.id.home_bt_clear, R.id.home_update,R.id.home_aboutus, R.id.home_suggestion, R.id.home_exit})
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
            case R.id.home_newlycourse:
                startActivity(new Intent(me,PrepareNewActivity.class));
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
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //ToastUtils.showShort( me, "item" + (i - mListView.getHeaderViewsCount()) + "被点击了！");
        if (i==1){
            startActivity(new Intent(me,CreatePrepareLessonsActivity.class));
        }else if(i==2){
            Intent in=new Intent(me, PrepareActivity.class);
            startActivity(in);
        }else if(i==3){

        }else {
            new UserIconDialog.Builder(me,(String) SharedPreferencesUtils.getParam(me, "userImg", "")).create().show();
        }
    }
}
