package com.atguigu.beijingnewstest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.utils.CacheUtils;
import com.atguigu.beijingnewstest.utils.DensityUtil;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class GuideActivity extends AppCompatActivity {

    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.btn_start_main)
    Button btnStartMain;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.iv_red_point)
    ImageView ivRedPoint;

    private ArrayList<ImageView> imageViews;
    private int[] ids = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3};

    //两点间的距离
    private int leftMargin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.inject(this);

        initData();

        vp.setAdapter(new MyAdaper());

        vp.addOnPageChangeListener(new MyOnPageChangeListener());

        //求出两点间的间距,当视图加载好后。。。。
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                leftMargin = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();
                Log.e("TAG", "leftMargin==" + leftMargin);
            }
        });
    }

    private void initData() {
        imageViews = new ArrayList<>();
        for (int i = 0; i < ids.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(ids[i]);

            imageViews.add(imageView);

            //绘制下方的指示点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.normal_point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 10), DensityUtil.dip2px(this, 10));
            point.setLayoutParams(params);
            if (i != 0) {
                params.leftMargin = DensityUtil.dip2px(this, 10);

            }

            llPointGroup.addView(point);
        }
    }


    private class MyAdaper extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = imageViews.get(position);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        /**
         * 当滑到的时候回调
         *
         * @param position             当前滑动的页面的下标位置
         * @param positionOffset       滑动百分比
         * @param positionOffsetPixels 滑动的像素
         */
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //红点移动的距离： 间距 = ViewPager滑动的百分比
            //红点移动的距离 = 间距*ViewPager滑动的百分比
            float left = leftMargin * (position + positionOffset);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
            params.leftMargin = (int) left;
            ivRedPoint.setLayoutParams(params);

            Log.e("TAG","position=="+position+",positionOffset=="+positionOffset+",positionOffsetPixels=="+positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if (position == imageViews.size() - 1) {
                btnStartMain.setVisibility(View.VISIBLE);
            } else {
                btnStartMain.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @OnClick(R.id.btn_start_main)
    public void onViewClicked() {
        //保存记录已经进入过引导页面
        CacheUtils.putBoolean(this,"start_main",true);

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
