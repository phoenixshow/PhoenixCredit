package com.phoenix.credit.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.phoenix.credit.R;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.ui.RoundProgress;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by flashing on 2017/5/6.
 */

public class ProductAdapter1 extends MyBaseAdapter1<Product> {
    public ProductAdapter1(List<Product> list) {
        super(list);
    }

    @Override
    protected View myGetView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), R.layout.item_product_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //装配数据
        Product product = list.get(position);
        holder.pMinnum.setText(product.memberNum);
        holder.pMinzouzi.setText(product.minTouMoney);
        holder.pMoney.setText(product.money);
        holder.pName.setText(product.name);
        holder.pProgresss.setProgress(Integer.parseInt(product.progress));
        holder.pSuodingdays.setText(product.suodingDays);
        holder.pYearlv.setText(product.yearRate);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.p_name)
        TextView pName;
        @BindView(R.id.p_money)
        TextView pMoney;
        @BindView(R.id.p_yearlv)
        TextView pYearlv;
        @BindView(R.id.p_suodingdays)
        TextView pSuodingdays;
        @BindView(R.id.p_minzouzi)
        TextView pMinzouzi;
        @BindView(R.id.p_minnum)
        TextView pMinnum;
        @BindView(R.id.p_progresss)
        RoundProgress pProgresss;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
