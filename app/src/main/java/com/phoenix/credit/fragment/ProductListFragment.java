package com.phoenix.credit.fragment;

import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.adapter.ProductAdapter;
import com.phoenix.credit.adapter.ProductAdapter1;
import com.phoenix.credit.adapter.ProductAdapter2;
import com.phoenix.credit.adapter.ProductAdapter3;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.common.AppNetConfig;
import com.phoenix.credit.common.BaseFragment;

import java.util.List;

import butterknife.BindView;

/**
 * ListView的使用：1.ListView，2.BaseAdapter，3.Item Layout，4.集合数据（联网获取）
 */

public class ProductListFragment extends BaseFragment {
    @BindView(R2.id.tv_product_title)
    TextView tvProductTitle;
    @BindView(R2.id.lv_product_list)
    ListView lvProductList;
    private List<Product> productList;

    @Override
    protected void initData(String content) {
//        //方式一：使得当前的TextView获取焦点
//        tvProductTitle.setFocusable(true);
//        tvProductTitle.setFocusableInTouchMode(true);
//        tvProductTitle.requestFocus();
        //方式二：提供TextView的子类，重写isFocused()方法，返回true即可

        JSONObject jsonObject = JSON.parseObject(content);
        boolean success = jsonObject.getBoolean("success");
        if (success){
            String data = jsonObject.getString("data");
            //获取集合数据
            productList = JSON.parseArray(data, Product.class);

//            //方式一：没有抽取
//            ProductAdapter productAdapter = new ProductAdapter(productList);
//            lvProductList.setAdapter(productAdapter);//显示列表
//            //方式二：抽取了，但是抽取力度小（相较方法三可以作为选择）
//            ProductAdapter1 productAdapter1 = new ProductAdapter1(productList);
//            lvProductList.setAdapter(productAdapter1);//显示列表
//            //方式三：抽取了，但是没有使用ViewHolder，getView()优化得不够
//            ProductAdapter2 productAdapter2 = new ProductAdapter2(productList);
//            lvProductList.setAdapter(productAdapter2);//显示列表
            //方式四、抽取了，最好的方式
            ProductAdapter3 productAdapter3 = new ProductAdapter3(productList);
            lvProductList.setAdapter(productAdapter3);//显示列表
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_productlist;
    }

    @Override
    protected String getUrl() {
        return AppNetConfig.PRODUCT;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }
}
