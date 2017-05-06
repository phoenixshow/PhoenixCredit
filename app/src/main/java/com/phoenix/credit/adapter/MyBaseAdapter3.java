package com.phoenix.credit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by flashing on 2017/5/6.
 */

public abstract class MyBaseAdapter3<T> extends BaseAdapter {
    protected List<T> list;

    //通过构造器初始化集合数据
    public MyBaseAdapter3(List<T> list) {
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

    /**
     * 问题一：Item Layout的布局是不确定的
     * 问题二：将集合中指定位置的数据装配到Item是不确定的
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<T> holder;
        if (convertView == null){
            holder = getHolder();
        }else {
            holder = (BaseHolder<T>) convertView.getTag();
        }

        //装配数据
        T t = list.get(position);
        holder.setData(t);

        return holder.getRootView();
    }

    protected abstract BaseHolder<T> getHolder();
}
