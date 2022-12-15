package com.ecare.smartmeal.model.bean.req;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public class CustomerComboDeliveryRequest {

    private List<Integer> comboIdList;

    public CustomerComboDeliveryRequest(List<Integer> comboIdList) {
        this.comboIdList = comboIdList;
    }

    public List<Integer> getComboIdList() {
        return comboIdList;
    }

    public void setComboIdList(List<Integer> comboIdList) {
        this.comboIdList = comboIdList;
    }
}
