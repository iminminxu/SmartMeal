package com.ecare.smartmeal.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.contract.ComboDeliverContract;
import com.ecare.smartmeal.model.bean.rsp.CustomerComboResponse;
import com.ecare.smartmeal.model.bean.rsp.CustomerCombox;
import com.ecare.smartmeal.model.bean.rsp.DeliveryObject;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.presenter.ComboDeliverPresenter;
import com.ecare.smartmeal.widget.VerticalDividerItemDecoration;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
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
public class ComboDeliverFragment extends RootFragment<ComboDeliverContract.Presenter> implements ComboDeliverContract.View {

    //加载组件
    @BindView(R.id.view_main)
    SmartRefreshLayout srlRefresh;
    //未就餐
    @BindView(R.id.rv_unfinished)
    RecyclerView rvUnfinished;
    private BaseQuickAdapter<DeliveryObject, BaseViewHolder> mUnfinishedAdapter;
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
    protected ComboDeliverContract.Presenter createPresenter() {
        return new ComboDeliverPresenter();
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
                mPresenter.getComboDeliverList(CommonSubscriber.SHOW_REFRESH_LAYOUT);
            }
        });
    }

    /**
     * 初始化未就餐列表
     */
    private void initUnfinishedRecyclerView() {
        rvUnfinished.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mUnfinishedAdapter = new BaseQuickAdapter<DeliveryObject, BaseViewHolder>(R.layout.item_combo_deliver) {
            @Override
            protected void convert(BaseViewHolder holder, DeliveryObject item) {
                if (item == null) {
                    return;
                }
                holder.setImageResource(R.id.iv_check, item.isCheck() ? R.drawable.icon_check_square : R.drawable.icon_un_check_square);
                //设置标题
                List<CustomerCombox> list = item.getList();
                if (list == null || list.size() == 0) {
                    holder.setText(R.id.tv_title, item.getName() + "（共0人，已选中0人）");
                } else {
                    if (item.isCheck()) {
                        holder.setText(R.id.tv_title, item.getName() + "（共" + list.size() + "人，已选中" + list.size() + "人）");
                    } else {
                        int checkNum = 0;
                        for (CustomerCombox customerCombox : list) {
                            if (customerCombox == null) {
                                continue;
                            }
                            if (customerCombox.isCheck()) {
                                checkNum++;
                            }
                        }
                        holder.setText(R.id.tv_title, item.getName() + "（共" + list.size() + "人，已选中" + checkNum + "人）");
                    }
                }
                //设置送餐记录列表
                RecyclerView rvRecord = holder.getView(R.id.rv_record);
                rvRecord.setLayoutManager(new LinearLayoutManager(mContext));
                BaseQuickAdapter<CustomerCombox, BaseViewHolder> recordAdapter = new BaseQuickAdapter<CustomerCombox, BaseViewHolder>(R.layout.item_combo_deliver_record, list) {
                    @Override
                    protected void convert(BaseViewHolder baseViewHolder, CustomerCombox customerCombox) {
                        if (customerCombox == null) {
                            return;
                        }
                        baseViewHolder.setImageResource(R.id.iv_check, customerCombox.isCheck() ? R.drawable.icon_check_square : R.drawable.icon_un_check_square)
                                .setText(R.id.tv_name, customerCombox.getName())
                                .setText(R.id.tv_mobile, customerCombox.getMobile())
                                .setText(R.id.tv_num, String.valueOf(customerCombox.getRemainTimes()))
                                .setTextColor(R.id.tv_num, getResources().getColor(customerCombox.getRemainTimes() < 7 ? R.color.color_f71413 : R.color.color_666666));
                    }
                };
                recordAdapter.addChildClickViewIds(R.id.iv_check, R.id.tv_cancel);
                recordAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                        CustomerCombox customerCombox = recordAdapter.getItem(position);
                        if (customerCombox == null) {
                            return;
                        }
                        switch (view.getId()) {
                            case R.id.iv_check:
                                customerCombox.setCheck(!customerCombox.isCheck());
                                int checkNum = 0;
                                for (CustomerCombox combox : list) {
                                    if (combox == null) {
                                        continue;
                                    }
                                    if (combox.isCheck()) {
                                        checkNum++;
                                    }
                                }
                                item.setCheck(checkNum == list.size());
                                mUnfinishedAdapter.notifyDataSetChanged();
                                break;
                            case R.id.tv_cancel:
                                new XPopup.Builder(mContext).asConfirm(customerCombox.getName() + "，确认请假，本餐不扣餐次", "该操作不可恢复，请仔细核对老人姓名", "取消", "确认", new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        mPresenter.comboCancel(customerCombox.getId());
                                    }
                                }, null, false).show();
                                break;
                            default:
                                break;
                        }
                    }
                });
                rvRecord.setAdapter(recordAdapter);
                recordAdapter.setEmptyView(R.layout.view_empty);
            }
        };
        mUnfinishedAdapter.addChildClickViewIds(R.id.iv_check, R.id.tv_confirm);
        mUnfinishedAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                DeliveryObject item = mUnfinishedAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                List<CustomerCombox> list = item.getList();
                switch (view.getId()) {
                    case R.id.iv_check:
                        item.setCheck(!item.isCheck());
                        if (list != null && list.size() != 0) {
                            for (CustomerCombox customerCombox : list) {
                                if (customerCombox == null) {
                                    continue;
                                }
                                customerCombox.setCheck(item.isCheck());
                            }
                        }
                        mUnfinishedAdapter.notifyDataSetChanged();
                        break;
                    case R.id.tv_confirm:
                        new XPopup.Builder(mContext).asConfirm("提示", "是否执行批量送餐？", "取消", "确认", new OnConfirmListener() {
                            @Override
                            public void onConfirm() {
                                List<Integer> checkList = new ArrayList<>();
                                if (list != null && list.size() != 0) {
                                    for (CustomerCombox customerCombox : list) {
                                        if (customerCombox == null) {
                                            continue;
                                        }
                                        if (customerCombox.isCheck()) {
                                            checkList.add(customerCombox.getId());
                                        }
                                    }
                                }
                                if (checkList.size() == 0) {
                                    showMsg("请选择送餐用户");
                                    return;
                                }
                                mPresenter.comboDeliver(checkList);
                            }
                        }, null, false).show();
                        break;
                    default:
                        break;
                }
            }
        });
        rvUnfinished.setAdapter(mUnfinishedAdapter);
        mUnfinishedAdapter.setEmptyView(R.layout.view_empty);
        mUnfinishedAdapter.getEmptyLayout().setBackgroundResource(R.drawable.corners_3_solid_white);
        rvUnfinished.addItemDecoration(new VerticalDividerItemDecoration.Builder(mContext)
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.dp_13)
                .build());
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

    @Override
    protected void doBusiness() {
        stateLoading();
        mPresenter.getComboDeliverList(CommonSubscriber.SHOW_STATE);
    }

    @Override
    protected void firstLoadDataFailRetry() {
        stateLoading();
        mPresenter.getComboDeliverList(CommonSubscriber.SHOW_STATE);
    }

    @Override
    public void setComboDeliverList(CustomerComboResponse data) {
        if (data == null) {
            return;
        }
        mUnfinishedAdapter.setNewInstance(data.getDeliveryList());
        List<CustomerCombox> eatList = data.getEatList();
        tvFinishedTitle.setText("已就餐（" + eatList.size() + "人）");
        mFinishedAdapter.setNewInstance(eatList);
        List<CustomerCombox> leaveList = data.getLeaveList();
        tvCancelTitle.setText("已请假（" + leaveList.size() + "人）");
        mCancelAdapter.setNewInstance(leaveList);
    }

    @Override
    public void autoRefresh() {
        srlRefresh.autoRefresh();
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
