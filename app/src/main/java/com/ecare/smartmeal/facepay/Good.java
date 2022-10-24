package com.ecare.smartmeal.facepay;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/27.
 * Email: iminminxu@gmail.com
 */
public class Good {

    private String name;
    private String price;
    private String actualPrice;
    private String finPrice;
    private String finActualPrice;
    private String number;

    public Good(String name, String price, String actualPrice, String finPrice, String finActualPrice, String number) {
        this.name = name;
        this.price = price;
        this.actualPrice = actualPrice;
        this.finPrice = finPrice;
        this.finActualPrice = finActualPrice;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(String actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getFinPrice() {
        return finPrice;
    }

    public void setFinPrice(String finPrice) {
        this.finPrice = finPrice;
    }

    public String getFinActualPrice() {
        return finActualPrice;
    }

    public void setFinActualPrice(String finActualPrice) {
        this.finActualPrice = finActualPrice;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
