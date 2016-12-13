package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/8/13.
 */
public class BannerEntity {

    private String BannerID ;
    private String BannerLink ;
    private String TypeID ;
    private String DetailID ;
    private String type ;
    private String Url ;
    private String Title ;

    public BannerEntity(){}

    public BannerEntity(String bannerID, String bannerLink, String typeID, String detailID, String type, String url, String title) {
        BannerID = bannerID;
        BannerLink = bannerLink;
        TypeID = typeID;
        DetailID = detailID;
        this.type = type;
        Url = url;
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getBannerID() {
        return BannerID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setBannerID(String bannerID) {
        BannerID = bannerID;
    }

    public String getBannerLink() {
        return BannerLink;
    }

    public void setBannerLink(String bannerLink) {
        BannerLink = bannerLink;
    }

    public String getTypeID() {
        return TypeID;
    }

    public void setTypeID(String typeID) {
        TypeID = typeID;
    }

    public String getDetailID() {
        return DetailID;
    }

    public void setDetailID(String detailID) {
        DetailID = detailID;
    }
}
