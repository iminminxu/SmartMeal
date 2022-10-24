package com.ecare.smartmeal.model.bean.rsp;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/23.
 * Email: iminminxu@gmail.com
 */
public class CommodityCombUnitVo {

    private String singleName;
    private int userNum;
    private List<CommodityItem> list;

    public String getSingleName() {
        return singleName;
    }

    public void setSingleName(String singleName) {
        this.singleName = singleName;
    }

    public int getUserNum() {
        return userNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public List<CommodityItem> getList() {
        return list;
    }

    public void setList(List<CommodityItem> list) {
        this.list = list;
    }
}
