package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface MainContract {

    interface View extends BaseView {

        void setMerchantInfo(MerchantInfoResponse data);

        void setAuthToken(String authToken);
    }

    interface Presenter extends BasePresenter<View> {

        void getMerchantInfo();

        void getAuthToken(String metaInfo);
    }
}
