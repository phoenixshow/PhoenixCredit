package com.phoenix.credit.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.ui.randomLayout.StellarMap;
import com.phoenix.credit.utils.UIUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 如何在布局中添加子视图？
 * 1.直接在布局文件中，以标签的方式添加————静态
 * 2.在Java代码中，动态的添加子视图————动态
 *      -->addView(...)：一个一个的添加//ViewGroup实现了ViewManager接口，这是接口中的方法
 *      -->设置Adapter的方式，批量装配数据
 */

public class ProductRecommendFragment extends BaseFragment {
    @BindView(R2.id.sm_product_recommend)
    StellarMap smProductRecommend;

    //提供装配的数据
    private String[] datas = new String[]{"新手福利计划", "财神道90天计划", "硅谷钱包计划", "30天理财计划(加息2%)", "180天理财计划(加息5%)", "月月升理财计划(加息10%)",
            "中情局投资商业经营", "大学老师购买车辆", "屌丝下海经商计划", "美人鱼影视拍摄投资", "Android培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "摩托罗拉洗钱计划", "铁路局回款计划", "屌丝迎娶白富美计划"
    };
    //声明两个子数组
    private String[] oneDatas = new String[datas.length / 2];
    private String[] twoDatas = new String[datas.length - datas.length / 2];

    private Random random = new Random();

    @Override
    protected void initData(String content) {
        //初始化子数组的数据
        for (int i = 0; i < datas.length; i++) {
            if (i < datas.length / 2){
                oneDatas[i] = datas[i];
            }else {
                twoDatas[i - datas.length / 2] = datas[i];
            }
        }

        StellarAdapter adapter = new StellarAdapter();
        smProductRecommend.setAdapter(adapter);

        //设置StellarMap的内边距
        int leftPadding = UIUtils.dp2px(10);
        int topPadding = UIUtils.dp2px(10);
        int rightPadding = UIUtils.dp2px(10);
        int bottomPadding = UIUtils.dp2px(10);
        smProductRecommend.setInnerPadding(leftPadding, topPadding, rightPadding, bottomPadding);

        //必须调用如下的两个方法，否则StellarMap不能显示数据
        //设置显示的数据在X轴、Y轴方向上的稀疏度，值越大越稀疏
        smProductRecommend.setRegularity(5, 7);
        //设置初始化显示的组别，以及是否需要使用动画
        smProductRecommend.setGroup(0, true);
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_productrecommend;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    //提供Adapter的实现类
    class StellarAdapter implements StellarMap.Adapter{

        //获取组的个数
        @Override
        public int getGroupCount() {
            return 2;//一共16条数据，每页8条，共2组
        }

        //返回每组中显示的数据的个数
        @Override
        public int getCount(int group) {
            if (group == 0){
                return datas.length / 2;
            }else {
                return datas.length - datas.length / 2;//datas.length有可能是奇数
            }
        }

        //返回具体的View
        //position：不同的组别，position都是从0开始
        @Override
        public View getView(int group, int position, View convertView) {
            final TextView tv = new TextView(getActivity());
            //设置属性
            //设置文本的内容
            if (group == 0) {
                tv.setText(oneDatas[position]);
            }else {
                tv.setText(twoDatas[position]);
            }
            //设置字体的大小，如果希望字号整体调大改前面的值，如果要字号差别大些改后面的值
            tv.setTextSize(UIUtils.dp2px(4) + UIUtils.dp2px(random.nextInt(4)));
            //设置字体的颜色
            int red = random.nextInt(211);//00~ff//0~255
            int green = random.nextInt(211);
            int blue = random.nextInt(211);
            tv.setTextColor(Color.rgb(red, green, blue));

            //设置TextView的点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtils.toast(tv.getText().toString(), false);
                }
            });
            return tv;
        }

        //返回下一组显示平移动画的组别
        //查看源码发现，此方法从未被调用，所以可以不重写
        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        //返回下一组显示缩放动画的组别
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            if (group == 0){
                return 1;
            }else {
                return 0;
            }
        }
    }
}
