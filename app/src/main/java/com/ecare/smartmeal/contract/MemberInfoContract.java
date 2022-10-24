package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface MemberInfoContract {

    interface View extends BaseView {

        void setMemberInfo(CustomerListResponse data);

        void updateMemberInfoSuc(String idCard, String cardNo);

        void refundCardSuc();
    }

    interface Presenter extends BasePresenter<View> {

        void getMemberInfo(int customerId, String cardNo);

        void updateMemberInfo(int customerId, String idCard, String cardNo, String oldCardNo);

        void refundCard(int customerId, String cardNo);
    }
}
