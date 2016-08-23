package com.hanboard.teacher.common.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hanboard.teacher.R;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by Administrator on 2016/8/22.
 */
public class UserIconDialog extends Dialog{
    public UserIconDialog(Context context) {
        super(context);
    }

    public UserIconDialog(Context context, int theme) {
        super(context, theme);
    }
    public static class Builder {
        private Context context;
        private ImageView imageView;
        private String path;
        public Builder(Context context,String path) {
            this.context = context;
            this.path=path;
        }

        public UserIconDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // LayoutInflater layoutInflater = LayoutInflater.from(context);
            final UserIconDialog dialog = new UserIconDialog(context, R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog_image, null);
            imageView= (ImageView) layout.findViewById(R.id.highimg);
            dialog.addContentView(layout, new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            dialog.setContentView(layout);
            Picasso.with(context).load(path).into(imageView);
            return dialog;
        }
    }
}
