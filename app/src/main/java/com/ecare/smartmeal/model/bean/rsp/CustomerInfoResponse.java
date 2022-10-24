package com.ecare.smartmeal.model.bean.rsp;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/10/11.
 * Email: iminminxu@gmail.com
 */
public class CustomerInfoResponse {

    private String idCard;
    private String name;
    private String mobile;
    private String liveAddrx;
    private String liveAreasx;
    private String liveCityx;
    private String liveProvincex;

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}
