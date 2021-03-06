package com.hanboard.teacher.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.hanboard.teacher.R;
import com.hanboard.teacher.app.AppContext;
import com.hanboard.teacher.common.tools.ViewPagerScroller;
import com.hanboard.teacher.common.view.SmartViewPager;
import com.hanboard.teacher.entity.Banner;

import java.lang.reflect.Field;
import java.util.List;

import it.sephiroth.android.library.picasso.Picasso;

public class ViewHolder implements OnTouchListener,ViewPager.OnPageChangeListener {
	// 返回的view
	private View viewToReturn;
	// 所有图片资源文件的id集合
	//private List<Integer> mDatas;
	private List<Banner> mDatas;
	// viewpager，使用自定义控件，重写了onMeasure方法,自适应图片宽高
	private SmartViewPager viewPager;
	// ViewPager的布局
	private RelativeLayout.LayoutParams rlp;
	// 自动播放的任务
	private AutoPlayTask autoPlayTask;
	private ImageView cirlce1,cirlce2,cirlce3;
	private final int AUOT_PLAY_TIME = 3000;
	private Context context;

	public ViewHolder(List<Banner> data,Context context) {
		this.context = context;
		viewToReturn = initView();
		viewToReturn.setTag(this);
		setData(data);
	}


	// 把准备好的View返回去
	public View getRootView() {
		return viewToReturn;
	}
	public void setData(List<Banner> data) {
		this.mDatas = data;
		// 刷新界面
		refreshView();
	}

	/** 先设置从setData传入的数据，然后刷新界面的方法 */
	public void refreshView() {
		mDatas = getData();
		// 让轮播图从0处开始滚动
		viewPager.setCurrentItem(0);
	}

	public List<Banner> getData() {
		return this.mDatas;
	}
	/** 初始化view的方法 ，返回一个view */
	protected View initView() {
		// 初始化头布局
		//RelativeLayout mHeaderView = new RelativeLayout(AppContext.getInstance());
		RelativeLayout mHeaderView= (RelativeLayout) LayoutInflater.from(AppContext.getInstance()).inflate(
				R.layout.mianlist_header, null);
		// 设置轮播图的宽高
		// 设置LayoutParams的时候，一定要带一个父容器的限制，这里使用AbsListView的原因是因为，Listview没有.LayoutParams选项
		// 此处让宽度是EXACTLY，而高度不是，所以测量时会以宽度为准
		mHeaderView.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		//viewPager = new SmartViewPager(AppContext.getInstance());
		//viewPager = (SmartViewPager) View.inflate(AppContext.getInstance(), R.layout.mianlist_header, null);
          viewPager= (SmartViewPager) mHeaderView.findViewById(R.id.mianlist_header_viewpager);
		viewPager.addOnPageChangeListener(this);
		viewPager.setCurrentItem(496);
		cirlce1= (ImageView) mHeaderView.findViewById(R.id.circle1);
		cirlce1.setImageResource(R.mipmap.circleon);
		cirlce2= (ImageView) mHeaderView.findViewById(R.id.circle2);
		cirlce3= (ImageView) mHeaderView.findViewById(R.id.circle3);
		// 改为在xml布局文件中设置自定义属性
		 //viewPager.setRatio(16 / 9.0f);

		rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// 给SmartViewPager设置宽高的模式，宽为MATCH_PARENT/EXACTLY，高为WRAP_CONTENT/非EXACTLY
		// 便于测量
		viewPager.setLayoutParams(rlp);

		ViewPagerAdapter adapter = new ViewPagerAdapter();
		initViewPagerScroll();
		// 设置adapter
		viewPager.setAdapter(adapter);

		autoPlayTask = new AutoPlayTask();

		// 这一句start要加，否则要按一下才开始跳
		autoPlayTask.start();

		viewPager.setOnTouchListener(this);

		// 把轮播图添加到header中去，添加也要按照次序，先添加的在下面
		//mHeaderView.addView(viewPager);

		return mHeaderView;
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
          //resertCirle();
		if (position%3==0){
			resertCirle();
			cirlce1.setImageResource(R.mipmap.circleon);
		}else if (position%3==1){
			resertCirle();
			cirlce2.setImageResource(R.mipmap.circleon);

		}else if (position%3==2){
			resertCirle();
			cirlce3.setImageResource(R.mipmap.circleon);

		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	private class ViewPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;// 总共能够滑动的次数
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// return false;
			return view == object;// 返回的view就是这个object
		}
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			ImageView imageView = new ImageView(AppContext.getInstance());
			/*// 加载图片，%一下，防止数组越界  本地图片
			Integer id = mDatas.get(position % mDatas.size());
			Drawable drawable = AppContext.getInstance().getResources()
					.getDrawable(id);
			imageView.setImageDrawable(drawable);
*/
			// 如果是从网络获取图片的话，必须增加这个属性，缩放，填充控件
			if (mDatas.size()>0){
				Picasso.with(AppContext.getInstance()).load(mDatas.get(position% mDatas.size()).imageUrl).into(imageView);
				imageView.setScaleType(ScaleType.FIT_XY);// 每个imageview都可以设置两个源：backGround是imageview本身的大小，而src是设置的图片的大
                 imageView.setOnClickListener(new View.OnClickListener() {
					 @Override
					 public void onClick(View view) {
						 final Intent issueIntent = new Intent(Intent.ACTION_VIEW);
						 issueIntent.setData(Uri.parse(mDatas.get(position% mDatas.size()).linkUrl));
						context.startActivity(issueIntent);
						 ///////////////////////////
					//	 String[] browser = { "com.tencent.mtt", "com.UCMobile", "com.uc.browser", "com.oupeng.browser", "com.oupeng.mini.android", "com.android.browser" };
					 }
				 });
			}else {
				imageView.setImageResource(R.mipmap.img_black);
			}
			container.addView(imageView);
			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((ImageView) object);

		}

	}

	/** 自动播放的Runnable */
	private class AutoPlayTask implements Runnable {

		private boolean IS_PALYING = false;

		@Override
		public void run() {
			// 运行的方法
			if (IS_PALYING) {
				Log.i("hahahaha", "run: ========"+viewPager.getCurrentItem());
				viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
				AppContext.getMainThreadHandler().postDelayed(this,
						AUOT_PLAY_TIME);
				/*if (viewPager.getCurrentItem()%3==0){
					resertCirle();
					cirlce2.setImageResource(R.mipmap.circleon);

				}else if (viewPager.getCurrentItem()%3==1){
					resertCirle();
					cirlce3.setImageResource(R.mipmap.circleon);

				}else if (viewPager.getCurrentItem()%3==2){
					resertCirle();
					cirlce1.setImageResource(R.mipmap.circleon);

				}*/

			}
		}

		public void stop() {
			// 停止的方法
			AppContext.getMainThreadHandler().removeCallbacks(this);
			IS_PALYING = false;// 不置为false，下次就start不起来了

		}

		public void start() {
			// 开始的方法
			if (!IS_PALYING) {
				IS_PALYING = true;
				// UIUtils.postDelayed(this, AUOT_PLAY_TIME);
				// 移除任何在消息队列中的对应参数中的runnable对象
				AppContext.getMainThreadHandler().removeCallbacks(this);// 这句清空的操作很有必要，防止越跳越快的情况
				AppContext.getMainThreadHandler().postDelayed(this,
						AUOT_PLAY_TIME);
			}
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN
				|| action == MotionEvent.ACTION_CANCEL) {
			// down事件和cancel事件 停止轮播
			// cancel表示屏幕被挡住之后
			autoPlayTask.stop();

		} else if (action == MotionEvent.ACTION_UP) {
			// up事件 恢复轮播
			autoPlayTask.start();
		}
		return false;
	}
	/**
	 * 设置ViewPager的滑动速度
	 *
	 * */
	private void initViewPagerScroll( ){
		try {
			Field mField  = null;
			mField  = ViewPager.class.getDeclaredField("mScroller");
			mField .setAccessible(true);
			ViewPagerScroller mScroller = new ViewPagerScroller( viewPager.getContext( ),new AccelerateInterpolator() );
			mScroller.setmDuration(1000);
			mField.set( viewPager, mScroller);
		}catch(NoSuchFieldException e){

		}catch (IllegalArgumentException e){

		}catch (IllegalAccessException e){

		}
	}
	public void resertCirle(){
		cirlce1.setImageResource(R.mipmap.circleof);
		cirlce2.setImageResource(R.mipmap.circleof);
		cirlce3.setImageResource(R.mipmap.circleof);
	}
}
