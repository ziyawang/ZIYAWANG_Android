package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2018/5/18.
 */

public class NewsEntity {

    private String NewsID ;
    private String NewsTitle ;
    private String NewsLogo ;
    private String Target ;
    private String PublishTime ;

    public NewsEntity(){}

    public NewsEntity(String newsID, String newsTitle, String newsLogo, String target, String publishTime) {
        super();
        NewsID = newsID;
        NewsTitle = newsTitle;
        NewsLogo = newsLogo;
        Target = target;
        PublishTime = publishTime;
    }

    public String getNewsID() {
        return NewsID;
    }

    public void setNewsID(String newsID) {
        NewsID = newsID;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNewsLogo() {
        return NewsLogo;
    }

    public void setNewsLogo(String newsLogo) {
        NewsLogo = newsLogo;
    }

    public String getTarget() {
        return Target;
    }

    public void setTarget(String target) {
        Target = target;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }

}
