package com.ecare.smartmeal.presenter;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ecare.smartmeal.base.RxPresenter;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.contract.MemberAddContract;
import com.ecare.smartmeal.model.api.ProjectApi;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.CustomerRequest;
import com.ecare.smartmeal.model.bean.rsp.CustomerInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.JsonBean;
import com.ecare.smartmeal.model.rxjava.CommonSubscriber;
import com.ecare.smartmeal.model.rxjava.RxUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/18.
 * Email: iminminxu@gmail.com
 */
public class MemberAddPresenter extends RxPresenter<MemberAddContract.View> implements MemberAddContract.Presenter {

    @Override
    public void getAddress() {
        ArrayList<JsonBean> options1Items = new ArrayList<>();
        ArrayList<ArrayList<JsonBean.CityBean>> options2Items = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<JsonBean.AreaBean>>> options3Items = new ArrayList<>();
        Observable.create(emitter -> {
            initCityJson(options1Items, options2Items, options3Items);
            emitter.onNext(1);
            emitter.onComplete();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (isAttachView()) {
                        mView.showPickerView(options1Items, options2Items, options3Items);
                    }
                });
    }

    private void initCityJson(ArrayList<JsonBean> options1Items, ArrayList<ArrayList<JsonBean.CityBean>> options2Items, ArrayList<ArrayList<ArrayList<JsonBean.AreaBean>>> options3Items) {
        /**
         * ?????????assets ????????????Json??????????????????????????????????????????????????????
         * ???????????????????????????
         *
         * */
        String JsonData = getJson("province.json");//??????assets????????????json????????????
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//???FastJson ????????????
        /**
         * ??????????????????
         *
         * ???????????????????????????JavaBean????????????????????????????????? IPickerViewData ?????????
         * PickerView?????????getPickerViewText????????????????????????????????????
         */
        options1Items.addAll(jsonBean);
        for (int i = 0; i < jsonBean.size(); i++) {//????????????
            ArrayList<JsonBean.CityBean> CityList = new ArrayList<>();//????????????????????????????????????
            ArrayList<ArrayList<JsonBean.AreaBean>> Province_AreaList = new ArrayList<>();//??????????????????????????????????????????
            for (int c = 0; c < jsonBean.get(i).getCitylist().size(); c++) {//??????????????????????????????
                JsonBean.CityBean CityName = jsonBean.get(i).getCitylist().get(c);
                CityList.add(CityName);//????????????
                ArrayList<JsonBean.AreaBean> City_AreaList = new ArrayList<>();//??????????????????????????????
                //??????????????????????????????????????????????????????????????????null ?????????????????????????????????????????????
                if (jsonBean.get(i).getCitylist().get(c).getArealist() == null
                        || jsonBean.get(i).getCitylist().get(c).getArealist().size() == 0) {
                    City_AreaList.add(new JsonBean.AreaBean());
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCitylist().get(c).getArealist());
                }
                Province_AreaList.add(City_AreaList);//??????????????????????????????
            }
            /**
             * ??????????????????
             */
            options2Items.add(CityList);
            /**
             * ??????????????????
             */
            options3Items.add(Province_AreaList);
        }
    }

    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = App.getInstance().getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = JSONArray.parseArray(result);
            for (int i = 0; i < data.size(); i++)
                detail.add(JSONObject.parseObject(data.get(i).toString(), JsonBean.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    @Override
    public void addMember(String idCard, String name, String mobile, String liveProvincex, String liveCityx, String liveAreasx, String liveAddrx, String cardNo) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .addMember(new CustomerRequest(cardNo, idCard, liveAddrx, liveAreasx, liveCityx, liveProvincex, mobile, name))
                .compose(RxUtils.<BaseResponse<String>>rxSchedulerHelper())
                .compose(RxUtils.<String>handleResult())
                .subscribeWith(new CommonSubscriber<String>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(String data) {
                        if (isAttachView()) {
                            mView.addMemberSuc();
                        }
                    }
                })
        );
    }

    @Override
    public void findCustomer(String idCard, String mobile) {
        if (!isAttachView()) {
            return;
        }
        mView.showLoadingDialog();
        addSubscribe(ProjectApi.getInstance().getApiService()
                .findCustomer(new CustomerRequest("", idCard, "", "", "", "", mobile, ""))
                .compose(RxUtils.<BaseResponse<CustomerInfoResponse>>rxSchedulerHelper())
                .compose(RxUtils.<CustomerInfoResponse>handleResult())
                .subscribeWith(new CommonSubscriber<CustomerInfoResponse>(mView, CommonSubscriber.SHOW_LOADING_DIALOG) {
                    @Override
                    public void onNext(CustomerInfoResponse data) {
                        if (isAttachView()) {
                            mView.findCustomerSuc(data);
                        }
                    }
                })
        );
    }
}
