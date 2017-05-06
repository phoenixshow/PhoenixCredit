package com.phoenix.context;

import android.app.Application;
import android.util.Log;

/**
 * Created by flashing on 2017/5/6.
 */

public class MyApplication extends Application {
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public MyApplication() {
        Log.e("TAG", "MyApplication--------->MyApplication()");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "MyApplication--------->onCreate()");
    }
}
