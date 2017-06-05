package com.atguigu.beijingnewstest.menudetailpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.base.MenuDetailBasePager;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;
import com.atguigu.beijingnewstest.domain.TabDetailPagerBean;
import com.atguigu.beijingnewstest.utils.ConstantUtils;
import com.atguigu.beijingnewstest.utils.DensityUtil;
import com.atguigu.beijingnewstest.view.HorizontalScrollViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Call;

/**
 * Created by Administrator on 2017/6/4.
 */

public class TabDetailPager extends MenuDetailBasePager {

    @InjectView(R.id.pull_refresh_list)
    PullToRefreshListView pull_refresh_list;
    ListView lv;

    private NewsCenterBean.DataBean.ChildrenBean childrenBean;

    private String url;

    private List<TabDetailPagerBean.DataBean.TopnewsBean> topnews;
    private int prePosition = 0;

    /**
     * 新闻列表数据集合
     */
    private List<TabDetailPagerBean.DataBean.NewsBean> newsBeanList;
    private MyListAdapter adapter;

    HorizontalScrollViewPager viewpager;
    TextView tvTitle;
    LinearLayout llPointGroup;

    private String moreUrl;//上拉刷新的链接
    private boolean isLoadingMore = false;//判断是否为下拉刷新

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

        //得到ListView
        lv = pull_refresh_list.getRefreshableView();

        //顶部视图
        View viewTopNews = View.inflate(context, R.layout.tab_detail_topnews, null);
        viewpager = (HorizontalScrollViewPager) viewTopNews.findViewById(R.id.viewpager);
        tvTitle = (TextView) viewTopNews.findViewById(R.id.tv_title);
        llPointGroup = (LinearLayout) viewTopNews.findViewById(R.id.ll_point_group);
        //把顶部的部分已添加头的方式加入listview中
        lv.addHeaderView(viewTopNews);

        //监听页面的变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //把之前设置为默认
                llPointGroup.getChildAt(prePosition).setEnabled(false);
                //当前的设置true
                llPointGroup.getChildAt(position).setEnabled(true);

                prePosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                tvTitle.setText(topnews.get(position).getTitle());


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置上下来刷新的监听
        pull_refresh_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新
                isLoadingMore = false;
                getDataFromNet(url);
                Toast.makeText(context, "下拉刷新...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                //上拉加载更多
                if(!TextUtils.isEmpty(moreUrl)) {
                    isLoadingMore = true;
                    getDataFromNet(moreUrl);
                }else {
                    Toast.makeText(context, "没有更多数据了...", Toast.LENGTH_SHORT).show();

                }

            }
        });


        return view;
    }

    @Override
    public void initData() {
        super.initData();
        //设置数据
        //tvTitle.setText(childrenBean.getTitle());

        url = ConstantUtils.BASE_URL + childrenBean.getUrl();
        Log.e("TAG", "url==" + url);
        //设置数据
        getDataFromNet(url);
    }

    private void getDataFromNet(String url) {

        OkHttpUtils
                .get()
                .url(url)
//                .addParams("username", "hyman")
//                .addParams("password", "123")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "请求失败==" + e.getMessage());

                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        Log.e("TAG", "请求成功==" + response);
                        //缓存数据
                        processData(response);
                        //结束刷新
                        pull_refresh_list.onRefreshComplete();
                    }


                });
    }

    private void processData(String response) {
        TabDetailPagerBean bean = new Gson().fromJson(response, TabDetailPagerBean.class);

        Log.e("TAG", "" + bean.getData().getNews().get(0).getTitle());

        String more = bean.getData().getMore();
        if(!TextUtils.isEmpty(more)) {
            moreUrl = ConstantUtils.BASE_URL +  more;
        }

        if(!isLoadingMore) {
            //下拉刷新
            topnews = bean.getData().getTopnews();

            viewpager.setAdapter(new MyPagerAdapter());
            //设置图片标题的文字
            tvTitle.setText(topnews.get(prePosition).getTitle());

            //添加图片viewPager的指示点
            llPointGroup.removeAllViews();//把之前的先移除掉
            for (int i = 0; i < topnews.size(); i++) {
                ImageView point = new ImageView(context);
                point.setBackgroundResource(R.drawable.point_selector);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, 8), DensityUtil.dip2px(context, 8));
                point.setLayoutParams(params);

                if (i == 0) {
                    point.setEnabled(true);
                } else {
                    point.setEnabled(false);
                    params.leftMargin = DensityUtil.dip2px(context, 8);
                }

                //添加到线性布局中
                llPointGroup.addView(point);
            }

            //设置listview的数据
            newsBeanList = bean.getData().getNews();
            adapter = new MyListAdapter();
            lv.setAdapter(new MyListAdapter());
        }else {
            isLoadingMore = false;
            //上拉刷新
            //把新的集合加进来
            newsBeanList.addAll(bean.getData().getNews());
            adapter.notifyDataSetChanged();
        }





    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.drawable.pic_item_list_default);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            //设置网络图片

            String imageUrl = ConstantUtils.BASE_URL + topnews.get(position).getTopimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            container.addView(imageView);

            return imageView;
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

    private class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return newsBeanList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_tab_detail, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //根据位置得到对应的数据
            TabDetailPagerBean.DataBean.NewsBean newsBean = newsBeanList.get(position);
            viewHolder.tvDesc.setText(newsBean.getTitle());
            viewHolder.tvTime.setText(newsBean.getPubdate());

            String imageUrl = ConstantUtils.BASE_URL+newsBean.getListimage();
            Glide.with(context)
                    .load(imageUrl)
                    .placeholder(R.drawable.pic_item_list_default)
                    .error(R.drawable.pic_item_list_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.ivIcon);
            return convertView;
        }

    }
    static class ViewHolder {
        @InjectView(R.id.iv_icon)
        ImageView ivIcon;
        @InjectView(R.id.tv_desc)
        TextView tvDesc;
        @InjectView(R.id.tv_time)
        TextView tvTime;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
