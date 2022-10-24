package com.ecare.smartmeal.contract;

import com.ecare.smartmeal.base.BasePresenter;
import com.ecare.smartmeal.base.BaseView;
import com.ecare.smartmeal.model.bean.rsp.CommodityCombUnitVo;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponse;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public interface HomeCashierContract {

    interface View extends BaseView {

        void setAllCommodity(List<CommodityxAllResponse> data);

        void setCommodityUnit(int commodityId, List<CommodityCombUnitVo> data);
    }

    interface Presenter extends BasePresenter<View> {

        void getAllCommodity();

        void getCommodityUnit(int commodityId, int merchantId);
    }
}
