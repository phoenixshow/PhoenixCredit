package com.phoenix.credit.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.utils.UIUtils;

import cz.msebera.android.httpclient.Header;

import static com.phoenix.credit.common.PhoenixApplication.context;

/**
 * Created by flashing on 2017/5/5.
 */

public abstract class LoadingPage extends FrameLayout {
    //1.定义4种不同的显示状态
    private static final int STATE_LOADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;
    private static final int STATE_SUCCESS = 4;
    private int state_current = STATE_LOADING;//默认情况下，当前状态为正在加载

    //2.提供4种不同的界面
    private View view_loading;
    private View view_error;
    private View view_empty;
    private View view_success;
    private LayoutParams params;
    private ResultState resultState;
    private Context mContext;

    public LoadingPage(Context context) {
        this(context, null);
    }

    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    //初始化方法
    private void init() {
        //实例化View
        //1.提供布局显示的参数
        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (view_loading == null){
            //2.加载布局
            view_loading = UIUtils.getView(R.layout.page_loading);
            //3.添加到当前的FrameLayout中
            addView(view_loading, params);
        }
        if (view_empty == null){
            //2.加载布局
            view_empty = UIUtils.getView(R.layout.page_empty);
            //3.添加到当前的FrameLayout中
            addView(view_empty, params);
        }
        if (view_error == null){
            //2.加载布局
            view_error = UIUtils.getView(R.layout.page_error);
            //3.添加到当前的FrameLayout中
            addView(view_error, params);
        }
        
        showSafePage();
    }

    //保证如下的操作在主线程中执行：更新界面
    private void showSafePage() {
        UIUtils.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //保证run()中的操作在主线程中执行
                showPage();
            }
        });
    }

    private void showPage() {
        //根据当前state_current的值，决定显示哪个View
        view_loading.setVisibility(state_current == STATE_LOADING ? View.VISIBLE : INVISIBLE);
        view_error.setVisibility(state_current == STATE_ERROR ? View.VISIBLE : INVISIBLE);
        view_empty.setVisibility(state_current == STATE_EMPTY ? View.VISIBLE : INVISIBLE);

        if (view_success == null){
//            view_success = UIUtils.getView(layoutId());//加载布局使用的是Application
            view_success = View.inflate(mContext, layoutId(), null);
            addView(view_success, params);
        }

        view_success.setVisibility(state_current == STATE_SUCCESS ? View.VISIBLE : INVISIBLE);
    }

    //在show()中实现联网加载数据
    public void show(){
        String url = url();
        //如果有的页面不需要联网
        if (TextUtils.isEmpty(url)){
            resultState = ResultState.SUCCESS;
            resultState.setContent("");
            loadImage();//为了修改state_current，并且决定加载哪个页面：showSafePage()
            return;
        }

        //因为本地数据加载太快，看不到加载中的过程，实际项目中应当取消延迟操作
        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url(), params(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String content = new String(responseBody);
                        if (TextUtils.isEmpty(content)){
                            resultState = ResultState.EMPTY;
                            resultState.setContent("");
                        }else {
                            resultState = ResultState.SUCCESS;
                            resultState.setContent(content);
                        }
                        loadImage();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        resultState = ResultState.ERROR;
                        resultState.setContent("");
                        loadImage();
                    }
                });
            }
        }, 2000);
    }

    private void loadImage() {
        switch (resultState) {
            case ERROR:
                state_current = STATE_ERROR;
                break;
            case EMPTY:
                state_current = STATE_EMPTY;
                break;
            case SUCCESS:
                state_current = STATE_SUCCESS;
                break;
        }
        //根据修改以后的state_current更新视图的显示
        showSafePage();

        if (state_current == STATE_SUCCESS){
            onSuccess(resultState, view_success);
        }
    }

    protected abstract int layoutId();
    //提供联网的请求地址
    protected abstract String url();
    //提供联网的请求参数
    protected abstract RequestParams params();

    protected abstract void onSuccess(ResultState resultState, View view_success);

    //提供枚举类，封装联网以后的状态值和数据
    public enum ResultState{
        ERROR(2),
        EMPTY(3),
        SUCCESS(4);

        int state;
        private String content;

        ResultState(int state){
            this.state = state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
