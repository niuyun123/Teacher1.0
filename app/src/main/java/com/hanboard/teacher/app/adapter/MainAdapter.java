package com.hanboard.teacher.app.adapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.AppContext;
import com.hanboard.teacher.common.tools.DisplayUtil;

import java.util.List;
public class MainAdapter extends BaseAdapter {
	private List<String> items;
	private List<Integer> imgs;
	private int ListViewHeaderCounts;
	public MainAdapter(List<String> items, List<Integer> imgs, int ListViewHeaderCounts) {
		this.items = items;
		this.imgs=imgs;
		this.ListViewHeaderCounts = ListViewHeaderCounts;
	}

	ListViewItemHolder holder;

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {

		// int headerViewsCount = mListView.getHeaderViewsCount();
		// return position - headerViewsCount;
		return position - ListViewHeaderCounts;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView != null) {
			holder = (ListViewItemHolder) convertView.getTag();
		} else {
			convertView = View.inflate(AppContext.getInstance(),
					R.layout.mainlist_item, null);
			holder = new ListViewItemHolder();

			convertView.setTag(holder);
			holder.tv = (TextView) convertView.findViewById(R.id.mian_tv_prepare);
			holder.iv= (ImageView) convertView.findViewById(R.id.mian_iv_prepare);

			if (position==2){
				RelativeLayout layout= (RelativeLayout) convertView.findViewById(R.id.main_rvl_item);
				RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) layout.getLayoutParams();
				params.height= DisplayUtil.dip2px(AppContext.getInstance(),225);
				layout.setLayoutParams(params);
			}
		}

		holder.tv.setText(items.get(position));
		holder.iv.setImageResource(imgs.get(position));
		return convertView;
	}

	class ListViewItemHolder {
		TextView tv;
		ImageView iv;

	}
}
