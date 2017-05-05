package com.phoenix.credit.common;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phoenix.credit.R;
import com.phoenix.credit.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by flashing on 2017/5/5.
 */
public abstract class BaseFragment extends Fragment {
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = UIUtils.getView(getLayoutId());
        unbinder = ButterKnife.bind(this, view);

        //初始化Title
        initTitle();
        //初始化数据
        initData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //初始化界面的数据
    protected abstract void initData();
    //初始化Title
    protected abstract void initTitle();
    //提供布局
    protected abstract int getLayoutId();
}
