package com.phoenix.credit.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PieChartActivity extends BaseActivity {

    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R.id.pie_chart)
    PieChart pieChart;

    private Typeface mTf;
    private SpannableString mCenterText;

    @Override
    protected void initData() {
        mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mCenterText = generateCenterText();

        // apply styling
        pieChart.getDescription().setEnabled(true);
        Description description = new Description();
        description.setText("Android厂商2017年手机占有率");
        pieChart.setDescription(description);
        pieChart.setHoleRadius(52f);//最内层的圆的半径
        pieChart.setTransparentCircleRadius(57f);//包裹内层圆的半径（半透明57-52宽度的边缘）
        pieChart.setCenterText(mCenterText);//设置中间的文字
        pieChart.setCenterTextTypeface(mTf);//设置中间文字的字体
        pieChart.setCenterTextSize(9f);//设置中间文字的字号
        pieChart.setUsePercentValues(true);//是否使用总和百分比，true：各部分的百分比的和是100%，false则不是
        pieChart.setExtraOffsets(5, 10, 50, 10);//外边距

        PieData pieData = generateDataPie();
        pieData.setValueFormatter(new PercentFormatter());//使用百分数格式化显示
        pieData.setValueTypeface(mTf);//扇瓣的字体
        pieData.setValueTextSize(11f);//扇瓣的字号
        pieData.setValueTextColor(Color.WHITE);//扇瓣的颜色
        // set data
        pieChart.setData(pieData);

        //获取右上角的描述结构的对象
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//上对齐
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);//右对齐
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);//设置图例是否会在图表或外部绘制
        l.setYEntrySpace(0f);//相邻的Entry在Y轴上的间距
        l.setYOffset(0f);//第一个Entry距离最顶端的间距

        // do not forget to refresh the chart
        // pieChart.invalidate();
        pieChart.animateY(900);
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.VISIBLE);
        tvTitle.setText("饼状图");
        ivTitleSetting.setVisibility(View.INVISIBLE);
    }

    @OnClick(R2.id.iv_title_back)
    public void back(View view) {
        removeCurrentActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pie_chart;
    }

    /**
     * generates a random ChartData object with just one DataSet
     * @return
     */
    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        //生成4个随机大小的扇瓣
        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));//每个扇瓣的占比及文字
        }

        PieDataSet d = new PieDataSet(entries, "Android厂商占比");//右上角的描述结构下方的描述文字

        // space between slices
        d.setSliceSpace(2f);//扇瓣间隙
        d.setColors(ColorTemplate.VORDIPLOM_COLORS);//各扇瓣的颜色数组

        PieData cd = new PieData(d);
        return cd;
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Android 2017年\n各大手机厂商\n市场份额占比图");//\n算一个位置，汉字算一个位置
        //分别设置三行文字的大小及颜色
        s.setSpan(new RelativeSizeSpan(1.6f), 0, 14, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.VORDIPLOM_COLORS[0]), 0, 14, 0);
        s.setSpan(new RelativeSizeSpan(.9f), 14, 20, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, 20, 0);
        s.setSpan(new RelativeSizeSpan(1.4f), 20, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 20, s.length(), 0);
        return s;
    }
}
