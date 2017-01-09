package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/12/30.
 */
public class StarListEntity {
    private String StarID ;
    private String StarName ;
    private String Price ;
    private String OPrice ;

    public StarListEntity(){}

    public StarListEntity(String starID, String starName, String price, String OPrice) {
        StarID = starID;
        StarName = starName;
        Price = price;
        this.OPrice = OPrice;
    }

    public String getStarID() {
        return StarID;
    }

    public void setStarID(String starID) {
        StarID = starID;
    }

    public String getStarName() {
        return StarName;
    }

    public void setStarName(String starName) {
        StarName = starName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getOPrice() {
        return OPrice;
    }

    public void setOPrice(String OPrice) {
        this.OPrice = OPrice;
    }
}
