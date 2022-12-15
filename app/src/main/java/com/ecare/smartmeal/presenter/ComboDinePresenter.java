package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.ComboDineContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.rsp.CustomerComboResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public class ComboDinePresenter extends RxPresenter<ComboDineContract.View> implements ComboDineContract.Presenter {

    @Override
    public void getComboDineList(int showType) {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getComboDineList()
                .compose(RxUtils.<BaseResponse<CustomerComboResponse>>rxSchedulerHelper())
                .compose(RxUtils.<CustomerComboResponse>handleResult())
                .subscribeWith(new CommonSubscriber<CustomerComboResponse>(mView, showType) {
                    @Override
                    public void onNext(CustomerComboResponse data) {
                        if (isAttachView()) {
                            if (showType == CommonSubscriber.SHOW_STATE) {
                                mView.stateMain();
                            }
                            mView.setComboDineList(data);
                        }
                    }
                })
        );
    }
}
