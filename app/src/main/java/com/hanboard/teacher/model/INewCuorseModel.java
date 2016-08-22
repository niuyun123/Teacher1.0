package com.hanboard.teacher.model;

import com.hanboard.teacher.common.callback.IDataCallback;
import com.hanboard.teacher.entity.Domine;

/**
 * Created by Administrator on 2016/8/18.
 */
public interface INewCuorseModel {
    void getNewCourse(String accountId, String teachBookId, String pageNum, IDataCallback<Domine> iDataCallback);

}
