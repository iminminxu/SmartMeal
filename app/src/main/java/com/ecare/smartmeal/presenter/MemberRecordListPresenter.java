package com.ecare.smartmeal.presenter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
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
import com.ecare.smartmeal.ui.activity.MemberInfoActivity;
import com.ecare.smartmeal.ui.fragment.MemberRecordListFragment;
import com.ecare.smartmeal.utils.NumUtils;
import com.ecare.smartmeal.utils.PrintUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.sunmi.externalprinterlibrary.api.Status;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/21.
 * Email: iminminxu@gmail.com
 */
public class MemberRecordListPresenter extends PagingPresenter<MemberRecordListContract.View, CustomerBalanceRecordx> implements MemberRecordListContract.Presenter {

    //上下文
    private Context mContext;
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
        BaseQuickAdapter<CustomerBalanceRecordx, BaseViewHolder> adapter = new BaseQuickAdapter<CustomerBalanceRecordx, BaseViewHolder>(R.layout.item_member_record) {
            @Override
            public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                MemberRecordListPresenter.this.mContext = parent.getContext();
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            protected void convert(BaseViewHolder helper, CustomerBalanceRecordx item) {
                if (item == null) {
                    return;
                }
                String refundHint = mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION && item.getIsRefund() == 1 ? "(已退款)" : "";
                helper.setText(R.id.tv_left, (mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION ? "消费时间：" : "充值时间：") + item.getCreateTime())
                        .setText(R.id.tv_center, mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION ? "" : "实付金额：" + NumUtils.parsePrintAmount(item.getMoney()))
                        .setText(R.id.tv_right, (mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION ? "消费金额：" : "充值金额：") + NumUtils.parsePrintAmount(item.getAmount()) + refundHint)
                        .setGone(R.id.tv_print, mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION);
            }
        };
        adapter.addChildClickViewIds(R.id.tv_print);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                CustomerBalanceRecordx item = mAdapter.getItem(position);
                if (!isAttachView() || item == null || mBizType == MemberRecordListFragment.BIZ_TYPE_CONSUMPTION) {
                    return;
                }
                new XPopup.Builder(mContext).asConfirm("提示", "确定补打小票？", "取消", "确认", new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        print(item);
                    }
                }, null, false).show();
            }
        });
        return adapter;
    }

    /**
     * 普通打印一单小票
     * 普通打印不关心打印执行结果
     * 建议打印接口调用在非主线程（网络打印机必须在非主线程）
     */
    public void print(CustomerBalanceRecordx item) {
        if (!isAttachView() || !(mContext instanceof MemberInfoActivity)) {
            return;
        }
        try {
            int status = SunmiPrinterApi.getInstance().getPrinterStatus();
            if (!SunmiPrinterApi.getInstance().isConnected() || status != Status.RUNNING) {
                mView.showMsg("未连接打印机，请确认打印机是否连接！");
                return;
            }
            mView.showMsg("请取走小票凭证！");
            PrintUtils.printMemberRechargeOrder(((MemberInfoActivity) mContext).getData(), item.getAmount(), item.getMoney(), item.getBalance(), item.getCreateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @Override
    public void detachView() {
        super.detachView();
        mContext = null;
    }
}
