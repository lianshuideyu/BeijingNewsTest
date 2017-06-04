package com.atguigu.beijingnewstest.fragment;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.activity.MainActivity;
import com.atguigu.beijingnewstest.base.BaseFragment;
import com.atguigu.beijingnewstest.base.BasePager;
import com.atguigu.beijingnewstest.pager.HomePager;
import com.atguigu.beijingnewstest.pager.NewsPager;
import com.atguigu.beijingnewstest.pager.SettingPager;
import com.atguigu.beijingnewstest.view.NoViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class ContentFragment extends BaseFragment {

    @InjectView(R.id.vp)
    NoViewPager vp;
    @InjectView(R.id.rg_main)
    RadioGroup rgMain;

    private ArrayList<BasePager> pagers;

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.fragment_content, null);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //准备数据
        pagers = new ArrayList<>();
        pagers.add(new HomePager(context));
        pagers.add(new NewsPager(context));
        pagers.add(new SettingPager(context));

        vp.setAdapter(new MyAdaper());

        vp.addOnPageChangeListener(new MyOnPageChangeListener());

        rgMain.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        //设置默认选择
        rgMain.check(R.id.rb_home);

        pagers.get(0).initData();

        isEnableSlidingMenu(context,SlidingMenu.TOUCHMODE_NONE);
    }


    private class MyAdaper extends PagerAdapter {
        @Override
        public int getCount() {
            return pagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager basePager = pagers.get(position);
            View rootView = basePager.rootView;
            container.addView(rootView);
            //basePager.initData();

            return rootView;
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
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            BasePager basePager = pagers.get(position);
            basePager.initData();

            if (position == 1) {
                //可以侧滑
                isEnableSlidingMenu(context,SlidingMenu.TOUCHMODE_FULLSCREEN);
            } else {
                //不可以侧滑
                isEnableSlidingMenu(context,SlidingMenu.TOUCHMODE_NONE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 是否让SlidingMenu可以滑动
     * @param context
     * @param touchmodeFullscreen
     */
    private static void isEnableSlidingMenu(Context context,int touchmodeFullscreen) {
        MainActivity mainActivity = (MainActivity) context;
        SlidingMenu slidingMenu = mainActivity.getSlidingMenu();
        slidingMenu.setTouchModeAbove(touchmodeFullscreen);
    }

    private class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int id) {
            switch (id) {
                case R.id.rb_home:
                    vp.setCurrentItem(0, false);
                    break;
                case R.id.rb_news:
                    vp.setCurrentItem(1, false);
                    break;
                case R.id.rb_setting:
                    vp.setCurrentItem(2, false);
                    break;
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
