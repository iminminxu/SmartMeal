package com.ecare.smartmeal.presenter;

import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.contract.HomeCashierContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityCombUnitVo;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponse;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class HomeCashierPresenter extends RxPresenter<HomeCashierContract.View> implements HomeCashierContract.Presenter {

    @Override
    public void getAllCommodity() {
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getAllCommodity()
                .compose(RxUtils.<BaseResponse<List<CommodityxAllResponse>>>rxSchedulerHelper())
                .compose(RxUtils.<List<CommodityxAllResponse>>handleResult())
                .subscribeWith(new CommonSubscriber<List<CommodityxAllResponse>>(mView, CommonSubscriber.SHOW_STATE) {
                    @Override
                    public void onNext(List<CommodityxAllResponse> data) {
                        if (isAttachView()) {
                            mView.stateMain();
                            mView.setAllCommodity(data);
                        }
                    }
                })
        );
    }

    @Override
    public void getCommodityUnit(int commodityId, int merchantId) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .getCommodityUnit(commodityId, merchantId)
                .compose(RxUtils.<BaseResponse<List<CommodityCombUnitVo>>>rxSchedulerHelper())
                .compose(RxUtils.<List<CommodityCombUnitVo>>handleResult())
                .subscribeWith(new CommonSubscriber<List<CommodityCombUnitVo>>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(List<CommodityCombUnitVo> data) {
                        if (isAttachView()) {
                            mView.setCommodityUnit(commodityId, data);
                        }
                    }
                })
        );
    }
}
