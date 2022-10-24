package com.ecare.smartmeal.config;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/16.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * Application
 */
public class App extends MultiDexApplication {

    private static App instance;
    private MerchantInfoResponse merchantInfo;

    public static App getInstance() {
        return instance;
    }

    public MerchantInfoResponse getMerchantInfo() {
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfoResponse merchantInfo) {
        this.merchantInfo = merchantInfo;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化工具类
        Utils.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}