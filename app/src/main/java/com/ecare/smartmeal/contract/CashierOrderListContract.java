package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePagingPresenter;
import com.ecare.smartmeal.base.BasePagingView;
import com.ecare.smartmeal.model.bean.rsp.OrderCountResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderListResponse;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/8.
 * Email: iminminxu@gmail.com
 */
public interface CashierOrderListContract {

    interface View extends BasePagingView {

        void setOrderCount(OrderCountResponse data);
    }

    interface Presenter extends BasePagingPresenter<View, OrderListResponse> {

        void getOrderCount();
    }
}
