package com.ecare.smartmeal.model.bean.rsp;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/6/15.
 * Email: iminminxu@gmail.com
 */
public class Event {

    public static class OrderListRefreshEvent {

        private int orderWay;

        public OrderListRefreshEvent(int orderWay) {
            this.orderWay = orderWay;
        }

        public int getOrderWay() {
            return orderWay;
        }

        public void setOrderWay(int orderWay) {
            this.orderWay = orderWay;
        }
    }

    public static class OrderListNewRefreshEvent {

        private int source;

        public OrderListNewRefreshEvent(int source) {
            this.source = source;
        }

        public int getSource() {
            return source;
        }

        public void setSource(int source) {
            this.source = source;
        }
    }

    public static class ModifyCardNoEvent {

        private String cardNo;

        public ModifyCardNoEvent(String cardNo) {
            this.cardNo = cardNo;
        }

        public String getCardNo() {
            return cardNo;
        }

        public void setCardNo(String cardNo) {
            this.cardNo = cardNo;
        }
    }
}
