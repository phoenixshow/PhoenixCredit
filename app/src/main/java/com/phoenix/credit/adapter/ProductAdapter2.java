package com.phoenix.credit.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix.credit.R;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.ui.RoundProgress;
import com.phoenix.credit.utils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by flashing on 2017/5/6.
 */

public class ProductAdapter2 extends MyBaseAdapter2<Product> {
    public ProductAdapter2(List<Product> list) {
        super(list);
    }

    @Override
    protected View initView(Context context) {
        return View.inflate(context, R.layout.item_product_list, null);
    }

    @Override
    protected void setData(View convertView, Product product) {
        ((TextView)convertView.findViewById(R.id.p_name)).setText(product.name);
        //其它略
        Log.e("TAG", "setData--------->");
    }
}
