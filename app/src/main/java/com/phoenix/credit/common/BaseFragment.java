package com.phoenix.credit.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.ui.LoadingPage;
import com.phoenix.credit.utils.UIUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by flashing on 2017/5/5.
 */
public abstract class BaseFragment extends Fragment {
    Unbinder unbinder;
    private LoadingPage loadingPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = UIUtils.getView(getLayoutId());
        loadingPage = new LoadingPage(container.getContext()){
            @Override
            protected int layoutId() {
                return getLayoutId();
            }

            @Override
            protected String url() {
                return getUrl();
            }

            @Override
            protected RequestParams params() {
                return getParams();
            }

            @Override
            protected void onSuccess(ResultState resultState, View view_success) {
                unbinder = ButterKnife.bind(BaseFragment.this, view_success);
                initTitle();
                initData(resultState.getContent());
            }
        };

        return loadingPage;
    }

    //为了保证loadingPage不为NULL，所以在这里调用show()方法
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*//模拟联网操作的延迟//但写在这里不太好
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        }, 2000);*/
        show();
    }

    public void show(){
        loadingPage.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    //初始化界面的数据
    protected abstract void initData(String content);
    //初始化Title
    protected abstract void initTitle();
    //提供布局
    protected abstract int getLayoutId();

    protected abstract String getUrl();

    protected abstract RequestParams getParams();
}
