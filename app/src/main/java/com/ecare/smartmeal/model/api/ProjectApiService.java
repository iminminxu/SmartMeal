package com.ecare.smartmeal.model.api;

import com.ecare.smartmeal.model.bean.BasePaging;
import com.ecare.smartmeal.model.bean.BaseResponse;
import com.ecare.smartmeal.model.bean.req.AccountRequest;
import com.ecare.smartmeal.model.bean.req.AllOrderRequest;
import com.ecare.smartmeal.model.bean.req.CommodityOrderPayRequest;
import com.ecare.smartmeal.model.bean.req.CommodityOrderRequest;
import com.ecare.smartmeal.model.bean.req.ConfirmDeliveryRequest;
import com.ecare.smartmeal.model.bean.req.CustomerInfoRequest;
import com.ecare.smartmeal.model.bean.req.CustomerListRequest;
import com.ecare.smartmeal.model.bean.req.CustomerRequest;
import com.ecare.smartmeal.model.bean.req.CustomerxDTO;
import com.ecare.smartmeal.model.bean.req.ElderAuthRequest;
import com.ecare.smartmeal.model.bean.req.LoginReqDTO;
import com.ecare.smartmeal.model.bean.req.NewOrderRequest;
import com.ecare.smartmeal.model.bean.req.OrderListRequest;
import com.ecare.smartmeal.model.bean.req.RechargeRequest;
import com.ecare.smartmeal.model.bean.req.RefundCardRequest;
import com.ecare.smartmeal.model.bean.req.RefundOrderRequest;
import com.ecare.smartmeal.model.bean.req.RegisterReqDTO;
import com.ecare.smartmeal.model.bean.rsp.CommodityCombUnitVo;
import com.ecare.smartmeal.model.bean.rsp.CommodityOrderPayResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponse;
import com.ecare.smartmeal.model.bean.rsp.CustomerBalanceRecordx;
import com.ecare.smartmeal.model.bean.rsp.CustomerInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.bean.rsp.ElderCodeResponse;
import com.ecare.smartmeal.model.bean.rsp.LoginRspDTO;
import com.ecare.smartmeal.model.bean.rsp.OrderCountResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderInfoResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderListResponse;
import com.ecare.smartmeal.model.bean.rsp.PrintTicketResponse;
import com.ecare.smartmeal.model.bean.rsp.RiderNewOrdersResponse;

import java.util.List;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * AndroidProject
 * <p>
 * Created by xuminmin on 2018/12/11.
 * Email: iminminxu@gmail.com
 * Copyright © 2018年 Hangzhou Gravity Cyberinfo. All rights reserved.
 * ProjectApiService
 */
public interface ProjectApiService {

    @POST("login")
    Flowable<BaseResponse<LoginRspDTO>> login(@Body LoginReqDTO loginReqDTO);

    @GET("smsCode")
    Flowable<BaseResponse<String>> getSmsCode(@Query("mobile") String mobile, @Query("type") int type);

    @POST("register")
    Flowable<BaseResponse<String>> register(@Body RegisterReqDTO registerReqDTO);

    @POST("changePassword")
    Flowable<BaseResponse<String>> retrievePassword(@Body RegisterReqDTO registerReqDTO);

    @POST("commodity/listAll")
    Flowable<BaseResponse<List<CommodityxAllResponse>>> getAllCommodity();

    @GET("commodity/getCommodityUnit")
    Flowable<BaseResponse<List<CommodityCombUnitVo>>> getCommodityUnit(@Query("commodityId") int commodityId, @Query("merchantId") int merchantId);

    @POST("order/getOrderInfo")
    Flowable<BaseResponse<OrderInfoResponse>> getOrderInfo(@Body CommodityOrderRequest commodityOrderRequest);

    @POST("account/elderInfo")
    Flowable<BaseResponse<ElderCodeResponse>> getElderInfo(@Body ElderAuthRequest elderAuthRequest);

    @POST("order/checkTaboos")
    Flowable<BaseResponse<CommodityOrderRequest>> checkTaboos(@Body CommodityOrderRequest commodityOrderRequest);

    @POST("order/createOrder")
    Flowable<BaseResponse<Integer>> createOrder(@Body CommodityOrderRequest commodityOrderRequest);

    @POST("order/payOrder")
    Flowable<BaseResponse<CommodityOrderPayResponse>> payOrder(@Body CommodityOrderPayRequest commodityOrderPayRequest);

    @POST("order/allOrder")
    Flowable<BaseResponse<List<RiderNewOrdersResponse>>> getOrderList(@Body AllOrderRequest allOrderRequest);

    @POST("order/longAllOrder")
    Flowable<BaseResponse<List<RiderNewOrdersResponse>>> getBookingOrderList(@Body AllOrderRequest allOrderRequest);

    @POST("order/newOrder")
    Flowable<BaseResponse<LoginRspDTO>> operateOrder(@Body NewOrderRequest newOrderRequest);

    @POST("order/confirmDelivery")
    Flowable<BaseResponse<LoginRspDTO>> confirmDelivery(@Body ConfirmDeliveryRequest confirmDeliveryRequest);

    @POST("account/list")
    Flowable<BaseResponse<BasePaging<CustomerListResponse>>> getMemberList(@Body CustomerListRequest customerListRequest);

    @POST("account/add")
    Flowable<BaseResponse<String>> addMember(@Body CustomerRequest customerRequest);

    @POST("account/accountInfo")
    Flowable<BaseResponse<CustomerListResponse>> getMemberInfo(@Body CustomerInfoRequest customerInfoRequest);

    @POST("account/accountList")
    Flowable<BaseResponse<BasePaging<CustomerBalanceRecordx>>> getMemberRecordList(@Body AccountRequest accountRequest);

    @POST("account/recharge")
    Flowable<BaseResponse<String>> memberRecharge(@Body RechargeRequest rechargeRequest);

    @POST("account/update")
    Flowable<BaseResponse<Integer>> updateMemberInfo(@Body CustomerxDTO customerxDTO);

    @POST("order/getOrderList")
    Flowable<BaseResponse<List<OrderListResponse>>> getCashierOrderList(@Body OrderListRequest orderListRequest);

    @POST("order/getOrderCount")
    Flowable<BaseResponse<OrderCountResponse>> getOrderCount();

    @POST("order/refundOrder")
    Flowable<BaseResponse<String>> refundOrder(@Body RefundOrderRequest refundOrderRequest);

    @POST("account/refundCard")
    Flowable<BaseResponse<String>> refundCard(@Body RefundCardRequest refundCardRequest);

    @POST("account/findCustomer")
    Flowable<BaseResponse<CustomerInfoResponse>> findCustomer(@Body CustomerRequest customerRequest);

    @POST("order/printTicket")
    Flowable<BaseResponse<PrintTicketResponse>> printTicket(@Body RefundOrderRequest refundOrderRequest);
}
