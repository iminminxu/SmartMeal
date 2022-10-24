package com.ecare.smartmeal.presenter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.base.PagingPresenter;
import com.ecare.smartmeal.contract.MemberRecordListContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BasePaging;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.AccountRequest;
import com.ecare.smartmeal.model.bean.rsp.CustomerBalanceRecordx;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;
import com.ecare.smartmeal.ui.fragment.MemberRecordListFragment;
import com.ecare.smartmeal.utils.NumUtils;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/21.
 * Email: iminminxu@gmail.com
 */
public class MemberRecordListPresenter extends PagingPresenter<MemberRecordListContract.View, CustomerBalanceRecordx> implements MemberRecordListContract.Presenter {

    private int mCustomerId;
    private int mBizType;
    private String mCardNo;

    public MemberRecordListPresenter(int customerId, int bizType, String cardNo) {
        mCustomerId = customerId;
        mBizType = bizType;
        mCardNo = cardNo;
    }

    @Override
    protected BaseQuickAdapter<CustomerBalanceRecordx, BaseViewHolder> createAdapter() {
        return new BaseQuickAdapter<CustomerBalanceRecordx, BaseViewHolder>(R.layout.item_member_record) {
            @Override
            protected void convert(BaseViewHolder helper, CustomerBalanceRecordx item) {
                if (item == null) {
                    return;
                }
                String refundHint = mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION && item.getIsRefund() == 1 ? "(已退款)" : "";
                helper.setText(R.id.tv_left, (mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION ? "消费时间：" : "充值时间：") + item.getCreateTime())
                        .setText(R.id.tv_center, mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION ? "" : "实付金额：" + NumUtils.parsePrintAmount(item.getMoney()))
                        .setText(R.id.tv_right, (mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION ? "消费金额：" : "充值金额：") + NumUtils.parsePrintAmount(item.getAmount()) + refundHint);
            }
        };
    }

    @Override
    protected void loadData(int showType, int pageIndex) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getMemberRecordList(new AccountRequest(mBizType, mCustomerId, pageIndex, mPageSize, mCardNo))
                .compose(RxUtils.<BaseResponse<BasePaging<CustomerBalanceRecordx>>>rxSchedulerHelper())
                .compose(RxUtils.<BasePaging<CustomerBalanceRecordx>>handleResult())
                .subscribeWith(new CommonSubscriber<BasePaging<CustomerBalanceRecordx>>(mView, showType) {
                    @Override
                    public void onNext(BasePaging<CustomerBalanceRecordx> data) {
                        if (isAttachView()) {
                            setData(showType, pageIndex, data.getList());
                        }
                    }
                })
        );
    }

    @Override
    public void setCardNo(String cardNo) {
        mCardNo = cardNo;
    }
}
