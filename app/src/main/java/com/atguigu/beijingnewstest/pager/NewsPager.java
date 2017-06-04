package com.atguigu.beijingnewstest.pager;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.atguigu.beijingnewstest.activity.MainActivity;
import com.atguigu.beijingnewstest.base.BasePager;
import com.atguigu.beijingnewstest.base.MenuDetailBasePager;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;
import com.atguigu.beijingnewstest.fragment.LeftMenuFragment;
import com.atguigu.beijingnewstest.utils.ConstantUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/2.
 */

public class NewsPager extends BasePager {

    private List<NewsCenterBean.DataBean> datas;

    private ArrayList<MenuDetailBasePager> pagers;

    public NewsPager(Context context) {
        super(context);
    }

    @Override
    public void initData() {
        super.initData();
        Log.e("TAG","NewsPager--initData");

        tvTitle.setText("新闻页面");
        //创建子类的视图
        TextView textView = new TextView(context);
        textView.setText("新闻页面的内容");
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.RED);
        textView.setTextSize(25);

        ibMenu.setVisibility(View.VISIBLE);

        //添加到Fragment布局上
        flContent.addView(textView);

        //联网获取数据
        getDataFromNet();


    }

    private void getDataFromNet() {
        String url = ConstantUtils.NEWSCENTER_PAGER_URL;
        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG","NewsPager联网失败" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG","NewsPager联网成功" + response);
                        //解析数据
                        processData(response);
                    }
                });
    }

    private void processData(String json) {
        NewsCenterBean newsCenterBean = new Gson().fromJson(json, NewsCenterBean.class);
        Log.e("TAG","数据解析==" + newsCenterBean.getData().get(0).getTitle());

        datas = newsCenterBean.getData();

        //给左侧菜单栏传递数据
        //1.得到MainActivity
        MainActivity mainActivity = (MainActivity) context;

        //准备数据，当左侧栏点击切换的时候切换使用
        pagers = new ArrayList<>();
        pagers.add(new NewsMenuDetailPager(context));
        pagers.add(new InteractMenuDetailPager(context));
        pagers.add(new PhotosMenuDetailPager(context));
        pagers.add(new TopicMenuDetailPager(context));
        pagers.add(new VoteMenuDetailPager(context));

        //2.得到左侧菜单
        LeftMenuFragment leftMenuFragment = mainActivity.getLeftMenuFragment();
        //3.将数据传递给左侧菜单
        leftMenuFragment.setNewsData(datas);


    }

    @Override
    public void swichPager(int positon) {
        super.swichPager(positon);

        flContent.removeAllViews();
        flContent.addView(pagers.get(positon).rootView);
        pagers.get(positon).initData();
    }
}
