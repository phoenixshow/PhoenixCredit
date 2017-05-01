package com.phoenix.credit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.bean.Image;
import com.phoenix.credit.bean.Index;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

import java.io.UnsupportedEncodingException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

/**
 * Created by flashing on 2017/4/28.
 */

public class HomeFragment extends Fragment {
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.tv_home_product)
    TextView tvHomeProduct;
    @BindView(R2.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    @BindView(R2.id.vp_home)
    ViewPager vpHome;
    @BindView(R2.id.cpi_home_indicator)
    CirclePageIndicator cpiHomeIndicator;
    Unbinder unbinder;
    private Index index;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = View.inflate(getActivity(), R.layout.fragment_home, null);
        View view = UIUtils.getView(R.layout.fragment_home);
        unbinder = ButterKnife.bind(this, view);

        //初始化Title
        initTitle();

        //初始化数据
        initData();
        return view;
    }

    private void initData() {
        index = new Index();
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = AppNetConfig.INDEX;
        Log.e("TAG", "initData--------->url:" + url);

        client.post(url, new AsyncHttpResponseHandler() {
            //200：响应成功
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody, "utf-8");
                    //解析Json数据
                    JSONObject jsonObject = JSON.parseObject(result);
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

                    //设置ViewPager
                    vpHome.setAdapter(new MyAdapter());
                    //设置小圆圈的显示
                    cpiHomeIndicator.setViewPager(vpHome);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            //响应失败
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(UIUtils.getContext(), "联网获取数据失败", Toast.LENGTH_LONG).show();
                Log.e("TAG", "onFailure--------->" + error);
            }
        });
    }

    private void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.home);
        ivTitleSetting.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            List<Image> images = index.images;
            return images == null ? 0 : images.size();
        }

        //界面中显示的视图View是否是当前Object创建的
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //实例化
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getActivity());
            //1.ImageView显示图片
            String imgurl = index.images.get(position).IMAURL;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.e("TAG", "instantiateItem--------->" + AppNetConfig.BASE_URL + imgurl);
            Picasso.with(getActivity()).load(AppNetConfig.BASE_URL + imgurl).into(imageView);//使用Picasso联网下载并缓存图片
            //2.ImageView添加到容器中
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//移除操作
        }
    }
}
