package com.phoenix.credit.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.utils.MD5Utils;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

import static android.R.attr.name;

public class UserRegistActivity extends BaseActivity {
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.et_register_number)
    EditText etRegisterNumber;
    @BindView(R2.id.et_register_name)
    EditText etRegisterName;
    @BindView(R2.id.et_register_pwd)
    EditText etRegisterPwd;
    @BindView(R2.id.et_register_pwdagain)
    EditText etRegisterPwdagain;
    @BindView(R2.id.btn_register)
    Button btnRegister;

    @Override
    protected void initData() {
        //设置注册Button的点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.获取用户注册的信息
                String name = etRegisterName.getText().toString().trim();
                String number = etRegisterNumber.getText().toString().trim();
                String pwd = etRegisterPwd.getText().toString().trim();
                String pwdAgain = etRegisterPwdagain.getText().toString().trim();

                //2.所填写的信息不能为空
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
                    UIUtils.toast("填写的信息不能为空", false);
                    return;
                }
                //3.两次密码必须一致
                else if(!pwd.equals(pwdAgain)){
                    UIUtils.toast("两次填写的密码不一致", false);
                    etRegisterPwd.setText("");
                    etRegisterPwdagain.setText("");
                    return;
                }
                //4.联网发送用户注册信息
                else {
                    String url = AppNetConfig.USER_REGISTER;
                    RequestParams params = new RequestParams();
                    params.put("name", name);
                    params.put("password", MD5Utils.MD5(pwd));
                    params.put("phone", number);
                    client.post(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String content = new String(responseBody);
                            JSONObject jsonObject = JSON.parseObject(content);
                            boolean isExist = jsonObject.getBoolean("isExist");
                            if (isExist){
                                UIUtils.toast("此用户已注册", false);
                            }else {
                                UIUtils.toast("注册成功", false);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            UIUtils.toast("联网请求失败", false);
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户注册");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R2.id.iv_title_back)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_regist;
    }
}
