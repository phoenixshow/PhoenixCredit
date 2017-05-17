package com.phoenix.credit.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.activity.UserRegistActivity;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.common.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by flashing on 2017/4/28.
 */

public class MoreFragment extends BaseFragment {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R.id.tv_more_regist)
    TextView tvMoreRegist;
    @BindView(R.id.toggle_more)
    ToggleButton toggleMore;
    @BindView(R.id.tv_more_reset)
    TextView tvMoreReset;
    @BindView(R.id.rl_more_contact)
    RelativeLayout rlMoreContact;
    @BindView(R.id.tv_more_sms)
    TextView tvMoreSms;
    @BindView(R.id.tv_more_share)
    TextView tvMoreShare;
    @BindView(R.id.tv_more_about)
    TextView tvMoreAbout;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
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
        //用户注册
        userResgist();

        //设置手势密码

    }

    private void userResgist() {
        tvMoreRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)MoreFragment.this.getActivity()).goToActivity(UserRegistActivity.class, null);
            }
        });
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.more);
        ivTitleSetting.setVisibility(View.GONE);
    }
}
