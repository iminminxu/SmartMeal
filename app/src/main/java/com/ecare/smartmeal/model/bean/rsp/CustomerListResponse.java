package com.ecare.smartmeal.model.bean.rsp;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/16.
 * Email: iminminxu@gmail.com
 */
public class CustomerListResponse {

    private int id;
    private BigDecimal balance;
    private String cardNo;
    private String createTime;
    private String idCard;
    private String liveAddrx;
    private String mobile;
    private String name;
    private BigDecimal totalAmount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLiveAddrx() {
        return liveAddrx;
    }

    public void setLiveAddrx(String liveAddrx) {
        this.liveAddrx = liveAddrx;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
