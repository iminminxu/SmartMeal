package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.LoginRspDTO;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface LoginContract {

    interface View extends BaseView {

        void loginSuc(LoginRspDTO data);
    }

    interface Presenter extends BasePresenter<View> {

        void login(String mobile, String password);
    }
}
