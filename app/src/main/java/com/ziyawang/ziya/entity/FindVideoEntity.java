package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/8/4.
 */
public class FindVideoEntity {

    private String VideoID ;
    private String VideoTitle ;
    private String VideoDes ;
    private String VideoLogo ;
    private String VideoLabel ;
    private String VideoAuthor ;
    private String PublishTime ;
    private String ViewCount ;
    private String CollectionCount ;
    private String VideoLink ;
    private String VideoLink2 ;
    private String CollectFlag ;

    public FindVideoEntity(){}

    public FindVideoEntity(String videoID, String videoTitle, String videoDes, String videoLogo, String videoLabel, String videoAuthor, String publishTime, String viewCount, String collectionCount, String videoLink, String videoLink2 , String collectFlag) {
        VideoID = videoID;
        VideoTitle = videoTitle;
        VideoDes = videoDes;
        VideoLogo = videoLogo;
        VideoLabel = videoLabel;
        VideoAuthor = videoAuthor;
        PublishTime = publishTime;
        ViewCount = viewCount;
        CollectionCount = collectionCount;
        VideoLink = videoLink;
        VideoLink2 = videoLink2;
        CollectFlag = collectFlag ;

    }

    public String getCollectFlag() {
        return CollectFlag;
    }

    public void setCollectFlag(String collectFlag) {
        CollectFlag = collectFlag;
    }

    public String getVideoID() {
        return VideoID;
    }

    public void setVideoID(String videoID) {
        VideoID = videoID;
    }

    public String getVideoTitle() {
        return VideoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        VideoTitle = videoTitle;
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

    public String getVideoLabel() {
        return VideoLabel;
    }

    public void setVideoLabel(String videoLabel) {
        VideoLabel = videoLabel;
    }

    public String getVideoAuthor() {
        return VideoAuthor;
    }

    public void setVideoAuthor(String videoAuthor) {
        VideoAuthor = videoAuthor;
    }

    public String getPublishTime() {
        return PublishTime;
    }

    public void setPublishTime(String publishTime) {
        PublishTime = publishTime;
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

    public String getVideoLink() {
        return VideoLink;
    }

    public void setVideoLink(String videoLink) {
        VideoLink = videoLink;
    }

    public String getVideoLink2() {
        return VideoLink2;
    }

    public void setVideoLink2(String videoLink2) {
        VideoLink2 = videoLink2;
    }
}
