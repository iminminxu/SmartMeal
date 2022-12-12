package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/8.
 * Email: iminminxu@gmail.com
 */
public class ChangeOrderStatusRequest {

    private String orderno;
    private int status;

    public ChangeOrderStatusRequest(String orderno, int status) {
        this.orderno = orderno;
        this.status = status;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
