package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/19.
 * Email: iminminxu@gmail.com
 */
public interface RegisterOrRetrievePasswordContract {

    interface View extends BaseView {

        void getSmsCodeSuc();

        void registerSuc();

        void retrievePasswordSuc();
    }

    interface Presenter extends BasePresenter<View> {

        void getSmsCode(String mobile, int type);

        void register(String mobile, String password, String smsCode);

        void retrievePassword(String mobile, String password, String smsCode);
    }
}
