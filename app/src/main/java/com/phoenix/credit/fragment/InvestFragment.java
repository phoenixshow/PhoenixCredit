package com.phoenix.credit.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.R2;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.utils.UIUtils;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by flashing on 2017/4/28.
 */

public class InvestFragment extends BaseFragment {
    @BindView(R2.id.iv_title_back)
    ImageView ivTitleBack;
    @BindView(R2.id.tv_title)
    TextView tvTitle;
    @BindView(R2.id.iv_title_setting)
    ImageView ivTitleSetting;
    @BindView(R2.id.tpi_invest)
    TabPageIndicator tpiInvest;
    @BindView(R2.id.vp_invest)
    ViewPager vpInvest;

    private List<Fragment> fragmentList = new ArrayList<>(3);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invest;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {
        //1.加载三个不同的Fragment
        initFragments();
        //2.ViewPager设置三个Fragment的显示
        MyAdapter adapter = new MyAdapter(getFragmentManager());
        vpInvest.setAdapter(adapter);
        //3.将TabPagerIndicator与View关联
        tpiInvest.setViewPager(vpInvest);
    }

    private void initFragments() {
        ProductListFragment productListFragment = new ProductListFragment();
        ProductRecommendFragment productRecommendFragment = new ProductRecommendFragment();
        ProductHotFragment productHotFragment = new ProductHotFragment();
        //添加到集合中
        fragmentList.add(productListFragment);
        fragmentList.add(productRecommendFragment);
        fragmentList.add(productHotFragment);
    }

    @Override
    protected void initTitle() {
        ivTitleBack.setVisibility(View.GONE);
        tvTitle.setText(R.string.invest);
        ivTitleSetting.setVisibility(View.GONE);
    }

    /**
     * 提供PagerAdapter的实现
     * 如果ViewPager中加载的是Fragment，则提供的Adapter可以继承于具体的FragmentStatePagerAdapter或FragmentPagerAdapter
     * FragmentStatePagerAdapter：适用于ViewPager中加载的Fragment过多，会根据最近最少使用算法，实现内存中Fragment的清理，避免内存溢出
     * FragmentPagerAdapter：适用于ViewPager中加载的Fragment不多时，系统不会清理已经加载的Fragment
     */
    class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 :fragmentList.size();
        }

        //提供TabPageIndicator的显示内容
        @Override
        public CharSequence getPageTitle(int position) {
//            //方式一
//            if (position == 0){
//                return "全部理财";
//            }else if (position == 1){
//                return "推荐理财";
//            }else if (position == 2){
//                return "热门理财";
//            }
            //方式二
            return UIUtils.getStringArr(R.array.invest_tab)[position];
        }
    }
}
