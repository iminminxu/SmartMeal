package com.ecare.smartmeal.widget;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ecare.smartmeal.R;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.model.bean.rsp.JsonBean;
import com.lxj.xpopup.core.CenterPopupView;

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
 * Created by xuminmin on 2022/6/7.
 * Email: iminminxu@gmail.com
 */
public class ImproveInfoPopup extends CenterPopupView {

    //上下文
    private Context mContext;
    //手机号
    private String mMobile;
    //完善信息控件
    private EditText etName;
    private EditText etIdCard;
    private TextView tvMobile;
    private TextView tvAddress;
    private EditText etAddressDetail;
    //选择地区弹框
    private OptionsPickerView mPickerView;
    //选择的省市区
    private String mProvince;
    private String mCity;
    private String mDistrict;
    //完善信息回调
    private OnImproveInfoListener mListener;

    public ImproveInfoPopup(@NonNull Context context, String mobile) {
        super(context);
        mContext = context;
        mMobile = mobile;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.pop_improve_info;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        etName = findViewById(R.id.et_name);
        etIdCard = findViewById(R.id.et_id_card);
        tvMobile = findViewById(R.id.tv_mobile);
        tvMobile.setText(mMobile);
        tvAddress = findViewById(R.id.tv_address);
        etAddressDetail = findViewById(R.id.et_address_detail);
        tvAddress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(v);
                if (mPickerView == null) {
                    getAddress();
                } else {
                    mPickerView.show();
                }
            }
        });
        findViewById(R.id.tv_save).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etName.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    ToastUtils.showShort("请输入老人姓名");
                    return;
                }
                String iDCard = etIdCard.getText().toString();
                if (!RegexUtils.isIDCard18(iDCard)) {
                    ToastUtils.showShort("请输入正确的身份证号");
                    return;
                }
                String address = tvAddress.getText().toString();
                if (StringUtils.isEmpty(address)) {
                    ToastUtils.showShort("请选择省市区");
                    return;
                }
                String addressDetail = etAddressDetail.getText().toString();
                if (StringUtils.isEmpty(addressDetail)) {
                    ToastUtils.showShort("请输入详细地址");
                    return;
                }
                if (mListener != null) {
                    mListener.onImproveInfo(name, iDCard, mMobile, mProvince, mCity, mDistrict, addressDetail);
                }
                dismiss();
            }
        });
        findViewById(R.id.tv_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

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
                    showPickerView(options1Items, options2Items, options3Items);
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

    public void showPickerView(ArrayList<JsonBean> options1Items, ArrayList<ArrayList<JsonBean.CityBean>> options2Items, ArrayList<ArrayList<ArrayList<JsonBean.AreaBean>>> options3Items) {
        mPickerView = new OptionsPickerBuilder(mContext, (options1, options2, options3, v) -> {
            mProvince = options1Items.get(options1).getName();
            mCity = options2Items.get(options1).get(options2).getName();
            mDistrict = options3Items.get(options1).get(options2).get(options3).getName();
            tvAddress.setText(mProvince + mCity + mDistrict);
        })
                .setTitleText("请选择省市区")
                .setDecorView(getWindowDecorView().findViewById(android.R.id.content))
                .build();
        mPickerView.setPicker(options1Items, options2Items, options3Items);
        mPickerView.show();
    }

    public interface OnImproveInfoListener {
        void onImproveInfo(String name, String iDCard, String mobile, String province, String city, String district, String addressDetail);
    }

    public ImproveInfoPopup setOnImproveInfoListener(OnImproveInfoListener listener) {
        this.mListener = listener;
        return this;
    }
}
