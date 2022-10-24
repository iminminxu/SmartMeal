package com.ecare.smartmeal.model.bean.rsp;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/26.
 * Email: iminminxu@gmail.com
 */
public class OrderInfoResponse {

    private BigDecimal amount;
    private Integer couponsId;
    private BigDecimal packing;
    private BigDecimal ticketDiscountMoney;
    private BigDecimal totalFee;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getCouponsId() {
        return couponsId;
    }

    public void setCouponsId(Integer couponsId) {
        this.couponsId = couponsId;
    }

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getTicketDiscountMoney() {
        return ticketDiscountMoney;
    }

    public void setTicketDiscountMoney(BigDecimal ticketDiscountMoney) {
        this.ticketDiscountMoney = ticketDiscountMoney;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }
}
