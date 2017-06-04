package com.atguigu.beijingnewstest.fragment;

import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstest.base.BaseFragment;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private TextView textView;

    private List<NewsCenterBean.DataBean> datas;
    @Override
    public View initView() {
        textView = new TextView(context);
        textView.setTextSize(30);
        textView.setTextColor(Color.RED);
        textView.setGravity(Gravity.CENTER);


        return textView;
    }

    @Override
    public void initData() {
        super.initData();

        textView.setText("左侧页面");
    }

    /**
     * 获得从新闻页面传来的数据
     * @param datas
     */
    public void setNewsData(List<NewsCenterBean.DataBean> datas) {
        this.datas = datas;

        for(int i = 0; i < datas.size(); i++) {
            Log.e("TAG","传到左侧菜单的数据==" + datas.get(i).getTitle());

        }
    }
}
