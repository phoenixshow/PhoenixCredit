package com.phoenix.credit.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.loopj.android.http.JsonHttpResponseHandler;
import com.phoenix.credit.R;
import com.phoenix.credit.bean.Image;
import com.phoenix.credit.bean.Index;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.utils.UIUtils;

import org.json.JSONArray;

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
    @BindView(R.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_home_product)
    TextView tvHomeProduct;
    @BindView(R.id.tv_home_yearrate)
    TextView tvHomeYearrate;
    @BindView(R.id.vp_home)
    ViewPager vpHome;
    Unbinder unbinder;

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
        final Index index = new Index();
        AsyncHttpClient client = new AsyncHttpClient();
        final String url = AppNetConfig.INDEX;
        Log.e("TAG", "initData--------->url:" + url);

        client.post(url, new AsyncHttpResponseHandler() {
            //200：响应成功
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody,"utf-8");
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
}
