package com.ecare.smartmeal.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.widget.VerticalDividerItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/11/15.
 * Email: iminminxu@gmail.com
 */
public class ComboDeliverFragment extends RootFragment {

    //加载组件
    @BindView(R.id.view_main)
    SmartRefreshLayout srlRefresh;
    //未就餐
    @BindView(R.id.rv_unfinished)
    RecyclerView rvUnfinished;
    //已就餐
    @BindView(R.id.tv_finished_title)
    TextView tvFinishedTitle;
    @BindView(R.id.rv_finished)
    RecyclerView rvFinished;
    //已请假
    @BindView(R.id.tv_cancel_title)
    TextView tvCancelTitle;
    @BindView(R.id.rv_cancel)
    RecyclerView rvCancel;

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_combo_deliver;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
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

            }
        });
    }

    /**
     * 初始化未就餐列表
     */
    private void initUnfinishedRecyclerView() {
        List<String> data = new ArrayList<>();
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        rvUnfinished.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        rvUnfinished.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_combo_deliver, data) {
            @Override
            protected void convert(BaseViewHolder holder, String s) {
                RecyclerView rvRecord = holder.getView(R.id.rv_record);
                List<String> datas = new ArrayList<>();
                datas.add("");
                datas.add("");
                datas.add("");
                datas.add("");
                rvRecord.setLayoutManager(new LinearLayoutManager(mContext));
                rvRecord.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_combo_deliver_record, datas) {
                    @Override
                    protected void convert(BaseViewHolder holder, String s) {

                    }
                });
            }
        });
        rvUnfinished.addItemDecoration(new VerticalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.dp_13)
                .build());
    }

    /**
     * 初始化已就餐列表
     */
    private void initFinishedRecyclerView() {
        List<String> data = new ArrayList<>();
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        rvFinished.setLayoutManager(new LinearLayoutManager(mContext));
        rvFinished.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_combo_record, data) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {

            }
        });
    }

    /**
     * 初始化已请假列表
     */
    private void initCancelRecyclerView() {
        List<String> data = new ArrayList<>();
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        data.add("");
        rvCancel.setLayoutManager(new LinearLayoutManager(mContext));
        rvCancel.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_combo_record, data) {
            @Override
            protected void convert(BaseViewHolder baseViewHolder, String s) {

            }
        });
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    protected void firstLoadDataFailRetry() {

    }
}
