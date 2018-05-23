package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2018/5/18.
 */

public class V3BannerEntity {

    private String Id ;
    private String BannerLink ;
    private String created_at ;

    public V3BannerEntity(){}

    public V3BannerEntity(String id, String bannerLink, String created_at) {
        super();
        Id = id;
        BannerLink = bannerLink;
        this.created_at = created_at;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getBannerLink() {
        return BannerLink;
    }

    public void setBannerLink(String bannerLink) {
        BannerLink = bannerLink;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
