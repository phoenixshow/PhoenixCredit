package com.phoenix.credit.adapter;

import android.view.View;
import android.widget.TextView;

import com.phoenix.credit.R;
import com.phoenix.credit.bean.Product;
import com.phoenix.credit.ui.RoundProgress;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;

/**
 * Created by flashing on 2017/5/6.
 */

public class MyHolder extends BaseHolder<Product> {
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

    @Override
    protected View initView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_product_list, null);
    }

    @Override
    protected void refreshData() {
        Product data = this.getData();
        //装数据
        pMinnum.setText(data.memberNum);
        pMinzouzi.setText(data.minTouMoney);
        pMoney.setText(data.money);
        pName.setText(data.name);
        pProgresss.setProgress(Integer.parseInt(data.progress));
        pSuodingdays.setText(data.suodingDays);
        pYearlv.setText(data.yearRate);
    }
}
