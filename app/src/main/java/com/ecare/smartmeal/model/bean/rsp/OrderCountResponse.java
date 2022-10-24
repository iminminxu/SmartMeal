package com.ecare.smartmeal.model.bean.rsp;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/9/13.
 * Email: iminminxu@gmail.com
 */
public class OrderCountResponse {

    private int customerNum;
    private int totalOrderNum;
    private BigDecimal totalOrderPrice;

    public int getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(int customerNum) {
        this.customerNum = customerNum;
    }

    public int getTotalOrderNum() {
        return totalOrderNum;
    }

    public void setTotalOrderNum(int totalOrderNum) {
        this.totalOrderNum = totalOrderNum;
    }

    public BigDecimal getTotalOrderPrice() {
        return totalOrderPrice;
    }

    public void setTotalOrderPrice(BigDecimal totalOrderPrice) {
        this.totalOrderPrice = totalOrderPrice;
    }
}
