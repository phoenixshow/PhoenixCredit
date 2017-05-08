package com.phoenix.credit.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.activity.LoginActivity;
import com.phoenix.credit.bean.User;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
    @BindView(R2.id.ll_touzi)
    TextView llTouzi;
    @BindView(R2.id.ll_touzi_zhiguan)
    TextView llTouziZhiguan;
    @BindView(R2.id.ll_zichan)
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
        Picasso.with(this.getActivity()).load(AppNetConfig.BASE_URL+user.getImageurl()).into(ivMeIcon);
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
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.my_assets);
        ivTitleSetting.setVisibility(View.GONE);
    }
}
