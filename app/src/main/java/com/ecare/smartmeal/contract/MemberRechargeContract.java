package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface MemberRechargeContract {

    interface View extends BaseView {

        void setMemberInfo(CustomerListResponse data);

        void paySuc(boolean data);

        void memberRechargeSuc(boolean data);
    }

    interface Presenter extends BasePresenter<View> {

        void getMemberInfo(int customerId, String cardNo);

        void pay(String subject, String totalAmount, String ftoken, String outTradeNo, String sellerId, String uid, String terminalParams);

        void memberRecharge(int customerId, int type, BigDecimal money, BigDecimal amount, String cardNo);
    }
}
