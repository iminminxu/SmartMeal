package com.ecare.smartmeal.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.RootFragment;
import com.ecare.smartmeal.contract.HomeRechargeContract;
import com.ecare.smartmeal.presenter.HomeRechargePresenter;
import com.ecare.smartmeal.ui.activity.MemberAddActivity;
import com.ecare.smartmeal.widget.HorizontalDividerItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/13.
 * Email: iminminxu@gmail.com
 */
public class HomeRechargeFragment extends RootFragment<HomeRechargeContract.Presenter> implements HomeRechargeContract.View {

    //搜索条件
    @BindView(R.id.et_card_no)
    EditText etCardNo;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id_card)
    EditText etIDCard;
    @BindView(R.id.et_mobile)
    EditText etMobile;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    //加载组件
    @BindView(R.id.view_main)
    SmartRefreshLayout srlRefresh;
    //会员列表
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @Override
    protected HomeRechargeContract.Presenter createPresenter() {
        return new HomeRechargePresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_recharge;
    }

    @Override
    protected void initVariables() {

    }

    @Override
    public void initViews(@Nullable Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        //初始化SmartRefreshLayout
        initSmartRefreshLayout();
        //初始化会员列表
        initRecyclerView();
        //请求数据
        doBusiness();
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
                .color(Color.TRANSPARENT)
                .sizeResId(R.dimen.dp_21)
                .showLastDivider()
                .build());
    }

    @OnClick({R.id.tv_add_member, R.id.tv_start_time, R.id.tv_end_time, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_member:
                startActivity(new Intent(mContext, MemberAddActivity.class));
                break;
            case R.id.tv_start_time:
            case R.id.tv_end_time:
                new TimePickerBuilder(mContext, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        ((TextView) view).setText(TimeUtils.date2String(date, TimeUtils.getSafeDateFormat("yyyy-MM-dd")));
                    }
                })
                        .setTitleText(view.getId() == R.id.tv_start_time ? "开始时间" : "结束时间")
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .setRangDate(null, Calendar.getInstance())
                        .setDecorView(getActivity().getWindow().getDecorView().findViewById(android.R.id.content))
                        .build()
                        .show();
                break;
            case R.id.tv_search:
                String iDCard = etIDCard.getText().toString();
                if (!StringUtils.isEmpty(iDCard) && !RegexUtils.isIDCard18(iDCard)) {
                    showMsg("请输入正确的身份证号");
                    break;
                }
                String mobile = etMobile.getText().toString();
                if (!StringUtils.isEmpty(mobile) && !RegexUtils.isMobileSimple(mobile)) {
                    showMsg("请输入正确的手机号");
                    break;
                }
                String startTime = tvStartTime.getText().toString();
                String endTime = tvEndTime.getText().toString();
                if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && TimeUtils.getTimeSpan(endTime, startTime, TimeUtils.getSafeDateFormat("yyyy-MM-dd"), TimeConstants.DAY) < 0) {
                    showMsg("结束时间不能早于开始时间");
                    break;
                }
                mPresenter.setSearch(etCardNo.getText().toString(), etName.getText().toString(), iDCard, mobile, startTime, endTime);
                break;
            default:
                break;
        }
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
    public void startActivityForResult(Class<?> clz, Bundle bundle, int requestCode) {
        Intent intent = new Intent(mContext, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }
}
