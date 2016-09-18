package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/8/7.
 */
public class MyPersonEntity {

    private String ServiceID ;
    private String ServiceName ;
    private String ServiceIntroduction ;
    private String ServiceLocation ;
    private String ServiceType ;
    private String ServiceLevel ;
    private String ConnectPerson ;
    private String ConnectPhone ;
    private String ServiceArea ;
    private String ViewCount ;
    private String Label ;
    private String UserID ;
    private String CoNumber ;
    private String UserPicture ;
    private String ServiceNumber ;
    private String CollectFlag ;

    private String CooperateFlag ;

    public MyPersonEntity(){}

    public MyPersonEntity(String serviceID, String serviceName, String serviceIntroduction, String serviceLocation, String serviceType, String serviceLevel, String connectPerson, String connectPhone, String serviceArea, String viewCount, String label, String userID, String coNumber, String userPicture, String serviceNumber, String collectFlag , String cooperateFlag) {
        ServiceID = serviceID;
        ServiceName = serviceName;
        ServiceIntroduction = serviceIntroduction;
        ServiceLocation = serviceLocation;
        ServiceType = serviceType;
        ServiceLevel = serviceLevel;
        ConnectPerson = connectPerson;
        ConnectPhone = connectPhone;
        ServiceArea = serviceArea;
        ViewCount = viewCount;
        Label = label;
        UserID = userID;
        CoNumber = coNumber;
        UserPicture = userPicture;
        ServiceNumber = serviceNumber;
        CollectFlag = collectFlag;
        CooperateFlag = cooperateFlag ;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceIntroduction() {
        return ServiceIntroduction;
    }

    public void setServiceIntroduction(String serviceIntroduction) {
        ServiceIntroduction = serviceIntroduction;
    }

    public String getCooperateFlag() {
        return CooperateFlag;
    }

    public void setCooperateFlag(String cooperateFlag) {
        CooperateFlag = cooperateFlag;
    }

    public String getServiceLocation() {
        return ServiceLocation;
    }

    public void setServiceLocation(String serviceLocation) {
        ServiceLocation = serviceLocation;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getServiceLevel() {
        return ServiceLevel;
    }

    public void setServiceLevel(String serviceLevel) {
        ServiceLevel = serviceLevel;
    }

    public String getConnectPerson() {
        return ConnectPerson;
    }

    public void setConnectPerson(String connectPerson) {
        ConnectPerson = connectPerson;
    }

    public String getConnectPhone() {
        return ConnectPhone;
    }

    public void setConnectPhone(String connectPhone) {
        ConnectPhone = connectPhone;
    }

    public String getServiceArea() {
        return ServiceArea;
    }

    public void setServiceArea(String serviceArea) {
        ServiceArea = serviceArea;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getCoNumber() {
        return CoNumber;
    }

    public void setCoNumber(String coNumber) {
        CoNumber = coNumber;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getServiceNumber() {
        return ServiceNumber;
    }

    public void setServiceNumber(String serviceNumber) {
        ServiceNumber = serviceNumber;
    }

    public String getCollectFlag() {
        return CollectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        CollectFlag = collectFlag;
    }
}
