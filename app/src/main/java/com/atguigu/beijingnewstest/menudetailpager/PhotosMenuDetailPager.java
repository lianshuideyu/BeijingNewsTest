package com.atguigu.beijingnewstest.menudetailpager;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.atguigu.beijingnewslibrary.utils.CacheUtils;
import com.atguigu.beijingnewslibrary.utils.ConstantUtils;
import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.adapter.PhotosMenuDetailPagerAdapater;
import com.atguigu.beijingnewstest.base.MenuDetailBasePager;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;
import com.atguigu.beijingnewstest.domain.PhotosMenuDetailPagerBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/4.
 */

public class PhotosMenuDetailPager extends MenuDetailBasePager {

    private final NewsCenterBean.DataBean dataBean;
    @InjectView(R.id.recyclerview)
    RecyclerView recyclerview;
    @InjectView(R.id.progressbar)
    ProgressBar progressbar;
    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private String url;
    private List<PhotosMenuDetailPagerBean.DataBean.NewsBean> datas;

    private PhotosMenuDetailPagerAdapater adapter;

    public PhotosMenuDetailPager(Context context, NewsCenterBean.DataBean dataBean) {
        super(context);
        this.dataBean = dataBean;
        Log.e("TAG", "PhotosMenuDetailPager--dataBean=" + dataBean.getTitle());
    }

    @Override
    public View initView() {
        //创建子类的视图
        View view = View.inflate(context, R.layout.pager_photos_menu_detail, null);
        ButterKnife.inject(this, view);

        //设置刷新的监听
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromNet(url);
            }
        });
        //设置滑动多少距离有效果
        //refreshLayout.setDistanceToTriggerSync(200);

        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //联网请求
        url = ConstantUtils.BASE_URL + dataBean.getUrl();

        //先取出缓存数据并解析
        String savaJson = CacheUtils.getString(context, url);
        if(!TextUtils.isEmpty(savaJson)) {
            processData(savaJson);
        }

        getDataFromNet(url);

    }

    private void getDataFromNet(final String url) {
        OkHttpUtils
                .get()
                .url(url)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "图组请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", "图组请求成功==" + response);
                        //存储数据
                        CacheUtils.putString(context,url,response);

                        //解析数据
                        processData(response);
                    }
                });
    }

    /**
     * 解析数据
     *
     * @param json
     */
    private void processData(String json) {
        PhotosMenuDetailPagerBean bean = new Gson().fromJson(json, PhotosMenuDetailPagerBean.class);
        datas = bean.getData().getNews();

        if (datas != null && datas.size() > 0) {
            //有数据
            progressbar.setVisibility(View.GONE);

            //设置适配器
            adapter = new PhotosMenuDetailPagerAdapater(context, datas,recyclerview);
            recyclerview.setAdapter(adapter);

            //布局管理器不要忘记
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        } else {
            progressbar.setVisibility(View.VISIBLE);
        }

        //隐藏刷新效果（小圆圈）
        refreshLayout.setRefreshing(false);
    }

    private boolean isShowList = false;

    public void switchListAndGrid(ImageButton ibSwitchListGrid) {
        if (isShowList) {
            //显示gridview
            ibSwitchListGrid.setBackgroundResource(R.drawable.icon_pic_list_type);
            recyclerview.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false));

            isShowList = false;
        } else {
            //显示List
            //布局管理器
            ibSwitchListGrid.setBackgroundResource(R.drawable.icon_pic_grid_type);
            recyclerview.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            isShowList = true;
        }

    }
}
