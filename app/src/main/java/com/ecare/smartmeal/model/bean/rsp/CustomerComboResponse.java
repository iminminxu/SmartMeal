package com.ecare.smartmeal.model.bean.rsp;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public class CustomerComboResponse {

    private List<DeliveryObject> deliveryList;
    private List<CustomerCombox> eatList;
    private List<CustomerCombox> leaveList;
    private List<CustomerCombox> unEatList;

    public List<DeliveryObject> getDeliveryList() {
        return deliveryList;
    }

    public void setDeliveryList(List<DeliveryObject> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public List<CustomerCombox> getEatList() {
        return eatList;
    }

    public void setEatList(List<CustomerCombox> eatList) {
        this.eatList = eatList;
    }

    public List<CustomerCombox> getLeaveList() {
        return leaveList;
    }

    public void setLeaveList(List<CustomerCombox> leaveList) {
        this.leaveList = leaveList;
    }

    public List<CustomerCombox> getUnEatList() {
        return unEatList;
    }

    public void setUnEatList(List<CustomerCombox> unEatList) {
        this.unEatList = unEatList;
    }
}
