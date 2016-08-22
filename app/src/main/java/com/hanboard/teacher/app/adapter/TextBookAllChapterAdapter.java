package com.hanboard.teacher.app.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.entity.tree.Node;

import java.util.List;

/**
 * 项目名称：TeacherHD
 * 类描述：
 * 创建人：peng.an@hanboard.com
 * 作者单位：四川汉博德信息技术有限公司
 * 创建时间：2016/8/5 0005 12:43
 */
public class TextBookAllChapterAdapter<T> extends TreeListViewAdapter<T> {
    /**
     * @param mTree
     * @param context
     * @param datas
     * @param defaultExpandLevel 默认展开几级树
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public TextBookAllChapterAdapter(ListView mTree, Context context, List<T> datas, int defaultExpandLevel) throws IllegalArgumentException, IllegalAccessException {
        super(mTree, context, datas, defaultExpandLevel);
    }
    @Override
    public View getConvertView(Node node, int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.textbook_chapter_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.layout= (LinearLayout) convertView.findViewById(R.id.textbook_chapter_container);
            viewHolder.line= (TextView) convertView.findViewById(R.id.chapter_v_line);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.textbook_chapter_icon);
            viewHolder.label = (TextView) convertView.findViewById(R.id.textbook_chapter_name);
            viewHolder.img = (ImageView)convertView.findViewById(R.id.textbook_chapter_img);
            convertView.setTag(viewHolder);
        } else {/**360U2727817184**/
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (node.getIcon() == -1) {
            viewHolder.icon.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.icon.setVisibility(View.VISIBLE);
            viewHolder.icon.setImageResource(node.getIcon());
        }
        if(node.getLevel()==0){
            viewHolder.line.setVisibility(View.VISIBLE);
            viewHolder.layout.setBackground(mContext.getResources().getDrawable(R.drawable.bg_chapter_item));
            viewHolder.label.setTextColor(Color.WHITE);
            viewHolder.label.setTextSize(16);
            viewHolder.img.setImageResource(R.mipmap.noet);
        }else if(node.getLevel()== 1){
            viewHolder.layout.setBackgroundColor(Color.WHITE);
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.label.setTextColor(Color.BLACK);
            viewHolder.label.setTextSize(12);
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.img.setImageResource(R.mipmap.noet_null);
        }else if(node.getLevel()==2){
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.label.setTextColor(Color.BLACK);
            viewHolder.label.setTextSize(16);
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.img.setImageResource(R.mipmap.noet_null);
        }else{
            viewHolder.line.setVisibility(View.GONE);
            viewHolder.label.setTextSize(16);
            convertView.setBackgroundColor(Color.WHITE);
            viewHolder.label.setTextColor(Color.BLACK);
            viewHolder.img.setImageResource(R.mipmap.noet_null);
        }



        viewHolder.label.setText(node.getName());
        return convertView;
    }

    private final class ViewHolder
    {   TextView line;
        ImageView icon;
        TextView label;
        ImageView img;
        LinearLayout layout;
    }

}
