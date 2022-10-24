package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/10/19.
 * Email: iminminxu@gmail.com
 */
public class RefundOrderRequest {

    private String orderno;

    public RefundOrderRequest(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
