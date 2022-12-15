package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.ComboDeliverContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.CustomerComboDeliveryRequest;
import com.ecare.smartmeal.model.bean.req.CustomerComboEatRequest;
import com.ecare.smartmeal.model.bean.rsp.CustomerComboResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public class ComboDeliverPresenter extends RxPresenter<ComboDeliverContract.View> implements ComboDeliverContract.Presenter {

    @Override
    public void getComboDeliverList(int showType) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getComboDeliverList()
                .compose(RxUtils.<BaseResponse<CustomerComboResponse>>rxSchedulerHelper())
                .compose(RxUtils.<CustomerComboResponse>handleResult())
                .subscribeWith(new CommonSubscriber<CustomerComboResponse>(mView, showType) {
                    @Override
                    public void onNext(CustomerComboResponse data) {
                        if (isAttachView()) {
                            if (showType == CommonSubscriber.SHOW_STATE) {
                                mView.stateMain();
                            }
                            mView.setComboDeliverList(data);
                        }
                    }
                })
        );
    }

    @Override
    public void comboCancel(int comboId) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .comboCancel(new CustomerComboEatRequest(comboId, 0))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.showMsg("该套餐已终止，订单已标记为请假");
                            mView.autoRefresh();
                        }
                    }
                })
        );
    }

    @Override
    public void comboDeliver(List<Integer> comboIdList) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .comboDeliver(new CustomerComboDeliveryRequest(comboIdList))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.showMsg("操作成功,本次批量送餐人数" + comboIdList.size() + "人");
                            mView.autoRefresh();
                        }
                    }
                })
        );
    }
}
