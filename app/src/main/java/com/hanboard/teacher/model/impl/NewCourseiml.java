package com.hanboard.teacher.model.impl;

import com.google.gson.reflect.TypeToken;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.config.BaseMap;
import com.hanboard.teacher.common.config.CodeInfo;
import com.hanboard.teacher.common.config.CoderConfig;
import com.hanboard.teacher.common.config.Constants;
import com.hanboard.teacher.common.config.Urls;
import com.hanboard.teacher.entity.Domine;
import com.hanboard.teacher.entity.Elements;
import com.hanboard.teacher.entity.MData;
import com.hanboard.teacher.entity.PrepareChapter;
import com.hanboard.teacher.model.INewCuorseModel;
import com.hanboard.teacherhd.lib.common.http.okhttp.OkHttpUtils;
import com.hanboard.teacherhd.lib.common.http.okhttp.callback.StringCallback;
import com.hanboard.teacherhd.lib.common.utils.DESCoder;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/18.
 */
public class NewCourseiml implements INewCuorseModel {
    @Override
    public void getNewCourse(String accountId, String teachBookId, String pageNum, final IDataCallback<Domine> iDataCallback) {
        try {
            Map<String,String> param = new BaseMap().initMap();
            param.put("accountId",new DESCoder(CoderConfig.CODER_CODE).encrypt(accountId));
            param.put("teachBookId",new DESCoder(CoderConfig.CODER_CODE).encrypt(teachBookId));
            param.put("pageNum",pageNum);
            OkHttpUtils.get().params(param).url(Urls.URL_NEWCOURSE).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    iDataCallback.onError(e.getMessage(),id);
                }

                @Override
                public void onResponse(String response, int id) {
                    if(null!=response||!response.equals("")){
                        MData<Elements<PrepareChapter>> res = JsonUtil.fromJson(response,new TypeToken<MData<Elements<PrepareChapter>>>(){}.getType());
                        if(res.code.equals(Constants.CODE_SUCCESS)){
                            iDataCallback.onSuccess(res.result);
                        }else{
                            iDataCallback.onError(CodeInfo.REQUEST_FAILDE,Integer.valueOf(res.code));
                        }
                    }else {
                        iDataCallback.onError(CodeInfo.REQUEST_EMPTY,0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

