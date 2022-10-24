package com.ecare.smartmeal.facepay;

import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.iot.bpaas.api.BPaaSApi;
import com.alipay.iot.bpaas.api.BPaaSInitCallback;
import com.alipay.iot.bpaas.api.service.BPaaSCallback;
import com.blankj.utilcode.util.ToastUtils;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.config.Constants;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;
import com.ecare.smartmeal.utils.NumUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/27.
 * Email: iminminxu@gmail.com
 */
public class FacePayUtils {

    /**
     * 人脸付初始化
     *
     * @param merchantId   商户id
     * @param merchantName 商户名称
     */
    public static void init(String merchantId, String merchantName) {
        Bundle extInfo = new Bundle();
        extInfo.putString("ext_tpl_app_id", Constants.KEY_TEMPLATE_APP_ID);
        BPaaSApi.getInstance().init(App.getInstance(), Constants.KEY_ABCP_APPLICATION_APPID, Constants.KEY_ABCP_APPLICATION_VERSION, extInfo, new BPaaSInitCallback() {
            @Override
            public void onSuccess() {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("logicGroupID", "business_" + merchantId + "_01");
                jsonObject.put("industryBizType", "business");
                jsonObject.put("partnerId", Constants.KEY_PARTNER_ID);
                jsonObject.put("merchantId", merchantId);
                jsonObject.put("merchantName", merchantName);
                jsonObject.put("isvName", Constants.KEY_PARTNER_NAME);
                jsonObject.put("useOfflinePay", true);
                jsonObject.put("uploadTimeOutSecs", 30);
                Bundle bundle = new Bundle();
                bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
                bundle.putString("operateType", "init");
                bundle.putString("page", "canteen");
                bundle.putString("templateParams", jsonObject.toJSONString());
                BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, null);
            }

            @Override
            public void onFail(String subCode) {
                ToastUtils.showShort("人脸付初始化失败");
            }
        });
    }

    /**
     * 显示关闭SKU信息
     *
     * @param data          已点菜品
     * @param totalDiscount 优惠金额
     * @param totalAmount   合计金额
     * @param actualAmount  应付金额
     * @param callback      回调
     */
    public static void showOrHideSKU(List<CommodityxAllResponseItem> data, BigDecimal totalDiscount, BigDecimal totalAmount, BigDecimal actualAmount, BPaaSCallback callback) {
        if (data == null || data.size() == 0) {
            //隐藏
            Bundle bundle = new Bundle();
            bundle.putString("page", "sku");
            bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
            bundle.putString("operateType", "close");
            BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, callback);
        } else {
            //显示
            List<Good> list = new ArrayList<>();
            for (CommodityxAllResponseItem item : data) {
                if (item == null) {
                    continue;
                }
                BigDecimal price = item.getPrice();
                int num = item.getNum();
                BigDecimal finPrice = price.multiply(new BigDecimal(String.valueOf(num)));
                list.add(new Good(item.getName(), NumUtils.parseAmount(price), NumUtils.parseAmount(price),
                        NumUtils.parseAmount(finPrice), NumUtils.parseAmount(finPrice), "x" + num));
            }
            Trade trade = new Trade(NumUtils.parseAmount(totalAmount), NumUtils.parseAmount(actualAmount), NumUtils.parseAmount(totalDiscount));
            Bundle bundle = new Bundle();
            bundle.putString("page", "sku");
            bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
            bundle.putString("operateType", "show");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("goods", JSON.toJSON(list));
            jsonObject.put("trade", JSON.toJSON(trade));
            bundle.putString("templateParams", jsonObject.toString());
            BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, callback);
        }
    }

    /**
     * 启动刷脸
     *
     * @param callback 回调
     */
    public static void startScanFace(BigDecimal tradeAmount, BPaaSCallback callback) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tradeAmount", NumUtils.parsePrintAmount(tradeAmount));
        Bundle bundle = new Bundle();
        bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
        bundle.putString("operateType", "startVerify");
        bundle.putString("page", "faceVerify");
        bundle.putString("templateParams", jsonObject.toJSONString());
        BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, callback);
    }

    /**
     * 退出刷脸
     *
     * @param sessionId 最近一次刷脸调用返回的sessionId
     * @param callback  回调
     */
    public static void closeScanFace(String sessionId, BPaaSCallback callback) {
        Bundle bundle = new Bundle();
        bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
        bundle.putString("operateType", "close");
        bundle.putString("page", "faceVerify");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", sessionId);
        bundle.putString("templateParams", jsonObject.toJSONString());
        BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, callback);
    }

    /**
     * 展示刷脸支付结果页
     *
     * @param ftoken        支付结果，true表示支付成功，false为支付失败
     * @param sessionId     会话id
     * @param tradeNo       订单id
     * @param totalDiscount 优惠金额
     * @param totalAmount   合计金额
     * @param actualAmount  应付金额
     * @param callback      回调
     */
    public static void goOfflineFacePayResult(boolean success, String ftoken, String sessionId, String tradeNo, BigDecimal totalDiscount, BigDecimal totalAmount, BigDecimal actualAmount, BPaaSCallback callback) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", success); // 如果支付失败传false
        jsonObject.put("sessionId", sessionId);
        jsonObject.put("tradeNo", tradeNo);
        jsonObject.put("ftoken", ftoken);
        jsonObject.put("title", success ? "支付成功" : "支付失败"); // 支付失败可传"支付失败"
        jsonObject.put("totalAmount", NumUtils.parseAmount(totalAmount));
        jsonObject.put("actualAmount", NumUtils.parseAmount(actualAmount));
        jsonObject.put("changeAmount", "¥0.00");
        jsonObject.put("resultTimeOut", 3); //支付结果页倒计时时间，单位s
        jsonObject.put("subCode", "10000"); //自定义支付结果码，支付失败时必填
        jsonObject.put("subMsg", success ? "支付成功" : "支付失败"); //自定义支付结果的message，支付失败时必填
        JSONObject discount1 = new JSONObject();
        discount1.put("name", "优惠");
        discount1.put("amount", NumUtils.parseAmount(totalDiscount));
        JSONArray discountList = new JSONArray();
        discountList.add(discount1);
        jsonObject.put("discountList", discountList.toJSONString());
        Bundle bundle = new Bundle();
        bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
        bundle.putString("operateType", "showScanFaceOfflinePayResult");
        bundle.putString("page", "payResult");
        bundle.putString("templateParams", jsonObject.toJSONString());
        BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, callback);
    }

    /**
     * 关闭支付结果页
     *
     * @param sessionId 与调用结果页接口传入的sessionId必须一致
     * @param callback  回调
     */
    public static void closePayResult(String sessionId, BPaaSCallback callback) {
        JSONObject params = new JSONObject();
        params.put("sessionId", sessionId);
        Bundle bundle = new Bundle();
        bundle.putString("templateId", Constants.KEY_TEMPLATE_APP_ID);
        bundle.putString("operateType", "close");
        bundle.putString("page", "payResult");
        bundle.putString("templateParams", params.toJSONString());
        BPaaSApi.getInstance().startBPaaSService(Constants.KEY_ABCP_APPLICATION_APPID, "BPaaSTemplateChannel", bundle, callback);
    }
}
