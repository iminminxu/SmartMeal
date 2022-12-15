package com.ecare.smartmeal.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.contract.ComboDineContract;
import com.ecare.smartmeal.model.bean.rsp.CustomerComboResponse;
import com.ecare.smartmeal.model.bean.rsp.CustomerCombox;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.presenter.ComboDinePresenter;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/11/15.
 * Email: iminminxu@gmail.com
 */
public class ComboDineFragment extends RootFragment<ComboDineContract.Presenter> implements ComboDineContract.View {

    //加载组件
    @BindView(R.id.view_main)
    SmartRefreshLayout srlRefresh;
    //未就餐
    @BindView(R.id.tv_unfinished_title)
    TextView tvUnfinishedTitle;
    @BindView(R.id.rv_unfinished)
    RecyclerView rvUnfinished;
    private BaseQuickAdapter<CustomerCombox, BaseViewHolder> mUnfinishedAdapter;
    //已就餐
    @BindView(R.id.tv_finished_title)
    TextView tvFinishedTitle;
    @BindView(R.id.rv_finished)
    RecyclerView rvFinished;
    private BaseQuickAdapter<CustomerCombox, BaseViewHolder> mFinishedAdapter;
    //已请假
    @BindView(R.id.tv_cancel_title)
    TextView tvCancelTitle;
    @BindView(R.id.rv_cancel)
    RecyclerView rvCancel;
    private BaseQuickAdapter<CustomerCombox, BaseViewHolder> mCancelAdapter;

    @Override
    protected ComboDineContract.Presenter createPresenter() {
        return new ComboDinePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_combo_dine;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //设置加载布局
        View loading = mRootView.findViewById(R.id.view_loading);
        loading.setBackgroundResource(R.drawable.corners_3_solid_white);
        View error = mRootView.findViewById(R.id.view_error);
        error.setBackgroundResource(R.drawable.corners_3_solid_white);
        //初始化SmartRefreshLayout
        initSmartRefreshLayout();
        //初始化未就餐列表
        initUnfinishedRecyclerView();
        //初始化已就餐列表
        initFinishedRecyclerView();
        //初始化已请假列表
        initCancelRecyclerView();
    }

    /**
     * 初始化SmartRefreshLayout
     */
    private void initSmartRefreshLayout() {
        srlRefresh.setEnableLoadMore(false);
        srlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getComboDineList(CommonSubscriber.SHOW_REFRESH_LAYOUT);
            }
        });
    }

    /**
     * 初始化未就餐列表
     */
    private void initUnfinishedRecyclerView() {
        rvUnfinished.setLayoutManager(new LinearLayoutManager(mContext));
        mUnfinishedAdapter = new BaseQuickAdapter<CustomerCombox, BaseViewHolder>(R.layout.item_combo_dine_record) {
            @Override
            protected void convert(BaseViewHolder holder, CustomerCombox item) {
                if (item == null) {
                    return;
                }
                holder.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_mobile, item.getMobile())
                        .setText(R.id.tv_type, item.getTypeName())
                        .setText(R.id.tv_num, String.valueOf(item.getRemainTimes()))
                        .setTextColor(R.id.tv_num, getResources().getColor(item.getRemainTimes() < 7 ? R.color.color_f71413 : R.color.color_666666));
            }
        };
        rvUnfinished.setAdapter(mUnfinishedAdapter);
        mUnfinishedAdapter.setEmptyView(R.layout.view_empty);
    }

    /**
     * 初始化已就餐列表
     */
    private void initFinishedRecyclerView() {
        rvFinished.setLayoutManager(new LinearLayoutManager(mContext));
        mFinishedAdapter = new BaseQuickAdapter<CustomerCombox, BaseViewHolder>(R.layout.item_combo_record) {
            @Override
            protected void convert(BaseViewHolder holder, CustomerCombox item) {
                if (item == null) {
                    return;
                }
                holder.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_type, item.getTypeName());
            }
        };
        rvFinished.setAdapter(mFinishedAdapter);
        mFinishedAdapter.setEmptyView(R.layout.view_empty);
    }

    /**
     * 初始化已请假列表
     */
    private void initCancelRecyclerView() {
        rvCancel.setLayoutManager(new LinearLayoutManager(mContext));
        mCancelAdapter = new BaseQuickAdapter<CustomerCombox, BaseViewHolder>(R.layout.item_combo_record) {
            @Override
            protected void convert(BaseViewHolder holder, CustomerCombox item) {
                if (item == null) {
                    return;
                }
                holder.setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_type, item.getTypeName());
            }
        };
        rvCancel.setAdapter(mCancelAdapter);
        mCancelAdapter.setEmptyView(R.layout.view_empty);
    }

    @OnClick({R.id.tv_get_identity, R.id.tv_mobile_identify, R.id.tv_id_card_input})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_identity:
                break;
            case R.id.tv_mobile_identify:
                break;
            case R.id.tv_id_card_input:
                break;
            default:
                break;
        }
    }

    @Override
    protected void doBusiness() {
        stateLoading();
        mPresenter.getComboDineList(CommonSubscriber.SHOW_STATE);
    }

    @Override
    protected void firstLoadDataFailRetry() {
        stateLoading();
        mPresenter.getComboDineList(CommonSubscriber.SHOW_STATE);
    }

    @Override
    public void setComboDineList(CustomerComboResponse data) {
        if (data == null) {
            return;
        }
        List<CustomerCombox> unEatList = data.getUnEatList();
        tvUnfinishedTitle.setText("未就餐（" + unEatList.size() + "人）");
        mUnfinishedAdapter.setNewInstance(unEatList);
        List<CustomerCombox> eatList = data.getEatList();
        tvFinishedTitle.setText("已就餐（" + eatList.size() + "人）");
        mFinishedAdapter.setNewInstance(eatList);
        List<CustomerCombox> leaveList = data.getLeaveList();
        tvCancelTitle.setText("已请假（" + leaveList.size() + "人）");
        mCancelAdapter.setNewInstance(leaveList);
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
