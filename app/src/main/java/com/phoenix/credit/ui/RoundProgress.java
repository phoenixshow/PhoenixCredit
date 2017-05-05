package com.phoenix.credit.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.phoenix.credit.utils.UIUtils;

/**
 * 自定义圆形进度条视图
 */
public class RoundProgress extends View {
    //设置绘制的圆环及文本的属性
    private int roundColor = Color.GRAY;//圆环的颜色
    private int roundProgressColor = Color.RED;//圆弧的颜色
    private int textColor = Color.BLUE;//文本的颜色

    private int roundWidth = UIUtils.dp2px(10);//圆环的宽度
    private int textSize = UIUtils.dp2px(20);//文本的字体大小

    private int max = 100;//圆环的最大值
    private int progress = 60;//圆环的进度

    private int width;//当前视图的宽度（=高度）

    private Paint paint;//画笔

    public RoundProgress(Context context) {
        this(context, null);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);//去除毛边
    }

    //测量：获取当前视图宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = this.getMeasuredWidth();
    }

    //Canvas：画布，对应着视图在布局中的范围区间。范围的左上顶点即为坐标原点
    @Override
    protected void onDraw(Canvas canvas) {
        //1.绘制圆环
        //获取圆心坐标
        int cx = width / 2;
        int cy = width / 2;
        int radius = width / 2 - roundWidth / 2;
        paint.setColor(roundColor);//设置画笔颜色
        paint.setStyle(Paint.Style.STROKE);//设置为空心（圆环）的样式
        paint.setStrokeWidth(roundWidth);//设置圆环的宽度
        canvas.drawCircle(cx, cy, radius, paint);
        
        //2.绘制圆弧
        RectF rectF = new RectF(roundWidth/2, roundWidth/2, width-roundWidth/2, width-roundWidth/2);
        paint.setColor(roundProgressColor);//设置画笔颜色
        canvas.drawArc(rectF, 0, progress*360/max, false, paint);//true是封闭的扇形，false是圆弧

        //3.绘制文本
        String text = progress * 100 / max + "%";
        //设置paint
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(0);//设置过圆环的宽度，需要再改回来
        Rect rect = new Rect();//创建了一个矩形，此时矩形没有具体的宽度和高度
        paint.getTextBounds(text, 0, text.length(), rect);//此时的矩形的宽度和高度即为正好包裹文本的矩形的宽高
        //获取左下顶点的坐标
        int x = width / 2 - rect.width() / 2;
        int y = width / 2 + rect.height() / 2;
        canvas.drawText(text, x, y, paint);
    }
}
