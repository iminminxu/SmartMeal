package com.ecare.smartmeal.model.bean.rsp;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/11/28.
 * Email: iminminxu@gmail.com
 */
public class UnionPayRspDTO {

    private String code;
    private String message;
    private String cardNumber;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
