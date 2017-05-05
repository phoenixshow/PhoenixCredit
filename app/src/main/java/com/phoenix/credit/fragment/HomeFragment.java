package com.phoenix.credit.fragment;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.bean.Image;
import com.phoenix.credit.bean.Index;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.ui.RoundProgress;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by flashing on 2017/4/28.
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_home_product)
    TextView tvHomeProduct;
    @BindView(R.id.rp_home)
    RoundProgress rpHome;
    @BindView(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;

    private Index index;
    private int currentProress;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            rpHome.setMax(100);
            for (int i = 0; i < currentProress; i++) {
                rpHome.setProgress(i + 1);
                SystemClock.sleep(20);//延时，别太快了
                //强制重绘
//                rpHome.invalidate();//只有主线程才可以如此调用
                rpHome.postInvalidate();//主线程、分线程都可以如此调用
            }
        }
    };

    @Override
    protected void initData(String content) {
        index = new Index();
        //解析Json数据
        JSONObject jsonObject = JSON.parseObject(content);
        //解析Json对象
        String proInfo = jsonObject.getString("proInfo");
        Product product = JSON.parseObject(proInfo, Product.class);
        //解析Json数组
        String imageArr = jsonObject.getString("imageArr");
        List<Image> images = jsonObject.parseArray(imageArr, Image.class);
        index.product = product;
        index.images = images;

        //更新页面数据
        tvHomeProduct.setText(product.name);
        tvHomeYearrate.setText(product.yearRate + "%");
        //获取数据中的进度值
        currentProress = Integer.parseInt(index.product.progress);
        //在分线程中，实现进度的动态变化
        new Thread(runnable).start();

        //加载显示图片
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//显示圆形指示器和标题
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片地址构成的集合
        ArrayList<String> imagesUrl = new ArrayList<>(index.images.size());
        ArrayList<String> imagesTitle = new ArrayList<>(index.images.size());
        for (int i = 0; i < index.images.size(); i++) {
            imagesUrl.add(AppNetConfig.BASE_URL + index.images.get(i).IMAURL);
            imagesTitle.add(index.images.get(i).IMATITLE);
        }
        banner.setImages(imagesUrl);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(imagesTitle);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.home);
        ivTitleSetting.setVisibility(View.GONE);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.INDEX;
    }

    @Override
    protected RequestParams getParams() {
//        return new RequestParams();//皆可
        return null;
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String) path).into(imageView);
        }
    }
}
