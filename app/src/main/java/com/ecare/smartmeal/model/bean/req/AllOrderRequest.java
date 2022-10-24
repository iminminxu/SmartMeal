package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/6.
 * Email: iminminxu@gmail.com
 */
public class AllOrderRequest {

    private int orderWay;
    private int page;
    private int status;

    public AllOrderRequest(int orderWay, int page, int status) {
        this.orderWay = orderWay;
        this.page = page;
        this.status = status;
    }

    public int getOrderWay() {
        return orderWay;
    }

    public void setOrderWay(int orderWay) {
        this.orderWay = orderWay;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
