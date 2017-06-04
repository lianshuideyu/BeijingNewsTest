package com.atguigu.beijingnewstest.base;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.atguigu.beijingnewstest.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2017/6/2.
 */

public class BasePager {

    public Context context;

    public View rootView;
    @InjectView(R.id.tv_title)
    public TextView tvTitle;
    @InjectView(R.id.ib_menu)
    public ImageButton ibMenu;
    @InjectView(R.id.fl_content)
    public FrameLayout flContent;


    public BasePager(Context context) {
        this.context = context;

        rootView = View.inflate(context, R.layout.base_pager, null);
        ButterKnife.inject(this,rootView);
    }


    public void initData() {

    }
}
