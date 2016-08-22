package com.hanboard.teacher.model;

import com.hanboard.teacher.common.callback.IDataCallback;

/**
 * Created by Administrator on 2016/8/20.
 */
public interface ISugesstionModel {
    void submitSugesstion(String accountId, String opinionContent, IDataCallback<String> iDataCallback);
}
