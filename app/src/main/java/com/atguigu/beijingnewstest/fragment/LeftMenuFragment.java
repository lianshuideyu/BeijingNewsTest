package com.atguigu.beijingnewstest.fragment;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.beijingnewstest.R;
import com.atguigu.beijingnewstest.activity.MainActivity;
import com.atguigu.beijingnewstest.base.BaseFragment;
import com.atguigu.beijingnewstest.domain.NewsCenterBean;
import com.atguigu.beijingnewstest.pager.NewsPager;

import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LeftMenuFragment extends BaseFragment {
    private ListView listView;

    private MyAdapter adapter;
    private List<NewsCenterBean.DataBean> datas;
    private int prePositon = 0;
    @Override
    public View initView() {
        listView = new ListView(context);

        //设置ListView的item的点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positon, long l) {
                prePositon = positon;
                //刷新适配器，重新调用适配器getView()方法
                adapter.notifyDataSetChanged();

                MainActivity mainActivity = (MainActivity) context;
                mainActivity.getSlidingMenu().toggle();//关<--->开

                //点击切换新闻页面的内容
                switchPager(prePositon);


            }
        });
        return listView;
    }

    private void switchPager(int positon) {
        MainActivity mainActivity = (MainActivity) context;
        ContentFragment contentFragment = mainActivity.getContentFragment();
        NewsPager newsPager = (NewsPager) contentFragment.pagers.get(1);
        newsPager.swichPager(positon);
    }

    @Override
    public void initData() {
        super.initData();


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

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        switchPager(prePositon);
    }


    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public NewsCenterBean.DataBean getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View itemView, ViewGroup viewGroup) {
            //Log.e("TAG","适配器");
            TextView textView = (TextView) View.inflate(context, R.layout.leftmenu_item,null);

            textView.setText(datas.get(position).getTitle());
            if(position == prePositon) {
                textView.setEnabled(true);
            }else {
                textView.setEnabled(false);
            }

            return textView;
        }
    }
}
