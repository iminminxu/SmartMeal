package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/16.
 * Email: iminminxu@gmail.com
 */
public class CustomerListRequest {

    private String beginTime;
    private String cardNo;
    private String endTime;
    private String idCard;
    private String mobile;
    private String name;
    private int pageNum;
    private int pageSize;

    public CustomerListRequest(String beginTime, String cardNo, String endTime, String idCard, String mobile, String name, int pageNum, int pageSize) {
        this.beginTime = beginTime;
        this.cardNo = cardNo;
        this.endTime = endTime;
        this.idCard = idCard;
        this.mobile = mobile;
        this.name = name;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
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
}
