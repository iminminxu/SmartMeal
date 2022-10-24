package com.ecare.smartmeal.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.CashierOrderListContract;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.model.bean.rsp.OrderCountResponse;
import com.ecare.smartmeal.presenter.CashierOrderListPresenter;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.ecare.smartmeal.utils.TextSpanBuilder;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

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
public class CashierOrderListFragment extends RootFragment<CashierOrderListContract.Presenter> implements CashierOrderListContract.View {

    //订单状态
    public static final int STATUS_COMPLETED = 1; //已完成
    public static final int STATUS_REFUNDED = 7;  //已退单

    @IntDef({STATUS_COMPLETED, STATUS_REFUNDED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {

    }

    /**
     * CashierOrderListFragment构建方法
     *
     * @param status 订单状态
     * @return CashierOrderListFragment
     */
    public static CashierOrderListFragment newInstance(@Status int status) {
        CashierOrderListFragment fragment = new CashierOrderListFragment();
        Bundle args = new Bundle();
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
    //订单状态
    private int mStatus;
    //头布局
    private TextView tvTurnover;
    private TextView tvNum;
    //头布局数据
    private OrderCountResponse mOrderCountResponse;

    @Override
    protected CashierOrderListContract.Presenter createPresenter() {
        return new CashierOrderListPresenter(mStatus);
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
                if (mStatus == STATUS_COMPLETED) {
                    mPresenter.getOrderCount();
                }
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
        //设置头布局
        if (mStatus == STATUS_COMPLETED) {
            View headView = LayoutInflater.from(mContext).inflate(R.layout.head_order, rvList, false);
            tvTurnover = headView.findViewById(R.id.tv_turnover);
            tvNum = headView.findViewById(R.id.tv_num);
            headView.findViewById(R.id.tv_print).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int status = SunmiPrinterApi.getInstance().getPrinterStatus();
                        if (!SunmiPrinterApi.getInstance().isConnected() || status != com.sunmi.externalprinterlibrary.api.Status.RUNNING) {
                            showMsg("未连接打印机，请确认打印机是否连接！");
                        } else {
                            showMsg("请取走小票凭证！");
                            PrintUtils.printDailyOrderStatistics(mOrderCountResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            adapter.addHeaderView(headView);
            adapter.setHeaderWithEmptyEnable(true);
        }
    }

    @Override
    protected void doBusiness() {
        mPresenter.firstLoadData();
        if (mStatus == STATUS_COMPLETED) {
            mPresenter.getOrderCount();
        }
    }

    @Override
    protected void firstLoadDataFailRetry() {
        mPresenter.firstLoadData();
        if (mStatus == STATUS_COMPLETED) {
            mPresenter.getOrderCount();
        }
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

    @Override
    public void setOrderCount(OrderCountResponse data) {
        if (data == null) {
            return;
        }
        mOrderCountResponse = data;
        tvTurnover.setText(TextSpanBuilder.create("今日总营业额：")
                .append(NumUtils.parsePrintAmount(data.getTotalOrderPrice()) + "元").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_18)).foregroundColor(mContext.getResources().getColor(R.color.color_ec7220))
                .build());
        tvNum.setText(TextSpanBuilder.create("今日交易数：")
                .append(data.getTotalOrderNum() + "单").sizeInPx((int) mContext.getResources().getDimension(R.dimen.font_18)).foregroundColor(mContext.getResources().getColor(R.color.color_ec7220))
                .build());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.OrderListRefreshEvent event) {
        if (event.getOrderWay() == OrderListFragment.ORDER_WAY_CASHIER) {
            srlRefresh.autoRefresh();
        }
    }
}
