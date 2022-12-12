package com.ecare.smartmeal.ui.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BaseFragmentPageAdapter;
import com.ecare.smartmeal.base.SimpleFragment;
import com.ecare.smartmeal.config.Constants;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/6.
 * Email: iminminxu@gmail.com
 */
public class OrderCategoryFragment extends SimpleFragment {

    @BindView(R.id.tl_order)
    TabLayout tlOrder;
    @BindView(R.id.vp_order)
    ViewPager vpOrder;

    /**
     * OrderCategoryFragment构建方法
     *
     * @param isNew 是否是新的订单列表
     * @param type  订单类型
     * @return OrderCategoryFragment
     */
    public static OrderCategoryFragment newInstance(boolean isNew, int type) {
        OrderCategoryFragment fragment = new OrderCategoryFragment();
        Bundle args = new Bundle();
        args.putBoolean(Constants.IT_IS_NEW, isNew);
        args.putInt(Constants.IT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_order_category;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initViews(@Nullable Bundle savedInstanceState) {
        //设置ViewPager
        String[] titles = null;
        List<Fragment> fragmentList = new ArrayList<>();
        boolean isNew = getArguments().getBoolean(Constants.IT_IS_NEW);
        int type = getArguments().getInt(Constants.IT_TYPE);
        if (isNew) {
            switch (type) {
                case OrderListNewFragment.SOURCE_CASHIER:
                    titles = new String[]{"已完成", "已退单"};
                    fragmentList.add(CashierOrderListFragment.newInstance(type, OrderListNewFragment.STATUS_COMPLETED));
                    fragmentList.add(CashierOrderListFragment.newInstance(type, OrderListNewFragment.STATUS_REFUNDED));
                    break;
                case OrderListNewFragment.SOURCE_DELIVERY:
                    titles = new String[]{"待接单", "待配送", "已完成"};
                    fragmentList.add(OrderListNewFragment.newInstance(type, OrderListNewFragment.STATUS_PENDING_ORDER));
                    fragmentList.add(OrderListNewFragment.newInstance(type, OrderListNewFragment.STATUS_PENDING_DELIVERY));
                    fragmentList.add(OrderListNewFragment.newInstance(type, OrderListNewFragment.STATUS_COMPLETED));
                    break;
                default:
                    break;
            }
        } else {
            switch (type) {
                case OrderListFragment.ORDER_WAY_DELIVERY:
                    titles = new String[]{"新订单", "待配送"};
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_PENDING_ORDER));
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_PENDING_DELIVERY));
                    break;
                case OrderListFragment.ORDER_WAY_SHOP:
                    titles = new String[]{"新订单", "待配货", "待取货"};
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_PENDING_ORDER));
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_TO_BE_SERVED));
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_MEAL_WAITING));
                    break;
                case OrderListFragment.ORDER_WAY_BOOKING:
                    titles = new String[]{"新预定", "今日待配货"};
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_PENDING_ORDER));
                    fragmentList.add(OrderListFragment.newInstance(type, OrderListFragment.STATUS_PENDING_DELIVERY));
                    break;
                default:
                    break;
            }
        }
        vpOrder.setAdapter(new BaseFragmentPageAdapter(getChildFragmentManager(), fragmentList, titles));
        tlOrder.setupWithViewPager(vpOrder);
    }

    @Override
    protected void doBusiness() {

    }

    /**
     * 特殊处理
     *
     * @return true
     */
    @Override
    protected boolean isParentVisible() {
        return true;
    }
}
