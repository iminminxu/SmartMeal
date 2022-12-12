package com.ecare.smartmeal.utils;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ecare.smartmeal.config.App;
import com.ecare.smartmeal.model.bean.rsp.CommodityItem;
import com.ecare.smartmeal.model.bean.rsp.CommodityOrderPayResponse;
import com.ecare.smartmeal.model.bean.rsp.CommodityxAllResponseItem;
import com.ecare.smartmeal.model.bean.rsp.CustomerListResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderCountResponse;
import com.ecare.smartmeal.model.bean.rsp.OrderListResponse;
import com.ecare.smartmeal.model.bean.rsp.PrintTicketResponse;
import com.ecare.smartmeal.model.bean.rsp.RiderNewOrdersResponse;
import com.ecare.smartmeal.ui.activity.MainActivity;
import com.ecare.smartmeal.ui.fragment.OrderListFragment;
import com.sunmi.externalprinterlibrary.api.PrinterException;
import com.sunmi.externalprinterlibrary.api.SunmiPrinterApi;

import java.math.BigDecimal;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/1.
 * Email: iminminxu@gmail.com
 */
public class PrintUtils {

    public static void printDineInOrder(List<CommodityxAllResponseItem> data, BigDecimal packing, BigDecimal totalDiscount, BigDecimal totalAmount, BigDecimal actualAmount, CommodityOrderPayResponse commodityOrderPayResponse) throws PrinterException {
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("收银台");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(App.getInstance().getMerchantInfo() == null ? "" : App.getInstance().getMerchantInfo().getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("* * * * *  堂食订单  * * * * *");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().printText("下单时间：" + TimeUtils.getNowString(TimeUtils.getSafeDateFormat("MM-dd HH:mm")));
        SunmiPrinterApi.getInstance().flush();
        if (commodityOrderPayResponse != null) {
            SunmiPrinterApi.getInstance().printText("会员姓名:" + commodityOrderPayResponse.getName());
            SunmiPrinterApi.getInstance().flush();
            SunmiPrinterApi.getInstance().printText("会员手机号:" + MainActivity.desensitizedPhoneNumber(commodityOrderPayResponse.getMobile()));
            SunmiPrinterApi.getInstance().flush();
            SunmiPrinterApi.getInstance().printText("会员卡卡号:" + commodityOrderPayResponse.getCardNo());
            SunmiPrinterApi.getInstance().flush();
        }
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printColumnsText(new String[]{"菜名", "数量", "小计"}, width, align);
        if (data != null && data.size() != 0) {
            for (CommodityxAllResponseItem item : data) {
                if (item == null) {
                    continue;
                }
                int num = item.getNum();
                BigDecimal price = item.getPrice().multiply(new BigDecimal(String.valueOf(num)));
                SunmiPrinterApi.getInstance().printColumnsText(new String[]{item.getName(), "x" + num, NumUtils.parsePrintAmount(price)}, width, align);
                List<CommodityItem> commodityComb = item.getCommodityComb();
                if (commodityComb == null || commodityComb.size() == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (CommodityItem commodityItem : commodityComb) {
                    if (commodityItem == null) {
                        continue;
                    }
                    builder.append(commodityItem.getName()).append(commodityItem.getNum()).append("；");
                }
                if (builder.length() != 0) {
                    SunmiPrinterApi.getInstance().printText("【" + builder.toString() + "】");
                    SunmiPrinterApi.getInstance().flush();
                }
            }
        }
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("打包费:" + NumUtils.parsePrintAmount(packing));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("优惠:" + NumUtils.parsePrintAmount(totalDiscount));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("合计:" + NumUtils.parsePrintAmount(totalAmount));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("应付                    " + NumUtils.parsePrintAmount(actualAmount) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        if (commodityOrderPayResponse != null) {
            SunmiPrinterApi.getInstance().printText("会员余额:" + NumUtils.parsePrintAmount(commodityOrderPayResponse.getBalance()));
            SunmiPrinterApi.getInstance().flush();
        }
        SunmiPrinterApi.getInstance().lineWrap(5);
    }

    public static void printPendingOrder(int orderWay, RiderNewOrdersResponse item) throws PrinterException {
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(item.getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("订单编号：" + item.getOrderno());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(orderWay == OrderListFragment.ORDER_WAY_SHOP ? item.getEatWay() == 1 ? "堂食" : "打包带走" : "外送");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("预约时间：" + item.getExpectTime());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("下单时间：" + item.getPayTime());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("备注：" + item.getRemark());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printColumnsText(new String[]{"名称", "数量", "价格"}, width, align);
        //打印菜品
        List<RiderNewOrdersResponse.OrderDetailsDTO> orderDetails = item.getOrderDetails();
        if (orderDetails != null && orderDetails.size() != 0) {
            for (RiderNewOrdersResponse.OrderDetailsDTO orderDetail : orderDetails) {
                if (orderDetail == null) {
                    continue;
                }
                SunmiPrinterApi.getInstance().printColumnsText(new String[]{orderDetail.getName(), String.valueOf(orderDetail.getNumber()), NumUtils.parsePrintAmount(orderDetail.getSinglePrice())}, width, align);
                List<RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO> specx = orderDetail.getSpecx();
                if (specx == null || specx.size() == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO specxDTO : specx) {
                    if (specxDTO == null) {
                        continue;
                    }
                    List<RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO.ListDTO> list = specxDTO.getList();
                    if (list == null || list.size() == 0) {
                        continue;
                    }
                    for (RiderNewOrdersResponse.OrderDetailsDTO.SpecxDTO.ListDTO listDTO : list) {
                        if (listDTO == null) {
                            continue;
                        }
                        builder.append(listDTO.getName()).append(listDTO.getNum()).append("；");
                    }
                }
                if (builder.length() != 0) {
                    SunmiPrinterApi.getInstance().printText("【" + builder.toString() + "】");
                    SunmiPrinterApi.getInstance().flush();
                }
            }
        }
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("打包费：" + NumUtils.parsePrintAmount(item.getTotalPacking()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("配送费：" + NumUtils.parsePrintAmount(item.getSendFee()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("优惠：" + NumUtils.parsePrintAmount(item.getTicketDiscountMoney()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("实付：￥" + NumUtils.parsePrintAmount(item.getAmount()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        if (orderWay == OrderListFragment.ORDER_WAY_SHOP) {
            SunmiPrinterApi.getInstance().printText("店铺地址：" + item.getMerchantAddress());
            SunmiPrinterApi.getInstance().flush();
            SunmiPrinterApi.getInstance().printText("联系电话：" + item.getMerchantTel());
            SunmiPrinterApi.getInstance().flush();
        } else {
            SunmiPrinterApi.getInstance().printText(item.getArea());
            SunmiPrinterApi.getInstance().flush();
            SunmiPrinterApi.getInstance().printText(item.getConsigneeName() + " " + item.getConsigneePhone());
            SunmiPrinterApi.getInstance().flush();
        }
        SunmiPrinterApi.getInstance().lineWrap(5);
    }

    public static void printPendingOrder(OrderListResponse item, PrintTicketResponse data) throws PrinterException {
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(data.getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("订单编号：" + item.getOrderno());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("外送");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("预约时间：");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("下单时间：" + item.getCreatTime());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("备注：");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printColumnsText(new String[]{"名称", "数量", "价格"}, width, align);
        //打印菜品
        List<OrderListResponse.OrderDetailsDTO> orderDetails = item.getOrderDetails();
        if (orderDetails != null && orderDetails.size() != 0) {
            for (OrderListResponse.OrderDetailsDTO orderDetailsDTO : orderDetails) {
                if (orderDetailsDTO == null) {
                    continue;
                }
                int num = orderDetailsDTO.getNumber();
                SunmiPrinterApi.getInstance().printColumnsText(new String[]{orderDetailsDTO.getName(), "x" + num, NumUtils.parsePrintAmount(orderDetailsDTO.getSinglePrice())}, width, align);
                List<OrderListResponse.OrderDetailsDTO.ItemListDTO> itemList = orderDetailsDTO.getItemList();
                if (itemList == null || itemList.size() == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (OrderListResponse.OrderDetailsDTO.ItemListDTO itemListDTO : itemList) {
                    if (itemListDTO == null) {
                        continue;
                    }
                    builder.append(itemListDTO.getName()).append(itemListDTO.getNum()).append("；");
                }
                if (builder.length() != 0) {
                    SunmiPrinterApi.getInstance().printText("【" + builder.toString() + "】");
                    SunmiPrinterApi.getInstance().flush();
                }
            }
        }
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("打包费：" + NumUtils.parsePrintAmount(data.getPacking()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("配送费：0.00");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("优惠：" + NumUtils.parsePrintAmount(data.getTicketDiscountMoney()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("实付：￥" + NumUtils.parsePrintAmount(item.getAmount()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText(item.getLiveAddr());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText(item.getName() + " " + item.getMobile());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().lineWrap(5);
    }

    public static void printMemberRechargeOrder(CustomerListResponse memberInfo, BigDecimal rechargeAmount, BigDecimal paymentAmount) throws PrinterException {
        if (memberInfo == null) {
            ToastUtils.showShort("会员信息加载失败，无法打印小票！");
            return;
        }
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("收银台");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(App.getInstance().getMerchantInfo() == null ? "" : App.getInstance().getMerchantInfo().getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("会员卡充值");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().printText("充值时间：" + TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm")));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("卡号:" + memberInfo.getCardNo());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("姓名:" + memberInfo.getName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("联系电话:" + MainActivity.desensitizedPhoneNumber(memberInfo.getMobile()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("原有金额:" + NumUtils.parsePrintAmount(memberInfo.getBalance()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("充值金额:" + NumUtils.parsePrintAmount(rechargeAmount));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("实付金额:" + NumUtils.parsePrintAmount(paymentAmount));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("应付                    " + NumUtils.parsePrintAmount(paymentAmount) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("会员余额:" + NumUtils.parsePrintAmount(memberInfo.getBalance().add(rechargeAmount)) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().lineWrap(5);
    }

    public static void printMemberRechargeOrder(CustomerListResponse memberInfo, BigDecimal rechargeAmount, BigDecimal paymentAmount, BigDecimal balance, String createTime) throws PrinterException {
        if (memberInfo == null) {
            ToastUtils.showShort("会员信息加载失败，无法打印小票！");
            return;
        }
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("收银台");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(App.getInstance().getMerchantInfo() == null ? "" : App.getInstance().getMerchantInfo().getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("会员卡充值");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().printText("充值时间：" + createTime);
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("卡号:" + memberInfo.getCardNo());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("姓名:" + memberInfo.getName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("联系电话:" + MainActivity.desensitizedPhoneNumber(memberInfo.getMobile()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("原有金额:" + NumUtils.parsePrintAmount(balance.subtract(rechargeAmount)));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("充值金额:" + NumUtils.parsePrintAmount(rechargeAmount));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("实付金额:" + NumUtils.parsePrintAmount(paymentAmount));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("应付                    " + NumUtils.parsePrintAmount(paymentAmount) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("会员余额:" + NumUtils.parsePrintAmount(balance) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().lineWrap(5);
    }

    public static void printDailyOrderStatistics(OrderCountResponse orderCountResponse) throws PrinterException {
        if (orderCountResponse == null) {
            ToastUtils.showShort("日订单统计数据加载失败，无法打印小票！");
            return;
        }
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("收银台");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(App.getInstance().getMerchantInfo() == null ? "" : App.getInstance().getMerchantInfo().getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("日订单统计");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().printText("统计日期：" + TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy-MM-dd")));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("总收款金额:" + NumUtils.parsePrintAmount(orderCountResponse.getTotalOrderPrice()) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("总交易笔数:" + orderCountResponse.getTotalOrderNum() + "单");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("新增会员数:" + orderCountResponse.getCustomerNum() + "个");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("签字区");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("小票打印时间:" + TimeUtils.getNowString(TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm")));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().lineWrap(5);
    }

    public static void printDineInOrder(OrderListResponse item, PrintTicketResponse data) throws PrinterException {
        if (item == null || data == null) {
            ToastUtils.showShort("订单信息加载失败，无法打印小票！");
            return;
        }
        int[] width = new int[]{11, 10, 11};
        int[] align = new int[]{0, 1, 2};
        SunmiPrinterApi.getInstance().printerInit();
        SunmiPrinterApi.getInstance().setAlignMode(1);
        SunmiPrinterApi.getInstance().setFontZoom(2, 2);
        SunmiPrinterApi.getInstance().printText("收银台");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText(App.getInstance().getMerchantInfo() == null ? "" : App.getInstance().getMerchantInfo().getMerchantName());
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("* * * * *  堂食订单  * * * * *");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setAlignMode(0);
        SunmiPrinterApi.getInstance().printText("下单时间：" + data.getCreateTime());
        SunmiPrinterApi.getInstance().flush();
        if (StringUtils.equals("1", item.getPayType())) {
            SunmiPrinterApi.getInstance().printText("会员姓名:" + data.getName());
            SunmiPrinterApi.getInstance().flush();
            SunmiPrinterApi.getInstance().printText("会员手机号:" + MainActivity.desensitizedPhoneNumber(data.getMobile()));
            SunmiPrinterApi.getInstance().flush();
            SunmiPrinterApi.getInstance().printText("会员卡卡号:" + data.getCardNo());
            SunmiPrinterApi.getInstance().flush();
        }
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printColumnsText(new String[]{"菜名", "数量", "小计"}, width, align);
        List<OrderListResponse.OrderDetailsDTO> orderDetails = item.getOrderDetails();
        if (orderDetails != null && orderDetails.size() != 0) {
            for (OrderListResponse.OrderDetailsDTO orderDetailsDTO : orderDetails) {
                if (orderDetailsDTO == null) {
                    continue;
                }
                int num = orderDetailsDTO.getNumber();
                BigDecimal price = orderDetailsDTO.getPrice().multiply(new BigDecimal(String.valueOf(num)));
                SunmiPrinterApi.getInstance().printColumnsText(new String[]{orderDetailsDTO.getName(), "x" + num, NumUtils.parsePrintAmount(price)}, width, align);
                List<OrderListResponse.OrderDetailsDTO.ItemListDTO> itemList = orderDetailsDTO.getItemList();
                if (itemList == null || itemList.size() == 0) {
                    continue;
                }
                StringBuilder builder = new StringBuilder();
                for (OrderListResponse.OrderDetailsDTO.ItemListDTO itemListDTO : itemList) {
                    if (itemListDTO == null) {
                        continue;
                    }
                    builder.append(itemListDTO.getName()).append(itemListDTO.getNum()).append("；");
                }
                if (builder.length() != 0) {
                    SunmiPrinterApi.getInstance().printText("【" + builder.toString() + "】");
                    SunmiPrinterApi.getInstance().flush();
                }
            }
        }
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("打包费:" + NumUtils.parsePrintAmount(data.getPacking()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("优惠:" + NumUtils.parsePrintAmount(data.getTicketDiscountMoney()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("合计:" + NumUtils.parsePrintAmount(data.getTotalFee()));
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 2);
        SunmiPrinterApi.getInstance().printText("应付                    " + NumUtils.parsePrintAmount(data.getAmount()) + "元");
        SunmiPrinterApi.getInstance().flush();
        SunmiPrinterApi.getInstance().setFontZoom(1, 1);
        SunmiPrinterApi.getInstance().printText("--------------------------------");
        SunmiPrinterApi.getInstance().flush();
        if (StringUtils.equals("1", item.getPayType())) {
            SunmiPrinterApi.getInstance().printText("会员余额:" + NumUtils.parsePrintAmount(data.getBalance()));
            SunmiPrinterApi.getInstance().flush();
        }
        SunmiPrinterApi.getInstance().lineWrap(5);
    }
}
