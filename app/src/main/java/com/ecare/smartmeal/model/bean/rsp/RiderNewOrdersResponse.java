package com.ecare.smartmeal.model.bean.rsp;

import java.math.BigDecimal;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/6.
 * Email: iminminxu@gmail.com
 */
public class RiderNewOrdersResponse {

    private String orderno;
    private String expectTime;
    private String consigneeName;
    private String area;
    private String consigneePhone;
    private List<OrderDetailsDTO> orderDetails;
    private String remark;
    private String income;
    private String customerIdCard;
    private BigDecimal amount;
    private BigDecimal ticketDiscountMoney;
    private BigDecimal totalPacking;
    private String merchantName;
    private String merchantAddress;
    private String merchantTel;
    private String payTime;
    private BigDecimal sendFee;
    //到店取餐字段
    private int eatWay;
    private int id;
    //长期预订字段
    private String sonOrderNo;
    private int cycle;
    private String startTime;
    private String commodityName;
    private String timeNodeList;
    private int mealsType;

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public List<OrderDetailsDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getCustomerIdCard() {
        return customerIdCard;
    }

    public void setCustomerIdCard(String customerIdCard) {
        this.customerIdCard = customerIdCard;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getTicketDiscountMoney() {
        return ticketDiscountMoney;
    }

    public void setTicketDiscountMoney(BigDecimal ticketDiscountMoney) {
        this.ticketDiscountMoney = ticketDiscountMoney;
    }

    public BigDecimal getTotalPacking() {
        return totalPacking;
    }

    public void setTotalPacking(BigDecimal totalPacking) {
        this.totalPacking = totalPacking;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantTel() {
        return merchantTel;
    }

    public void setMerchantTel(String merchantTel) {
        this.merchantTel = merchantTel;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getSendFee() {
        return sendFee;
    }

    public void setSendFee(BigDecimal sendFee) {
        this.sendFee = sendFee;
    }

    public int getEatWay() {
        return eatWay;
    }

    public void setEatWay(int eatWay) {
        this.eatWay = eatWay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSonOrderNo() {
        return sonOrderNo;
    }

    public void setSonOrderNo(String sonOrderNo) {
        this.sonOrderNo = sonOrderNo;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getCommodityName() {
        return commodityName;
    }

    public void setCommodityName(String commodityName) {
        this.commodityName = commodityName;
    }

    public String getTimeNodeList() {
        return timeNodeList;
    }

    public void setTimeNodeList(String timeNodeList) {
        this.timeNodeList = timeNodeList;
    }

    public int getMealsType() {
        return mealsType;
    }

    public void setMealsType(int mealsType) {
        this.mealsType = mealsType;
    }

    public static class OrderDetailsDTO {

        private int id;
        private String name;
        private BigDecimal price;
        private int number;
        private BigDecimal singlePrice;
        private String remark;
        private String spec;
        private List<SpecxDTO> specx;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public BigDecimal getSinglePrice() {
            return singlePrice;
        }

        public void setSinglePrice(BigDecimal singlePrice) {
            this.singlePrice = singlePrice;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public List<SpecxDTO> getSpecx() {
            return specx;
        }

        public void setSpecx(List<SpecxDTO> specx) {
            this.specx = specx;
        }

        public static class SpecxDTO {

            private String singleName;
            private int singleType;
            private List<ListDTO> list;

            public String getSingleName() {
                return singleName;
            }

            public void setSingleName(String singleName) {
                this.singleName = singleName;
            }

            public int getSingleType() {
                return singleType;
            }

            public void setSingleType(int singleType) {
                this.singleType = singleType;
            }

            public List<ListDTO> getList() {
                return list;
            }

            public void setList(List<ListDTO> list) {
                this.list = list;
            }

            public static class ListDTO {

                private String name;
                private int id;
                private int num;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public int getNum() {
                    return num;
                }

                public void setNum(int num) {
                    this.num = num;
                }
            }
        }
    }
}
