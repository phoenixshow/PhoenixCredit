package com.phoenix.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyView dispatchTouchEvent() action = " + event.getAction());
        //反拦截
//        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyView onTouchEvent() action = " + event.getAction());
//        return super.onTouchEvent(event);
        //反拦截
        getParent().requestDisallowInterceptTouchEvent(true);
        return true;
    }
}
