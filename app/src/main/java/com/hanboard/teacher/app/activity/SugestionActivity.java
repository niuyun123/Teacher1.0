package com.hanboard.teacher.app.activity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.model.ISugesstionModel;
import com.hanboard.teacher.model.impl.SugesstionIml;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SugestionActivity extends BaseActivity {
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.sugestion_content)
    EditText mContent;
    private ISugesstionModel iSugesstionModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sugestion);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        iSugesstionModel=new SugesstionIml();
        ButterKnife.bind(this);
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {

    }

    @Override
    protected void handler(Message msg) {

    }

    @OnClick({R.id.back, R.id.sugestion_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sugestion_submit:
                final String content = mContent.getText().toString();
                iSugesstionModel.submitSugesstion((String) SharedPreferencesUtils.getParam(me, "id", ""), content, new IDataCallback<String>() {
                    @Override
                    public void onSuccess(String data) {
                        ToastUtils.showShort(me,data);
                    }

                    @Override
                    public void onError(String msg, int code) {
                             ToastUtils.showShort(me,msg+code);
                    }
                });
                mContent.setText("");
                break;
            case R.id.back:
              finish();
                break;
        }
    }
}
