package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/10/19.
 * Email: iminminxu@gmail.com
 */
public class RefundCardRequest {

    private String cardNo;
    private int customerId;

    public RefundCardRequest(String cardNo, int customerId) {
        this.cardNo = cardNo;
        this.customerId = customerId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
