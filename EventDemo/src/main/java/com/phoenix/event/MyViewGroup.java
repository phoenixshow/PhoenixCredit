package com.phoenix.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyViewGroup extends LinearLayout {
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TAG", "MyViewGroup dispatchTouchEvent() action = " + ev.getAction());
        return super.dispatchTouchEvent(ev);
//        return false;
//        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("TAG", "MyViewGroup onInterceptTouchEvent() action = " + ev.getAction());
//        return true;
        if(ev.getAction() == MotionEvent.ACTION_MOVE){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyViewGroup onTouchEvent() action = " + event.getAction());
//        return super.onTouchEvent(event);
        return true;
    }
}
