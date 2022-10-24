package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.LoginContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.LoginReqDTO;
import com.ecare.smartmeal.model.bean.rsp.LoginRspDTO;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    @Override
    public void login(String mobile, String password) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .login(new LoginReqDTO(mobile, "", password))
                .compose(RxUtils.<BaseResponse<LoginRspDTO>>rxSchedulerHelper())
                .compose(RxUtils.<LoginRspDTO>handleResult())
                .subscribeWith(new CommonSubscriber<LoginRspDTO>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(LoginRspDTO data) {
                        if (isAttachView()) {
                            mView.loginSuc(data);
                        }
                    }
                })
        );
    }
}
