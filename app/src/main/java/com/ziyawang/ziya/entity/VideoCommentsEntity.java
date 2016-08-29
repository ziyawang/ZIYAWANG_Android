package com.ziyawang.ziya.entity;

/**
 * Created by 牛海丰 on 2016/8/12.
 */
public class VideoCommentsEntity {

    private String UserName ;
    private String UserPicture ;
    private String Content ;
    private String PubTime ;

    public VideoCommentsEntity(){}

    public VideoCommentsEntity(String userName, String userPicture, String content, String pubTime) {
        UserName = userName;
        UserPicture = userPicture;
        Content = content;
        PubTime = pubTime;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPicture() {
        return UserPicture;
    }

    public void setUserPicture(String userPicture) {
        UserPicture = userPicture;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getPubTime() {
        return PubTime;
    }

    public void setPubTime(String pubTime) {
        PubTime = pubTime;
    }
}
