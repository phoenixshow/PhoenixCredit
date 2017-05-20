package com.phoenix.credit.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zgj.multiChannelPackageTool.MCPTool;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.ActivityManager;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.fragment.HomeFragment;
import com.phoenix.credit.fragment.InvestFragment;
import com.phoenix.credit.fragment.MeFragment;
import com.phoenix.credit.fragment.MoreFragment;
import com.phoenix.credit.utils.ChannelUtil;
import com.phoenix.credit.utils.UIUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private final int SDK_PERMISSION_REQUEST = 127;
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
    protected void initData() {
//        getPersimmions();
        //默认显示首页
        setSelect(0);

        /*//模拟异常
        String str = null;
        if (str.equals("abc")){
            Log.e("TAG", "onCreate--------->");
        }*/

        //显示来自于哪个渠道的应用
        String channel = getChannel();
        UIUtils.toast(channel, false);

        //美团的多渠道打包_获取渠道信息
        String channel_meituan = ChannelUtil.getChannel(this, "yingyongbao");
        UIUtils.toast(channel_meituan, false);

        //360的多渠道打包_获取渠道信息，参数第二个是密码，第三个是默认值
        String channel_360 = MCPTool.getChannelId(this,"12345678","");
        UIUtils.toast(channel_meituan, false);
    }

    private String getChannel() {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString("UMENG_CHANNEL");
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return "";
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R2.id.ll_main_home, R2.id.ll_main_invest, R2.id.ll_main_me, R2.id.ll_main_more})
    public void showTab(View view) {
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
                //错误的调用位置，这时生命周期方法BaseFragment中的onCreateView还没有调用，LoadingPage还未初始化就调用它的show方法会空指针
//                homeFragment.show();
                //改变选中项的图片和文本颜色的变化
                ivMainHome.setImageResource(R.drawable.bottom02);
                tvMainHome.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;
            case 1:
                if (investFragment == null){
                    investFragment = new InvestFragment();
                    transaction.add(R.id.fl_main, investFragment);
                }
                transaction.show(investFragment);
                ivMainInvest.setImageResource(R.drawable.bottom04);
                tvMainInvest.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;
            case 2:
                if (meFragment == null){
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                transaction.show(meFragment);
                ivMainMe.setImageResource(R.drawable.bottom06);
                tvMainMe.setTextColor(UIUtils.getColor(R.color.home_back_selected01));
                break;
            case 3:
                if (moreFragment == null){
                    moreFragment = new MoreFragment();
                    transaction.add(R.id.fl_main, moreFragment);
                }
                transaction.show(moreFragment);
                ivMainMore.setImageResource(R.drawable.bottom08);
                tvMainMore.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                break;
        }
//        transaction.commit();//提交事务
        transaction.commitAllowingStateLoss();//提交事务
    }

    private void resetTab() {
        ivMainHome.setImageResource(R.drawable.bottom01);
        ivMainInvest.setImageResource(R.drawable.bottom03);
        ivMainMe.setImageResource(R.drawable.bottom05);
        ivMainMore.setImageResource(R.drawable.bottom07);

        tvMainHome.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        tvMainInvest.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        tvMainMe.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
        tvMainMore.setTextColor(UIUtils.getColor(R.color.home_back_unselected));
//        //这种方式也可以
//        tvMainMore.setTextColor(ContextCompat.getColor(this, R.color.home_back_unselected));
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

//    @TargetApi(23)
//    private void getPersimmions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            ArrayList<String> permissions = new ArrayList<>();
//            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
//            }
//
//            if (permissions.size() > 0) {
//                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
//            }else {
//                //默认显示首页
//                setSelect(0);
//            }
//        }else {
//            //默认显示首页
//            setSelect(0);
//        }
//    }
//
//    @TargetApi(23)
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case SDK_PERMISSION_REQUEST:
//                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    // 允许
//                    //默认显示首页
//                    setSelect(0);
//                }else{
//                    // 不允许
//                    UIUtils.toast("已拒绝授权", true);
//                }
//                break;
//        }
//    }
}
