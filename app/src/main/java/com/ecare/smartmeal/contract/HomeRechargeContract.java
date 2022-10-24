package com.ecare.smartmeal.contract;

import android.os.Bundle;

import com.ecare.smartmeal.base.BasePagingPresenter;
import com.ecare.smartmeal.base.BasePagingView;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/8.
 * Email: iminminxu@gmail.com
 */
public interface HomeRechargeContract {

    interface View extends BasePagingView {

        void startActivityForResult(Class<?> clz, Bundle bundle, int requestCode);
    }

    interface Presenter extends BasePagingPresenter<View, CustomerListResponse> {

        void setSearch(String cardNo, String name, String iDCard, String mobile, String beginTime, String endTime);
    }
}
