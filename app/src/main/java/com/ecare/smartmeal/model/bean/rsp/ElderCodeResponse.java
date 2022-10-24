package com.ecare.smartmeal.model.bean.rsp;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/26.
 * Email: iminminxu@gmail.com
 */
public class ElderCodeResponse {

    private int age;
    private String areaCode;
    private String areaName;
    private int customerId;
    private String elderCode;
    private String idCard;
    private String name;
    private String nation;
    private int sex;
    private int hasAuthentication;
    private String cardNo;
    private BigDecimal balance;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getHasAuthentication() {
        return hasAuthentication;
    }

    public void setHasAuthentication(int hasAuthentication) {
        this.hasAuthentication = hasAuthentication;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
