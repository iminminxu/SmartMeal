package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/9/13.
 * Email: iminminxu@gmail.com
 */
public class OrderListRequest {

    private int pageNum;
    private int pageSize;
    private int source;
    private int status;

    public OrderListRequest(int pageNum, int pageSize, int source, int status) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.source = source;
        this.status = status;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
