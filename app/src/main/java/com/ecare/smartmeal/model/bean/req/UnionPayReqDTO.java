package com.ecare.smartmeal.model.bean.req;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/11/28.
 * Email: iminminxu@gmail.com
 */
public class UnionPayReqDTO {

    private String psaAmt;
    private String psaType;

    public UnionPayReqDTO(String psaAmt, String psaType) {
        this.psaAmt = psaAmt;
        this.psaType = psaType;
    }

    public String getPsaAmt() {
        return psaAmt;
    }

    public void setPsaAmt(String psaAmt) {
        this.psaAmt = psaAmt;
    }

    public String getPsaType() {
        return psaType;
    }

    public void setPsaType(String psaType) {
        this.psaType = psaType;
    }
}
