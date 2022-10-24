package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePagingPresenter;
import com.ecare.smartmeal.base.BasePagingView;
import com.ecare.smartmeal.model.bean.rsp.CustomerBalanceRecordx;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/8.
 * Email: iminminxu@gmail.com
 */
public interface MemberRecordListContract {

    interface View extends BasePagingView {

    }

    interface Presenter extends BasePagingPresenter<View, CustomerBalanceRecordx> {

        void setCardNo(String cardNo);
    }
}
