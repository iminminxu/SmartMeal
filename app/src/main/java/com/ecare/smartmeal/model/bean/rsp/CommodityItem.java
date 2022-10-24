package com.ecare.smartmeal.model.bean.rsp;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * SmartMeal
 * <p>
 * Created by xuminmin on 2022/5/23.
 * Email: iminminxu@gmail.com
 */
public class CommodityItem implements Parcelable {

    private int id;
    private String name;
    private int num;
    private BigDecimal packing;
    private BigDecimal price;
    private int singleType;
    private String spec;
    private String taboos;
    private int type;
    private int merchantId;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public BigDecimal getPacking() {
        return packing;
    }

    public void setPacking(BigDecimal packing) {
        this.packing = packing;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getSingleType() {
        return singleType;
    }

    public void setSingleType(int singleType) {
        this.singleType = singleType;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTaboos() {
        return taboos;
    }

    public void setTaboos(String taboos) {
        this.taboos = taboos;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.num);
        dest.writeSerializable(this.packing);
        dest.writeSerializable(this.price);
        dest.writeInt(this.singleType);
        dest.writeString(this.spec);
        dest.writeString(this.taboos);
        dest.writeInt(this.type);
        dest.writeInt(this.merchantId);
    }

    public void readFromParcel(Parcel source) {
        this.id = source.readInt();
        this.name = source.readString();
        this.num = source.readInt();
        this.packing = (BigDecimal) source.readSerializable();
        this.price = (BigDecimal) source.readSerializable();
        this.singleType = source.readInt();
        this.spec = source.readString();
        this.taboos = source.readString();
        this.type = source.readInt();
        this.merchantId = source.readInt();
    }

    public CommodityItem() {
    }

    protected CommodityItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.num = in.readInt();
        this.packing = (BigDecimal) in.readSerializable();
        this.price = (BigDecimal) in.readSerializable();
        this.singleType = in.readInt();
        this.spec = in.readString();
        this.taboos = in.readString();
        this.type = in.readInt();
        this.merchantId = in.readInt();
    }

    public static final Parcelable.Creator<CommodityItem> CREATOR = new Parcelable.Creator<CommodityItem>() {
        @Override
        public CommodityItem createFromParcel(Parcel source) {
            return new CommodityItem(source);
        }

        @Override
        public CommodityItem[] newArray(int size) {
            return new CommodityItem[size];
        }
    };
}
