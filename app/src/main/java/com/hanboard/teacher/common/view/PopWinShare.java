package com.hanboard.teacher.common.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.hanboard.teacher.R;

/**
 * Created by Administrator on 2016/8/18.
 */
public class PopWinShare extends PopupWindow{
        private View mainView;
        private LinearLayout popTouying, popPrepare,popRecord,popBaiban,popExit;

        public PopWinShare(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
            super(paramActivity);
            //窗口布局
            mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popwin_share, null);

            popTouying = ((LinearLayout)mainView.findViewById(R.id.pop_touying));
            popPrepare = (LinearLayout)mainView.findViewById(R.id.pop_prepare);
            popRecord= (LinearLayout) mainView.findViewById(R.id.pop_record);
            popBaiban= (LinearLayout) mainView.findViewById(R.id.pop_baiban);
            popExit= (LinearLayout) mainView.findViewById(R.id.pop_exit);
            //设置每个子布局的事件监听器
            if (paramOnClickListener != null){
                popTouying.setOnClickListener(paramOnClickListener);
                popPrepare.setOnClickListener(paramOnClickListener);
                popRecord.setOnClickListener(paramOnClickListener);
                popBaiban.setOnClickListener(paramOnClickListener);
                popExit.setOnClickListener(paramOnClickListener);
            }
            setContentView(mainView);
            //设置宽度
            setWidth(paramInt1);
            //设置高度
            setHeight(paramInt2);
            //设置显示隐藏动画
            setAnimationStyle(R.style.AnimTools);
            //设置背景透明
            setBackgroundDrawable(new ColorDrawable(0));
        }


}
