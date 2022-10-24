package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.MemberInfoContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.CustomerInfoRequest;
import com.ecare.smartmeal.model.bean.req.CustomerxDTO;
import com.ecare.smartmeal.model.bean.req.RefundCardRequest;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class MemberInfoPresenter extends RxPresenter<MemberInfoContract.View> implements MemberInfoContract.Presenter {

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
    public void updateMemberInfo(int customerId, String idCard, String cardNo, String oldCardNo) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .updateMemberInfo(new CustomerxDTO(customerId, idCard, cardNo, oldCardNo))
                .compose(RxUtils.<BaseResponse<Integer>>rxSchedulerHelper())
                .compose(RxUtils.<Integer>handleResult())
                .subscribeWith(new CommonSubscriber<Integer>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(Integer data) {
                        if (isAttachView()) {
                            mView.updateMemberInfoSuc(idCard, cardNo);
                        }
                    }
                })
        );
    }

    @Override
    public void refundCard(int customerId, String cardNo) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .refundCard(new RefundCardRequest(cardNo, customerId))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.refundCardSuc();
                        }
                    }
                })
        );
    }
}
