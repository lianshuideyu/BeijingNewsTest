package com.atguigu.beijingnewstest.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstest.base.MenuDetailBasePager;

/**
 * Created by Administrator on 2017/6/4.
 */

public class TopicMenuDetailPager extends MenuDetailBasePager {

    private TextView textView;

    public TopicMenuDetailPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        //创建子类的视图
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        return textView;
    }

    @Override
    public void initData() {
        super.initData();
        textView.setText("专题详情页面的内容");
    }
}
