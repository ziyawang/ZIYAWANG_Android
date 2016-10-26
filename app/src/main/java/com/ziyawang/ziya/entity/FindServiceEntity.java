package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/7/25.
 */
public class FindServiceEntity {

    private String ServiceID ;
    private String ServiceName ;
    private String ServiceLocation ;
    private String ServiceType ;
    private String ServiceLevel ;
    private String ConnectPerson ;

    public FindServiceEntity(){}

    public FindServiceEntity(String serviceID, String serviceName, String serviceLocation, String serviceType, String serviceLevel , String connectPerson) {
        ServiceID = serviceID;
        ServiceName = serviceName;
        ServiceLocation = serviceLocation;
        ServiceType = serviceType;
        ServiceLevel = serviceLevel;
        ConnectPerson = connectPerson ;
    }

    public String getConnectPerson() {
        return ConnectPerson;
    }

    public void setConnectPerson(String connectPerson) {
        ConnectPerson = connectPerson;
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
}
