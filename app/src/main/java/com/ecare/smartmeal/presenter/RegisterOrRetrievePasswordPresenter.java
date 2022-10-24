package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.RegisterOrRetrievePasswordContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.RegisterReqDTO;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/19.
 * Email: iminminxu@gmail.com
 */
public class RegisterOrRetrievePasswordPresenter extends RxPresenter<RegisterOrRetrievePasswordContract.View> implements RegisterOrRetrievePasswordContract.Presenter {

    @Override
    public void getSmsCode(String mobile, int type) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getSmsCode(mobile, type)
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.getSmsCodeSuc();
                        }
                    }
                })
        );
    }

    @Override
    public void register(String mobile, String password, String smsCode) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .register(new RegisterReqDTO(mobile, password, smsCode))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.registerSuc();
                        }
                    }
                })
        );
    }

    @Override
    public void retrievePassword(String mobile, String password, String smsCode) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .retrievePassword(new RegisterReqDTO(mobile, password, smsCode))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.retrievePasswordSuc();
                        }
                    }
                })
        );
    }
}
