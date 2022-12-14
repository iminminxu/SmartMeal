package com.ecare.smartmeal.config;

import com.ecare.smartmeal.BuildConfig;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/11.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * Constants
 */
public class Constants {

    //========== Api ==========

    public static final String API_BASE_URL = BuildConfig.DEBUG ? "http://www.ecare.ltd:4280/cloud-war/" : "http://www.ecare.ltd:4280/cloud-war/";
    public static final String API_PAY_BASE_URL = BuildConfig.DEBUG ? "https://xhky.hzxh.gov.cn/ecare-alipay/" : "https://xhky.hzxh.gov.cn/ecare-alipay/";
    public static final int API_SUCCESS = 0;

    //========== Paging ==========

    //默认分页初始页码
    public static final int PAGE_INDEX = 1;
    //默认分页加载数量
    public static final int PAGE_SIZE = 10;

    //========== Intent ==========

    public static final String IT_TYPE = "type";
    public static final String IT_SELECTED_DISHES = "selected_dishes";
    public static final String IT_ORDER_WAY = "order_way";
    public static final String IT_STATUS = "status";
    public static final String IT_SOURCE = "source";
    public static final String IT_IS_NEW = "is_new";
    public static final String IT_CUSTOMER_ID = "customer_id";
    public static final String IT_TITLE = "title";
    public static final String IT_URL = "url";
    public static final String IT_POSITION = "position";
    public static final String IT_REFUND_CARD = "refund_card";
    public static final String IT_ID_CARD = "id_card";
    public static final String IT_CARD_NO = "card_no";
    public static final String IT_BALANCE = "balance";
    public static final String IT_NEW_CARD_NO = "new_card_no";

    //========== PREFERENCE ==========

    public static final String SP_USER = "user";
    public static final String SP_TOKEN = "token";
    public static final String SP_MOBILE = "mobile";

    //========== RequestCode ==========

    public static final int REQ_CONFIRM_ORDER = 1001;
    public static final int REQ_MEMBER_INFO = 1002;
    public static final int REQ_MEMBER_RECHARGE = 1003;
    public static final int REQ_UNION_PAY_RECOGNITION = 1004;
    public static final int REQ_UNION_PAY_CONSUMPTION = 1005;

    //========== Key ==========

    public static final String KEY_ABCP_APPLICATION_APPID = "2021003169616639";
    public static final String KEY_ABCP_APPLICATION_VERSION = "1.0.0.0";
    public static final String KEY_TEMPLATE_APP_ID = "2021003168658663";
    public static final String KEY_PARTNER_ID = "2088441214796590";
    public static final String KEY_PARTNER_NAME = "杭州易护科技有限公司";
    public static final String KEY_MERCHANT_ID = "2088531881197881";
    public static final String KEY_MERCHANT_NAME = "杭州易护科技有限公司";

    //========== Url ==========

    public static final String URL_STATISTICAL_STATEMENT = "http://www.ecare.ltd:4280/wroot-ck/console/merchantLogin?userName=";
}
