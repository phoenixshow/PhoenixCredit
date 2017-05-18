package com.phoenix.credit.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.activity.GestureEditActivity;
import com.phoenix.credit.activity.UserRegistActivity;
import com.phoenix.credit.common.BaseActivity;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by flashing on 2017/4/28.
 */

public class MoreFragment extends BaseFragment {
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.tv_more_regist)
    TextView tvMoreRegist;
    @BindView(R2.id.toggle_more)
    ToggleButton toggleMore;
    @BindView(R2.id.tv_more_reset)
    TextView tvMoreReset;
    @BindView(R2.id.rl_more_contact)
    RelativeLayout rlMoreContact;
    @BindView(R2.id.tv_more_sms)
    TextView tvMoreSms;
    @BindView(R2.id.tv_more_share)
    TextView tvMoreShare;
    @BindView(R2.id.tv_more_about)
    TextView tvMoreAbout;
    @BindView(R2.id.tv_more_phone)
    TextView tvMorePhone;

    private SharedPreferences sp;

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
        //初始化SharedPreferences
        sp = this.getActivity().getSharedPreferences("secret_protect", Context.MODE_PRIVATE);

        //用户注册
        userResgist();

        //获取当前设置手势密码的ToggleButton状态
        getGestureStatus();

        //设置手势密码
        setGesturePassword();

        //重置手势密码
        resetGesture();

        //联系客服
        contactService();
    }

    private void contactService() {
        rlMoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MoreFragment.this.getActivity())
                        .setTitle("联系客服")
                        .setMessage("是否现在联系客服：010-12345678")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //获取手机号码
                                String phone = tvMorePhone.getText().toString().trim();
                                //使用隐式意图，启动系统拨号应用界面
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + phone));
                                if (ActivityCompat.checkSelfPermission(MoreFragment.this.getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    UIUtils.toast("您还未授予拨打电话的权限哦~", true);
                                    return;
                                }
                                MoreFragment.this.getActivity().startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
    }

    private void resetGesture() {
        tvMoreReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = toggleMore.isChecked();
                if (checked) {
                    ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(GestureEditActivity.class, null);
                } else {
                    UIUtils.toast("手势密码操作已关闭，请开启后再设置", false);
                }
            }
        });
    }

    private void getGestureStatus() {
        boolean isOpen = sp.getBoolean("isOpen", false);
        toggleMore.setChecked(isOpen);
    }

    private void setGesturePassword() {
        toggleMore.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    UIUtils.toast("开启了手势密码", false);
//                    sp.edit().putBoolean("isOpen", true).commit();
                    String inputCode = sp.getString("inputCode", "");
                    if (TextUtils.isEmpty(inputCode)) {//之前没有设置过
                        new AlertDialog.Builder(MoreFragment.this.getActivity())
                                .setTitle("设置手势密码")
                                .setMessage("是否现在设置手势密码")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UIUtils.toast("现在设置手势密码", false);
                                        sp.edit().putBoolean("isOpen", true).commit();
//                                        toggleMore.setChecked(true);
                                        //开启新的Activity
                                        ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(GestureEditActivity.class, null);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        UIUtils.toast("取消了现在设置手势密码", false);
                                        sp.edit().putBoolean("isOpen", false).commit();
                                        toggleMore.setChecked(false);
                                    }
                                })
                                .show();
                    } else {
                        UIUtils.toast("开启了手势密码", false);
                        sp.edit().putBoolean("isOpen", true).commit();
//                        toggleMore.setChecked(true);
                    }
                } else {
                    UIUtils.toast("关闭了手势密码", false);
                    sp.edit().putBoolean("isOpen", false).commit();
//                    toggleMore.setChecked(false);
                }
            }
        });
    }

    private void userResgist() {
        tvMoreRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) MoreFragment.this.getActivity()).goToActivity(UserRegistActivity.class, null);
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
