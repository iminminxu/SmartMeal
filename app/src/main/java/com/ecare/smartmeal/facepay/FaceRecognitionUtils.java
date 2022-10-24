package com.ecare.smartmeal.facepay;

import com.alipay.iot.sdk.APIManager;
import com.alipay.zoloz.smile2pay.InstallCallback;
import com.alipay.zoloz.smile2pay.MetaInfoCallback;
import com.alipay.zoloz.smile2pay.Zoloz;
import com.alipay.zoloz.smile2pay.ZolozConfig;
import com.alipay.zoloz.smile2pay.ZolozConstants;
import com.alipay.zoloz.smile2pay.verify.Smile2PayResponse;
import com.alipay.zoloz.smile2pay.verify.VerifyCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.config.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/2.
 * Email: iminminxu@gmail.com
 */
public class FaceRecognitionUtils {

    /**
     * 获取设备信息
     *
     * @param merchantId   商户id
     * @param merchantName 商户名称
     * @param callback     回调
     */
    public static void getMetaInfo(String merchantId, String merchantName, MetaInfoCallback callback) {
        Zoloz zoloz = Zoloz.getInstance(App.getInstance());
        Map<String, Object> merchantInfo = new HashMap<>();
        // 以下信息请根据真实情况填写
        // 必填项，商户id(比如：学校：外标，企业：机构代码)
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_MERCHANT_ID, merchantId);
        // 必填项，商户name(比如：学校/企业食堂)
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_MERCHANT_NAME, merchantName);
        // 必填项，支付宝开发平台分配的pid，为isv的pid
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_PARTNER_ID, Constants.KEY_PARTNER_ID);
        // 必填项，ISV 名称
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_ISV_NAME, Constants.KEY_PARTNER_NAME);
        // 必填项，添加刷脸付功能的appid，学校应用的appId
        merchantInfo.put(ZolozConfig.MERCHANT_APP_ID, Constants.KEY_TEMPLATE_APP_ID);
        // 必填项，逻辑库ID
        merchantInfo.put(ZolozConstants.KEY_LOGIC_GROUP_ID, "business_" + merchantId + "_01");
        // 必填项，行业类型
        merchantInfo.put(ZolozConstants.KEY_INDUSTRY_BIZ_TYPE, "business");
        // 必填项，商户机具终端编号
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_DEVICE_NUM, APIManager.getInstance().getDeviceAPI().getDeviceSn());
        zoloz.getMetaInfo(merchantInfo, callback);
    }

    /**
     * 人脸识别初始化
     *
     * @param merchantId   商户id
     * @param merchantName 商户名称
     * @param authToken    回调
     */
    public static void init(String merchantId, String merchantName, String authToken) {
        Zoloz zoloz = Zoloz.getInstance(App.getInstance());
        Map<String, Object> merchantInfo = new HashMap<>();
        //以下信息请根据真实情况填写
        //必填项，商户id(比如：学校：外标，企业：机构代码，景区：机构代码)
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_MERCHANT_ID, merchantId);
        // 必填项，商户name(比如：学校/企业食堂/景区名称)
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_MERCHANT_NAME, merchantName);
        // 必填项，支付宝开发平台分配的pid，为isv的pid
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_PARTNER_ID, Constants.KEY_PARTNER_ID);
        // 必填项，ISV 名称
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_ISV_NAME, Constants.KEY_PARTNER_NAME);
        // 必填项，添加刷脸付功能的appid，学校应用的appId
        merchantInfo.put(ZolozConfig.MERCHANT_APP_ID, Constants.KEY_TEMPLATE_APP_ID);
        // 必填项，逻辑库ID，企业/高校
        merchantInfo.put(ZolozConstants.KEY_LOGIC_GROUP_ID, "business_" + merchantId + "_01");
        // 必填项，行业类型
        merchantInfo.put(ZolozConstants.KEY_INDUSTRY_BIZ_TYPE, "business");
        // 必填项，授权token
        merchantInfo.put(ZolozConstants.KEY_AUTH_TOKEN, authToken);
        // 必填项，商户机具终端编号
        merchantInfo.put(ZolozConstants.KEY_MERCHANT_INFO_DEVICE_NUM, APIManager.getInstance().getDeviceAPI().getDeviceSn());
        // 选填项，是否支持离线支付 : true -> 支持， false -> 不支持
        merchantInfo.put(ZolozConstants.KEY_ENABLE_OFF_FTOKEN, true);
        zoloz.install(merchantInfo);
        zoloz.register(null, new InstallCallback() {
            @Override
            public void onResponse(Smile2PayResponse smile2PayResponse) {
                if (smile2PayResponse.getCode() != Smile2PayResponse.CODE_SUCCESS) {
                    ToastUtils.showShort("人脸识别初始化失败");
                }
            }
        });
    }

    /**
     * 唤起刷脸
     *
     * @param callback 回调
     */
    public static void speedyVerify(VerifyCallback callback) {
        Zoloz zoloz = Zoloz.getInstance(App.getInstance());
        Map<String, Object> merchantInfo = new HashMap<>();
        //支付：pay  通用核身：auth
        merchantInfo.put(ZolozConfig.SERVICE_ID, "pay");
        // 必填项，添加刷脸付功能的appid，学校应用的appId
        merchantInfo.put(ZolozConfig.MERCHANT_APP_ID, Constants.KEY_TEMPLATE_APP_ID);
        // 设置为“拍照付”模式（Smile5.1.2版本开始支持）
        int captureUIMode = ZolozConfig.CaptureUIMode.CLICK;
        merchantInfo.put(ZolozConfig.KEY_CAPTURE_UI_MODE, captureUIMode);
        merchantInfo.put(ZolozConfig.KEY_SMILE_MODE, ZolozConfig.SmileMode.SMILE_MODE_EXT_DISPLAY);
        // 拍照付页面倒计时超时时间（单位为秒）
        merchantInfo.put("expireTime", "30");
        zoloz.speedyVerify(merchantInfo, callback);
    }
}
