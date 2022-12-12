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
import com.ecare.smartmeal.base.BasePagingPresenter;
import com.ecare.smartmeal.base.BasePagingView;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.presenter.OrderListNewPresenter;
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
public class OrderListNewFragment extends RootFragment<BasePagingPresenter> implements BasePagingView {

    //订单渠道
    public static final int SOURCE_CASHIER = 1;           //收银台
    public static final int SOURCE_DELIVERY = 2;          //外送订单
    //订单状态
    public static final int STATUS_COMPLETED = 1;         //已完成
    public static final int STATUS_REFUNDED = 7;          //已退单
    public static final int STATUS_PENDING_ORDER = 20;    //待接单
    public static final int STATUS_PENDING_DELIVERY = 21; //待配送

    @IntDef({SOURCE_CASHIER, SOURCE_DELIVERY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {

    }

    @IntDef({STATUS_COMPLETED, STATUS_REFUNDED, STATUS_PENDING_ORDER, STATUS_PENDING_DELIVERY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {

    }

    /**
     * OrderListNewFragment构建方法
     *
     * @param source 订单渠道
     * @param status 订单状态
     * @return OrderListNewFragment
     */
    public static OrderListNewFragment newInstance(@Source int source, @Status int status) {
        OrderListNewFragment fragment = new OrderListNewFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.IT_SOURCE, source);
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
    //页面传递参数
    private int mSource;
    private int mStatus;

    @Override
    protected BasePagingPresenter createPresenter() {
        return new OrderListNewPresenter(mSource, mStatus);
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
        mSource = arguments.getInt(Constants.IT_SOURCE);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.OrderListNewRefreshEvent event) {
        if (event.getSource() == mSource) {
            srlRefresh.autoRefresh();
        }
    }
}
