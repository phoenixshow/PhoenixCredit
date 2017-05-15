package com.phoenix.credit.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LineChartActivity extends BaseActivity {

    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.line_chart)
    LineChart lineChart;

    //声明字体库
    private Typeface mTf;

    @Override
    protected void initData() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // apply styling
//        lineChart.getDescription().setEnabled(false);
        lineChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("林丹出轨事件的关注度");
        lineChart.setDescription(description);//设置当前折线图的描述
        lineChart.setDrawGridBackground(false);//是否绘制网格背景

        //获取当前的X轴对象
        XAxis xAxis = lineChart.getXAxis();
        //设置X轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        //设置X轴的字体
        xAxis.setTypeface(mTf);
        //是否绘制X轴的网格线(穿过奇数节点的纵向线)
        xAxis.setDrawGridLines(false);
        //是否绘制X轴的轴线（每个区间节点的横向线）
        xAxis.setDrawAxisLine(true);

        //获取左边的Y轴对象
        YAxis leftAxis = lineChart.getAxisLeft();
        //设置左边Y轴的字体
        leftAxis.setTypeface(mTf);
        //参数1：左边Y轴提供的区间的个数，参数2：是否均匀分布这几个区间，false：均匀，true：不均匀
//        leftAxis.setLabelCount(35, true);
        leftAxis.setLabelCount(5, false);
        //设置此轴的自定义最小值
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //获取右边的Y轴对象
        YAxis rightAxis = lineChart.getAxisRight();
        //将右边的Y轴设置为不显示的
//        rightAxis.setEnabled(false);
        rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        //提供折线数据，调用generateDataLine方法随机生成的数据，通常情况下，折线的数据来自于服务器的数据
        LineData lineData = generateDataLine(1);
        lineChart.setData(lineData);

        // 设置X轴方向的动画，执行时间是750
        // lineChart.invalidate();//注意：如果调用动画方法后，就没有必要调用invalidate（）方法，来刷新界面了
        lineChart.animateX(750);
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("折线图");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R2.id.iv_title_back)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_line_chart;
    }
    /**
     * generates a random ChartData object with just one DataSet
     * 参数用于每条折线代表内容的显示，可动态传入，如果不需要也可以去掉参数
     * @return
     */
    private LineData generateDataLine(int cnt) {
        //构造折线一
        //Entry是每个节点
        ArrayList<Entry> e1 = new ArrayList<Entry>();

        //提供折线中点的数据，循环得到一组12个随机数
        for (int i = 0; i < 12; i++) {
            e1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        LineDataSet d1 = new LineDataSet(e1, "New DataSet " + cnt + ", (1)");
        d1.setLineWidth(2.5f);//设置折线的宽度
        d1.setCircleRadius(4.5f);//设置节点半径
        d1.setHighLightColor(Color.rgb(244, 0, 0));//选中某节点时，十字高亮线的颜色
        d1.setDrawValues(false);//是否显示节点的数值

        //构造折线二
        ArrayList<Entry> e2 = new ArrayList<Entry>();

        for (int i = 0; i < 12; i++) {
            e2.add(new Entry(i, e1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(e2, "New DataSet " + cnt + ", (2)");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setCircleColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        d2.setDrawValues(false);

        //将构造的所有折线放入集合
        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        sets.add(d1);
        sets.add(d2);

        LineData cd = new LineData(sets);
        return cd;
    }
}
