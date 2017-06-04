package com.atguigu.beijingnewstest.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.atguigu.beijingnewstest.base.BasePager;

/**
 * Created by Administrator on 2017/6/2.
 */

public class SettingPager extends BasePager {


    public SettingPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","SettingPager--initData");

        tvTitle.setText("设置页面");
        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("设置页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        //添加到Fragment布局上
        flContent.addView(textView);
    }
}
