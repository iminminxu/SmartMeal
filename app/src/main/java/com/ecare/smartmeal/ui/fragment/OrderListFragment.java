package com.ecare.smartmeal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.OrderListContract;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.presenter.OrderListPresenter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import butterknife.BindView;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/6.
 * Email: iminminxu@gmail.com
 */
public class OrderListFragment extends RootFragment<OrderListContract.Presenter> implements OrderListContract.View {

    //订单方式
    public static final int ORDER_WAY_DELIVERY = 1; //外送点餐
    public static final int ORDER_WAY_SHOP = 2;     //到店取餐
    public static final int ORDER_WAY_BOOKING = 3;  //长期预定
    //订单状态
    public static final int STATUS_PENDING_ORDER = 1;    //待接单
    public static final int STATUS_PENDING_DELIVERY = 2; //待配送
    public static final int STATUS_MEAL_WAITING = 3;     //待取餐
    public static final int STATUS_TO_BE_SERVED = 4;     //待配餐

    @IntDef({ORDER_WAY_DELIVERY, ORDER_WAY_SHOP, ORDER_WAY_BOOKING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrderWay {

    }

    @IntDef({STATUS_PENDING_ORDER, STATUS_PENDING_DELIVERY, STATUS_MEAL_WAITING, STATUS_TO_BE_SERVED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {

    }

    /**
     * OrderListFragment构建方法
     *
     * @param orderWay 订单方式
     * @param status   订单状态
     * @return OrderListFragment
     */
    public static OrderListFragment newInstance(@OrderWay int orderWay, @Status int status) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.IT_ORDER_WAY, orderWay);
        args.putInt(Constants.IT_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    //加载组件
    @BindView(R.id.view_main)
    SmartRefreshLayout srlRefresh;
    //订单列表
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    //订单方式
    private int mOrderWay;
    //订单状态
    private int mStatus;

    @Override
    protected OrderListContract.Presenter createPresenter() {
        return new OrderListPresenter(mOrderWay, mStatus);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_paging;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void initVariables() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        mOrderWay = arguments.getInt(Constants.IT_ORDER_WAY);
        mStatus = arguments.getInt(Constants.IT_STATUS);
    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //初始化SmartRefreshLayout
        initSmartRefreshLayout();
        //初始化订单列表
        initRecyclerView();
    }

    /**
     * 初始化SmartRefreshLayout
     */
    private void initSmartRefreshLayout() {
        srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPresenter.loadMoreData();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.refreshData();
            }
        });
    }

    /**
     * 初始化订单列表
     */
    private void initRecyclerView() {
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        BaseQuickAdapter adapter = mPresenter.getAdapter();
        rvList.setAdapter(adapter);
        adapter.setEmptyView(R.layout.view_empty);
    }

    @Override
    protected void doBusiness() {
        mPresenter.firstLoadData();
    }

    @Override
    protected void firstLoadDataFailRetry() {
        mPresenter.firstLoadData();
    }

    @Override
    public void setNoMoreData() {
        srlRefresh.setNoMoreData(true);
    }

    @Override
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    @Override
    public void autoRefresh() {
        srlRefresh.autoRefresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.OrderListRefreshEvent event) {
        if (event.getOrderWay() == mOrderWay) {
            autoRefresh();
        }
    }
}
