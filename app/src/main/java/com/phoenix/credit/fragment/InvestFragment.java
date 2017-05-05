package com.phoenix.credit.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.phoenix.credit.R;
import com.phoenix.credit.common.BaseFragment;

import butterknife.BindView;

/**
 * Created by flashing on 2017/4/28.
 */

public class InvestFragment extends BaseFragment {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invest;
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.invest);
        ivTitleSetting.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
    }
}
