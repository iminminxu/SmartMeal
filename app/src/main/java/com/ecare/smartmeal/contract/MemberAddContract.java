package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.CustomerInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.JsonBean;

import java.util.ArrayList;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface MemberAddContract {

    interface View extends BaseView {

        void showPickerView(ArrayList<JsonBean> options1Items, ArrayList<ArrayList<JsonBean.CityBean>> options2Items, ArrayList<ArrayList<ArrayList<JsonBean.AreaBean>>> options3Items);

        void addMemberSuc();

        void findCustomerSuc(CustomerInfoResponse data);
    }

    interface Presenter extends BasePresenter<View> {

        void getAddress();

        void addMember(String idCard, String name, String mobile, String liveProvincex, String liveCityx, String liveAreasx, String liveAddrx, String cardNo);

        void findCustomer(String idCard, String mobile);
    }
}
