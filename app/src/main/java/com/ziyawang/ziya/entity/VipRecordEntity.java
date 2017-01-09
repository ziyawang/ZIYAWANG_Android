package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/12/15.
 */
public class VipRecordEntity {

    private String StartTime ;
    private String EndTime ;
    private String OrderNumber ;
    private String MemberName ;
    private String PayMoney ;
    private String Operates ;

    public VipRecordEntity(){}

    public VipRecordEntity(String startTime, String endTime, String orderNumber, String memberName, String payMoney, String operates) {
        StartTime = startTime;
        EndTime = endTime;
        OrderNumber = orderNumber;
        MemberName = memberName;
        PayMoney = payMoney;
        Operates = operates;
    }

    public String getPayMoney() {
        return PayMoney;
    }

    public void setPayMoney(String payMoney) {
        PayMoney = payMoney;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getMemberName() {
        return MemberName;
    }

    public void setMemberName(String memberName) {
        MemberName = memberName;
    }

    public String getOperates() {
        return Operates;
    }

    public void setOperates(String operates) {
        Operates = operates;
    }
}
