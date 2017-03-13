package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/9/24.
 */
public class GoldDetailsEntity {
    //1是充值，2是消费
    private String created_at ;
    private String Money;
    private String Type ;
    private String OrderNumber ;
    private String Operates ;
    private String VideoID ;

    public GoldDetailsEntity(){}

    public GoldDetailsEntity(String created_at, String money, String type , String OrderNumber , String Operates  ,String videoID ) {
        this.created_at = created_at;
        this.Money = money;
        this.Type = type;
        this.OrderNumber = OrderNumber ;
        this.Operates = Operates ;
        VideoID = videoID ;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getOperates() {
        return Operates;
    }

    public void setOperates(String operates) {
        Operates = operates;
    }
}
