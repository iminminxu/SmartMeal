package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.CustomerComboResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public interface ComboDeliverContract {

    interface View extends BaseView {

        void setComboDeliverList(CustomerComboResponse data);

        void autoRefresh();
    }

    interface Presenter extends BasePresenter<View> {

        void getComboDeliverList(@CommonSubscriber.ShowType int showType);

        void comboCancel(int comboId);

        void comboDeliver(List<Integer> comboIdList);
    }
}
