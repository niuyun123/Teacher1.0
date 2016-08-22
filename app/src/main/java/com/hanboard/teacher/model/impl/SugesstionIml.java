package com.hanboard.teacher.model.impl;

import com.google.gson.reflect.TypeToken;
import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.common.config.BaseMap;
import com.hanboard.teacher.common.config.CodeInfo;
import com.hanboard.teacher.common.config.CoderConfig;
import com.hanboard.teacher.common.config.Constants;
import com.hanboard.teacher.common.config.Urls;
import com.hanboard.teacher.entity.MData;
import com.hanboard.teacher.model.ISugesstionModel;
import com.hanboard.teacherhd.lib.common.http.okhttp.OkHttpUtils;
import com.hanboard.teacherhd.lib.common.http.okhttp.callback.StringCallback;
import com.hanboard.teacherhd.lib.common.utils.DESCoder;
import com.hanboard.teacherhd.lib.common.utils.JsonUtil;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/20.
 */
public class SugesstionIml implements ISugesstionModel {
    @Override
    public void submitSugesstion(String accountId, String opinionContent, final IDataCallback<String> iDataCallback) {
        try {
            Map<String,String> param = new BaseMap().initMap();
            param.put("accountId",accountId);
            param.put("opinionContent",opinionContent);
            OkHttpUtils.get().params(param).url(Urls.URL_SUGESTION).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    iDataCallback.onError(e.getMessage(),id);
                }
                @Override
                public void onResponse(String response, int id) {
                    if(null!=response||!response.equals("")){
                        MData res = JsonUtil.fromJson(response,new TypeToken<MData>(){}.getType());
                        if(res.code.equals(Constants.CODE_SUCCESS)){
                            iDataCallback.onSuccess(res.message);
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
