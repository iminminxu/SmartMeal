package com.ecare.smartmeal.presenter;

import com.alipay.iot.sdk.APIManager;
import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.MainContract;
import com.ecare.smartmeal.model.api.PayApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.MerchantInfoRequest;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    @Override
    public void getMerchantInfo() {
        addSubscribe(PayApi.getInstance().getApiService()
                .getMerchantInfo(new MerchantInfoRequest(APIManager.getInstance().getDeviceAPI().getDeviceSn()))
                .compose(RxUtils.<BaseResponse<MerchantInfoResponse>>rxSchedulerHelper())
                .compose(RxUtils.<MerchantInfoResponse>handleResult())
                .subscribeWith(new CommonSubscriber<MerchantInfoResponse>(mView) {
                    @Override
                    public void onNext(MerchantInfoResponse data) {
                        if (isAttachView()) {
                            mView.setMerchantInfo(data);
                        }
                    }
                })
        );
    }

    @Override
    public void getAuthToken(String metaInfo) {
        addSubscribe(PayApi.getInstance().getApiService()
                .getAuthToken(metaInfo)
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.setAuthToken(data);
                        }
                    }
                })
        );
    }
}
