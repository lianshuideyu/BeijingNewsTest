package com.atguigu.beijingnewstest.menudetailpager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstest.base.MenuDetailBasePager;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;

/**
 * Created by Administrator on 2017/6/4.
 */

public class TabDetailPager extends MenuDetailBasePager {
    private TextView textView;
    private NewsCenterBean.DataBean.ChildrenBean childrenBean;

    public TabDetailPager(Context context, NewsCenterBean.DataBean.ChildrenBean childrenBean) {
        super(context);
        this.childrenBean = childrenBean;
    }

    @Override
    public View initView() {
        //图组详情页面的视图
        textView = new TextView(context);
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText(childrenBean.getTitle());
    }
}
