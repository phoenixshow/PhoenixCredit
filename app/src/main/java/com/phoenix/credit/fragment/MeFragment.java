package com.phoenix.credit.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.activity.BarChartActivity;
import com.phoenix.credit.activity.LineChartActivity;
import com.phoenix.credit.activity.LoginActivity;
import com.phoenix.credit.activity.PieChartActivity;
import com.phoenix.credit.activity.RechargeActivity;
import com.phoenix.credit.activity.UserInfoActivity;
import com.phoenix.credit.activity.WithdrawActivity;
import com.phoenix.credit.bean.User;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.utils.BitmapUtils;
import com.phoenix.credit.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by flashing on 2017/4/28.
 */

public class MeFragment extends BaseFragment {
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.iv_me_icon)
    ImageView ivMeIcon;
    @BindView(R2.id.rl_me_icon)
    RelativeLayout rlMeIcon;
    @BindView(R2.id.tv_me_name)
    TextView tvMeName;
    @BindView(R2.id.rl_me)
    RelativeLayout rlMe;
    @BindView(R2.id.recharge)
    ImageView recharge;
    @BindView(R2.id.withdraw)
    ImageView withdraw;
    @BindView(R2.id.ll_investment)
    TextView llTouzi;
    @BindView(R2.id.ll_investment_intuition)
    TextView llTouziZhiguan;
    @BindView(R2.id.ll_assets)
    TextView llZichan;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {
        //判断用户是否已经登录
        isLogin();
    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)){
            //本地没有保存过用户信息，给出提示：登录
            doLogin();
        }else {
            //已经登录过，则直接加载用户的信息并显示
            doUser();
        }
    }

    //加载用户信息并显示
    private void doUser() {
        //1.读取本地保存的用户信息
        User user = ((BaseActivity)this.getActivity()).readUser();
        //2.获取对象信息，并设置给相应的视图显示
        tvMeName.setText(user.getName());
        //判断本地是否已经保存头像的图片，如果有，则不再执行联网操作
        boolean isExist = readImage();
        if (isExist){
            return;
        }
        //使用Picasso联网获取图片
        Picasso.with(this.getActivity()).load(AppNetConfig.BASE_URL+user.getImageurl()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {//下载以后的内存中的Bitmap对象
                //压缩处理
                Bitmap bitmap = BitmapUtils.zoom(source, UIUtils.dp2px(62), UIUtils.dp2px(62));
                //保存到本地
                BitmapUtils.saveImage(bitmap);
                //圆形处理
                bitmap = BitmapUtils.circleBitmap(source);
                //回收Bitmap资源
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "";//需要保证返回值不能为空，否则报错
            }
        }).into(ivMeIcon);
    }

    //给出提示：登录
    private void doLogin() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您还没有登录哦！么么~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        UIUtils.toast("进入登录页面", false);
                        ((BaseActivity)MeFragment.this.getActivity()).goToActivity(LoginActivity.class, null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.INVISIBLE);//为使标题居中，不要设为GONE
        tvTitle.setText(R.string.my_assets);
        ivTitleSetting.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.iv_title_setting)
    public void setting(View view){
        //启动用户信息界面的Activity
        ((BaseActivity)this.getActivity()).goToActivity(UserInfoActivity.class, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        //读取本地保存的图片
        readImage();
    }

    private boolean readImage() {
        Bitmap bitmap = BitmapUtils.readImage();
        if (bitmap != null){
            ivMeIcon.setImageBitmap(bitmap);
            return true;
        }
        return false;
    }

    //设置充值操作
    @OnClick(R2.id.recharge)
    public void recharge(View view){
        ((BaseActivity)this.getActivity()).goToActivity(RechargeActivity.class, null);
    }

    //设置提现操作
    @OnClick(R2.id.withdraw)
    public void withdraw(View view){
        ((BaseActivity)this.getActivity()).goToActivity(WithdrawActivity.class, null);
    }

    //启动折线图
    @OnClick(R2.id.ll_investment)
    public void startLineChart(View view){
        ((BaseActivity)this.getActivity()).goToActivity(LineChartActivity.class, null);
    }

    //启动柱状图
    @OnClick(R2.id.ll_investment_intuition)
    public void startBarChart(View view){
      ((BaseActivity)this.getActivity()).goToActivity(BarChartActivity.class, null);
    }

    //启动饼图
    @OnClick(R2.id.ll_assets)
    public void startPieChart(View view){
        ((BaseActivity)this.getActivity()).goToActivity(PieChartActivity.class, null);
    }
}
