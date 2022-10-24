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
     * @param orderWay 订单方式
     * @return OrderCategoryFragment
     */
    public static OrderCategoryFragment newInstance(@OrderListFragment.OrderWay int orderWay) {
        OrderCategoryFragment fragment = new OrderCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.IT_ORDER_WAY, orderWay);
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
        int orderWay = getArguments().getInt(Constants.IT_ORDER_WAY);
        switch (orderWay) {
            case OrderListFragment.ORDER_WAY_DELIVERY:
                titles = new String[]{"新订单", "待配送"};
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_PENDING_ORDER));
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_PENDING_DELIVERY));
                break;
            case OrderListFragment.ORDER_WAY_SHOP:
                titles = new String[]{"新订单", "待配餐", "待取餐"};
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_PENDING_ORDER));
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_TO_BE_SERVED));
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_MEAL_WAITING));
                break;
            case OrderListFragment.ORDER_WAY_BOOKING:
                titles = new String[]{"新预定", "今日待配餐"};
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_PENDING_ORDER));
                fragmentList.add(OrderListFragment.newInstance(orderWay, OrderListFragment.STATUS_PENDING_DELIVERY));
                break;
            case OrderListFragment.ORDER_WAY_CASHIER:
                titles = new String[]{"已完成", "已退单"};
                fragmentList.add(CashierOrderListFragment.newInstance(CashierOrderListFragment.STATUS_COMPLETED));
                fragmentList.add(CashierOrderListFragment.newInstance(CashierOrderListFragment.STATUS_REFUNDED));
                break;
            default:
                break;
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
