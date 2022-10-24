package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/7/13.
 * Email: iminminxu@gmail.com
 */
public class CustomerxDTO {

    private int customerId;
    private String idCard;
    private String cardNo;
    private String oldCardNo;

    public CustomerxDTO(int customerId, String idCard, String cardNo, String oldCardNo) {
        this.customerId = customerId;
        this.idCard = idCard;
        this.cardNo = cardNo;
        this.oldCardNo = oldCardNo;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getOldCardNo() {
        return oldCardNo;
    }

    public void setOldCardNo(String oldCardNo) {
        this.oldCardNo = oldCardNo;
    }
}
