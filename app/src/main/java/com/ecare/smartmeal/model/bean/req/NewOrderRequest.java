package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/8.
 * Email: iminminxu@gmail.com
 */
public class NewOrderRequest {

    private String orderno;
    private String sonOrderno;
    private int status;

    public NewOrderRequest(String orderno, String sonOrderno, int status) {
        this.orderno = orderno;
        this.sonOrderno = sonOrderno;
        this.status = status;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getSonOrderno() {
        return sonOrderno;
    }

    public void setSonOrderno(String sonOrderno) {
        this.sonOrderno = sonOrderno;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
