package com.ecare.smartmeal.model.bean.rsp;

import java.math.BigDecimal;
import java.util.List;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/9/13.
 * Email: iminminxu@gmail.com
 */
public class OrderListResponse {

    private String orderno;
    private String mobile;
    private String name;
    private String creatTime;
    private List<OrderDetailsDTO> orderDetails;
    private BigDecimal amount;
    private String payType;
    private String idCard;
    private String liveAddr;

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public List<OrderDetailsDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getLiveAddr() {
        return liveAddr;
    }

    public void setLiveAddr(String liveAddr) {
        this.liveAddr = liveAddr;
    }

    public static class OrderDetailsDTO {
        private String name;
        private int number;
        private BigDecimal price;
        private BigDecimal singlePrice;
        private List<ItemListDTO> itemList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public void setPrice(BigDecimal price) {
            this.price = price;
        }

        public BigDecimal getSinglePrice() {
            return singlePrice;
        }

        public void setSinglePrice(BigDecimal singlePrice) {
            this.singlePrice = singlePrice;
        }

        public List<ItemListDTO> getItemList() {
            return itemList;
        }

        public void setItemList(List<ItemListDTO> itemList) {
            this.itemList = itemList;
        }

        public static class ItemListDTO {
            private String name;
            private int num;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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
