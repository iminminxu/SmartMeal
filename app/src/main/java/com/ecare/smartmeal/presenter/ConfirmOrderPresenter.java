package com.ecare.smartmeal.presenter;

import com.alipay.iot.sdk.APIManager;
import com.blankj.utilcode.util.RegexUtils;
import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.ConfirmOrderContract;
import com.ecare.smartmeal.model.api.PayApi;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.CommodityOrderPayRequest;
import com.ecare.smartmeal.model.bean.req.CommodityOrderRequest;
import com.ecare.smartmeal.model.bean.req.CustomerxDTO;
import com.ecare.smartmeal.model.bean.req.ElderAuthRequest;
import com.ecare.smartmeal.model.bean.req.GetIdentityRequest;
import com.ecare.smartmeal.model.bean.req.PayReq;
import com.ecare.smartmeal.model.bean.rsp.CommodityOrderPayResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;
import com.ecare.smartmeal.model.bean.rsp.ElderCodeResponse;
import com.ecare.smartmeal.model.bean.rsp.GetIdentityResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderInfoResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;
import com.ecare.smartmeal.utils.IDCardUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class ConfirmOrderPresenter extends RxPresenter<ConfirmOrderContract.View> implements ConfirmOrderContract.Presenter {

    @Override
    public void getOrderInfo(int showType, int eatWay, String idCard, List<CommodityxAllResponseItem> list) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getOrderInfo(new CommodityOrderRequest(eatWay, idCard, list))
                .compose(RxUtils.<BaseResponse<OrderInfoResponse>>rxSchedulerHelper())
                .compose(RxUtils.<OrderInfoResponse>handleResult())
                .subscribeWith(new CommonSubscriber<OrderInfoResponse>(mView, showType) {
                    @Override
                    public void onNext(OrderInfoResponse data) {
                        if (isAttachView()) {
                            if (showType == CommonSubscriber.SHOW_STATE) {
                                mView.stateMain();
                            }
                            mView.setOrderInfo(data);
                        }
                    }
                })
        );
    }

    @Override
    public void getIdentity(String uid) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(PayApi.getInstance().getApiService()
                .getIdentity(new GetIdentityRequest(uid))
                .compose(RxUtils.<BaseResponse<GetIdentityResponse>>rxSchedulerHelper())
                .compose(RxUtils.<GetIdentityResponse>handleResult())
                .subscribeWith(new CommonSubscriber<GetIdentityResponse>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(GetIdentityResponse data) {
                        if (isAttachView()) {
                            if (data == null) {
                                mView.showMsg("人脸识别失败");
                                return;
                            }
                            getElderInfo(new ElderAuthRequest("", "", data.getIdNumber(), ""), data.getUserName());
                        }
                    }
                })
        );
    }

    @Override
    public void getElderInfo(ElderAuthRequest elderAuthRequest, String name) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getElderInfo(elderAuthRequest)
                .compose(RxUtils.<BaseResponse<ElderCodeResponse>>rxSchedulerHelper())
                .compose(RxUtils.<ElderCodeResponse>handleResult())
                .subscribeWith(new CommonSubscriber<ElderCodeResponse>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(ElderCodeResponse data) {
                        if (isAttachView()) {
                            mView.setElderInfo(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        String idCard = elderAuthRequest.getIdCard();
                        if (isAttachView() && RegexUtils.isIDCard18(idCard)) {
                            ElderCodeResponse data = new ElderCodeResponse();
                            data.setIdCard(idCard);
                            data.setAge(IDCardUtils.getAgeByIDCard(idCard));
                            data.setName(name);
                            mView.setElderInfo(data);
                        }
                    }
                })
        );
    }

    @Override
    public void checkTaboos(String idCard, List<CommodityxAllResponseItem> list) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .checkTaboos(new CommodityOrderRequest(idCard, list))
                .compose(RxUtils.<BaseResponse<CommodityOrderRequest>>rxSchedulerHelper())
                .compose(RxUtils.<CommodityOrderRequest>handleResult())
                .subscribeWith(new CommonSubscriber<CommodityOrderRequest>(mView) {
                    @Override
                    public void onNext(CommodityOrderRequest data) {
                        if (isAttachView()) {
                            mView.checkTaboosSuc(data);
                        }
                    }
                })
        );
    }

    @Override
    public void createOrder(BigDecimal amount, Integer couponsId, int eatWay, String idCard, List<CommodityxAllResponseItem> list, BigDecimal ticketDiscountMoney, String customerName) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .createOrder(new CommodityOrderRequest(amount, couponsId, eatWay, idCard, list, ticketDiscountMoney, customerName))
                .compose(RxUtils.<BaseResponse<Integer>>rxSchedulerHelper())
                .compose(RxUtils.<Integer>handleResult())
                .subscribeWith(new CommonSubscriber<Integer>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(Integer data) {
                        if (isAttachView()) {
                            mView.createOrderSuc(data);
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
    public void payOrder(int customerId, int orderId, int payWay, String cardNo) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .payOrder(new CommodityOrderPayRequest(customerId, orderId, payWay, APIManager.getInstance().getDeviceAPI().getDeviceSn(), cardNo))
                .compose(RxUtils.<BaseResponse<CommodityOrderPayResponse>>rxSchedulerHelper())
                .compose(RxUtils.<CommodityOrderPayResponse>handleResult())
                .subscribeWith(new CommonSubscriber<CommodityOrderPayResponse>(mView) {
                    @Override
                    public void onNext(CommodityOrderPayResponse data) {
                        if (isAttachView()) {
                            mView.payOrderSuc(data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isAttachView()) {
                            mView.payOrderFail();
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
                            mView.updateMemberInfoSuc(idCard);
                        }
                    }
                })
        );
    }
}
