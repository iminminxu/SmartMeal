package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/27.
 * Email: iminminxu@gmail.com
 */
public class PayReq {

    private String subject;
    private String totalAmount;
    private String ftoken;
    private String outTradeNo;
    private String sellerId;
    private String uid;
    private String terminalParams;

    public PayReq(String subject, String totalAmount, String ftoken, String outTradeNo, String sellerId, String uid, String terminalParams) {
        this.subject = subject;
        this.totalAmount = totalAmount;
        this.ftoken = ftoken;
        this.outTradeNo = outTradeNo;
        this.sellerId = sellerId;
        this.uid = uid;
        this.terminalParams = terminalParams;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFtoken() {
        return ftoken;
    }

    public void setFtoken(String ftoken) {
        this.ftoken = ftoken;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTerminalParams() {
        return terminalParams;
    }

    public void setTerminalParams(String terminalParams) {
        this.terminalParams = terminalParams;
    }
}
