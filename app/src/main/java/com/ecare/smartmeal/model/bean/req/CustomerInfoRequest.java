package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/20.
 * Email: iminminxu@gmail.com
 */
public class CustomerInfoRequest {

    private int customerId;
    private String cardNo;

    public CustomerInfoRequest(int customerId, String cardNo) {
        this.customerId = customerId;
        this.cardNo = cardNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
