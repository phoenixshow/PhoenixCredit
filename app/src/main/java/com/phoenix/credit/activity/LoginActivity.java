package com.phoenix.credit.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.bean.User;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.utils.MD5Utils;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * Created by flashing on 2017/5/7.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.tv_login_number)
    TextView tvLoginNumber;
    @BindView(R2.id.et_login_number)
    EditText etLoginNumber;
    @BindView(R2.id.rl_login)
    RelativeLayout rlLogin;
    @BindView(R2.id.tv_login_pwd)
    TextView tvLoginPwd;
    @BindView(R2.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R2.id.btn_login)
    Button btnLogin;

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("用户登录");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    public void login(View view){//登录按钮的点击事件
        String number = etLoginNumber.getText().toString().trim();
        String pwd = etLoginPwd.getText().toString().trim();
        if(!TextUtils.isEmpty(number) && !TextUtils.isEmpty(pwd)){
            String url = AppNetConfig.LOGIN;
            RequestParams params = new RequestParams();
            params.put("phone", number);
            params.put("password", MD5Utils.MD5(pwd));
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {//200 404
                    String content = new String(responseBody);
                    //解析Json
                    JSONObject jsonObject = JSON.parseObject(content);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success){
                        //解析Json数据，生成User对象
                        String data = jsonObject.getString("data");
                        User user = JSON.parseObject(data, User.class);

                        //保存用户信息
                        saveUser(user);
                        //重新加载界面
                        removeAll();//销毁所有的Activity
                        goToActivity(MainActivity.class, null);
                    }else {
                        UIUtils.toast("用户名不存在或密码不正确", true);
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    UIUtils.toast("联网失败", false);
                }
            });
        }else{
            UIUtils.toast("用户名或密码不能为空", false);
        }
    }

    @OnClick(R.id.iv_title_back)
    protected void back(View view){
        removeAll();
        goToActivity(MainActivity.class, null);
    }
}
