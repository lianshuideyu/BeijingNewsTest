package com.atguigu.beijingnewstest.pager;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnewstest.base.BasePager;

/**
 * Created by Administrator on 2017/6/2.
 */

public class HomePager extends BasePager {


    public HomePager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();

        tvTitle.setText("主页面");
        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("主页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //添加到Fragment布局上
        flContent.addView(textView);
    }
}
