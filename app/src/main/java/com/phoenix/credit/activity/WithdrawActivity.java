package com.phoenix.credit.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity {

    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.account_zhifubao)
    TextView accountZhifubao;
    @BindView(R2.id.select_bank)
    RelativeLayout selectBank;
    @BindView(R2.id.chongzhi_text)
    TextView chongzhiText;
    @BindView(R2.id.view)
    View view;
    @BindView(R2.id.et_input_money)
    EditText etInputMoney;
    @BindView(R2.id.chongzhi_text2)
    TextView chongzhiText2;
    @BindView(R2.id.textView5)
    TextView textView5;
    @BindView(R2.id.btn_withdraw)
    Button btnWithdraw;

    @Override
    protected void initData() {
        //设置当前的提现的Button是不可操作的
        btnWithdraw.setClickable(false);
        etInputMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String money = etInputMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)){
                    //设置Button不可操作
                    btnWithdraw.setClickable(false);
                    //修改背景颜色
                    btnWithdraw.setBackgroundResource(R.drawable.btn_02);
                }else {
                    //设置Button可操作
                    btnWithdraw.setClickable(true);
                    //修改背景颜色
                    btnWithdraw.setBackgroundResource(R.drawable.btn_01);
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("提现");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }
    
    @OnClick(R2.id.iv_title_back)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_withdraw;
    }

    @OnClick(R2.id.btn_withdraw)
    public void withdraw(View view){
        //将要提现的具体数额发送给后台，由后台连接第三方支付平台，完成金额的提现操作（略）
        UIUtils.toast("您的提现申请已被成功受理。审核通过后，24小时内，你的钱自然会到账", false);
        //退出当前页面
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                removeCurrentActivity();
            }
        }, 2000);
    }
}
