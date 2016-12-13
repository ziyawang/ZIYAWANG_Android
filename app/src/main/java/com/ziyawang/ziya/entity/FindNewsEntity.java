package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/10/31.
 */
public class FindNewsEntity {

    private String NewsID ;
    private String NewsTitle ;
    private String NewsContent ;
    private String NewsLogo ;
    private String NewsThumb ;
    private String NewsLabel ;
    private String PublishTime ;
    private String NewsAuthor ;
    private String ViewCount ;
    private String CollectionCount ;
    private String Brief ;

    public FindNewsEntity(){}

    public FindNewsEntity(String newsID, String newsTitle, String newsContent, String newsLogo, String newsThumb, String newsLabel, String publishTime, String newsAuthor, String viewCount, String collectionCount, String brief) {
        super();
        NewsID = newsID;
        NewsTitle = newsTitle;
        NewsContent = newsContent;
        NewsLogo = newsLogo;
        NewsThumb = newsThumb;
        NewsLabel = newsLabel;
        PublishTime = publishTime;
        NewsAuthor = newsAuthor;
        ViewCount = viewCount;
        CollectionCount = collectionCount;
        Brief = brief;
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

    public String getNewsContent() {
        return NewsContent;
    }

    public void setNewsContent(String newsContent) {
        NewsContent = newsContent;
    }

    public String getNewsLogo() {
        return NewsLogo;
    }

    public void setNewsLogo(String newsLogo) {
        NewsLogo = newsLogo;
    }

    public String getNewsThumb() {
        return NewsThumb;
    }

    public void setNewsThumb(String newsThumb) {
        NewsThumb = newsThumb;
    }

    public String getNewsLabel() {
        return NewsLabel;
    }

    public void setNewsLabel(String newsLabel) {
        NewsLabel = newsLabel;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
    }

    public String getNewsAuthor() {
        return NewsAuthor;
    }

    public void setNewsAuthor(String newsAuthor) {
        NewsAuthor = newsAuthor;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    public String getCollectionCount() {
        return CollectionCount;
    }

    public void setCollectionCount(String collectionCount) {
        CollectionCount = collectionCount;
    }

    public String getBrief() {
        return Brief;
    }

    public void setBrief(String brief) {
        Brief = brief;
    }
}
