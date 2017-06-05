package com.atguigu.beijingnewstest.menudetailpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.base.MenuDetailBasePager;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/4.
 */

public class TabDetailPager extends MenuDetailBasePager {
    @InjectView(R.id.viewpager)
    ViewPager viewpager;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @InjectView(R.id.lv)
    ListView lv;

    private NewsCenterBean.DataBean.ChildrenBean childrenBean;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图
        View view = View.inflate(context, R.layout.pager_tab_detail, null);
        ButterKnife.inject(this, view);
        //创建子类的视图
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        tvTitle.setText(childrenBean.getTitle());
    }
}
