package com.ecare.smartmeal.model.bean.rsp;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/17.
 * Email: iminminxu@gmail.com
 */
public class JsonBean implements IPickerViewData {

    /**
     * name : 省份
     * citylist : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
     */
    private int code;
    private String name;
    private List<CityBean> citylist;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<CityBean> getCitylist() {
        return citylist;
    }

    public void setCitylist(List<CityBean> citylist) {
        this.citylist = citylist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // 实现 IPickerViewData 接口，
    // 这个用来显示在PickerView上面的字符串，
    // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
    @Override
    public String getPickerViewText() {
        return this.name;
    }

    public static class CityBean implements IPickerViewData {

        /**
         * name : 城市
         * area : ["东城区","西城区","崇文区","昌平区"]
         */
        private int code;
        private String name;
        private List<AreaBean> arealist;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public List<AreaBean> getArealist() {
            return arealist;
        }

        public void setArealist(List<AreaBean> arealist) {
            this.arealist = arealist;
        }

        @Override
        public String getPickerViewText() {
            return this.name;
        }
    }

    public static class AreaBean implements IPickerViewData {

        private int code;
        private String name;

        public AreaBean() {
            code = -1;
            name = "暂无详细地区";
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRealName() {
            return name.equals("暂无详细地区") ? "" : name;
        }

        @Override
        public String getPickerViewText() {
            return this.name;
        }
    }
}
