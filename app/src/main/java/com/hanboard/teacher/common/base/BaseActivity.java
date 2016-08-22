package com.hanboard.teacher.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.hanboard.teacher.app.AppContext;
import com.hanboard.teacher.broadCast.NetBrodeCaset;
import com.hanboard.teacher.common.view.LoadingDialog;
import com.hanboard.teacher.model.InetEventHandler;
import com.hanboard.teacherhd.lib.common.utils.NetUtil;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;
import com.hanboard.teacherhd.lib.handle.UIHandler;

/**
 * 项目名称：TeacherHD
 * 类描述：activity基类
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/7/21 0021 9:43
 */
public abstract class BaseActivity extends FragmentActivity implements InetEventHandler{
    protected Activity me;
    protected static UIHandler handler = new UIHandler(Looper.getMainLooper());
    private Intent intent;
    private LoadingDialog loadingDialog;
    public BaseActivity() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        me = this;
        initContentView(savedInstanceState);
        setHandler();
        NetState();

    }

    public void NetState() {
        NetBrodeCaset.mListeners.add(this);
        if (AppContext.mNetWorkState== NetUtil.NETWORN_NONE){
            ToastUtils.showShort(this,"当前无网络..");
        }
    }
    private void setHandler() {
        handler.setHandler(new UIHandler.IHandler() {
            public void handleMessage(Message msg) {
                handler(msg);//有消息就提交给子类实现的方法
            }
        });
    }

    // 初始化UI，setContentView等
    protected abstract void initContentView(Bundle savedInstanceState);

    //让子类处理消息
    protected abstract void handler(Message msg);

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        AppManager.getAppManager().finishActivity(this);
    }

    /**
     * 以无参数的模式启动Activity。
     *
     * @param activityClass
     */
    public void startActivity(Class<? extends Activity> activityClass) {
        me.startActivity(getLocalIntent(activityClass, null));
    }

    /**
     * 以绑定参数的模式启动Activity。
     *
     * @param activityClass
     */
    public void startActivity(Class<? extends Activity> activityClass, Bundle bd) {
        me.startActivity(getLocalIntent(activityClass, bd));
    }

    /**
     * 获取当前程序中的本
     * 地目标
     *
     * @param localIntent
     * @return
     */
    public Intent getLocalIntent(Class<? extends Context> localIntent, Bundle bd) {
        Intent intent = new Intent(me, localIntent);
        if (null != bd) {
            intent.putExtras(bd);
        }

        return intent;
    }


    /**
     * 隐藏键盘
     *
     * @param view
     */
    protected void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**
     * 显示progress对话框
     */
    protected void showProgress(final String msg) {
        loadingDialog = new LoadingDialog(me,msg);
    }

    /**
     * 取消progress对话框
     */
    protected void disProgress() {
       loadingDialog.dismiss();
    }
    @Override
    public void onWifiNet() {
        if (NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE) {
            ToastUtils.showShort(this, "网络中断");
        } else {
            ToastUtils.showShort(this, "网络已连接");
        }
    }

}
