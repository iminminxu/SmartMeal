package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/17.
 * Email: iminminxu@gmail.com
 */
public class CustomerRequest {

    private String cardNo;
    private String idCard;
    private String liveAddrx;
    private String liveAreasx;
    private String liveCityx;
    private String liveProvincex;
    private String mobile;
    private String name;

    public CustomerRequest(String cardNo, String idCard, String liveAddrx, String liveAreasx, String liveCityx, String liveProvincex, String mobile, String name) {
        this.cardNo = cardNo;
        this.idCard = idCard;
        this.liveAddrx = liveAddrx;
        this.liveAreasx = liveAreasx;
        this.liveCityx = liveCityx;
        this.liveProvincex = liveProvincex;
        this.mobile = mobile;
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLiveAddrx() {
        return liveAddrx;
    }

    public void setLiveAddrx(String liveAddrx) {
        this.liveAddrx = liveAddrx;
    }

    public String getLiveAreasx() {
        return liveAreasx;
    }

    public void setLiveAreasx(String liveAreasx) {
        this.liveAreasx = liveAreasx;
    }

    public String getLiveCityx() {
        return liveCityx;
    }

    public void setLiveCityx(String liveCityx) {
        this.liveCityx = liveCityx;
    }

    public String getLiveProvincex() {
        return liveProvincex;
    }

    public void setLiveProvincex(String liveProvincex) {
        this.liveProvincex = liveProvincex;
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
}
