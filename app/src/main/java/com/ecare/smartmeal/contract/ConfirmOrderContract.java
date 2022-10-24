package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.req.CommodityOrderRequest;
import com.ecare.smartmeal.model.bean.req.ElderAuthRequest;
import com.ecare.smartmeal.model.bean.rsp.CommodityOrderPayResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;
import com.ecare.smartmeal.model.bean.rsp.ElderCodeResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderInfoResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface ConfirmOrderContract {

    interface View extends BaseView {

        void setOrderInfo(OrderInfoResponse data);

        void setElderInfo(ElderCodeResponse data);

        void checkTaboosSuc(CommodityOrderRequest data);

        void createOrderSuc(int data);

        void paySuc(boolean data);

        void payOrderSuc(CommodityOrderPayResponse data);

        void payOrderFail();

        void updateMemberInfoSuc(String idCard);
    }

    interface Presenter extends BasePresenter<View> {

        void getOrderInfo(int showType, int eatWay, String idCard, List<CommodityxAllResponseItem> list);

        void getIdentity(String uid);

        void getElderInfo(ElderAuthRequest elderAuthRequest, String name);

        void checkTaboos(String idCard, List<CommodityxAllResponseItem> list);

        void createOrder(BigDecimal amount, Integer couponsId, int eatWay, String idCard, List<CommodityxAllResponseItem> list, BigDecimal ticketDiscountMoney);

        void pay(String subject, String totalAmount, String ftoken, String outTradeNo, String sellerId, String uid, String terminalParams);

        void payOrder(int customerId, int orderId, int payWay, String cardNo);

        void updateMemberInfo(int customerId, String idCard, String cardNo, String oldCardNo);
    }
}
