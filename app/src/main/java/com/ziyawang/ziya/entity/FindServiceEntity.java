package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/7/25.
 */
public class FindServiceEntity {
    private String ServiceID ;
    private String ServiceName ;
    private String ServiceIntroduction ;
    private String ServiceLocation ;
    private String ServiceType ;
    private String ServiceLevel ;
    private String ConnectPerson ;
    private String ConnectPhone ;
    private String ServiceArea ;
    private String ConfirmationP1 ;
    private String ConfirmationP2 ;
    private String ConfirmationP3 ;
    private String CollectionCount ;
    private String ViewCount ;
    private String Label ;
    private String created_at ;
    private String updated_at ;
    private String UserID ;
    private String CheckCount ;
    private String Size ;
    private String Founds ;
    private String RegTime ;
    private String Level ;
    private String CollectCount ;
    private String CollectFlag ;
    private String right ;
    private String showright ;
    private String showrightios ;
    private String showrightarr ;
    private String CoNumber ;
    private String UserPicture ;
    private String ServiceNumber ;

    public FindServiceEntity(){}

    public FindServiceEntity(String serviceID, String serviceName, String serviceIntroduction, String serviceLocation, String serviceType, String serviceLevel, String connectPerson, String connectPhone, String serviceArea, String confirmationP1, String confirmationP2, String confirmationP3, String collectionCount, String viewCount, String label, String created_at, String updated_at, String userID, String checkCount, String size, String founds, String regTime, String level, String collectCount, String collectFlag, String right, String showright, String showrightios, String showrightarr, String coNumber, String userPicture, String serviceNumber) {
        ServiceID = serviceID;
        ServiceName = serviceName;
        ServiceIntroduction = serviceIntroduction;
        ServiceLocation = serviceLocation;
        ServiceType = serviceType;
        ServiceLevel = serviceLevel;
        ConnectPerson = connectPerson;
        ConnectPhone = connectPhone;
        ServiceArea = serviceArea;
        ConfirmationP1 = confirmationP1;
        ConfirmationP2 = confirmationP2;
        ConfirmationP3 = confirmationP3;
        CollectionCount = collectionCount;
        ViewCount = viewCount;
        Label = label;
        this.created_at = created_at;
        this.updated_at = updated_at;
        UserID = userID;
        CheckCount = checkCount;
        Size = size;
        Founds = founds;
        RegTime = regTime;
        Level = level;
        CollectCount = collectCount;
        CollectFlag = collectFlag;
        this.right = right;
        this.showright = showright;
        this.showrightios = showrightios;
        this.showrightarr = showrightarr;
        CoNumber = coNumber;
        UserPicture = userPicture;
        ServiceNumber = serviceNumber;
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

    public String getConfirmationP1() {
        return ConfirmationP1;
    }

    public void setConfirmationP1(String confirmationP1) {
        ConfirmationP1 = confirmationP1;
    }

    public String getConfirmationP2() {
        return ConfirmationP2;
    }

    public void setConfirmationP2(String confirmationP2) {
        ConfirmationP2 = confirmationP2;
    }

    public String getConfirmationP3() {
        return ConfirmationP3;
    }

    public void setConfirmationP3(String confirmationP3) {
        ConfirmationP3 = confirmationP3;
    }

    public String getCollectionCount() {
        return CollectionCount;
    }

    public void setCollectionCount(String collectionCount) {
        CollectionCount = collectionCount;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getCheckCount() {
        return CheckCount;
    }

    public void setCheckCount(String checkCount) {
        CheckCount = checkCount;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getFounds() {
        return Founds;
    }

    public void setFounds(String founds) {
        Founds = founds;
    }

    public String getRegTime() {
        return RegTime;
    }

    public void setRegTime(String regTime) {
        RegTime = regTime;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String level) {
        Level = level;
    }

    public String getCollectCount() {
        return CollectCount;
    }

    public void setCollectCount(String collectCount) {
        CollectCount = collectCount;
    }

    public String getCollectFlag() {
        return CollectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        CollectFlag = collectFlag;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getShowright() {
        return showright;
    }

    public void setShowright(String showright) {
        this.showright = showright;
    }

    public String getShowrightios() {
        return showrightios;
    }

    public void setShowrightios(String showrightios) {
        this.showrightios = showrightios;
    }

    public String getShowrightarr() {
        return showrightarr;
    }

    public void setShowrightarr(String showrightarr) {
        this.showrightarr = showrightarr;
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
}
