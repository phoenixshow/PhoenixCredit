package com.phoenix.credit.adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by flashing on 2017/5/6.
 */

public abstract class BaseHolder<T> {
    private View rootView;
    private T data;

    public BaseHolder() {
        rootView = initView();
        rootView.setTag(this);
        ButterKnife.bind(this, rootView);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        refreshData();
    }

    public View getRootView() {
        return rootView;
    }

    //提供Item的布局
    protected abstract View initView();
    //装配过程
    protected abstract void refreshData();
}
