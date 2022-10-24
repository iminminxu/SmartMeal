package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/26.
 * Email: iminminxu@gmail.com
 */
public class ElderAuthRequest {

    private String cardNo;
    private String elderCode;
    private String idCard;
    private String mobile;

    public ElderAuthRequest(String cardNo, String elderCode, String idCard, String mobile) {
        this.cardNo = cardNo;
        this.elderCode = elderCode;
        this.idCard = idCard;
        this.mobile = mobile;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getElderCode() {
        return elderCode;
    }

    public void setElderCode(String elderCode) {
        this.elderCode = elderCode;
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
}
