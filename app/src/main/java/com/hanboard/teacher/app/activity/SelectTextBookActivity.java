package com.hanboard.teacher.app.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.adapter.QueryTextbookAdapter;
import com.hanboard.teacher.common.base.BaseActivity;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.view.CustomDialog;
import com.hanboard.teacher.common.view.WheelView;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Pickers;
import com.hanboard.teacher.entity.Status;
import com.hanboard.teacher.entity.Subject;
import com.hanboard.teacher.entity.SuitObject;
import com.hanboard.teacher.entity.TextBook;
import com.hanboard.teacher.entity.listentity.SubjectList;
import com.hanboard.teacher.entity.listentity.SuitObjectList;
import com.hanboard.teacher.entity.listentity.TextBookList;
import com.hanboard.teacher.model.ISelectTextBookModel;
import com.hanboard.teacher.model.impl.SelectTextBookModelImpl;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;
import com.hanboard.teacherhd.lib.common.utils.SharedPreferencesUtils;
import com.hanboard.teacherhd.lib.common.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectTextBookActivity extends BaseActivity  implements IDataCallback<Domine> {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.ok)
    TextView ok;
    @BindView(R.id.tv_select_sunject)
    TextView tvSelectSunject;
    @BindView(R.id.lnl_select_sunject)
    LinearLayout lnlSelectSunject;
    @BindView(R.id.tv_select_suitobject)
    TextView tvSelectSuitobject;
    @BindView(R.id.lnl_select_suitobject)
    LinearLayout lnlSelectSuitobject;
    @BindView(R.id.btn_serach)
    Button btnSerach;
    @BindView(R.id.gd_textbook)
    GridView gdTextbook;
    private ISelectTextBookModel iSubjectModel;

    private List<Pickers> mAllSubject = new ArrayList<>();
    private List<Pickers> mAllSuitObject = new ArrayList<>();
    private WheelView mWlwSelectTextbookSubject;
    private WheelView mWlwSelectTextbookGrade;
    private String subjectId = "";
    private String suitObjectId = "";
    private List<TextBook> textbooks = new ArrayList<>();
    private QueryTextbookAdapter mAdapter;
    private TextBook mTextBook = null;
    public CustomDialog b;
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_select_text_book);
        iSubjectModel = new SelectTextBookModelImpl();
        ButterKnife.bind(this);
        gdTextbook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAdapter.setSeclection(position);
                mAdapter.notifyDataSetChanged();
                mTextBook = (TextBook) parent.getAdapter().getItem(position);
            }
        });
    }
    @Override
    protected void handler(Message msg) {
        switch (msg.what) {
            case 0:
                selectSubject();
                break;
            case 1:
                selectSuitObject();
                break;
            case 2:
                disProgress();
                mAdapter = new QueryTextbookAdapter(me,R.layout.query_textbook_item,textbooks);
                gdTextbook.setAdapter(mAdapter);
                break;
            case 3:

                break;
        }
    }
    @OnClick({R.id.back, R.id.ok, R.id.lnl_select_sunject, R.id.lnl_select_suitobject, R.id.btn_serach})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ok:
                ok();
                break;
            case R.id.lnl_select_sunject:
                iSubjectModel.getAllSubject(this);
                break;
            case R.id.lnl_select_suitobject:
                iSubjectModel.getAllSuitObject(this);
                break;
            case R.id.btn_serach:
                query();
                break;
        }
    }

    private void ok() {
        if(null == mTextBook){
            ToastUtils.showShort(me,"教材不能为空！");
        }else {
            CustomDialog.Builder s = new CustomDialog.Builder(SelectTextBookActivity.this, "提示信息！", "您是否需要添加" + mTextBook.subjectName + mTextBook.sectionName + mTextBook.versionName + "到你的课本?", new CustomDialog.Builder.LeaveDialogListener() {
                @Override
                public void cancel(View v) {
                    b.dismiss();
                }
                @Override
                public void confirm(View v) {
                    Intent intent=new Intent();
                    intent.putExtra("textbook", JsonUtil.toJson(mTextBook));
                    setResult(CreatePrepareLessonsActivity.REQUEST_CODE, intent);
                    finish();
                    b.dismiss();
                }
            });
            b = s.create();
            b.show();

        }
    }
    private void query() {
        if(suitObjectId.equals("")||subjectId.equals("")){
            ToastUtils.showShort(me,"请选择学科和年级");
        }else {
            showProgress("玩命加载中...");
            iSubjectModel.getTextbookBySubIdAndSuitID(subjectId, suitObjectId, this);
        }
    }
    /*选择学科*/
    private void selectSubject() {
        View outerView = LayoutInflater.from(me).inflate(R.layout.wheel_view, null);
        mWlwSelectTextbookSubject = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        TextView t = (TextView) outerView.findViewById(R.id.wheel_view_tv);
        t.setText("请选择学科");
        mWlwSelectTextbookSubject.setOffset(2);
        mWlwSelectTextbookSubject.setSeletion(2);
        mWlwSelectTextbookSubject.setItems(mAllSubject);

        Dialog dialog = new AlertDialog.Builder(this)
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSelectSunject.setText(mWlwSelectTextbookSubject.getSeletedItem().getShowConetnt());
                        subjectId = mWlwSelectTextbookSubject.getSeletedItem().getShowId();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }
    /*选择年级*/
    private void selectSuitObject() {
        View outerView = LayoutInflater.from(me).inflate(R.layout.wheel_view, null);
        mWlwSelectTextbookGrade = (WheelView) outerView.findViewById(R.id.wheel_view_wv);
        TextView t = (TextView) outerView.findViewById(R.id.wheel_view_tv);
        t.setText("请选择年级");
        mWlwSelectTextbookGrade.setOffset(2);
        mWlwSelectTextbookGrade.setSeletion(2);
        mWlwSelectTextbookGrade.setItems(mAllSuitObject);

        Dialog dialog = new AlertDialog.Builder(this)
                .setView(outerView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tvSelectSuitobject.setText(mWlwSelectTextbookGrade.getSeletedItem().getShowConetnt());
                        suitObjectId = mWlwSelectTextbookGrade.getSeletedItem().getShowId();
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }
    @Override
    public void onSuccess(Domine data) {
        if (data instanceof SubjectList)
        {
            mAllSubject.clear();
            List<Subject> allSubject = ((SubjectList) data).subjects;
            for (Subject subject:allSubject){
                mAllSubject.add(new Pickers(subject.title,subject.id));
            }
            handler.sendEmptyMessage(0);
        }else if (data instanceof SuitObjectList){
            mAllSuitObject.clear();
            List<SuitObject> allSuitObjects = ((SuitObjectList) data).suitObjects;
            for (SuitObject suitObject:allSuitObjects){
                mAllSuitObject.add(new Pickers(suitObject.title,suitObject.id));
            }
            handler.sendEmptyMessage(1);
        }else if(data instanceof TextBookList){
            textbooks.clear();
            textbooks = ((TextBookList) data).textBooks;
            handler.sendEmptyMessage(2);
        }else if(data instanceof Status){
            handler.sendEmptyMessage(3);
        }
    }
    @Override
    public void onError(String msg, int code) {
        ToastUtils.showShort(me,msg);
    }
}
