package com.atguigu.beijingnewstest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.atguigu.beijingnewslibrary.utils.CacheUtils;
import com.atguigu.beijingnewstest.activity.GuideActivity;
import com.atguigu.beijingnewstest.activity.MainActivity;

public class WelcomeActivity extends AppCompatActivity {

    private RelativeLayout rl_rootview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_rootview = (RelativeLayout)findViewById(R.id.rl_rootview);

        //欢迎页面动画，缩放，渐变，旋转
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(2000);
        sa.setFillAfter(true);

        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(sa);
        set.addAnimation(aa);
        set.addAnimation(ra);

        rl_rootview.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                boolean isStartMain = CacheUtils.getBoolean(WelcomeActivity.this, "start_main");
                if(isStartMain) {
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }else {
                    //进入引导页面
                    Intent intent = new Intent(WelcomeActivity.this,GuideActivity.class);
                    startActivity(intent);
                }


                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
