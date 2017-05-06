package com.phoenix.credit.adapter;

import com.phoenix.credit.bean.Product;

import java.util.List;

/**
 * Created by flashing on 2017/5/6.
 */

public class ProductAdapter3 extends MyBaseAdapter3<Product> {
    public ProductAdapter3(List<Product> list) {
        super(list);
    }

    @Override
    protected BaseHolder<Product> getHolder() {
        return new MyHolder();
    }
}
