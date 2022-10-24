package com.ecare.smartmeal.model.bean.req;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/27.
 * Email: iminminxu@gmail.com
 */
public class RechargeRequest {

    private BigDecimal amount;
    private int customerId;
    private BigDecimal money;
    private int type;
    private String cardNo;

    public RechargeRequest(BigDecimal amount, int customerId, BigDecimal money, int type, String cardNo) {
        this.amount = amount;
        this.customerId = customerId;
        this.money = money;
        this.type = type;
        this.cardNo = cardNo;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
