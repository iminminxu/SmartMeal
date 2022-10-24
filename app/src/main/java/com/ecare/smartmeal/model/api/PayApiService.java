package com.ecare.smartmeal.model.api;

import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.GetIdentityRequest;
import com.ecare.smartmeal.model.bean.req.MerchantInfoRequest;
import com.ecare.smartmeal.model.bean.req.PayReq;
import com.ecare.smartmeal.model.bean.rsp.GetIdentityResponse;
import com.ecare.smartmeal.model.bean.rsp.MerchantInfoResponse;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/11.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * ProjectApiService
 */
public interface PayApiService {

    @POST("ecare/alipay/sn/info")
    Flowable<BaseResponse<MerchantInfoResponse>> getMerchantInfo(@Body MerchantInfoRequest merchantInfoRequest);

    @POST("face/face/token")
    Flowable<BaseResponse<String>> getAuthToken(@Body String metaInfo);

    @POST("face/get/identity")
    Flowable<BaseResponse<GetIdentityResponse>> getIdentity(@Body GetIdentityRequest getIdentityRequest);

    @POST("face/trade/pay")
    Flowable<BaseResponse<Boolean>> pay(@Body PayReq payReq);
}
