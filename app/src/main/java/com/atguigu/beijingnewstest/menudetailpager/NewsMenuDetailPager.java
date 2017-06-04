package com.atguigu.beijingnewstest.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.base.MenuDetailBasePager;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/4.
 */

public class NewsMenuDetailPager extends MenuDetailBasePager {


    /**
     * 新闻详情页面的数据
     */
    private final List<NewsCenterBean.DataBean.ChildrenBean> childrenData;

    /**
     * 页签页面的集合
     */
    private ArrayList<TabDetailPager> tabDetailPagers;

    @InjectView(R.id.viewpager)
    ViewPager viewpager;

    public NewsMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.childrenData = dataBean.getChildren();//12条
    }

    @Override
    public View initView() {
        //创建子类的视图
        //新闻详情页面
        View view = View.inflate(context, R.layout.news_menu_detail_pager, null);

        ButterKnife.inject(this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();

        //准备数据--页面
        tabDetailPagers = new ArrayList<>();
        for(int i = 0; i < childrenData.size(); i++) {
            tabDetailPagers.add(new TabDetailPager(context,childrenData.get(i)));

        }
        //设置适配器
        viewpager.setAdapter(new MyPagerAdapter());

    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return tabDetailPagers.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager tabDetailPager = tabDetailPagers.get(position);
            View rootView = tabDetailPager.rootView;
            container.addView(rootView);
            tabDetailPager.initData();

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
}
