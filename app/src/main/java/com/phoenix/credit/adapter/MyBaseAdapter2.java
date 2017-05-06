package com.phoenix.credit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by flashing on 2017/5/6.
 */

public abstract class MyBaseAdapter2<T> extends BaseAdapter {
    protected List<T> list;

    //通过构造器初始化集合数据
    public MyBaseAdapter2(List<T> list) {
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
        ViewHolder holder;
        if (convertView == null){
            convertView = initView(parent.getContext());
            holder = new ViewHolder(convertView);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        
        //获取集合数据
        T t = list.get(position);
        
        //装配数据
        setData(convertView, t);

        return convertView;
    }

    private class ViewHolder {
        public ViewHolder(View view) {
            view.setTag(this);
        }
    }

    //提供具体的Item Layout的布局
    protected abstract View initView(Context context);
    //装配数据的操作
    protected abstract void setData(View convertView, T t);
}