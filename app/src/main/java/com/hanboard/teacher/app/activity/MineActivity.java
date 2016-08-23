package com.hanboard.teacher.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanboard.teacher.R;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.config.CoderConfig;
import com.hanboard.teacher.common.config.Urls;
import com.hanboard.teacher.common.tools.ImmersedStatubarUtils;
import com.hanboard.teacher.common.view.CircleImageView;
import com.hanboard.teacher.common.view.EditDialog;
import com.hanboard.teacher.common.view.UpDatePwdDialog;
import com.hanboard.teacher.entity.Account;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Status;
import com.hanboard.teacher.model.IUserModel;
import com.hanboard.teacher.model.impl.UserModelImpl;
import com.hanboard.teacherhd.lib.common.utils.AppManager;
import com.hanboard.teacherhd.lib.common.utils.DESCoder;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import it.sephiroth.android.library.picasso.Picasso;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MineActivity extends BaseActivity implements IDataCallback<Domine> {
    @BindView(R.id.top)
    LinearLayout topView;
    @BindView(R.id.nick_name)
    TextView nickName;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tel)
    TextView tel;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.user_img)
    CircleImageView userImg;
    private EditDialog editNickName;
    private UpDatePwdDialog pwdDialog;
    private String originalPassword;
    private String updatePassword;
    private String verifyPassword;
    private IUserModel iUserModel;
    private String accountId;
    private String accountName;
    private Bitmap cameraBitmap;
    private AlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        ImmersedStatubarUtils.initAfterSetContentView(this, topView);
        Picasso.with(this).load((String) SharedPreferencesUtils.getParam(this, "userImg", "")).into(userImg);
        iUserModel = new UserModelImpl();
        loadData();
        initDialog();
        accountId = (String) SharedPreferencesUtils.getParam(me, "id", "");
        iUserModel.getUserIno(accountId, this);
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
        builder.setTitle("请选择头像")
                .setMessage("从图库中或相机中选择")
                .setPositiveButton("从图库", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        upDateUserIcon();
                    }
                }).setNeutralButton("从相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        }).setCancelable(true);
        mDialog = builder.create();

    }

    private void loadData() {
        iUserModel.getUserIno(accountId, this);
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
    }

    @Override
    protected void handler(Message msg) {

    }

    @OnClick({R.id.user_img, R.id.nick_name, R.id.name, R.id.tel, R.id.email, R.id.password, R.id.login_out, R.id.mine_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_img:
                mDialog.show();
                break;
            case R.id.nick_name:
                upDateNickName();
                break;
            case R.id.name:
                upDateName();
                break;
            case R.id.tel:
                upDateTel();
                break;
            case R.id.email:
                upDateEmail();
                break;
            case R.id.password:
                upDatePwd();
                break;
            case R.id.mine_back:
                finish();
                break;
            case R.id.login_out:
                AppManager.getAppManager().finishActivity(HomeActivity.class);
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    private void upDateUserIcon() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 240);
        intent.putExtra("outputY", 240);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == 1) {
                cameraBitmap = (Bitmap) data.getExtras().get("data");
                userImg.setImageBitmap(cameraBitmap);
                if (cameraBitmap == null) {
                    cameraBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img_myg);
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                OkHttpClient client = new OkHttpClient();
                MultipartBody multipartBody = null;
                try {
                    multipartBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("accountId", new DESCoder(CoderConfig.CODER_CODE).encrypt(accountId))
                            .addFormDataPart("userIcon", "", RequestBody.create(MediaType.parse("img/jpeg"), bos.toByteArray()))
                            .build();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Request request = new Request.Builder()
                        .url(Urls.URL_USERICON)
                        .post(multipartBody)
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("PersonInfo", "请求失败.");
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                    }
                });
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /*修改密码*/
    private void upDatePwd() {
        pwdDialog = new UpDatePwdDialog.Builder(me, new UpDatePwdDialog.Builder.LeaveDialogListener() {
            @Override
            public void ok(View v, String original_password, String update_password, String verify_password) {
                originalPassword = original_password;
                updatePassword = update_password;
                verifyPassword = verify_password;
                if(updatePassword.equals(verifyPassword)){
                    iUserModel.checkOldPwd(accountId, accountName, originalPassword, new IDataCallback<Domine>() {
                        @Override
                        public void onSuccess(Domine data) {
                            if (data instanceof Status){
                                iUserModel.updatePwd(accountId,accountName,verifyPassword, new IDataCallback<Domine>() {
                                    @Override
                                    public void onSuccess(Domine data) {
                                        ToastUtils.showShort(me,"修改成功");
                                        pwdDialog.dismiss();
                                    }
                                    @Override
                                    public void onError(String msg, int code) {
                                        ToastUtils.showShort(me,msg);
                                    }
                                });
                            }
                        }
                        @Override
                        public void onError(String msg, int code) {
                            Toast.makeText(me,"原密码输入错误",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(me,"请保持两次密码输入一致",Toast.LENGTH_SHORT).show();
                }
            }
        }).create();
        pwdDialog.show();
    }

    /*修改邮箱*/
    private void upDateEmail() {
        editNickName = new EditDialog.Builder(me, "修改电子邮箱", "请输入您的电子邮箱", new EditDialog.Builder.LeaveDialogListener() {
            @Override
            public void ok(View v, String res) {
                iUserModel.updateEmail(accountId, res, MineActivity.this);
            }
        }).create();
        editNickName.show();
    }

    /*修改手机*/
    private void upDateTel() {
        editNickName = new EditDialog.Builder(me, "修改电话号码", "请输入您的电话号码", new EditDialog.Builder.LeaveDialogListener() {
            @Override
            public void ok(View v, String res) {
                iUserModel.updateTel(accountId, res, MineActivity.this);
            }
        }).create();
        editNickName.show();
    }

    /*修改姓名*/
    private void upDateName() {
        editNickName = new EditDialog.Builder(me, "修改姓名", "请输入您的姓名", new EditDialog.Builder.LeaveDialogListener() {
            @Override
            public void ok(View v, String res) {
                iUserModel.updateName(accountId, res, MineActivity.this);
            }
        }).create();
        editNickName.show();
    }

    /**
     * 修改昵称
     */
    private void upDateNickName() {
        editNickName = new EditDialog.Builder(me, "修改昵称", "请输入您的昵称", new EditDialog.Builder.LeaveDialogListener() {
            @Override
            public void ok(View v, String res) {
                iUserModel.updataNickName(accountId, res, MineActivity.this);
            }
        }).create();
        editNickName.show();
    }
    @Override
    public void onSuccess(Domine data) {
        if (data instanceof Account) {
            Picasso.with(me).load(((Account) data).avatarUrl).into(userImg);
            nickName.setText(((Account) data).nickName);
            name.setText(((Account) data).trueName);
            tel.setText(((Account) data).phone);
            email.setText(((Account) data).email);
            accountName = ((Account) data).accountName;
        }else if(data instanceof Status){
            Toast.makeText(me,"修改成功",Toast.LENGTH_SHORT).show();
            editNickName.dismiss();
            iUserModel.getUserIno(accountId, this);
        }
    }
    @Override
    public void onError(String msg, int code) {
        Toast.makeText(me, msg, Toast.LENGTH_SHORT).show();
    }
}
