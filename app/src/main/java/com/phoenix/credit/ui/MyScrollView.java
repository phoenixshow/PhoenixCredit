package com.phoenix.credit.ui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

import com.phoenix.credit.utils.UIUtils;

import static android.R.attr.translateY;
import static com.phoenix.credit.common.PhoenixApplication.context;

/**
 * 自定义ViewGroup
 */

public class MyScrollView extends ScrollView {

    private View childView;
    //上一次Y轴方向操作的坐标位置
    private int lastY;
    //用于记录临界状态的左、上、右、下
    private Rect normal = new Rect();
    //是否动画结束
    private boolean isFinishAnimation = true;
    private int lastX,downX,downY;

    //拦截：实现父视图对子视图的拦截。是否拦截成功，取决于方法的返回值，返回true拦截成功，反之失败
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        int eventX = (int) ev.getX();
        int eventY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //onInterceptTouchEvent早于onTouchEvent，所以要给lastY赋值
                lastX = downX = eventX;
                lastY = downY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                //获取水平和垂直方向的移动距离
                int absX = Math.abs(eventX - downX);
                int absY = Math.abs(eventY - downY);

                if (absY > absX && absY >= UIUtils.dp2px(10)){
                    //执行拦截
                    isIntercept = true;
                }

                lastX = eventX;
                lastY = eventY;
                break;
        }
        return isIntercept;
    }

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        //父类的构造方法调用三个参数的构造器，第三个参数不是0，所以这里就不一个调一个了
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            //获取子视图
            childView = getChildAt(0);//ScrollView只有一个子视图
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (childView == null || !isFinishAnimation){
            return super.onTouchEvent(ev);
        }
        //获取当前的Y轴坐标
        int eventY = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastY = eventY;
                break;
            case MotionEvent.ACTION_MOVE:
                //微小的移动量
                int dy = eventY - lastY;
                if (isNeedMove()){
                    if (normal.isEmpty()) {
                        //记录了childView的临界状态的左、上、右、下
                        normal.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
                    }
                    //重新布局//只计算垂直方向，dy/2实现滑动很大距离只拉伸一点点
                    childView.layout(childView.getLeft(), childView.getTop()+dy/2, childView.getRight(), childView.getBottom()+dy/2);
                }
                //重新赋值
                lastY = eventY;
                break;
            case MotionEvent.ACTION_UP:
                if (isNeedAnimation()){
                    //使用平移动画
                    int translateY = childView.getBottom() - normal.bottom;
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -translateY);
                    translateAnimation.setDuration(200);
//                    translateAnimation.setFillAfter(true);//停留在最终位置上，但这样会导致点击事件错位，所以不采用
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isFinishAnimation = false;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isFinishAnimation = true;
                            //清除动画
                            childView.clearAnimation();
                            //重新布局
                            childView.layout(normal.left, normal.top, normal.right, normal.bottom);
                            //清除normal的数据
                            normal.setEmpty();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                    //启动动画
                    childView.startAnimation(translateAnimation);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    //判断是否需要执行平移动画
    private boolean isNeedAnimation() {
        return !normal.isEmpty();
    }

    private boolean isNeedMove() {
        //获取子视图的高度
        int childViewMeasuredHeight = childView.getMeasuredHeight();
        //获取布局的高度
        int scrollViewMeasuredHeight = this.getMeasuredHeight();

//        Log.e("TAG", "isNeedMove--------->childViewMeasuredHeight=" + childViewMeasuredHeight);
//        Log.e("TAG", "isNeedMove--------->scrollViewMeasuredHeight=" + scrollViewMeasuredHeight);

        //dy >= 0
        int dy = childViewMeasuredHeight - scrollViewMeasuredHeight;

        //获取用户在Y轴方向上的偏移量（上+ 下-）
        int scrollY = this.getScrollY();
        if (scrollY <= 0 || scrollY >= dy){
            //按照自定义的MyScrollView的方式处理
            return true;
        }
        //其他处在临界范围内的，返回false，表示仍按照ScrollView的方式处理
        return false;
    }
}
