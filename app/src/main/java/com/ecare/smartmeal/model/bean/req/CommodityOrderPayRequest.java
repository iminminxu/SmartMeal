package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/31.
 * Email: iminminxu@gmail.com
 */
public class CommodityOrderPayRequest {

    private int customerId;
    private int orderId;
    private int payWay;
    private String terminalCode;
    private String cardNo;

    public CommodityOrderPayRequest(int customerId, int orderId, int payWay, String terminalCode, String cardNo) {
        this.customerId = customerId;
        this.orderId = orderId;
        this.payWay = payWay;
        this.terminalCode = terminalCode;
        this.cardNo = cardNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public String getTerminalCode() {
        return terminalCode;
    }

    public void setTerminalCode(String terminalCode) {
        this.terminalCode = terminalCode;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
}
