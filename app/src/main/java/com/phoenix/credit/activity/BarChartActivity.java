package com.phoenix.credit.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BarChartActivity extends BaseActivity {

    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.bar_chart)
    BarChart barChart;

    private Typeface mTf;

    @Override
    protected void initData() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        // apply styling
        barChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("三星note7爆炸门事件后，三星品牌度");
        barChart.setDescription(description);//设置当前折线图的描述
        barChart.setDrawGridBackground(false);//是否绘制网格背景
        barChart.setDrawBarShadow(false);//是否绘制柱状图的背景（把剩余部分用背景色填满）

        XAxis xAxis = barChart.getXAxis();//获取当前的X轴对象
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的显示位置
        xAxis.setTypeface(mTf);//设置X轴的字体
        xAxis.setDrawGridLines(false);//是否绘制X轴的网格线
        xAxis.setDrawAxisLine(true);//是否绘制X轴的轴线

        YAxis leftAxis = barChart.getAxisLeft();//获取左边的Y轴对象
        leftAxis.setTypeface(mTf);//设置左边Y轴的字体
        leftAxis.setLabelCount(5, false);//参数1：左边Y轴提供的区间的个数，参数2：是否均匀分布这几个区间，false：均匀，true：不均匀
        leftAxis.setSpaceTop(20f);//设置最大值距离顶部的距离
        leftAxis.setAxisMinimum(0f);//设置此轴的自定义最小值 // this replaces setStartAtZero(true)

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setEnabled(false);

        //提供柱状图的数据
        BarData barData = generateDataBar();
        //设置数值的字体
        barData.setValueTypeface(mTf);

        // set data
        barChart.setData(barData);
        barChart.setFitBars(true);

        // do not forget to refresh the chart
//        barChart.invalidate();
        barChart.animateY(700);//设置Y轴方向的动画
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("柱状图");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R2.id.iv_title_back)
    public void back(View view){
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bar_chart;
    }

    /**
     * generates a random ChartData object with just one DataSet
     * @return
     */
    private BarData generateDataBar() {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet ");

        d.setColors(ColorTemplate.VORDIPLOM_COLORS);//设置柱的颜色，是5个颜色的数组，也可以自定义
        d.setHighLightAlpha(255);//设置高亮的透明度

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);//相当于旧版的BarDataSet.setbarSpacePercent(40f);设置相邻的柱状图之间的距离
        return cd;
    }
}
