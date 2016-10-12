package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/9/25.
 */
public class RechargeTypeEntity {

    private String RealMoney ;
    private String YBCount ;
    private String selected ;

    public RechargeTypeEntity(){}

    public RechargeTypeEntity(String realMoney, String YBCount , String selected ) {
        RealMoney = realMoney ;
        this.YBCount = YBCount ;
        this.selected = selected ;
    }

    public String getRealMoney() {
        return RealMoney;
    }

    public void setRealMoney(String realMoney) {
        RealMoney = realMoney;
    }

    public String getYBCount() {
        return YBCount;
    }

    public void setYBCount(String YBCount) {
        this.YBCount = YBCount;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
