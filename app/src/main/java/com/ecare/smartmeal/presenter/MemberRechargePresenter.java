package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.MemberRechargeContract;
import com.ecare.smartmeal.model.api.PayApi;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.CustomerInfoRequest;
import com.ecare.smartmeal.model.bean.req.PayReq;
import com.ecare.smartmeal.model.bean.req.RechargeRequest;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class MemberRechargePresenter extends RxPresenter<MemberRechargeContract.View> implements MemberRechargeContract.Presenter {

    @Override
    public void getMemberInfo(int customerId, String cardNo) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getMemberInfo(new CustomerInfoRequest(customerId, cardNo))
                .compose(RxUtils.<BaseResponse<CustomerListResponse>>rxSchedulerHelper())
                .compose(RxUtils.<CustomerListResponse>handleResult())
                .subscribeWith(new CommonSubscriber<CustomerListResponse>(mView, CommonSubscriber.SHOW_STATE) {
                    @Override
                    public void onNext(CustomerListResponse data) {
                        if (isAttachView()) {
                            mView.stateMain();
                            mView.setMemberInfo(data);
                        }
                    }
                })
        );
    }

    @Override
    public void pay(String subject, String totalAmount, String ftoken, String outTradeNo, String sellerId, String uid, String terminalParams) {
        addSubscribe(PayApi.getInstance().getApiService()
                .pay(new PayReq(subject, totalAmount, ftoken, outTradeNo, sellerId, uid, terminalParams))
                .compose(RxUtils.<BaseResponse<Boolean>>rxSchedulerHelper())
                .compose(RxUtils.<Boolean>handleResult())
                .subscribeWith(new CommonSubscriber<Boolean>(mView) {
                    @Override
                    public void onNext(Boolean data) {
                        if (isAttachView()) {
                            mView.paySuc(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isAttachView()) {
                            mView.paySuc(false);
                        }
                    }
                })
        );
    }

    @Override
    public void memberRecharge(int customerId, int type, BigDecimal money, BigDecimal amount, String cardNo) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .memberRecharge(new RechargeRequest(amount, customerId, money, type, cardNo))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.memberRechargeSuc(true);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isAttachView()) {
                            mView.memberRechargeSuc(false);
                        }
                    }
                })
        );
    }
}
