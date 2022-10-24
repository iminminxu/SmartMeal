package com.ecare.smartmeal.facepay;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/27.
 * Email: iminminxu@gmail.com
 */
public class Trade {

    private String totalAmount;
    private String actualAmount;
    private String totalDiscount;

    public Trade(String totalAmount, String actualAmount, String totalDiscount) {
        this.totalAmount = totalAmount;
        this.actualAmount = actualAmount;
        this.totalDiscount = totalDiscount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }
}
