package com.ecare.smartmeal.ui.fragment;

import android.graphics.Outline;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseFragmentPageAdapter;
import com.ecare.smartmeal.base.SimpleFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/13.
 * Email: iminminxu@gmail.com
 */
public class HomePendingFragment extends SimpleFragment {

    @BindView(R.id.ll_pending)
    LinearLayout llPending;
    @BindView(R.id.tl_pending)
    TabLayout tlPending;
    @BindView(R.id.vp_pending)
    ViewPager vpPending;

    @Override
    protected int getLayoutId() {
        return R.layout.frag_pending;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        llPending.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), getResources().getDimension(R.dimen.dp_3));
            }
        });
        llPending.setClipToOutline(true);
        //设置ViewPager
        String[] titles = {"收银台", "外送订单", "到店取餐", "长期预定"};
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(OrderCategoryFragment.newInstance(true, OrderListNewFragment.SOURCE_CASHIER));
        fragmentList.add(OrderCategoryFragment.newInstance(true, OrderListNewFragment.SOURCE_DELIVERY));
        fragmentList.add(OrderCategoryFragment.newInstance(false, OrderListFragment.ORDER_WAY_SHOP));
        fragmentList.add(OrderCategoryFragment.newInstance(false, OrderListFragment.ORDER_WAY_BOOKING));
        vpPending.setAdapter(new BaseFragmentPageAdapter(getChildFragmentManager(), fragmentList, titles));
        tlPending.setupWithViewPager(vpPending);
    }

    @Override
    protected void doBusiness() {

    }
}
