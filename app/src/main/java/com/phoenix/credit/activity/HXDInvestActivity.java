package com.phoenix.credit.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenix.credit.R;
import com.phoenix.credit.common.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class HXDInvestActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("关于惠信贷理财");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.iv_title_back)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hxd_invest;
    }
}
