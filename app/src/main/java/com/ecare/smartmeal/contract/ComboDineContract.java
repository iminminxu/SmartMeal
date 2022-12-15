package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.CustomerComboResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public interface ComboDineContract {

    interface View extends BaseView {

        void setComboDineList(CustomerComboResponse data);
    }

    interface Presenter extends BasePresenter<View> {

        void getComboDineList(@CommonSubscriber.ShowType int showType);
    }
}
