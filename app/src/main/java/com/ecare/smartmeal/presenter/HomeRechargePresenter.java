package com.ecare.smartmeal.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.PagingPresenter;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.contract.HomeRechargeContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BasePaging;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.CustomerListRequest;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;
import com.ecare.smartmeal.ui.activity.MemberInfoActivity;
import com.ecare.smartmeal.ui.activity.MemberRechargeActivity;
import com.ecare.smartmeal.utils.NumUtils;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/16.
 * Email: iminminxu@gmail.com
 */
public class HomeRechargePresenter extends PagingPresenter<HomeRechargeContract.View, CustomerListResponse> implements HomeRechargeContract.Presenter {

    //搜索条件
    private String mCardNo;
    private String mName;
    private String mIDCard;
    private String mMobile;
    private String mBeginTime;
    private String mEndTime;

    @Override
    protected BaseQuickAdapter<CustomerListResponse, BaseViewHolder> createAdapter() {
        BaseQuickAdapter<CustomerListResponse, BaseViewHolder> adapter = new BaseQuickAdapter<CustomerListResponse, BaseViewHolder>(R.layout.item_member) {
            @Override
            protected void convert(BaseViewHolder helper, CustomerListResponse item) {
                if (item == null) {
                    return;
                }
                helper.setText(R.id.tv_card_no, "卡号： " + item.getCardNo())
                        .setText(R.id.tv_name, item.getName())
                        .setText(R.id.tv_id_card, item.getIdCard())
                        .setText(R.id.tv_mobile, item.getMobile())
                        .setText(R.id.tv_address, item.getLiveAddrx())
                        .setText(R.id.tv_balance, NumUtils.parsePrintAmount(item.getBalance()))
                        .setText(R.id.tv_consumption_amount, NumUtils.parsePrintAmount(item.getTotalAmount()))
                        .setText(R.id.tv_create_time, item.getCreateTime());
            }
        };
        adapter.addChildClickViewIds(R.id.tv_detail, R.id.tv_recharge);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CustomerListResponse item = mAdapter.getItem(position);
                if (!isAttachView() || item == null) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.IT_POSITION, position);
                bundle.putInt(Constants.IT_CUSTOMER_ID, item.getId());
                bundle.putString(Constants.IT_CARD_NO, item.getCardNo());
                switch (view.getId()) {
                    case R.id.tv_detail:
                        mView.startActivityForResult(MemberInfoActivity.class, bundle, Constants.REQ_MEMBER_INFO);
                        break;
                    case R.id.tv_recharge:
                        mView.startActivityForResult(MemberRechargeActivity.class, bundle, Constants.REQ_MEMBER_RECHARGE);
                        break;
                    default:
                        break;
                }
            }
        });
        return adapter;
    }

    @Override
    protected void loadData(int showType, int pageIndex) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getMemberList(new CustomerListRequest(mBeginTime, mCardNo, mEndTime, mIDCard, mMobile, mName, pageIndex, mPageSize))
                .compose(RxUtils.<BaseResponse<BasePaging<CustomerListResponse>>>rxSchedulerHelper())
                .compose(RxUtils.<BasePaging<CustomerListResponse>>handleResult())
                .subscribeWith(new CommonSubscriber<BasePaging<CustomerListResponse>>(mView, showType) {
                    @Override
                    public void onNext(BasePaging<CustomerListResponse> data) {
                        if (isAttachView()) {
                            setData(showType, pageIndex, data.getList());
                        }
                    }
                })
        );
    }

    @Override
    public void setSearch(String cardNo, String name, String iDCard, String mobile, String beginTime, String endTime) {
        mCardNo = cardNo;
        mName = name;
        mIDCard = iDCard;
        mMobile = mobile;
        mBeginTime = beginTime;
        mEndTime = endTime;
        firstLoadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data == null) {
            return;
        }
        int position = data.getIntExtra(Constants.IT_POSITION, -1);
        if (position < 0 || position >= mAdapter.getItemCount()) {
            return;
        }
        int customerId = data.getIntExtra(Constants.IT_CUSTOMER_ID, 0);
        String cardNo = data.getStringExtra(Constants.IT_CARD_NO);
        CustomerListResponse item = mAdapter.getItem(position);
        if (item == null || item.getId() != customerId || !StringUtils.equals(cardNo, item.getCardNo())) {
            return;
        }
        switch (requestCode) {
            case Constants.REQ_MEMBER_INFO:
                boolean refundCard = data.getBooleanExtra(Constants.IT_REFUND_CARD, false);
                if (refundCard) {
                    mAdapter.removeAt(position);
                } else {
                    item.setIdCard(data.getStringExtra(Constants.IT_ID_CARD));
                    item.setCardNo(data.getStringExtra(Constants.IT_NEW_CARD_NO));
                    mAdapter.notifyItemChanged(position);
                }
                break;
            case Constants.REQ_MEMBER_RECHARGE:
                item.setBalance((BigDecimal) data.getSerializableExtra(Constants.IT_BALANCE));
                mAdapter.notifyItemChanged(position);
                break;
            default:
                break;
        }
    }
}
