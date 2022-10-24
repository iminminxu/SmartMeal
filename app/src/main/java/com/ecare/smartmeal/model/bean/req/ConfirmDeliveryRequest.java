package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/8.
 * Email: iminminxu@gmail.com
 */
public class ConfirmDeliveryRequest {

    private double latitude;
    private double longitude;
    private String orderno;

    public ConfirmDeliveryRequest(double latitude, double longitude, String orderno) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderno = orderno;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
}
