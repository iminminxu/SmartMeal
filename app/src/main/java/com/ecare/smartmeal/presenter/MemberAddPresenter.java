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
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = getJson("province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用FastJson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items.addAll(jsonBean);
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<JsonBean.CityBean> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<JsonBean.AreaBean>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCitylist().size(); c++) {//遍历该省份的所有城市
                JsonBean.CityBean CityName = jsonBean.get(i).getCitylist().get(c);
                CityList.add(CityName);//添加城市
                ArrayList<JsonBean.AreaBean> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCitylist().get(c).getArealist() == null
                        || jsonBean.get(i).getCitylist().get(c).getArealist().size() == 0) {
                    City_AreaList.add(new JsonBean.AreaBean());
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCitylist().get(c).getArealist());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
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
