package com.atguigu.beijingnewstest.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.fragment.ContentFragment;
import com.atguigu.beijingnewstest.fragment.LeftMenuFragment;
import com.atguigu.beijingnewstest.utils.DensityUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    public static final String MAIN_TAG = "main_tag";
    public static final String LEFT_TAG = "left_tag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置主页面及左侧菜单
        initSlidingMenu();

        //初始化Fragment
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.fl_content,new ContentFragment(), MAIN_TAG);
        ft.replace(R.id.fl_leftMenu,new LeftMenuFragment(), LEFT_TAG);

        ft.commit();
    }

    private void initSlidingMenu() {
        //设置主页面
        setContentView(R.layout.activity_main);
        //设置左侧菜单
        setBehindContentView(R.layout.left_menu);

        SlidingMenu slidingMenu = getSlidingMenu();

        //设置右侧栏
        //slidingMenu.setSecondaryMenu(R.layout.left_menu);

        //设置模式：左侧+主页；左侧+主页+右侧；主页+右侧
        slidingMenu.setMode(SlidingMenu.LEFT);

        //设置滑动模式：不可用滑动，滑动编边缘，全屏滑动
        //slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setTouchModeAbove(slidingMenu.TOUCHMODE_MARGIN);

        //设置主页面占的宽度
        slidingMenu.setBehindOffset(DensityUtil.dip2px(this,200));
    }
}
