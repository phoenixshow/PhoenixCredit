package com.phoenix.context;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import static android.R.attr.data;

/**
 * Created by flashing on 2017/5/6.
 */

public class MyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyApplication application = (MyApplication) this.getApplication();
        String data = application.getData();
        Log.e("TAG", "onStartCommand--------->data=" + data);
        return super.onStartCommand(intent, flags, startId);
    }
}
