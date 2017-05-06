package com.phoenix.credit.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import static android.R.id.list;

/**
 * Created by flashing on 2017/5/6.
 */

public abstract class MyBaseAdapter1<T> extends BaseAdapter {
    protected List<T> list;

    //通过构造器初始化集合数据
    public MyBaseAdapter1(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = myGetView(position, convertView, parent);
        return view;
    }

    protected abstract View myGetView(int position, View convertView, ViewGroup parent);
}
