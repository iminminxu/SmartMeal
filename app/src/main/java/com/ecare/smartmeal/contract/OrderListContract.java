package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePagingPresenter;
import com.ecare.smartmeal.base.BasePagingView;
import com.ecare.smartmeal.model.bean.rsp.RiderNewOrdersResponse;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/8.
 * Email: iminminxu@gmail.com
 */
public interface OrderListContract {

    interface View extends BasePagingView {

        void autoRefresh();
    }

    interface Presenter extends BasePagingPresenter<View, RiderNewOrdersResponse> {

    }
}
