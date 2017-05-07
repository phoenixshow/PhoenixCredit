package com.phoenix.event;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

public class MyButton extends android.support.v7.widget.AppCompatButton {


    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyButton dispatchTouchEvent() action = " + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyButton onTouchEvent() action = " + event.getAction());
        return super.onTouchEvent(event);
    }
}
