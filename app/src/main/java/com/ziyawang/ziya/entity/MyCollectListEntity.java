package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/8/9.
 */
public class MyCollectListEntity {

    /**
     * 1、信息 2、视频 4、服务
     */
    private String TypeID ;
    //收藏时间
    private String CollectTime ;
    //ID
    private String ItemID ;

    //1、收藏信息
    private String TypeName ;
    private String ProArea ;
    private String WordDes ;
    private String PictureDes1 ;
    private String ProjectNumber ;

    //2、收藏视频
    private String VideoTitle ;
    private String ViewCount ;
    private String VideoDes ;
    private String VideoLogo ;

    //3、收藏服务
    private String ServiceName ;
    private String ServiceType ;
    private String ServiceArea ;
    private String UserPicture ;

    public MyCollectListEntity(){}

    public MyCollectListEntity(String typeID, String collectTime, String itemID, String typeName, String proArea, String wordDes, String pictureDes1, String projectNumber, String videoTitle, String viewCount, String videoDes, String videoLogo, String serviceName, String serviceType, String serviceArea, String userPicture) {
        TypeID = typeID;
        CollectTime = collectTime;
        ItemID = itemID;
        TypeName = typeName;
        ProArea = proArea;
        WordDes = wordDes;
        PictureDes1 = pictureDes1;
        ProjectNumber = projectNumber;
        VideoTitle = videoTitle;
        ViewCount = viewCount;
        VideoDes = videoDes;
        VideoLogo = videoLogo;
        ServiceName = serviceName;
        ServiceType = serviceType;
        ServiceArea = serviceArea;
        UserPicture = userPicture;
    }

    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getCollectTime() {
        return CollectTime;
    }

    public void setCollectTime(String collectTime) {
        CollectTime = collectTime;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getTypeName() {
        return TypeName;
    }

    public void setTypeName(String typeName) {
        TypeName = typeName;
    }

    public String getProArea() {
        return ProArea;
    }

    public void setProArea(String proArea) {
        ProArea = proArea;
    }

    public String getWordDes() {
        return WordDes;
    }

    public void setWordDes(String wordDes) {
        WordDes = wordDes;
    }

    public String getPictureDes1() {
        return PictureDes1;
    }

    public void setPictureDes1(String pictureDes1) {
        PictureDes1 = pictureDes1;
    }

    public String getProjectNumber() {
        return ProjectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        ProjectNumber = projectNumber;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    public String getVideoDes() {
        return VideoDes;
    }

    public void setVideoDes(String videoDes) {
        VideoDes = videoDes;
    }

    public String getVideoLogo() {
        return VideoLogo;
    }

    public void setVideoLogo(String videoLogo) {
        VideoLogo = videoLogo;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String serviceType) {
        ServiceType = serviceType;
    }

    public String getServiceArea() {
        return ServiceArea;
    }

    public void setServiceArea(String serviceArea) {
        ServiceArea = serviceArea;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }
}
