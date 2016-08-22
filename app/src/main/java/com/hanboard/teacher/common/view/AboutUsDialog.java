package com.hanboard.teacher.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.hanboard.teacher.R;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AboutUsDialog extends Dialog{
    public AboutUsDialog(Context context) {
        super(context);
    }

    public AboutUsDialog(Context context, int theme) {
        super(context, theme);
    }
    public static class Builder {
        private Context context;
        public Builder(Context context) {
            this.context = context;
        }

        public AboutUsDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           // LayoutInflater layoutInflater = LayoutInflater.from(context);
            final AboutUsDialog dialog = new AboutUsDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_aboutus, null);
            dialog.addContentView(layout, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
