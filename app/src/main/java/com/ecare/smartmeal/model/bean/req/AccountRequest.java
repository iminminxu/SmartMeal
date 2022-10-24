package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/20.
 * Email: iminminxu@gmail.com
 */
public class AccountRequest {

    private int bizType;
    private int customerId;
    private int pageNum;
    private int pageSize;
    private String cardNo;

    public AccountRequest(int bizType, int customerId, int pageNum, int pageSize, String cardNo) {
        this.bizType = bizType;
        this.customerId = customerId;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.cardNo = cardNo;
    }

    public int getBizType() {
        return bizType;
    }

    public void setBizType(int bizType) {
        this.bizType = bizType;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
