package com.ecare.smartmeal.model.bean.rsp;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/23.
 * Email: iminminxu@gmail.com
 */
public class CommodityxAllResponse {

    private String typeName;
    private List<CommodityItem> list;
    private boolean check;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<CommodityItem> getList() {
        return list;
    }

    public void setList(List<CommodityItem> list) {
        this.list = list;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
