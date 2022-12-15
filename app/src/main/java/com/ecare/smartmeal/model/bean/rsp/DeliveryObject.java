package com.ecare.smartmeal.model.bean.rsp;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/12/14.
 * Email: iminminxu@gmail.com
 */
public class DeliveryObject {

    private String name;
    private List<CustomerCombox> list;
    private boolean check;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CustomerCombox> getList() {
        return list;
    }

    public void setList(List<CustomerCombox> list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
