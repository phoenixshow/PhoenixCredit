package com.phoenix.credit.fragment;

import android.view.ViewGroup;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.phoenix.credit.R;
import com.phoenix.credit.common.BaseFragment;
import com.phoenix.credit.ui.FlowLayout;
import com.phoenix.credit.utils.UIUtils;

import butterknife.BindView;

import static com.phoenix.credit.utils.UIUtils.dp2px;

/**
 * Created by flashing on 2017/5/6.
 */

public class ProductHotFragment extends BaseFragment {
    @BindView(R.id.fl_hot)
    FlowLayout flHot;

    private String[] datas = new String[]{"新手福利计划", "财神道90天计划", "硅谷计划", "30天理财计划", "180天理财计划", "月月升","中情局投资商业经营", "大学老师购买车辆", "屌丝下海经商计划", "美人鱼影视拍摄投资", "Android培训老师自己周转", "养猪场扩大经营",
            "旅游公司扩大规模", "摩托罗拉洗钱计划", "铁路局回款计划", "屌丝迎娶白富美计划"
    };

    @Override
    protected void initData(String content) {
        for (int i = 0; i < datas.length; i++) {
            TextView tv = new TextView(getContext());
            //设置属性
            tv.setText(datas[i]);
            ViewGroup.MarginLayoutParams mp = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mp.leftMargin = dp2px(10);
            mp.rightMargin = dp2px(10);
            mp.topMargin = dp2px(10);
            mp.bottomMargin = dp2px(10);
            tv.setLayoutParams(mp);//设置边距

            int padding = UIUtils.dp2px(5);
            tv.setPadding(padding, padding, padding, padding);//设置内边距

//            tv.setTextSize(UIUtils.dp2px(10));
            tv.setTextSize(14);
            flHot.addView(tv);
        }
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_producthot;
    }

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }
}
