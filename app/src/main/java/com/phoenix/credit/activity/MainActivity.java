package com.phoenix.credit.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.ActivityManager;
import com.phoenix.credit.fragment.HomeFragment;
import com.phoenix.credit.fragment.InvestFragment;
import com.phoenix.credit.fragment.MeFragment;
import com.phoenix.credit.fragment.MoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final int WHAT_RESET_BACK = 1;

    @BindView(R2.id.fl_main)
    FrameLayout flMain;
    @BindView(R2.id.iv_main_home)
    ImageView ivMainHome;
    @BindView(R2.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R2.id.ll_main_home)
    LinearLayout llMainHome;
    @BindView(R2.id.iv_main_invest)
    ImageView ivMainInvest;
    @BindView(R2.id.tv_main_invest)
    TextView tvMainInvest;
    @BindView(R2.id.ll_main_invest)
    LinearLayout llMainInvest;
    @BindView(R2.id.iv_main_me)
    ImageView ivMainMe;
    @BindView(R2.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R2.id.ll_main_me)
    LinearLayout llMainMe;
    @BindView(R2.id.iv_main_more)
    ImageView ivMainMore;
    @BindView(R2.id.tv_main_more)
    TextView tvMainMore;
    @BindView(R2.id.ll_main_more)
    LinearLayout llMainMore;

    private HomeFragment homeFragment;
    private InvestFragment investFragment;
    private MeFragment meFragment;
    private MoreFragment moreFragment;
    private FragmentTransaction transaction;

    private boolean flag = true;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    flag = true;//复原
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //将当前的activity添加到ActivityManager中
        ActivityManager.getInstance().add(this);

        //默认显示首页
        setSelect(0);
        
        /*//模拟异常
        String str = null;
        if (str.equals("abc")){
            Log.e("TAG", "onCreate--------->");
        }*/
    }

    @OnClick({R2.id.ll_main_home, R2.id.ll_main_invest, R2.id.ll_main_me, R2.id.ll_main_more})
    public void showTab(View view) {
//        Toast.makeText(this, "选择了具体的Tab", Toast.LENGTH_LONG).show();
        switch (view.getId()) {
            case R.id.ll_main_home://首页
                setSelect(0);
                break;
            case R.id.ll_main_invest://投资
                setSelect(1);
                break;
            case R.id.ll_main_me://我的资产
                setSelect(2);
                break;
            case R.id.ll_main_more://更多
                setSelect(3);
                break;
        }
    }

    //提供相应的Fragment的显示
    private void setSelect(int i) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        //隐藏所有Fragment的显示
        hideFragments();
        //重置ImageView和TextView的显示状态
        resetTab();

        switch (i) {
            case 0:
                if (homeFragment == null){
                    homeFragment = new HomeFragment();//创建对象以后，并不会马上调用生命周期方法，而是在commit()之后才调用
                    transaction.add(R.id.fl_main, homeFragment);
                }
                //显示当前的Fragment
                transaction.show(homeFragment);
                //改变选中项的图片和文本颜色的变化
                ivMainHome.setImageResource(R.drawable.bottom02);
                tvMainHome.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 1:
                if (investFragment == null){
                    investFragment = new InvestFragment();
                    transaction.add(R.id.fl_main, investFragment);
                }
                transaction.show(investFragment);
                ivMainInvest.setImageResource(R.drawable.bottom04);
                tvMainInvest.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 2:
                if (meFragment == null){
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                transaction.show(meFragment);
                ivMainMe.setImageResource(R.drawable.bottom06);
                tvMainMe.setTextColor(getResources().getColor(R.color.home_back_selected01));
                break;
            case 3:
                if (moreFragment == null){
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.fl_main, moreFragment);
                }
                transaction.show(moreFragment);
                ivMainMore.setImageResource(R.drawable.bottom08);
                tvMainMore.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
        }
        transaction.commit();//提交事务
    }

    private void resetTab() {
        ivMainHome.setImageResource(R.drawable.bottom01);
        ivMainInvest.setImageResource(R.drawable.bottom03);
        ivMainMe.setImageResource(R.drawable.bottom05);
        ivMainMore.setImageResource(R.drawable.bottom07);

        tvMainHome.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainInvest.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainMe.setTextColor(getResources().getColor(R.color.home_back_unselected));
        tvMainMore.setTextColor(getResources().getColor(R.color.home_back_unselected));
    }

    private void hideFragments() {
        if (homeFragment != null){
            transaction.hide(homeFragment);
        }
        if (investFragment != null){
            transaction.hide(investFragment);
        }
        if (meFragment != null){
            transaction.hide(meFragment);
        }
        if (moreFragment != null){
            transaction.hide(moreFragment);
        }
    }

    //重写onKeyUp()，实现连续两次点击方可退出当前应用
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag){
            Toast.makeText(this, R.string.click_again, Toast.LENGTH_LONG).show();
            flag = false;
            //发送延迟消息
            handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    //为了避免出现内存的泄漏，需要在onDestroy()中，移除所有未被执行的消息
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        //方式一：移除指定ID的所有消息
//        handler.removeMessages(WHAT_RESET_BACK);
        //方式二：移除所有的未被执行的消息
        handler.removeCallbacksAndMessages(null);
    }
}
