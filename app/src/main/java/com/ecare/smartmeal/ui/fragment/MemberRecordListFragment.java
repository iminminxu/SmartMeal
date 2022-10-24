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
import com.ecare.smartmeal.contract.MemberRecordListContract;
import com.ecare.smartmeal.model.bean.rsp.Event;
import com.ecare.smartmeal.presenter.MemberRecordListPresenter;
import com.ecare.smartmeal.ui.activity.MemberInfoActivity;
import com.ecare.smartmeal.widget.HorizontalDividerItemDecoration;
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
 * Created by xuminmin on 2022/6/21.
 * Email: iminminxu@gmail.com
 */
public class MemberRecordListFragment extends RootFragment<MemberRecordListContract.Presenter> implements MemberRecordListContract.View {

    //记录类型
    public static final int BIZ_TYPE_CONSUMPTION = 0; //出账
    public static final int BIZ_TYPE_RECHARGE = 1;    //入账

    @IntDef({BIZ_TYPE_CONSUMPTION, BIZ_TYPE_RECHARGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BizType {

    }

    /**
     * MemberRecordListFragment构建方法
     *
     * @param bizType 记录类型
     * @return MemberRecordListFragment
     */
    public static MemberRecordListFragment newInstance(int customerId, @BizType int bizType) {
        MemberRecordListFragment fragment = new MemberRecordListFragment();
        Bundle args = new Bundle();
        args.putInt(Constants.IT_CUSTOMER_ID, customerId);
        args.putInt(Constants.IT_TYPE, bizType);
        fragment.setArguments(args);
        return fragment;
    }

    //加载组件
    @BindView(R.id.view_main)
    SmartRefreshLayout srlRefresh;
    //会员列表
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    //跳转参数
    private int mCustomerId;
    private int mBizType;

    @Override
    protected MemberRecordListContract.Presenter createPresenter() {
        return new MemberRecordListPresenter(mCustomerId, mBizType, ((MemberInfoActivity) getActivity()).getCardNo());
    }

    @Override
    protected void initVariables() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mCustomerId = bundle.getInt(Constants.IT_CUSTOMER_ID);
        mBizType = bundle.getInt(Constants.IT_TYPE);
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
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //初始化SmartRefreshLayout
        initSmartRefreshLayout();
        //初始化会员列表
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
     * 初始化会员列表
     */
    private void initRecyclerView() {
        rvList.setLayoutManager(new LinearLayoutManager(mContext));
        BaseQuickAdapter adapter = mPresenter.getAdapter();
        rvList.setAdapter(adapter);
        adapter.setEmptyView(R.layout.view_empty);
        rvList.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_e6e6e6))
                .sizeResId(R.dimen.dp_1)
                .showLastDivider()
                .build());
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
    public void onMessageEvent(Event.ModifyCardNoEvent event) {
        mPresenter.setCardNo(event.getCardNo());
    }
}
