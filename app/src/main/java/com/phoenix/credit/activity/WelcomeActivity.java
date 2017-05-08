package com.phoenix.credit.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.ActivityManager;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by flashing on 2017/4/28.
 */

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R2.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @BindView(R2.id.rl_welcome)
    RelativeLayout rlWelcome;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        //去掉窗口标题
//        getSupportActionBar().hide();
        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //将当前的activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);
        //提供启动动画
        setAnimation();
    }
    private void setAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);//0：完全透明  1：完全不透明
        alphaAnimation.setDuration(3000);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());//设置动画的变化率，可以匀速，也可以变速，这里是加速动画

        /*//方式一：
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            //当动画结束时：调用如下方法
            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();//销毁当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });*/
        //方式二：使用handler
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
//                finish();//销毁当前页面
                //结束activity的显示，并从栈空间中移除
                ActivityManager.getInstance().remove(WelcomeActivity.this);
            }
        }, 3000);

        //启动动画
        rlWelcome.startAnimation(alphaAnimation);
    }
}
