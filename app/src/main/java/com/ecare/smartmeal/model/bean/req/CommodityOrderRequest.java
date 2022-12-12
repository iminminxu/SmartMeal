package com.ecare.smartmeal.model.bean.req;

import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/26.
 * Email: iminminxu@gmail.com
 */
public class CommodityOrderRequest {

    private BigDecimal amount;
    private Integer couponsId;
    private int eatWay;
    private String idCard;
    private List<CommodityxAllResponseItem> list;
    private BigDecimal ticketDiscountMoney;
    private String customerName;

    public CommodityOrderRequest(int eatWay, String idCard, List<CommodityxAllResponseItem> list) {
        this.eatWay = eatWay;
        this.idCard = idCard;
        this.list = list;
    }

    public CommodityOrderRequest(String idCard, List<CommodityxAllResponseItem> list) {
        this.idCard = idCard;
        this.list = list;
    }

    public CommodityOrderRequest(BigDecimal amount, Integer couponsId, int eatWay, String idCard, List<CommodityxAllResponseItem> list, BigDecimal ticketDiscountMoney, String customerName) {
        this.amount = amount;
        this.couponsId = couponsId;
        this.eatWay = eatWay;
        this.idCard = idCard;
        this.list = list;
        this.ticketDiscountMoney = ticketDiscountMoney;
        this.customerName = customerName;
    }

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

    public int getEatWay() {
        return eatWay;
    }

    public void setEatWay(int eatWay) {
        this.eatWay = eatWay;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public List<CommodityxAllResponseItem> getList() {
        return list;
    }

    public void setList(List<CommodityxAllResponseItem> list) {
        this.list = list;
    }

    public BigDecimal getTicketDiscountMoney() {
        return ticketDiscountMoney;
    }

    public void setTicketDiscountMoney(BigDecimal ticketDiscountMoney) {
        this.ticketDiscountMoney = ticketDiscountMoney;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
